package com.winning.marsx_security.core.module;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.winning.marsx_security.core.DirectiveManager;
import com.winning.marsx_security.util.Constants;

import com.winning.marsx_annotation.Action;

/**
 * 响铃(系统铃音)
 * @author sharkchao
 *
 */
@Action(value = Constants.ACTION_RING_DOWN)
public class RingDownAction implements BaseAction{
    @Override
    public void doAction(String t) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(DirectiveManager.currentActivity(), uri);
        rt.play();
    }
}
