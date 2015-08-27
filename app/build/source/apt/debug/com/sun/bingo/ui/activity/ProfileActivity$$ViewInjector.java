// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProfileActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.ProfileActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624072, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624090, "field 'civUserAvatar'");
    target.civUserAvatar = (com.sun.bingo.widget.UploadAvatarView) view;
    view = finder.findRequiredView(source, 2131624089, "field 'rlUserAvatar'");
    target.rlUserAvatar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624095, "field 'tvUserSignTitle'");
    target.tvUserSignTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624096, "field 'tvUserSign'");
    target.tvUserSign = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624094, "field 'rlUserSign'");
    target.rlUserSign = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624092, "field 'tvNickNameTitle'");
    target.tvNickNameTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624093, "field 'tvNickName'");
    target.tvNickName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624091, "field 'rlNickName'");
    target.rlNickName = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624097, "field 'tvLogout'");
    target.tvLogout = (android.widget.TextView) view;
  }

  public static void reset(com.sun.bingo.ui.activity.ProfileActivity target) {
    target.toolbar = null;
    target.civUserAvatar = null;
    target.rlUserAvatar = null;
    target.tvUserSignTitle = null;
    target.tvUserSign = null;
    target.rlUserSign = null;
    target.tvNickNameTitle = null;
    target.tvNickName = null;
    target.rlNickName = null;
    target.tvLogout = null;
  }
}
