package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

public class EditActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.tv_addcontact_title)
    TextView mTvAddcontactTitle;
    @BindView(R.id.edittext)
    EditText mEdittext;

    public static final int VIEW_TYPE_ADD_CONTACT = 0;
    public static final int VIEW_TYPE_CHANGE_GROUP_NAME = 1;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_edit);
        ButterKnife.bind(this);
        int type = getIntent().getIntExtra("type",0);
        initView(type);

        String data = getIntent().getStringExtra("data");
        if (data != null)
            mEdittext.setText(data);
        mEdittext.setSelection(mEdittext.length());

    }

    private void initView(int type) {
        mImgBack.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mBtnSend.setVisibility(View.VISIBLE);
        if(type == VIEW_TYPE_ADD_CONTACT){
            mTxtTitle.setText(R.string.addcontact_title);
            mBtnSend.setText(R.string.addcontact_send_msg);
            mTvAddcontactTitle.setVisibility(View.VISIBLE);
        }
        if(type == VIEW_TYPE_CHANGE_GROUP_NAME){
            mTxtTitle.setText(R.string.Change_the_group_name);
            mBtnSend.setText(R.string.button_save);
        }
    }

    @OnClick(R.id.btn_send)
    public void save() {
        setResult(RESULT_OK, new Intent().putExtra("data", mEdittext.getText().toString()));
        finish();
    }

    public void back(View view) {
        MFGT.finish(EditActivity.this);
    }

    @OnClick(R.id.img_back)
    public void imgbackOnClick() {
        MFGT.finish(EditActivity.this);
    }
}
