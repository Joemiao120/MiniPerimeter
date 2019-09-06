package com.nsitd.miniperimeter.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;

/**
 * Created by reimu on 16/3/28.
 */
public class AboutActivity extends BaseActivity{

    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);
        tv_version = (TextView) this.findViewById(R.id.tv_version);
        tv_tilte_bar.setText("关于");
        tv_version.setText(getVersion());
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        String str = "版本号: ";
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return str + version;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }
}
