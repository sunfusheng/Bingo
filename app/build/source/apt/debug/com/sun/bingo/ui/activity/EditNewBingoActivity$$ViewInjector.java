// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class EditNewBingoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.EditNewBingoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624072, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624075, "field 'etWebsite'");
    target.etWebsite = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624074, "field 'cvWebsite'");
    target.cvWebsite = (android.support.v7.widget.CardView) view;
    view = finder.findRequiredView(source, 2131624078, "field 'etDescribe'");
    target.etDescribe = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624076, "field 'cvDescribe'");
    target.cvDescribe = (android.support.v7.widget.CardView) view;
    view = finder.findRequiredView(source, 2131624080, "field 'llContainer'");
    target.llContainer = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624077, "field 'hsImages'");
    target.hsImages = (android.widget.HorizontalScrollView) view;
    view = finder.findRequiredView(source, 2131624081, "field 'ivImage'");
    target.ivImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624082, "field 'tvCommit'");
    target.tvCommit = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624079, "field 'rlBottomLayout'");
    target.rlBottomLayout = (android.widget.RelativeLayout) view;
  }

  public static void reset(com.sun.bingo.ui.activity.EditNewBingoActivity target) {
    target.toolbar = null;
    target.etWebsite = null;
    target.cvWebsite = null;
    target.etDescribe = null;
    target.cvDescribe = null;
    target.llContainer = null;
    target.hsImages = null;
    target.ivImage = null;
    target.tvCommit = null;
    target.rlBottomLayout = null;
  }
}
