package com.example.atry.Alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.atry.R;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    private List<Plan> backList;
    private List<Plan> planList;
    PlanAdapter.MyFilter mFilter;

    public PlanAdapter(Context mContext, List<Plan> planList) {
        this.mContext = mContext;
        this.planList = planList;
        backList = planList;
    }

    @Override
    public int getCount() {
        return planList.size();
    }

    @Override
    public Object getItem(int position) {
        return planList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(mContext, R.layout.plan_layout, null);
        TextView tv_title = (TextView)v.findViewById(R.id.tv_title);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);

        tv_title.setText(planList.get(position).getTitle());
        tv_content.setText(planList.get(position).getContent());
        tv_time.setText(planList.get(position).getTime());

        v.setTag(planList.get(position).getId());

        return v;
    }

    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new PlanAdapter.MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Plan> list;
            if (TextUtils.isEmpty(charSequence)) {
                list = backList;
            } else {
                list = new ArrayList<>();
                for (Plan plan : backList) {
                    if (plan.getTitle().contains(charSequence) || plan.getContent().contains(charSequence)) {
                        list.add(plan);
                    }

                }
            }
            result.values = list;
            result.count = list.size();

            return result;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            planList = (List<Plan>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }
        }
    }
}
