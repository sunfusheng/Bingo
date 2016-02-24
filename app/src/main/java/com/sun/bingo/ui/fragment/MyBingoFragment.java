package com.sun.bingo.ui.fragment;

import com.sun.bingo.R;
import com.sun.bingo.control.PageControl;

public class MyBingoFragment extends BaseListFragment<PageControl> {

    @Override
    protected void onRefreshStart() {
        mControl.getMyBingoListData(getActivity(), userEntity.getObjectId());
    }

    @Override
    protected void onScrollLast() {
        mControl.getMyBingoListDataMore(getActivity(), userEntity.getObjectId());
    }

    @Override
    protected int emptyDataString() {
        return R.string.no_my_bingo_data;
    }
}