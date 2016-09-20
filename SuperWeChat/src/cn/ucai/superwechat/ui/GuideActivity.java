package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;

/**
 * Created by clawpo on 16/9/20.
 */
public class GuideActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_login, R.id.img_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login:

                break;
            case R.id.img_register:
                break;
        }
    }
}
