package com.sun.bingo.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.LoadingDialog;
import com.sun.bingo.framework.dialog.TipDialog;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.GroupImageView.GroupImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<BingoEntity> mEntities;
    private UserEntity userEntity;
    private int type = NORMAL;

    public static final int NORMAL = 0;
    public static final int CANCEL_FAVORITE = 1;

    public RecyclerViewAdapter(Context context, List<BingoEntity> entities) {
        this.mContext = context;
        this.mEntities = entities;
        userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
    }

    public RecyclerViewAdapter(Context context, List<BingoEntity> entities, int type) {
        this(context, entities);
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BingoEntity entity = mEntities.get(position);
        final int mPosition = position;

        UserEntityUtil.setUserAvatarView(holder.civUserAvatar, entity.getUserEntity().getUserAvatar());
        UserEntityUtil.setTextViewData(holder.tvNickName, entity.getUserEntity().getNickName());

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
                        NavigateManager.gotoBingoDetailActivity(mContext, entity);
                    }
                });
                animator.start();
            }
        });

        holder.ivItemMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(v, mPosition);
            }
        });
    }

    private void showPopMenu(View ancho, final int position) {
        userEntity = BmobUser.getCurrentUser(mContext, UserEntity.class);
        final BingoEntity entity = mEntities.get(position);
        List<String> favoriteList = userEntity.getFavoriteList();


        PopupMenu popupMenu = new PopupMenu(mContext, ancho);
        popupMenu.getMenuInflater().inflate(R.menu.item_pop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pop_favorite:
                        LoadingDialog.show(mContext);
                        if (item.getTitle().equals("取消收藏")) {
                            cancelFavoriteBingo(entity.getObjectId(), position);
                        } else {
                            handleFavoriteBingo(entity.getObjectId());
                        }
                        userEntity = BmobUser.getCurrentUser(mContext, UserEntity.class);
                        return true;
                    case R.id.pop_share:

                        return true;
                }
                return false;
            }
        });
        if (type == CANCEL_FAVORITE || (favoriteList != null && favoriteList.indexOf(entity.getObjectId()) >= 0)) {
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.pop_favorite);
            menuItem.setTitle("取消收藏");
        }
        popupMenu.show();
    }

    private void handleFavoriteBingo(String bingoId) {
        List<String> favoriteList = userEntity.getFavoriteList();
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
        if (favoriteList.indexOf(bingoId) >= 0) {
            TipDialog.showToastDialog(mContext, "您已收藏过了");
            return;
        }
        favoriteList.add(bingoId);
        userEntity.setFavoriteList(favoriteList);
        userEntity.update(mContext, userEntity.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                LoadingDialog.dismiss();
                TipDialog.showToastDialog(mContext, "收藏成功");
            }

            @Override
            public void onFailure(int i, String s) {
                LoadingDialog.dismiss();
                TipDialog.showToastDialog(mContext, "收藏失败");
            }
        });
    }

    private void cancelFavoriteBingo(String bingoId, final int position) {
        List<String> favoriteList = userEntity.getFavoriteList();
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
        if (favoriteList.indexOf(bingoId) < 0) {
            TipDialog.showToastDialog(mContext, "您已取消收藏了");
            return;
        }
        favoriteList.remove(bingoId);
        userEntity.setFavoriteList(favoriteList);
        userEntity.update(mContext, userEntity.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                LoadingDialog.dismiss();
                TipDialog.showToastDialog(mContext, "取消成功");
                mEntities.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                LoadingDialog.dismiss();
                TipDialog.showToastDialog(mContext, "取消失败");
            }
        });
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
