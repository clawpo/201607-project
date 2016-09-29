package cn.ucai.superwechat.ui;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;
import cn.ucai.superwechat.utils.UserUtils;

public class UserProfileActivity extends BaseActivity {

    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.view_user)
    RelativeLayout mViewUser;
    @BindView(R.id.txt_userinfo_nick)
    TextView mTxtUserinfoNick;
    @BindView(R.id.layout_userinfo_nick)
    LinearLayout mLayoutUserinfoNick;
    @BindView(R.id.txt_userinfo_name)
    TextView mTxtUserinfoName;
    @BindView(R.id.layout_userinfo_name)
    LinearLayout mLayoutUserinfoName;
    @BindView(R.id.txt_userinfo_qrcode)
    TextView mTxtUserinfoQrcode;
    @BindView(R.id.txt_userinfo_address)
    TextView mTxtUserinfoAddress;
    @BindView(R.id.txt_userinfo_sex)
    TextView mTxtUserinfoSex;
    @BindView(R.id.layout_userinfo_sex)
    LinearLayout mLayoutUserinfoSex;
    @BindView(R.id.txt_userinfo_area)
    TextView mTxtUserinfoArea;
    @BindView(R.id.layout_userinfo_area)
    LinearLayout mLayoutUserinfoArea;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    private ProgressDialog dialog;
    UserAvatar user;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        user = SuperWeChatHelper.getInstance().getCurrentUserAvatar();
        L.e("UserProfileActivity", "user=" + user);
        if (user == null) {
            finish();
        } else {
            UserUtils.setUserNick(user.getMUserNick(), mTxtUserinfoNick);
            UserUtils.setUserName(user.getMUserName(), mTxtUserinfoName);
            UserUtils.setUserAvatar(SuperWeChatApplication.applicationContext, user.getMUserName(), mProfileImage);
        }
    }

    private void initView() {
        mImgBack.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(getString(R.string.userinfo_txt_title));

    }


    public void asyncFetchUserInfo(String username) {
        SuperWeChatHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    SuperWeChatHelper.getInstance().saveContact(user);
                    if (isFinishing()) {
                        return;
                    }
                    mTxtUserinfoNick.setText(user.getNick());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
                        Glide.with(UserProfileActivity.this).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(mProfileImage);
                    } else {
                        Glide.with(UserProfileActivity.this).load(R.drawable.em_default_avatar).into(mProfileImage);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }


    private void uploadHeadPhoto() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(UserProfileActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void updateRemoteNick(final String nickName) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = SuperWeChatHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
                if (UserProfileActivity.this.isFinishing()) {
                    return;
                }
                if (!updatenick) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    user.setMUserNick(nickName);
                    updateLocalUser();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
                                    .show();
                            mTxtUserinfoNick.setText(nickName);
                        }
                    });
                }
            }
        }).start();
    }

    private void updateLocalUser() {
        SuperWeChatHelper.getInstance().saveCurrentUserAvatar(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    uploadUserAvatar(data);
//                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void uploadUserAvatar(final Intent data) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        File file = saveBitmapFile(data);
        OkHttpUtils<String> utils = new OkHttpUtils<>(UserProfileActivity.this);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,user.getMUserName())
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .targetClass(String.class)
                .addFile2(file)
                .post()
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        L.e("s="+s);
                        Result result = ResultUtils.getResultFromJson(s,UserAvatar.class);
                        L.e("result="+result);
                        if(result!=null && result.isRetMsg()){
                            UserAvatar u= (UserAvatar) result.getRetData();
                            user = u;
                            updateLocalUser();
                            setPicToView(data);
                        }else{
                            dialog.dismiss();
                            CommonUtils.showShortResultMsg(result!=null?result.getRetCode():R.string.toast_updatephoto_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        dialog.dismiss();
                        CommonUtils.showShortToast(R.string.toast_updatephoto_fail);
                    }
                });
    }

    public File saveBitmapFile(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            File file = new File(UserUtils.getAvatarPath(UserProfileActivity.this,I.AVATAR_TYPE_USER_PATH),
                    System.currentTimeMillis()+I.AVATAR_SUFFIX_JPG);//将要保存图片的路径
            L.e("file path="+file.getAbsolutePath());
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }
    /**
     * save the picture data
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            mProfileImage.setImageDrawable(drawable);
            uploadUserAvatar(Bitmap2Bytes(photo));
        }

    }

    private void uploadUserAvatar(final byte[] data) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = SuperWeChatHelper.getInstance().getUserProfileManager().uploadUserAvatar(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (avatarUrl != null) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }).start();

        dialog.show();
    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @OnClick(R.id.img_back)
    public void img_back_onclick() {
        MFGT.finish(UserProfileActivity.this);
    }

    @OnClick(R.id.layout_userinfo_nick)
    public void userinfo_nick_onclick() {
        final EditText editText = new EditText(this);
        editText.setText(user.getMUserNick());
        new Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!user.getMUserNick().equals(nickString)) {
                            updateAppNick(nickString);
                        }else{
                            CommonUtils.showShortToast(R.string.toast_updatenick_fail);
                        }
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    private void updateAppNick(final String nickString) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        OkHttpUtils<Result> utils = new OkHttpUtils<>(UserProfileActivity.this);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,user.getMUserName())
                .addParam(I.User.NICK,nickString)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        Log.e("userProfile","result="+result);
                        if(result!=null && result.isRetMsg()){
                            updateRemoteNick(nickString);
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                            .show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                        .show();
                                dialog.dismiss();
                            }
                        });
                    }
                });
    }

    @OnClick(R.id.view_user)
    public void view_user_onclick() {
        uploadHeadPhoto();
    }


    @OnClick({R.id.layout_userinfo_name, R.id.txt_userinfo_qrcode, R.id.txt_userinfo_address,
            R.id.layout_userinfo_sex, R.id.layout_userinfo_area, R.id.layout_userinfo_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_userinfo_name:
                break;
            case R.id.txt_userinfo_qrcode:
                break;
            case R.id.txt_userinfo_address:
                break;
            case R.id.layout_userinfo_sex:
                break;
            case R.id.layout_userinfo_area:
                break;
            case R.id.layout_userinfo_sign:
                break;
        }
    }
}
