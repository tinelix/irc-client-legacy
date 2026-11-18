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
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.legacy.ui.views.AutoMaskedEditText;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;

@SuppressWarnings("ConstantConditions")
public class CommandsListAdapter extends ArrayAdapter<IRCCommand> {

    private final Context mContext;
    private final ArrayList<IRCCommand> mCommands;
    private final ArrayList<IRCCommand> mFilteredCommands;

    public CommandsListAdapter(@NonNull Context context, ArrayList<IRCCommand> commands) {
        super(context, R.layout.list_item_command, commands);
        mContext = context;
        mCommands = commands;
        mFilteredCommands = new ArrayList<>();
    }

    public static CommandsListAdapter createInstance(Context context) {
        CommandsListAdapter adapter;

        ArrayList<IRCCommand> commands = new ArrayList<>();

        IRCCommand helpCmd          = new IRCCommand("/help");
        IRCCommand versionCmd       = new IRCCommand("/version");
        IRCCommand nsIdentifyCmd    = new IRCCommand("/ns id");
        IRCCommand nsRegisterCmd    = new IRCCommand("/ns reg");
        IRCCommand nickCmd          = new IRCCommand("/nick");
        IRCCommand msgCmd           = new IRCCommand("/msg");
        IRCCommand joinCmd          = new IRCCommand("/join");
        IRCCommand partCmd          = new IRCCommand("/part");
        IRCCommand quitCmd          = new IRCCommand("/quit");

        IRCCommand.putCommandDescription(context, helpCmd);
        IRCCommand.putCommandDescription(context, versionCmd);
        IRCCommand.putCommandDescription(context, nsIdentifyCmd);
        IRCCommand.putCommandDescription(context, nsRegisterCmd);
        IRCCommand.putCommandDescription(context, nickCmd);
        IRCCommand.putCommandDescription(context, msgCmd);
        IRCCommand.putCommandDescription(context, joinCmd);
        IRCCommand.putCommandDescription(context, partCmd);
        IRCCommand.putCommandDescription(context, quitCmd);

        commands.add(helpCmd);
        commands.add(versionCmd);
        commands.add(nsIdentifyCmd);
        commands.add(nsRegisterCmd);
        commands.add(nickCmd);
        commands.add(msgCmd);
        commands.add(joinCmd);
        commands.add(partCmd);
        commands.add(quitCmd);

        adapter = new CommandsListAdapter(context, commands);

        return adapter;
    }

    @Nullable
    @Override
    public IRCCommand getItem(int position) {
        return mCommands.get(position);
    }

    @Override
    public int getCount() {
        return mCommands.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(parent instanceof ListView && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            ((ListView) parent).setCacheColorHint(Color.TRANSPARENT);
            ((ListView) parent).setSelector(R.drawable.holo4ux_highlight);
        }

        View view = null;

        if(inflater != null)
            view = inflater.inflate(R.layout.list_item_command, null, true);

        if(view == null) return null;

        TextView cmdName = view.findViewById(android.R.id.text1);
        TextView cmdDesc = view.findViewById(android.R.id.text2);

        IRCCommand command = getItem(position);

        boolean isNickServ = command.type == IRCBroadcastSignal.OUT_SVC_NICKSERV_REGISTER ||
                             command.type == IRCBroadcastSignal.OUT_SVC_NICKSERV_IDENTIFY;

        cmdName.setText(isNickServ ? command.body : command.getName());
        cmdDesc.setText(command.description);

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<IRCCommand> matchingItems = new ArrayList<>();

                if(constraint == null || constraint.length() == 0) {
                    results.values = mCommands;
                    results.count = mCommands.size();

                    mFilteredCommands.clear();
                    mFilteredCommands.addAll(mCommands);

                } else {
                    String pattern = constraint.toString().toLowerCase().trim();

                    for (IRCCommand command : mCommands) {
                        String commandName = command.getName();

                        if(commandName.startsWith(pattern) && !commandName.equals(pattern))
                            matchingItems.add(command);
                    }

                    if(matchingItems.size() == 0) {
                        mFilteredCommands.clear();
                        mFilteredCommands.addAll(matchingItems);

                        results.values = matchingItems;
                        results.count = matchingItems.size();
                    } else {
                        results.values = matchingItems;
                        results.count = matchingItems.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults.values != null) {
                    ArrayList<?> filteredObjects = (ArrayList<?>) filterResults.values;

                    CommandsListAdapter.this.mFilteredCommands.clear();

                    for (Object obj : filteredObjects) {
                        CommandsListAdapter.this.mFilteredCommands.add((IRCCommand) obj);
                    }

                    if (filterResults.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {

                if(!(resultValue instanceof IRCCommand)) {
                    return "";
                }

                int type = ((IRCCommand) resultValue).type;

                boolean isNickServ =
                        type == IRCBroadcastSignal.OUT_SVC_NICKSERV_REGISTER ||
                        type == IRCBroadcastSignal.OUT_SVC_NICKSERV_IDENTIFY;

                return isNickServ ? ((IRCCommand) resultValue).body : ((IRCCommand) resultValue).getName();
            }
        };
    }
}
