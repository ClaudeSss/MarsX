package com.winning.marsx_security.core.module;

import com.winning.marsx_security.core.DirectiveManager;
import com.winning.marsx_security.util.Constants;
import com.winning.marsx_security.util.PermisionUtils;

import com.winning.marsx_annotation.Action;

/**
 * @author sharkchao
 * 恢复出厂设置,双清存储数据（包括外置sd卡），wipeData后重启
 */
@Action(value = Constants.ACTION_WIPE_DATA)
public class WipeDataAction implements BaseAction {
    @Override
    public void doAction(String t) {
        if (PermisionUtils.verifyDeviceAdminPermissions(DirectiveManager.currentActivity())){
            PermisionUtils.wipeData();
        }
    }
}
