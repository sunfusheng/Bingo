package com.sun.bingo.ui.fragment;

import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.control.PageControl;

public class FavoriteFragment extends BaseListFragment<PageControl> {

    @Override
    protected void onRefreshStart() {
        mAdapter.setHandleType(RecyclerViewAdapter.HANDLE_CANCEL_FAVORITE);
        mControl.getFavoriteBingoListData(getActivity());
    }

    @Override
    protected void onScrollLast() {
        mControl.getFavoriteBingoListDataMore(getActivity());
    }

    @Override
    protected int emptyDataString() {
        return R.string.no_favorite_data;
    }
}
