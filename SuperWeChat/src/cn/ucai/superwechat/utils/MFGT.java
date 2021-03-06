package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import com.hyphenate.chat.EMClient;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.common.ExitAppUtils;
import cn.ucai.superwechat.ui.ChatActivity;
import cn.ucai.superwechat.ui.FrientProfileActivity;
import cn.ucai.superwechat.ui.GuideActivity;
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

    public static void gotoFrientActivity(Activity context, UserAvatar user) {
        context.startActivity(new Intent(context, FrientProfileActivity.class).putExtra("user", user));
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoFrientActivity(Activity context, String username) {
        context.startActivity(new Intent(context, FrientProfileActivity.class).putExtra("username", username));
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoChat(Activity context,String userId){
        context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", userId));
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoGuide(Activity context){
        ExitAppUtils.getInstance().exit();
        Intent it = new Intent(context, GuideActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
