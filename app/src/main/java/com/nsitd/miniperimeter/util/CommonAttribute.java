package com.nsitd.miniperimeter.util;

import android.text.TextUtils;

import com.nsitd.miniperimeter.BuildConfig;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用的属性信息
 *
 * @author li
 */
public class CommonAttribute {
    public static String IP = "";
    //    public static String IP = "172.16.250.131";
    public static String psytopicIP = "172.16.250.146";
    public static String produceIP = "172.16.250.146";
    public static String psytopicPort = "8080";
    public static String producePort = "8080";
    public static final String port = "8080";
    public static String url = "http://" + IP + ":" + port + "/msw-web/app/";

    public static String Http_getHostName = "getHostName";
    public static final String Http_login = "login";
    public static final String Http_setPassWord = "setPassWord";
    public static final String Http_setLineLength = "setLineLength";
    public static final String Http_getLineLength = "getLineLength";
    public static final String Http_setPower = "setPower";
    public static final String Http_getPower = "getPower";
    public static final String Http_setSensitivity = "setSensitivity";
    public static final String Http_getSensitivity = "getSensitivity";
    public static final String Http_getHistroyAlarm = "getHistroyAlarm";
    public static final String Http_init = "init";
    public static final String Http_getResource = "getResource";
    public static final String Http_getLineRelation = "getLineRelation";
    public static final String Http_addLineRelation = "addLineRelation";
    public static final String Http_updateLineRelation = "updateLineRelation";
    public static final String Http_deleteHistroyAlarm = "deleteHistroyAlarm";
    public static final String Http_getStandardLine = "getStandardLine";
    public static final String Http_setStandardLine = "setStandardLine";
    public static final String Http_deviceAlarmInfo = "deviceAlarmInfo";
    public static final String Http_setDefenseModel = "setDefenseModel";
    public static final String Http_getDefenseModel = "getDefenseModel";
    public static final String Http_getfncountinfo = "getfncountinfo";
    //vedio
    public final static String VedioCameraChannel = "vediocamerachannel";//摄像头channel
    public final static String VedioFullViewPlayAction = "com.nsitd.vediofull.action";//全屏播放

    public final static String PACKAGE_PSYTOPIC = "com.test.miniperimeter";
    public final static String PACKAGE_PRODUCE = "com.nsitd.miniperimeter";
    public final static String FLAVOR_PSYTOPIC = "psytopic";
    public final static String FLAVOR_PRODUCE = "produce";

    public final static String url_suffix = ".do";
    public final static String Http_deleteSomedayHistroyAlarm = "deleteSomedayHistroyAlarm";

    public static String getUrl(String mehtod) {
        String ip = getIP();
        String port = getPort();
        String urlStr = "http://" + ip + ":" + port + "/msw-web/app/";
        return urlStr + mehtod + url_suffix;
    }

    private static String socketUrlPre = "ws://";
    private static String socketUrlsuffix = "/msw-web/websocket/alertserver";

    public static String getSocketUrl() {
        String ip = getIP();
        String port = getPort();
        return socketUrlPre + ip + ":" + port + socketUrlsuffix;
    }

    private static String imageUrlSuffix = "/msw-web/CapturePic/";
    private static String imageUrlPre = "http://";

    //        http://IP:Port/msw-web/CapturePic/事件ID.jpg
    public static String getImageUrl(String eventId) {
        String ip = getIP();
        String port = getPort();
        return imageUrlPre + ip + ":" + port + imageUrlSuffix + eventId + ".jpg";
    }

    public static String getIP() {
        String ip = null;
        IP = PrefUtils.getString(CommonAttribute.PRE_HOST_ADDRESS,"");
        if(TextUtils.isEmpty(IP)){
            if (BuildConfig.FLAVOR.equals(FLAVOR_PRODUCE)) {
                ip = produceIP;
            } else if (BuildConfig.FLAVOR.equals(FLAVOR_PSYTOPIC)) {
                ip = psytopicIP;
            } else {
                ip = psytopicIP;
            }
        }else{
            ip = IP;
        }
//        ip = "172.16.250.131";
        return ip;
    }

    public static String getPort() {
        String port = null;
        if (BuildConfig.FLAVOR.equals(FLAVOR_PRODUCE)) {
            port = producePort;
        } else if (BuildConfig.FLAVOR.equals(FLAVOR_PSYTOPIC)) {
            port = psytopicPort;
        } else {
            port = psytopicPort;
        }
        return port;
    }


    public final static String PRE_HOST_ADDRESS = "hostAddress";
    public final static String HOST_ADDRESS = "172.16.254.188";
    public final static String RESULTSATE_SUCCESS = "success";


    public final static String VedioAlarmFullViewPlayAction = "com.nsitd.vedioalarmfull.action";//全屏播放
    public final static String VedioCameraMessage = "vediocameramessage";
    public final static String VedioCameraId = "vediocameraid";
    public final static int VedioAlarm = 1;
    public static int ShowContentViewWidth = 0;//右边显示的宽度

    //play seekbar 不同状态显示不同颜色
    public final static int Play_seekbar_start_endType = 0;
    public final static int Play_seekbar_alarmType = 1;
    public final static int Play_seekbar_nomal = 2;

    public static PlaySurfaceView currentPlaySurfaceview = null;


    public static String StartTime ,EndTime;

    public static int SUCCESS =1,FAIL =0;

    public static int VedioHomeLoadNumber = 0;

    public static final String TIME_TYPE_TODAY = "0";
    public static final String TIME_TYPE_YESTERDAY   = "-1";
    public static final String TIME_TYPE_BEFOREDAY = "-2";

    public static final String METHOD_DEVICEALARMMSG ="\"method\":\"deviceAlarmMsg\"";
    public static final String isalarm = "1";
    public static final String PRE_CC_ISALARM = "ccIsAlarm";
    public static final String PRE_ALINE_ISALARM = "alineIsAlarm";
    public static final String PRE_BLINE_ISALARM = "blineIsAlarm";
    public static final String PRE_FN_ISALARM = "fnIsAlarm";
    public static final String PRE_VCR_ISALARM = "vcrIsAlarm";
    public static final String PRE_CAMERA_ISALARM = "cameraIsAlarm";
    public static final String PRE_NC_ISALARM = "ncIsAlarm";
    public static List<AlarmHistory> CameraList = new ArrayList<>();

    public static final String LINE_TYPE = "lineType";
    public static final String LINE_TYPE_A = "A";
    public static final String LINE_TYPE_B = "B";


    public static final String DEVICE_ALARM_PUSH_TYPE_CC = "0";
    public static final String DEVICE_ALARM_PUSH_TYPE_ALINE = "1";
    public static final String DEVICE_ALARM_PUSH_TYPE_BLINE = "2";
    public static final String DEVICE_ALARM_PUSH_TYPE_FN = "3";
    public static final String DEVICE_ALARM_PUSH_TYPE_VCR = "4";
    public static final String DEVICE_ALARM_PUSH_TYPE_CAMERA = "5";
    public static final String DEVICE_ALARM_PUSH_TYPE_NC = "6";

    public static final String PRE_ISALARM = "isAlarm";
    public static final String PRE_MESSAGE_PUSH = "messagePush";

    public static final int HEX_IS_CC = 0x0000001;
    public static final int HEX_NOT_CC =0x1111111;
    public static final int HEX_IS_ALINE = 0x0000010;
    public static final int HEX_NOT_ALINE =0x1111101;
    public static final int HEX_IS_BLINE = 0x0000100;
    public static final int HEX_NOT_BLINE =0x1111011;
    public static final int HEX_IS_FN= 0x0001000;
    public static final int HEX_NOT_FN =0x1110111;
    public static final int HEX_IS_VCR = 0x0010000;
    public static final int HEX_NOT_VCR =0x1101111;
    public static final int HEX_IS_CAMERA = 0x0100000;
    public static final int HEX_NOT_CAMERA =0x1011111;
    public static final int HEX_IS_NC = 0x1000000;
    public static final int HEX_NOT_NC =0x0111111;

    public static final int HEX_DEFALUT = 0x0000000;

    public static final String INVADE_FLAG_EVENT = "abVideoOpen";
    public static final String INVADE_COMMAND_ABVIDEOOPEN = "abVideoOpen";
    public static final String INVADE_COMMAND_ABVIDEOCLOSE = "abVideoClose";
    //入侵结束
    public static final String INVADE_COMMAND_EVENT_OVER = "event_over";
    public static final String INVADE_COMMAND_NOVIDEO = "novideo";

    public static final String PRE_IS_SAVE_PASSWORD = "IS_SAVE_PASSWORD";
    public static final String PRE_IS_AUTO_LOGIN = "IS_AUTO_LOGIN";
    public static final String PRE_PASSWORD = "PASSWORD";

    public static final String NAME_IMAGE = "picture.jpeg";
}
