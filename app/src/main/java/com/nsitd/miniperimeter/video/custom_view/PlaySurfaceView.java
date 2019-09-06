package com.nsitd.miniperimeter.video.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.bean.EventBusBean;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.inter.IVedioCallback;

import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.PlayerCallBack;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import de.greenrobot.event.EventBus;

/**
 * 实时播放，历史回放 view
 */
@SuppressLint("NewApi")
public class PlaySurfaceView extends SurfaceView implements Callback {

    private final String TAG = "PlaySurfaceView";
    private int m_iWidth = 0;
    private int m_iHeight = 0;
    public int m_iPreviewHandle = -1;
    private int m_iPort = -1;
    private boolean m_bSurfaceCreated = false;

    private int m_iPlaybackID = -1;
    private Context context;

    private IVedioCallback iVedioCallback;
    private Handler handler;
    private double proportion;

    //通知view 更新执行一次
    private boolean isFirstStart = false;
    private boolean isFirstPlayBackStart = false;

    //
    private boolean isPlaybackStop = false;
    private int pauseOrStart = -1;;


    public PlaySurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        System.out.println("surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        m_bSurfaceCreated = true;
        Log.i(TAG, "surfaceCreated======" + m_bSurfaceCreated);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.RGB_565);
//		getHolder().setFormat();
//		setBackgroundColor(getResources().getColor(R.color.gray));
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = arg0.getSurface();
        if (true == surface.isValid()) {
            if (false == Player.getInstance().setVideoWindow(m_iPort, 0, arg0)) {
                Log.e(TAG, "Player setVideoWindow failed!" + Player.getInstance().getLastError(m_iPort));
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        m_bSurfaceCreated = false;
        Log.i(TAG, "surfaceDestroyed======" + m_bSurfaceCreated);
        if (-1 == m_iPort) {
            return;
        }
        if (true == arg0.getSurface().isValid()) {
            if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                Log.e(TAG, "Player setVideoWindow failed!" + Player.getInstance().getLastError(m_iPort));
                if (iVedioCallback != null)
                    iVedioCallback.vedioProcessResultCallback(CommonAttribute.FAIL);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = sizeCallback.getWidth();
//        int height = sizeCallback.getHeight();
//        if (m_iWidth != width || m_iHeight != height) {
//            m_iWidth = width;
//            m_iHeight = height;
//
////            if (handler != null) {
////                Message message = new Message();
////                message.what = 3;
////                handler.sendMessage(message);
////            }
//
//        }
        super.setMeasuredDimension(m_iWidth, m_iHeight);

//        super.setMeasuredDimension(sizeCallback.getWidth(), sizeCallback.getHeight());

    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public void setParam(int nScreenSize) {
        m_iWidth = nScreenSize;
        m_iHeight = (int) (m_iWidth * proportion);
    }

    public void setScreenHight(int nScreenSize) {
        m_iHeight = nScreenSize;
        m_iWidth = (int) ((m_iHeight / 9) * 16);//(int)(m_iHeight * 16) / 9;
    }

    public void setScreenWidth(int nScreenSize) {
        m_iWidth = (int) (nScreenSize - this.getResources().getDimension(R.dimen.test));
    }

    public void setCallBack(IVedioCallback iVedioCallback) {
        this.iVedioCallback = iVedioCallback;
    }

    public int getCurWidth() {
        return m_iWidth;
    }

    public int getCurHeight() {
        return m_iHeight;
    }

    public boolean startPreview(int iUserID, int iChan, boolean isHd) {
        isFirstStart = true;
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return false;
        }
        Log.i(TAG, "preview channel:" + iChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = iChan;
        previewInfo.dwStreamType = isHd ? 0 : 1;
        previewInfo.bBlocked = 1;
        // HCNetSDK start preview
        m_iPreviewHandle = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(iUserID, previewInfo, fRealDataCallBack);
        if (m_iPreviewHandle < 0) {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return false;
        }
        return true;
    }


    public void stopPreview() {
        HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPreviewHandle);
        stopPlayer();
        m_iPort = -1;
    }

    private void stopPlayer() {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort)) {
            Log.e(TAG, "stop is failed!" + Player.getInstance().getLastError(m_iPort));
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort)) {
            Log.e(TAG, "closeStream is failed!" + Player.getInstance().getLastError(m_iPort));
            return;
        }
        if (!Player.getInstance().freePort(m_iPort)) {
            Log.e(TAG, "freePort is failed!" + m_iPort + Player.getInstance().getLastError(m_iPort));
            return;
        }
        m_iPort = -1;
    }

    /**
     * 回放
     *
     * @param m_iLogID
     * @param ichannel
     * @param startTime
     * @param endTime
     */
    public boolean startPlayBack(int m_iLogID, int ichannel, NET_DVR_TIME startTime, NET_DVR_TIME endTime) {

        try {
            Log.e(TAG, "startPlayBack=====");
            isPlaybackStop = false;
            isFirstPlayBackStart = true;
            PlaybackCallBack fPlaybackCallBack = getPlayerbackPlayerCbf();
            if (fPlaybackCallBack == null) {
                Log.e(TAG, "fPlaybackCallBack object is failed!");
                return false;
            }
            m_iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackByTime(m_iLogID, ichannel, startTime, endTime);
            if (m_iPlaybackID >= 0) {

                if (!HCNetSDK.getInstance().NET_DVR_SetPlayDataCallBack(m_iPlaybackID, fPlaybackCallBack)) {
                    Log.e(TAG, "Set playback callback failed!");
                    return false;
                }
                NET_DVR_PLAYBACK_INFO struPlaybackInfo = null;
                if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(m_iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, struPlaybackInfo)) {
                    Log.e(TAG, "net sdk playback start failed!");
                    return false;
                }
                int nProgress = -1;
            /*
            while(true)
			{
				nProgress = HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(m_iPlaybackID);
				System.out.println("NET_DVR_GetPlayBackPos:" + nProgress);
				if(nProgress < 0 || nProgress >= 100)
				{
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			*/
            } else {
                Log.i(TAG, "NET_DVR_PlayBackByTime failed, error code: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return false;
            }
        } catch (Exception err) {
            LogFactory.i(TAG, err.toString());
            return false;
        }
        return true;
    }



    /**
     * 停止回放
     */
    public void stopPlayBack() {
        try {
            isPlaybackStop = true;
            Log.e(TAG, "net sdk stop playback" + m_iPlaybackID);
            if (pauseOrStart == 1)//规避暂停时调用stop 出现异常
            {
                pause(0);
            }

            if (!HCNetSDK.getInstance().NET_DVR_StopPlayBack(m_iPlaybackID)) {
                Log.e(TAG, "net sdk stop playback failed");
            }
            stopPlayer();
        } catch (Exception err) {
            LogFactory.i(TAG, "stopPlayBack=====" + err.toString());
        } finally {
            // player stop play
            m_iPlaybackID = -1;
        }
        Log.e(TAG, "complete net sdk stop playback" );

    }


    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    /**
     * @return callback instance
     * @fn getPlayerbackPlayerCbf
     * @author zhuzhenlei
     * @brief get Playback instance
     */
    private PlaybackCallBack getPlayerbackPlayerCbf() {
        PlaybackCallBack cbf = new PlaybackCallBack() {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                // player channel 1

                if (isPlaybackStop)
                {
                    HCNetSDK.getInstance().NET_DVR_SetPlayDataCallBack(m_iPlaybackID, null);
                    return;
                }

                playBackProcessRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_FILE);
            }
        };
        return cbf;
    }

    private void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
//	    Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
        Log.i(TAG, "processRealData====HCNetSDK.NET_DVR_SYSHEAD:" + HCNetSDK.NET_DVR_SYSHEAD + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            if (m_iPort >= 0) {
                return;
            }
            m_iPort = Player.getInstance().getPort();
            if (m_iPort == -1) {
                Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
                return;
            }
            Log.i(TAG, "getPort succ with: " + m_iPort);
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
                {
                    Log.e(TAG, "setStreamOpenMode failed");
                    return;
                }
                if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) //open stream
                {
                    Log.e(TAG, "openStream failed");
                    return;
                }

                Player.getInstance().setDisplayCB(m_iPort, new PlayerCallBack.PlayerDisplayCB() {
                    @Override
                    public void onDisplay(int i, byte[] bytes, int i1, int i2, int i3, int i4, int i5, int i6) {
                        if (iVedioCallback != null && isFirstStart) {
                            iVedioCallback.vedioProcessResultCallback(CommonAttribute.SUCCESS);
                            isFirstStart = false;
                            Log.i(TAG, "setDisplayCB=====================================================");
                        }
                    }
                });

                while (!m_bSurfaceCreated) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.i(TAG, "wait 100 for surface, handle:" + iPlayViewNo);
                }

                if (!Player.getInstance().play(m_iPort, getHolder())) {
                    Log.e(TAG, "play failed,error:" + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                if (!Player.getInstance().playSound(m_iPort)) {
                    Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.i(TAG, "processRealData======" + CommonAttribute.SUCCESS);

//                if (iVedioCallback != null)
//                    iVedioCallback.vedioProcessResultCallback(CommonAttribute.SUCCESS);
            }
        } else {
            if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
                int error = Player.getInstance().getLastError(m_iPort);
                Log.e(TAG, "inputData failed with: " + error);
                if (iVedioCallback != null)
                    iVedioCallback.vedioProcessResultCallback(error);
            }

        }
    }

    private void playBackProcessRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
//	    Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
        Log.i(TAG, "playBackProcessRealData==HCNetSDK.NET_DVR_SYSHEAD:" + HCNetSDK.NET_DVR_SYSHEAD + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize+"==="+iVedioCallback);


        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            if (m_iPort >= 0) {
                return;
            }
            m_iPort = Player.getInstance().getPort();
            if (m_iPort == -1) {
                Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
                return;
            }
            Log.i(TAG, "getPort succ with: " + m_iPort);
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
                {
                    Log.e(TAG, "setStreamOpenMode failed");
                    return;
                }
                if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) //open stream
                {
                    Log.e(TAG, "openStream failed");
                    return;
                }

                Player.getInstance().setDisplayCB(m_iPort, new PlayerCallBack.PlayerDisplayCB() {
                    @Override
                    public void onDisplay(int i, byte[] bytes, int i1, int i2, int i3, int i4, int i5, int i6) {

//                        Log.d(TAG, "playBackProcessRealData11111111111111111111111111=============isFirstPlayBackStart" + isFirstPlayBackStart + "==iVedioCallback==" + iVedioCallback);

                        if (iVedioCallback != null && isFirstPlayBackStart) {
                            iVedioCallback.vedioProcessResultCallback(CommonAttribute.SUCCESS);
                            isFirstPlayBackStart = false;

                        }
                    }
                });

                while (!m_bSurfaceCreated) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.i(TAG, "wait 100 for surface, handle:" + iPlayViewNo);
                }

                if (!Player.getInstance().play(m_iPort, getHolder())) {
                    Log.e(TAG, "play failed,error:" + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                if (!Player.getInstance().playSound(m_iPort)) {
                    Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(m_iPort));
                    return;
                }

                Log.d(TAG, "22222222222222222222processRealData======" + CommonAttribute.SUCCESS);

//                if (iVedioCallback != null)
//                    iVedioCallback.vedioProcessResultCallback(CommonAttribute.SUCCESS);
            }
        } else {
            if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
//				Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                for (int i = 0; i < 4000 && m_iPlaybackID >= 0; i++) {
                    if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
                    }
//						Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                    else
                        break;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.i(TAG, "=========");

                    }
                }
            }
        }
    }

    /**
     * 快速播放 每次调用将使当前播放速度加快一倍,最多加速至 16 倍
     * 注意:实际能达到倍数取决与机器性能、视频的分辨率等多种因素。
     * 成功返回 true;失败返回 false
     */

    public boolean fast() {
        return Player.getInstance().fast(m_iPort);
    }

    /**
     * 慢速播放,每次调用将使当前播放速度慢一倍;最多减速至 1/16
     * 成功返回 true;失败返回 false
     */
    public boolean slow() {
        return Player.getInstance().slow(m_iPort);
    }


    /**
     * @param pauseOrStart 1 暂停,0 恢复
     * @return 成功返回 true;失败返回 false
     */
    public boolean pause(int pauseOrStart) {
        this.pauseOrStart = pauseOrStart;
        return Player.getInstance().pause(m_iPort, pauseOrStart);
    }

    /**
     * 设置文件当前播放位置
     *
     * @param fRelativePos 文件位置比例,范围 0-100%
     * @return 设置文件播放指针的相对位置(百分比)。如果在建立文件索引的前提下使用, 为精确定位, 否则即为粗略定位;
     */
    public boolean setPlayPercentage(float fRelativePos) {
        m_iPort = Player.getInstance().getPort();
        if (m_iPort == -1) {
            Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
            return false;
        }
        return Player.getInstance().setPlayPos(m_iPort, fRelativePos);
    }

    /**
     * 获取文件当前播放位置(百分比)
     *
     * @return
     */
    public float getPlayPercentage() {
        m_iPort = Player.getInstance().getPort();
        if (m_iPort == -1) {
            Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
            return -1;
        }
        return Player.getInstance().getPlayPos(m_iPort);
    }

    /**
     * 设置文件当前播放时间(毫秒)
     *
     * @param ntime
     * @return
     */
    public boolean setPlayTimeEx(int ntime) {
        m_iPort = Player.getInstance().getPort();
        if (m_iPort == -1) {
            Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
            return false;
        }
        return Player.getInstance().setPlayedTimeEx(m_iPort, ntime);
    }

    /**
     * 获取文件当前播放时间(毫秒)
     *
     * @return
     */
    public long getPlayTimeEx() {
        m_iPort = Player.getInstance().getPort();
        if (m_iPort == -1) {
            Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
            return -1;
        }
        return Player.getInstance().getPlayedTimeEx(m_iPort);
    }

    /**
     * 获取文件总时间 不支持对文件的数据追加(不支持对正在写入的文件进行时间的读取)
     * 文件总时间长度,单位秒
     *
     * @return
     */
    public long getFileTime() {
        m_iPort = Player.getInstance().getPort();
        if (m_iPort == -1) {
            Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
            return -1;
        }
        return Player.getInstance().getFileTime(m_iPort);
    }

    public boolean getSystemTime(Player.MPSystemTime mpSystemTime) {
        return Player.getInstance().getSystemTime(m_iPort, mpSystemTime);
    }

    public interface SetSizeCallback {
        public int getWidth();

        public int getHeight();
    }

    private SetSizeCallback sizeCallback;

    public void setSizeCallback(SetSizeCallback sizeCallback) {
        this.sizeCallback = sizeCallback;
    }

    public void getImageDate() {
        // 获取图片
        Player.MPInteger stWidth = new Player.MPInteger();
        Player.MPInteger stHeight = new Player.MPInteger();
        if (!Player.getInstance().getPictureSize(m_iPort, stWidth, stHeight)) {
            Log.e(TAG, "getPictureSize failed with error code:" + Player.getInstance().getLastError(m_iPort));
            return;
        }
        int nSize = 5 * stWidth.value * stHeight.value;
        byte[] picBuf = new byte[nSize];
        Player.MPInteger stSize = new Player.MPInteger();
        if (!Player.getInstance().getBMP(m_iPort, picBuf, nSize, stSize)) {
            Log.e(TAG, "getBMP failed with error code:" + Player.getInstance().getLastError(m_iPort));
            return;
        }

        saveBitmap2file(BitmapFactory.decodeByteArray(picBuf, 0, picBuf.length), CommonAttribute.NAME_IMAGE);

        EventBus.getDefault().post(new EventBusBean("visible"));

    }

    boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream("/sdcard/" + filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

}
