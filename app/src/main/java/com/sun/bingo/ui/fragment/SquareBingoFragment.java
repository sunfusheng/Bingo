package com.sun.bingo.ui.fragment;

import com.sun.bingo.R;
import com.sun.bingo.control.PageControl;

public class SquareBingoFragment extends BaseListFragment<PageControl> {

    @Override
    protected void onRefreshStart() {
        mControl.getSquareBingoListData(getActivity());
    }

    @Override
    protected void onScrollLast() {
        mControl.getSquareBingoListDataMore(getActivity());
    }

    @Override
    protected int emptyDataString() {
        return R.string.no_data;
    }
}
