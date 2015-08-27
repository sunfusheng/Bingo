// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseFragment$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.fragment.BaseFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624066, "field 'recyclerView'");
    target.recyclerView = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131624065, "field 'circleRefreshLayout'");
    target.circleRefreshLayout = (com.sun.bingo.widget.CircleRefreshLayout) view;
    view = finder.findRequiredView(source, 2131624067, "field 'googleProgressBar'");
    target.googleProgressBar = (com.jpardogo.android.googleprogressbar.library.GoogleProgressBar) view;
  }

  public static void reset(com.sun.bingo.ui.fragment.BaseFragment target) {
    target.recyclerView = null;
    target.circleRefreshLayout = null;
    target.googleProgressBar = null;
  }
}
