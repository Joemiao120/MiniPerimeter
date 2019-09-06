package com.nsitd.miniperimeter.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.AboutActivity;
import com.nsitd.miniperimeter.activity.DeviceStatusActivity;
import com.nsitd.miniperimeter.activity.LoginActivity;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.DeviceAlarmPushMsgBean;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.PrefUtils;

/**
 * Created by reimu on 16/3/22.
 */
public class SystemSettingActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_red_point;
    private int isalarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_setting);
        super.onCreate(savedInstanceState);
        tv_tilte_bar.setText("系统配置");
        isalarm = PrefUtils.getInt(CommonAttribute.PRE_ISALARM,CommonAttribute.HEX_DEFALUT);
        iv_red_point = (ImageView) this.findViewById(R.id.iv_red_point);
        if(isalarm>CommonAttribute.HEX_DEFALUT){
            iv_red_point.setVisibility(View.VISIBLE);
        }else{
            iv_red_point.setVisibility(View.GONE);
        }
        initListener();
    }

    private void initListener() {
        this.findViewById(R.id.iv_device_status).setOnClickListener(this);
        this.findViewById(R.id.iv_modify_password).setOnClickListener(this);
        this.findViewById(R.id.iv_line_length).setOnClickListener(this);
        this.findViewById(R.id.iv_about).setOnClickListener(this);
        this.findViewById(R.id.iv_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_device_status:
                if(isalarm>CommonAttribute.HEX_DEFALUT){
                    isalarm = CommonAttribute.HEX_DEFALUT;
                    PrefUtils.setInt(CommonAttribute.PRE_ISALARM, isalarm);
                    iv_red_point.setVisibility(View.GONE);
                }
                startActivity(new Intent(this, DeviceStatusActivity.class));
                break;
            case R.id.iv_modify_password:
                startActivity(new Intent(this,ModifyPasswordActivity.class));
                break;
            case R.id.iv_line_length:
                startActivity(new Intent(this,LineSettingActivity.class));
                break;
            case R.id.iv_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.iv_logout:
                showDialog();
                break;

        }
    }


    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                //删除登陆信息
                PrefUtils.setBoolean(CommonAttribute.PRE_IS_AUTO_LOGIN, false);
                //返回登陆界面
                startActivity(new Intent(SystemSettingActivity.this, LoginActivity.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }


    @Override
    protected void onMessageAtUIThread(DeviceAlarmPushMsgBean deviceAlarmPushMsgBean) {
        super.onMessageAtUIThread(deviceAlarmPushMsgBean);
        isalarm = PrefUtils.getInt(CommonAttribute.PRE_ISALARM,CommonAttribute.HEX_DEFALUT);
        iv_red_point.setVisibility(isalarm>CommonAttribute.HEX_DEFALUT?View.VISIBLE:View.GONE);
    }
}
