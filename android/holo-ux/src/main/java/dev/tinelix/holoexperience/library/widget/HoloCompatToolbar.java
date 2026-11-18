package dev.tinelix.holoexperience.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.tinelix.holoexperience.library.R;
import dev.tinelix.holoexperience.library.app.HoloCompatActivity;

public class HoloCompatActionBar extends LinearLayout {

    public HoloCompatActionBar(Context context) {
        super(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.holo4ux_layout_actionbar, null);

        this.addView(view);

        view.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)(50 * getContext().getResources().getDisplayMetrics().scaledDensity)
        ));

        setLogo(context.getApplicationInfo().icon);

        if(context instanceof HoloCompatActivity)
            setTitle(((HoloCompatActivity) context).getTitle());

        findViewById(android.R.id.text2).setVisibility(GONE);
        findViewById(R.id.home_button).setVisibility(INVISIBLE);
    }

    public HoloCompatActionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context)
                .inflate(R.layout.holo4ux_layout_actionbar, null);

        this.addView(view);

        setLogo(context.getApplicationInfo().icon);
        if(context instanceof HoloCompatActivity)
            setTitle(((HoloCompatActivity) context).getTitle());

        findViewById(android.R.id.text2).setVisibility(GONE);
    }

    public void setLogo(int drawableRes) {
        ImageView view = findViewById(android.R.id.icon);
        view.setImageDrawable(
                getContext().getResources().getDrawable(drawableRes)
        );
    }

    public void setTitle(int stringRes) {
        TextView tv = findViewById(android.R.id.text1);
        tv.setText(stringRes);
    }

    public void setTitle(CharSequence sequence) {
        TextView tv = findViewById(android.R.id.text1);
        tv.setText(sequence);
    }

    public void setSubtitle(int stringRes) {
        TextView tv = findViewById(android.R.id.text2);
        tv.setText(stringRes);
    }

    public void setSubtitle(CharSequence sequence) {
        TextView tv = findViewById(android.R.id.text2);
        tv.setText(sequence);
    }
}
