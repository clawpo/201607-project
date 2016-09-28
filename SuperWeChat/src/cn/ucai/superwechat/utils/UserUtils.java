package cn.ucai.superwechat.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;

/**
 * Created by clawpo on 2016/9/28.
 */

public class UserUtils {
    public static UserAvatar getUserInfo(String username){
        if(username==null || username.equals(SuperWeChatHelper.getInstance().getCurrentUsernName())){
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

    public static String getUserAvatarPath(String username){
        //http://101.251.196.90:8000/SuperWeChatServerV2.0/downloadAvatar?
        // name_or_hxid=a952700&avatarType=user_avatar&m_avatar_suffix=.jpg&width=200&height=200
        StringBuilder path = new StringBuilder(I.SERVER_ROOT);
        path.append(I.REQUEST_DOWNLOAD_AVATAR)
                .append(I.QUESTION)
                .append(I.NAME_OR_HXID).append(I.EQUAL).append(username)
                .append(I.AND)
                .append(I.AVATAR_TYPE).append(I.EQUAL).append(I.AVATAR_TYPE_USER_PATH)
                .append(I.AND)
                .append(I.Avatar.AVATAR_SUFFIX).append(I.EQUAL).append(I.AVATAR_SUFFIX_JPG)
                .append(I.AND)
                .append(I.AVATAR_WIDTH).append(I.EQUAL).append(I.AVATAR_WIDTH_DEFAULT)
                .append(I.AND)
                .append(I.AVATAR_HEIGHT).append(I.EQUAL).append(I.AVATAR_HEIGHT_DEFAULT);
        return path.toString();
    }

    public static void setUserAvatar(Context context, String username, ImageView imageView){
        L.e("setUserAvatar username="+username);
        UserAvatar user = getUserInfo(username);
        if(username != null && user.getMAvatarSuffix()!=null){
            String path = getUserAvatarPath(username);
            L.e("avatar path="+path);
            try {
                Glide.with(context).load(path).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(com.hyphenate.easeui.R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(com.hyphenate.easeui.R.drawable.ease_default_avatar).into(imageView);
        }
    }
}
