package com.nsitd.miniperimeter.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.FnCountInfoBean;
import com.nsitd.miniperimeter.util.CommonAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/4/20.
 */
public class LineFaultActivity extends BaseActivity {

    private ListView lv_fault;
    private List<String> faultList = new ArrayList<>();
    private MyAdapter mAdapter = null;
    private List<FnCountInfoBean.FnInfo> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_line_fault);
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        FnCountInfoBean.FnCountInfo fnCountInfo = (FnCountInfoBean.FnCountInfo) getIntent().getSerializableExtra("data");
        if(fnCountInfo!=null&&fnCountInfo.fnerrorInfos.size()>0){
            list = fnCountInfo.fnerrorInfos;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        String lineType = getIntent().getStringExtra(CommonAttribute.LINE_TYPE);
        if (TextUtils.equals(lineType, CommonAttribute.LINE_TYPE_B)) {
            tv_tilte_bar.setText("B线");
        } else tv_tilte_bar.setText("A线");
        lv_fault = (ListView) this.findViewById(R.id.lv_fault);
        lv_fault.setAdapter(mAdapter);
    }

    private class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public FnCountInfoBean.FnInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_fncount_info_item,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item = (TextView) convertView.findViewById(R.id.lv_fault);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            FnCountInfoBean.FnInfo fnInfo = getItem(position);
            viewHolder.tv_item.setText("FN序号:"+fnInfo.index+":"+fnInfo.errormsg);
            return convertView;
        }

        private class ViewHolder{
            TextView tv_item;
        }
    }
}
