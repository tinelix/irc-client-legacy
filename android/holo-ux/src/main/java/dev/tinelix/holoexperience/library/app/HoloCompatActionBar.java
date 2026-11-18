/*
 *  Copyright (C) 2013 The Android Open Source Project
 *  Copyright (C) 2026 Dmitry Tretyakov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package dev.tinelix.holoexperience.library.wrappers;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.tinelix.holoexperience.library.R;
import dev.tinelix.holoexperience.library.app.HoloCompatActivity;
import dev.tinelix.holoexperience.library.widget.HoloCompatActionBar;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ActionBarWrapper {

    private final Context mContext;
    private HoloCompatActionBar mActionBar;
    private ActionBar mActionBarHoneycomb;

    public ActionBarWrapper(Context context) {
        mContext = context;

        float dp = context.getResources().getDisplayMetrics().scaledDensity;

        if(context instanceof HoloCompatActivity) {

            HoloCompatActivity activity = (HoloCompatActivity) context;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mActionBarHoneycomb = activity.getActionBar();
            else {
                mActionBar = new HoloCompatActionBar(mContext);
                mActionBar.setId(R.id.action_bar);
                activity.setActionBar(mActionBar);
            }
        }
    }

    public void setTitle(int resId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mActionBarHoneycomb.setTitle(resId);
        else if(mActionBar != null)
            mActionBar.setTitle(resId);
    }

    public void setTitle(CharSequence title) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mActionBarHoneycomb.setTitle(title);
        else if(mActionBar != null)
            mActionBar.setTitle(title);
    }
}
