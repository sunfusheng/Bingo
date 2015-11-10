package com.sun.bingo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.sun.bingo.R;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.GroupImageView.GroupImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 15/11/10.
 */
public class UserBingoAdapter extends BaseListAdapter<BingoEntity> {

    private UserEntity userEntity;

    public UserBingoAdapter(Context context, List<BingoEntity> list) {
        super(context, list);
        userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_card_main, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BingoEntity entity = getItem(position);

        if (entity.getUserEntity() != null) {
            UserEntityUtil.setUserAvatarView(holder.civUserAvatar, entity.getUserEntity().getUserAvatar());
            UserEntityUtil.setTextViewData(holder.tvNickName, entity.getUserEntity().getNickName());
        }

        holder.tvDescribe.setText(entity.getDescribe());

        if (entity.getCreateTime() > 0) {
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(DateUtil.getDateStr(mContext, entity.getCreateTime()));
        } else {
            holder.tvTime.setVisibility(View.GONE);
        }

        if (entity.getImageList() != null && entity.getImageList().size() > 0) {
            holder.givImageGroup.setVisibility(View.VISIBLE);
            holder.givImageGroup.setPics(entity.getImageList());
        } else {
            holder.givImageGroup.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.civ_user_avatar)
        CircularImageView civUserAvatar;
        @InjectView(R.id.tv_nick_name)
        TextView tvNickName;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.iv_item_more)
        ImageView ivItemMore;
        @InjectView(R.id.ll_icons)
        LinearLayout llIcons;
        @InjectView(R.id.tv_describe)
        TextView tvDescribe;
        @InjectView(R.id.img01)
        ImageView img01;
        @InjectView(R.id.img02)
        ImageView img02;
        @InjectView(R.id.img03)
        ImageView img03;
        @InjectView(R.id.img04)
        ImageView img04;
        @InjectView(R.id.img05)
        ImageView img05;
        @InjectView(R.id.img06)
        ImageView img06;
        @InjectView(R.id.img07)
        ImageView img07;
        @InjectView(R.id.img08)
        ImageView img08;
        @InjectView(R.id.img09)
        ImageView img09;
        @InjectView(R.id.giv_image_group)
        GroupImageView givImageGroup;
        @InjectView(R.id.ll_root_view)
        LinearLayout llRootView;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
