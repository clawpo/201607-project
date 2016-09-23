package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hyphenate.chat.EMClient;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.em_activity_splash);
		super.onCreate(arg0);
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (SuperWeChatHelper.getInstance().isLoggedIn()) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
					long start = System.currentTimeMillis();
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					UserAvatar user = SuperWeChatHelper.getInstance().getCurrentUserAvatar();
					L.e("splash,aotu login,user="+user);
					//enter main screen
					MFGT.gotoMainActivity(SplashActivity.this);
					finish();
				}else {
					startActivity(new Intent(SplashActivity.this, GuideActivity.class));
					finish();
				}
			}
		},sleepTime);

	}
	
	/**
	 * get sdk version
	 */
	private String getVersion() {
	    return EMClient.getInstance().getChatConfig().getVersion();
	}
}
