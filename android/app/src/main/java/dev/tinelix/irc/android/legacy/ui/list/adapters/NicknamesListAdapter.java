package dev.tinelix.irc.android.legacy.ui.list.adapters;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.holoexperience.library.menu.MenuItemAdapter;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class NicknamesListAdapter extends RecyclerView.Adapter<NicknamesListAdapter.Holder> {

    private final Context mContext;
    private ArrayList<ServerProfile.Nickname> mNicknames;
    private OnItemChangeListener mListener;

    public NicknamesListAdapter(Context context, ArrayList<ServerProfile.Nickname> nicknames) {
        mContext = context;
        mNicknames = nicknames;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) View.inflate(mContext, R.layout.list_item_nickname, null);
        layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)(48 * mContext.getResources().getDisplayMetrics().scaledDensity)
                )
        );
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNicknames.size();
    }

    public void setArray(ArrayList<ServerProfile.Nickname> nicknames) {
        mNicknames = nicknames;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView nameTv;
        private final ImageButton deleteBtn;

        public Holder(View view) {
            super(view);
            this.view = view;
            this.nameTv = view.findViewById(R.id.profile_item_label);
            this.deleteBtn = view.findViewById(R.id.delete_btn);
        }

        public void bind(final int position) {
            final ServerProfile.Nickname nick = getItem(position);
            nameTv.setText(nick.nickname);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsersCacheDatabase.deleteNickname(nick);
                    mNicknames.remove(position);
                    notifyItemRemoved(position);

                    if(mListener != null)
                        mListener.onItemChange(position);
                }
            });
        }
    }

    private ServerProfile.Nickname getItem(int position) {
        return mNicknames.get(position);
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnItemChangeListener {
        public void onItemChange(int position);
    }
}
