package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class CompletedTaskAdapter extends BaseAdapter {
    private List<String> photos;
    private Context context;
    private LayoutInflater from;

    public CompletedTaskAdapter(Context context) {
        this.context = context;
        from = LayoutInflater.from(context);
        photos = new ArrayList<>();
    }

    public void setPhotos(List<String> p) {
        photos.clear();
        photos.addAll(p);
        notifyDataSetChanged();
    }

    public  void setPhoto(String p){
        photos.add(p);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos.size();
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
            view = from.inflate(R.layout.item_complete_task, viewGroup, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ImageLoaderUtils.load(vh.icon, photos.get(i));
        return view;
    }

    class ViewHolder {
        private SimpleDraweeView icon;

        public ViewHolder(View v) {
            icon = ((SimpleDraweeView) v.findViewById(R.id.complete_icon));
        }
    }
}
