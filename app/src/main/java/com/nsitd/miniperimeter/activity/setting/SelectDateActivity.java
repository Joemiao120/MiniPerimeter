package com.nsitd.miniperimeter.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.util.CalendarUtil;

/**
 * Created by reimu on 16/3/29.
 */
public class SelectDateActivity extends BaseActivity implements View.OnClickListener{

    private Button bt_date1;
    private Button bt_date2;
    private Button bt_date3;
    private Button bt_date4;
    private Button bt_date5;
    private Button bt_date6;
    private Button bt_date7;
    private Button bt_date8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_date);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        bt_date3.setText(CalendarUtil.getDate(-2));
        bt_date4.setText(CalendarUtil.getDate(-3));
        bt_date5.setText(CalendarUtil.getDate(-4));
        bt_date6.setText(CalendarUtil.getDate(-5));
        bt_date7.setText(CalendarUtil.getDate(-6));
        bt_date8.setText(CalendarUtil.getDate(-7));
    }

    private void initListener() {
        bt_date1.setOnClickListener(this);
        bt_date2.setOnClickListener(this);
        bt_date3.setOnClickListener(this);
        bt_date4.setOnClickListener(this);
        bt_date5.setOnClickListener(this);
        bt_date6.setOnClickListener(this);
        bt_date7.setOnClickListener(this);
        bt_date8.setOnClickListener(this);
    }

    private void initView() {
        bt_date1 = (Button) this.findViewById(R.id.bt_date1);
        bt_date2 = (Button) this.findViewById(R.id.bt_date2);
        bt_date3 = (Button) this.findViewById(R.id.bt_date3);
        bt_date4 = (Button) this.findViewById(R.id.bt_date4);
        bt_date5 = (Button) this.findViewById(R.id.bt_date5);
        bt_date6 = (Button) this.findViewById(R.id.bt_date6);
        bt_date7 = (Button) this.findViewById(R.id.bt_date7);
        bt_date8 = (Button) this.findViewById(R.id.bt_date8);
        tv_tilte_bar.setText("选择日期");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_date1:
            case R.id.bt_date2:
            case R.id.bt_date3:
            case R.id.bt_date4:
            case R.id.bt_date5:
            case R.id.bt_date6:
            case R.id.bt_date7:
            case R.id.bt_date8:
                Button button = (Button) v;
                String date = button.getText().toString().trim();
                if(TextUtils.equals("今天",date)){
                    date = CalendarUtil.getDate(0);
                }else if(TextUtils.equals("昨天",date)){
                    date = CalendarUtil.getDate(-1);
                }
                Intent intent = new Intent(this, SelectTimeActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
                break;
        }
    }
}
