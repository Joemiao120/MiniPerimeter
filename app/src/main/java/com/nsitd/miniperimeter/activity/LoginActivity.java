package com.nsitd.miniperimeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.nsitd.miniperimeter.MyApplication;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.activity.vedio.VedioHomeActivity;
import com.nsitd.miniperimeter.bean.UserLoginBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap.IHttpListenerWrapCallback;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.util.MD5Encoder;
import com.nsitd.miniperimeter.util.PrefUtils;

/**
 * Created by reimu on 16/3/22.
 */
public class LoginActivity extends BaseActivity {

    private EditText et_password;
    private HttpRequest httpRequest;
    private EditText et_host;
    private String host = "";
    private CheckBox cb_is_save_password;
    private CheckBox cb_is_auto_login;
    private boolean isSavePassword;
    private boolean isAutoLogin;
    private String oldPassword;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        httpRequest = HttpRequest.getInstance(this);
        initData();
        initListener();
        MyApplication.closeAllNotThis(LoginActivity.this.getClass());
    }

    private void initListener() {
        cb_is_save_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSavePassword = isChecked;
                if (!isSavePassword && isAutoLogin) cb_is_auto_login.setChecked(false);
            }
        });
        cb_is_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAutoLogin = isChecked;
                if (!isSavePassword && isAutoLogin) cb_is_save_password.setChecked(true);
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String password = et_password.getText().toString();
                    if (TextUtils.equals(password, oldPassword))
                        et_password.setText("");
                }
            }
        });
    }

    private void initData() {
        host = CommonAttribute.getIP();
        et_host.setText(host);
        isSavePassword = PrefUtils.getBoolean(CommonAttribute.PRE_IS_SAVE_PASSWORD, false);
        isAutoLogin = PrefUtils.getBoolean(CommonAttribute.PRE_IS_AUTO_LOGIN, false);
        oldPassword = PrefUtils.getString(CommonAttribute.PRE_PASSWORD, "");

        et_password.setText(oldPassword);
        cb_is_auto_login.setChecked(isAutoLogin);
        cb_is_save_password.setChecked(isSavePassword);
        if(isAutoLogin){
            login(oldPassword);
        }
    }

    private void initView() {
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_host = (EditText) this.findViewById(R.id.et_host);
        cb_is_save_password = (CheckBox) this.findViewById(R.id.cb_is_save_password);
        cb_is_auto_login = (CheckBox) this.findViewById(R.id.cb_is_auto_login);
    }

    public void login(View view) {

        password = et_password.getText().toString().trim();
        String host = et_host.getText().toString().trim();
        PrefUtils.setString(CommonAttribute.PRE_HOST_ADDRESS, host);
        if (TextUtils.isEmpty(host)) {
            showShortToast(R.string.please_host_hint);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showShortToast(R.string.please_password_hint);
            return;
        }
        if (!TextUtils.equals(oldPassword, password)) {
            password = MD5Encoder.encode(password);
        }
        login(password);
    }

    private void login(String password) {
        IHttpListenerWrapCallback<UserLoginBean> callback = new IHttpListenerWrapCallback<UserLoginBean>() {
            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message, UserLoginBean
                    userLoginBean) {

                if (requestSate == HttpListenerState.SERVER_FINISH && CommonAttribute.RESULTSATE_SUCCESS.equals
                        (resultSate)) {
                    LogFactory.i(TAG, userLoginBean.toString());
                    saveLoginInfo();
                    startActivity(new Intent(LoginActivity.this, VedioHomeActivity.class));
                    finish();
                } else if (userLoginBean != null && !TextUtils.isEmpty(userLoginBean.message)) {
                    showShortToast(userLoginBean.message);
                } else {
                    showShortToast("登陆失败");
                }
            }
        };
        httpRequest.login(password, callback);
    }

    private void saveLoginInfo() {
        PrefUtils.setBoolean(CommonAttribute.PRE_IS_SAVE_PASSWORD, isSavePassword);
        PrefUtils.setBoolean(CommonAttribute.PRE_IS_AUTO_LOGIN, isAutoLogin);
        if (isSavePassword) {
            PrefUtils.setString(CommonAttribute.PRE_PASSWORD, password);
        } else {
            PrefUtils.setString(CommonAttribute.PRE_PASSWORD, "");
        }
    }


}
