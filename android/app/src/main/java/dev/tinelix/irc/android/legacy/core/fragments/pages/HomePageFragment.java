package dev.tinelix.irc.android.legacy.core.fragments.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;

public class ProfilesListFragment extends BaseFragment {
    private RecyclerView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout layout = new LinearLayout(getContext());

        layout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        mListView = new RecyclerView(getContext());
        mListView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));

        layout.addView(mListView);

        return layout;
    }

    public void loadProfilesList() {

    }
}
