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

package dev.tinelix.irc.android.legacy.ui.transforms;

import android.graphics.Rect;
import android.text.GetChars;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class NickServPasswordTransform extends HideReturnsTransformationMethod {

    private PasswordCharSequence mSequence;

    public boolean checkNickServConditions() {
        String str = mSequence.toString();

        boolean value = (str.startsWith("/ns id ") ||
                str.startsWith("/ns identify ") ||
                str.startsWith("/nickserv id ") ||
                str.startsWith("/nickserv identify ") ||
                str.startsWith("/msg nickserv id ") ||
                str.startsWith("/msg nickserv identify ")
        ) && !str.endsWith(" ");

        if (value) {
            String[] words = str.split(" ", 4);

            if (words.length == 4)
                mSequence.setOffset(str.indexOf(words[3]));
            else {
                if (str.startsWith("/ns id"))
                    mSequence.setOffset("/ns id ".length());

                else if (str.startsWith("/ns identify"))
                    mSequence.setOffset("/ns identify ".length());

                else if (str.startsWith("/nickserv id"))
                    mSequence.setOffset("/nickserv id ".length());

                else if (str.startsWith("/nickserv identify"))
                    mSequence.setOffset("/nickserv identify ".length());

                else if (str.startsWith("/msg nickserv id"))
                    mSequence.setOffset("/msg nickserv id ".length());

                else if (str.startsWith("/msg nickserv identify"))
                    mSequence.setOffset("/msg nickserv identify ".length());
            }

        } else
            mSequence.setOffset(-1);

        return value;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText,
                               boolean focused, int direction, Rect previouslyFocusedRect) {

    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {

        if(mSequence == null)
            mSequence = new PasswordCharSequence(source);
        else
            mSequence.setSource(source);

        return mSequence;
    }

    public void setOffset(int offset) {
        if (mSequence != null)
            mSequence.setOffset(offset);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        private int mOffset = -1;

        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        public void setSource(CharSequence source) {
            mSource = source;
        }

        public void setOffset(int offset) {
            mOffset = offset;
        }

        @Override
        public char charAt(int index) {
            if (index < 0) return ' ';

            if(mSource == null) return '\0';

            char c = mSource.charAt(index);

            if (c == '\r' || c == '\n')
                return c;
            else if (index >= mOffset && mOffset >= 0)
                return '*';
            else
                return mSource.charAt(index);
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }

        @Override
        public String toString() {
            return mSource.toString();
        }
    }
}
