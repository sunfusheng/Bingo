// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecyclerViewAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.adapter.RecyclerViewAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624090, "field 'civUserAvatar'");
    target.civUserAvatar = (com.github.siyamed.shapeimageview.CircularImageView) view;
    view = finder.findRequiredView(source, 2131624093, "field 'tvNickName'");
    target.tvNickName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624133, "field 'tvTime'");
    target.tvTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624134, "field 'ivItemMore'");
    target.ivItemMore = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624132, "field 'llIcons'");
    target.llIcons = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624135, "field 'tvDescribe'");
    target.tvDescribe = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624083, "field 'llRootView'");
    target.llRootView = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624136, "field 'givImageGroup'");
    target.givImageGroup = (com.sun.bingo.widget.GroupImageView.GroupImageView) view;
  }

  public static void reset(com.sun.bingo.adapter.RecyclerViewAdapter.ViewHolder target) {
    target.civUserAvatar = null;
    target.tvNickName = null;
    target.tvTime = null;
    target.ivItemMore = null;
    target.llIcons = null;
    target.tvDescribe = null;
    target.llRootView = null;
    target.givImageGroup = null;
  }
}
