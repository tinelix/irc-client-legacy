package dev.tinelix.irc.android.support.models;

import java.util.ArrayList;

import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.ServerUser;

public class IRCUsers extends IRCBroadcastSignal {
    private ArrayList<ServerUser> mMembers;

    public IRCUsers(String rawContent) {
        super(IN_CMD_WHO, true);
        mRawContent = rawContent;
        mMembers = new ArrayList<>();

        String[] words = mRawContent.split(" ");

        if(words.length >= 4)
            destination = words[3];
    }

    public boolean parseContent() {

        String[] lines = mRawContent.split("\r\n");

        for (String line : lines) {

            String[] words = line.split(" ");

            if (!words[0].startsWith(":")) return false;

            if (words.length < 10) return false;

            StringBuilder userMaskBuilder = new StringBuilder();
            String server = null;
            String nickname = null;
            String statuses = null;
            String realname;
            StringBuilder realnameBuilder = new StringBuilder();

            for (int i = 3; i < words.length; i++) {
                switch (i) {
                    case 3:
                        if(words[i].startsWith("#") || words[i].startsWith("&"))
                            destination = words[i];
                        break;
                    case 4:
                        userMaskBuilder.append(words[i]);
                        break;
                    case 5:
                        userMaskBuilder.append("@").append(words[i]);
                        break;
                    case 6:
                        if(!words[i].equals("*"))
                            server = words[i];
                        break;
                    case 7:
                        if(!words[i].equals("#") && !words[i].equals("&"))
                            nickname = words[i];
                        break;
                    case 8:
                        statuses = words[i];
                        break;
                    case 9:
                        break;
                    default:
                        realnameBuilder.append(words[i]).append(" ");
                        break;
                }
            }

            if(realnameBuilder.length() > 0)
                realname = realnameBuilder.substring(0, realnameBuilder.length() - 1);
            else
                realname = realnameBuilder.append(nickname).toString();

            mMembers.add(new ServerUser(nickname, userMaskBuilder.toString(), realname, server, statuses));
        }

        finish();

        return true;
        //return super.parseContent(rawContent);
    }

    @Override
    public void merge(IRCBroadcastSignal signal) {

        if(isFinished()) return;

        if(signal == null) return;

        if(mRawBuilder == null)
            mRawBuilder = new StringBuilder(mRawContent);

        String rawContent = signal.toString();

        mRawBuilder.append(rawContent);

        mRawContent = mRawBuilder.toString().substring(0, mRawBuilder.length() - 2);
    }

    public ArrayList<ServerUser> getList() {
        return mMembers;
    }

    public ServerUser findMember(String usermask, boolean fullFormat) {

        if(mMembers == null) return null;

        for(ServerUser user : mMembers) {
            if(fullFormat && user.userMask.equals(usermask))
                return user;
            else if(user.userMask.endsWith(usermask))
                return user;
        }

        return null;
    }
}
