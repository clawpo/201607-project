package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.RegisterActivity;

/**
 * Created by clawpo on 16/9/20.
 */
public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoLogin(Activity context){
        startActivity(context, LoginActivity.class);
    }
    public static void gotoRegister(Activity context){
        startActivity(context, RegisterActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
