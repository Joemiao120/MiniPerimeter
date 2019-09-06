package com.nsitd.miniperimeter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.bean.HistoryAlarmBean;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.MyViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private List<HistoryAlarmBean.HistroyAlarm> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private int totalNumber;
    private int pageNo = 1;

    public int pageNoPlus() {
        pageNo++;
        return pageNo;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public MyGridAdapter(Context context, List<HistoryAlarmBean.HistroyAlarm> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.context = context;
        pageNo = 1;
    }

    public MyGridAdapter(Context context, RecyclerView recyclerView) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        pageNo = 1;
    }

    public HistoryAlarmBean.HistroyAlarm getItem(int position) {
        if (mDatas == null || mDatas.size() <= position) return null;
        return mDatas.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.activity_history_grid_item, parent, false));
        Log.i("TAG", "onCreateViewHolder:" + holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.i("TAG", "onBindViewHolder:" + position + ",holder:" + holder);

        HistoryAlarmBean.HistroyAlarm histroyAlarm = mDatas.get(position);


        if (!TextUtils.isEmpty(histroyAlarm.startTime)) {
            String[] times = histroyAlarm.startTime.split(" ");
            holder.tv_history_alarm_time.setText(times[1]);
        }

        if (isMoreItem(position)) {
            holder.iv_history_alarm_more.setVisibility(View.VISIBLE);
            holder.iv_alpha30_white.setVisibility(View.VISIBLE);
        } else {
            holder.iv_history_alarm_more.setVisibility(View.GONE);
            holder.iv_alpha30_white.setVisibility(View.GONE);
        }

        String imageUrl = CommonAttribute.getImageUrl(histroyAlarm.alarmId);
        Log.i(TAG, imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .error(R.mipmap.image_load_fail)
                .placeholder(R.mipmap.image_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_history_alarm_image);


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    removeData(pos);
                    return false;
                }
            });
        }
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    /**
     * 判断是否是更多那个item
     *
     * @param position
     * @return
     */
    public boolean isMoreItem(int position) {
        if (position == mDatas.size() - 1 && mDatas.size() < totalNumber) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size();
    }

    public void addData(HistoryAlarmBean.HistroyAlarm alarm) {
        int position = mDatas.size();
        mDatas.add(position, alarm);
        LogFactory.i(TAG, "addData-position:" + position);
        notifyItemInserted(position);
    }


    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    private void addAll(List<HistoryAlarmBean.HistroyAlarm> alarmArray) {
        if(alarmArray==null||alarmArray.size()<=0)return;
        int positionStart = mDatas.size();
        int position = 0;
        for (HistoryAlarmBean.HistroyAlarm alarm :
                alarmArray) {
            position = mDatas.size();
            mDatas.add(position, alarm);
        }
//        notifyDataSetChanged();
        notifyItemRangeInserted(positionStart,alarmArray.size());
//        notifyItemMoved(positionStart-1,mDatas.size()-1);
    }

    public void setData(HistoryAlarmBean.HistroyAlarmList bean, boolean isMore) {
        if (!isMore) clearAll();
        setData(bean);
    }

    private void clearAll() {
        if (mDatas == null) return;
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            removeData(i);
        }
    }

    public void setData(HistoryAlarmBean.HistroyAlarmList bean) {
        if (bean != null) {
            int position = getItemCount() - 1;
            if (bean.alarmArray != null && bean.alarmArray.size() > 0) {
                addAll(bean.alarmArray);
            }
            if (!TextUtils.isEmpty(bean.totalNumber)) {
                totalNumber = Integer.parseInt(bean.totalNumber);
                if (bean.alarmArray != null && bean.alarmArray.size() > 0) notifyItemChanged(position);
                LogFactory.i(TAG,"=====================setData-position:"+position);
            } else {
                totalNumber = 0;
            }
        } else {
            totalNumber = 0;
        }
    }


    class MyViewHolder extends ViewHolder {

        ImageView iv_history_alarm_image;
        TextView tv_history_alarm_time;
        ImageView iv_history_alarm_more;
        ImageView iv_alpha30_white;

        public MyViewHolder(View view) {
            super(view);
            this.tv_history_alarm_time = (TextView) view.findViewById(R.id.tv_history_alarm_time);
            this.iv_history_alarm_image = (ImageView) view.findViewById(R.id
                    .iv_history_alarm_image);
            this.iv_history_alarm_more = (ImageView) view.findViewById(R.id.iv_history_alarm_more);
            this.iv_alpha30_white = (ImageView) view.findViewById(R.id.iv_alpha30_white);

        }
    }


}