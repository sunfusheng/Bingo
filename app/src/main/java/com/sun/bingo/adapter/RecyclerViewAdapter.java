package com.sun.bingo.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.dialog.ToastTip;
import com.sun.bingo.R;
import com.sun.bingo.control.BmobControl;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.manager.ImageManager;
import com.sun.bingo.model.BingoEntity;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.ui.activity.MainActivity;
import com.sun.bingo.ui.activity.UserInfoActivity;
import com.sun.bingo.util.NetWorkUtil;
import com.sun.bingo.util.ShareUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BingoEntity> mEntities;
    private UserEntity mineEntity;

    private int type = HANDLE_NORMAL;
    public static final int HANDLE_NORMAL = 0;
    public static final int HANDLE_CANCEL_FAVORITE = 1;
    private static final int TYPE_LIST = 0;
    private static final int TYPE_FOOT_VIEW = 1;

    private ImageManager mImageManager;
    private BmobControl mControl;

    private View footView;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public RecyclerViewAdapter(Context context, List<BingoEntity> entities) {
        this(context);
        this.mEntities = entities;
        mineEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        mImageManager = new ImageManager(context);
        mControl = new BmobControl(context, mineEntity);
    }

    public void setHandleType(int type) {
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return mEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOT_VIEW;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TYPE_LIST:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_layout, parent, false);
                viewHolder = new ListViewHolder(view);
                break;
            case TYPE_FOOT_VIEW:
                footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footview_layout, parent, false);
                footView.setVisibility(View.GONE);
                viewHolder = new FootViewHolder(footView);
                break;
            default:
                viewHolder = new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_layout, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            final ListViewHolder viewHolder = (ListViewHolder) holder;
            final BingoEntity entity = mEntities.get(position);
            final UserEntity userEntity = entity.getUserEntity();
            final int mPosition = position;

            if (userEntity != null && !TextUtils.isEmpty(userEntity.getUserAvatar())) {
                mImageManager.loadCircleImage(userEntity.getUserAvatar(), viewHolder.ivUserAvatar);
            } else {
                mImageManager.loadCircleResImage(R.drawable.ic_user_avatar, viewHolder.ivUserAvatar);
            }

            if (TextUtils.isEmpty(entity.getTitle())) {
                viewHolder.tvTitle.setText(entity.getDescribe());
                viewHolder.tvDes.setVisibility(View.GONE);
            } else {
                viewHolder.tvTitle.setText(entity.getTitle());
                if (TextUtils.isEmpty(entity.getDescribe())) {
                    viewHolder.tvDes.setVisibility(View.GONE);
                } else {
                    viewHolder.tvDes.setVisibility(View.VISIBLE);
                    viewHolder.tvDes.setText(entity.getDescribe());
                }
            }

            if (entity.getFavoriteUserIds() != null && entity.getFavoriteUserIds().size() > 0) {
                viewHolder.tvFavoriteCount.setVisibility(View.VISIBLE);
                viewHolder.tvFavoriteCount.setText(entity.getFavoriteUserIds().size() + "");
                if (mineEntity.getFavoriteList() != null && mineEntity.getFavoriteList().indexOf(entity.getObjectId()) >= 0) {
                    viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    viewHolder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);
                }
            } else {
                viewHolder.tvFavoriteCount.setVisibility(View.GONE);
                viewHolder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);
            }

            if (entity.getImageList() != null && entity.getImageList().size() > 0) {
                viewHolder.ivImageCover.setVisibility(View.VISIBLE);
                mImageManager.loadUrlImage(entity.getImageList().get(0), viewHolder.ivImageCover);
            } else {
                viewHolder.ivImageCover.setVisibility(View.GONE);
            }

            viewHolder.ivUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getUserEntity() != null) {
                        NavigateManager.gotoUserInfoActivity(mContext, entity.getUserEntity());
                    }
                }
            });

            viewHolder.llFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mControl.favoriteBingo(entity, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                }
            });

            viewHolder.rlRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtil.isLinkAvailable(entity.getWebsite())) {
                        NavigateManager.gotoBingoDetailActivity(mContext, entity);
                    }
                }
            });
        }
    }

    public void setLoadMoreViewText(String text) {
        if (footView == null) return;
        ((TextView) ButterKnife.findById(footView, R.id.tv_loading_more)).setText(text);
        notifyItemChanged(getItemCount());
    }

    public void setLoadMoreViewVisibility(int visibility) {
        if (footView == null) return;
        footView.setVisibility(visibility);
        notifyItemChanged(getItemCount());
    }

    public boolean isLoadMoreShown() {
        if (footView == null) return false;
        return footView.isShown();
    }

    public String getLoadMoreViewText() {
        if (footView == null) return "";
        return ((TextView) ButterKnife.findById(footView, R.id.tv_loading_more)).getText().toString();
    }

    /**
     * 弹出菜单
     */
    private void showPopMenu(View ancho, final int position) {
        mineEntity = BmobUser.getCurrentUser(mContext, UserEntity.class);
        final BingoEntity entity = mEntities.get(position);
        List<String> favoriteList = mineEntity.getFavoriteList();

        PopupMenu popupMenu = new PopupMenu(mContext, ancho);
        popupMenu.getMenuInflater().inflate(R.menu.item_pop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pop_favorite:
                        return true;
                    case R.id.pop_share_sina:
                        if (mContext instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) mContext;
                            mainActivity.sendMultiMessageToSina(entity);
                        } else if (mContext instanceof UserInfoActivity) {
                            UserInfoActivity userInfoActivity = (UserInfoActivity) mContext;
                            userInfoActivity.sendMultiMessageToSina(entity);
                        }
                        break;
                    case R.id.pop_share:
                        ShareUtil.share(mContext, entity.getDescribe() + entity.getWebsite() + "\n[来自" + mContext.getString(R.string.app_name) + "的分享，下载地址：https://fir.im/Bingo]");
                        return true;
                    case R.id.pop_delete:
                        new MaterialDialog.Builder(mContext)
                                .content("确认删除该Bingo么？")
                                .contentColor(mContext.getResources().getColor(R.color.font_black_3))
                                .positiveText(R.string.ok)
                                .negativeText(R.string.cancel)
                                .negativeColor(mContext.getResources().getColor(R.color.font_black_3))
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        deleteMyBingo(position);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                    }
                                })
                                .show();
                        break;
                }
                return false;
            }
        });
        if (type == HANDLE_CANCEL_FAVORITE || (favoriteList != null && favoriteList.indexOf(entity.getObjectId()) >= 0)) {
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.pop_favorite);
            menuItem.setTitle("取消收藏");
        }
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.pop_delete);
        if (entity.getUserId().equals(mineEntity.getObjectId())) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        popupMenu.show();
    }

    /**
     * 删除我的Bingo
     */
    private void deleteMyBingo(final int position) {
        BingoEntity entity = mEntities.get(position);
        entity.delete(mContext, new DeleteListener() {
            @Override
            public void onSuccess() {
                ToastTip.show("删除成功");
                mEntities.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show("删除失败");
            }
        });
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_image_cover)
        ImageView ivImageCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @Bind(R.id.ll_title_user)
        RelativeLayout llTitleUser;
        @Bind(R.id.tv_des)
        TextView tvDes;
        @Bind(R.id.tv_tag_from)
        TextView tvTagFrom;
        @Bind(R.id.tv_favorite_count)
        TextView tvFavoriteCount;
        @Bind(R.id.iv_favorite)
        ImageView ivFavorite;
        @Bind(R.id.rl_root_view)
        RelativeLayout rlRootView;
        @Bind(R.id.ll_favorite)
        LinearLayout llFavorite;

        public ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_loading_more)
        TextView tvLoadingMore;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
