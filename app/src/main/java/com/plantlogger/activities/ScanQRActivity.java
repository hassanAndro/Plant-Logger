package com.plantlogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.SubAreaAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanQRActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    @BindView(R.id.back)
    RelativeLayout back;

    DatabaseHelper databaseHelper;
    PreferencesKeeper preferencesKeeper;
    Boolean tag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        ButterKnife.bind(this);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(false);

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.e("onQRCodeRead", "string: " + text);
        if (tag) {
            tag = false;
            SQLiteDatabase db = DatabaseHelper.getInstance(ScanQRActivity.this).getWritableDatabase(AppConstants.db_password);
            if (db != null) {
                AreasDatum areasDatumArrayList = databaseHelper.getAreasFromQR(db
                        , preferencesKeeper.getEmail()
                        , text
                        , preferencesKeeper.getCustomerId());

                if (areasDatumArrayList != null) {

                    db = DatabaseHelper.getInstance(ScanQRActivity.this).getWritableDatabase(AppConstants.db_password);
                    if (db != null) {
                        ArrayList<AreasDatum> areasDatumArray_List = databaseHelper.getAreas(db, preferencesKeeper.getEmail()
                                , areasDatumArrayList.getCatId(), preferencesKeeper.getCustomerId());
                        if (areasDatumArray_List != null && !areasDatumArray_List.isEmpty()) {

                            if (areasDatumArray_List.size() > 0) {
                                navigateSubArea(areasDatumArrayList.getCatName(), areasDatumArrayList.getCatId());

                            } else {
                                navigateRequirement(areasDatumArrayList.getCatName(), areasDatumArrayList.getCatId());
                            }


                        } else {
                            navigateRequirement(areasDatumArrayList.getCatName(), areasDatumArrayList.getCatId());
                        }
                    }

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("")
                            .setMessage("QR not found.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tag = true;
                                }
                            }).create().show();
                }
            }
        }
    }

    public void navigateRequirement(String name, String group_id) {

        Intent i = new Intent(this, RequirementActivity.class);
        i.putExtra("name", name);
        i.putExtra("groupId", group_id);
        AppConstants.qrTag = true;
        startActivity(i);
        finish();
    }

    public void navigateSubArea(String name, String father_id) {

        Intent i = new Intent(this, SubAreasActivity.class);
        i.putExtra("name", name);
        i.putExtra("father_id", father_id);
        AppConstants.qrTag = true;
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
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