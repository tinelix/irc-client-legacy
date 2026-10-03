package dev.tinelix.irc.android.support.models;

import java.util.ArrayList;

import dev.tinelix.irc.android.support.enitites.ServerUser;

public class ServerChannel {
    public final String name;
    public String topic;
    public ArrayList<ServerUser> members;

    public ServerChannel(String name, ArrayList<ServerUser> members) {
        this.name = name;
        this.members = members;
    }

}
