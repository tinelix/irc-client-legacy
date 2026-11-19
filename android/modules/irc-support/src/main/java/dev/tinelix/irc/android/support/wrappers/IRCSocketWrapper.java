package dev.tinelix.irc.android.support.wrappers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dev.tinelix.irc.android.support.enitites.ChatMessage;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;
import dev.tinelix.irc.android.support.listeners.SocketConnectionListener;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.OUT_CMD_PING;

public class IRCSocketWrapper {

    public static final String TAG = "IRC-SW";

    public static final int STATUS_NOT_INITIALIZED          = 0;
    public static final int STATUS_INITIALIZED              = 1;
    public static final int STATUS_CONNECTING               = 2;
    public static final int STATUS_AUTHORIZED               = 3;
    public static final int STATUS_CONNECTED                = 4;
    public static final int STATUS_RUNNING                  = 5;

    public static final int STATUS_DISCONNECTED             = -1;
    public static final int STATUS_SERVER_ERROR             = -2;
    public static final int STATUS_CONNECTION_TIMEOUT       = -3;
    public static final int STATUS_SERVER_NOT_FOUND         = -4;
    public static final int STATUS_NO_INTERNET_CONNECTION   = -5;
    public static final int STATUS_INTERAL_ERROR            = -6;

    private ServerProfile mProfile;
    private boolean mTrustAllTlsCerts = false;
    private Socket mSocket;
    private int mConnStatus;
    private String mIncompleteData;
    private long mIncompleteDataSize;

    private FileWriter mFileWriter;
    private BufferedInputStream mReader;
    private BufferedOutputStream mWriter;
    private SocketConnectionListener mListener;

    private String mLogFilePath;
    private String mCurrentNick;
    private int mCurrentNickIndex = 0;
    private ArrayList<ServerProfile.Nickname> mNicknames;

    private long mConnStartTime;
    private long mPingStartTime;
    private long mPingEstimated;
    private long mPrevPingEstimated;

    private Timer mPingBouncer;

    boolean mNotResponding = true;

    boolean mNicknameLock;

    public IRCSocketWrapper(Context context, ServerProfile profile) {
        mProfile = profile;

        try {
            File directory = new File(context.getFilesDir().getPath(), "raw_sockets");

            if(!directory.exists())
                directory.mkdir();

            mLogFilePath = String.format(
                    "%s/%s_%s.log",
                    directory.getPath(),
                    mProfile.name,
                    System.currentTimeMillis()
            );

            mFileWriter = new FileWriter(mLogFilePath);
        } catch (Exception ignored) {

        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void connect(boolean trustAllTlsCerts) {

        IRCServer server = mProfile.getServerParams();

        Log.d(TAG,
                String.format(
                    "[%s:%s] Connecting...\r\nProfile: %s",
                    server.address, server.port,
                    mProfile.name
                )
        );

        if(server.tlsEnabled) {
            TrustManager[] allCertsTrustManagers = new TrustManager[] {

            };
            if (trustAllTlsCerts) {
                allCertsTrustManagers = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String s) {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String s) {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                };
            }

            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                if(trustAllTlsCerts)
                    sslContext.init(null, allCertsTrustManagers, new SecureRandom());
                else
                    sslContext.init(null, null, null);

                mConnStatus = STATUS_CONNECTING;

                SSLSocketFactory factory = sslContext.getSocketFactory();
                InetAddress serverAddr = InetAddress.getByName(server.address);
                mSocket = factory.createSocket(serverAddr, server.port);

                if(mSocket.isConnected()) {
                    mConnStatus = STATUS_CONNECTED;

                    Log.d(TAG, String.format(
                            server.tlsEnabled ?
                                "[%s:%s] Connected using TLS\r\nProfile: %s" :
                                "[%s:%s] Connected\r\nProfile: %s",
                            server.address, server.port,
                            mProfile.name
                        )
                    );

                    mConnStartTime = System.currentTimeMillis();
                    createSocketBuffer();
                    mListener.onConnect(this, mProfile);
                }
            } catch (UnknownHostException e) {
                mConnStatus = STATUS_SERVER_NOT_FOUND;
                mListener.onConnectionFailed(mConnStatus, mProfile);
            } catch (ConnectException e) {
                mConnStatus = STATUS_CONNECTION_TIMEOUT;
                mListener.onConnectionFailed(mConnStatus, mProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                InetAddress serverAddr = InetAddress.getByName(server.address);
                mSocket = new Socket(serverAddr, server.port);

                if(mSocket.isConnected()) {
                    mConnStartTime = System.currentTimeMillis();
                    mConnStatus = STATUS_CONNECTED;
                    createSocketBuffer();
                    mListener.onConnect(this, mProfile);
                }
            } catch (UnknownHostException e) {
                mConnStatus = STATUS_SERVER_NOT_FOUND;
                mListener.onConnectionFailed(mConnStatus, mProfile);
            } catch (ConnectException e) {
                mConnStatus = STATUS_CONNECTION_TIMEOUT;
                mListener.onConnectionFailed(mConnStatus, mProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createSocketBuffer() {
        try {
            mReader = new BufferedInputStream(mSocket.getInputStream());
            mWriter = new BufferedOutputStream(mSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnectionListener(SocketConnectionListener listener) {
        mListener = listener;
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public void readRawData() {
        if(mSocket == null)
            return;

        byte[] buffer = new byte[4096];

        try {
            while(mSocket.isConnected()) {

                float timeDiff = System.currentTimeMillis() - mConnStartTime;

                if (mReader != null) {
                    int ret = mReader.read(buffer, 0, 4096);
                    int packetSize = measurePacketLength(buffer, ret);

                    long receivedTime = System.currentTimeMillis();
                    mNotResponding = (ret == 0 && timeDiff >= 60000) ||
                                     (receivedTime - mPingStartTime >= 10000 && mPingEstimated == 0);

                    if(mListener != null && ret > 0) {

                        String receivedData = new String(
                                buffer,
                                0, packetSize,
                                IRCServer.getCurrentEncodingStr(
                                    mProfile.getServerParams().outputEncoding
                                )
                        );

                        String gluedData = mIncompleteData != null ?
                                mIncompleteData + receivedData : receivedData;

                        if(mFileWriter != null) {
                            String timestamp = String.format("%s ", String.valueOf(receivedTime));

                            for (String line : gluedData.split("\r\n")) {
                                if(line.length() == 0) continue;

                                String[] words = line.split(" ");

                                if(words.length >= 2) {
                                    if(words[0].equals("PING") || words[1].equals("PING") ||
                                       words[1].equals("001"))
                                        mConnStatus = STATUS_RUNNING;
                                    else if(words[1].equals("PONG"))
                                        mPingEstimated = Math.abs(receivedTime - mPingStartTime);
                                    else if(words[1].equals("NICK") && words.length >= 3)
                                        mCurrentNick = words[2];
                                }

                                mFileWriter.write(timestamp + line + "\r\n");
                                mFileWriter.flush();
                            }
                        }

                        if(packetSize < ret) {
                            mIncompleteDataSize = ret - packetSize;

                            mIncompleteData = new String(
                                    buffer, packetSize, (int) mIncompleteDataSize,
                                    IRCServer.getCurrentEncodingStr(mProfile.getServerParams().outputEncoding)
                            );

                            if(mIncompleteData.endsWith("\r"))
                                mIncompleteData = String.format("%s\n", mIncompleteData);

                        } else {
                            mIncompleteData = null;
                            mIncompleteDataSize = 0;
                        }

                        mListener.onReceive(
                                this, mProfile, gluedData,
                                gluedData.length(), receivedTime
                        );
                    } else if(mNotResponding) {

                        IRCServer server = mProfile.getServerParams();

                        Log.e(TAG,
                              String.format(
                                    "[%s:%s] The server has been unresponsive for more than 60 seconds",
                                    server.address, server.port
                              )
                        );
                        break;
                    } else if(ret < 0) {
                        break;
                    }
                }
            }

            mFileWriter.close();
            mFileWriter = null;

            File file = new File(mLogFilePath);
            if(file.exists() && file.isFile())
                file.delete();

            mConnStatus = STATUS_DISCONNECTED;

            mPingStartTime = 0;
            mPingEstimated = 0;
            mPrevPingEstimated = 0;

            mListener.onDisconnect(this, mProfile);

        } catch (SocketException e) {

            IRCServer server = mProfile.getServerParams();

            Log.e(TAG, String.format(
                    "[%s:%s] Socket read error: %s",
                    server.address, server.port, e.getMessage()
            ));

            File file = new File(mLogFilePath);
            if(file.exists() && file.isFile())
                file.delete();

            mConnStatus = STATUS_DISCONNECTED;

            mPingStartTime = 0;
            mPingEstimated = 0;
            mPrevPingEstimated = 0;

            mListener.onDisconnect(this, mProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int measurePacketLength(byte[] buffer, int size) { // for CR+LF line endings
        if(size > buffer.length || size <= 0)
            return buffer.length;

        int lastCrLf = size;

        if(buffer[size - 2] == 0x0D && buffer[size - 1] == 0x0A)
            return lastCrLf;

        for(int i = size - 1; i > 1; i--) {
            if(buffer[i - 1] == 0x0D && buffer[i] == 0x0A) {
                lastCrLf = i - 1;
                break;
            }
        }

        return lastCrLf;
    }

    public void sendCommand(IRCCommand cmd, int encoding) {

        if(hasError() || cmd.hasInvalidSyntax() || cmd.type == 0) return;

        try {
            if(cmd.type == IRCBroadcastSignal.OUT_CMD_NICK)
                mCurrentNick = cmd.getArguments()[0];

            if(cmd.type == IRCBroadcastSignal.OUT_CMD_PING)
                mPingStartTime = System.currentTimeMillis();

            if(mSocket != null && mSocket.isConnected() && !mSocket.isClosed()) {
                if(mWriter != null) {
                    byte[] outBytes = cmd.toByteArray(encoding);
                    mWriter.write(outBytes);
                    mWriter.flush();
                }
            }
        } catch (SocketException e) {

            IRCServer server = mProfile.getServerParams();

            Log.e(TAG, String.format(
                    "[%s:%s] Socket send error: %s\r\nCommand type: %s",
                    server.address, server.port, e.getMessage(), cmd.type
            ));
            if(mSocket.isConnected()) disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasError() {
        return  getConnectionStatus() == STATUS_CONNECTING ||
                getConnectionStatus() == STATUS_DISCONNECTED ||
                getConnectionStatus() == STATUS_CONNECTION_TIMEOUT ||
                getConnectionStatus() == STATUS_SERVER_NOT_FOUND ||
                getConnectionStatus() == STATUS_NO_INTERNET_CONNECTION ||
                getConnectionStatus() == STATUS_SERVER_ERROR;
    }

    public void sendMessage(ChatMessage message, int encoding) {

        if(hasError()) return;

        if(message.isIncoming) return;

        try {
            if(mSocket != null && mSocket.isConnected() && !mSocket.isClosed()) {
                if(mWriter != null) {
                    byte[] outBytes = message.toByteArray(encoding);
                    mWriter.write(outBytes);
                    mWriter.flush();
                }
            }
        } catch (SocketException e) {
            IRCServer server = mProfile.getServerParams();

            Log.e(TAG, String.format(
                    "[%s:%s] Socket send error: %s\r\nMessage type: %s",
                    server.address, server.port, e.getMessage(), message.type
            ));
            if(mSocket.isConnected()) disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.close();
                mReader.close();
                mWriter.close();

                mFileWriter.close();
                mFileWriter = null;

                File file = new File(mLogFilePath);
                if(file.exists() && file.isFile())
                    file.delete();

                mConnStatus = STATUS_DISCONNECTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getConnectionStatus() {
        return mConnStatus;
    }

    public ServerProfile getServerProfile() {
        return mProfile;
    }

    public String getCurrentNickname() {
        return mCurrentNick;
    }

    public void setNicknames(ArrayList<ServerProfile.Nickname> nicknames) {
        this.mNicknames = nicknames;
    }

    public ArrayList<ServerProfile.Nickname> getNicknamesList() {
        return mNicknames;
    }

    public void selectNickname(int index) {

        if(mNicknameLock)
            return;

        mCurrentNickIndex = index;

        if(getNicknamesList() == null || getNicknamesList().size() < index)
            return;

        sendCommand(
                new IRCCommand(
                        String.format("/nick %s", getNicknamesList().get(index))
                ), mProfile.getServerParams().outputEncoding
        );
        mNicknameLock = true;
    }

    public void selectNextNickname() {

        if(getNicknamesList() == null || getNicknamesList().size() <= mCurrentNickIndex + 1)
            return;

        mCurrentNickIndex++;
        mCurrentNick = getNicknamesList().get(mCurrentNickIndex).toString();

        sendCommand(
                new IRCCommand(
                        String.format("/nick %s", getNicknamesList().get(mCurrentNickIndex))
                ), mProfile.getServerParams().outputEncoding
        );
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public String getSocketHistory(int size) {
        String history = null;

        int maxBytes = size;

        byte[] buffer = new byte[4096];

        try {
            StringBuilder builder = new StringBuilder();
            long ret;
            long skippedBytes = 0;

            File file = new File(mLogFilePath);

            FileInputStream fis = new FileInputStream(file);

            while(file.length() > maxBytes && skippedBytes < maxBytes) {
                ret = fis.skip(file.length() - maxBytes);

                if(ret <= 0)
                    break;

                skippedBytes += ret;
            }

            while ((ret = fis.read(buffer)) != -1) {
                builder.append(
                        new String(
                                buffer, 0, (int)ret
                        )
                );
            }

            history = builder.toString();

            if(file.length() > maxBytes && history.contains("\r\n")) {
                int firstCrLf = history.indexOf("\r\n");
                history = history.substring(firstCrLf + 2);
            }

            history = builder.toString();

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }

    public long getConnectionStartTime() {
        return mConnStartTime;
    }

    public long getConnectionUptime() {
        return System.currentTimeMillis() - mConnStartTime;
    }

    public void startPingBouncerTask(int interval, boolean value) {

        if(mPingBouncer != null && !value) {
            mPingBouncer.cancel();
            mPingBouncer.purge();
            mPingBouncer = null;
            return;
        }

        if(mPingBouncer != null) return;

        mPingBouncer = new Timer();

        mPingBouncer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        int sequence = random.nextInt(32768);
                        if(getConnectionStatus() == IRCSocketWrapper.STATUS_RUNNING) {
                            if(mPingEstimated >= 0) {
                                mPrevPingEstimated = mPingEstimated;
                                mPingEstimated = -1;
                                sendCommand(
                                        new IRCCommand(
                                                OUT_CMD_PING, String.format("TLXIRC-SW-%s", sequence)
                                        ),
                                        mProfile.getServerParams().inputEncoding
                                );
                            }
                        } else {
                            cancel();
                        }
                    }
                }, 1500, interval);
    }

    public long getCurrentPing() {
        return mPingEstimated;
    }

    public long getPreviousPing() {
        return mPrevPingEstimated;
    }
}
