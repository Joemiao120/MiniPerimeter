package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/22.
 */
public class CameraInfoParam extends AbsBaseParam<CameraInfoParam.CameraInfoCotent> {
    public CameraInfoParam(){
        content = new CameraInfoCotent();
    }
    public static class CameraInfoCotent{
        public String resourceId;
        public String resourceType;
    }
}
