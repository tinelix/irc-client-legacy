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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.holoexperience.library.menu.MenuItemAdapter;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.MainActivity;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class ProfilesListAdapter extends RecyclerView.Adapter<ProfilesListAdapter.Holder> {

    private final Context mContext;
    private final ArrayList<ServerProfile> mProfiles;
    private final BaseActivity mActivity;
    private OnItemChangeListener mListener;

    public ProfilesListAdapter(Context context,
                               BaseActivity activity,
                               ArrayList<ServerProfile> profiles) {
        mContext = context;
        mActivity = activity;
        mProfiles = profiles;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.list_item_profile, null);
        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView nameTv;
        private final TextView serverTv;
        private final ImageButton menuBtn;
        private final ImageButton connectBtn;
        private ListPopupWindow mPopupWindow;
        private PopupMenu mPopupMenu;

        public Holder(View view) {
            super(view);
            this.view = view;
            this.nameTv = view.findViewById(R.id.profile_item_label);
            this.serverTv = view.findViewById(R.id.profile_server_label);
            this.menuBtn = view.findViewById(R.id.menu_btn);
            this.connectBtn = view.findViewById(R.id.connect_btn);
        }

        public void bind(int position) {
            final ServerProfile profile = getItem(position);

            final ListPopupWindow window = createPopupMenu(position);

            nameTv.setText(profile.fullName);
            serverTv.setText(
                    String.format(
                            "%s:%s",
                            profile.getServerParams().address,
                            Integer.toString(profile.getServerParams().port)
                    )
            );

            connectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mActivity instanceof MainActivity) {
                        ((MainActivity) mActivity).startService(profile);
                    }
                }
            });

            if(window != null) {
                menuBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!window.isShowing())
                                    window.show();
                                else
                                    window.dismiss();
                            }
                        }
                );
            }
        }

        private ListPopupWindow createPopupMenu(final int itemPos) {
            final ListPopupWindow popupWindow = new ListPopupWindow(mContext);
            popupWindow.setAnchorView(menuBtn);
            final PopupMenu menu = new PopupMenu(mContext, menuBtn);
            menu.getMenuInflater().inflate(R.menu.profile_item, menu.getMenu());

            ArrayList<String> items = new ArrayList<>();
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

            if (wm == null)
                return null;

            for (int i = 0; i < menu.getMenu().size(); i++) {
                items.add(menu.getMenu().getItem(i).getTitle().toString());
            }

            MenuItemAdapter adapter = new MenuItemAdapter(mContext, items);

            wm.getDefaultDisplay().getMetrics(metrics);

            popupWindow.setBackgroundDrawable(
                    mContext.getResources().getDrawable(
                            dev.tinelix.holoexperience.library.R.drawable.holo4ux_dark_dialog_full
                    )
            );
            popupWindow.setAdapter(adapter);
            popupWindow.setWidth((int) (168 * metrics.scaledDensity));

            if (popupWindow.getListView() != null) {
                popupWindow.getListView().setCacheColorHint(
                        mContext.getResources().getColor(android.R.color.transparent)
                );
            }

            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onMenuItemSelected(menu.getMenu().getItem(position), itemPos);
                    popupWindow.dismiss();
                }
            });
            return popupWindow;
        }

        private void onMenuItemSelected(MenuItem item, int itemPos) {
            ServerProfile profile = getItem(itemPos);

            switch (item.getItemId()) {
                case R.id.profile_menu_edit:
                    IRCProfileManager.startProfileSettingsActivity(mContext, profile);
                    break;
                case R.id.profile_menu_delete:
                    IRCProfileManager.deleteProfile(mContext, profile);
                    mProfiles.remove(itemPos);
                    notifyItemRemoved(itemPos);

                    if(mListener != null)
                        mListener.onItemChange(itemPos);
                    break;
            }
        }
    }

    private ServerProfile getItem(int position) {
        return mProfiles.get(position);
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnItemChangeListener {
        public void onItemChange(int position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
