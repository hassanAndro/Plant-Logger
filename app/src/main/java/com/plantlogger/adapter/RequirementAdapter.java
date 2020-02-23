package com.plantlogger.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListDatum;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.R;
import com.plantlogger.activities.AreaCheckListActivity;
import com.plantlogger.activities.EquipmentStatusActivity;
import com.plantlogger.activities.ParameterDataEntryActivity;
import com.plantlogger.activities.ParameterDataEntryInputActivity;
import com.plantlogger.activities.RequirementActivity;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.SyncModels.AreaCheckListSyncModel;
import com.plantlogger.models.SyncModels.EquipmentStatusSyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Hassan_Hameed on 9/27/2017.
 */

public class RequirementAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    String color = null;

    public RequirementAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        preferencesKeeper = new PreferencesKeeper(context);
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.requirement_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.area_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);

        if (position == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        } else if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#f5f5f5"));
        } else if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        }

        if (position == 0) {
            if (equipmentStatus()) {
                rowView.setBackgroundColor(Color.parseColor("#00e500"));
            }
        }

        if (position == 1) {
            paramData();
//            if (paramData()) {
            if (color != null) {
                if (color.equals("green")) {
                    rowView.setBackgroundColor(Color.parseColor("#00e500"));
                } else if (color.equals("red")) {
                    rowView.setBackgroundColor(Color.parseColor("#ff4c4c"));
                }
            }
//            }
        }
        if (position == 2) {
            if (areaCheckList()) {
                rowView.setBackgroundColor(Color.parseColor("#00e500"));
            }
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
//                    Intent i = new Intent(context, EquipmentStatusActivity.class);
                    Intent i = new Intent(context, ParameterDataEntryActivity.class);
                    i.putExtra("path", "equipment");
                    context.startActivity(i);
                    //Equipment Status
                } else if (position == 1) {
                    Intent i = new Intent(context, ParameterDataEntryActivity.class);
                    i.putExtra("path", "param");
                    context.startActivity(i);

                    //Parameter Data Entry
                } else if (position == 2) {
//                    Intent i = new Intent(context, AreaCheckListActivity.class);
                    Intent i = new Intent(context, ParameterDataEntryActivity.class);
                    i.putExtra("path", "check");
                    context.startActivity(i);
                    //Area check list
                }

            }
        });


        return rowView;
    }

    public Boolean equipmentStatus() {
        Boolean tag = false;

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            AreaItemDatum areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                    , preferencesKeeper.getCustomerId(), "equipment_status");
            if (areaItemDatum != null) {
                db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);

                ArrayList<EquipmentStatusSyncModel> savedItems = databaseHelper.getEquipmentync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getEquipment_id(),areaItemDatum.getId());
                if (savedItems != null && !savedItems.isEmpty()) {
                    if (savedItems.size() > 0) {
                        tag = true;
                    }
                }
            }
        }

        return tag;
    }

    public Boolean areaCheckList() {
        Boolean tag = false;

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            AreaItemDatum areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                    , preferencesKeeper.getCustomerId(), "area_check_list");
            if (areaItemDatum != null) {
                db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);

                ArrayList<AreaCheckListSyncModel> savedItems = databaseHelper.getCheckListync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getChecklist_id(), areaItemDatum.getId());
                if (savedItems != null && !savedItems.isEmpty()) {
                    if (savedItems.size() > 0) {
                        tag = true;
                    }
                }
            }
        }

        return tag;
    }

    public void paramData() {


        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            ArrayList<AreaItemDatum> areasDatumArrayList = databaseHelper.getAreas_Items(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId(), "param");
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                if (areasDatumArrayList.size() > 0) {
                    for (int i = 0; i < areasDatumArrayList.size(); i++) {
                        AreaItemDatum itemDatum = areasDatumArrayList.get(i);
                        if (itemDatum.getIsFilled().equals("FALSE") || itemDatum.getIsFilled().equals("WRONG")) {
                            color = "red";
                            break;
                        } else if (itemDatum.getIsFilled().equals("TRUE")) {
                            color = "green";
//                            return true;
                        } else {
                            color = null;
                        }
                    }
                } else {
                    color = null;
//                    return false;
                }
            } else {
                color = null;
//                return false;
            }
        } else {
            color = null;
//            return false;
        }


//        return tag;
    }
}

