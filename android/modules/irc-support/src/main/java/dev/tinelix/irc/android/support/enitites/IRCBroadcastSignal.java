package dev.tinelix.irc.android.support.enitites;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.seppius.i18n.plurals.PluralResources;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dev.tinelix.irc.android.support.R;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.IRCModes;

@SuppressWarnings({"unused", "WeakerAccess"})
public class IRCBroadcastSignal extends LazyEntity {

    /* Internet Relay Chat message and response format implementation
     * Referenced from RFC2812 - Internet Relay Chat: Client Protocol.
     *
     * We calling a *signals* because network connection to an IRC server
     * represents the transmission of events, commands outputs and messages
     * in near real-time.
     *
     * It's like a network broadcast, but there is no continuous stream
     * of data or unnecessary noise.
     */

    // Connection statuses
    public static final int IN_DHCP_NXDOMAIN                =   -3;
    public static final int IN_DISCONNECTED                 =   -2;
    public static final int IN_CONNECTION_REFUSED           =   -1;

    // Client-server only incoming signal types
    public static final int IN_WELCOME                      =    1; // or 001
    public static final int IN_YOURHOST                     =    2; // or 002
    public static final int IN_CREATED                      =    3; // or 003
    public static final int IN_MYINFO                       =    4; // or 004
    public static final int IN_BOUNCE                       =    5; // or 005
    public static final int IN_NOTICE                       =    6; // or if starts with NOTICE
    public static final int IN_PING                         =    7; // or if starts with PING
    public static final int IN_ERROR                        =    8; // or if starts with ERROR
    public static final int IN_USER_MODES                   =    9; // or if starts with MODE
    public static final int IN_CMD_PONG                     =   10; /* or if starts with
                                                                       :[server] PONG  */
    public static final int IN_NICK                         =   11; /* or if starts with
                                                                       :[user mask] NICK  */
    public static final int IN_JOIN                         =   12; /* or if starts with
                                                                       :[user mask] JOIN  */
    public static final int IN_PRIVMSG                      =   13; /* or if starts with
                                                                       :[user mask] PRIVMSG  */
    public static final int IN_PART                         =   14; /* or if starts with
                                                                       :[user mask] PART  */
    public static final int IN_QUIT                         =   15; /* or if starts with
                                                                       :[user mask] QUIT  */
    public static final int IN_HOSTCHANGED                  =  396;

    // Command responses
    public static final int IN_CMD_RULES                    =  232;
    public static final int IN_CMD_LUSER_CLIENTS            =  251;
    public static final int IN_CMD_LUSER_ADMINS             =  252;
    public static final int IN_CMD_LUSER_UNKNOWNS           =  253;
    public static final int IN_CMD_LUSER_CHANNELS           =  254;
    public static final int IN_CMD_LUSER_ME                 =  255;
    public static final int IN_CMD_LUSER_LOCAL              =  265;
    public static final int IN_CMD_LUSER_GLOBAL             =  266;
    public static final int IN_CMD_AWAY                     =  301;
    public static final int IN_CMD_USERHOST                 =  302;
    public static final int IN_CMD_ISON                     =  303;
    public static final int IN_CMD_UNAWAY                   =  305;
    public static final int IN_CMD_NOWAWAY                  =  306;
    public static final int IN_CMD_RULES_BEGIN              =  308;
    public static final int IN_CMD_RULES_END                =  309;
    public static final int IN_CMD_WHOIS_USER               =  311;
    public static final int IN_CMD_WHOIS_SERVER             =  312;
    public static final int IN_CMD_WHOIS_OPERATOR           =  313;
    public static final int IN_CMD_WHOWAS_USER              =  314;
    public static final int IN_CMD_WHO_END                  =  315;
    public static final int IN_CMD_WHOIS_IDLE               =  317;
    public static final int IN_CMD_WHOIS_END                =  318;
    public static final int IN_CMD_WHOIS_CHANNELS           =  319;
    public static final int IN_CMD_LIST                     =  322;
    public static final int IN_CMD_LIST_END                 =  323;
    public static final int IN_CMD_CHANNEL_MODES            =  324;
    public static final int IN_CMD_UNIQOPIS                 =  325;
    public static final int IN_CMD_CHANNEL_WEBSITE          =  328;
    public static final int IN_CMD_TOPIC                    =  332;
    public static final int IN_CMD_TOPIC_CREATED            =  333;
    public static final int IN_CMD_VERSION                  =  351;
    public static final int IN_CMD_WHO                      =  352;
    public static final int IN_CMD_CHANNEL_NAMES            =  353;
    public static final int IN_CMD_CHANNEL_NAMES_END        =  366;
    public static final int IN_CMD_BANLIST                  =  367;
    public static final int IN_CMD_BANLIST_END              =  368;
    public static final int IN_CMD_WHOWAS_END               =  369;
    public static final int IN_CMD_MOTD                     =  372;
    public static final int IN_CMD_MOTD_BEGIN               =  375;
    public static final int IN_CMD_MOTD_END                 =  376;
    public static final int IN_CMD_OPER                     =  381;
    public static final int IN_CMD_REHASH                   =  382;
    public static final int IN_CMD_TIME                     =  391;

    // Errors
    public static final int IN_ERR_TRYAGAIN                 =  263;
    public static final int IN_ERR_NONE                     =  300;
    public static final int IN_ERR_NOTOPIC                  =  331;
    public static final int IN_ERR_NOSUCHNICK               =  401;
    public static final int IN_ERR_NOSUCHSERVER             =  402;
    public static final int IN_ERR_NOSUCHCHANNEL            =  403;
    public static final int IN_ERR_CANTSENDTOCHAN           =  404;
    public static final int IN_ERR_TOOMANYCHANNELS          =  405;
    public static final int IN_ERR_WASNOSUCHNICK            =  406;
    public static final int IN_ERR_TOOMANYTARGETS           =  407;
    public static final int IN_ERR_NOSUCHSERVICE            =  408;
    public static final int IN_ERR_NOORIGIN                 =  409;
    public static final int IN_ERR_NORECIPIENT              =  411;
    public static final int IN_ERR_NOTEXTTOSEND             =  412;
    public static final int IN_ERR_NOTOPLEVEL               =  413;
    public static final int IN_ERR_WILDTOPLEVEL             =  414;
    public static final int IN_ERR_BADMASK                  =  415;
    public static final int IN_ERR_UNKNOWNCOMMAND           =  416;
    public static final int IN_ERR_NOMOTD                   =  417;
    public static final int IN_ERR_NONICKNAMEGIVEN          =  431;
    public static final int IN_ERR_BADNICKNAME              =  432;
    public static final int IN_ERR_NICKNAMEINUSE            =  433;
    public static final int IN_ERR_NICKCOLLISION            =  436;
    public static final int IN_ERR_UNAVAILABLERES           =  437;
    public static final int IN_ERR_USERNOTINCHANNEL         =  441;
    public static final int IN_ERR_NOTONCHANNEL             =  442;
    public static final int IN_ERR_USERONCHANNEL            =  443;
    public static final int IN_ERR_NOTLOGIN                 =  444;
    public static final int IN_ERR_NOTREGISTERED            =  451;
    public static final int IN_ERR_NEEDMOREPARAMS           =  461;
    public static final int IN_ERR_PASSWDMISMATCH           =  464;
    public static final int IN_ERR_YOUREBANNEDCREEP         =  465;
    public static final int IN_ERR_CHANNELISFULL            =  471;
    public static final int IN_ERR_UNKNOWNMODE              =  472;
    public static final int IN_ERR_INVITEONLYCHAN           =  473;
    public static final int IN_ERR_BANNEDFROMCHAN           =  474;
    public static final int IN_ERR_NOCHANMODES              =  477;
    public static final int IN_ERR_BANLISTFILL              =  478;
    public static final int IN_ERR_NOPRIVILEGES             =  481;
    public static final int IN_ERR_RESTRICTED               =  484;

    // IRCv2/v3 Daemon Supported Commands
    public static final int OUT_CMD_HELP                    = 1001;
    public static final int OUT_CMD_TIME                    = 1002;
    public static final int OUT_CMD_USER                    = 1003;
    public static final int OUT_CMD_NICK                    = 1004;
    public static final int OUT_CMD_PONG                    = 1005;
    public static final int OUT_CMD_MOTD                    = 1006;
    public static final int OUT_CMD_RULES                   = 1007;
    public static final int OUT_CMD_MODE                    = 1008;
    public static final int OUT_CMD_AWAY                    = 1009;
    public static final int OUT_CMD_PRIVMSG                 = 1010;
    public static final int OUT_CMD_PING                    = 1011;
    public static final int OUT_CMD_WHO                     = 1012;
    public static final int OUT_CMD_WHOIS                   = 1013;
    public static final int OUT_CMD_WHOWAS                  = 1014;
    public static final int OUT_CMD_LIST                    = 1015;
    public static final int OUT_CMD_JOIN                    = 1016;
    public static final int OUT_CMD_NAMES                   = 1017;
    public static final int OUT_CMD_TOPIC                   = 1018;
    public static final int OUT_CMD_PART                    = 1019;
    public static final int OUT_CMD_CREDITS                 = 1020;
    public static final int OUT_CMD_VERSION                 = 1021;
    public static final int OUT_CMD_LICENSE                 = 1022;
    public static final int OUT_CMD_QUIT                    = 1023;

    // IRCv2/v3 Raw Commands
    public static final int OUT_RAW_PONG                    = 1050;

    // NickServ Commands
    public static final int OUT_SVC_NICKSERV_HELP           = 1101;
    public static final int OUT_SVC_NICKSERV_IDENTIFY       = 1102;
    public static final int OUT_SVC_NICKSERV_REGISTER       = 1103;
    public static final int OUT_SVC_NICKSERV_INFO           = 1104;
    public static final int OUT_SVC_NICKSERV_SENDPASS       = 1105;
    public static final int OUT_SVC_NICKSERV_GHOST          = 1106;
    public static final int OUT_SVC_NICKSERV_GROUP          = 1107;
    public static final int OUT_SVC_NICKSERV_UNGROUP        = 1108;
    public static final int OUT_SVC_NICKSERV_DROP           = 1109;
    public static final int OUT_SVC_NICKSERV_LOGOUT         = 1110;


    private boolean mWithTimestamp;

    public    String        sender;
    public    String        destination;
    public    String        params;
    public    String        body;
    public    long          date;
    public    boolean       isIncoming;
    protected String        mRawContent;
    protected StringBuilder mRawBuilder;
    protected StringBuilder mBodyBuilder;
    public    int           linesCount;
    protected boolean       mIsFinished;

    public IRCBroadcastSignal(int type, boolean isIncoming) {
        this.type = type;
        this.isIncoming = isIncoming;
        this.date = -1;
        mRawBuilder = new StringBuilder();
        mBodyBuilder = new StringBuilder();
    }

    public IRCBroadcastSignal(int type, String body, boolean isIncoming) {
        this.type = type;
        this.body = body;
        this.isIncoming = isIncoming;
        this.date = -1;
        mRawBuilder = new StringBuilder();
        mBodyBuilder = new StringBuilder();

        linesCount++;
    }

    public IRCBroadcastSignal(String rawContent, boolean isIncoming) {
        this.isIncoming = isIncoming;

        this.date = -1;

        mRawBuilder = new StringBuilder();
        mBodyBuilder = new StringBuilder();

        if(isIncoming && !rawContent.startsWith("/"))
            parseContent(rawContent);

        linesCount++;
    }

    public IRCBroadcastSignal(String rawContent, boolean isIncoming, boolean withTimestamp) {
        this.mWithTimestamp = withTimestamp;
        this.isIncoming = isIncoming;
        this.date = -1;

        mRawBuilder = new StringBuilder();
        mBodyBuilder = new StringBuilder();

        if(isIncoming && !rawContent.startsWith("/"))
            parseContent(rawContent);
    }

    public static IRCBroadcastSignal fromString(String rawContent, boolean isIncoming) {
        IRCBroadcastSignal signal = new IRCBroadcastSignal(rawContent, isIncoming);

        if(rawContent.startsWith("/"))
            signal = new IRCCommand(rawContent);
        else if(rawContent.startsWith(":")) {
            String[] words = rawContent.split(" ", 3);
            if(words.length < 2)
                return null;

            String sender = words[0].substring(1);

            if(ServerUser.isValidUserMask(sender, true) && words[1].equals("PRIVMSG"))
                signal = new ChatMessage(rawContent, isIncoming);
        }

        return signal;
    }

    public static IRCBroadcastSignal fromString(String trackContent, boolean isIncoming, boolean withTimestamp) {
        try {
            String[] trackContentSplit = null;
            String rawContent;

            if (withTimestamp) {
                trackContentSplit = trackContent.split(" ", 2);
                if (trackContentSplit.length == 2)
                    rawContent = trackContentSplit[1];
                else
                    return null;
            } else {
                rawContent = trackContent;
            }

            IRCBroadcastSignal signal = new IRCBroadcastSignal(rawContent, isIncoming);

            long date = 0;

            if (withTimestamp && trackContentSplit[0].length() > 1) {
                if (trackContentSplit[1].startsWith(":"))
                    date = Long.parseLong(trackContentSplit[0]);
                else
                    return null;
            }

            if (rawContent.startsWith("/"))
                signal = new IRCCommand(rawContent);
            else if (rawContent.startsWith(":")) {
                String[] words = rawContent.split(" ", 4);

                if (words.length < 2) return null;

                String sender = words[0].substring(1);

                if (ServerUser.isValidUserMask(sender, true) && words[1].equals("PRIVMSG")) {
                    signal = new ChatMessage(rawContent, isIncoming);
                } else if (words[1].equals("MODE")) {
                    signal = new IRCModes(rawContent);
                } else if(words[1].equals("PONG")) {
                    if(words.length < 4) return null;

                    if(words[3].startsWith(":TLXIRC-CMD"))
                        signal = new ServerPong(true);
                    else
                        return null;
                } else if (words[1].matches("^\\d+$")) { // is integer?
                    int type = Integer.parseInt(words[1]);

                    switch (type) {
                        case IN_CMD_LUSER_CHANNELS:
                        case IN_CMD_LUSER_LOCAL:
                        case IN_CMD_LUSER_GLOBAL:
                        case IN_CMD_LUSER_ADMINS:
                        case IN_CMD_LUSER_UNKNOWNS:
                            signal = new ServerStats(type, rawContent);
                            break;
                        case IN_CMD_CHANNEL_MODES:
                            signal = new IRCModes(rawContent);
                            break;
                        case IN_CMD_WHO:
                            signal = new IRCUsers(rawContent);
                            break;
                    }
                }
            }

            if (date >= 0) signal.date = date;

            return signal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean parseContent(String rawContent) {
        mRawContent = rawContent;

        StringBuilder builder = new StringBuilder();

        if(rawContent.length() == 0) return false;

        String[] words = rawContent.split(" ");

        if(isIncoming) {
            if (words.length == 2 && words[0].equals("PING")) {
                type = IN_PING;
                if (words[1].startsWith(":"))
                    body = words[1].substring(1);
                else
                    body = words[1];
                finish();
                return true;
            } else if (words.length >= 2 && words[0].equals("ERROR")) {
                type = IN_ERROR;
                body = parseBody(1, words);
                finish();
                return true;
            }
        }

        if (!words[0].startsWith(":")) return false;

        if (words.length < 3) return false;

        if (words[1].matches("^\\d+$")) { // is integer?
            type = Integer.parseInt(words[1]);

            switch (type) {
                case IN_CMD_LUSER_UNKNOWNS:
                case IN_CMD_LUSER_CHANNELS:
                case IN_CMD_LUSER_ADMINS:
                    if(words.length < 4) break;

                    params = words[2];
                    body = parseBody(3, words);
                    break;
                case IN_CMD_LUSER_LOCAL:
                case IN_CMD_LUSER_GLOBAL:
                    if(words.length < 6) break;

                    body = parseBody(5, words);
                    break;
                case IN_CMD_TIME:
                case IN_CMD_VERSION:
                case IN_CMD_RULES:
                case IN_CMD_MOTD:
                    if(words.length < 5) break;

                    params = words[2] + " " + words[3];
                    body = parseBody(4, words);
                    break;
                case IN_CMD_WHO:
                case IN_CMD_WHOIS_USER:
                case IN_CMD_WHOIS_SERVER:
                case IN_CMD_WHOIS_OPERATOR:
                case IN_CMD_WHOIS_IDLE:
                case IN_CMD_WHOIS_END:
                case IN_CMD_WHOIS_CHANNELS:
                case IN_ERR_NICKNAMEINUSE:

                    if(words.length < 5) break;

                    params = words[2] + " " + words[3];
                    body = parseBody(4, words);
                    break;
                case IN_CMD_MOTD_END:
                    break;
                case IN_CMD_CHANNEL_WEBSITE:
                case IN_CMD_TOPIC:
                    if(words.length < 5) break;

                    params = words[2];
                    destination = words[3];

                    body = parseBody(4, words);
                    break;
                case IN_CMD_TOPIC_CREATED:
                    if(words.length < 6)
                        break;

                    destination = words[3];
                    params = words[2];
                    sender = words[4];
                    date = Long.parseLong(words[5]);
                    break;
                case IN_CMD_CHANNEL_NAMES:
                    if(words.length < 6) break;

                    params = words[2];
                    destination = words[4];
                    body = parseBody(5, words);
                    break;
                case IN_CMD_WHO_END:
                    if(words.length < 5) break;

                    params = words[2];
                    destination = words[3];
                    break;
                default:
                    if(words.length < 4) break;

                    params = words[2];
                    body = parseBody(3, words);
                    break;
            }
        } else {
            switch (words[1]) {
                case "PONG":
                    type = IN_CMD_PONG;
                    body = parseBody(3, words);
                    break;
                case "NICK":
                    type = IN_NICK;

                    body = parseBody(2, words);

                    if(words[0].startsWith(":"))
                        sender = words[0].substring(1);
                    else
                        sender = words[0];
                    break;
                case "NOTICE":
                    type = IN_NOTICE;

                    body = parseBody(3, words);

                    if(words[0].startsWith(":"))
                        sender = words[0].substring(1);
                    else
                        sender = words[0];
                    break;
                case "JOIN":
                    type = IN_JOIN;

                    if(words[0].startsWith(":"))
                        sender = words[0].substring(1);
                    else
                        sender = words[0];

                    if(words[2].startsWith(":"))
                        destination = words[2].substring(1);
                    else
                        destination = words[2];
                    break;
                case "PART":
                    type = IN_PART;

                    if(words[0].startsWith(":"))
                        sender = words[0].substring(1);
                    else
                        sender = words[0];

                    if(words[2].startsWith(":"))
                        destination = words[2].substring(1);
                    else
                        destination = words[2];
                    break;
                case "TOPIC":
                    type = IN_CMD_TOPIC;
                    body = parseBody(3, words);
                    destination = words[2];
                    break;
                case "QUIT":
                    type = IN_QUIT;

                    if(words[0].startsWith(":"))
                        sender = words[0].substring(1);
                    else
                        sender = words[0];

                    body = parseBody(2, words);
                    break;
                default:
                    body = parseBody(3, words);
            }
        }

        if(type == IN_CMD_MOTD) {
            SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());
            format.setLenient(false);

            try {
                format.parse(body);
                date = format.getCalendar().getTime().getTime();
                body = body.substring(body.indexOf("\r\n") + 1);
            } catch (Exception ignored) { }
        }

        mRawBuilder = mRawContent != null ? new StringBuilder(mRawContent) : new StringBuilder();
        mBodyBuilder = body != null ? new StringBuilder(body) : new StringBuilder();
        mIsFinished = type != IN_CMD_CHANNEL_NAMES && type != IN_CMD_TOPIC;
        return true;
    }

    private String parseBody(int startIndex, String[] words) {

        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < words.length; i++) {

            if(words[i].length() == 0) {
                builder.append(" ");
                continue;
            }

            char c = words[i].charAt(0);

            if(i == startIndex && c == ':') {
                builder.append(words[i].substring(1));
            } else
                builder.append(words[i]);

            if (i < words.length - 1)
                builder.append(" ");
        }

        return builder.toString();
    }

    public static String getCategoryString(Context context, IRCBroadcastSignal signal) {
        String[] categories = context.getResources().getStringArray(
                dev.tinelix.irc.android.support.R.array.ircs_signal_types
        );

        switch (signal.type) {
            case IN_WELCOME:
                return categories[0];
            case IN_YOURHOST:
            case IN_HOSTCHANGED:
                return categories[1];
            case IN_CREATED:
            case IN_MYINFO:
                return categories[2];
            case IN_CMD_CHANNEL_WEBSITE:
            case IN_CMD_CHANNEL_NAMES:
                return categories[3];
            case IN_BOUNCE:
                return categories[4];
            case IN_CMD_LUSER_CLIENTS:
            case IN_CMD_LUSER_ADMINS:
            case IN_CMD_LUSER_UNKNOWNS:
            case IN_CMD_LUSER_CHANNELS:
            case IN_CMD_LUSER_ME:
            case IN_CMD_LUSER_LOCAL:
            case IN_CMD_LUSER_GLOBAL:
                return categories[5];
            case IN_CMD_MOTD_BEGIN:
            case IN_CMD_MOTD:
            case IN_CMD_MOTD_END:
                return categories[6];
            case IN_CMD_RULES_BEGIN:
            case IN_CMD_RULES:
            case IN_CMD_RULES_END:
                return categories[7];
            case IN_USER_MODES:
            case IN_CMD_CHANNEL_MODES:
                return categories[8];
            case IN_CMD_TOPIC:
                return categories[9];
            case IN_NOTICE:
            case OUT_CMD_PRIVMSG:
            case IN_PRIVMSG:
                return categories[10];
            case IN_NICK:
            case IN_JOIN:
            case IN_PART:
            case IN_QUIT:
                return categories[11];
        }

        if(isError(signal))
            return categories[13];

        return categories[12];
    }

    @Override
    public String toString() {
        return mRawContent + "\r\n";
    }

    public void merge(IRCBroadcastSignal signal) {

        if(signal.toString().split("\r\n").length > 1)
            return;

        if(signal.type == IN_CMD_CHANNEL_NAMES_END) return;

        if(this.type == IN_CMD_TOPIC && signal.type == IN_CMD_TOPIC_CREATED) {

            if(signal.toString() != null)
                mRawBuilder.append(signal.toString());

            this.sender = signal.sender;
            this.date = signal.date;

            linesCount++;

        } else if(signal.type == this.type) {
            if(this.sender == null)
                this.sender = signal.sender;

            if(this.destination == null)
                this.destination = signal.destination;

            if(signal.toString() != null)
                mRawBuilder.append(signal.toString());

            if(signal.body != null)
                mBodyBuilder.append(signal.body);

            mBodyBuilder.append("\r\n");

            mRawContent = mRawBuilder.toString();
            body = mBodyBuilder.toString();

            linesCount++;

            if(type == IN_CMD_MOTD && linesCount == 1) {
                SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());
                format.setLenient(false);

                try {
                    format.parse(body);
                    date = format.getCalendar().getTime().getTime();
                } catch (Exception ignored) { }
            }
        }
    }

    public static boolean isError(IRCBroadcastSignal signal) {
        return signal.type == IN_ERROR                  ||
               signal.type == IN_ERR_TRYAGAIN           ||
               signal.type == IN_ERR_NONE               ||
               signal.type == IN_ERR_NOTOPIC            ||
               signal.type == IN_ERR_NOSUCHNICK         ||
               signal.type == IN_ERR_NOSUCHSERVER       ||
               signal.type == IN_ERR_NOSUCHCHANNEL      ||
               signal.type == IN_ERR_CANTSENDTOCHAN     ||
               signal.type == IN_ERR_TOOMANYCHANNELS    ||
               signal.type == IN_ERR_WASNOSUCHNICK      ||
               signal.type == IN_ERR_TOOMANYTARGETS     ||
               signal.type == IN_ERR_NOSUCHSERVICE      ||
               signal.type == IN_ERR_NOORIGIN           ||
               signal.type == IN_ERR_NORECIPIENT        ||
               signal.type == IN_ERR_NOTEXTTOSEND       ||
               signal.type == IN_ERR_NOTOPLEVEL         ||
               signal.type == IN_ERR_WILDTOPLEVEL       ||
               signal.type == IN_ERR_BADMASK            ||
               signal.type == IN_ERR_UNKNOWNCOMMAND     ||
               signal.type == IN_ERR_NOMOTD             ||
               signal.type == IN_ERR_NONICKNAMEGIVEN    ||
               signal.type == IN_ERR_BADNICKNAME        ||
               signal.type == IN_ERR_NICKNAMEINUSE      ||
               signal.type == IN_ERR_NICKCOLLISION      ||
               signal.type == IN_ERR_UNAVAILABLERES     ||
               signal.type == IN_ERR_USERNOTINCHANNEL   ||
               signal.type == IN_ERR_NOTONCHANNEL       ||
               signal.type == IN_ERR_USERONCHANNEL      ||
               signal.type == IN_ERR_NOTLOGIN           ||
               signal.type == IN_ERR_NOTREGISTERED      ||
               signal.type == IN_ERR_NEEDMOREPARAMS     ||
               signal.type == IN_ERR_PASSWDMISMATCH     ||
               signal.type == IN_ERR_YOUREBANNEDCREEP   ||
               signal.type == IN_ERR_CHANNELISFULL      ||
               signal.type == IN_ERR_UNKNOWNMODE        ||
               signal.type == IN_ERR_INVITEONLYCHAN     ||
               signal.type == IN_ERR_BANNEDFROMCHAN     ||
               signal.type == IN_ERR_NOCHANMODES        ||
               signal.type == IN_ERR_BANLISTFILL        ||
               signal.type == IN_ERR_RESTRICTED         ||
               signal.type == IN_ERR_NOPRIVILEGES;
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public SpannableString getLocalizedSpannedBody(Context context, int highlightColor) {
        Resources res = context.getResources();

        String localizedBody = body;
        SpannableString spannedBody;
        SimpleDateFormat originalDateFormat;
        SimpleDateFormat localizedDateFormat;

        String senderNick = "";

        if(sender != null)
            senderNick = sender.split("!")[0];

        if(body == null && type != IN_JOIN && type != IN_PART && type != IN_QUIT)
            return null;

        String[] bodyWords = null;

        if(body != null) {
            bodyWords = body.split(" ");
            spannedBody = new SpannableString(localizedBody);
        } else {
            spannedBody = new SpannableString("");
        }

        switch (type) {
            case OUT_SVC_NICKSERV_IDENTIFY:
                if(bodyWords == null) break;

                String startCmd = "/nickserv identify";

                if(bodyWords.length == 3)
                    startCmd = bodyWords[0] + " " + bodyWords[1];
                else if(bodyWords.length > 3)
                    startCmd = bodyWords[0] + " " + bodyWords[1] + " "  + bodyWords[2] + " " + bodyWords[3];

                localizedBody = startCmd + " ***";
                spannedBody = new SpannableString(localizedBody);

                break;
            case IN_NOTICE:
                if(body.equals("*** Looking up your hostname..."))
                    localizedBody = res.getString(R.string.ircs_signal_notice_lookup_hostname);
                else if(body.startsWith("*** Couldn't resolve your hostname;"))
                    localizedBody = res.getString(R.string.ircs_signal_notice_cant_resolve_hostname);

                spannedBody = new SpannableString(localizedBody);
                break;
            case IN_WELCOME:
                String networkName;
                String userMask;

                if(bodyWords.length == 7) {
                    if(body.startsWith("Welcome to the ") && bodyWords[4].equals("IRC") &&
                            bodyWords[5].equals("Network")) {
                        networkName = bodyWords[3];
                        userMask = ServerUser.isValidUserMask(bodyWords[6], true) ?
                                bodyWords[6] : "N/A";

                        localizedBody = res.getString(R.string.ircs_signal_welcome, networkName, userMask);

                        spannedBody = new SpannableString(localizedBody);

                        if(networkName.length() > 0) {
                            int networkNameIndex = localizedBody.indexOf(networkName);
                            spannedBody.setSpan(
                                    new StyleSpan(Typeface.BOLD),
                                    networkNameIndex, networkNameIndex + networkName.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            );
                        }
                    }
                }
                break;
            case IN_CREATED:
                if(!body.startsWith("This server was created"))
                    break;

                body = body.replace(" EEST "," GMT+03:00 ")
                           .replace(" MSK ", " GMT+03:00 ")
                           .replace(" MSD ", " GMT+04:00 ")
                           .replace(" EET ", " GMT+02:00 ")
                           .replace(" CEST "," GMT+02:00 ")
                           .replace(" CET ", " GMT+01:00 ");

                String dateFormatted = "?";

                if(body.contains(" at "))
                    originalDateFormat = new SimpleDateFormat(
                        "E MMM d yyyy 'at' HH:mm:ss zzz", Locale.ENGLISH
                    );
                else
                    originalDateFormat = new SimpleDateFormat(
                        "E MMM d HH:mm:ss z yyyy", Locale.US
                    );

                localizedDateFormat = new SimpleDateFormat(
                    Locale.getDefault() == Locale.US ? "MMMM d, yyyy" : "d MMMM yyyy",
                    Locale.getDefault()
                );

                try {
                    if(body.contains(" at "))
                        originalDateFormat.parse(body.substring("This server was created ".length()));
                    else
                        originalDateFormat.parse(body.substring("This server was created on ".length()));

                    dateFormatted = localizedDateFormat.format(
                            originalDateFormat.getCalendar().getTime().getTime()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                localizedBody = res.getString(R.string.ircs_signal_server_created, dateFormatted);

                spannedBody = new SpannableString(localizedBody);

                break;
            case IN_HOSTCHANGED:
                if(body != null) {
                    localizedBody = res.getString(R.string.ircs_signal_host_changed, bodyWords[0]);
                    spannedBody = new SpannableString(localizedBody);

                    if(bodyWords[0].length() > 0) {
                        int hostAddrIndex = localizedBody.indexOf(bodyWords[0]);

                        spannedBody.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                hostAddrIndex, hostAddrIndex + bodyWords[0].length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                }
                break;
            case IN_CMD_TIME:
                try {
                    originalDateFormat = new SimpleDateFormat(
                            "EEEE MMMM dd yyyy -- HH:mm XXX", Locale.ENGLISH
                    );

                    originalDateFormat.parse(body);

                    localizedDateFormat = new SimpleDateFormat(
                            Locale.getDefault() == Locale.US ?
                            "MMMM d, yyyy hh:mm a" :
                            "d MMMM yyyy HH:mm",
                            Locale.getDefault()
                    );

                    dateFormatted = localizedDateFormat.format(
                            originalDateFormat.getCalendar().getTime().getTime()
                    );

                    localizedBody = dateFormatted;
                } catch (Exception e) {
                    localizedBody = body;
                }

                spannedBody = new SpannableString(localizedBody);
                break;
            case IN_CMD_CHANNEL_NAMES:
                localizedBody = res.getString(R.string.ircs_signal_channel_members_count, destination,
                    IRCBroadcastSignal.getQuantityString(
                            context, R.plurals.members_count,
                            bodyWords.length, 0, false
                    )
                );
                spannedBody = new SpannableString(localizedBody);

                spannedBody.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        localizedBody.indexOf(destination),
                        localizedBody.indexOf(destination) + destination.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                break;
            case IN_CMD_CHANNEL_WEBSITE:
                localizedBody = res.getString(R.string.ircs_signal_channel_website, body);
                spannedBody = new SpannableString(localizedBody);
                spannedBody.setSpan(
                        new StyleSpan(Typeface.BOLD), 0, localizedBody.indexOf(body),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                break;
            case IN_NICK:
                localizedBody = res.getString(R.string.ircs_signal_nickname_changed,
                        sender.split("!")[0], body
                );
                spannedBody = new SpannableString(localizedBody);

                if(senderNick.length() > 0)
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD), 0, senderNick.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                if(body.length() > 0) {
                    int newNicknameIndex = localizedBody.indexOf(body);

                    if(newNicknameIndex < 0) break;

                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD), newNicknameIndex,
                            newNicknameIndex + body.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                break;
            case IN_JOIN:
                localizedBody = res.getString(R.string.ircs_signal_member_join, senderNick, destination);
                spannedBody = new SpannableString(localizedBody);

                if(senderNick.length() > 0)
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD), 0, senderNick.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                if(destination.length() > 0) {
                    int destinationIndex = localizedBody.indexOf(destination);
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            destinationIndex, destinationIndex + destination.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
                break;
            case IN_PART:
                localizedBody = res.getString(R.string.ircs_signal_member_leave, senderNick, destination);
                spannedBody = new SpannableString(localizedBody);

                if(senderNick.length() > 0)
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD), 0, senderNick.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                if(destination.length() > 0) {
                    int destinationIndex = localizedBody.indexOf(destination);
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            destinationIndex, destinationIndex + destination.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                break;
            case IN_QUIT:

                if(body == null) {
                    localizedBody = res.getString(R.string.ircs_signal_member_quit, senderNick);
                } else if(body.startsWith("Ping timeout: ") && bodyWords[2].matches("^\\d+$")){
                    localizedBody = res.getString(
                            R.string.ircs_signal_member_quit_ping_timeout, senderNick,
                            Integer.parseInt(bodyWords[2])
                    );
                } else if(body.equals("Normal Exit") || body.equals("Client exited") ||
                          body.equals("Connection closed")) {
                    localizedBody = res.getString(
                            R.string.ircs_signal_member_quit, senderNick
                    );
                } else if(body.equals("Read error")) {
                    localizedBody = res.getString(
                            R.string.ircs_signal_member_quit_client_error, senderNick
                    );
                } else if(body.startsWith("Quit: ")) {
                    localizedBody = res.getString(
                            R.string.ircs_signal_member_quit_with_reason, senderNick,
                            body.substring("Quit: ".length())
                    );
                } else {
                    localizedBody = res.getString(
                            R.string.ircs_signal_member_quit_with_reason,
                            senderNick, body
                    );
                }

                spannedBody = new SpannableString(localizedBody);

                if(senderNick.length() > 0)
                    spannedBody.setSpan(
                            new StyleSpan(Typeface.BOLD), 0, senderNick.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                break;
            case IN_ERR_NICKNAMEINUSE:
                localizedBody = res.getString(R.string.ircs_signal_error_nickname_in_use);
                spannedBody = new SpannableString(localizedBody);
                break;
        }

        return spannedBody;
    }

    @SuppressWarnings("SameParameterValue")
    protected static String getQuantityString(
            Context context, int pluralId, long quantity,
            int leadingCount, boolean leadingZeros
    ) {

        String quantityStr = String.valueOf(quantity);
        int endNumber = quantityStr.length() >= 10 ?
                            Integer.parseInt(quantityStr.substring(quantityStr.length() - 1)) :
                            (int) quantity;

        Resources res = context.getResources();

        StringBuilder builder = new StringBuilder();
        builder.append("%");

        if(leadingCount > 0) {
            if (leadingZeros)
                builder.append("0");

            builder.append(String.valueOf(leadingCount));
        }
        builder.append("d");

        String strPattern = builder.toString();
        builder.setLength(0);

        try {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    res.getQuantityString(
                        pluralId, endNumber,
                        String.format(Locale.getDefault(), strPattern, quantity)
                    ) : new PluralResources(res).getQuantityString(
                        pluralId, endNumber,
                        String.format(Locale.getDefault(), strPattern, quantity)
                    );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public void finish() {
        mIsFinished = true;
    }
}
