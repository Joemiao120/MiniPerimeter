package com.nsitd.miniperimeter.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.ModifyPasswordBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.MD5Encoder;

/**
 * Created by reimu on 16/3/22.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_password;
    private EditText et_newPassword;
    private EditText et_newPassword_affirm;
    private HttpRequest mHttpRequest;
    private String password;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_password);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        mHttpRequest = HttpRequest.getInstance(this);
    }

    private void initListener() {
        this.findViewById(R.id.bt_save).setOnClickListener(this);
        this.findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    private void initView() {
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_newPassword = (EditText) this.findViewById(R.id.et_newPassword);
        et_newPassword_affirm = (EditText) this.findViewById(R.id.et_newPassword_affirm);
        tv_tilte_bar.setText("修改密码");
    }

    private boolean checkInput(){
        password = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(password)){
            showShortToast(R.string.please_password_hint);
            return false;
        }
        newPassword = et_newPassword.getText().toString().trim();
        if(TextUtils.isEmpty(newPassword)){
            showShortToast(R.string.please_newPassword_hint);
            return false;
        }
        String newPassword_affirm = et_newPassword_affirm.getText().toString().trim();
        if(TextUtils.isEmpty(newPassword_affirm)){
            showShortToast(R.string.please_newPassword_affirm_hint);
            return false;
        }
        if(!TextUtils.equals(newPassword,newPassword_affirm)){
            showShortToast("两次密码输入不一致,请重新输入");
            return false;
        }
        return true;
    }

    private void modifyPassword(){
        String passwordMd5 = MD5Encoder.encode(password);
        String newPasswordMd5 = MD5Encoder.encode(newPassword);
        mHttpRequest.modifyPassword(passwordMd5, newPasswordMd5, new HttpListenerWrap.IHttpListenerWrapCallback<ModifyPasswordBean>() {

            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message, ModifyPasswordBean
                    modifyPasswordBean) {
                if(HttpListenerState.SERVER_FINISH==requestSate&& CommonAttribute.RESULTSATE_SUCCESS.equals(resultSate)){
                    showShortToast("密码修改成功");
                    finish();
                }else{
                    String msg = "密码修改失败";
                    if(!TextUtils.isEmpty(message)){
                        msg = message;
                    }
                    showShortToast(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save:
                if(checkInput()){
                    modifyPassword();
                }
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }
}
