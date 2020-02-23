package com.plantlogger.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.RequirementAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.SyncModels.AreaCheckListSyncModel;
import com.plantlogger.models.SyncModels.EquipmentStatusSyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequirementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.breadCrum)
    TextView breadCrum;
    @BindView(R.id.listview)
    ListView listview;

    RequirementAdapter adapter;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    String title_text;

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / " + title_text, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] values = new String[]{"Equipment Status", "Parameter Data Entry", "Area Check List"};
        adapter = new RequirementAdapter(this, values);
        listview.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);
        ButterKnife.bind(this);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        title_text = intent.getStringExtra("name");
        String groupId = intent.getStringExtra("groupId");
        if (!TextUtils.isEmpty(groupId)) {
            preferencesKeeper.setGroupId(groupId);
        }
        title.setText(title_text);
        AppConstants.breadCrumString = AppConstants.breadCrumString + " / " + title_text;
        breadCrum.setText(AppConstants.breadCrumString);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentStatus();
                areaCheckList();
                paramData();
                adapter.notifyDataSetChanged();
                FancyToast.makeText(RequirementActivity.this
                        , "Data saved successfully."
                        , 2000, FancyToast.SUCCESS, false).show();
            }
        });
    }

    public void equipmentStatus() {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            AreaItemDatum areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                    , preferencesKeeper.getCustomerId(), "equipment_status");
            if (areaItemDatum != null) {
                db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);

                ArrayList<EquipmentStatusSyncModel> savedItems = databaseHelper.getEquipmentync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getEquipment_id(),areaItemDatum.getId());
                if (savedItems != null && !savedItems.isEmpty()) {
                    if (savedItems.size() > 0) {

                        for (int i = 0; i < savedItems.size(); i++) {
                            db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                            if (AppConstants.qrTag) {
                                databaseHelper.updateEquipmentync(db, preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getEquipment_id(), "1");
                            } else {
                                databaseHelper.updateEquipmentync(db, preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getEquipment_id(), "0");
                            }

                        }
                    }
                }
            }
        }
    }

    public void areaCheckList() {

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            AreaItemDatum areaItemDatum = databaseHelper.getStatusItems(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                    , preferencesKeeper.getCustomerId(), "area_check_list");
            if (areaItemDatum != null) {
                db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);

                ArrayList<AreaCheckListSyncModel> savedItems = databaseHelper.getCheckListync(db, preferencesKeeper.getEmail(),
                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getChecklist_id(),areaItemDatum.getId());
                if (savedItems != null && !savedItems.isEmpty()) {
                    if (savedItems.size() > 0) {
                        for (int i = 0; i < savedItems.size(); i++) {
                            db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                            if (AppConstants.qrTag) {
                                databaseHelper.updateCheckListync(db, preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getChecklist_id(), "1");
                            } else {

                                databaseHelper.updateCheckListync(db, preferencesKeeper.getEmail(),
                                        preferencesKeeper.getCustomerId(), preferencesKeeper.getGroupId(), areaItemDatum.getChecklist_id(), "0");
                            }
                        }
                    }
                }
            }
        }
    }

    public void paramData() {


        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            ArrayList<AreaItemDatum> areasDatumArrayList = databaseHelper.getAreas_Items(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId(),"param");
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                if (areasDatumArrayList.size() > 0) {
                    for (int i = 0; i < areasDatumArrayList.size(); i++) {
                        AreaItemDatum areaItemDatum = areasDatumArrayList.get(i);
                        db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                        databaseHelper.updateAreas_Items(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "0");

                        db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                        Boolean data = databaseHelper.ParamSyncData(db, areaItemDatum.getId()
                                , preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId());
                        if (data) {
                            db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                            databaseHelper.updateMegaParamSync(db, areaItemDatum.getId()
                                    , preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId(), preferencesKeeper.getEmail());

                        }
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(RequirementActivity.this, ScanQRActivity.class);
                startActivity(intent);
            } else {
                FancyToast.makeText(this, "Permission is required.", Toast.LENGTH_LONG, FancyToast.WARNING, false).show();
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }
            }
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
