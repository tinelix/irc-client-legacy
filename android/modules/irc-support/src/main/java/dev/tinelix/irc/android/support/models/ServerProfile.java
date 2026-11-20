package dev.tinelix.irc.android.support.models;

import android.content.SharedPreferences;

public class ServerProfile {

    public final String name;
    public String fullName;
    private IRCServer server;
    public boolean isConnected;
    public String hostname;
    public String realname;
    public String quitMessage;
    public int pingInterval;

    public ServerProfile(String name, String fullName, IRCServer server, boolean isConnected) {
        this.name = name;
        this.fullName = fullName;
        this.server = server;
        this.isConnected = isConnected;
        this.pingInterval = 60000;
    }

    public ServerProfile(String name, String fullName, String hostName, String realName,
                         String quitMessage, IRCServer server, int pingInterval, boolean isConnected) {
        this.name = name;
        this.fullName = fullName;
        this.hostname = hostName;
        this.realname = realName;
        this.quitMessage = quitMessage;
        this.server = server;
        this.isConnected = isConnected;
        if(pingInterval == 10000 || pingInterval == 30000 || pingInterval == 60000 || pingInterval == 90000)
            this.pingInterval = pingInterval;
        else
            this.pingInterval = 60000;
    }

    public IRCServer getServerParams() {
        return server;
    }

    public void setServerParams(IRCServer server) {
        this.server = server;
    }

    public static ServerProfile from(String prefsName, SharedPreferences prefs) {
        return new ServerProfile(
                prefsName,
                prefs.getString("profileName", ""),
                prefs.getString("hostName", ""),
                prefs.getString("realName", "Tinelix IRC User"),
                prefs.getString("quitMessage", ""),
                new IRCServer(prefs.getString("serverAddress", "")),
                prefs.getInt("pingInterval", 60000),
                false
        );
    }

    public static class Nickname {
        public String nickname;
        public boolean isPrimary;
        private String privateKey;
        private String encPassword;
        private String plainPassword;

        public Nickname(String nickname, boolean isPrimary) {
            this.nickname = nickname;
            this.isPrimary = isPrimary;
        }

        @Override
        public String toString() {
            return nickname;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String key) {
            this.privateKey = key;
        }

        public void setEncryptedPassword(String password) {
            this.encPassword = password;
        }

        public String getEncryptedPassword() {
            return encPassword;
        }

        public void setPlainPassword(String password) {
            plainPassword = password;
            encPassword = null;
        }

        public String getPlainPassword() {
            return plainPassword;
        }
    }
}
