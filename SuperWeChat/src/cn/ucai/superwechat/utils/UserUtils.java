package cn.ucai.superwechat.utils;

import android.widget.TextView;

import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;

/**
 * Created by clawpo on 2016/9/28.
 */

public class UserUtils {
    public static UserAvatar getUserInfo(String username){
        if(username==null){
            return SuperWeChatHelper.getInstance().getCurrentUserAvatar();
        }
        return null;
    }

    public static void setText(String values, TextView textView){
        textView.setText(values);
    }

    public static void setUserNick(String username,TextView textView){
        if(textView != null){
            UserAvatar user = getUserInfo(username);
            if(user != null && user.getMUserNick() != null){
                textView.setText(user.getMUserNick());
            }else{
                setText(username,textView);
            }
        }
    }

    public static void setUserName(String username,TextView textView){
        if(textView != null){
            UserAvatar user = getUserInfo(username);
            if(user != null && user.getMUserName() != null){
                textView.setText(user.getMUserName());
            }else{
                setText(username,textView);
            }
        }
    }
}
