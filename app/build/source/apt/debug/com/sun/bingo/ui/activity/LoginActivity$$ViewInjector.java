// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624058, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624072, "field 'metPhoneNum'");
    target.metPhoneNum = (com.rengwuxian.materialedittext.MaterialEditText) view;
    view = finder.findRequiredView(source, 2131624074, "field 'metVerifyCode'");
    target.metVerifyCode = (com.rengwuxian.materialedittext.MaterialEditText) view;
    view = finder.findRequiredView(source, 2131624073, "field 'tvVerifyCode'");
    target.tvVerifyCode = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624070, "field 'tvCommit'");
    target.tvCommit = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624071, "field 'llRootView'");
    target.llRootView = (android.widget.LinearLayout) view;
  }

  public static void reset(com.sun.bingo.ui.activity.LoginActivity target) {
    target.toolbar = null;
    target.metPhoneNum = null;
    target.metVerifyCode = null;
    target.tvVerifyCode = null;
    target.tvCommit = null;
    target.llRootView = null;
  }
}
