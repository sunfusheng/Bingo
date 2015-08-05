// Generated code from Butter Knife. Do not modify!
package com.sun.bingo.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecyclerViewAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.sun.bingo.adapter.RecyclerViewAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624078, "field 'civUserAvatar'");
    target.civUserAvatar = (com.github.siyamed.shapeimageview.CircularImageView) view;
    view = finder.findRequiredView(source, 2131624081, "field 'tvNickName'");
    target.tvNickName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624121, "field 'tvTime'");
    target.tvTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624122, "field 'ivItemMore'");
    target.ivItemMore = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131624120, "field 'llIcons'");
    target.llIcons = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624123, "field 'tvTitle'");
    target.tvTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624124, "field 'tvContent'");
    target.tvContent = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131624071, "field 'llRootView'");
    target.llRootView = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624125, "field 'givImageGroup'");
    target.givImageGroup = (com.sun.bingo.widget.GroupImageView.GroupImageView) view;
  }

  public static void reset(com.sun.bingo.adapter.RecyclerViewAdapter.ViewHolder target) {
    target.civUserAvatar = null;
    target.tvNickName = null;
    target.tvTime = null;
    target.ivItemMore = null;
    target.llIcons = null;
    target.tvTitle = null;
    target.tvContent = null;
    target.llRootView = null;
    target.givImageGroup = null;
  }
}
