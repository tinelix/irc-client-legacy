package dev.tinelix.irc.android.support.enitites;

import android.content.Context;
import android.content.res.Resources;

import dev.tinelix.irc.android.support.R;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerChannel;

public class IRCCommand extends IRCBroadcastSignal {

    private String mCmdName;
    private int mCmdNameInt;
    private String[] mArgs;
    public String description;

    private boolean mInvalidSyntax;

    public IRCCommand(String rawContent) {
        super(rawContent, false);
        parseContent(rawContent);
    }

    public IRCCommand(int type, String body) {
        super(type, body, false);
        mCmdNameInt = type;
        mArgs = body.split(" ");
    }

    @Override
    public boolean parseContent(String rawContent) {
        String[] details = rawContent.split(" ");

        if(details.length == 0 || details[0].length() == 0)
            return false;

        mCmdName = details[0];

        String argsStr = rawContent.substring(mCmdName.length());
        if(argsStr.length() > 1) {
            mArgs = rawContent.substring(mCmdName.length() + 1).split(" ");
            body = rawContent;
        }

        switch (mCmdName) {
            case "/help":
                mCmdNameInt = OUT_CMD_HELP;
                break;
            case "/time":
                mCmdNameInt = OUT_CMD_TIME;
                break;
            case "/user":
                mCmdNameInt = OUT_CMD_USER;
                break;
            case "/nick":
                mCmdNameInt = OUT_CMD_NICK;
                break;
            case "/pong":
                mCmdNameInt = OUT_CMD_PONG;
                break;
            case "/motd":
                mCmdNameInt = OUT_CMD_MOTD;
                break;
            case "/rules":
                mCmdNameInt = OUT_CMD_RULES;
                break;
            case "/away":
                mCmdNameInt = OUT_CMD_AWAY;
                break;
            case "/mode":
                mCmdNameInt = OUT_CMD_MODE;
                break;
            case "/privmsg":
            case "/msg":
            case "/pm":
            case "/sendpm":
                mCmdNameInt = OUT_CMD_PRIVMSG;
                break;
            case "/ping":
                mCmdNameInt = OUT_CMD_PING;
                break;
            case "/who":
                mCmdNameInt = OUT_CMD_WHO;
                break;
            case "/whois":
                mCmdNameInt = OUT_CMD_WHOIS;
                break;
            case "/whowas":
                mCmdNameInt = OUT_CMD_WHOWAS;
                break;
            case "/list":
                mCmdNameInt = OUT_CMD_LIST;
                break;
            case "/join":
                mCmdNameInt = OUT_CMD_JOIN;
                break;
            case "/names":
                mCmdNameInt = OUT_CMD_NAMES;
                break;
            case "/topic":
                mCmdNameInt = OUT_CMD_TOPIC;
                break;
            case "/part":
                mCmdNameInt = OUT_CMD_PART;
                break;
            case "/credits":
                mCmdNameInt = OUT_CMD_CREDITS;
                break;
            case "/version":
                mCmdNameInt = OUT_CMD_VERSION;
                break;
            case "/license":
                mCmdNameInt = OUT_CMD_LICENSE;
                break;
            case "/quit":
                mCmdNameInt = OUT_CMD_QUIT;
                break;
            case "/ns":
            case "/nickserv":
                if(mArgs == null) {
                    mCmdNameInt = OUT_SVC_NICKSERV_HELP;
                    mArgs = new String[] {"help"};
                } else if(mArgs.length > 0) {
                    switch (mArgs[0]) {
                        case "help":
                            mCmdNameInt = OUT_SVC_NICKSERV_HELP;
                            break;
                        case "id":
                        case "identify":
                            mCmdNameInt = OUT_SVC_NICKSERV_IDENTIFY;
                            break;
                        case "reg":
                        case "register":
                            mCmdNameInt = OUT_SVC_NICKSERV_REGISTER;
                            break;
                        case "info":
                            mCmdNameInt = OUT_SVC_NICKSERV_INFO;
                            break;
                        case "sendpass":
                            mCmdNameInt = OUT_SVC_NICKSERV_SENDPASS;
                            break;
                        case "ghost":
                            mCmdNameInt = OUT_SVC_NICKSERV_GHOST;
                            break;
                        case "grp":
                        case "group":
                            mCmdNameInt = OUT_SVC_NICKSERV_GROUP;
                            break;
                        case "ungrp":
                        case "ungroup":
                            mCmdNameInt = OUT_SVC_NICKSERV_UNGROUP;
                            break;
                        case "drop":
                            mCmdNameInt = OUT_SVC_NICKSERV_DROP;
                            break;
                        case "logout":
                            mCmdNameInt = OUT_SVC_NICKSERV_LOGOUT;
                            break;
                    }
                }
                break;
            case "/rawsend":
                if(mArgs.length > 0)
                    mRawContent = body.substring("/rawsend".length() + 1);
                else
                    mInvalidSyntax = true;
                break;
        }

        type = mCmdNameInt;

        return true;
    }

    public byte[] toByteArray(int textEncoding) {
        StringBuilder builder = new StringBuilder();

        String outStr;
        byte[] outBytes = null;

        if(mInvalidSyntax)
            return null;

        try {
            switch (mCmdNameInt) {
                case OUT_CMD_HELP:
                    builder.append("HELP");
                    break;
                case OUT_CMD_TIME:
                    builder.append("TIME");
                    break;
                case OUT_CMD_LICENSE:
                    builder.append("LICENSE");
                    break;
                case OUT_CMD_USER:
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
                case OUT_CMD_NICK:
                    builder.append("NICK ");

                    if (mArgs.length == 1)
                        builder.append(mArgs[0]);
                    else
                        mInvalidSyntax = true;

                    break;
                case OUT_CMD_PING:
                    builder.append("PING");

                    if(mArgs == null || mArgs.length == 0) {
                        mInvalidSyntax = true;
                        break;
                    }

                    if (mArgs.length == 1) {
                        if (!mArgs[0].startsWith(":"))
                            builder.append(" :").append(mArgs[0]);
                        else
                            builder.append(" ").append(mArgs[0]);
                    }
                    break;
                case OUT_RAW_PONG:
                case OUT_CMD_PONG:
                    builder.append("PONG");

                    if (mArgs != null && mArgs.length == 1)
                        builder.append(" ").append(mArgs[0]);
                    break;
                case OUT_CMD_MOTD:
                    builder.append("MOTD");
                    break;
                case OUT_CMD_RULES:
                    builder.append("RULES");
                    break;
                case OUT_CMD_JOIN:
                    builder.append("JOIN");

                    if (mArgs != null && mArgs.length == 1) {
                        if(mArgs[0].startsWith("#") || mArgs[0].startsWith("&"))
                            builder.append(" ").append(mArgs[0]);
                    }
                    break;
                case OUT_CMD_PART:
                    builder.append("PART");

                    if (mArgs != null && mArgs.length == 1) {
                        if(mArgs[0].startsWith("#") || mArgs[0].startsWith("&"))
                            builder.append(" ").append(mArgs[0]);
                    }
                    break;
                case OUT_CMD_PRIVMSG:
                    builder.append("PRIVMSG");

                    if (mArgs != null && mArgs.length >= 2) {
                        builder.append(" ").append(mArgs[0]);

                        for (int i = 1; i < mArgs.length; i++) {
                            if (i == 1 && !mArgs[i].startsWith(":"))
                                builder.append(" :").append(mArgs[i]);
                            else
                                builder.append(" ").append(mArgs[i]);
                        }
                    } else
                        mInvalidSyntax = true;
                    break;
                case OUT_CMD_WHO:
                    builder.append("WHO");

                    if (mArgs != null) {
                        for (int i = 0; i < mArgs.length; i++) {
                            if (i == 0 && !mArgs[i].startsWith(":"))
                                builder.append(" :").append(mArgs[i]);
                            else
                                builder.append(" ").append(mArgs[i]);
                        }
                    }
                    break;
                case OUT_CMD_WHOIS:
                    builder.append("WHOIS");

                    if (mArgs.length == 1)
                        builder.append(" ").append(mArgs[0]);
                    else
                        mInvalidSyntax = true;

                    break;
                case OUT_CMD_VERSION:
                    builder.append("VERSION");
                    break;
                case OUT_CMD_QUIT:
                    builder.append("QUIT");

                    if (mArgs != null) {
                        for (int i = 0; i < mArgs.length; i++) {
                            if (i == 0 && !mArgs[i].startsWith(":"))
                                builder.append(" :").append(mArgs[i]);
                            else
                                builder.append(" ").append(mArgs[i]);
                        }
                    }
                    break;
                case OUT_SVC_NICKSERV_HELP:
                case OUT_SVC_NICKSERV_IDENTIFY:
                case OUT_SVC_NICKSERV_REGISTER:
                case OUT_SVC_NICKSERV_SENDPASS:
                case OUT_SVC_NICKSERV_INFO:
                case OUT_SVC_NICKSERV_GROUP:
                case OUT_SVC_NICKSERV_UNGROUP:
                    builder.append("PRIVMSG NickServ");

                    if (mArgs != null) {
                        for (int i = 0; i < mArgs.length; i++) {
                            if (i == 0 && !mArgs[i].startsWith(":"))
                                builder.append(" :").append(mArgs[i]);
                            else
                                builder.append(" ").append(mArgs[i]);
                        }
                    }
                    break;
                default:
                    builder.append(mRawContent);
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
                    case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                        outBytes = outStr.getBytes("windows-1251");
                        break;
                    case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                        outBytes = outStr.getBytes("windows-1252");
                        break;
                }
            }

            return outBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outBytes;
    }

    public String[] getArguments() {
        return mArgs;
    }

    public static void putCommandDescription(Context context, IRCCommand command) {

        Resources res = context.getResources();
        int descriptionIndex;

        switch (command.type) {
            case OUT_CMD_HELP:
                descriptionIndex = 1;
                break;
            case OUT_CMD_RULES:
                descriptionIndex = 2;
                break;
            case OUT_CMD_PING:
                descriptionIndex = 3;
                break;
            case OUT_CMD_TIME:
                descriptionIndex = 4;
                break;
            case OUT_CMD_VERSION:
                descriptionIndex = 5;
                break;
            case OUT_SVC_NICKSERV_IDENTIFY:
                descriptionIndex = 6;
                break;
            case OUT_SVC_NICKSERV_REGISTER:
                descriptionIndex = 7;
                break;
            case OUT_SVC_NICKSERV_INFO:
                descriptionIndex = 8;
                break;
            case OUT_CMD_MODE:
                descriptionIndex = 9;
                break;
            case OUT_CMD_NICK:
                descriptionIndex = 10;
                break;
            case OUT_CMD_PRIVMSG:
                descriptionIndex = 11;
                break;
            case OUT_CMD_JOIN:
                descriptionIndex = 12;
                break;
            case OUT_CMD_PART:
                descriptionIndex = 13;
                break;
            case OUT_CMD_QUIT:
                descriptionIndex = 14;
                break;
            default:
                descriptionIndex = 15;
                break;
        }

        command.description = res.getStringArray(R.array.ircs_command_descriptions)[descriptionIndex];
    }

    public String getName() {
        return mCmdName;
    }

    public boolean hasInvalidSyntax() {
        return mInvalidSyntax;
    }
}
