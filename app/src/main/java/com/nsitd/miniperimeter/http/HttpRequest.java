package com.nsitd.miniperimeter.http;

import android.content.Context;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.request.param.HttpMethods;
import com.nsitd.miniperimeter.bean.AddLineRelationBean;
import com.nsitd.miniperimeter.bean.AddLineRelationParam;
import com.nsitd.miniperimeter.bean.AddLineRelationRequest;
import com.nsitd.miniperimeter.bean.DeleteHistoryAlarmBean;
import com.nsitd.miniperimeter.bean.DeleteHistoryAlarmParam;
import com.nsitd.miniperimeter.bean.DeleteHistoryAlarmRequest;
import com.nsitd.miniperimeter.bean.DeleteSomedayHistoryAlarmBean;
import com.nsitd.miniperimeter.bean.DeleteSomedayHistoryAlarmParam;
import com.nsitd.miniperimeter.bean.DeleteSomedayHistoryAlarmRequest;
import com.nsitd.miniperimeter.bean.DeviceAlarmInfoBean;
import com.nsitd.miniperimeter.bean.DeviceAlarmInfoParam;
import com.nsitd.miniperimeter.bean.DeviceAlarmInfoRequest;
import com.nsitd.miniperimeter.bean.FnCountInfoBean;
import com.nsitd.miniperimeter.bean.FnCountInfoParam;
import com.nsitd.miniperimeter.bean.FnCountInfoRequest;
import com.nsitd.miniperimeter.bean.HistoryAlarmBean;
import com.nsitd.miniperimeter.bean.HistoryAlarmParam;
import com.nsitd.miniperimeter.bean.HistoryAlarmRequest;
import com.nsitd.miniperimeter.bean.HostNameBean;
import com.nsitd.miniperimeter.bean.HostNameParam;
import com.nsitd.miniperimeter.bean.HostNameRequest;
import com.nsitd.miniperimeter.bean.LineLengthBean;
import com.nsitd.miniperimeter.bean.LineLengthParam;
import com.nsitd.miniperimeter.bean.LineLengthRequest;
import com.nsitd.miniperimeter.bean.ModifyPasswordBean;
import com.nsitd.miniperimeter.bean.ModifyPasswordParam;
import com.nsitd.miniperimeter.bean.ModifyPasswordRequest;
import com.nsitd.miniperimeter.bean.ObtainDefenseModelBean;
import com.nsitd.miniperimeter.bean.ObtainDefenseModelParam;
import com.nsitd.miniperimeter.bean.ObtainDefenseModelRequest;
import com.nsitd.miniperimeter.bean.ObtainLineRelationBean;
import com.nsitd.miniperimeter.bean.ObtainLineRelationParam;
import com.nsitd.miniperimeter.bean.ObtainLineRelationRequest;
import com.nsitd.miniperimeter.bean.ObtainResourceBean;
import com.nsitd.miniperimeter.bean.ObtainResourceParam;
import com.nsitd.miniperimeter.bean.ObtainResourceRequest;
import com.nsitd.miniperimeter.bean.ObtainStandardLineBean;
import com.nsitd.miniperimeter.bean.ObtainStandardLineParam;
import com.nsitd.miniperimeter.bean.ObtainStandardLineRequest;
import com.nsitd.miniperimeter.bean.SetDefenseModelBean;
import com.nsitd.miniperimeter.bean.SetDefenseModelParam;
import com.nsitd.miniperimeter.bean.SetDefenseModelRequest;
import com.nsitd.miniperimeter.bean.SetLineLengthBean;
import com.nsitd.miniperimeter.bean.SetLineLengthParam;
import com.nsitd.miniperimeter.bean.SetLineLengthRequest;
import com.nsitd.miniperimeter.bean.SetStandardLineBean;
import com.nsitd.miniperimeter.bean.SetStandardLineParam;
import com.nsitd.miniperimeter.bean.SetStandardLineRequest;
import com.nsitd.miniperimeter.bean.UpdateLineRelationBean;
import com.nsitd.miniperimeter.bean.UpdateLineRelationParam;
import com.nsitd.miniperimeter.bean.UpdateLineRelationRequest;
import com.nsitd.miniperimeter.bean.UserLoginBean;
import com.nsitd.miniperimeter.bean.UserLoginParam;
import com.nsitd.miniperimeter.bean.UserLoginRequest;
import com.nsitd.miniperimeter.bean.ViewInitBean;
import com.nsitd.miniperimeter.bean.ViewInitParam;
import com.nsitd.miniperimeter.bean.ViewInitRequest;
import com.nsitd.miniperimeter.http.HttpListenerWrap.IHttpListenerWrapCallback;
import com.nsitd.miniperimeter.util.CommonAttribute;

/**
 * Created by reimu on 16/3/17.
 */
public class HttpRequest {
    private static HttpRequest instance;
    private Context context;
    private LiteHttp liteHttp;

    private HttpRequest(Context context) {
        this.context = context;
        initLiteHttp(context);
    }

    private void initLiteHttp(Context context) {
        if (liteHttp == null) {
            HttpConfig config = new HttpConfig(context) /* configuration quickly*/.setDebugged(true)
              /* log output when debugged*/.setDetectNetwork(true)              /* detect network before connect*/
                    .setDoStatistics(true)               /* statistics of time and traffic*/.setDefaultHttpMethod
                            (HttpMethods.Post).setUserAgent("Mozilla/5.0 (...)")   /* set custom User-Agent*/
                    .setTimeOut(10000, 10000);             /* connect and socket timeout: 10s*/
            liteHttp = LiteHttp.newApacheHttpClient(config);
        } else
            liteHttp.getConfig()                        /* configuration directly*/.setDebugged(true)
              /* log output when debugged*/.setDetectNetwork(true)             /* detect network before connect*/
                    .setDefaultHttpMethod(HttpMethods.Post).setDoStatistics(true)              /* statistics of time
                    and traffic*/.setUserAgent("Mozilla/5.0 (...)")  /* set custom User-Agent*/.setTimeOut(10000,
                    10000);            /* connect and socket timeout: 10s*/
    }

    public static HttpRequest getInstance(Context context) {
        if (instance == null)

            instance = new HttpRequest(context);
        return instance;
    }


    /**
     * 获取host地址
     *
     * @param callback
     */
    public void obtainHostName(IHttpListenerWrapCallback<HostNameBean> callback) {
        HostNameParam param = new HostNameParam();
        param.method = CommonAttribute.Http_getHostName;
        HttpListenerWrap<HostNameBean> listener = new HttpListenerWrap<>(callback);
        HostNameRequest hostNameRequest = new HostNameRequest(param, listener);
        liteHttp.executeAsync(hostNameRequest);
    }

    /**
     * 登陆
     *
     * @param callback
     */
    public void login(String passWord, IHttpListenerWrapCallback<UserLoginBean> callback) {
        UserLoginParam param = new UserLoginParam();
        param.method = CommonAttribute.Http_login;
        param.content.passWord = passWord;
        HttpListenerWrap<UserLoginBean> listener = new HttpListenerWrap<>(callback);
        UserLoginRequest request = new UserLoginRequest(param, listener);
        liteHttp.executeAsync(request);

    }

    /**
     * 修改密码
     *
     * @param passWord
     * @param newPassword
     * @param callback
     */
    public void modifyPassword(String passWord, String newPassword, IHttpListenerWrapCallback<ModifyPasswordBean>
            callback) {
        ModifyPasswordParam param = new ModifyPasswordParam();
        param.method = CommonAttribute.Http_setPassWord;
        param.content.passWord = passWord;
        param.content.newPassWord = newPassword;
        HttpListenerWrap<ModifyPasswordBean> listener = new HttpListenerWrap<>(callback);
        ModifyPasswordRequest request = new ModifyPasswordRequest(param, listener);
        liteHttp.executeAsync(request);
    }

    /**
     * 设置线长
     *
     * @param aLineId
     * @param aLineLength
     * @param blineId
     * @param bLineLength
     * @param callback
     */
    public void setLineLength(String aLineId, String aLineLength, String blineId, String bLineLength,
                              IHttpListenerWrapCallback<SetLineLengthBean> callback) {
        SetLineLengthParam param = new SetLineLengthParam();
        param.method = CommonAttribute.Http_setLineLength;
        param.content.aLineId = aLineId;
        param.content.aLineLength = aLineLength;
        param.content.bLineId = blineId;
        param.content.bLineLength = bLineLength;
        HttpListenerWrap<SetLineLengthBean> listener = new HttpListenerWrap<>(callback);
        SetLineLengthRequest request = new SetLineLengthRequest(param, listener);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取线长
     *
     * @param callback
     */
    public void obtainLineLength(IHttpListenerWrapCallback<LineLengthBean> callback) {
        LineLengthParam param = new LineLengthParam();
        param.method = CommonAttribute.Http_getLineLength;
        HttpListenerWrap<LineLengthBean> listenerWrap = new HttpListenerWrap<>(callback);
        LineLengthRequest request = new LineLengthRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取历史告警
     *
     * @param pageNo
     * @param pageSize
     * @param timeType
     * @param callback
     */
    public void obtainHistroyAlarm(String pageNo, String pageSize, String timeType,
                                   IHttpListenerWrapCallback<HistoryAlarmBean> callback) {
        HistoryAlarmParam param = new HistoryAlarmParam();
        param.method = CommonAttribute.Http_getHistroyAlarm;
        param.content.timeType = timeType;
        param.content.pageNo = pageNo;
        param.content.pageSize = pageSize;
        HttpListenerWrap<HistoryAlarmBean> listenerWrap = new HttpListenerWrap<>(callback);
        HistoryAlarmRequest request = new HistoryAlarmRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 视图初始化
     *
     * @param callback
     */
    public void viewInit(IHttpListenerWrapCallback<ViewInitBean> callback) {
        ViewInitParam param = new ViewInitParam();
        param.method = CommonAttribute.Http_init;
        HttpListenerWrap<ViewInitBean> listenerWrap = new HttpListenerWrap<>(callback);
        ViewInitRequest request = new ViewInitRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 摄像头信息查询
     *
     * @param resourceId
     * @param resourceType
     * @param callback
     */
    public void obtainResource(String resourceId, String resourceType, IHttpListenerWrapCallback<ObtainResourceBean>
            callback) {
        ObtainResourceParam param = new ObtainResourceParam();
        param.method = CommonAttribute.Http_getResource;
        param.content.resourceId = resourceId;
        param.content.resourceType = resourceType;
        HttpListenerWrap<ObtainResourceBean> listenerWrap = new HttpListenerWrap<>(callback);
        ObtainResourceRequest request = new ObtainResourceRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 视频与线关联信息查询
     *
     * @param resourceId
     * @param resourceType
     * @param callback
     */
    public void obtainLineRelation(String resourceId, String resourceType,
                                   IHttpListenerWrapCallback<ObtainLineRelationBean> callback) {
        ObtainLineRelationParam param = new ObtainLineRelationParam();
        param.method = CommonAttribute.Http_getLineRelation;
        param.content.resourceId = resourceId;
        param.content.resourceType = resourceType;
        HttpListenerWrap<ObtainLineRelationBean> listenerWrap = new HttpListenerWrap<>(callback);
        ObtainLineRelationRequest request = new ObtainLineRelationRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 视频与线关联信息保存
     *
     * @param resourceId
     * @param resourceType
     * @param callback
     */
    public void addLineRelation(String resourceName,String resourceId, String resourceType, String alineId, String alineStartNum, String
            alineEndNum, String blineId, String blineStartNum, String blineEndNum,
                                IHttpListenerWrapCallback<AddLineRelationBean> callback) {
        AddLineRelationParam param = new AddLineRelationParam();
        param.method = CommonAttribute.Http_addLineRelation;
        param.content.resourceId = resourceId;
        param.content.resourceType = resourceType;
        param.content.alineId = alineId;
        param.content.alineStartNum = alineStartNum;
        param.content.alineEndNum = alineEndNum;
        param.content.blineId = blineId;
        param.content.blineStartNum = blineStartNum;
        param.content.blineEndNum = blineEndNum;
        param.content.resourceName = resourceName;
        HttpListenerWrap<AddLineRelationBean> listenerWrap = new HttpListenerWrap<>(callback);
        AddLineRelationRequest request = new AddLineRelationRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 视频与线关联信息更新
     *
     * @param resourceId
     * @param resourceType
     * @param lineRelationId
     * @param callback
     */
    public void updateLineRelation(String resourceName,String resourceId, String resourceType, String alineId, String alineStartNum, String
            alineEndNum, String blineId, String blineStartNum, String blineEndNum, String lineRelationId,
                                   IHttpListenerWrapCallback<UpdateLineRelationBean> callback) {
        UpdateLineRelationParam param = new UpdateLineRelationParam();
        param.method = CommonAttribute.Http_updateLineRelation;
        param.content.resourceId = resourceId;
        param.content.resourceType = resourceType;
        param.content.alineEndNum = alineEndNum;
        param.content.alineId = alineId;
        param.content.alineStartNum = alineStartNum;
        param.content.blineEndNum = blineEndNum;
        param.content.blineId = blineId;
        param.content.blineStartNum = blineStartNum;
        param.content.lineRelationId = lineRelationId;
        param.content.resourceName = resourceName;
        HttpListenerWrap<UpdateLineRelationBean> listenerWrap = new HttpListenerWrap<>(callback);
        UpdateLineRelationRequest request = new UpdateLineRelationRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 删除历史告警
     *
     * @param alarmIds
     * @param callback
     */
    public void deleteHistroyAlarm(String alarmIds, IHttpListenerWrapCallback<DeleteHistoryAlarmBean> callback) {
        DeleteHistoryAlarmParam param = new DeleteHistoryAlarmParam();
        param.method = CommonAttribute.Http_deleteHistroyAlarm;
        param.content.alarmIds = alarmIds;
        HttpListenerWrap<DeleteHistoryAlarmBean> listenerWrap = new HttpListenerWrap<>(callback);
        DeleteHistoryAlarmRequest request = new DeleteHistoryAlarmRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 删除某天历史告警
     *
     * @param timeType
     * @param callback
     */
    public void deleteSomedayHistroyAlarm(String timeType, IHttpListenerWrapCallback<DeleteSomedayHistoryAlarmBean>
            callback) {
        DeleteSomedayHistoryAlarmParam param = new DeleteSomedayHistoryAlarmParam();
        param.content.timeType = timeType;
        param.method = CommonAttribute.Http_deleteSomedayHistroyAlarm;
        HttpListenerWrap<DeleteSomedayHistoryAlarmBean> listenerWrap = new HttpListenerWrap<>(callback);
        DeleteSomedayHistoryAlarmRequest request = new DeleteSomedayHistoryAlarmRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取基准线
     *
     * @param callback
     */
    public void obtainStandardLine(IHttpListenerWrapCallback<ObtainStandardLineBean> callback) {
        ObtainStandardLineParam param = new ObtainStandardLineParam();
        param.method = CommonAttribute.Http_getStandardLine;
        HttpListenerWrap<ObtainStandardLineBean> listenerWrap = new HttpListenerWrap<>(callback);
        ObtainStandardLineRequest request = new ObtainStandardLineRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 设置基准线
     *
     * @param callback
     */
    public void setStandardLine(IHttpListenerWrapCallback<SetStandardLineBean> callback) {
        SetStandardLineParam param = new SetStandardLineParam();
        param.method = CommonAttribute.Http_setStandardLine;
        HttpListenerWrap<SetStandardLineBean> listenerWrap = new HttpListenerWrap<>(callback);
        SetStandardLineRequest request = new SetStandardLineRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取设备告警详情
     *
     * @param callback
     */
    public void obtainDeviceAlarmInfo(IHttpListenerWrapCallback<DeviceAlarmInfoBean> callback) {
        DeviceAlarmInfoParam param = new DeviceAlarmInfoParam();
        param.method = CommonAttribute.Http_deviceAlarmInfo;
        HttpListenerWrap<DeviceAlarmInfoBean> listenerWrap = new HttpListenerWrap<>(callback);
        DeviceAlarmInfoRequest request = new DeviceAlarmInfoRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 设置布防模式
     *
     * @param model
     * @param callback
     */
    public void setDefenseModel(String model, IHttpListenerWrapCallback<SetDefenseModelBean> callback) {
        SetDefenseModelParam param = new SetDefenseModelParam();
        param.method = CommonAttribute.Http_setDefenseModel;
        param.content.model = model;
        HttpListenerWrap<SetDefenseModelBean> listenerWrap = new HttpListenerWrap<>(callback);
        SetDefenseModelRequest request = new SetDefenseModelRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取布防模式
     *
     * @param callback
     */
    public void obtainDefenseModel(IHttpListenerWrapCallback<ObtainDefenseModelBean> callback) {
        ObtainDefenseModelParam param = new ObtainDefenseModelParam();
        param.method = CommonAttribute.Http_getDefenseModel;
        HttpListenerWrap<ObtainDefenseModelBean> listenerWrap = new HttpListenerWrap<>(callback);
        ObtainDefenseModelRequest request = new ObtainDefenseModelRequest(param, listenerWrap);
        liteHttp.executeAsync(request);
    }

    /**
     * 获取fn信息
     * @param callback
     */
    public void obtainFnCountInfo(IHttpListenerWrapCallback<FnCountInfoBean> callback){
        FnCountInfoParam param = new FnCountInfoParam();
        param.method = CommonAttribute.Http_getfncountinfo;
        HttpListenerWrap<FnCountInfoBean> listenerWrap = new HttpListenerWrap<>(callback);
        FnCountInfoRequest request = new FnCountInfoRequest(param,listenerWrap);
        liteHttp.executeAsync(request);
    }
}
