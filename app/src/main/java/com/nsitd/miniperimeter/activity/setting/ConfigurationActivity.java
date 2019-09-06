package com.nsitd.miniperimeter.activity.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.AddLineRelationBean;
import com.nsitd.miniperimeter.bean.ObtainLineRelationBean;
import com.nsitd.miniperimeter.bean.UpdateLineRelationBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/28.
 */
public class ConfigurationActivity extends BaseActivity implements View.OnClickListener {

    private static final int UPDATE_ALIAS = 1;
    private WheelView wv_aline_start;
    private WheelView wv_aline_end;
    private WheelView wv_bline_start;
    private WheelView wv_bline_end;
    private List<String> lineDatas = new ArrayList<>();
    private int aLineMax = 120;
    private int bLineMax = 120;
    private String aLineStart = null;
    private String bLineStart = null;
    private String aLineEnd = null;
    private String bLineEnd = null;
    private EditText et_camera_alias;
    private String alias;
    private HttpRequest mHttpRequest;
    private String resourceId = null;
    private String   resourceType = "video";
    private ObtainLineRelationBean relationBean;
    private String alineId;
    private String blineId;
    private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case UPDATE_ALIAS:
                    String resourceName = relationBean.content.resourceName;
                    if(!TextUtils.isEmpty(resourceName))et_camera_alias.setText(resourceName);
                    if(relationBean.content.lineRelationList!=null&&relationBean.content.lineRelationList.size()>0){
                        ObtainLineRelationBean.LineRelation lineRelation = relationBean.content.lineRelationList.get(0);
                        if(!TextUtils.isEmpty(lineRelation.alineStartNum))wv_aline_start.setSeletion(Integer.parseInt(lineRelation.alineStartNum));
                        if(!TextUtils.isEmpty(lineRelation.alineEndNum))wv_aline_end.setSeletion(Integer.parseInt(lineRelation.alineEndNum));
                        if(!TextUtils.isEmpty(lineRelation.blineStartNum))wv_bline_start.setSeletion(Integer.parseInt(lineRelation.blineStartNum));
                        if(!TextUtils.isEmpty(lineRelation.blineEndNum))wv_bline_end.setSeletion(Integer.parseInt(lineRelation.blineEndNum));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_configuration);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        mHttpRequest = HttpRequest.getInstance(this);
        for (int i = 0; i <= 120; i++) {
            lineDatas.add(i + "米");
        }
        wv_aline_start.setItems(lineDatas);
        wv_bline_start.setItems(lineDatas);
        wv_aline_end.setItems(lineDatas);
        wv_bline_end.setItems(lineDatas);
        resourceId = "ef14b2ba4253e640799a3cb034797579";
//        resourceId = getIntent().getStringExtra(CommonAttribute.VedioCameraId);
        obtainLineRelation();
    }

    private void obtainLineRelation() {
        new Thread(){
            @Override
            public void run() {
                mHttpRequest.obtainLineRelation(resourceId, resourceType, new HttpListenerWrap
                        .IHttpListenerWrapCallback<ObtainLineRelationBean>() {


                    @Override
                    public void finished(HttpListenerState requestSate, String resultSate, String message,
                                         ObtainLineRelationBean obtainLineRelationBean) {
                        if (requestSate == HttpListenerState.SERVER_FINISH && CommonAttribute
                                .RESULTSATE_SUCCESS.equals
                                        (resultSate)) {
                            if (obtainLineRelationBean != null) {
                                relationBean = obtainLineRelationBean;
                                mHandler.sendEmptyMessage(UPDATE_ALIAS);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private void initView() {
        wv_aline_start = (WheelView) findViewById(R.id.wv_aline_start);
        wv_aline_end = (WheelView) findViewById(R.id.wv_aline_end);
        wv_bline_start = (WheelView) findViewById(R.id.wv_bline_start);
        wv_bline_end = (WheelView) findViewById(R.id.wv_bline_end);

        wv_aline_start.setOffset(2);
        wv_aline_end.setOffset(2);
        wv_bline_start.setOffset(2);
        wv_bline_end.setOffset(2);

        et_camera_alias = (EditText) this.findViewById(R.id.et_camera_alias);
//        wv_aline_end.setItems(Arrays.asList(PLANETS));
//        wv_bline_start.setItems(Arrays.asList(PLANETS));
//        wv_bline_end.setItems(Arrays.asList(PLANETS));
        tv_tilte_bar.setText("配置");

    }

    private void initListener() {
        this.findViewById(R.id.bt_save).setOnClickListener(this);
        this.findViewById(R.id.bt_cancel).setOnClickListener(this);

        wv_aline_start.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                List<String> temp = new ArrayList<String>();
                int offSet = wv_aline_end.getOffset();
//                int position = offSet;
//                if(!TextUtils.isEmpty(aLineEnd)){
//                   int index = wv_aline_end.getItems().indexOf(aLineEnd);
//                    if(index>=0)position = index;
//                }


                for (int i = selectedIndex - offSet; i <= aLineMax; i++) {
                    temp.add(i + "米");
                }
                wv_aline_end.setItems(temp);
                wv_aline_end.setSeletion(0);
//                wv_aline_end.setSeletion(position);
                aLineStart = item;
                aLineEnd = null;
            }
        });
        wv_aline_end.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                aLineEnd = item;
            }
        });
        wv_bline_start.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                List<String> temp = new ArrayList<String>();
                for (int i = selectedIndex - wv_bline_end.getOffset(); i < bLineMax; i++) {
                    temp.add(i + "米");
                }
                wv_bline_end.setItems(temp);
                wv_bline_end.setSeletion(0);
                bLineStart = item;
                bLineEnd = null;
            }
        });
        wv_bline_end.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                bLineEnd = item;
            }
        });

    }

    private boolean checkData() {
        alias = et_camera_alias.getText().toString();
        if (TextUtils.isEmpty(alias)) {
            showShortToast(R.string.please_camera_alias);
            return false;
        }
//        aLineStart = wv_aline_start.getSeletedItem();
        if (TextUtils.isEmpty(aLineStart)) {
            showShortToast("请选择A线长起始位置");
            return false;
        }
//        aLineEnd = wv_aline_end.getSeletedItem();
        if (TextUtils.isEmpty(aLineEnd)) {
            showShortToast("请选择B线长结束位置");
            return false;
        }
//        bLineStart = wv_bline_start.getSeletedItem();
        if (TextUtils.isEmpty(bLineStart)) {
            showShortToast("请选择B线长起始位置");
            return false;
        }
//        bLineEnd = wv_bline_end.getSeletedItem();
        if (TextUtils.isEmpty(bLineEnd)) {
            showShortToast("请选择B线长结束位置");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                if (checkData()) {
                    setLineRelation();
                }
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }

    private void setLineRelation() {
        String aLineStartStr = aLineStart.replace("米","");
        String aLineEndStr = aLineEnd.replace("米","");
        String bLineStartStr = bLineStart.replace("米","");
        String bLineEndStr = bLineEnd.replace("米","");

//        if (relationBean != null && relationBean.content != null && relationBean.content.lineRelationList != null&&relationBean.content.lineRelationList.size()>0) {
            String lineRelationId = relationBean.content.lineRelationList.get(0).lineRelationId;
            alineId = relationBean.content.lineRelationList.get(0).alineId;
            blineId = relationBean.content.lineRelationList.get(0).blineId;
//            updateLineRelation(alias,aLineStartStr, aLineEndStr, bLineStartStr, bLineEndStr, lineRelationId);
//        } else {
//            alineId = "";
//            blineId = "";
            addLineRelation(alias,aLineStartStr, aLineEndStr, bLineStartStr, bLineEndStr);
//        }
    }


    private void addLineRelation(String resourceName,String aLineStartStr, String aLineEndStr, String bLineStartStr, String bLineEndStr) {
        mHttpRequest.addLineRelation(resourceName,resourceId, resourceType, alineId, aLineStartStr, aLineEndStr, blineId, bLineStartStr,
                bLineEndStr, new HttpListenerWrap.IHttpListenerWrapCallback<AddLineRelationBean>() {


                    @Override
                    public void finished(HttpListenerState requestSate, String resultSate, String message,
                                         AddLineRelationBean addLineRelationBean) {
                        if (HttpListenerState.SERVER_FINISH == requestSate && addLineRelationBean != null &&
                                TextUtils.equals
                                (CommonAttribute.RESULTSATE_SUCCESS, resultSate)) {
                            showShortToast("设置成功!");
                            finish();
                        }else if(!TextUtils.isEmpty(message)){
                            showShortToast(message);
                        }else{
                            showShortToast("添加失败");
                        }
                    }
                });
    }

    private void updateLineRelation(String resourceName,String aLineStartStr, String aLineEndStr, String bLineStartStr, String
            bLineEndStr, String lineRelationId) {

        mHttpRequest.updateLineRelation(resourceName,resourceId, resourceType, alineId, aLineStartStr, aLineEndStr, blineId, bLineStartStr,
                bLineEndStr, lineRelationId, new HttpListenerWrap.IHttpListenerWrapCallback<UpdateLineRelationBean>() {
                    @Override
                    public void finished(HttpListenerState requestSate, String resultSate, String message,
                                         UpdateLineRelationBean updateLineRelationBean) {
                        if (HttpListenerState.SERVER_FINISH == requestSate && updateLineRelationBean != null &&
                                TextUtils
                                .equals(CommonAttribute.RESULTSATE_SUCCESS, resultSate)) {
                            showShortToast("设置成功!");
                            finish();
                        }
                    }
                });
    }


}
