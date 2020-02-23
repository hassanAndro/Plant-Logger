package com.plantlogger.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plantlogger.ApiCalls.APIClient;
import com.plantlogger.ApiCalls.APIInterface;
import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListDatum;
import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListResponse;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusResponse;
import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.ApiResponse.FetchArea.AreasResponse;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemsResponse;
import com.plantlogger.ApiResponse.FetchUsers.UsersDatum;
import com.plantlogger.ApiResponse.FetchUsers.UsersResponse;
import com.plantlogger.ApiResponse.SelectionSets.SelectionSetsDatum;
import com.plantlogger.ApiResponse.SelectionSets.SelectionSetsResponse;
import com.plantlogger.ApiResponse.SyncResponse;
import com.plantlogger.ApiResponse.Units.Datum;
import com.plantlogger.ApiResponse.Units.UnitsResponse;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.AreaAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.DBSyncModel.AreaCheckListDBSyncModel;
import com.plantlogger.models.DBSyncModel.DBSyncMain;
import com.plantlogger.models.DBSyncModel.EquipmentStatusDBSyncModel;
import com.plantlogger.models.DBSyncModel.NotesDBSyncModel;
import com.plantlogger.models.DBSyncModel.ParamterDataEntryDBSyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.stetho.inspector.network.PrettyPrinterDisplayType.JSON;

public class HomeAreasActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.LogsMain)
    LinearLayout LogsMain;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sync)
    Button sync;
    @BindView(R.id.changePass)
    Button changePass;
    @BindView(R.id.scan)
    Button scan;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.listview)
    ListView listview;
    AreaAdapter adapter;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    APIInterface apiInterface;

    @Override
    protected void onResume() {
        super.onResume();
        AppConstants.qrTag = false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        setTitle(null);
        preferencesKeeper = new PreferencesKeeper(this);
        name.setText(preferencesKeeper.getName());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase.loadLibs(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, topToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        clicks();


        areasCheck(drawer);

    }

    public void areasCheck(final DrawerLayout drawerLayout) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (databaseHelper.areasCount(db)) {
                getAreas(drawerLayout);
            } else {
                alertForSync(drawerLayout);
            }
        }
    }

    public void alertForSync(final DrawerLayout drawerLayout) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("")
                .setMessage("You don't have any area in record, Please Sync Areas.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }).create().show();
    }

    public void getAreas(DrawerLayout drawerLayout) {
        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            ArrayList<AreasDatum> areasDatumArrayList = databaseHelper.getAreas(db, preferencesKeeper.getEmail(), "0", preferencesKeeper.getCustomerId());
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                scan.setVisibility(View.VISIBLE);
                adapter = new AreaAdapter(this, areasDatumArrayList);
                listview.setAdapter(adapter);
            } else {
                alertForSync(drawerLayout);
            }
        }
    }

    public void clicks() {

        scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(HomeAreasActivity.this, ScanQRActivity.class);
                    startActivity(intent);
                }
            }
        });
        LogsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                if (Utils.wifiCheck(HomeAreasActivity.this)) {
                    createJsonSync();
                    areasCall(drawer);
                } else {
                    showAlert("", "No internet access.");
                }
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent i = new Intent(HomeAreasActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesKeeper.clearAppData();
                Intent intent = new Intent(HomeAreasActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createJsonSync() {

        DBSyncMain main = new DBSyncMain();
        main.setTime(Utils.currentDateTime());
        String email_pref = preferencesKeeper.getEmail();
        String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
        main.setEmail(removedBraces);
        main.setAdminId(preferencesKeeper.getID());
        if (getParamData() != null) {
            main.setParameter_data_entry(getParamData());
        }
        if (getEquipmentData() != null) {
            main.setEquipment_status(getEquipmentData());
        }
        if (getAreaCheckListData() != null) {
            main.setAreachecklist_status(getAreaCheckListData());
        }
        if (getNotesData() != null) {
            main.setNotes(getNotesData());
        }
        Log.e("SyncData", "json: " + main.toJson().toString());

        if (getParamData().size() > 0 || getAreaCheckListData().size() > 0 || getEquipmentData().size() > 0 || getNotesData().size() > 0) {
            syncDataCall(main.toJson().toString());
        }
    }


    public ArrayList<ParamterDataEntryDBSyncModel> getParamData() {

        ArrayList<ParamterDataEntryDBSyncModel> models = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            models = databaseHelper.getParamDBSyncModel(
                    db
                    , preferencesKeeper.getEmail()
                    , preferencesKeeper.getCustomerId());
            if (models == null) {
                models = new ArrayList<ParamterDataEntryDBSyncModel>();
            }
        }
        return models;
    }

    public ArrayList<EquipmentStatusDBSyncModel> getEquipmentData() {

        ArrayList<EquipmentStatusDBSyncModel> models = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            models = databaseHelper.getEquipmentDBSyncModel(
                    db
                    , preferencesKeeper.getEmail()
                    , preferencesKeeper.getCustomerId());
            if (models == null) {
                models = new ArrayList<EquipmentStatusDBSyncModel>();
            }
        }
        return models;
    }

    public ArrayList<AreaCheckListDBSyncModel> getAreaCheckListData() {

        ArrayList<AreaCheckListDBSyncModel> models = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            models = databaseHelper.getAreaCheckListDBSyncModel(
                    db
                    , preferencesKeeper.getEmail()
                    , preferencesKeeper.getCustomerId());

            if (models == null) {
                models = new ArrayList<AreaCheckListDBSyncModel>();
            }
        }
        return models;
    }

    public ArrayList<NotesDBSyncModel> getNotesData() {

        ArrayList<NotesDBSyncModel> models = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            models = databaseHelper.getNotesDBSyncModel(
                    db
                    , preferencesKeeper.getEmail()
                    , preferencesKeeper.getCustomerId());
            if (models == null) {
                models = new ArrayList<NotesDBSyncModel>();
            }
        }
        return models;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(HomeAreasActivity.this, ScanQRActivity.class);
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

    public void syncDataCall(String data) {
        showProgress("Syncing Data...");
        Log.e("SyncData", "started");
        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.sendSyncData(removedBraces, data);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                SyncResponse syncResponse = (SyncResponse) response.body();
                                if (syncResponse != null) {
                                    Log.e("SyncData", "Sent Result: " + syncResponse.toJson());
                                    if (syncResponse.getStatus().equals("200")) {
                                        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                        if (db != null) {
                                            String date_time = databaseHelper.getMaxDateTime(db, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId());
                                            Log.e("asjdha", "date_time:" + date_time);
                                            db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                            if (db != null) {
                                                databaseHelper.deleteAfterSync(db, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(), date_time);
                                            }

                                            db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                            if (db != null) {
                                                databaseHelper.setIsSynced(db, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId());
                                            }

//                                            db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
//                                            if (db != null) {
//                                                databaseHelper.deleteParam_Sync(db);
//                                            }
                                            db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                            if (db != null) {
                                                databaseHelper.updateAreas_ItemsIsFilled(db, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId());
                                            }
                                        }

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(syncResponse.getMessage())) {
                                            showAlert("Error", syncResponse.getMessage());
                                        } else {
                                            showAlert("Error", " Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.toString());
                        Log.e("SyncData", "SyncData response onFailure: " + t.toString());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void areasCall(final DrawerLayout drawerLayout) {


        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {


                showProgress("Fetching Data...");
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.areas(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                AreasResponse areasResponse = (AreasResponse) response.body();
                                if (areasResponse != null) {
                                    if (areasResponse.getStatus() == 200) {
                                        Log.e("Areas", "Areas Result: " + areasResponse.getData());
                                        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                        if (db != null && areasResponse.getData() != null) {
                                            databaseHelper.createAreas(db, areasResponse.getData(), preferencesKeeper.getEmail());
                                            getAreas(drawerLayout);
                                            area_itemsCall();
                                        }

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(areasResponse.getMessage())) {
                                            showAlert("Error", areasResponse.getMessage());
                                        } else {
                                            showAlert("Error", "Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("Areas", "response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void area_itemsCall() {


        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.areas_items(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                AreaItemsResponse areasResponse = (AreaItemsResponse) response.body();
                                if (areasResponse != null) {
                                    if (areasResponse.getStatus() == 200) {
                                        Log.e("Areas", "Areas_Items Result: " + areasResponse.getData());
                                        SQLiteDatabase db = DatabaseHelper.getInstance(HomeAreasActivity.this).getWritableDatabase(AppConstants.db_password);
                                        if (db != null && areasResponse.getData() != null) {
                                            databaseHelper.createAreas_Items(db, areasResponse.getData(), preferencesKeeper.getEmail());
                                            fetchUsersCall();
                                        }

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(areasResponse.getMessage())) {
                                            showAlert("Error", areasResponse.getMessage());
                                        } else {
                                            showAlert("Error", " Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("Areas", "Areas_Items response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void fetchUsersCall() {

        Call call = apiInterface.users();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        UsersResponse usersResponse = (UsersResponse) response.body();
                        if (usersResponse != null) {
                            if (usersResponse.getStatus() == 200) {
                                Log.e("Areas", "Users Result: " + usersResponse.getData());
                                saveUsers(usersResponse.getData());
                                preferencesKeeper.setSyncTime(System.currentTimeMillis());
                                fetchUnits();

                            } else {
                                dismissProgress();
                                if (!TextUtils.isEmpty(usersResponse.getMessage())) {
                                    showAlert("Error", usersResponse.getMessage());
                                } else {
                                    showAlert("Error", "Response was unsuccessful.");
                                }
                            }
                        }

                    } else {
                        dismissProgress();
                        showAlert("Failure", "Response was unsuccessful.");
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                dismissProgress();
                showAlert("Failure", t.getMessage());
                Log.e("Areas", "Users response onFailure: " + t.getMessage());
                call.cancel();
            }
        });
    }

    public void fetchUnits() {

        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.units(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                UnitsResponse unitsResponse = (UnitsResponse) response.body();
                                if (unitsResponse != null) {
                                    if (unitsResponse.getStatus() == 200) {
                                        Log.e("Areas", "Units Result: " + unitsResponse.getData());
                                        saveUnits(unitsResponse.getData());
                                        fetchAreaCheckList();

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(unitsResponse.getMessage())) {
                                            showAlert("Error", unitsResponse.getMessage());
                                        } else {
                                            showAlert("Error", "Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("Areas", "Units response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void fetchAreaCheckList() {

        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.area_check_list(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                AreaCheckListResponse areaCheckListResponse = (AreaCheckListResponse) response.body();
                                if (areaCheckListResponse != null) {
                                    if (areaCheckListResponse.getStatus() == 200) {
                                        Log.e("Areas", "AreaCheckList Result: " + areaCheckListResponse.getData());
                                        saveAreaCheckList(areaCheckListResponse.getData());
                                        fetchEquipmentStatus();

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(areaCheckListResponse.getMessage())) {
                                            showAlert("Error", areaCheckListResponse.getMessage());
                                        } else {
                                            showAlert("Error", "Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("Areas", "Area Check List response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void fetchEquipmentStatus() {

        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.equipment_status(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                EquipmentStatusResponse equipmentStatusResponse = (EquipmentStatusResponse) response.body();
                                if (equipmentStatusResponse != null) {
                                    if (equipmentStatusResponse.getStatus() == 200) {
                                        Log.e("Areas", "equipmentStatusResponse Result: " + equipmentStatusResponse.getData());
                                        saveEquipmentStatus(equipmentStatusResponse.getData());
                                        fetchSelectionSets();

                                    } else {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(equipmentStatusResponse.getMessage())) {
                                            showAlert("Error", equipmentStatusResponse.getMessage());
                                        } else {
                                            showAlert("Error", "Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                dismissProgress();
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("Areas", "equipmentStatusResponse response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void fetchSelectionSets() {

        if (preferencesKeeper != null) {
            if (preferencesKeeper.getEmail() != null) {
                String email_pref = preferencesKeeper.getEmail();
                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                Call call = apiInterface.selection_sets(removedBraces);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                SelectionSetsResponse equipmentStatusResponse = (SelectionSetsResponse) response.body();
                                if (equipmentStatusResponse != null) {
                                    if (equipmentStatusResponse.getStatus() == 200) {
                                        Log.e("Areas", "SelectionSetsResponse Result: " + equipmentStatusResponse.getData());
                                        saveSelectionSets(equipmentStatusResponse.getData());

                                    } else {
                                        Log.e("Areas", "SelectionSetsResponse error: " + equipmentStatusResponse.getMessage());
                                        if (!TextUtils.isEmpty(equipmentStatusResponse.getMessage())) {
                                            showAlert("Error", equipmentStatusResponse.getMessage());
                                        } else {
                                            showAlert("Error", "Response was unsuccessful.");
                                        }
                                    }
                                }

                            } else {
                                Log.e("Areas", "SelectionSetsResponse unsucsseefull error: ");
                                showAlert("Failure", "Response was unsuccessful.");
                            }
                        }
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("Areas", "SelectionSetsResponse onFailure error: " + t.getMessage());
                        dismissProgress();
                        showAlert("Failure", t.getMessage());
                        Log.e("SelectionSetsResponse", "SelectionSetsResponse response onFailure: " + t.getMessage());
                        call.cancel();
                    }
                });
            }
        }
    }

    public void saveUsers(ArrayList<UsersDatum> users) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (users != null) {
                databaseHelper.createUsers(db, users);
            }
        }
    }

    public void saveUnits(ArrayList<Datum> units) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (units != null) {
                databaseHelper.createUnits(db, units);
            }
        }
    }

    public void saveAreaCheckList(ArrayList<AreaCheckListDatum> areaCheckListData) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (areaCheckListData != null) {
                databaseHelper.createAreaCheckList(db, areaCheckListData);
            }
        }
    }

    public void saveEquipmentStatus(ArrayList<EquipmentStatusDatum> equipmentStatusData) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (equipmentStatusData != null) {
                databaseHelper.createEquipmentStatus(db, equipmentStatusData);
            }
        }
    }

    public void saveSelectionSets(ArrayList<SelectionSetsDatum> selectionSetsData) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (selectionSetsData != null) {
                databaseHelper.createSelectionSet(db, selectionSetsData);
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        if (preferencesKeeper != null) {
            if (preferencesKeeper.getSyncTime() != -1) {
                long timeInMilliSeconds = System.currentTimeMillis() - preferencesKeeper.getSyncTime();

                long seconds = timeInMilliSeconds / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
//                String finalTime = hours % 24 + " hours " + minutes % 60 + " mins " + seconds % 60 + " secs ago";
                String finalTime = hours % 24 + " hours ago";
                time.setText(finalTime);

            }
        }

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
