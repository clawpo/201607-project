package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.UserUtils;

import static cn.ucai.superwechat.utils.CommonUtils.getAddContactPrefixString;

/**
 * Created by clawpo on 2016/9/29.
 */

public class FrientProfileActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.tv_userinfo_nick)
    TextView mTvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView mTvUserinfoName;
    @BindView(R.id.btn_add_contact)
    Button mBtnAddContact;
    @BindView(R.id.btn_send_msg)
    Button mBtnSendMsg;
    @BindView(R.id.btn_send_video)
    Button mBtnSendVideo;

    UserAvatar user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_frient_profile);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent!=null){
            user = (UserAvatar) intent.getSerializableExtra("user");
            L.e("user="+user);
            UserUtils.setUserNick(user.getMUserNick(),mTvUserinfoNick);
            UserUtils.setUserName(user.getMUserName(),mTvUserinfoName);
            UserUtils.setAvatar(FrientProfileActivity.this,user,mProfileImage);
            isFrient();
        }else{
            MFGT.finish(FrientProfileActivity.this);
        }
    }

    private void isFrient() {
        if(SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())){
            mBtnSendMsg.setVisibility(View.VISIBLE);
            mBtnSendVideo.setVisibility(View.VISIBLE);
        }else{
            mBtnAddContact.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mImgBack.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(R.string.userinfo_txt_profile);
    }

    @Override
    public void back(View view) {
        MFGT.finish(FrientProfileActivity.this);
    }

    @OnClick(R.id.img_back)
    public void back() {
        MFGT.finish(FrientProfileActivity.this);
    }

    @OnClick(R.id.btn_add_contact)
    public void addContact(){
        if (EMClient.getInstance().getCurrentUser().equals(user.getMUserName())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (SuperWeChatHelper.getInstance().getContactList().containsKey(user.getMUserName())) {
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        startActivityForResult(new Intent(this, EditActivity.class)
                .putExtra("data", getAddContactPrefixString()
                        +SuperWeChatHelper.getInstance().getCurrentUserAvatar().getMUserNick()), 0);

    }

    private void addEMContact(final String reason){
        new Thread(new Runnable() {
            public void run() {

                try {
                    EMClient.getInstance().contactManager().addContact(user.getMUserName(), reason);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final String reason = data.getStringExtra("data");
        if(!TextUtils.isEmpty(reason)){
            progressDialog = new ProgressDialog(this);
            String stri = getResources().getString(R.string.addcontact_adding);
            progressDialog.setMessage(stri);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            addEMContact(reason);
        }
    }
}
