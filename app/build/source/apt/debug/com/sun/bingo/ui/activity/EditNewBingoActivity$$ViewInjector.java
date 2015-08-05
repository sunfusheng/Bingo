// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class EditNewBingoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.ui.activity.EditNewBingoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624058, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131624063, "field 'etTitle'");
    target.etTitle = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624066, "field 'etContent'");
    target.etContent = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131624070, "field 'tvCommit'");
    target.tvCommit = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624062, "field 'cvTitle'");
    target.cvTitle = (android.support.v7.widget.CardView) view;
    view = finder.findRequiredView(source, 2131624064, "field 'cvContent'");
    target.cvContent = (android.support.v7.widget.CardView) view;
    view = finder.findRequiredView(source, 2131624069, "field 'ivImage'");
    target.ivImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624067, "field 'rlBottomLayout'");
    target.rlBottomLayout = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131624068, "field 'llContainer'");
    target.llContainer = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624065, "field 'hsImages'");
    target.hsImages = (android.widget.HorizontalScrollView) view;
  }

  public static void reset(com.sun.bingo.ui.activity.EditNewBingoActivity target) {
    target.toolbar = null;
    target.etTitle = null;
    target.etContent = null;
    target.tvCommit = null;
    target.cvTitle = null;
    target.cvContent = null;
    target.ivImage = null;
    target.rlBottomLayout = null;
    target.llContainer = null;
    target.hsImages = null;
  }
}
