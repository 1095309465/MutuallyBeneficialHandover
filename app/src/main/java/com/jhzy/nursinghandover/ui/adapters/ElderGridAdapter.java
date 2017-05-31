package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.ElderGridBean;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ElderGridAdapter extends BaseAdapter {

    private Context context;
    private List<ElderGridBean.DataBean> list;

    public ElderGridAdapter(Context context, List<ElderGridBean.DataBean> list) {
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
        ElderGridViewHolder eldervh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.elder_grid_item, viewGroup, false);
            eldervh = new ElderGridViewHolder(view);
            view.setTag(eldervh);
        }
        eldervh = (ElderGridViewHolder) view.getTag();
        eldervh.elderGridItemSim.setImageURI(Uri.parse(list.get(i).getCareItemUrl()));
        eldervh.elderGridItemName.setText(list.get(i).getCareItemName());
        eldervh.elderGridItemBedname.setText(list.get(i).getStartTime()+"-"+list.get(i).getEndTime());
        return view;
    }

    class ElderGridViewHolder {
        SimpleDraweeView elderGridItemSim;
        TextView elderGridItemName;
        TextView elderGridItemBedname;

        ElderGridViewHolder(View view) {
            elderGridItemSim = ((SimpleDraweeView) view.findViewById(R.id.elder_grid_item_sim));
            elderGridItemName = ((TextView) view.findViewById(R.id.elder_grid_item_name));
            elderGridItemBedname = ((TextView) view.findViewById(R.id.elder_grid_item_bedname));
        }
    }
}
