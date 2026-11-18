package dev.tinelix.irc.android.support.models;

import java.util.ArrayList;

import dev.tinelix.irc.android.support.enitites.LazyEntity;
import dev.tinelix.irc.android.support.enitites.ServerUser;

public class ServerChannel extends LazyEntity {
    public final String name;
    public String topic;
    public ArrayList<ServerUser> members;

    public ServerChannel(String name) {
        this.name = name;
    }

    public ServerChannel(String name, ArrayList<ServerUser> members) {
        this.name = name;
        this.members = members;
    }

}
