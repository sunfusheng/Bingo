// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DetailActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.DetailActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624057, "field 'backdrop'");
    target.backdrop = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624058, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624056, "field 'collapsingToolbar'");
    target.collapsingToolbar = (android.support.design.widget.CollapsingToolbarLayout) view;
    view = finder.findRequiredView(source, 2131624055, "field 'appbar'");
    target.appbar = (android.support.design.widget.AppBarLayout) view;
    view = finder.findRequiredView(source, 2131624060, "field 'recyclerView'");
    target.recyclerView = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131624059, "field 'circleRefreshLayout'");
    target.circleRefreshLayout = (com.sun.bingo.widget.CircleRefreshLayout) view;
    view = finder.findRequiredView(source, 2131624061, "field 'progressWheel'");
    target.progressWheel = (com.sun.bingo.widget.ProgressWheel) view;
    view = finder.findRequiredView(source, 2131624054, "field 'mainContent'");
    target.mainContent = (android.support.design.widget.CoordinatorLayout) view;
  }

  public static void reset(com.sun.bingo.ui.activity.DetailActivity target) {
    target.backdrop = null;
    target.toolbar = null;
    target.collapsingToolbar = null;
    target.appbar = null;
    target.recyclerView = null;
    target.circleRefreshLayout = null;
    target.progressWheel = null;
    target.mainContent = null;
  }
}
