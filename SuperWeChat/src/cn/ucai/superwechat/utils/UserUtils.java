package cn.ucai.superwechat.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.util.HanziToPinyin;

import java.io.File;
import java.util.ArrayList;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;

import static cn.ucai.superwechat.utils.CommonUtils.getWeChatNoString;

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
                textView.setText(getWeChatNoString()+user.getMUserName());
            }else{
                setText(getWeChatNoString()+username,textView);
            }
        }
    }

    public static String getUserAvatarPath(String username, String suffix, String updateTime){
        //http://101.251.196.90:8000/SuperWeChatServerV2.0/downloadAvatar?
        // name_or_hxid=a952700&avatarType=user_avatar&m_avatar_suffix=.jpg&width=200&height=200
        StringBuilder path = new StringBuilder(I.SERVER_ROOT);
        path.append(I.REQUEST_DOWNLOAD_AVATAR)
                .append(I.QUESTION)
                .append(I.NAME_OR_HXID).append(I.EQUAL).append(username)
                .append(I.AND)
                .append(I.AVATAR_TYPE).append(I.EQUAL).append(I.AVATAR_TYPE_USER_PATH)
                .append(I.AND)
                .append(I.Avatar.AVATAR_SUFFIX).append(I.EQUAL).append(suffix)
                .append(I.AND)
                .append(I.AVATAR_WIDTH).append(I.EQUAL).append(I.AVATAR_WIDTH_DEFAULT)
                .append(I.AND)
                .append(I.AVATAR_HEIGHT).append(I.EQUAL).append(I.AVATAR_HEIGHT_DEFAULT)
                .append(I.AND)
                .append(I.Avatar.UPDATE_TIME).append(I.EQUAL).append(updateTime);
        return path.toString();
    }

    public static void setUserAvatar(Context context, String username, ImageView imageView){
        L.e("setUserAvatar username="+username);
        UserAvatar user = getUserInfo(username);
        setAvatar(context,user,imageView);
    }

    public static void setAvatar(Context context, UserAvatar user, ImageView imageView) {
        if(user != null && user.getMAvatarSuffix()!=null){
            String path = getUserAvatarPath(user.getMUserName(),user.getMAvatarSuffix(),user.getMAvatarLastUpdateTime());
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

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path){
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir,path);
        if(!folder.exists()){
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

    /**
     * set initial letter of according user's nickname( username if no nickname)
     *
     * @param user
     */
    public static void setUserInitialLetter(UserAvatar user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if ( !TextUtils.isEmpty(user.getMUserNick()) ) {
            letter = new GetInitialLetter().getLetter(user.getMUserNick());
            user.setInitialLetter(letter);
            return;
        }
        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(user.getMUserName())) {
            letter = new GetInitialLetter().getLetter(user.getMUserName());
        }
        user.setInitialLetter(letter);
    }
}
