package com.plantlogger.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.plantlogger.activities.RequirementActivity;
import com.plantlogger.activities.SubAreasActivity;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Hassan_Hameed on 9/27/2017.
 */

public class SubAreaAdapter extends ArrayAdapter<AreasDatum> {
    private final Context context;
    private final ArrayList<AreasDatum> areasDatumArrayList;
    private final PreferencesKeeper preferencesKeeper;
    private final DatabaseHelper databaseHelper;

    public SubAreaAdapter(Context context, ArrayList<AreasDatum> areasDatumArrayList) {
        super(context, -1, areasDatumArrayList);
        this.context = context;
        this.areasDatumArrayList = areasDatumArrayList;
        preferencesKeeper = new PreferencesKeeper(context);
        databaseHelper = new DatabaseHelper(context);

        if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
            for (int i = 0; i < areasDatumArrayList.size(); i++) {
                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);
                if (db != null) {
                    ArrayList<AreasDatum> areasDatumArray_List_ = databaseHelper.getAreas(db, preferencesKeeper.getEmail()
                            , areasDatumArrayList.get(i).getCatId(), preferencesKeeper.getCustomerId());
                    if (areasDatumArray_List_ != null && !areasDatumArray_List_.isEmpty()) {
                        if (areasDatumArray_List_.size() > 0) {
                            areasDatumArrayList.get(i).setIsLocked("0");
                        } else {
                            areasDatumArrayList.get(i).setIsLocked("1");
                        }
                    } else {
                        areasDatumArrayList.get(i).setIsLocked("1");
                    }
                }
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.subarea_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.area_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView lock = (ImageView) rowView.findViewById(R.id.lock);
        textView.setText(areasDatumArrayList.get(position).getCatName());

        if (areasDatumArrayList.get(position).getIsLocked().equals("1")) {
            lock.setVisibility(View.VISIBLE);
        } else {
            lock.setVisibility(View.INVISIBLE);
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


                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);
                if (db != null) {
                    ArrayList<AreasDatum> areasDatumArray_List = databaseHelper.getAreas(db, preferencesKeeper.getEmail()
                            , areasDatumArrayList.get(position).getCatId(), preferencesKeeper.getCustomerId());
                    if (areasDatumArray_List != null && !areasDatumArray_List.isEmpty()) {

                        if (areasDatumArray_List.size() > 0) {
                            Log.e("sub_area", "areasDatumArray_List.size(): " + areasDatumArray_List.size());
                            Intent i = new Intent(context, SubAreasActivity.class);
                            i.putExtra("name", areasDatumArrayList.get(position).getCatName());
                            i.putExtra("father_id", areasDatumArrayList.get(position).getCatId());
                            context.startActivity(i);
                        } else {
                            Intent i = new Intent(context, RequirementActivity.class);
                            i.putExtra("name", areasDatumArrayList.get(position).getCatName());
                            i.putExtra("groupId", areasDatumArrayList.get(position).getCatId());
                            context.startActivity(i);
                        }


                    } else {
                        Intent i = new Intent(context, RequirementActivity.class);
                        i.putExtra("name", areasDatumArrayList.get(position).getCatName());
                        i.putExtra("groupId", areasDatumArrayList.get(position).getCatId());
                        context.startActivity(i);
                    }
                }
            }
        });


        return rowView;
    }
}
