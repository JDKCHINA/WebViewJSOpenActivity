package com.example.js.util;

import android.content.Context;
import android.content.Intent;

import com.example.js.DemoActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fangzhu on 2015/1/7.
 */
public class OpenActivityUtil {
    public static final String SCHEME = "message://";
    public static final String KEY_ACTION = "action";
    public static final String IS_GOHOME = "gohome";//是否返回首页
//     message://login?key1=value1&key2=value2


    /**
     *
     * 隐士启动
     * 手机上有两个以上的同别名 IntentFilter activity 会出现选择框 可设置pakageName
     *
     * 如果pakageName 可能修改
     * 或者不想使用别名
     *
     * 则为需要的activity 配置别名 使用activityMap寻找
     * 根据别名找到类全路径
     *
     */
    public static Map<String, String> activityMap = new HashMap<String, String>();

    /*bellow string do not change*/
    public static final String ACT_DEMO = "demo";


    static {
         /*行情首页*/
        activityMap.put(ACT_DEMO, DemoActivity.class.getName());



    }
    /**
     *
     * @param context
     * @param action
     */
    public static void open(Context context, String action) {
        context.startActivity(getIntent(context, action));
    }

    /**
     * 使用场景
     *  登录成功 需要跳转到指定activity
     *
     * @param context
     * @param clazz 当前activity
     * @param callBackAct 跳转activity 自定义的缩写名称
     * @param stringMap
     * @return
     */
    public static Intent initAction (Context context, Class clazz, String callBackAct, Map<String, String> stringMap) {
        StringBuffer stringBuffer = null;
        try {
            stringBuffer = new StringBuffer();
            stringBuffer.append(SCHEME);
            stringBuffer.append(callBackAct);
            stringBuffer.append("?");
            Iterator<String> keys = stringMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                stringBuffer.append(key);
                stringBuffer.append(stringMap.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.putExtra(KEY_ACTION, stringBuffer.toString());
        return intent;
    }
    public static Intent getIntent (Context context, String action) {
        try {
            if (action == null)
                return null;
            if (action.trim().length() == 0)
                return null;
            action = action.trim();
            if (!action.startsWith(SCHEME))
                return null;
            action = action.replace(SCHEME, "");

            if (action.contains("?")) {
                String[] strs = action.split("\\?");
                Intent intent = new Intent();
                String clsAction = strs[0].trim();
                //方式一
                intent.setAction(clsAction);

                //方式二
//                if (activityMap.containsKey(clsAction)) {
//                    intent.putExtra(IS_GOHOME, true);
//                    intent.setClassName(context, activityMap.get(clsAction));
//                } else {
//                    return null;
//                }

                if (strs.length > 1) {
                    String[] params = strs[1].split("&");
                    for (int i = 0; i < params.length; i++) {
                        String str = params[i].trim();
                        String[] temp = str.split("=");
                        intent.putExtra(temp[0].trim(), temp[1].trim());
                    }
                }
                return intent;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isGoHome (Context context, Intent intent) {
        return intent.getBooleanExtra(IS_GOHOME, false);
    }
}
