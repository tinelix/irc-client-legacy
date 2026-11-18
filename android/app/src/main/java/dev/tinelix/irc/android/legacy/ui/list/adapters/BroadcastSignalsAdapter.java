package dev.tinelix.irc.android.legacy.ui.list.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChannelMembersActivity;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.legacy.core.receivers.IRCBroadcastReceiver;
import dev.tinelix.irc.android.legacy.typefaces.DefaultFontFamily;
import dev.tinelix.irc.android.legacy.typefaces.RobotoMono;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;

public class BroadcastSignalsAdapter extends RecyclerView.Adapter<BroadcastSignalsAdapter.Holder> {

    private final Context mContext;
    private ArrayList<IRCBroadcastSignal> mSignals;
    private float mFontSize = 14.0f;
    private float mSmallFontSize = 9.5f;
    private boolean mSmallFontSizeRequired = false;
    private DefaultFontFamily mMonoTypeface;

    public BroadcastSignalsAdapter(Context context, ArrayList<IRCBroadcastSignal> signals) {
        mContext = context;
        mSignals = signals;

        if(mContext.getResources().getDisplayMetrics().xdpi < 340) {
            mSmallFontSize = 8.8f;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mMonoTypeface = new RobotoMono(mContext);
            mSmallFontSize = 11.0f;
        }
    }

    @Override
    public BroadcastSignalsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = viewType == IRCBroadcastSignal.IN_JOIN || viewType == IRCBroadcastSignal.IN_PART ?
                        View.inflate(mContext, R.layout.list_item_irc_event, null) :
                        View.inflate(mContext, R.layout.list_item_irc_signal, null);

        view.setLayoutParams(
                new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        );
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(BroadcastSignalsAdapter.Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSignals.size();
    }

    public void setArray(ArrayList<IRCBroadcastSignal> array) {
        mSignals = array;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final TextView msgTextTv;
        private final TextView authorTv;
        private final LinearLayout infoLayout;
        private final TextView badgeTv;
        private final TextView dateTv;
        private final View divider;
        private final ImageView iconIv;
        private boolean mMOTDScrollRequired;

        Holder(View view) {
            super(view);
            msgTextTv = view.findViewById(android.R.id.text1);
            authorTv = view.findViewById(R.id.author);
            badgeTv = view.findViewById(R.id.category_badge);
            dateTv = view.findViewById(R.id.timestamp);
            infoLayout = view.findViewById(R.id.info_layout);
            divider = view.findViewById(R.id.divider);
            iconIv = view.findViewById(R.id.icon);
        }

        void bind(int position) {
            Resources res = mContext.getResources();

            setLayoutVisibillity(position);

            final IRCBroadcastSignal signal = getItem(position);

            msgTextTv.setText(
                    signal.getLocalizedSpannedBody(mContext, res.getColor(R.color.holo_blue_light))
            );
            msgTextTv.setAutoLinkMask(Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
            msgTextTv.setMovementMethod(LinkMovementMethod.getInstance());

            boolean isPortrait = res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

            itemView.setBackgroundColor(res.getColor(android.R.color.transparent));

            msgTextTv.setTextSize(mFontSize);

            if(mMonoTypeface != null)
                msgTextTv.setTypeface(mMonoTypeface.getTypeface(500, false));

            if (signal.type == IRCBroadcastSignal.IN_CMD_MOTD && isPortrait &&
                !HoloCompatSupport.isTablet(mContext)) {

                if(signal.body == null) {
                    infoLayout.setVisibility(View.GONE);
                    return;
                }

                for(String line : signal.body.split("\r\n")) {
                    if(line.length() > 35)
                        mSmallFontSizeRequired = true;

                    if(line.length() > 45)
                        mMOTDScrollRequired = true;
                }

                if (mSmallFontSizeRequired)
                    msgTextTv.setTextSize(mSmallFontSize);

                if(mMOTDScrollRequired) {
                    msgTextTv.setHorizontallyScrolling(true);
                    msgTextTv.setMovementMethod(new ScrollingMovementMethod());
                }

            } else if (signal.type == IRCBroadcastSignal.IN_NOTICE ||
                    signal.type == IRCBroadcastSignal.IN_CMD_TOPIC) {

                msgTextTv.setTextColor(res.getColor(android.R.color.white));
                msgTextTv.setLinkTextColor(res.getColor(R.color.white));
                itemView.setBackgroundColor(
                        res.getColor(dev.tinelix.holoexperience.library.R.color.holo_blue_twilight)
                );
                badgeTv.setBackgroundDrawable(res.getDrawable(R.drawable.badge_shape_dark_blue));
                badgeTv.setTextColor(res.getColor(R.color.white));

            } else if (signal.type == IRCBroadcastSignal.IN_JOIN) {

                iconIv.setImageDrawable(
                        res.getDrawable(R.drawable.ic_holo4ux_dark_accent_navigation_forward)
                );

                msgTextTv.setTextColor(res.getColor(android.R.color.white));
                msgTextTv.setLinkTextColor(res.getColor(R.color.white));
                itemView.setBackgroundColor(
                        res.getColor(dev.tinelix.holoexperience.library.R.color.holo_green_twilight)
                );
            } else if (IRCBroadcastSignal.isError(signal) ||
                       signal.type == IRCBroadcastSignal.IN_PART) {

                if(signal.type == IRCBroadcastSignal.IN_PART)
                    iconIv.setImageDrawable(
                            res.getDrawable(R.drawable.ic_holo4ux_dark_accent_navigation_back)
                    );

                msgTextTv.setTextColor(res.getColor(android.R.color.white));
                msgTextTv.setLinkTextColor(res.getColor(R.color.white));
                itemView.setBackgroundColor(
                        res.getColor(dev.tinelix.holoexperience.library.R.color.holo_red_twilight)
                );

                if(infoLayout != null) {
                    badgeTv.setBackgroundDrawable(res.getDrawable(R.drawable.badge_shape_red));
                    badgeTv.setTextColor(res.getColor(R.color.white));
                }
            }

            if(authorTv != null) {
                if(signal.type == IRCBroadcastSignal.IN_PRIVMSG ||
                   signal.type == IRCBroadcastSignal.OUT_CMD_PRIVMSG)
                        badgeTv.setVisibility(View.GONE);

                if (signal.sender != null &&
                        (signal.type == IRCBroadcastSignal.IN_NOTICE ||
                         signal.type == IRCBroadcastSignal.IN_PRIVMSG ||
                         signal.type == IRCBroadcastSignal.IN_CMD_TOPIC)) {

                    if(!HoloCompatSupport.isTablet(mContext))
                        authorTv.setVisibility(View.VISIBLE);

                    authorTv.setText(signal.sender.split("!", 2)[0]);
                } else {
                    authorTv.setVisibility(View.GONE);
                    authorTv.setText("");
                }
            }

            if(!signal.isIncoming)
                msgTextTv.setTextColor(res.getColor(R.color.holo_blue_bright));

            if(signal.date >= 0) {
                if (isSentYesterday(signal)) {
                    dateTv.setText(
                            new SimpleDateFormat(
                                    Locale.getDefault() == Locale.US ? "yyyy/MM/dd" : "dd.MM.yyyy",
                                    Locale.getDefault()
                            ).format(new Date(signal.date))
                    );
                } else {
                    dateTv.setText(
                            new SimpleDateFormat(
                                    Locale.getDefault() == Locale.US ? "h:mm:ss a" : "H:mm:ss",
                                    Locale.getDefault()
                            ).format(new Date(signal.date))
                    );
                }
            } else {
                dateTv.setVisibility(View.GONE);
            }

            if(signal.type == IRCBroadcastSignal.IN_CMD_CHANNEL_NAMES) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mContext instanceof ChatActivity)
                            ((ChatActivity) mContext).showMembersList(true, signal.destination);
                    }
                });

                msgTextTv.setFocusable(false);
                if(infoLayout != null)
                    infoLayout.setFocusable(false);
            }
        }

        private void setLayoutVisibillity(int position) {

            float dp = mContext.getResources().getDisplayMetrics().scaledDensity;

            IRCBroadcastSignal signal = getItem(position);

            if(getItemCount() <= 1) return;

            if(badgeTv == null || authorTv == null || dateTv == null || infoLayout == null)
                return;

            if(HoloCompatSupport.isTablet(mContext)) {

                LinearLayout layout = itemView.findViewById(R.id.signal_layout);

                if (position > 0 && position < getItemCount() - 1) {

                    IRCBroadcastSignal prevSignal = getItem(position - 1);
                    IRCBroadcastSignal nextSignal = getItem(position + 1);

                    boolean nextVisibility = checkSignalConditions(signal, nextSignal);
                    boolean prevVisibility = checkSignalConditions(signal, prevSignal);

                    infoLayout.setVisibility(
                            nextVisibility || prevVisibility ? View.VISIBLE : View.INVISIBLE
                    );

                    if(nextVisibility && prevVisibility) {

                        itemView.findViewById(R.id.signal_layout).setPadding(
                                layout.getPaddingLeft(), (int)(3 * dp),
                                layout.getPaddingRight(), (int)(3 * dp)
                        );

                        badgeTv.setVisibility(
                                signal.type != IRCBroadcastSignal.IN_PRIVMSG ? View.VISIBLE : View.GONE
                        );
                        authorTv.setVisibility(View.VISIBLE);
                        dateTv.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);

                    } else if(prevVisibility) {

                        itemView.findViewById(R.id.signal_layout).setPadding(
                                layout.getPaddingLeft(), 0,
                                layout.getPaddingRight(), (int)(3 * dp)
                        );

                        badgeTv.setVisibility(View.GONE);
                        authorTv.setVisibility(View.GONE);
                        dateTv.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);

                    } else if(nextVisibility) {

                        itemView.findViewById(R.id.signal_layout).setPadding(
                                layout.getPaddingLeft(), (int)(3 * dp),
                                layout.getPaddingRight(), 0
                        );

                        badgeTv.setVisibility(
                                signal.type != IRCBroadcastSignal.IN_PRIVMSG ? View.VISIBLE : View.GONE
                        );
                        authorTv.setVisibility(View.VISIBLE);
                        dateTv.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);

                    } else {
                        badgeTv.setVisibility(View.GONE);
                        authorTv.setVisibility(View.GONE);
                        dateTv.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                    }

                } else if(position == getItemCount() - 1){
                    IRCBroadcastSignal nextSignal = getItem(position - 1);
                    boolean nextVisibility = checkSignalConditions(signal, nextSignal);

                    itemView.findViewById(R.id.signal_layout).setPadding(
                            layout.getPaddingLeft(), (int)(3 * dp),
                            layout.getPaddingRight(), nextVisibility ? (int)(3 * dp) : 0
                    );

                    infoLayout.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.GONE);
                    badgeTv.setVisibility(
                            signal.type != IRCBroadcastSignal.IN_PRIVMSG ? View.VISIBLE : View.GONE
                    );
                    authorTv.setVisibility(View.VISIBLE);
                    dateTv.setVisibility(nextVisibility ? View.VISIBLE : View.GONE);

                } else {
                    IRCBroadcastSignal prevSignal = getItem(position + 1);
                    boolean prevVisibility = checkSignalConditions(signal, prevSignal);

                    itemView.findViewById(R.id.signal_layout).setPadding(
                            layout.getPaddingLeft(), prevVisibility ? (int)(3 * dp) : 0,
                            layout.getPaddingRight(), 0
                    );


                    infoLayout.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.GONE);
                    badgeTv.setVisibility(
                            prevVisibility || signal.type != IRCBroadcastSignal.IN_PRIVMSG ?
                            View.VISIBLE : View.GONE
                    );
                    authorTv.setVisibility(prevVisibility ? View.VISIBLE : View.GONE);
                    dateTv.setVisibility(View.VISIBLE);
                }

            } else {
                if (position > 0) {
                    IRCBroadcastSignal prevSignal = getItem(position - 1);
                    boolean visibility = checkSignalConditions(signal, prevSignal);

                    divider.setVisibility(visibility ? View.VISIBLE : View.GONE);
                    badgeTv.setVisibility(visibility ? View.VISIBLE : View.GONE);
                    authorTv.setVisibility(visibility ? View.VISIBLE : View.GONE);
                    dateTv.setVisibility(visibility ? View.VISIBLE : View.GONE);
                    infoLayout.setVisibility(visibility ? View.VISIBLE : View.GONE);
                } else {
                    infoLayout.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.GONE);
                }
            }

            badgeTv.setText(IRCBroadcastSignal.getCategoryString(mContext, signal));
        }

        private boolean checkSignalConditions(
                IRCBroadcastSignal signal, IRCBroadcastSignal signal2
        ) {
            if(signal == null || signal2 == null)
                return false;

            String category = IRCBroadcastSignal.getCategoryString(mContext, signal);
            String prevCategory = IRCBroadcastSignal.getCategoryString(mContext, signal2);


            boolean categoryNotMatched = !category.equals(prevCategory);

            boolean senderNotMatched =
                    signal.sender != null && signal2.sender != null &&
                    !signal.sender.equals(signal2.sender);

            boolean isIncomingNotMatched = signal.isIncoming != signal2.isIncoming;

            boolean isLongTimeDiff = signal.date - signal2.date >= 600000;

            return categoryNotMatched || senderNotMatched || isIncomingNotMatched || isLongTimeDiff;
        }

        private boolean isSentYesterday(IRCBroadcastSignal signal) {
            return System.currentTimeMillis() - signal.date >= 86400000;
        }
    }

    public void setFontSize(float size) {
        mFontSize = size;
    }

    private IRCBroadcastSignal getItem(int position) {
        return mSignals.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }
}
