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

package dev.tinelix.irc.android.legacy.ui.list.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.models.ServerChannel;

public class ChannelMembersAdapter extends RecyclerView.Adapter<ChannelMembersAdapter.Holder> {

    private final Context mContext;
    private final ArrayList<ServerUser> mUsers;
    private final ServerChannel mChannel;

    public ChannelMembersAdapter(Context context, ServerChannel channel, ArrayList<ServerUser> users) {
        this.mChannel = channel;
        this.mContext = context;
        this.mUsers = users;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.list_item_chanuser_item, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        private final TextView statusTv;
        private final TextView titleTv;
        private final LinearLayout badgesLayout;

        public Holder(View view) {
            super(view);
            titleTv = view.findViewById(android.R.id.text1);
            statusTv = view.findViewById(android.R.id.text2);
            badgesLayout = view.findViewById(R.id.badges_layout);
        }

        public void bind(int position) {

            Resources res = mContext.getResources();

            float dp = res.getDisplayMetrics().scaledDensity;

            final ServerUser user = mUsers.get(position);
            titleTv.setText(user.realname);

            if(user.nickname.equals(user.realname))
                statusTv.setVisibility(View.GONE);
            else {
                statusTv.setVisibility(View.VISIBLE);
                statusTv.setText(user.nickname);
            }

            ArrayList<Integer> statuses = user.getStatuses();

            boolean hasPrivileges = false;
            boolean hasStatuses = false;

            if(statuses.size() == 0)
                badgesLayout.setVisibility(View.GONE);
            else {
                TextView privilegesBadge = badgesLayout.findViewById(R.id.privileges_badge);
                TextView statusBadge     = badgesLayout.findViewById(R.id.status_badge);

                badgesLayout.setVisibility(View.VISIBLE);

                for(int i = 0; i < statuses.size(); i++) {
                    int stringPos = -1;
                    int badgeColor = -1;
                    int badgeTextColor = -1;

                    switch (statuses.get(i)) {
                        case ServerUser.PRIVILEGES_IRC_ADMINISTRATOR:
                            stringPos = 0;
                            badgeColor = R.drawable.badge_shape_red;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.PRIVILEGES_CHANNEL_OWNER:
                            stringPos = 1;
                            badgeColor = R.drawable.badge_shape_red;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.PRIVILEGES_CHANNEL_ADMINISTRATOR:
                            stringPos = 2;
                            badgeColor = R.drawable.badge_shape_red;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.PRIVILEGES_CHANNEL_MODERATOR:
                            stringPos = 3;
                            badgeColor = R.drawable.badge_shape_orange;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.PRIVILEGES_CHANNEL_VOICE:
                            stringPos = 4;
                            badgeColor = R.drawable.badge_shape_green;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.TYPE_BOT:
                            stringPos = 5;
                            badgeColor = R.drawable.badge_shape_dark_blue;
                            badgeTextColor = Color.WHITE;
                            break;
                        case ServerUser.STATUS_AWAY:
                            stringPos = 6;
                            badgeColor = R.drawable.badge_shape_white;
                            badgeTextColor = Color.BLACK;
                            break;
                    }

                    String[] badgeStrings = res.getStringArray(R.array.ircs_user_mode_badges);

                    if(stringPos >= 0) {
                        if (stringPos < 6) {
                            privilegesBadge.setText(badgeStrings[stringPos]);
                            privilegesBadge.setBackgroundDrawable(
                                    res.getDrawable(badgeColor)
                            );
                            privilegesBadge.setTextColor(badgeTextColor);
                            hasPrivileges = true;
                        } else {
                            statusBadge.setText(badgeStrings[stringPos]);
                            statusBadge.setBackgroundDrawable(
                                    res.getDrawable(badgeColor)
                            );
                            statusBadge.setTextColor(badgeTextColor);
                            hasStatuses = true;
                        }
                    }
                }

                privilegesBadge.setVisibility(hasPrivileges ? View.VISIBLE : View.GONE);
                statusBadge.setVisibility(hasStatuses ? View.VISIBLE : View.GONE);
            }

            itemView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (hasPrivileges || hasStatuses) &&
                            statusTv.getVisibility() == View.VISIBLE ?
                            (int)(76 * dp) : (int)(56 * dp)
                    )
            );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof ChatActivity)
                        ((ChatActivity) mContext).getWritePanel().addUserMention(user);
                }
            });

            itemView.findViewById(R.id.divider).setVisibility(
                    position == getItemCount() - 1 ? View.GONE : View.VISIBLE
            );
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
