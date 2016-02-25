package com.sun.bingo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunfusheng on 16/2/25.
 */
public class SinaUserInfoEntity implements Serializable {

    private long id;
    private String idstr;
    private String screen_name;
    private String name;
    private String province;
    private String city;
    private String location;
    private String description;
    private String url;
    private String profile_image_url;
    private String profile_url;
    private String domain;
    private String weihao;
    private String gender;
    private int followers_count;
    private int friends_count;
    private int pagefriends_count;
    private int statuses_count;
    private int favourites_count;
    private String created_at;
    private boolean following;
    private boolean allow_all_act_msg;
    private boolean geo_enabled;
    private boolean verified;
    private int verified_type;
    private String remark;
    private StatusEntity status;
    private int ptype;
    private boolean allow_all_comment;
    private String avatar_large;
    private String avatar_hd;
    private String verified_reason;
    private String verified_trade;
    private String verified_reason_url;
    private String verified_source;
    private String verified_source_url;
    private boolean follow_me;
    private int online_status;
    private int bi_followers_count;
    private String lang;
    private int star;
    private int mbtype;
    private int mbrank;
    private int block_word;
    private int block_app;
    private int credit_score;
    private int user_ability;
    private int urank;

    public void setId(long id) {
        this.id = id;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setWeihao(String weihao) {
        this.weihao = weihao;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public void setPagefriends_count(int pagefriends_count) {
        this.pagefriends_count = pagefriends_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVerified_type(int verified_type) {
        this.verified_type = verified_type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public void setVerified_trade(String verified_trade) {
        this.verified_trade = verified_trade;
    }

    public void setVerified_reason_url(String verified_reason_url) {
        this.verified_reason_url = verified_reason_url;
    }

    public void setVerified_source(String verified_source) {
        this.verified_source = verified_source;
    }

    public void setVerified_source_url(String verified_source_url) {
        this.verified_source_url = verified_source_url;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public void setMbtype(int mbtype) {
        this.mbtype = mbtype;
    }

    public void setMbrank(int mbrank) {
        this.mbrank = mbrank;
    }

    public void setBlock_word(int block_word) {
        this.block_word = block_word;
    }

    public void setBlock_app(int block_app) {
        this.block_app = block_app;
    }

    public void setCredit_score(int credit_score) {
        this.credit_score = credit_score;
    }

    public void setUser_ability(int user_ability) {
        this.user_ability = user_ability;
    }

    public void setUrank(int urank) {
        this.urank = urank;
    }

    public long getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getDomain() {
        return domain;
    }

    public String getWeihao() {
        return weihao;
    }

    public String getGender() {
        return gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public int getPagefriends_count() {
        return pagefriends_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getVerified_type() {
        return verified_type;
    }

    public String getRemark() {
        return remark;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public int getPtype() {
        return ptype;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public String getVerified_trade() {
        return verified_trade;
    }

    public String getVerified_reason_url() {
        return verified_reason_url;
    }

    public String getVerified_source() {
        return verified_source;
    }

    public String getVerified_source_url() {
        return verified_source_url;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public int getOnline_status() {
        return online_status;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public String getLang() {
        return lang;
    }

    public int getStar() {
        return star;
    }

    public int getMbtype() {
        return mbtype;
    }

    public int getMbrank() {
        return mbrank;
    }

    public int getBlock_word() {
        return block_word;
    }

    public int getBlock_app() {
        return block_app;
    }

    public int getCredit_score() {
        return credit_score;
    }

    public int getUser_ability() {
        return user_ability;
    }

    public int getUrank() {
        return urank;
    }

    public static class StatusEntity {
        private String created_at;
        private long id;
        private String mid;
        private String idstr;
        private String text;
        private int textLength;
        private int source_allowclick;
        private int source_type;
        private String source;
        private boolean favorited;
        private boolean truncated;
        private String in_reply_to_status_id;
        private String in_reply_to_user_id;
        private String in_reply_to_screen_name;
        private Object geo;
        private int reposts_count;
        private int comments_count;
        private int attitudes_count;
        private boolean isLongText;
        private int mlevel;
        /**
         * type : 0
         * list_id : 0
         */

        private VisibleEntity visible;
        private int biz_feature;
        private int userType;
        private List<?> pic_urls;
        /**
         * shooting : 1
         * client_mblogid : 8eca2c8b-7dd7-4cfe-8592-e4983d2e0d15
         */

        private List<AnnotationsEntity> annotations;
        private List<?> darwin_tags;
        private List<?> hot_weibo_tags;

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public void setIdstr(String idstr) {
            this.idstr = idstr;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTextLength(int textLength) {
            this.textLength = textLength;
        }

        public void setSource_allowclick(int source_allowclick) {
            this.source_allowclick = source_allowclick;
        }

        public void setSource_type(int source_type) {
            this.source_type = source_type;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

        public void setIn_reply_to_status_id(String in_reply_to_status_id) {
            this.in_reply_to_status_id = in_reply_to_status_id;
        }

        public void setIn_reply_to_user_id(String in_reply_to_user_id) {
            this.in_reply_to_user_id = in_reply_to_user_id;
        }

        public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
            this.in_reply_to_screen_name = in_reply_to_screen_name;
        }

        public void setGeo(Object geo) {
            this.geo = geo;
        }

        public void setReposts_count(int reposts_count) {
            this.reposts_count = reposts_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public void setAttitudes_count(int attitudes_count) {
            this.attitudes_count = attitudes_count;
        }

        public void setIsLongText(boolean isLongText) {
            this.isLongText = isLongText;
        }

        public void setMlevel(int mlevel) {
            this.mlevel = mlevel;
        }

        public void setVisible(VisibleEntity visible) {
            this.visible = visible;
        }

        public void setBiz_feature(int biz_feature) {
            this.biz_feature = biz_feature;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public void setPic_urls(List<?> pic_urls) {
            this.pic_urls = pic_urls;
        }

        public void setAnnotations(List<AnnotationsEntity> annotations) {
            this.annotations = annotations;
        }

        public void setDarwin_tags(List<?> darwin_tags) {
            this.darwin_tags = darwin_tags;
        }

        public void setHot_weibo_tags(List<?> hot_weibo_tags) {
            this.hot_weibo_tags = hot_weibo_tags;
        }

        public String getCreated_at() {
            return created_at;
        }

        public long getId() {
            return id;
        }

        public String getMid() {
            return mid;
        }

        public String getIdstr() {
            return idstr;
        }

        public String getText() {
            return text;
        }

        public int getTextLength() {
            return textLength;
        }

        public int getSource_allowclick() {
            return source_allowclick;
        }

        public int getSource_type() {
            return source_type;
        }

        public String getSource() {
            return source;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public boolean isTruncated() {
            return truncated;
        }

        public String getIn_reply_to_status_id() {
            return in_reply_to_status_id;
        }

        public String getIn_reply_to_user_id() {
            return in_reply_to_user_id;
        }

        public String getIn_reply_to_screen_name() {
            return in_reply_to_screen_name;
        }

        public Object getGeo() {
            return geo;
        }

        public int getReposts_count() {
            return reposts_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public int getAttitudes_count() {
            return attitudes_count;
        }

        public boolean isIsLongText() {
            return isLongText;
        }

        public int getMlevel() {
            return mlevel;
        }

        public VisibleEntity getVisible() {
            return visible;
        }

        public int getBiz_feature() {
            return biz_feature;
        }

        public int getUserType() {
            return userType;
        }

        public List<?> getPic_urls() {
            return pic_urls;
        }

        public List<AnnotationsEntity> getAnnotations() {
            return annotations;
        }

        public List<?> getDarwin_tags() {
            return darwin_tags;
        }

        public List<?> getHot_weibo_tags() {
            return hot_weibo_tags;
        }

        public static class VisibleEntity {
            private int type;
            private int list_id;

            public void setType(int type) {
                this.type = type;
            }

            public void setList_id(int list_id) {
                this.list_id = list_id;
            }

            public int getType() {
                return type;
            }

            public int getList_id() {
                return list_id;
            }
        }

        public static class AnnotationsEntity {
            private int shooting;
            private String client_mblogid;

            public void setShooting(int shooting) {
                this.shooting = shooting;
            }

            public void setClient_mblogid(String client_mblogid) {
                this.client_mblogid = client_mblogid;
            }

            public int getShooting() {
                return shooting;
            }

            public String getClient_mblogid() {
                return client_mblogid;
            }
        }
    }
}
