package com.winning.mars_security.core;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.winning.mars_security.core.strategy.mail.MailWorker;
import com.winning.mars_security.util.PermisionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class DirectiveManager {
    private static DirectiveManager mInstance;
    private static Context mContext;
    private static String mAppkey;
    private DirectiveManager(Context context,String appkey){
        mContext = context;
        mAppkey = appkey;
        PermisionUtils.verifySmsPermissions((Activity) context);
        PermisionUtils.verifyDeviceAdminPermissions(context);

        ActionData.init();
        startMailWorker();
    }
    public static DirectiveManager getInstance(Context context,String appkey){
        if (mInstance == null){
            synchronized (DirectiveManager.class){
                if (mInstance == null){
                    mInstance = new DirectiveManager(context,appkey);
                }
            }
        }
        return mInstance;
    }
    private void startMailWorker(){
        // 获取到LiveData然后监听数据变化
        WorkManager.getInstance().getStatusesByTag(mAppkey).observe((LifecycleOwner)getContext(),new Observer<List<WorkStatus>>() {
            @Override
            public void onChanged(@Nullable List<WorkStatus> workStatuses) {
                if (workStatuses == null || workStatuses.size() == 0){
                    initMailWorker();
                }
            }
        });
    }
    private void initMailWorker(){
        Constraints jobConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .build();

        PeriodicWorkRequest jobWorkManager =
                new PeriodicWorkRequest.Builder(MailWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                        TimeUnit.MILLISECONDS)
                        .setConstraints(jobConstraints)
                        .addTag(mAppkey)
                        .build();

        WorkManager.getInstance().enqueue(jobWorkManager);
    }

    public static Context getContext(){
        return mContext;
    }

    public static String getAppkey() {
        return mAppkey;
    }

}
