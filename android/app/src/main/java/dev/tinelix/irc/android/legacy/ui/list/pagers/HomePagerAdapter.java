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

package dev.tinelix.irc.android.legacy.ui.list.pagers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.core.fragments.pages.HomePageFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final ArrayList<BaseFragment> mFragments;

    public HomePagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        mFragments = new ArrayList<>();

        mFragments.add(HomePageFragment.createInstance(0));
        mFragments.add(HomePageFragment.createInstance(1));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getStringArray(R.array.home_pages)[position];
    }

    public void createListAdapter(int position) {

        if(position != 0) return;

        Fragment fragment = getItem(position);

        if(fragment != null && fragment instanceof BaseFragment) {
            if(((BaseFragment) fragment).getItemCount() == 0)
                ((BaseFragment) fragment).createAdapter(mContext);

            mFragments.set(position, (BaseFragment) fragment);
        }
    }

    public int getListItemCount() {
        int itemCount = 0;

        for (BaseFragment fragment : mFragments) {
            if(fragment != null)
                itemCount += fragment.getItemCount();
        }

        return itemCount;
    }
}
