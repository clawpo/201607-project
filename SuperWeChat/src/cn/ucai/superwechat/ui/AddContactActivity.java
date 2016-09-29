/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

public class AddContactActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_right)
    TextView mTxtRight;
    @BindView(R.id.edit_note)
    EditText mEditNote;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.indicator)
    Button mIndicator;
    @BindView(R.id.ll_user)
    RelativeLayout mLlUser;
    private EditText editText;
    private RelativeLayout searchedUserLayout;
    private TextView nameText;
    private Button searchBtn;
    private String toAddUsername;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        ButterKnife.bind(this);
        initView();
//        TextView mTextView = (TextView) findViewById(R.id.add_list_friends);
//
//        editText = (EditText) findViewById(R.id.edit_note);
//        String strAdd = getResources().getString(R.string.add_friend);
//        mTextView.setText(strAdd);
//        String strUserName = getResources().getString(R.string.user_name);
//        editText.setHint(strUserName);
//        searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
//        nameText = (TextView) findViewById(R.id.name);
//        searchBtn = (Button) findViewById(R.id.search);
    }

    private void initView() {
        mImgBack.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(R.string.menu_addfriend);
        mTxtRight.setVisibility(View.VISIBLE);
        mTxtRight.setText(R.string.search);
    }


    /**
     * search contact
     */
    @OnClick(R.id.txt_right)
    public void searchContact() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String name = mEditNote.getText().toString();
        toAddUsername = name.trim();
        if (TextUtils.isEmpty(toAddUsername)) {
            progressDialog.dismiss();
            new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
            return;
        }

        // TODO you can search the user from your app server here.

        //show the userame and add button if user exist
        searchedUserLayout.setVisibility(View.VISIBLE);
        nameText.setText(toAddUsername);
    }

    /**
     *  add contact
     * @param view
     */
    public void addContact(View view) {
        if (EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (SuperWeChatHelper.getInstance().getContactList().containsKey(nameText.getText().toString())) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }


        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
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

    public void back(View v) {
        MFGT.finish(AddContactActivity.this);
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        MFGT.finish(AddContactActivity.this);
    }
}
