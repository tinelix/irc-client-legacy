package dev.tinelix.irc.android.legacy.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;

public class ChatHeaderView extends LinearLayout {

    public ChatHeaderView(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.layout_channel_header, null);
        this.addView(view);
    }

    public void setPinnedMessage(IRCBroadcastSignal signal) {

    }
}
