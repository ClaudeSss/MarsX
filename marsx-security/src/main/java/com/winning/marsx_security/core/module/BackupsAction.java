package com.winning.marsx_security.core.module;

import android.util.Log;

import com.winning.marsx_security.util.Constants;

import com.winning.marsx_annotation.Action;

/**
 * 数据备份
 * value格式待定
 * @author sharkchao
 */
@Action(value = Constants.ACTION_BACKUP)
public class BackupsAction implements BaseAction{
    @Override
    public void doAction(String value) {
        Log.e("action", "备份数据");
    }
}
