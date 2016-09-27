package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by clawpo on 2016/9/23.
 */
public class ProfileFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.tvname)
    TextView mTvname;
    @BindView(R.id.tvmsg)
    TextView mTvmsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserInfo();
    }

    private void setUserInfo() {
        UserAvatar user = SuperWeChatHelper.getInstance().getCurrentUserAvatar();
        EaseUserUtils.setUserNick(user.getMUserNick(),mTvname);
        EaseUserUtils.setUserNick("微信号:"+user.getMUserName(),mTvmsg);
        EaseUserUtils.setUserAvatar(SuperWeChatApplication.applicationContext,user.getMUserName(),mProfileImage);
    }

    @OnClick(R.id.txt_setting)
    public void settings(){
        MFGT.gotoSettings(getActivity());
    }

    @OnClick(R.id.view_user)
    public void view_user_onclick(){
        MFGT.gotoUserView(getActivity());
    }
}
