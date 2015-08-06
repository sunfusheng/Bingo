package com.sun.bingo.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sun.bingo.R;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.GroupImageView.GroupImageView;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<BingoEntity> mEntities;

    private UserEntity userEntity;
    private DisplayImageOptions userImageOptions;

    public RecyclerViewAdapter(Context context, List<BingoEntity> entities) {
        this.mContext = context;
        this.mEntities = entities;

        userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        userImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageOnLoading(R.drawable.ic_user)
                .showImageForEmptyUri(R.drawable.ic_user)
                .showImageOnFail(R.drawable.ic_user)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BingoEntity entity = mEntities.get(position);

        UserEntityUtil.setUserAvatarView(holder.civUserAvatar, userEntity.getUserAvatar());
        UserEntityUtil.setTextViewData(holder.tvNickName, userEntity.getNickName());

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

        holder.llRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(holder.llRootView, "translationZ", 20, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Toast.makeText(mContext, "Todo", Toast.LENGTH_SHORT).show();
//                        mContext.startActivity(new Intent(mContext, DetailActivity.class));
                    }
                });
                animator.start();
            }
        });

        holder.ivItemMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(v);
            }
        });
    }

    private void showPopMenu(View ancho) {
        PopupMenu popupMenu = new PopupMenu(mContext, ancho);
        popupMenu.getMenuInflater().inflate(R.menu.item_pop_menu, popupMenu.getMenu());
        //menu item的点击事件监听
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //switch判断
                return false;
            }
        });

        //通过反射获取MenuPopupHelper实例，然后设置setForceShowIcon为true
        try {
            Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e){}

        //显示弹出式菜单
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        @InjectView(R.id.ll_root_view)
        LinearLayout llRootView;
        @InjectView(R.id.giv_image_group)
        GroupImageView givImageGroup;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
