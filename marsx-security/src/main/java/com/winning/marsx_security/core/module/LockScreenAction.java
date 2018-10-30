package com.winning.marsx_security.core.module;

import com.winning.marsx_security.core.DirectiveManager;
import com.winning.marsx_security.util.Constants;
import com.winning.marsx_security.util.PermisionUtils;

import com.winning.marsx_annotation.Action;

/**
 * 自动锁屏
 * @author sharkchao
 * value 暂时为空
 */
@Action(value = Constants.ACTION_LOCK_SCREEN)
public class LockScreenAction implements BaseAction {
    @Override
    public void doAction(String value) {
        if (PermisionUtils.verifyDeviceAdminPermissions(DirectiveManager.getContext())){
            PermisionUtils.LockScreenNow();
        }
    }
}
