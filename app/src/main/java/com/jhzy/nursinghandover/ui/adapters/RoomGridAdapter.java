package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.RoomGridBean;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RoomGridAdapter extends BaseAdapter {
    private Context context;
    private List<RoomGridBean.DataBean.TasksBean> list;


    public RoomGridAdapter(Context context, List<RoomGridBean.DataBean.TasksBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
    }


    @Override
    public Object getItem(int i) {
        return list.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RoomGridViewHolder roomvh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.room_grid_item, viewGroup, false);
            roomvh = new RoomGridViewHolder(view);
            view.setTag(roomvh);
        }
        roomvh = (RoomGridViewHolder) view.getTag();
        if (list.get(i).getPhotoUrl() != null) {
            ImageLoaderUtils.load(roomvh.roomGridItemSim,list.get(i).getPhotoUrl());
        }
        roomvh.roomGridItemName.setText(list.get(i).getElderName());
        roomvh.roomGridItemBedname.setText(list.get(i).getBedTitle());
        String staff = list.get(i).getStaff();
        if (TextUtils.isEmpty(staff)) {
            staff = "暂无责任护工";
        }else{
            staff = "责任护工:" + staff;
        }
        roomvh.roomGridItemNursename.setText(staff);
        roomvh.roomGridItemNumber.setText(list.get(i).getCount() + "");
        return view;
    }


    class RoomGridViewHolder {
        SimpleDraweeView roomGridItemSim;
        TextView roomGridItemName;
        TextView roomGridItemBedname;
        TextView roomGridItemNursename;
        TextView roomGridItemNumber;


        RoomGridViewHolder(View view) {
            roomGridItemSim = ((SimpleDraweeView) view.findViewById(R.id.room_grid_item_sim));
            roomGridItemName = ((TextView) view.findViewById(R.id.room_grid_item_name));
            roomGridItemBedname = ((TextView) view.findViewById(R.id.room_grid_item_bedname));
            roomGridItemNursename = ((TextView) view.findViewById(R.id.room_grid_item_nursename));
            roomGridItemNumber = ((TextView) view.findViewById(R.id.room_grid_item_number));
        }
    }
}
