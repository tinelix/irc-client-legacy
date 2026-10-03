package dev.tinelix.irc.android.support.models;

public class ServerProfile {

    public final String name;
    public IRCServer server;
    public boolean isConnected;

    public ServerProfile(String name, IRCServer server, boolean isConnected) {
        this.name = name;
        this.server = server;
        this.isConnected = isConnected;
    }

    public IRCServer getServerParams() {
        return server;
    }
}
