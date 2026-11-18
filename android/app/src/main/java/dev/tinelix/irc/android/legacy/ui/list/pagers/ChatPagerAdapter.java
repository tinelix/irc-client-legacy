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
        Fragment fragment = getItem(position);

        if(fragment != null && fragment instanceof BaseFragment) {
            if(((BaseFragment) fragment).getItemCount() == 0) {
                if(position == 0)
                    ((BaseFragment) fragment).createAdapter(mContext);
            }

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
