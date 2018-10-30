package com.winning.marsx_security.core;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.winning.mars_security.core.ActionData;
import com.winning.marsx_security.core.strategy.mail.MailWorker;
import com.winning.marsx_security.core.strategy.sms.SmsReceiver;
import com.winning.marsx_security.util.PermisionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class DirectiveManager {
    private static Stack<Activity> sStack;
    private static DirectiveManager mInstance;
    private static Context sApplication;
    private static String sAppkey;

    private DirectiveManager(Context context,String appkey){
        sApplication = context;
        sAppkey = appkey;
        sStack = new Stack<>();
        //绑定activity声明周期
        initActivityLifecycle();
    }

    private class IActivityLifecycleAdapter implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (sStack.empty()){
                addActivity(activity);
                ActionData.init();
                startSmsWorker();
                startMailWorker();
                return;
            }
            addActivity(activity);
        }
        @Override
        public void onActivityStarted(Activity activity) {
        }
        @Override
        public void onActivityResumed(Activity activity) {
        }
        @Override
        public void onActivityPaused(Activity activity) {
        }
        @Override
        public void onActivityStopped(Activity activity) {
        }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
        @Override
        public void onActivityDestroyed(Activity activity) {
           removeActivity(activity);
        }
    }
    private void initActivityLifecycle() {
        ((Application) sApplication).registerActivityLifecycleCallbacks(new IActivityLifecycleAdapter());
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

    private void startSmsWorker(){
        PermisionUtils.verifyDeviceAdminPermissions(currentActivity());
        AndPermission.with(currentActivity())
                .runtime()
                .permission(Permission.RECEIVE_SMS)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                })
                .onDenied(permissions -> {
                    PermisionUtils.showDeniedSmsMessageDialog(currentActivity());
                })
                .start();
    }
    private void startMailWorker(){
        // 获取到LiveData然后监听数据变化
        WorkManager.getInstance().getStatusesByTag(sAppkey).observe((LifecycleOwner) currentActivity(),new Observer<List<WorkStatus>>() {
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
                        .addTag(sAppkey)
                        .build();

        WorkManager.getInstance().enqueue(jobWorkManager);
    }
    private void startSmsReceiver(Context context){
        SmsReceiver smsReceiver = new SmsReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(999);
        context.registerReceiver(smsReceiver, filter);
    }
    /**
     * @param activity  需要添加进栈管理的activity
     */
    private void addActivity(Activity activity) {
        sStack.add(activity);
    }

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return
     */
    private boolean removeActivity(Activity activity) {
        return sStack.remove(activity);
    }

    public static Activity currentActivity() {
        Activity activity = null;
        if (!sStack.empty()){
            activity = sStack.lastElement();
        }
        return activity;
    }

    public static Context getApplication(){
        return sApplication;
    }

    public static String getAppkey() {
        return sAppkey;
    }

}
