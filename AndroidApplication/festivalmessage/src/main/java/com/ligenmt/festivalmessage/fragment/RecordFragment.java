package com.ligenmt.festivalmessage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ligenmt.festivalmessage.R;
import com.ligenmt.festivalmessage.bean.Festival;
import com.ligenmt.festivalmessage.bean.FestivalFactory;
import com.ligenmt.festivalmessage.bean.Record;
import com.ligenmt.festivalmessage.dao.FestivalDao;

import java.util.List;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class RecordFragment extends ListFragment {

    private ListView lvRecord;
    private RecordAdapter adapter;
    private List<Record> records;
    private FestivalDao dao;
    private LayoutInflater inflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_msg_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflater = LayoutInflater.from(getContext());
        dao = new FestivalDao(getContext());
        records = dao.enumRecord();

        adapter = new RecordAdapter();
        this.setListAdapter(adapter);

    }

    class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return records.size();
        }

        @Override
        public Record getItem(int i) {
            return records.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_record, viewGroup, false);
            }
            TextView tvRecord = (TextView) convertView.findViewById(R.id.tv_record);
            tvRecord.setText(getItem(i).getContent());
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            tvDate.setText(getItem(i).getDate());
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        records = dao.enumRecord();
        adapter.notifyDataSetChanged();
    }
}
