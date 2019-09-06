package com.nsitd.miniperimeter.activity.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.LineLengthBean;
import com.nsitd.miniperimeter.bean.SetLineLengthBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/23.
 */
public class LineSettingActivity extends BaseActivity implements View.OnClickListener {

    private static final int UPDATE_DATA = 1;
    private static final int UPDATE_SELETION = 2;
    private List<String> lineDatas = new ArrayList<>();
    private int bLineSelectPosition = 0;
    private int aLineSelectPosition = 0;
    private String TAG = LineSettingActivity.class.getSimpleName();
    private WheelView wv_aline;
    private WheelView wv_bline;
    private HttpRequest mHttpRequest;
    private LineLengthBean mLineLengthBean = null;
    private String aLineLength = null;
    private String bLineLength = null;
    private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case UPDATE_DATA:
                    wv_aline.setItems(lineDatas);
                    wv_bline.setItems(lineDatas);
                    break;
                case UPDATE_SELETION:
                    wv_aline.setSeletion(aIndex);
                    wv_bline.setSeletion(bIndex);
                    break;
            }
        }
    };
    private int aIndex;
    private int bIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_line_setting);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }
    private void initView() {
        wv_aline = (WheelView) this.findViewById(R.id.wv_aline);
        wv_bline = (WheelView) this.findViewById(R.id.wv_bline);
        wv_aline.setOffset(2);
        wv_bline.setOffset(2);
        tv_tilte_bar.setText("线长设置");
    }

    private void initListener() {
        this.findViewById(R.id.bt_save).setOnClickListener(this);
        this.findViewById(R.id.bt_cancel).setOnClickListener(this);
        wv_aline.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                aLineLength = item.charAt(0) + "";
            }
        });
        wv_bline.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                bLineLength = item.charAt(0) + "";
            }
        });
    }

    private void initData() {
        obtainLineLength();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void obtainLineLength(){
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<=120;i++){
                    lineDatas.add(i+"米");
                }
                mHttpRequest = HttpRequest.getInstance(LineSettingActivity.this);
                mHandler.sendEmptyMessage(UPDATE_DATA);
                mHttpRequest.obtainLineLength(new HttpListenerWrap.IHttpListenerWrapCallback<LineLengthBean>() {
                    @Override
                    public void finished(HttpListenerState requestSate, String resultSate, String message, LineLengthBean
                            lineLengthBean) {
                        if (lineLengthBean != null && lineLengthBean.content!=null) {
                            mLineLengthBean = lineLengthBean;
                            aLineLength = mLineLengthBean.content.aLineLength;
                            bLineLength = mLineLengthBean.content.bLineLength;
                            aIndex = 0;
                            if(!TextUtils.isEmpty(aLineLength)){
                                aIndex = wv_aline.getItems().indexOf(aLineLength);
                                if(aIndex <0|| aIndex >120) aIndex = 0;
                            }
                            bIndex = 0;
                            if(!TextUtils.isEmpty(bLineLength)){
                                bIndex = wv_bline.getItems().indexOf(bLineLength);
                                if(bIndex <0|| bIndex >120) bIndex = 0;
                            }
                            mHandler.sendEmptyMessage(UPDATE_SELETION);
                        }
                    }
                });
            }
        }.start();
    }

    private void setLineLength(){
        if(mLineLengthBean==null){
            showShortToast("获取不到线长id.请稍后重试");
            return;
        }
        String aLineId = mLineLengthBean.content.aLineId;
        String bLineId = mLineLengthBean.content.bLineId;
        if(TextUtils.isEmpty(aLineLength)){
            showShortToast("请设置A线长");
            return;
        }
        if(TextUtils.isEmpty(bLineLength)){
            showShortToast("请设置B线长");
            return;
        }

        mHttpRequest.setLineLength(aLineId, aLineLength, bLineId, bLineLength, new HttpListenerWrap.IHttpListenerWrapCallback<SetLineLengthBean>() {
            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message, SetLineLengthBean
                    setLineLengthBean) {
                if(HttpListenerState.SERVER_FINISH==requestSate&&setLineLengthBean!=null&& CommonAttribute.RESULTSATE_SUCCESS.equals(setLineLengthBean.state)){
                    showShortToast("设置成功");
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                setLineLength();
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }
}
