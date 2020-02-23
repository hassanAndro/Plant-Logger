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

import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListDatum;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.AreaCheckListAdapter;
import com.plantlogger.adapter.EquipmentStatusAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.SyncModels.AreaCheckListSyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AreaCheckListActivity extends BaseActivity {


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
    AreaCheckListAdapter adapter;


    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    //    AreaItemDatum areaItemDatum;
    ArrayList<AreaCheckListDatum> areasDatumArrayList;
    Boolean tag = false;
    String ID, name, check_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_check_list);
        ButterKnife.bind(this);
        ID = getIntent().getExtras().getString("ID");
        name = getIntent().getExtras().getString("name");
        check_id = getIntent().getExtras().getString("check_id");
        AppConstants.breadCrumString = AppConstants.breadCrumString + " / Area Check List";
        breadCrum.setText(AppConstants.breadCrumString);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);
        if (preferencesKeeper.getGroupId() != null) {
            getAreaCheckListItems(preferencesKeeper.getGroupId());
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
                if (areasDatumArrayList != null) {
                    if (adapter.getSelectedString() != null && !adapter.getSelectedString().isEmpty()) {
                        Log.e("adapter", "getSelectedString final size: " + adapter.getSelectedString().size());
                        SQLiteDatabase db = DatabaseHelper.getInstance(AreaCheckListActivity.this).getWritableDatabase(AppConstants.db_password);
                        if (db != null) {
                            if (areasDatumArrayList.get(0).getMultiple().equals("0")) {

                                databaseHelper.deleteCheckListSync(db,
                                        preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(),
                                        areasDatumArrayList.get(0).getSets_idx(),
                                        preferencesKeeper.getGroupId(), ID);
                            }
                            for (int i = 0; i < adapter.getSelectedString().size(); i++) {
                                if (db != null) {
                                    db = DatabaseHelper.getInstance(AreaCheckListActivity.this).getWritableDatabase(AppConstants.db_password);
                                    if (AppConstants.qrTag) {

                                        tag = databaseHelper.createCheckListSync(db,
                                                adapter.getSelectedString().get(i),
                                                preferencesKeeper.getEmail(),
                                                preferencesKeeper.getCustomerId(),
                                                Utils.currentDateTime(), "1", ID, name);
                                    } else {
                                        tag = databaseHelper.createCheckListSync(db,
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
                            FancyToast.makeText(AreaCheckListActivity.this, "Data saved successfully.", 2000, FancyToast.SUCCESS, false).show();
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
                Intent i = new Intent(AreaCheckListActivity.this, AreaCheckListNoteActivity.class);
                startActivity(i);
            }
        });
    }

    public void getAreaCheckListItems(String groupId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(AreaCheckListActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            Log.e("asdds", "email: " + preferencesKeeper.getEmail());
            Log.e("asdds", "group_id: " + preferencesKeeper.getGroupId());
            Log.e("asdds", "CUSTOMER_id: " + preferencesKeeper.getCustomerId());
            Log.e("asdds", "ID: " + ID);
//            areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail()
//                    , groupId, preferencesKeeper.getCustomerId(), "area_check_list");
//            if (areaItemDatum != null) {
            db = DatabaseHelper.getInstance(AreaCheckListActivity.this).getWritableDatabase(AppConstants.db_password);
            areasDatumArrayList = databaseHelper.getAreaCheckListData(db, check_id);
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                db = DatabaseHelper.getInstance(AreaCheckListActivity.this).getWritableDatabase(AppConstants.db_password);

                adapter = new AreaCheckListAdapter(this, areasDatumArrayList);
                listview.setAdapter(adapter);

                ArrayList<AreaCheckListSyncModel> savedItems = databaseHelper.getCheckListync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), check_id, ID);
                if (savedItems != null && !savedItems.isEmpty()) {
                    for (int i = 0; i < savedItems.size(); i++) {
                        AreaCheckListSyncModel model = savedItems.get(i);
                        String id = model.getChecklist_id();
                        for (int j = 0; j < areasDatumArrayList.size(); j++) {
                            AreaCheckListDatum datum = areasDatumArrayList.get(j);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / Area Check List", "");
    }
}
