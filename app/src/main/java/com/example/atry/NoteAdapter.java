package com.example.atry;

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

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;

    private List<Note> backList;
    private List<Note> noteList;
    private MyFilter mFilter;

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);

        String allText = noteList.get(position).getContent();
        if (sharedPreferences.getBoolean("noteTitle" ,true))
            tv_content.setText(allText.split("\n")[0]);
        else tv_content.setText(allText);
        tv_time.setText(noteList.get(position).getTime());

        v.setTag(noteList.get(position).getId());

        return v;
    }

    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(charSequence)) {
                list = backList;
            } else {
                list = new ArrayList<>();
                for (Note note : backList) {
                    if (note.getContent().contains(charSequence)) {
                        list.add(note);
                    }
                }
            }
            result.values = list;
            result.count = list.size();

            return result;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList = (List<Note>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }
        }
    }
}
