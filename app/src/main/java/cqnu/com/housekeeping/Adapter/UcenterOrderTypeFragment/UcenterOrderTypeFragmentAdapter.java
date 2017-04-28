package cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import cqnu.com.housekeeping.Activity.UserCenterActivity;
import cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment.Bean;
import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.R;


public class UcenterOrderTypeFragmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<Bean> list;
	LayoutInflater inflater;
	private AlertDialog.Builder builder;
	private Dialog dialog;


	public UcenterOrderTypeFragmentAdapter(Context mContext, List<Bean> list) {
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
			convertView = inflater.inflate(R.layout.fragment_orderlist_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time =(TextView) convertView.findViewById(R.id.time);
			holder.state =(TextView) convertView.findViewById(R.id.state);
			holder.tprice =(TextView) convertView.findViewById(R.id.tprice);
			holder.img=(ImageView)convertView.findViewById(R.id.head_img);
			holder.cancel=(ImageView)convertView.findViewById(R.id.cancel);
			holder.pay=(Button)convertView.findViewById(R.id.buy);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		boolean isCancel=true;
		if(list.size()>0){
			holder.name.setText(list.get(position).getGoodsname());
			holder.time.setText(list.get(position).getSumprice());
			holder.state.setText(list.get(position).getGoodstype());
			switch (list.get(position).getState()){
				case "confirm":
					holder.pay.setText("付款");
					break;
				case "haspay1":
					isCancel=false;
					holder.pay.setText("已付款");
					break;
				case "haspay2":
					holder.pay.setText("待评价");
					break;
			}
			holder.tprice.setText(list.get(position).getTprice());
			dsyApplication.instance.displayImage(list.get(position).getGoodsimg(), holder.img);
			final boolean finalIsCancel = isCancel;
			holder.cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final String orderid=list.get(position).getId();
					builder = new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					builder.setTitle("确认关闭订单？");
					builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(finalIsCancel){
								doCancel(orderid);
							}else{
								Toast.makeText(mContext, "已付款的订单无法取消，您可以到申诉退款页面提交申请", Toast.LENGTH_SHORT).show();
							}
						}
					});
					dialog=builder.show();
				}
			});
			holder.pay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final String orderid=list.get(position).getId();
							switch (list.get(position).getState()){
								case "confirm":
									Pay(orderid);
									break;
								case "haspay1":
									Toast.makeText(mContext, "请耐心等待商家确认", Toast.LENGTH_SHORT).show();
									break;
								case "haspay2":
									Toast.makeText(mContext, "评价系统尚未开放", Toast.LENGTH_SHORT).show();
									break;
							}

				}
			});
		}
		
		
		return convertView;
	}

	public static class ViewHolder {
		
		public TextView name,time,state,tprice;
		public ImageView img,cancel;
		public Button pay;
	}
	public void doCancel(String id){
		Intent intent = new Intent("cqnu.com.housekeeping.Fragment.UcenterOrderTypeFragment");
		HashMap<String,Object> CMD=new HashMap<String,Object>();
		CMD.put("type", "cancel");
		CMD.put("id", id);
		intent.putExtra("CMD", CMD);
		mContext.sendBroadcast(intent);
	}
	public void Pay(String id){
		Intent intent = new Intent("cqnu.com.housekeeping.Fragment.UcenterOrderTypeFragment");
		HashMap<String,Object> CMD=new HashMap<String,Object>();
		CMD.put("type", "pay");
		CMD.put("id", id);
		intent.putExtra("CMD", CMD);
		mContext.sendBroadcast(intent);
	}
}
