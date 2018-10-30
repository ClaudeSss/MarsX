package com.winning.marsx_security.core.module;

import com.winning.marsx_security.core.DirectiveManager;
import com.winning.marsx_security.util.Constants;
import com.winning.marsx_security.util.PermisionUtils;

import com.winning.marsx_annotation.Action;
/**
 * 重置锁屏密码
 * @author sharkchao
 * password
 */
@Action(value = Constants.ACTION_RESET_PASSWORD)
public class ResetPasswordAction implements BaseAction{
    @Override
    public void doAction(String value) {
        if (PermisionUtils.verifyDeviceAdminPermissions(DirectiveManager.currentActivity())){
            if (null != value && value.length() >= 6){
                //使用自定义密码规则
                PermisionUtils.resetPassword(value);
                PermisionUtils.LockScreenNow();
            }
        }
    }
}
