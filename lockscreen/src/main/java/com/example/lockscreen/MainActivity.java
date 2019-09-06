package com.example.lockscreen;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private ImageView imageView;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "onCreate");

        layout = (LinearLayout) findViewById(R.id.layout);
        imageView = (ImageView) findViewById(R.id.imageview);

        imageView.setOnTouchListener(new MulitPointTouchListener());

        dddddd(this);
    }

    public void dddddd(Activity activity){

        String a = activity.getComponentName().getClassName();

        String b = MainActivity.this.getComponentName().getClassName();

        String c = MainActivity.class.getName();
    }

}
