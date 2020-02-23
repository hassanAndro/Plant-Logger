package com.plantlogger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.R;
import com.plantlogger.activities.AreaCheckListActivity;
import com.plantlogger.activities.EquipmentStatusActivity;
import com.plantlogger.activities.ParameterDataEntryInputActivity;
import com.plantlogger.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by Hassan_Hameed on 9/28/2017.
 */

public class ParameterDataEntryAdapter extends ArrayAdapter<AreaItemDatum> {
    private final Context context;
    private final ArrayList<AreaItemDatum> areasDatumArrayList;
    private String path;

    public ParameterDataEntryAdapter(Context context, ArrayList<AreaItemDatum> areasDatumArrayList, String path) {
        super(context, -1, areasDatumArrayList);
        this.context = context;
        this.path = path;
        this.areasDatumArrayList = areasDatumArrayList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.parameter_data_entry_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.area_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(areasDatumArrayList.get(position).getName());

        if (areasDatumArrayList.get(position).getIsFilled().equals("TRUE")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.check_icon);
        } else if (areasDatumArrayList.get(position).getIsFilled().equals("FALSE")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.esclamation_red);
        } else if (areasDatumArrayList.get(position).getIsFilled().equals("WRONG")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.no_circle);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
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
                if (path.equals("param")) {
                    AppConstants.position = position;
                    Intent i = new Intent(context, ParameterDataEntryInputActivity.class);
                    i.putExtra("name", areasDatumArrayList.get(position).getName());
                    i.putExtra("ID", areasDatumArrayList.get(position).getId());
                    i.putExtra("absolute_range_flag", areasDatumArrayList.get(position).getAbsoluteRangeFlag());
                    i.putExtra("set_id", areasDatumArrayList.get(position).getSetId());
                    context.startActivity(i);
                } else if (path.equals("equipment")) {
                    Log.e("askdj", "id: " + areasDatumArrayList.get(position).getId());
                    Log.e("askdj", "name: " + areasDatumArrayList.get(position).getName());
                    Log.e("askdj", "equip_id: " + areasDatumArrayList.get(position).getEquipment_id());
                    Intent i = new Intent(context, EquipmentStatusActivity.class);
                    i.putExtra("name", areasDatumArrayList.get(position).getName());
                    i.putExtra("ID", areasDatumArrayList.get(position).getId());
                    i.putExtra("equip_id", areasDatumArrayList.get(position).getEquipment_id());
                    context.startActivity(i);
                } else if (path.equals("check")) {
                    Log.e("askdj", "id: " + areasDatumArrayList.get(position).getId());
                    Log.e("askdj", "name: " + areasDatumArrayList.get(position).getName());
                    Log.e("askdj", "check_id: " + areasDatumArrayList.get(position).getChecklist_id());
                    Intent i = new Intent(context, AreaCheckListActivity.class);
                    i.putExtra("name", areasDatumArrayList.get(position).getName());
                    i.putExtra("ID", areasDatumArrayList.get(position).getId());
                    i.putExtra("check_id", areasDatumArrayList.get(position).getChecklist_id());
                    context.startActivity(i);
                }
            }
        });


        return rowView;
    }
}

