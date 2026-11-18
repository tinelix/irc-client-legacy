package dev.tinelix.irc.android.support.listeners;

import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public interface SocketConnectionListener {
    void onConnect(IRCSocketWrapper wrapper, ServerProfile profile);
    void onReceive(
            IRCSocketWrapper wrapper, ServerProfile profile, String rawData,
            long dataSize, long receivedDate
    );
    void onDisconnect(IRCSocketWrapper wrapper, ServerProfile profile);

    void onConnectionFailed(int mConnStatus, ServerProfile profile);
}
