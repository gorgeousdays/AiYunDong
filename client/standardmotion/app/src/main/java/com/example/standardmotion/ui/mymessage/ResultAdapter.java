package com.example.standardmotion.ui.mymessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.example.standardmotion.R;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<Result> {
    private int resourceId;

    public ResultAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);
        View view;
        ResultAdapter.ViewHolder viewHolder;
        /**
         * 缓存布局和实例，优化 listView
         */
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.resultId = (TextView)view.findViewById(R.id.result_id);
            viewHolder.resultAdvice=(TextView)view.findViewById(R.id.result_advice);
            viewHolder.resultEnergy=(TextView)view.findViewById(R.id.result_energy);
            viewHolder.resultNum=(TextView)view.findViewById(R.id.result_num);
            viewHolder.resultTime=(TextView)view.findViewById(R.id.result_time);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ResultAdapter.ViewHolder) view.getTag();
//            if(view==null){
//                Log.d("VIEW","NULLLL");
//            }else{
//                Log.d("VIEW","Not NULLLL");
//            }
        }

        viewHolder.resultId.setText(result.getId());
        viewHolder.resultTime.setText(result.getTime());
        viewHolder.resultNum.setText(result.getNum());
        viewHolder.resultEnergy.setText(result.getEnergy());
        viewHolder.resultAdvice.setText(result.getAdvice());
        return view;

    }

    public class ViewHolder{
        TextView resultId;
        TextView resultTime;
        TextView resultNum;
        TextView resultAdvice;
        TextView resultEnergy;
    }
}
