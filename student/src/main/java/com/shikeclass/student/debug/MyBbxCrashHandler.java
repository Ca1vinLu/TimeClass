package com.shikeclass.student.debug;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

public class MyBbxCrashHandler implements UncaughtExceptionHandler {

    private static final String BBX_FILES = "MyAndroid";
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private static final String BBX_PATH = SD_PATH + BBX_FILES + File.separator;
    private static final String LOG_TXT = "bbxClientText";
    private static final String LOG_PATH = BBX_PATH;
    /**
     * Debug Log Tag
     */
    public static final String TAG = "MyBbxCrashHandler";
    // public static final boolean IS_SAVE_LOCALFILE = true;
    /**
     * CrashHandler实例
     */
    private static MyBbxCrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;
    /**
     * 错误信息
     */
    private String mErrInfo = "";

    /**
     * 错误类型
     */
    private String mErrorType = "";

    /**
     * 保证只有一个CrashHandler实例
     */
    private MyBbxCrashHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static MyBbxCrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MyBbxCrashHandler();
        return INSTANCE;
    }

    public interface OnCatch {
        public void onError();
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    OnCatch onCatch;

    public void init(Context ctx, OnCatch onCatch) {
        init(ctx);
        this.onCatch = onCatch;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            stop();
        }
        if (onCatch != null) {
            onCatch.onError();
        }
    }

    private synchronized void stop() {

//        ToastUtil.cancelToast();
//        for (int i = 0; i < BaseActivity.activityList.size(); i++) {
//            Activity activity = BaseActivity.activityList.get(i);
//            activity.finish();
//        }
//        MobclickAgent.onKillProcess(mContext);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        // 获取错误信息
        mErrInfo += saveCrashInfoToFile(ex);
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
//		Timer mTimer = new Timer();
//		mTimer.schedule(new TimerTask() {
//			// 4秒钟后若因为网络问题还没发送成功 强制退出
//			@Override
//			public void run() {
//				DebugUtil.printDebugInfo("！！！！！！！错误信息未发送成功");
//				stop();
//			}
////		}, 40000);
//        if (DebugUtil.isDebug) {
        saveLogFile();
//        }
//
//        LogUtils.debug(TimeUtil.getTime() + mErrInfo);

//        MobclickAgent.reportError(mContext, TimeUtil.getTime() + mErrInfo);
//        DebugUtil.printDebugInfo(mErrInfo);
//        LogUtils.e("flag--", "handleException(MyBbxCrashHandler.java:156)-->>异常" + "\n" + mErrInfo);
        // 发送错误报告到服务器
        // ErrRecordPostNetWork net = new ErrRecordPostNetWork(mContext,
        // mErrInfo, mErrorType, false);
        return true;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx context
     */
    public void collectCrashDeviceInfo(Context ctx) {
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mErrInfo += "[" + field.getName() + ":" + field.get(null) + "]";
//                if (DebugUtil.isDebug) {
//                    LogUtils.d(TAG, field.getName() + " : " + field.get(null));
//                }
            } catch (Exception e) {
                LogUtils.e(TAG, "Error while collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        ex.printStackTrace(printWriter);

        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            mErrorType = cause.toString();
//            DebugUtil.printDebugInfo("FIND_ERROR_COUSE cause = " + mErrorType + "\\\\\\\\\\endClass--cause");
            cause = cause.getCause();
        }

        // toString() 以字符串的形式返回该缓冲区的当前值。
        String result = info.toString();
        printWriter.close();
        int index = 0;
        for (int i = 0; i < result.length(); i++) {
            char temp = result.charAt(i);
            if (temp == ':') {
                index = i;
//                DebugUtil.printDebugInfo("i = " + i);
                break;
            }
        }

        mErrorType = result.substring(0, index);
        return result;
    }

    private void saveLogFile() {
        String path = null;
        if (mDefaultHandler != null) {
            String state = Environment.getExternalStorageState();
            // 判断SdCard是否存在并且是可用的
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                /*path = SystemUtil.getSDCardPath() + "/"
                        + mContext.getPackageName();*/
                path = LOG_PATH;
                // 创建一个logcat目录
                // path = UPDATA_PATH + "log";
                // path="/sdcard/a/log/";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                    if (file.exists()) {
                        System.out.println("mk success!");
                    } else {
                        System.out.println("mk fail!");
                    }
                }
                // deleteOldFile(path);
                String time = com.blankj.utilcode.util.TimeUtils.getNowString();
//                DebugUtil.printDebugInfo("local full time = " + time);
                // String fileName = time.substring(0, 10);
                // String fileName = time;
                File myFile = new File(path + "/" + "LogRecord.log");
//                SDK.GetSDK().setServiceLogPath(path + "/" + "clientsdklog.log");
                String str = "\n****" + Build.BRAND + "****" + "\n"
                        + time + "-->" + "[\n" + mErrInfo + "\n]";
                try {
                    FileWriter fw = new FileWriter(myFile, true);
                    fw.write(str);
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}