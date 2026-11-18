package dev.tinelix.irc.android.legacy.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import dev.tinelix.irc.android.legacy.R;

public class IRCChannelHeader extends LinearLayout {

    public IRCChannelHeader(Context context) {
        super(context);
        View view = View.inflate(this, R.layout.layout_channel_header, null);

    }
}
