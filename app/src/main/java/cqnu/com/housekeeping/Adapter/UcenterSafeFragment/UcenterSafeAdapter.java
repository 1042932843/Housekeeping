package cqnu.com.housekeeping.Adapter.UcenterSafeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cqnu.com.housekeeping.R;


public class UcenterSafeAdapter extends BaseAdapter {
	private Context mContext;
	private List<Bean> list;
	LayoutInflater inflater;

	public UcenterSafeAdapter(Context mContext, List<Bean> list) {
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
			convertView = inflater.inflate(R.layout.activity_user_center_safe_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.size()>0){
			holder.title.setText(list.get(position).getTitle());
			
		}
		
		
		return convertView;
	}

	public static class ViewHolder {
		
		public TextView title;
	}

}
