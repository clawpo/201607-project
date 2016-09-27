package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import com.hyphenate.chat.EMClient;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;

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
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void gotoSettings(Activity context){
        startActivity(context, SettingsActivity.class);
    }
    public static void gotoUserView(Activity context){
        context.startActivity(new Intent(context, UserProfileActivity.class).putExtra("setting", true)
						.putExtra("username", EMClient.getInstance().getCurrentUser()));
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
