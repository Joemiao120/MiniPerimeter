package com.nsitd.miniperimeter.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;

/**
 * Created by reimu on 16/3/22.
 */
public class PowerSettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_power;
    private int selectPower =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_power_setting);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        rg_power = (RadioGroup) this.findViewById(R.id.rg_power);
    }

    private void initListener() {
        rg_power.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_hign:
                selectPower = 3;
                break;
            case R.id.rb_middle:
                selectPower = 2;
                break;
            case R.id.rb_low:
                selectPower = 1;
                break;
        }
    }

    public void save(View view){
        if(selectPower==-1){
            showShortToast("请选择功率");
            return;
        }
    }
}
