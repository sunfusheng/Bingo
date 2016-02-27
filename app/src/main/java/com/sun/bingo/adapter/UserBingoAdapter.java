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
import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/*BingoB
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
        @Bind(R.id.civ_user_avatar)
        CircularImageView civUserAvatar;
        @Bind(R.id.tv_nick_name)
        TextView tvNickName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_item_more)
        ImageView ivItemMore;
        @Bind(R.id.ll_icons)
        LinearLayout llIcons;
        @Bind(R.id.tv_describe)
        TextView tvDescribe;
        @Bind(R.id.img01)
        ImageView img01;
        @Bind(R.id.img02)
        ImageView img02;
        @Bind(R.id.img03)
        ImageView img03;
        @Bind(R.id.img04)
        ImageView img04;
        @Bind(R.id.img05)
        ImageView img05;
        @Bind(R.id.img06)
        ImageView img06;
        @Bind(R.id.img07)
        ImageView img07;
        @Bind(R.id.img08)
        ImageView img08;
        @Bind(R.id.img09)
        ImageView img09;
        @Bind(R.id.giv_image_group)
        GroupImageView givImageGroup;
        @Bind(R.id.ll_root_view)
        LinearLayout llRootView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
