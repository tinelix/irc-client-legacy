package dev.tinelix.irc.android.legacy.ui.list.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class NicknamesListAdapter extends RecyclerView.Adapter<NicknamesListAdapter.Holder> {

    private final Context mContext;
    private final ArrayList<ServerProfile.Nickname> mNicknames;

    public NicknamesListAdapter(Context context, ArrayList<ServerProfile.Nickname> nicknames) {
        mContext = context;
        mNicknames = nicknames;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(View.inflate(mContext, R.layout.list_item_nickname, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView nameTv;

        public Holder(View view) {
            super(view);
            this.view = view;
            this.nameTv = view.findViewById(R.id.profile_item_label);
        }

        public void bind(int position) {
            nameTv.setText(getItem(position).nickname);
        }
    }

    private ServerProfile.Nickname getItem(int position) {
        return mNicknames.get(position);
    }
}
