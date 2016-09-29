package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.UserUtils;

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
        }else{
            MFGT.finish(FrientProfileActivity.this);
        }
    }

    private void initView() {
        mImgBack.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(R.string.userinfo_txt_profile);
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        MFGT.finish(FrientProfileActivity.this);
    }
}
