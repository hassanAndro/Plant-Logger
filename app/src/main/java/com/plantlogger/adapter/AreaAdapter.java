package com.plantlogger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.R;
import com.plantlogger.activities.SubAreasActivity;

import java.util.ArrayList;

public class AreaAdapter extends ArrayAdapter<AreasDatum> {
    private final Context context;
    private final ArrayList<AreasDatum> areasDatumArrayList;

    public AreaAdapter(Context context, ArrayList<AreasDatum> areasDatumArrayList) {
        super(context, -1, areasDatumArrayList);
        this.context = context;
        this.areasDatumArrayList = areasDatumArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.area_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.area_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(areasDatumArrayList.get(position).getCatName());

        if (position == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        } else if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#f5f5f5"));
        } else if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SubAreasActivity.class);
                i.putExtra("name", areasDatumArrayList.get(position).getCatName());
                i.putExtra("father_id", areasDatumArrayList.get(position).getCatId());
                context.startActivity(i);
            }
        });


        return rowView;
    }
}