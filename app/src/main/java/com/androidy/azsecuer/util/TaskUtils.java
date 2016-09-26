package com.androidy.azsecuer.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.SoftManagerInfo;
import com.androidy.azsecuer.entity.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/18.
 */
public class TaskUtils {

    public static boolean filterApp(ApplicationInfo info){
        //系统应用，用户手动升级
            if((info.flags& ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0){
                return true;
            }else if ((info.flags&ApplicationInfo.FLAG_SYSTEM)==0 ){//用户自动安装的应用程序
                return true;
            }
        return false;
    }

    public static List<TaskInfo> getTaskInfos(Context context){
            List<TaskInfo> taskInfos = new ArrayList<>();
        PackageManager packageManager =context.getPackageManager();
        ActivityManager activityManager =(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos=activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info:runningAppProcessInfos){
            TaskInfo taskInfo = new TaskInfo();
            //进程名称
            String packageName = info.processName;
            taskInfo.setPackageName(packageName);
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName,0);
                //图标
                Drawable icon = applicationInfo.loadIcon(packageManager);
                if (icon==null){
                    taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                }else {
                    taskInfo.setIcon(icon);
                }
                //名称
                String taskName = applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setTaskName(taskName);
                //判断是否是用户程序
                boolean isUserTask = filterApp(applicationInfo);
                taskInfo.setUserTask(isUserTask);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                taskInfo.setTaskName(packageName);
            }

            int pid = info.pid;
            Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{pid});
            Debug.MemoryInfo memoryInfo =memoryInfos[0];
            long totalPrivateDirty = memoryInfo.getTotalPrivateDirty();
            taskInfo.setMemory(totalPrivateDirty);
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

    public static  List<TaskInfo> getUserTaskInfos(Context context){
        List<TaskInfo> userTaskInfos = new ArrayList<>();
        for (TaskInfo taskInfo:getTaskInfos(context)){
            if (taskInfo.isUserTask()){
                userTaskInfos.add(taskInfo);
            }
        }
        return userTaskInfos;
    }
    public static  List<TaskInfo> getSystemTaskInfos(Context context){
        List<TaskInfo> systemTaskInfos = new ArrayList<>();
        for (TaskInfo taskInfo:getTaskInfos(context)){
            if (!taskInfo.isUserTask()){
                systemTaskInfos.add(taskInfo);
            }
        }
        return systemTaskInfos;
    }
    public static List<SoftManagerInfo> getSoftManagerInfo(Context context){
        List<SoftManagerInfo> infos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos= pm.getInstalledPackages(0);
        for (PackageInfo packageInfo:packageInfos){
            Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
            String taskName = packageInfo.applicationInfo.loadLabel(pm).toString();
            String packageName=packageInfo.packageName;
            String versionName = packageInfo.versionName;
            SoftManagerInfo softManagerInfo =new SoftManagerInfo(packageName,drawable,taskName,versionName);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName,0);
                boolean isUserTask = filterApp(applicationInfo);
                softManagerInfo.setUserTask(isUserTask);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            infos.add(softManagerInfo);
        }
        return infos;
    }
    public static List<SoftManagerInfo> getSystemSoftManagerInfo(Context context){
        List<SoftManagerInfo> infos = new ArrayList<>();
        for (SoftManagerInfo info:getSoftManagerInfo(context)){
            if (!info.isUserTask()){
                infos.add(info);
            }
        }
        return infos;
    }
    public static List<SoftManagerInfo> getUserSoftManagerInfo(Context context){
        List<SoftManagerInfo> infos = new ArrayList<>();
        for (SoftManagerInfo info:getSoftManagerInfo(context)){
            if (info.isUserTask()){
                infos.add(info);
            }
        }
        return infos;
    }


}


