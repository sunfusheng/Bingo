// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProfileActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.ProfileActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624058, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624078, "field 'civUserAvatar'");
    target.civUserAvatar = (com.sun.bingo.widget.UploadAvatarView) view;
    view = finder.findRequiredView(source, 2131624077, "field 'rlUserAvatar'");
    target.rlUserAvatar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624083, "field 'tvUserSignTitle'");
    target.tvUserSignTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624084, "field 'tvUserSign'");
    target.tvUserSign = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624082, "field 'rlUserSign'");
    target.rlUserSign = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624080, "field 'tvNickNameTitle'");
    target.tvNickNameTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624081, "field 'tvNickName'");
    target.tvNickName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624079, "field 'rlNickName'");
    target.rlNickName = (android.widget.RelativeLayout) view;
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
  }
}
