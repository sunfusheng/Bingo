package com.sun.bingo.model.eventbus;

/**
 * Created by sunfusheng on 15/9/24.
 */
public interface EventType {

    // 更新Bingo数据列表事件
    String EVENT_TYPE_UPDATE_BINGO_LIST = "type_update_bingo_list";

    // 改变主题事件
    String EVENT_TYPE_CHANGE_THEME = "type_change_theme";

    // 软件更新
    String EVENT_TYPE_UPDATE_APP = "type_update_app";

}
