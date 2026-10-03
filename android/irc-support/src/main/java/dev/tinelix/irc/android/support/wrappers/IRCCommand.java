package dev.tinelix.irc.android.support.wrappers;

import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.models.ServerChannel;

public class IRCCommand {

    private int name;
    private ServerChannel destChannel;
    private ServerUser destUser;
    private String args;

    public IRCCommand(String name, String args) {

    }
}
