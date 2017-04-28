package cqnu.com.housekeeping.Adapter.RechargeFragmentAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * Created by DSY on 2016/11/1.
 */
public class RechargeFragmentAdapter extends RecyclerView.Adapter<RechargeFragmentAdapter.BaseViewHolder> {
    private ArrayList<RechargeFragmentModel> dataList = new ArrayList<>();
    private int lastPressIndex = -1;


    public void replaceAll(ArrayList<RechargeFragmentModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RechargeFragmentModel.ONE:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one, parent, false));
            case RechargeFragmentModel.TWO:
                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position).data);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tv;

        public OneViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "OneViewHolder: ");
                    int position = getAdapterPosition();
                    if (lastPressIndex == position) {
                        lastPressIndex = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    StringUtil.reCharge=dataList.get(position).data.toString();
                    notifyDataSetChanged();


                }

            });
        }

        @Override
        void setData(Object data) {
            if (data != null) {
                final String text = (String) data;
                tv.setText(text+"元");
                if (getAdapterPosition() == lastPressIndex) {
                    tv.setSelected(true);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                } else {
                    tv.setSelected(false);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                }


            }


        }
    }

    private class TWoViewHolder extends BaseViewHolder {
        private EditText eta;

        public TWoViewHolder(View view) {
            super(view);
            eta = (EditText) view.findViewById(R.id.et);
            eta.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    StringUtil.reCharge=s.toString();
                }
            });
        }

        @Override
        void setData(Object data) {
            super.setData(data);
        }
    }
}
