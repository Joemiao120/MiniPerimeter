package com.nsitd.miniperimeter.video;

import android.content.Context;
import android.util.Log;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;

/**
 *
 *
 * Created by happy on 16/3/18.
 */
public class VedioProcess {

    private final String TAG = VedioProcess.class.getSimpleName();

    private	int m_iStartChan = 0;				// start channel no
    private int	m_iChanNum	 = 0;				//channel number
    private int m_iLogID = -1;

    private Context context;
    private static VedioProcess vedioProcess = null;

    private VedioProcess(Context context)
    {
        this.context = context;
    }

    public static VedioProcess initVedioProcess(Context context)
    {
        if (vedioProcess == null)
            vedioProcess = new VedioProcess(context);
        return vedioProcess;
    }

    public boolean initSDK()
    {
        //init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init())
        {
            Log.e(TAG, "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",true);
        return true;
    }


    public boolean login(String recorderIP,String recorderPort,String userName,String passwd)
    {
        try
        {
            if(m_iLogID < 0)
            {
                // login on the device
                m_iLogID = loginDevice(recorderIP,recorderPort,userName,passwd);
                if (m_iLogID < 0)
                {
                    Log.e(TAG, "This device logins failed!");
                    return false;
                }
                // get instance of exception callback and set
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null)
                {
                    Log.e(TAG, "ExceptionCallBack object is failed!");
                    return false;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf))
                {
                    Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                    return false;
                }
                Log.i(TAG, "Login sucess ****************************1***************************");
                return true;
            }
            else
            {
                // whether we have logout
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
                {
                    Log.e(TAG, " login NET_DVR_Logout is failed!");
                    return false;
                }
                m_iLogID = -1;
            }
        }
        catch (Exception err)
        {
            Log.e(TAG, "error: " + err.toString());
        }
        return false;
    }

    public boolean logout()
    {
        // whether we have logout
        if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
        {
            Log.e(TAG, " NET_DVR_Logout is failed!");
            return false;
        }
        // miaoyc
        m_iLogID = -1;
        return true;
    }

    /**
     * 预览
     *
     * @param playSurfaceView
     * @param channel
     */
    public boolean startVedioPreview(PlaySurfaceView playSurfaceView,int channel,boolean isHd)
    {
        if(m_iLogID < 0)
        {
            Log.e(TAG,"please login on device first");
            return false;
        }

      return playSurfaceView.startPreview(m_iLogID,channel,isHd);
    }

    /**
     * 停止预览
     * @param playSurfaceView
     */
    public void stopVedioPreview(PlaySurfaceView playSurfaceView)
    {
        playSurfaceView.stopPreview();
    }


    /**
     * @fn getExceptiongCbf
     * @author zhuzhenlei
     * @brief process exception
     * @param NULL [in]
     * @param NULL [out]
     * @return exception instance
     */
    private ExceptionCallBack getExceptiongCbf()
    {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle)
            {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    /**
     * loginDevice
     * @param recorderIP
     * @param recorderPort
     * @param userName
     * @param passwd
     * @return
     */
    private int loginDevice(String recorderIP,String recorderPort,String userName,String passwd)
    {
        // get instance
        NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30)
        {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        int	nPort = Integer.parseInt(recorderPort);
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        m_iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(recorderIP, nPort, userName, passwd, m_oNetDvrDeviceInfoV30);
        if (m_iLogID < 0)
        {
            Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if(m_oNetDvrDeviceInfoV30.byChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        }
        else if(m_oNetDvrDeviceInfoV30.byIPChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        return m_iLogID;
    }


    /**
     * 开始 回放
     * @param playSurfaceView
     * @param channel
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean startPlayBack(PlaySurfaceView playSurfaceView,int channel,NET_DVR_TIME startTime,NET_DVR_TIME endTime)
    {
        if(m_iLogID < 0)
        {
            Log.e(TAG,"please login on a device first");
            return false;
        }
        return playSurfaceView.startPlayBack(m_iLogID, channel, startTime, endTime);
    }

    /**
     * 停止回放
     * @param playSurfaceView
     * @return
     */
    public boolean stopPlayBack(PlaySurfaceView playSurfaceView)
    {
        if(m_iLogID < 0)
        {
            Log.e(TAG,"please login on a device first");
            return false;
        }
        playSurfaceView.stopPlayBack();
        return true;
    }



}
