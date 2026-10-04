package dev.tinelix.irc.android.support.wrappers;

import java.nio.charset.Charset;

import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerChannel;

public class IRCCommand {

    // IRCv2/v3 Daemon Supported Commands
    public static final int COMMAND_HELP                    = 0x0001;
    public static final int COMMAND_USER                    = 0x0002;
    public static final int COMMAND_NICK                    = 0x0003;
    public static final int COMMAND_PONG                    = 0x0004;
    public static final int COMMAND_MOTD                    = 0x0005;
    public static final int COMMAND_RULES                   = 0x0006;
    public static final int COMMAND_MODE                    = 0x0007;
    public static final int COMMAND_AWAY                    = 0x0008;
    public static final int COMMAND_PRIVMSG                 = 0x0009;
    public static final int COMMAND_PING                    = 0x000A;
    public static final int COMMAND_WHOIS                   = 0x000B;
    public static final int COMMAND_WHOWAS                  = 0x000C;
    public static final int COMMAND_LIST                    = 0x000D;
    public static final int COMMAND_JOIN                    = 0x000E;
    public static final int COMMAND_NAMES                   = 0x000F;
    public static final int COMMAND_TOPIC                   = 0x0010;
    public static final int COMMAND_PART                    = 0x0011;
    public static final int COMMAND_CREDITS                 = 0x0012;
    public static final int COMMAND_VERSION                 = 0x0013;
    public static final int COMMAND_LICENSE                 = 0x0014;
    public static final int COMMAND_QUIT                    = 0x0015;

    // NickServ Commands
    public static final int COMMAND_NICKSERV_HELP           = 0x0100;
    public static final int COMMAND_NICKSERV_IDENTIFY       = 0x0101;
    public static final int COMMAND_NICKSERV_REGISTER       = 0x0102;
    public static final int COMMAND_NICKSERV_INFO           = 0x0103;
    public static final int COMMAND_NICKSERV_SENDPASS       = 0x0104;
    public static final int COMMAND_NICKSERV_GHOST          = 0x0105;
    public static final int COMMAND_NICKSERV_GROUP          = 0x0106;
    public static final int COMMAND_NICKSERV_UNGROUP        = 0x0107;
    public static final int COMMAND_NICKSERV_DROP           = 0x0108;
    public static final int COMMAND_NICKSERV_LOGOUT         = 0x0109;

    private String mCmdName;
    private int mCmdNameInt;
    private ServerChannel mDestChannel;
    private ServerUser mDestUser;
    private String[] mArgs;

    private boolean mInvalidSyntax;

    public IRCCommand(String name, String[] args) {
        mCmdName = name;
        mArgs = args;

        String outStr;
        byte[] outBytes;

        switch (mCmdName) {
            case "/help":
                mCmdNameInt = COMMAND_HELP;
                break;
            case "/user":
                mCmdNameInt = COMMAND_USER;
                break;
            case "/nick":
                mCmdNameInt = COMMAND_NICK;
                break;
            case "/pong":
                mCmdNameInt = COMMAND_PONG;
                break;
            case "/motd":
                mCmdNameInt = COMMAND_MOTD;
                break;
            case "/rules":
                mCmdNameInt = COMMAND_RULES;
                break;
            case "/away":
                mCmdNameInt = COMMAND_AWAY;
                break;
            case "/mode":
                mCmdNameInt = COMMAND_MODE;
                break;
            case "/privmsg":
            case "/msg":
            case "/pm":
            case "/sendpm":
                mCmdNameInt = COMMAND_PRIVMSG;
                break;
            case "/ping":
                mCmdNameInt = COMMAND_PING;
                break;
            case "/whois":
                mCmdNameInt = COMMAND_WHOIS;
                break;
            case "/whowas":
                mCmdNameInt = COMMAND_WHOWAS;
                break;
            case "/list":
                mCmdNameInt = COMMAND_LIST;
                break;
            case "/join":
                mCmdNameInt = COMMAND_JOIN;
                break;
            case "/names":
                mCmdNameInt = COMMAND_NAMES;
                break;
            case "/topic":
                mCmdNameInt = COMMAND_TOPIC;
                break;
            case "/part":
                mCmdNameInt = COMMAND_PART;
                break;
            case "/credits":
                mCmdNameInt = COMMAND_CREDITS;
                break;
            case "/version":
                mCmdNameInt = COMMAND_VERSION;
                break;
            case "/license":
                mCmdNameInt = COMMAND_LICENSE;
                break;
            case "/quit":
                mCmdNameInt = COMMAND_QUIT;
                break;
            case "/ns":
            case "/nickserv":
                if(args.length > 0) {
                    switch (args[0]) {
                        case "help":
                            mCmdNameInt = COMMAND_NICKSERV_HELP;
                        case "id":
                        case "identify":
                            mCmdNameInt = COMMAND_NICKSERV_IDENTIFY;
                            mArgs = args;
                            break;
                        case "reg":
                        case "register":
                            mCmdNameInt = COMMAND_NICKSERV_REGISTER;
                            mArgs = args;
                            break;
                        case "info":
                            mCmdNameInt = COMMAND_NICKSERV_INFO;
                            break;
                        case "sendpass":
                            mCmdNameInt = COMMAND_NICKSERV_SENDPASS;
                            mArgs = args;
                            break;
                        case "ghost":
                            mCmdNameInt = COMMAND_NICKSERV_GHOST;
                            break;
                        case "grp":
                        case "group":
                            mCmdNameInt = COMMAND_NICKSERV_GROUP;
                            break;
                        case "ungrp":
                        case "ungroup":
                            mCmdNameInt = COMMAND_NICKSERV_UNGROUP;
                            break;
                        case "drop":
                            mCmdNameInt = COMMAND_NICKSERV_DROP;
                            break;
                        case "logout":
                            mCmdNameInt = COMMAND_NICKSERV_LOGOUT;
                    }
                }
                break;
        }
    }

    public byte[] toByteArray(int textEncoding) {
        StringBuilder builder = new StringBuilder();

        String outStr;
        byte[] outBytes = new byte[0];

        try {
            switch (mCmdNameInt) {
                case COMMAND_HELP:
                    builder.append("HELP");
                    break;
                case COMMAND_USER:
                    builder.append("USER");

                    if (mArgs.length > 0) {
                        for (int i = 0; i < mArgs.length; i++) {
                            if (i == 3 && !mArgs[i].startsWith(":"))
                                builder.append(" :").append(mArgs[i]);
                            else
                                builder.append(" ").append(mArgs[i]);
                        }
                    } else {
                        mInvalidSyntax = true;
                    }

                    break;
                case COMMAND_NICK:
                    builder.append("NICK ");

                    if (mArgs.length == 1)
                        builder.append(mArgs[0]);
                    else
                        mInvalidSyntax = true;

                    break;
                case COMMAND_PONG:
                    builder.append("PONG");

                    if (mArgs.length == 1) {
                        if (!mArgs[0].startsWith(":"))
                            builder.append(" :").append(mArgs[0]);
                        else
                            builder.append(" ").append(mArgs[0]);
                    }
                    break;
                case COMMAND_MOTD:
                    builder.append("MOTD");
                    break;
            }

            builder.append("\r\n");

            outStr = builder.toString();

            if(!mInvalidSyntax) {
                switch (textEncoding) {
                    case IRCServer.TEXT_ENCODING_UNICODE_UTF8:
                        outBytes = outStr.getBytes();
                        break;
                    case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                        outBytes = outStr.getBytes("UTF-16LE");
                        break;
                    case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                        outBytes = outStr.getBytes("UTF-16BE");
                        break;
                }
            }

            return outBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outBytes;
    }
}
