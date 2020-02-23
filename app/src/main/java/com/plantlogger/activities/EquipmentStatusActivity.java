package com.plantlogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.EquipmentStatusAdapter;
import com.plantlogger.adapter.ParameterDataEntryAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.SyncModels.EquipmentStatusSyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipmentStatusActivity extends BaseActivity {

    @BindView(R.id.note)
    ImageView note;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.breadCrum)
    TextView breadCrum;
    EquipmentStatusAdapter adapter;

    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    //    AreaItemDatum areaItemDatum;
    ArrayList<EquipmentStatusDatum> equipmentStatusDatumArrayList;
    Boolean tag = false;
    String ID, name, check_id;


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / Equipment Status", "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_status);
        ButterKnife.bind(this);
        ID = getIntent().getExtras().getString("ID");
        name = getIntent().getExtras().getString("name");
        check_id = getIntent().getExtras().getString("equip_id");
        AppConstants.breadCrumString = AppConstants.breadCrumString + " / Equipment Status";
        breadCrum.setText(AppConstants.breadCrumString);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);
        if (preferencesKeeper.getGroupId() != null) {
            getEquipmentItems(preferencesKeeper.getGroupId());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equipmentStatusDatumArrayList != null) {
                    if (adapter.getSelectedString() != null && !adapter.getSelectedString().isEmpty()) {
                        Log.e("adapter", "getSelectedString final size: " + adapter.getSelectedString().size());
                        SQLiteDatabase db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);
                        if (db != null) {
                            db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);

                            if (equipmentStatusDatumArrayList.get(0).getMultiple().equals("0")) {

                                databaseHelper.deleteEquipmentSync(db,
                                        preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(),
                                        equipmentStatusDatumArrayList.get(0).getSets_idx(),
                                        preferencesKeeper.getGroupId(),ID);
                            }
//                            if (tag) {
                            for (int i = 0; i < adapter.getSelectedString().size(); i++) {
                                if (db != null) {
                                    db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);
                                    if (AppConstants.qrTag) {

                                        tag = databaseHelper.createEquipmentSync(db,
                                                adapter.getSelectedString().get(i),
                                                preferencesKeeper.getEmail(),
                                                preferencesKeeper.getCustomerId(),
                                                Utils.currentDateTime(), "1", ID, name);
                                    } else {

                                        tag = databaseHelper.createEquipmentSync(db,
                                                adapter.getSelectedString().get(i),
                                                preferencesKeeper.getEmail(),
                                                preferencesKeeper.getCustomerId(),
                                                Utils.currentDateTime(), "0", ID, name);
                                    }

                                }
                            }
//                            }
                        }
                        if (tag) {
                            FancyToast.makeText(EquipmentStatusActivity.this, "Data saved successfully.", 2000, FancyToast.SUCCESS, false).show();
                        }
                    } else {
                        Log.e("SAVEEQUIPMENT", "2 " + adapter.getSelectedString().size());
                    }
                } else {
                    Log.e("SAVEEQUIPMENT", "1");
                }
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EquipmentStatusActivity.this, EquipmentStatusNoteActivity.class);
                startActivity(i);
            }
        });
    }

    public void getEquipmentItems(String groupId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
//            areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail(), groupId
//                    , preferencesKeeper.getCustomerId(), "equipment_status");
//            if (areaItemDatum != null) {
            db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);
            equipmentStatusDatumArrayList = databaseHelper.getEquipmentStatusData(db, check_id);
            if (equipmentStatusDatumArrayList != null && !equipmentStatusDatumArrayList.isEmpty()) {
                db = DatabaseHelper.getInstance(EquipmentStatusActivity.this).getWritableDatabase(AppConstants.db_password);

                adapter = new EquipmentStatusAdapter(this, equipmentStatusDatumArrayList);
                listview.setAdapter(adapter);

                ArrayList<EquipmentStatusSyncModel> savedItems = databaseHelper.getEquipmentync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), check_id,ID);
                if (savedItems != null && !savedItems.isEmpty()) {
                    for (int i = 0; i < savedItems.size(); i++) {
                        EquipmentStatusSyncModel model = savedItems.get(i);
                        String id = model.getEquipment_id();
                        for (int j = 0; j < equipmentStatusDatumArrayList.size(); j++) {
                            EquipmentStatusDatum datum = equipmentStatusDatumArrayList.get(j);
                            if (datum.getId().equals(id)) {
                                datum.setIsChecked("1");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("")
                        .setMessage("No Item found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();
            }
//            } else {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("")
//                        .setMessage("No Item found.")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        }).create().show();
//            }
        }
    }

    @Override
    protected void showProgress(String msg) {
        super.showProgress(msg);
    }

    @Override
    protected void dismissProgress() {
        super.dismissProgress();
    }

    @Override
    protected void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    protected void showAlert(String title, String msg) {
        super.showAlert(title, msg);
    }
}
