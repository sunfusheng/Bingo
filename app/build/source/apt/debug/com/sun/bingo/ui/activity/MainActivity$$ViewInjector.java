// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624058, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624109, "field 'tabLayout'");
    target.tabLayout = (android.support.design.widget.TabLayout) view;
    view = finder.findRequiredView(source, 2131624108, "field 'appBarLayout'");
    target.appBarLayout = (android.support.design.widget.AppBarLayout) view;
    view = finder.findRequiredView(source, 2131624110, "field 'viewPager'");
    target.viewPager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131624107, "field 'mainCoordinatorLayout'");
    target.mainCoordinatorLayout = (android.support.design.widget.CoordinatorLayout) view;
    view = finder.findRequiredView(source, 2131624076, "field 'mainNavigationLayout'");
    target.mainNavigationLayout = (android.support.design.widget.NavigationView) view;
    view = finder.findRequiredView(source, 2131624075, "field 'mainDrawerLayout'");
    target.mainDrawerLayout = (android.support.v4.widget.DrawerLayout) view;
    view = finder.findRequiredView(source, 2131624111, "field 'floatingActionButton'");
    target.floatingActionButton = (com.shamanland.fab.FloatingActionButton) view;
  }

  public static void reset(com.sun.bingo.ui.activity.MainActivity target) {
    target.toolbar = null;
    target.tabLayout = null;
    target.appBarLayout = null;
    target.viewPager = null;
    target.mainCoordinatorLayout = null;
    target.mainNavigationLayout = null;
    target.mainDrawerLayout = null;
    target.floatingActionButton = null;
  }
}
