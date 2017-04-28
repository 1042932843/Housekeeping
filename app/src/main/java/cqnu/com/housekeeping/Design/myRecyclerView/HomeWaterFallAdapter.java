package cqnu.com.housekeeping.Design.myRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.Model.Classify;
import cqnu.com.housekeeping.Model.ClassifyItem;
import cqnu.com.housekeeping.R;


/**
 * Created by HP on 2016/9/9.
 */
public class HomeWaterFallAdapter extends RecyclerView.Adapter<HomeWaterFallAdapter.ViewHolder>  {
    private Context mContext = null;
    private List<ClassifyItem> Data=new ArrayList<>();
    private MyItemClickListener mItemClickListener;

    public HomeWaterFallAdapter(Context context, List<ClassifyItem> data) {
        this.mContext = context;
        this.Data = data;
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, String tag);
    }
    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.fragment_home_page_recyclerview_item,parent, false),mItemClickListener);

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(Data.get(position).getTitle());
        holder.context.setText(Data.get(position).getContext());
        holder.icon.setVisibility(View.GONE);
        holder.itemView.setTag(Data.get(position).getTitle());
        //dsyApplication.instance.displayImage(Data.get(position).getImg(),holder.icon);

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView icon;
        public TextView title,context;
        private MyItemClickListener mListener;

        public ViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
           title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.imageView6);
            context= (TextView) itemView.findViewById(R.id.context);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, v.getTag().toString());
            }
        }
    }

}
