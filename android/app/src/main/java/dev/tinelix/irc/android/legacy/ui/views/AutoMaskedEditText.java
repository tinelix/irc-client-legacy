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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.holoexperience.library.widget.HoloAutoCompleteEditText;
import dev.tinelix.irc.android.legacy.ui.list.adapters.CommandsListAdapter;
import dev.tinelix.irc.android.legacy.ui.transforms.NickServPasswordTransform;

@SuppressLint("AppCompatCustomView")
public class AutoMaskedEditText extends HoloAutoCompleteEditText implements TextWatcher {

    private NickServPasswordTransform mTransfrom;
    public boolean mIsNickServ;
    private ViewGroup mParent;

    public AutoMaskedEditText(Context context) {
        super(context);
        mTransfrom = new NickServPasswordTransform();
        setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );

        float dp = getContext().getResources().getDisplayMetrics().scaledDensity;

        final int usableWidth = HoloCompatSupport.isTablet(getContext()) ?
                (int)(400 * dp) :
                context.getResources().getDisplayMetrics().widthPixels;

        setDropDownWidth(usableWidth);
        setDropDownHeight((int)(194 * dp));

        setMaxLines(!HoloCompatSupport.isTablet(getContext()) ? 6 : 3);
        setAdapter(CommandsListAdapter.createInstance(context));

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);

                setDropDownAnchor(getId());
                setThreshold(1);
            }
        });
        setThreshold(1);
    }

    public AutoMaskedEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );

        float dp = getContext().getResources().getDisplayMetrics().scaledDensity;

        final int usableWidth = HoloCompatSupport.isTablet(getContext()) ?
                (int)(400 * dp) :
                context.getResources().getDisplayMetrics().widthPixels;

        setDropDownWidth(usableWidth);
        setDropDownHeight((int)(194 * dp));

        setMaxLines(!HoloCompatSupport.isTablet(getContext()) ? 6 : 3);
        setAdapter(CommandsListAdapter.createInstance(context));

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);

                setDropDownAnchor(getId());
                setThreshold(1);
            }
        });
    }

    public void setParent(ViewGroup viewGroup) {
        mParent = viewGroup;
    }

    public void setMultiline(boolean value) {
        setText("");
        if(value) {
            setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
            );
        } else {
            setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            );
        }

        if(mTransfrom == null) {
            mTransfrom = new NickServPasswordTransform();
            setTransformationMethod(mTransfrom);
            addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(mTransfrom != null) {
            mIsNickServ = mTransfrom.checkNickServConditions();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*
         *  Encapsulating the onMeasure method fixes a bug related to the
         *  measurement of multiline text.
         *
         *  NOTE:
         *  The `paddingLR` variable is not linked to a dynamic variable.
         *  Therefore, if the padding is changed in the XML or the class, this
         *  variable must also be updated.
         */

        try {

            float dp = getResources().getDisplayMetrics().scaledDensity;
            float paddingLR = (28 * dp);
            int realWidth = HoloCompatSupport.isTablet(getContext()) ?
                    (int) (getMeasuredWidth() - paddingLR) : (int) (getMeasuredWidth() * 1.65);

            StaticLayout layout;

            if (realWidth > 0 && getText() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    layout = StaticLayout.Builder.obtain(
                            getText(), 0, getText().length(), getPaint(), realWidth
                    ).setAlignment(Layout.Alignment.ALIGN_NORMAL)
                            .setLineSpacing(0.0f, 1.0f)
                            .setIncludePad(true).build();
                } else {
                    layout = new StaticLayout(
                            getText(), getPaint(), realWidth, Layout.Alignment.ALIGN_NORMAL,
                            1.0f, 0.0f, true
                    );
                }
            } else {
                layout = (StaticLayout) getLayout();
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (layout == null) return;

            int lineCount = layout.getLineCount();
            int lineHeight = getLineHeight();
            int paddingTop = getCompoundPaddingTop();
            int paddingBottom = getCompoundPaddingBottom();

            int exactHeight = (lineHeight * lineCount) + paddingTop + paddingBottom;

            if (getMeasuredHeight() > exactHeight)
                setMeasuredDimension(getMeasuredWidth(), exactHeight);
        } catch (Exception e) {
            e.printStackTrace();
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }
}
