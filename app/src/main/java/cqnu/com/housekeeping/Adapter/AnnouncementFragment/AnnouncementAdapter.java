package cqnu.com.housekeeping.Adapter.AnnouncementFragment;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cqnu.com.housekeeping.R;


public class AnnouncementAdapter extends BaseAdapter {
	private Context mContext; 
	private List<Bean> list;
	LayoutInflater inflater;

	public AnnouncementAdapter(Context mContext, List<Bean> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_announcement_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.title);
			holder.type = (TextView) convertView
					.findViewById(R.id.tprice);
			holder.context = (TextView) convertView
					.findViewById(R.id.context);
			holder.time = (TextView) convertView
					.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.size()>0){
			holder.title.setText(list.get(position).getTitle());
			holder.time.setText(list.get(position).getTime());
			holder.type.setText("["+list.get(position).getType()+"]");
			if(list.get(position).getDescribe()!=null){
				if(!list.get(position).getDescribe().equals("")){
					holder.context.setText(list.get(position).getDescribe()+"...");
				}else{
					holder.context.setText("发公告的很懒，没有描述内容...");
				}
				
			}else{
				holder.context.setText("发公告的很懒，没有描述内容...");
			}
			
			
		}
		
		
		return convertView;
	}

	public static class ViewHolder {
		
		public TextView title,type;
		public TextView time,context;
	}

}
