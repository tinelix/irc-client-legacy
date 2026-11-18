package dev.tinelix.irc.android.support.enitites;

import dev.tinelix.irc.android.support.models.IRCServer;

public class ChatMessage extends IRCBroadcastSignal {
    public LazyEntity author;
    public String text;
    public long timestamp;

    public ChatMessage(String rawContent, boolean isIncoming) {
        super(rawContent, isIncoming);
        if(isIncoming) {
            type = IN_PRIVMSG;
        }
    }

    public ChatMessage(String text, String dest) {
        super(OUT_CMD_PRIVMSG, text, false);
        destination = dest;
    }

    @Override
    public boolean parseContent(String rawContent) {
        mRawContent = rawContent;

        StringBuilder builder = new StringBuilder();

        if(rawContent.length() == 0)
            return false;

        String[] words = rawContent.split(" ");

        if (!words[0].startsWith(":")) return false;

        if (words.length < 3) return false;

        if(words[1].matches("^\\d+$")) return false;

        switch (words[1]) {
            case "JOIN":
                type = IN_JOIN;

                if(words[2].startsWith(":"))
                    destination = words[2].substring(1);
                else
                    destination = words[2];

                break;
            case "PART":
                type = IN_PART;

                if(words[2].startsWith(":"))
                    destination = words[2].substring(1);
                else
                    destination = words[2];

                break;
            case "NOTICE":
                type = IN_NOTICE;

                for (int j = 3; j < words.length; j++) {
                    if(j == 3 && words[j].startsWith(":"))
                        builder.append(words[j].substring(1));
                    else
                        builder.append(words[j]);

                    if (j < words.length - 1)
                        builder.append(" ");
                }

                if(ServerUser.isValidUserMask(words[0].substring(1), true)) {
                    author = new ServerUser(words[0].substring(1));
                    sender = words[0].substring(1);
                }

                body = builder.toString();

                if(words[2].startsWith("#") || words[2].startsWith("&"))
                    destination = words[2];
                else
                    destination = words[2];

                break;
            case "PRIVMSG":
                type = IN_PRIVMSG;

                for (int j = 3; j < words.length; j++) {
                    if(j == 3 && words[j].startsWith(":"))
                        builder.append(words[j].substring(1));
                    else
                        builder.append(words[j]);

                    if (j < words.length - 1)
                        builder.append(" ");
                }

                author = new ServerUser(words[0].substring(1));
                sender = words[0].substring(1);
                body = builder.toString();
                destination = words[2];

                break;
        }

        finish();
        return true;
    }

    public byte[] toByteArray(int textEncoding) {
        if(destination == null && mRawContent == null)
            return null;

        byte[] outBytes = null;

        String outStr;

        if(mRawContent == null) {
            outStr = String.format("PRIVMSG %s :%s\r\n", destination, body);
        } else {
            outStr = mRawContent;
        }

        try {
            if (outStr != null) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outBytes;
    }
}
