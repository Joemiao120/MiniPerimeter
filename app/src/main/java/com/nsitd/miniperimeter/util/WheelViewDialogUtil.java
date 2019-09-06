package com.nsitd.miniperimeter.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.view.WheelView;

import java.util.List;

/**
 * Created by reimu on 16/3/23.
 */
public class WheelViewDialogUtil {
    /**
     *
     * @param context
     * @param datas
     * @param title
     */
   public static void showWheelViewDialog(Context context,List<String> datas,String title,int position,WheelView.OnWheelViewListener listener){
       View outerView = LayoutInflater.from(context).inflate(R.layout.wheel_view, null);
       WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
       wv.setOffset(3);
       wv.setItems(datas);
       wv.setSeletion(position);
       wv.setOnWheelViewListener(listener);

       new AlertDialog.Builder(context)
               .setTitle(title)
               .setView(outerView)
               .setPositiveButton("OK", null)
               .show();
    }


}
