package dev.tinelix.irc.android.support.enitites;

public class ServerUser {
    public String nickname;
    public String userMask;

    public ServerUser(String nickname, String userMask) {
        this.nickname = nickname;
        this.userMask = userMask;
    }

    public ServerUser(String fullUserMask) {
        String[] splitedMask = fullUserMask.split("!", 1);

        nickname = splitedMask[0];
        userMask = splitedMask[1];
    }
}
