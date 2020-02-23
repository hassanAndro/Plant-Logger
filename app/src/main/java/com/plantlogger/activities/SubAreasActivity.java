package com.plantlogger.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.AreaAdapter;
import com.plantlogger.adapter.SubAreaAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plantlogger.utils.AppConstants.breadCrumString;

public class SubAreasActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.breadCrum)
    TextView breadCrum;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.scan)
    Button scan;
    @BindView(R.id.listview)
    ListView listview;

    SubAreaAdapter adapter;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;


    String title_text;
    String father_id;

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.breadCrumString = "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_areas);
        ButterKnife.bind(this);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();

        title_text = intent.getStringExtra("name");
        father_id = intent.getStringExtra("father_id");

        title.setText(title_text);
        if (TextUtils.isEmpty(AppConstants.breadCrumString)) {
            breadCrumString = "Logs / " + title_text;
        } else {
            AppConstants.breadCrumString = AppConstants.breadCrumString + " / " + title_text;
        }
        breadCrum.setText(breadCrumString);


        breadCrum.setText(AppConstants.breadCrumString);

        getSubAreas(father_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(SubAreasActivity.this, ScanQRActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void getSubAreas(String father_id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(SubAreasActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            ArrayList<AreasDatum> areasDatumArrayList = databaseHelper.getAreas(db, preferencesKeeper.getEmail(), father_id, preferencesKeeper.getCustomerId());
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                adapter = new SubAreaAdapter(this, areasDatumArrayList);
                listview.setAdapter(adapter);
            } else {
                AppConstants.breadCrumString = "";
                breadCrumString = "Logs ";
                Intent i = new Intent(SubAreasActivity.this, RequirementActivity.class);
                i.putExtra("name", title_text);
                i.putExtra("groupId", father_id);
                startActivity(i);
                finish();
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("")
//                        .setMessage("No Sub Area found.")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        }).create().show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(SubAreasActivity.this, ScanQRActivity.class);
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
