package dev.tinelix.irc.android.support.wrappers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class IRCSocketWrapper {

    public static final int STATUS_NOT_INITIALIZED          = 0;
    public static final int STATUS_INITIALIZED              = 1;
    public static final int STATUS_CONNECTING               = 2;
    public static final int STATUS_AUTHORIZED               = 3;
    public static final int STATUS_CONNECTED                = 4;

    public static final int STATUS_DISCONNECTED             = -1;
    public static final int STATUS_SERVER_ERROR             = -2;
    public static final int STATUS_CONNECTION_TIMEOUT       = -3;
    public static final int STATUS_NO_INTERNET_CONNECTION   = -4;
    public static final int STATUS_INTERAL_ERROR            = -5;

    private ServerProfile mProfile;
    private boolean mTrustAllTlsCerts = false;
    private Socket socket;
    private int status;

    private BufferedReader reader;
    private BufferedWriter writer;

    public IRCSocketWrapper(ServerProfile profile) {
        mProfile = profile;
    }

    public IRCSocketWrapper(ServerProfile profile, boolean trustAllTlsCerts) {
        mProfile = profile;
        IRCServer server = mProfile.getServerParams();

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

                status = STATUS_CONNECTING;

                SSLSocketFactory factory = sslContext.getSocketFactory();
                InetAddress serverAddr = InetAddress.getByName(server.address);
                socket = factory.createSocket(serverAddr, server.port);

                if(socket.isConnected())
                    status = STATUS_CONNECTED;

                createSocketBuffer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createSocketBuffer() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(IRCCommand cmd) {
        try {
            if (socket != null && socket.isConnected() && !socket.isClosed()) {
                if(writer != null) {
                    writer.write(cmd.toString());
                    writer.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                reader.close();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
