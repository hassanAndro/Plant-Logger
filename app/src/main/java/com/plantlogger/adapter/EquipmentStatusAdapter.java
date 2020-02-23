package com.plantlogger.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.R;
import com.plantlogger.models.SyncModels.EquipmentStatusSyncModel;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Hassan_Hameed on 9/28/2017.
 */

public class EquipmentStatusAdapter extends ArrayAdapter<EquipmentStatusDatum> {
    private final Context context;
    private final ArrayList<EquipmentStatusDatum> areasDatumArrayList;
    ArrayList<EquipmentStatusSyncModel> selectedStrings;
    PreferencesKeeper preferencesKeeper;

    public EquipmentStatusAdapter(Context context, ArrayList<EquipmentStatusDatum> areasDatumArrayList) {
        super(context, -1, areasDatumArrayList);
        this.context = context;
        this.areasDatumArrayList = areasDatumArrayList;
        selectedStrings = new ArrayList<EquipmentStatusSyncModel>();
        preferencesKeeper = new PreferencesKeeper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.area_check_list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.area_name);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);

        textView.setText(areasDatumArrayList.get(position).getSets_value());
        EquipmentStatusSyncModel model = new EquipmentStatusSyncModel();
        model.setEquipment_id(areasDatumArrayList.get(position).getId());
        model.setSets_idx(areasDatumArrayList.get(position).getSets_idx());
        model.setSets_value(areasDatumArrayList.get(position).getSets_value());
        model.setEmail_id(preferencesKeeper.getEmail());
        model.setGroupd_id(preferencesKeeper.getGroupId());
        model.setAdminId(preferencesKeeper.getCustomerId());
        model.setDateTime(Utils.currentDateTime());
        if (areasDatumArrayList.get(position).getIsChecked().equals("1")) {
            checkBox.setChecked(true);
//            selectedStrings.add(model);
        } else if (areasDatumArrayList.get(position).getIsChecked().equals("0")) {
            checkBox.setChecked(false);
            if (contains(selectedStrings, areasDatumArrayList.get(position).getId())) {
                removeObject(selectedStrings, areasDatumArrayList.get(position).getId());
            }
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (areasDatumArrayList.get(position).getMultiple().equals("1")) {

                    EquipmentStatusSyncModel model = new EquipmentStatusSyncModel();
                    model.setEquipment_id(areasDatumArrayList.get(position).getId());
                    model.setSets_idx(areasDatumArrayList.get(position).getSets_idx());
                    model.setSets_value(areasDatumArrayList.get(position).getSets_value());
                    model.setEmail_id(preferencesKeeper.getEmail());
                    model.setGroupd_id(preferencesKeeper.getGroupId());
                    model.setAdminId(preferencesKeeper.getCustomerId());
                    model.setDateTime(Utils.currentDateTime());
                    model.setMultiple(areasDatumArrayList.get(position).getMultiple());
                    if (isChecked) {
                        selectedStrings.add(model);

                        Log.e("adapter", "add " + areasDatumArrayList.get(position).getSets_value());
                        Log.e("adapter", "size current " + selectedStrings.size());
                    } else {
                        if (contains(selectedStrings, areasDatumArrayList.get(position).getId())) {
                            Log.e("adapter", "remove " + areasDatumArrayList.get(position).getSets_value());
                            removeObject(selectedStrings, areasDatumArrayList.get(position).getId());
                            Log.e("adapter", "size current " + selectedStrings.size());
                        }
                    }
                } else if (areasDatumArrayList.get(position).getMultiple().equals("0")) {
                    for (int i = 0; i < areasDatumArrayList.size(); i++) {
                        areasDatumArrayList.get(i).setIsChecked("0");
                        removeObject(selectedStrings, areasDatumArrayList.get(i).getId());
                    }

                    if (isChecked) {
                        areasDatumArrayList.get(position).setIsChecked("1");
                        EquipmentStatusSyncModel model = new EquipmentStatusSyncModel();
                        model.setEquipment_id(areasDatumArrayList.get(position).getId());
                        model.setSets_idx(areasDatumArrayList.get(position).getSets_idx());
                        model.setSets_value(areasDatumArrayList.get(position).getSets_value());
                        model.setEmail_id(preferencesKeeper.getEmail());
                        model.setGroupd_id(preferencesKeeper.getGroupId());
                        model.setAdminId(preferencesKeeper.getCustomerId());
                        model.setDateTime(Utils.currentDateTime());
                        model.setMultiple(areasDatumArrayList.get(position).getMultiple());
                        selectedStrings.add(model);
                    } else {
                        if (contains(selectedStrings, areasDatumArrayList.get(position).getId())) {
                            removeObject(selectedStrings, areasDatumArrayList.get(position).getId());
                        }
                    }
                    notifyDataSetChanged();

                }
            }
        });

        if (position == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        } else if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#f5f5f5"));
        } else if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.WHITE);
        }


        return rowView;
    }

    public ArrayList<EquipmentStatusSyncModel> getSelectedString() {
        return selectedStrings;
    }

    boolean contains(ArrayList<EquipmentStatusSyncModel> list, String id) {
        for (EquipmentStatusSyncModel item : list) {
            if (item.getEquipment_id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void removeObject(ArrayList<EquipmentStatusSyncModel> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            EquipmentStatusSyncModel item = list.get(i);
            if (item.getEquipment_id().equals(id)) {
                list.remove(i);
                break;
            }
        }
    }
}
