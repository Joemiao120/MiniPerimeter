package com.nsitd.miniperimeter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.activity.vedio.alarm.VedioAlarmHistoryActivity;
import com.nsitd.miniperimeter.adapter.MyGridAdapter;
import com.nsitd.miniperimeter.bean.DateBean;
import com.nsitd.miniperimeter.bean.DeleteSomedayHistoryAlarmBean;
import com.nsitd.miniperimeter.bean.HistoryAlarmBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CalendarUtil;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.view.MyListView;
import com.nsitd.miniperimeter.view.MyStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/30.
 */
public class HistoryAlarmActivity extends BaseActivity implements View.OnClickListener {
    private MyListView lv_before_day;
    private MyListAdapter before_day_adapter;
    private HttpRequest mHttpRequest;
    private int selectItemId = -1;
    private LinearLayout ll_today;
    private LinearLayout ll_yesterday;
    private LinearLayout ll_before_day;
    private TextView tv_today_date;
    private TextView tv_before_day_date;
    private TextView tv_yesterday_date;
    private TextView tv_empty;
    private long preTime = 0l;
    private long time = 0l;
    private final String pageSize = "12";
    /*加载模式.true代表是添加,false代表设置(set),*/
    private boolean isMore = false;
    private View footer;
    private RecyclerView rv_today;
    private RecyclerView rv_yesterday;
    private MyGridAdapter today_adapter;
    private MyGridAdapter yesterday_adapter;
    private TextView tv_before_day_totalNumber;
    private TextView tv_today_totalNumber;
    private TextView tv_yesterday_totalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_alarm);
        super.onCreate(savedInstanceState);
        initView();
        initListener();

        mHttpRequest = HttpRequest.getInstance(this);
        preTime = System.currentTimeMillis();
        obtainHistoryAlarm();
    }

    public void startActivity(HistoryAlarmBean.HistroyAlarm histroyAlarm) {
        String startTimeStr = histroyAlarm.startTime;
        String endTimeStr = histroyAlarm.endTime;
        DateBean startTime = new DateBean(startTimeStr);
        DateBean endTime = new DateBean(endTimeStr);
        Intent intent = new Intent(this, VedioAlarmHistoryActivity.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("isAlarm", true);
        startActivity(intent);
    }

    private void initListener() {
        this.findViewById(R.id.iv_today_delete).setOnClickListener(this);
        this.findViewById(R.id.iv_yesterday_delete).setOnClickListener(this);
        this.findViewById(R.id.iv_before_day_delete).setOnClickListener(this);


        today_adapter.setOnItemClickLitener(new MyGridAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                if (today_adapter.isMoreItem(position)) {
                    isMore = true;
                    String pageNo = today_adapter.pageNoPlus() + "";
                    obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_TODAY, isMore);
                    LogFactory.i(TAG, "pageNo:" + pageNo);
                } else {
                    HistoryAlarmBean.HistroyAlarm histroyAlarm = today_adapter.getItem(position);
                    startActivity(histroyAlarm);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        yesterday_adapter.setOnItemClickLitener(new MyGridAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                if (yesterday_adapter.isMoreItem(position)) {
                    isMore = true;
                    String pageNo = yesterday_adapter.pageNoPlus() + "";
                    obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_YESTERDAY, isMore);
                    LogFactory.i(TAG, "pageNo:" + pageNo);
                } else {
                    HistoryAlarmBean.HistroyAlarm histroyAlarm = yesterday_adapter.getItem(position);
                    startActivity(histroyAlarm);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }


        });


        lv_before_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == before_day_adapter.getCount()) {
                    String pageNo = before_day_adapter.pageNoPlus() + "";
                    LogFactory.i(TAG, "pageNo:" + pageNo);
                    obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_BEFOREDAY, true);
                } else {
                    HistoryAlarmBean.HistroyAlarm histroyAlarm = before_day_adapter.getItem(position);
                    startActivity(histroyAlarm);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        rv_today = (RecyclerView) this.findViewById(R.id.rv_today);
        today_adapter = new MyGridAdapter(this, rv_today);
        rv_today.setAdapter(today_adapter);
        rv_today.setLayoutManager(new MyStaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.HORIZONTAL, this));
        // 设置item动画
        rv_today.setItemAnimator(new DefaultItemAnimator());

        rv_yesterday = (RecyclerView) this.findViewById(R.id.rv_yesterday);
        yesterday_adapter = new MyGridAdapter(this, rv_yesterday);
        rv_yesterday.setAdapter(yesterday_adapter);
        rv_yesterday.setLayoutManager(new MyStaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.HORIZONTAL, this));
        // 设置item动画
        rv_yesterday.setItemAnimator(new DefaultItemAnimator());


        tv_today_totalNumber = (TextView) this.findViewById(R.id.tv_today_totalNumber);
        tv_yesterday_totalNumber = (TextView) this.findViewById(R.id.tv_yesterday_totalNumber);
        tv_before_day_totalNumber = (TextView) this.findViewById(R.id.tv_before_day_totalNumber);

        lv_before_day = (MyListView) this.findViewById(R.id.lv_before_day);
        before_day_adapter = new MyListAdapter(this);
        lv_before_day.setAdapter(before_day_adapter);

        ll_today = (LinearLayout) this.findViewById(R.id.ll_today);
        ll_yesterday = (LinearLayout) this.findViewById(R.id.ll_yesterday);
        ll_before_day = (LinearLayout) this.findViewById(R.id.ll_before_day);
        tv_today_date = (TextView) this.findViewById(R.id.tv_today_date);
        tv_before_day_date = (TextView) this.findViewById(R.id.tv_before_day_date);
        tv_yesterday_date = (TextView) this.findViewById(R.id.tv_yesterday_date);
        String today = CalendarUtil.getDate(0);
        String yesterday = CalendarUtil.getDate(-1);
        String beforeDay = CalendarUtil.getDate(-2) + "~" + CalendarUtil.getDate(-6);
        tv_today_date.setText(today);
        tv_before_day_date.setText(beforeDay);
        tv_yesterday_date.setText(yesterday);

        tv_empty = (TextView) this.findViewById(R.id.tv_empty);

        footer = LayoutInflater.from(this).inflate(R.layout.pull_load_list_footer, null);
    }

    private void obtainHistoryAlarm() {
        String pageNo = "1";
        obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_TODAY, false);
        obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_YESTERDAY, false);
        obtianHistoryAlarm(pageNo, CommonAttribute.TIME_TYPE_BEFOREDAY, false);
    }

    private void obtianHistoryAlarm(String pageNo, final String timeType, final boolean isMore) {
        mHttpRequest.obtainHistroyAlarm(pageNo, pageSize, timeType, new HttpListenerWrap
                .IHttpListenerWrapCallback<HistoryAlarmBean>() {
            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message, HistoryAlarmBean
                    historyAlarmBean) {
                if (requestSate == HttpListenerState.SERVER_FINISH && CommonAttribute.RESULTSATE_SUCCESS.equals
                        (resultSate)) {
                    if (historyAlarmBean != null && historyAlarmBean.content != null && historyAlarmBean.content
                            .alarmArray != null) {
                        time = System.currentTimeMillis();
                        LogFactory.i(TAG, "obtainHistoryAlarm:" + (time - preTime));
                        preTime = System.currentTimeMillis();
                        showData(historyAlarmBean.content, timeType, isMore);
                    }
                }
            }
        });
    }

    private void showDialog(final String timeType) {
        new AlertDialog.Builder(this).setTitle("确认删除？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        deleteHistoryAlarm(timeType);
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    private void showData(HistoryAlarmBean.HistroyAlarmList bean, String timeType, boolean isMore) {

        time = System.currentTimeMillis();
        LogFactory.i(TAG, "for循环:" + (time - preTime));
        preTime = System.currentTimeMillis();
        if (CommonAttribute.TIME_TYPE_TODAY.equals(timeType)) {
            int count = today_adapter.getItemCount();
            today_adapter.setData(bean, isMore);
            //明明滑动3个item距离.却滑动到最后
            if (count < today_adapter.getItemCount()) {
                rv_today.scrollToPosition(count+3);
            }
            tv_today_totalNumber.setText("(共"+today_adapter.getTotalNumber()+"条)");
            if (today_adapter.getItemCount() > 0) {
                ll_today.setVisibility(View.VISIBLE);
            } else {
                ll_today.setVisibility(View.GONE);
            }
        } else if (CommonAttribute.TIME_TYPE_YESTERDAY.equals(timeType)) {
            int count = yesterday_adapter.getItemCount();
            yesterday_adapter.setData(bean, isMore);
            if (count < yesterday_adapter.getItemCount()) {
                rv_yesterday.scrollToPosition(count+3);
            }
            tv_yesterday_totalNumber.setText("(共"+yesterday_adapter.getTotalNumber()+"条)");
            if (yesterday_adapter.getItemCount() > 0) {
                ll_yesterday.setVisibility(View.VISIBLE);
            } else {
                ll_yesterday.setVisibility(View.GONE);
            }
        } else if (CommonAttribute.TIME_TYPE_BEFOREDAY.equals(timeType)) {
            before_day_adapter.setData(bean, true);
            before_day_adapter.notifyDataSetChanged();
            tv_before_day_totalNumber.setText("(共" + before_day_adapter.getTotalNumber() + "条)");
            boolean hasMore = before_day_adapter.hasMore();
            if (hasMore) {
                lv_before_day.removeFooterView(footer);
                lv_before_day.addFooterView(footer);
            } else {
                lv_before_day.removeFooterView(footer);
            }
            if (before_day_adapter.getCount() <= 0)
                ll_before_day.setVisibility(View.GONE);
            else ll_before_day.setVisibility(View.VISIBLE);
        }


        if (today_adapter.getItemCount() <= 0 && yesterday_adapter.getItemCount() <= 0 && before_day_adapter
                .getCount() <= 0) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        selectItemId = v.getId();
        String alarmIds = "";
        List<HistoryAlarmBean.HistroyAlarm> list = null;
        String deleteTimeType = "";
        switch (v.getId()) {
            case R.id.iv_before_day_delete:
                deleteTimeType = CommonAttribute.TIME_TYPE_BEFOREDAY;
                break;
            case R.id.iv_yesterday_delete:
                deleteTimeType = CommonAttribute.TIME_TYPE_YESTERDAY;
                break;
            case R.id.iv_today_delete:
                deleteTimeType = CommonAttribute.TIME_TYPE_TODAY;
                break;
        }
        if (!TextUtils.isEmpty(deleteTimeType))
            showDialog(deleteTimeType);
    }

    private void deleteHistoryAlarm(final String deleteTimeType) {
        mHttpRequest.deleteSomedayHistroyAlarm(deleteTimeType, new HttpListenerWrap
                .IHttpListenerWrapCallback<DeleteSomedayHistoryAlarmBean>() {


            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message,
                                 DeleteSomedayHistoryAlarmBean deleteSomedayHistoryAlarmBean) {
                if (requestSate == HttpListenerState.SERVER_FINISH && CommonAttribute.RESULTSATE_SUCCESS.equals
                        (resultSate)) {
                    showData(null, deleteTimeType, false);
                }
            }
        });
    }


    private class MyListAdapter extends BaseAdapter {
        private Context context;
        private long la_preTime = 0l;
        private long la_time = 0l;
        private int pageNo = 1;
        private int totalNumber = 0;
        private List<HistoryAlarmBean.HistroyAlarm> alarmArray = new ArrayList<>();

        public MyListAdapter(Context context) {
            this.context = context;
            pageNo = 1;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int pageNoPlus() {
            pageNo++;
            return pageNo;
        }

        public void setData(HistoryAlarmBean.HistroyAlarmList bean, boolean isMore) {
            if (!isMore) alarmArray.clear();
            addData(bean);
        }

        public void addData(HistoryAlarmBean.HistroyAlarmList bean) {
            if (bean != null) {
                if (!TextUtils.isEmpty(bean.totalNumber)) {
                    totalNumber = Integer.parseInt(bean.totalNumber);
                } else {
                    totalNumber = 0;
                }
                if (bean.alarmArray != null && bean.alarmArray.size() > 0) {
                    alarmArray.addAll(bean.alarmArray);
                }
            } else {
                totalNumber = 0;
            }
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        @Override
        public int getCount() {
            return alarmArray == null ? 0 : alarmArray.size();
        }

        @Override
        public HistoryAlarmBean.HistroyAlarm getItem(int position) {
            return alarmArray == null ? null : alarmArray.get(position);
        }

        public boolean hasMore() {
            if (alarmArray.size() < totalNumber) return true;
            return false;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoryAlarmBean.HistroyAlarm histroyAlarm = getItem(position);
            if (position == 0) la_preTime = System.currentTimeMillis();
            if (position == getCount() - 1) {
                la_time = System.currentTimeMillis();
                LogFactory.i(TAG, "LISTVIEW:" + (la_time - la_preTime));
            }
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_history_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_cotent = (TextView) convertView.findViewById(R.id.tv_cotent);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 0) {
                viewHolder.rl_item.setBackgroundColor(getResources().getColor(R.color.history_alarm_list_item_bg));
            } else {
                viewHolder.rl_item.setBackgroundColor(getResources().getColor(R.color.white));
            }

            viewHolder.tv_time.setText(histroyAlarm.startTime);
            viewHolder.tv_cotent.setText(histroyAlarm.location);

            return convertView;
        }

        public void pagePlus() {
            pageNo++;
        }

        class ViewHolder {
            RelativeLayout rl_item;
            TextView tv_time;
            TextView tv_cotent;
        }
    }


}
