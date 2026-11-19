/*
 * Copyleft © 2022, 2026 Dmitry Tretyakov (aka. Tinelix)
 *
 * This file is part of Tinelix IRC Client for Android.
 *
 * Tinelix IRC Client is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see https://www.gnu.org/licenses/.
 *
 * Source code: https://github.com/tinelix/irc-client-legacy
 */

package dev.tinelix.irc.android.legacy.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Random;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.support.enitites.ChatMessage;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;
import dev.tinelix.irc.android.support.enitites.LazyEntity;
import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.models.ServerChannel;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class WriteTextPanel extends LinearLayout {

    private IRCSocketWrapper mSocket;
    private LazyEntity mDestination;
    private ServerProfile mProfile;
    private OnSentListener mOnSentListener;

    public WriteTextPanel(Context context) {
        super(context);
        View view = inflate(getContext(), R.layout.layout_write_text_panel, null);
        addView(view);

        final AutoMaskedEditText edit = findViewById(R.id.message_edit);

        if(!HoloCompatSupport.isTablet(getContext()))
            edit.setMaxLines(6);
        else
            edit.setMaxLines(3);
    }

    public WriteTextPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(getContext(), R.layout.layout_write_text_panel, null);
        addView(view);

        final AutoMaskedEditText edit = findViewById(R.id.message_edit);

        if(!HoloCompatSupport.isTablet(getContext()))
            edit.setMaxLines(6);
        else
            edit.setMaxLines(3);
    }

    public void setOnSentListener(OnSentListener listener) {
        mOnSentListener = listener;
    }

    public void setServerProfile(ServerProfile profile) {
        mProfile = profile;

        final EditText edit = findViewById(R.id.message_edit);

        findViewById(R.id.send_message_btn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage(edit.getText().toString());
                        edit.setText("");
                    }
                }
        );
    }

    public void setSocketWrapper(IRCSocketWrapper wrapper) {
        mSocket = wrapper;
    }

    public void setDestination(LazyEntity destination) {
        this.mDestination = destination;

        final AutoMaskedEditText edit = findViewById(R.id.message_edit);

        edit.setMultiline(mDestination != null);
    }

    public void sendCommand(IRCCommand cmd) {
        if(mDestination == null ||
           cmd.type == IRCBroadcastSignal.OUT_CMD_JOIN ||
           cmd.type == IRCBroadcastSignal.OUT_CMD_PART ||
           cmd.type == IRCBroadcastSignal.OUT_CMD_PRIVMSG ||
           cmd.type == IRCBroadcastSignal.OUT_CMD_QUIT
        ) {
            if(mProfile != null)
                mSocket.sendCommand(cmd, mProfile.getServerParams().inputEncoding);
        }
    }

    public void sendMessage(final String text) {
        if(text.length() == 0) return;

        final String[] lines = text.split("\n");

        if(mProfile == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(String line : lines) {
                    IRCBroadcastSignal signal = null;
                    if(line.startsWith("/join")) {
                        signal = new IRCCommand(line);
                    } else if (mDestination == null) {
                        Random random = new Random();

                        int sequence = random.nextInt(32768);

                        switch (line) {
                            case "/ping":
                                signal = new IRCCommand(line + " TLXIRC-CMD-" + sequence);
                                break;
                            case "/quit":
                                signal = new IRCCommand(line + " " + mProfile.quitMessage);
                                break;
                            default:
                                signal = new IRCCommand(line);
                                break;
                        }
                    } else if (mDestination instanceof ServerUser) {
                        signal = new ChatMessage(line, ((ServerUser) mDestination).userMask);
                    } else {
                        switch (line) {
                            case "/part":
                                signal = new IRCCommand(
                                        line + " " + ((ServerChannel) mDestination).name
                                );
                                break;
                            case "/quit":
                                signal = new IRCCommand(line + " " + mProfile.quitMessage);
                                break;
                            case "/ns":
                                break;
                            default:
                                signal = new ChatMessage(line, ((ServerChannel) mDestination).name);
                                break;
                        }
                    }

                    if(signal == null) return;

                    signal.body = line;
                    signal.date = System.currentTimeMillis();

                    if (signal instanceof IRCCommand)
                        sendCommand((IRCCommand) signal);

                    if (signal instanceof ChatMessage) {
                        mSocket.sendMessage(
                                (ChatMessage) signal, mProfile.getServerParams().inputEncoding
                        );
                    }

                    final IRCBroadcastSignal fSignal = signal;
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mOnSentListener.onMessageSent(fSignal);
                            final AutoMaskedEditText edit = findViewById(R.id.message_edit);
                            if(mDestination == null) {
                                edit.setMultiline(true);
                                edit.setMultiline(false);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void addUserMention(ServerUser user) {
        final EditText edit = findViewById(R.id.message_edit);
        edit.setText(String.format("%s: %s", user.nickname, edit.getText().toString()));
    }

    public String getText() {
        final EditText edit = findViewById(R.id.message_edit);
        return edit.getText().toString();
    }

    public interface OnSentListener {
        public void onMessageSent(IRCBroadcastSignal signal);
    }
}
