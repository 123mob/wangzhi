package com.example.wangzhi6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangzhi6.Sql.UserData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * item页面的实现
 */
public class MyAdpter extends BaseAdapter {
    Context context;
    List<UserData> list;
    List<String> urlimglist;
    List<String> titlelist;
    List<String> remarklist;
    List<String> item_urllist;



    public MyAdpter(Context context, List<UserData> list,List<String> titlelist,List<String> remarklist,
                    List<String> urlimglist,List<String> item_urllist ) {
        this.context = context;
        this.list = list;
        this.urlimglist = urlimglist;
        this.titlelist = titlelist;
        this.remarklist = remarklist;
        this.item_urllist = item_urllist;
    }

    /**
     * 获取list的数目
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 获取
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * 获取每个item对应的id
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 为每个item加载内容
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {

//        UserData userData = list.iterator().next();
//        String title = userData.getTitle();
//        String time = userData.getTime();

        String title = titlelist.get(position);
        String remark = remarklist.get(position);
        String urlimg = urlimglist.get(position);
        String item_url = item_urllist.get(position);

        ViewHolder mViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null, false);
            mViewHolder = new ViewHolder();
            mViewHolder.title = (TextView) convertView.findViewById(R.id.title);
            mViewHolder.image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
            mViewHolder.remark = (TextView) convertView.findViewById(R.id.remark);
            mViewHolder.item_url = (TextView) convertView.findViewById(R.id.item_url);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.title.setText(title);
        mViewHolder.remark.setText(remark);
        mViewHolder.item_url.setText(item_url);

        if(!urlimg.equals("1")) {
            Picasso.get().load(urlimg).into(mViewHolder.image_icon);
        }
        else {
            mViewHolder.image_icon.setImageResource(R.drawable.keai);
        }
///改
        mViewHolder.image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onImageBackListner(v,position);

            }
        });



        return convertView;
    }

    /**
     * 显示在item上的控件
     */
    class ViewHolder {
        private TextView title;
        private ImageView image_icon;
        private TextView remark;
        private TextView item_url;
    }



    CallBack mCallBack;

    public void setOnImageListner(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface CallBack {
        void onImageBackListner(View v, int position);
    }
}
