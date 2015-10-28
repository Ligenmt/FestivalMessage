package com.ligenmt.festivalmessage.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ligenmt.festivalmessage.MsgChooseActivity;
import com.ligenmt.festivalmessage.R;
import com.ligenmt.festivalmessage.bean.Festival;
import com.ligenmt.festivalmessage.bean.FestivalFactory;
import com.ligenmt.festivalmessage.dao.FestivalDao;

import java.util.zip.Inflater;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class FestivalFragment extends Fragment {

    private GridView gvFestivalCategory;
    private ArrayAdapter<Festival> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gvFestivalCategory = (GridView) view.findViewById(R.id.gv_festival_category);

        gvFestivalCategory.setAdapter(adapter = new ArrayAdapter<Festival>
                (getActivity(), -1, new FestivalDao(getContext()).enumFestival()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = View.inflate(getActivity(), R.layout.item_festival, null);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv_festival);
                tv.setText(getItem(position).getName());

                return convertView;
            }
        });

        gvFestivalCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), MsgChooseActivity.class);
                //拿到节日Id
                intent.putExtra("festivalid", adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

    }
}
