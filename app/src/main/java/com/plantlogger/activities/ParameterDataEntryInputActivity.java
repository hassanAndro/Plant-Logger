package com.plantlogger.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.adapter.SpinnerAdapter;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.models.SpinnerItemModel;
import com.plantlogger.models.SyncModels.ParameterDataEntrySyncModel;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.KeyboardView;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.thomashaertel.widget.MultiSpinner;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plantlogger.activities.ParameterDataEntryActivity.areasDatumArrayList;

public class ParameterDataEntryInputActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.info)
    RelativeLayout info;
    @BindView(R.id.breadCrum)
    TextView breadCrum;
    @BindView(R.id.note_area)
    KeyboardView note_area;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.spinnerMulti)
    MultiSpinner spinnerMulti;
    @BindView(R.id.back_btn)
    Button back_btn;
    @BindView(R.id.next_btn)
    Button next_btn;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.check)
    LinearLayout check;

    AreaItemDatum areaItemDatum;
    ParameterDataEntrySyncModel dataEntrySyncModel;
    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    String title_text;
    String ID = null;
    String absolute_range_flag = null;
    String setID = null;

    String unit_id_main = null;
    String unit_name_main = null;

    public ArrayList<SpinnerItemModel> spinnerItems = null;
    SpinnerAdapter spinnerAdapter;
    Boolean saveTag = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / " + title_text, "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_data_entry_input);
        ButterKnife.bind(this);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        title_text = intent.getStringExtra("name");
        ID = intent.getStringExtra("ID");
        absolute_range_flag = intent.getStringExtra("absolute_range_flag");
        setID = intent.getStringExtra("set_id");
        title.setText(title_text);
        AppConstants.breadCrumString = AppConstants.breadCrumString + " / " + title_text;
        breadCrum.setText(AppConstants.breadCrumString);

        note_area.setInputType(InputType.TYPE_NULL);


        if (preferencesKeeper.getGroupId() != null && ID != null && absolute_range_flag != null) {
            if (absolute_range_flag.equals("0")) {

                spinnerItems = getUnits(setID);
                if (spinnerItems != null) {
                    if (spinnerItems.size() > 0) {
                        if (spinnerItems.get(0).getMultiple().equals("0")) {
                            spinner.setVisibility(View.VISIBLE);
                            spinnerMulti.setVisibility(View.GONE);

                            Resources res = getResources();
                            spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_row, spinnerItems, res);
                            spinner.setAdapter(spinnerAdapter);
                            spinner.setSelection(spinnerItems.size() - 1);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.e("Units", "ID: " + spinnerItems.get(i).getItem_id());
                                    Log.e("Units", "Name: " + spinnerItems.get(i).getItem_name());
                                    unit_id_main = spinnerItems.get(i).getItem_id();
                                    unit_name_main = spinnerItems.get(i).getItem_name();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

                        } else {
                            spinner.setVisibility(View.GONE);
                            spinnerMulti.setVisibility(View.VISIBLE);

//                            Resources res = getResources();
//                            spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_row, spinnerItems, res);
//                            spinnerMulti.setAdapter(spinnerItems, false);

                            ArrayList<String> names = new ArrayList<String>();
                            for (int i = 0; i < spinnerItems.size(); i++) {
                                SpinnerItemModel model = spinnerItems.get(i);
                                if (!model.getItem_name().equals("Select an option")) {
                                    names.add(model.getItem_name());
                                }
                            }
                            final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
                            adapter.addAll(names);
                            spinnerMulti.setAdapter(adapter, false, null);


//                            boolean[] selectedItems = new boolean[adapter.getCount()];
//                            selectedItems[adapter.getCount() - 1] = true; // select second item
                            spinnerMulti.setOnItemsSelectedListener(new MultiSpinner.MultiSpinnerListener() {
                                @Override
                                public void onItemsSelected(boolean[] selected) {

                                    if (selected != null && selected.length > 0) {

                                        StringBuilder selected_names = new StringBuilder();
                                        StringBuilder selected_ids = new StringBuilder();

                                        for (int i = 0; i < selected.length; i++) {
                                            if (selected[i]) {
                                                if (selected.length > 1) {
                                                    selected_ids.append(spinnerItems.get(i).getItem_id()).append(",");
                                                    selected_names.append(spinnerItems.get(i).getItem_name()).append(",");

                                                } else {
                                                    selected_ids.append(spinnerItems.get(i).getItem_id());
                                                    selected_names.append(spinnerItems.get(i).getItem_name());
                                                }
                                            }
                                        }
                                        unit_id_main = selected_ids.toString();
                                        if (null != unit_id_main && unit_id_main.length() > 0) {
                                            int endIndex = unit_id_main.lastIndexOf(",");
                                            if (endIndex != -1) {
                                                unit_id_main = unit_id_main.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                                            }
                                        }
                                        unit_name_main = selected_names.toString();
                                        if (null != unit_name_main && unit_name_main.length() > 0) {
                                            int endIndex = unit_name_main.lastIndexOf(",");
                                            if (endIndex != -1) {
                                                unit_name_main = unit_name_main.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                                            }
                                        }
                                    } else {
                                        unit_id_main = null;
                                        unit_name_main = null;
                                    }
                                }
                            });
                        }
                    }
                }

                info.setVisibility(View.GONE);
                if (setID != null) {

                    spinnerItems = getUnits(setID);
                }
            } else {
                note_area.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);


//                note_area.addTextChangedListener(new TextWatcher() {
//
//                    public void afterTextChanged(Editable s) {
//
//                    }
//
//                    public void beforeTextChanged(CharSequence s, int start,
//                                                  int count, int after) {
//                    }
//
//                    public void onTextChanged(CharSequence s, int start,
//                                              int before, int count) {
//
//
//                    }
//                });
            }
            getAreaItems(preferencesKeeper.getGroupId(), ID, absolute_range_flag);
            getParamSync();
        }

        if (AppConstants.position != areasDatumArrayList.size() - 1) {
            next_btn.setEnabled(true);
            next_btn.setBackgroundResource(R.drawable.login_btn);
        } else {
            next_btn.setEnabled(false);
            next_btn.setBackgroundResource(R.drawable.login_btn_dark);
        }

        if (AppConstants.position != 0) {
            back_btn.setEnabled(true);
            back_btn.setBackgroundResource(R.drawable.login_btn);
        } else {
            back_btn.setEnabled(false);
            back_btn.setBackgroundResource(R.drawable.login_btn_dark);
        }

        if (AppConstants.position == areasDatumArrayList.size() - 1 && AppConstants.position == 0) {
            back_btn.setVisibility(View.GONE);
            next_btn.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveDataAbsolute();
//                saveDataUnit();
                finish();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(false);

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(true);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (absolute_range_flag.equals("0")) {
                    saveData(false);
                } else {
                    saveData(true);
                }
            }
        });
    }

    public Boolean checkInput() {
        Boolean tag = true;
        if (areaItemDatum != null) {
            if (absolute_range_flag != null) {
                if (absolute_range_flag.equals("1")) {
                    if (!TextUtils.isEmpty(note_area.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(areaItemDatum.getOMin()) && !TextUtils.isEmpty(areaItemDatum.getOMax())) {
//                            if (!TextUtils.isEmpty(areaItemDatum.getAMin()) && !TextUtils.isEmpty(areaItemDatum.getAMax())) {
                            if (!TextUtils.isEmpty(note_area.getText().toString().trim())) {

                                if (Float.parseFloat(note_area.getText().toString().trim()) < Float.parseFloat(areaItemDatum.getOMin()) ||
                                        Float.parseFloat(note_area.getText().toString().trim()) > Float.parseFloat(areaItemDatum.getOMax())) {
                                    tag = false;
                                }
                            }
//                            }
                        }
                    }
                }
            }
        }
        return tag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveDataAbsolute();
        saveDataUnit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    public void fetchSavedData() {
//        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
//        if (db != null) {
//            parameterDataEntrySyncModel = databaseHelper.getInputParamters(db, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId() , ID, absolute_range_flag, preferencesKeeper.getCustomerId());
//        }
//    }

    public void saveDataAbsolute() {

        if (!checkbox.isChecked()) {
            if (areaItemDatum != null) {
                if (absolute_range_flag != null) {
                    if (absolute_range_flag.equals("1")) {
                        if (!TextUtils.isEmpty(note_area.getText().toString().trim())) {
                            SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                            if (db != null) {
                                ParameterDataEntrySyncModel model = new ParameterDataEntrySyncModel();
                                model.setArea_item_id(areaItemDatum.getId());
                                model.setName(areaItemDatum.getName());
                                model.setSet_id(areaItemDatum.getSetId());
                                model.setUnit_id(areaItemDatum.getUnitId());
                                model.setGroupd_id(preferencesKeeper.getGroupId());
                                model.setOptimal_range(areaItemDatum.getOptimalRange());
                                model.setAbsolute_range(note_area.getText().toString().trim());
                                model.setIsChecked("0");
                                model.setUnit("");

                                if (checkInput()) {

                                    Boolean data = databaseHelper.ParamSyncData(db, areaItemDatum.getId(), preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId());
                                    if (!data) {
                                        //CREATE
                                        SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                        Boolean tag;
                                        if (AppConstants.qrTag) {
                                            tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                    preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                        } else {
                                            tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                    preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                        }

                                        if (tag) {
                                            saveTag = true;
                                            if (areasDatumArrayList != null) {
                                                SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                                if (dbx != null) {
//                                                    if (Float.parseFloat(note_area.getText().toString().trim()) < Float.parseFloat(areaItemDatum.getOMin()) ||
//                                                            Float.parseFloat(note_area.getText().toString().trim()) > Float.parseFloat(areaItemDatum.getOMax())) {
//
//                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
//                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "FALSE");
//
//                                                    } else
                                                    if (Float.parseFloat(note_area.getText().toString().trim()) < Float.parseFloat(areaItemDatum.getAMin()) ||
                                                            Float.parseFloat(note_area.getText().toString().trim()) > Float.parseFloat(areaItemDatum.getAMax())) {

                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "FALSE");

                                                    } else {

                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "TRUE");
                                                    }
                                                }

                                            }
                                        }

                                    } else {
                                        //UPDATE
                                        SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                        Boolean tag;
                                        if (AppConstants.qrTag) {
                                            tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                    preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                        } else {
                                            tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                    preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                        }

                                        if (tag) {
                                            saveTag = true;
                                            if (areasDatumArrayList != null) {
                                                SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                                if (dbx != null) {
//                                                    if (Float.parseFloat(note_area.getText().toString().trim()) < Float.parseFloat(areaItemDatum.getOMin()) ||
//                                                            Float.parseFloat(note_area.getText().toString().trim()) > Float.parseFloat(areaItemDatum.getOMax())) {
//
//                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
//                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "FALSE");
//
//                                                    } else
                                                    if (Float.parseFloat(note_area.getText().toString().trim()) < Float.parseFloat(areaItemDatum.getAMin()) ||
                                                            Float.parseFloat(note_area.getText().toString().trim()) > Float.parseFloat(areaItemDatum.getAMax())) {

                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "FALSE");
                                                    } else {

                                                        databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                                , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "TRUE");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    showErrorDialog();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (areaItemDatum != null) {
                if (absolute_range_flag != null) {
                    if (absolute_range_flag.equals("1")) {
                        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                        if (db != null) {
                            ParameterDataEntrySyncModel model = new ParameterDataEntrySyncModel();
                            model.setArea_item_id(areaItemDatum.getId());
                            model.setName(areaItemDatum.getName());
                            model.setSet_id(areaItemDatum.getSetId());
                            model.setUnit_id(areaItemDatum.getUnitId());
                            model.setGroupd_id(preferencesKeeper.getGroupId());
                            model.setOptimal_range(areaItemDatum.getOptimalRange());
                            model.setAbsolute_range("0");
                            model.setIsChecked("1");
                            model.setUnit("");

//                            checkInput();

                            Boolean data = databaseHelper.ParamSyncData(db, areaItemDatum.getId(), preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId());
                            if (!data) {
                                //CREATE
                                SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                Boolean tag;
                                if (AppConstants.qrTag) {
                                    tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                            preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                } else {
                                    tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                            preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                }

                                if (tag) {
                                    saveTag = true;
                                    if (areasDatumArrayList != null) {
                                        SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                        if (dbx != null) {
                                            databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                    , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "WRONG");
                                        }
                                    }
                                }
                            } else {
                                //UPDATE
                                SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                Boolean tag;
                                if (AppConstants.qrTag) {
                                    tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                            preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                } else {
                                    tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                            preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                }

                                if (tag) {
                                    saveTag = true;
                                    if (areasDatumArrayList != null) {
                                        SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                        if (dbx != null) {
                                            databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                    , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "WRONG");
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveDataUnit() {
        if (areaItemDatum != null) {

            if (absolute_range_flag != null) {

                if (absolute_range_flag.equals("0")) {

                    if (unit_id_main != null && unit_name_main != null) {

                        if (!unit_id_main.equals("-1")) {

                            SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                            if (db != null) {
                                ParameterDataEntrySyncModel model = new ParameterDataEntrySyncModel();
                                model.setArea_item_id(areaItemDatum.getId());
                                model.setName(areaItemDatum.getName());
                                model.setSet_id(areaItemDatum.getSetId());
                                model.setUnit_id(unit_id_main);
                                model.setUnit(unit_name_main);
                                model.setGroupd_id(preferencesKeeper.getGroupId());
                                model.setOptimal_range(areaItemDatum.getOptimalRange());
                                model.setAbsolute_range("");
                                model.setIsChecked("0");

                                Boolean data = databaseHelper.ParamSyncData(db, areaItemDatum.getId(), preferencesKeeper.getGroupId(), preferencesKeeper.getCustomerId());
                                if (!data) {
                                    //CREATE
                                    SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                    Boolean tag;
                                    if (AppConstants.qrTag) {
                                        tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                    } else {
                                        tag = databaseHelper.createParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                    }

                                    if (tag) {
                                        Log.e("asdhjas", "10");
                                        saveTag = true;
                                        if (areasDatumArrayList != null) {
                                            SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                            if (dbx != null) {
                                                Log.e("asdhjas", "11");
                                                databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                        , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "TRUE");
                                            }
                                        }
                                    }
                                } else {
                                    //UPDATE
                                    SQLiteDatabase db_ = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                    Boolean tag;
                                    if (AppConstants.qrTag) {
                                        tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                preferencesKeeper.getName(), Utils.currentDateTime(), "1");
                                    } else {
                                        tag = databaseHelper.updateParamSync(db_, model, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                                                preferencesKeeper.getName(), Utils.currentDateTime(), "0");
                                    }

                                    if (tag) {
                                        saveTag = true;
                                        if (areasDatumArrayList != null) {
                                            SQLiteDatabase dbx = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
                                            if (dbx != null) {
                                                databaseHelper.updateAreas_Items(dbx, preferencesKeeper.getEmail(), preferencesKeeper.getGroupId()
                                                        , preferencesKeeper.getCustomerId(), areaItemDatum.getId(), areaItemDatum.getName(), "TRUE");
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void showErrorDialog() {
//        FancyToast.makeText(this, "This item's minimum and maximum range is between "
//                + areaItemDatum.getAMin() + " - " + areaItemDatum.getAMax() +
//                ". Please enter a value with in this range.", 3000, FancyToast.ERROR, false).show();
        if (areaItemDatum != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Optimal Range")
                    .setMessage(Html.fromHtml("This item's minimum and maximum range is between <b>"
                            + areaItemDatum.getOMin() + " - " + areaItemDatum.getOMax() +
                            "</b>. Please enter a value with in this range."))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
        }
    }

    public void navigate(Boolean tag) {
        if (absolute_range_flag != null) {
            if (absolute_range_flag.equals("1")) {
                if (!TextUtils.isEmpty(note_area.getText().toString().trim())) {
                    if (checkInput()) {
                        navigateFunc(tag);
                    } else {
                        showErrorDialog();
                    }
                } else {
                    navigateFunc(tag);
                }
            } else {
                navigateFunc(tag);
            }
        } else {
            navigateFunc(tag);
        }
    }

    public void navigateFunc(Boolean tag) {
        saveDataAbsolute();
        saveDataUnit();
        if (tag) {
            AppConstants.position = AppConstants.position + 1;
        } else {
            AppConstants.position = AppConstants.position - 1;
        }
        AppConstants.breadCrumString = AppConstants.breadCrumString.replace(" / " + title_text, "");
        Intent i = new Intent(this, ParameterDataEntryInputActivity.class);
        i.putExtra("name", areasDatumArrayList.get(AppConstants.position).getName());
        i.putExtra("ID", areasDatumArrayList.get(AppConstants.position).getId());
        i.putExtra("absolute_range_flag", areasDatumArrayList.get(AppConstants.position).getAbsoluteRangeFlag());
        i.putExtra("set_id", areasDatumArrayList.get(AppConstants.position).getSetId());
        startActivity(i);
        finish();

    }

    public void saveData(Boolean tag) {
        if (tag) {
            saveDataAbsolute();
        } else {
            saveDataUnit();
        }

        if (saveTag) {
            FancyToast.makeText(this, "Data saved successfully.", 2000, FancyToast.SUCCESS, false).show();
        }


    }

    public ArrayList<SpinnerItemModel> getUnits(String set_id) {
        ArrayList<SpinnerItemModel> unitModels = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            unitModels = databaseHelper.getSpinnerItems(db, set_id);
        }
        if (unitModels != null) {
            SpinnerItemModel model = new SpinnerItemModel();
            model.setItem_id("-1");
            model.setItem_name("Select an option");
            unitModels.add(model);
            return unitModels;
        } else {
            return null;
        }
    }

    public void getAreaItems(String groupId, String Id, String absolute_range_flag) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            areaItemDatum = databaseHelper.getInputParamters(db, preferencesKeeper.getEmail(), groupId, Id, absolute_range_flag, preferencesKeeper.getCustomerId());
            if (areaItemDatum == null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("")
                        .setMessage("No Record Found.")
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

    public void getParamSync() {
        SQLiteDatabase db = DatabaseHelper.getInstance(ParameterDataEntryInputActivity.this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            dataEntrySyncModel = databaseHelper.getParamSync(db, preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId(),
                    title_text, ID, preferencesKeeper.getGroupId());
            if (dataEntrySyncModel != null) {
                if (absolute_range_flag.equals("0")) {
                    if (spinnerItems != null && dataEntrySyncModel.getUnit_id() != null && dataEntrySyncModel.getUnit() != null) {

                        if (spinnerItems.get(0).getMultiple().equals("0")) {

                            for (int i = 0; i < spinnerItems.size(); i++) {
                                SpinnerItemModel model = spinnerItems.get(i);
                                if (model.getItem_id().equals(dataEntrySyncModel.getUnit_id()) && model.getItem_name().equals(dataEntrySyncModel.getUnit())) {
                                    unit_id_main = dataEntrySyncModel.getUnit_id();
                                    unit_name_main = dataEntrySyncModel.getUnit();
                                    spinner.setSelection(i);
                                    break;
                                }
                            }
                        } else {

                            if (dataEntrySyncModel.getUnit().contains(",")) {

                            }

                            for (int i = 0; i < spinnerItems.size(); i++) {
                                SpinnerItemModel model = spinnerItems.get(i);
                                if (model.getItem_id().equals(dataEntrySyncModel.getUnit_id()) && model.getItem_name().equals(dataEntrySyncModel.getUnit())) {
                                    unit_id_main = dataEntrySyncModel.getUnit_id();
                                    unit_name_main = dataEntrySyncModel.getUnit();
//                                    spinner.setSelection(i);

//                                    boolean[] selectedItems = new boolean[adapter.getCount()];
//                                    selectedItems[adapter.getCount() - 1] = true; // select second item
                                }
                            }
                        }
                    }

                } else {
                    if (dataEntrySyncModel.getIsChecked().equals("0")) {
                        note_area.setText(dataEntrySyncModel.getAbsolute_range());
                    } else {
                        note_area.setText("");
                        checkbox.setChecked(true);
                    }
                }
            }
        }
    }


    private void showDialog() {

        final Dialog dialog = new Dialog(this);

        View convertView = getLayoutInflater().inflate(R.layout.custom_menu_info, null);

        TextView numeric_text = (TextView) convertView.findViewById(R.id.numeric_text);
        TextView optimal_text = (TextView) convertView.findViewById(R.id.optimal_text);
        TextView last_value = (TextView) convertView.findViewById(R.id.last_value);
        TextView completed = (TextView) convertView.findViewById(R.id.completed);
        TextView user = (TextView) convertView.findViewById(R.id.user);

        if (areaItemDatum != null) {

            numeric_text.setText("Absolute Range: " + areaItemDatum.getAMin().toString() + " to " + areaItemDatum.getAMax().toString());
            //TODO
            optimal_text.setText("Optimal Range: " + areaItemDatum.getOMin().toString() + " to " + areaItemDatum.getOMax().toString());
            if (dataEntrySyncModel != null) {
                last_value.setText(dataEntrySyncModel.getAbsolute_range());
                completed.setText("Completed: " + dataEntrySyncModel.getDate_time());
                user.setText("User:             " + dataEntrySyncModel.getUser_name());
            } else {
                last_value.setText("");
                completed.setText("Completed: " + "");
                user.setText("User:             " + "");
            }
        } else {
            numeric_text.setText("Absolute Range: " + "");
            last_value.setText("");
            completed.setText("Completed: " + "");
            user.setText("User:             " + "");
        }
        dialog.setContentView(convertView);

        dialog.show();


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
