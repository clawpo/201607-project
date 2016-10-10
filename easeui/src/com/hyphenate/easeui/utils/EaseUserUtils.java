package com.hyphenate.easeui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }

    public static String getUserAvatarNick(String username){
        if(userProvider != null)
            return userProvider.getUserAvatarNick(username);
        return null;
    }

    public static String getUserAvatarPath(String username){
        if(userProvider != null)
            return userProvider.getUserAvatarPath(username);
        return null;
    }
    
    public static void setGroupAvatar(Context context, String hxid, ImageView imageView){
    	String path = "http://101.251.196.90:8000/SuperWeChatServerV2.0/downloadAvatar?"+
        "name_or_hxid="+hxid+"&avatarType=group_icon&m_avatar_suffix=.jpg&width=200&height=200";

        if(path != null){
            try {
                Glide.with(context).load(path).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_group_icon).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_group_icon).into(imageView);
        }
    }

    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        EaseUser user = getUserInfo(username);
        String path = getUserAvatarPath(username);
        Log.e("avavtar","path="+path+",user="+user);
        if(path != null){
            try {
                Log.e("avatar","path......1");
                Glide.with(context).load(path).into(imageView);
            } catch (Exception e) {
                Log.e("avatar","path......2");
                //use default avatar
                Glide.with(context).load(R.drawable.ease_default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else if(user != null && user.getAvatar() != null){
            Log.e("avatar","user......");
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Log.e("avatar","else.....default");
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
            Log.e("utils","user="+user+",nick="+getUserAvatarNick(username));
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
