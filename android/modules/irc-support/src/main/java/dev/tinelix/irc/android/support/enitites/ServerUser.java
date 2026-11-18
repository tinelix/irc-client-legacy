package dev.tinelix.irc.android.support.enitites;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class ServerUser extends LazyEntity {

    public static final int TYPE_USER       = 0x101;
    public static final int TYPE_BOT        = 0x102;
    public static final int TYPE_SERVICE    = 0x103;

    // User statuses
    public static final int STATUS_ONLINE                       =   1;
    public static final int STATUS_AWAY                         =   2;
    public static final int STATUS_AUTHORIZED                   =   3;
    public static final int STATUS_SECURED                      =   4;

    // User privileges
    public static final int PRIVILEGES_IRC_ADMINISTRATOR        = 101;
    public static final int PRIVILEGES_CHANNEL_OWNER            = 102;
    public static final int PRIVILEGES_CHANNEL_ADMINISTRATOR    = 103;
    public static final int PRIVILEGES_CHANNEL_MODERATOR        = 104;
    public static final int PRIVILEGES_CHANNEL_VOICE            = 105;

    public String nickname;
    public String userMask;
    public String realname;
    public String server;
    private ArrayList<Integer> mStatuses;

    public ServerUser(
            String nickname, String userMask, String realname,
            String server, String statuses
    ) {
        this.nickname = nickname;
        this.userMask = String.format("%s!%s", nickname, userMask);
        this.server = server;
        this.realname = realname;

        if(statuses != null) {
            parseStatues(statuses);
        }
    }

    private void parseStatues(String statuses) {

        if(statuses.length() == 0)
            return;

        if(mStatuses == null)
            this.mStatuses = new ArrayList<>();
        else
            this.mStatuses.clear();

        byte[] statusesBytes = statuses.getBytes();

        for(int i = 0; i < statuses.length(); i++) {

            switch (String.valueOf((char) statusesBytes[i])) {
                case "B":
                    mStatuses.add(TYPE_BOT);
                    break;
                case "H":
                    mStatuses.add(STATUS_ONLINE);
                    break;
                case "G":
                    mStatuses.add(STATUS_AWAY);
                    break;
                case "r":
                    mStatuses.add(STATUS_AUTHORIZED);
                    break;
                case "M":
                case "s":
                    mStatuses.add(STATUS_SECURED);
                    break;
                case "+":
                    mStatuses.add(PRIVILEGES_CHANNEL_VOICE);
                    break;
                case "*":
                    mStatuses.add(PRIVILEGES_IRC_ADMINISTRATOR);
                    break;
                case "~":
                    mStatuses.add(PRIVILEGES_CHANNEL_OWNER);
                    break;
                case "@":
                    mStatuses.add(PRIVILEGES_CHANNEL_ADMINISTRATOR);
                    break;
                case "%":
                    mStatuses.add(PRIVILEGES_CHANNEL_MODERATOR);
                    break;
            }
        }
    }

    public ServerUser(String nickname, String userMask) {
        String fullUserMask = toString();
        if(isValidUserMask(fullUserMask, true)) {
            this.nickname = nickname;
            this.userMask = userMask;

            if((nickname.equals("NickServ") && userMask.startsWith("NickServ@")) ||
               (nickname.equals("ChanServ") && userMask.startsWith("ChanServ@")))
                type = TYPE_SERVICE;
        }
    }

    public ServerUser(String fullUserMask) {
        if(isValidUserMask(fullUserMask, true)) {
            String[] splitedMask = fullUserMask.split("!", 2);

            if(splitedMask.length == 2)
                nickname = splitedMask[0];
                userMask = splitedMask[1];
        }
    }

    public static boolean isValidUserMask(String usermask, boolean fullFormat) {
        String regex = fullFormat ? "^.+?!.+?@.+$" : "^.+?@.+$"; // [nick]![host]@[address] or [host]@[address]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(usermask);
        return matcher.find();
    }

    @Override
    public String toString() {
        return nickname + "!" + userMask;
    }

    public ArrayList<Integer> getStatuses() {
        return mStatuses;
    }
}
