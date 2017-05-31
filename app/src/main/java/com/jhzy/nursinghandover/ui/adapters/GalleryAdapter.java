package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.nextItem.EldersBean;
import com.jhzy.nursinghandover.beans.nextItem.NurseBean;
import com.jhzy.nursinghandover.widget.ImageMaskView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigyu on 2017/3/25.
 * 床头模块的任务
 */

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<NurseBean> list;
    private LayoutInflater from;

    public GalleryAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        from = LayoutInflater.from(context);
    }

    public void setList(List<NurseBean> lists) {
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public int count() {
        return list.size();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = from.inflate(R.layout.item_gallery_task, viewGroup, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.icon.setFrontBg(R.mipmap.bed_bg);
        NurseBean nurseBean = list.get(i);
        vh.title.setText(nurseBean.getCareItemTitle());
        String startTime = nurseBean.getStartTime();
        String endTime = nurseBean.getEndTime();

        if (startTime.endsWith(":00")) {
            startTime = startTime.substring(0, startTime.length() - 3);
        }
        if (endTime.endsWith(":00")) {
            endTime = endTime.substring(0, endTime.length() - 3);
        }
        vh.time.setText(startTime + "-" + endTime);

        vh.icon.setbg(nurseBean.getCareItemUrl());
        List<EldersBean> elders = nurseBean.getElders();
        if (elders.size() == 1) {
            int isCompleted = elders.get(0).getIsCompleted();
            if (isCompleted == 0) {
                vh.icon.isCheck(false);
            } else {
                vh.icon.isCheck(true);
            }
        }
        return view;
    }

    public class ViewHolder {
        public TextView title;
        public ImageMaskView icon;
        public TextView time;

        public ViewHolder(View view) {
            icon = ((ImageMaskView) view.findViewById(R.id.icon));
            time = ((TextView) view.findViewById(R.id.time));
            title = ((TextView) view.findViewById(R.id.title));
        }
    }
}
