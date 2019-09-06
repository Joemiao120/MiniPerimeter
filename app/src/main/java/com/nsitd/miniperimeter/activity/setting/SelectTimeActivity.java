package com.nsitd.miniperimeter.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.activity.vedio.alarm.VedioAlarmHistoryActivity;
import com.nsitd.miniperimeter.bean.DateBean;
import com.nsitd.miniperimeter.util.CalendarUtil;

import java.util.Calendar;

/**
 * Created by reimu on 16/3/29.
 */
public class SelectTimeActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "清空", "0", "查询"};
    private GridView gv_number_keyboard;
    private int currentPosition = 0;
    private TextView tv_time1;
    private TextView tv_time2;
    private TextView tv_time3;
    private TextView tv_time4;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_time);
        super.onCreate(savedInstanceState);
        initView();
        date = getIntent().getStringExtra("date");
    }

    private void initView() {
        gv_number_keyboard = (GridView) this.findViewById(R.id.gv_number_keyboard);
        gv_number_keyboard.setAdapter(new MyGridAdapter(this));
        gv_number_keyboard.setOnItemClickListener(this);
        tv_time1 = (TextView) this.findViewById(R.id.tv_time1);
        tv_time2 = (TextView) this.findViewById(R.id.tv_time2);
        tv_time3 = (TextView) this.findViewById(R.id.tv_time3);
        tv_time4 = (TextView) this.findViewById(R.id.tv_time4);
        tv_tilte_bar.setText("选择时间");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==9){
            tv_time1.setText("-");
            tv_time2.setText("-");
            tv_time3.setText("-");
            tv_time4.setText("-");
            currentPosition = 0;
        }else if(position==11){
            String time1 = tv_time1.getText().toString();
            String time2 = tv_time2.getText().toString();
            String time3 = tv_time3.getText().toString();
            String time4 = tv_time4.getText().toString();
            if(time1.equals("-")|time2.equals("-")|time3.equals("-")|time4.equals("-")){
                showShortToast("请输入完整的时间");
                return;
            }

            DateBean startTime = dataWrapper(time1,time2,time3,time4);
            DateBean endTime = CalendarUtil.getDate(startTime, Calendar.MINUTE,30);
            Intent intent = new Intent(this, VedioAlarmHistoryActivity.class);
            intent.putExtra("startTime",startTime);
            intent.putExtra("endTime",endTime);
            startActivity(intent);
        }else{
            int num = Integer.parseInt(items[position]);
            switch (currentPosition){
                case 0:
                    if(0<=num&&num<=2){
                        tv_time1.setText(num+"");
                        tv_time2.setText("-");
                        tv_time3.setText("-");
                        tv_time4.setText("-");
                        currentPosition = (currentPosition+1)%4;
                    }

                    break;
                case 1:
                    int hourTime = Integer.parseInt(tv_time1.getText().toString());
                    if(hourTime==2){
                        if(num<=3){
                            tv_time2.setText(num+"");
                            currentPosition = (currentPosition+1)%4;
                        }
                    }else{
                        tv_time2.setText(num+"");
                        currentPosition = (currentPosition+1)%4;
                    }

                    break;
                case 2:
                    if(num<=5){
                        tv_time3.setText(num+"");
                        currentPosition = (currentPosition+1)%4;
                    }
                    break;
                case 3:
                    tv_time4.setText(num+"");
                    currentPosition = (currentPosition+1)%4;
                    break;
            }
        }
    }

    private DateBean dataWrapper(String time1, String time2, String time3, String time4) {

        DateBean dateBean = new DateBean();
        String[] dates = date.split("-");
        dateBean.year = Integer.parseInt(dates[0]);
        dateBean.month = Integer.parseInt(dates[1]);
        dateBean.day = Integer.parseInt(dates[2]);
        dateBean.hour = Integer.parseInt(time1+time2);
        dateBean.minute = Integer.parseInt(time3+time4);
        dateBean.second = 0;
        return dateBean;
    }

    private class MyGridAdapter extends BaseAdapter {
        private Context context;

        public MyGridAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public String getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_select_time_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.tv_keyborder = (TextView) convertView.findViewById(R.id.tv_keyborder);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_keyborder.setText(getItem(position));
            return convertView;
        }

        class ViewHolder{
            TextView tv_keyborder;
        }
    }
}
