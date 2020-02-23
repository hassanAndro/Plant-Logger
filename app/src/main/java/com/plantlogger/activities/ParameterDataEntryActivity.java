package com.plantlogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.ParameterDataEntryAdapter;
import com.plantlogger.adapter.SubAreaAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParameterDataEntryActivity extends BaseActivity {

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
    @BindView(R.id.title)
    TextView title;
    ParameterDataEntryAdapter adapter;
    String path;
    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    public static ArrayList<AreaItemDatum> areasDatumArrayList;

    @Override
    public void onResume() {
        super.onResume();
        if (preferencesKeeper.getGroupId() != null) {
            areasDatumArrayList = null;
            path = getIntent().getExtras().getString("path");
            if (!TextUtils.isEmpty(path)) {
                getAreaItems(preferencesKeeper.getGroupId(), path);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (path.equals("param")) {
            AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / Parameter Data Entry", "");
        } else if (path.equals("equipment")) {
            AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / Equipment Status", "");
        } else if (path.equals("check")) {
            AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / Area Check List", "");
        }


    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_data_entry);
        ButterKnife.bind(this);
        path = getIntent().getExtras().getString("path");
        if (path.equals("param")) {
            title.setText("Parameter Data Entry");
            AppConstants.breadCrumString = AppConstants.breadCrumString + " / Parameter Data Entry";
        } else if (path.equals("equipment")) {
            title.setText("Equipment Status");
            AppConstants.breadCrumString = AppConstants.breadCrumString + " / Equipment Status";
        } else if (path.equals("check")) {
            title.setText("Area Check List");
            AppConstants.breadCrumString = AppConstants.breadCrumString + " / Area Check List";
        }
        breadCrum.setText(AppConstants.breadCrumString);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (path.equals("param")) {
                    Intent i = new Intent(ParameterDataEntryActivity.this, ParamerterDataNoteActivity.class);
                    startActivity(i);
                } else if (path.equals("equipment")) {
                    Intent i = new Intent(ParameterDataEntryActivity.this, EquipmentStatusNoteActivity.class);
                    startActivity(i);
                } else if (path.equals("check")) {
                    Intent i = new Intent(ParameterDataEntryActivity.this, AreaCheckListNoteActivity.class);
                    startActivity(i);
                }
            }
        });
        //TODO
        save.setVisibility(View.INVISIBLE);
    }

    public void getAreaItems(String groupId, String path) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            areasDatumArrayList = databaseHelper.getAreas_Items(db, preferencesKeeper.getEmail(), groupId, preferencesKeeper.getCustomerId(), path);
            if (areasDatumArrayList != null && !areasDatumArrayList.isEmpty()) {
                adapter = new ParameterDataEntryAdapter(this, areasDatumArrayList, path);
                listview.setAdapter(adapter);
//                setListViewHeightBasedOnItems(listview);
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
