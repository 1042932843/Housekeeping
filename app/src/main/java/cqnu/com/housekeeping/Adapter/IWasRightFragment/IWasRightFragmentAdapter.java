package cqnu.com.housekeeping.Adapter.IWasRightFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cqnu.com.housekeeping.Activity.UniversalActivity;
import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.R;

/**
 * Created by HP on 2016/12/7.
 */
public class IWasRightFragmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bean> list;
    LayoutInflater inflater;

    public IWasRightFragmentAdapter(Context mContext, List<Bean> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_iwasright_item, null);
            holder = new ViewHolder();
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.sex=(TextView)convertView.findViewById(R.id.sex);
            holder.age=(TextView)convertView.findViewById(R.id.age);
            holder.type=(TextView)convertView.findViewById(R.id.tprice);
            holder.price=(TextView)convertView.findViewById(R.id.price);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            holder.head=(ImageView)convertView.findViewById(R.id.head_img);
            holder.des=(TextView)convertView.findViewById(R.id.des);
            holder.buy=(Button)convertView.findViewById(R.id.buy);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.size()>0){
            holder.name.setText(list.get(position).getName());
            holder.sex.setText(list.get(position).getSex());
            holder.age.setText(list.get(position).getAge());
            holder.type.setText(list.get(position).getGoodstype());
            holder.price.setText(list.get(position).getPrice());
            holder.address.setText(list.get(position).getAddress());
            holder.des.setText(list.get(position).getDes());
            dsyApplication.instance.displayImage(list.get(position).getImg(), holder.head);
            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type",list.get(position).getGoodstype());
                    bundle.putString("img",list.get(position).getImg());
                    bundle.putString("name",list.get(position).getName());
                    bundle.putString("sex",list.get(position).getSex());
                    bundle.putString("age",list.get(position).getAge());
                    bundle.putString("address",list.get(position).getAddress());
                    bundle.putString("id",list.get(position).getId());
                    bundle.putString("des",list.get(position).getDes());
                    bundle.putString("price",list.get(position).getPrice());
                    UniversalActivity.instance.go_OrderDetailFragment(bundle);
                }
            });
        }


        return convertView;
    }

    public static class ViewHolder {

        public TextView name,sex,age,type,price,address,des;
        public ImageView head;
        public Button buy;
    }

}