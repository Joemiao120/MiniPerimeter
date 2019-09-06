package com.nsitd.miniperimeter.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.nsitd.miniperimeter.R;

public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private Context context;
    private int spanCount = 1;
    public MyStaggeredGridLayoutManager(int spanCount, int orientation,Context context) {
        super(spanCount, orientation);
        this.context = context;
        this.spanCount = spanCount;
    }

    @Override
    public void onMeasure(Recycler recycler, State state, int widthSpec,
                          int heightSpec) {
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int childCount = getChildCount();
        //判断View值的存在,否则getChildAt()获取到的值为null
        int height = (int) context.getResources().getDimension(R.dimen.activity_history_alarm_grid_height)*spanCount;
        if (childCount <= 0) {
            setMeasuredDimension(measuredWidth, height);
            return;
        }
        int childHeight = getChildAt(0).getHeight();
        height = childHeight*spanCount;
        setMeasuredDimension(measuredWidth, height);
    }
}
