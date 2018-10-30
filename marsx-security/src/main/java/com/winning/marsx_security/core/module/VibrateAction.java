package com.winning.marsx_security.core.module;

import android.app.Activity;

import com.winning.marsx_security.core.DirectiveManager;
import com.winning.marsx_security.util.Constants;
import com.winning.marsx_security.util.TipUtils;

import com.winning.marsx_annotation.Action;

/**
 * 震动
 * @author sharkchao
 *
 */
@Action(value = Constants.ACTION_VIBRATE)
public class VibrateAction implements BaseAction{
    @Override
    public void doAction(String t) {
        TipUtils.Vibrate((Activity) DirectiveManager.currentActivity(),new long[]{800, 1000, 800, 1000, 800, 1000},true);
    }
}
