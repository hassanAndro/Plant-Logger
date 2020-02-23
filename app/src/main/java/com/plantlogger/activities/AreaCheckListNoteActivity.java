package com.plantlogger.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.database.DatabaseHelper;
import com.plantlogger.utils.AppConstants;
import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.sqlcipher.database.SQLiteDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaCheckListNoteActivity extends BaseActivity {
    @BindView(R.id.note_area)
    EditText note_area;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.breadCrum)
    TextView breadCrum;

    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;
    String finalNote;
    String tagName = "areaCheckList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_check_list_note);
        ButterKnife.bind(this);
        preferencesKeeper = new PreferencesKeeper(this);
        databaseHelper = new DatabaseHelper(this);
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
                String note = note_area.getText().toString().trim();
                if (!TextUtils.isEmpty(note)) {
                    SQLiteDatabase db = DatabaseHelper.getInstance(AreaCheckListNoteActivity.this)
                            .getWritableDatabase(AppConstants.db_password);
                    if (db != null) {
                        finalNote = databaseHelper.getNote(db, preferencesKeeper.getGroupId(), tagName,
                                preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId());
                        if (!TextUtils.isEmpty(finalNote)) {
                            //UPDATE
                            db = DatabaseHelper.getInstance(AreaCheckListNoteActivity.this)
                                    .getWritableDatabase(AppConstants.db_password);
                            if (db != null) {
                                Boolean tag;
                                if (AppConstants.qrTag) {
                                    tag = databaseHelper.updateNote(db
                                            , preferencesKeeper.getGroupId()
                                            , tagName
                                            , note
                                            , preferencesKeeper.getEmail()
                                            , preferencesKeeper.getCustomerId(),
                                            Utils.currentDateTime(), "1");
                                } else {
                                    tag = databaseHelper.updateNote(db
                                            , preferencesKeeper.getGroupId()
                                            , tagName
                                            , note
                                            , preferencesKeeper.getEmail()
                                            , preferencesKeeper.getCustomerId(),
                                            Utils.currentDateTime(), "0");
                                }


                                if (tag) {
                                    FancyToast.makeText(AreaCheckListNoteActivity.this
                                            , "Notes updated successfully."
                                            , 2000, FancyToast.SUCCESS, false).show();
                                    finish();
                                } else {
                                    FancyToast.makeText(AreaCheckListNoteActivity.this
                                            , "Notes were not updated successfully."
                                            , 2000, FancyToast.ERROR, false).show();
                                }
                            }
                        } else {
                            //ADD
                            db = DatabaseHelper.getInstance(AreaCheckListNoteActivity.this)
                                    .getWritableDatabase(AppConstants.db_password);
                            if (db != null) {
                                Boolean tag;
                                if (AppConstants.qrTag) {
                                    tag = databaseHelper.createNote(db
                                            , preferencesKeeper.getGroupId()
                                            , tagName
                                            , note
                                            , preferencesKeeper.getEmail()
                                            , preferencesKeeper.getCustomerId(),
                                            Utils.currentDateTime(), "1");
                                } else {
                                    tag = databaseHelper.createNote(db
                                            , preferencesKeeper.getGroupId()
                                            , tagName
                                            , note
                                            , preferencesKeeper.getEmail()
                                            , preferencesKeeper.getCustomerId(),
                                            Utils.currentDateTime(), "0");
                                }
                                if (tag) {
                                    FancyToast.makeText(AreaCheckListNoteActivity.this
                                            , "Notes saved successfully."
                                            , 2000, FancyToast.SUCCESS, false).show();
                                    finish();
                                } else {
                                    FancyToast.makeText(AreaCheckListNoteActivity.this
                                            , "Notes were not saved successfully."
                                            , 2000, FancyToast.ERROR, false).show();
                                }
                            }
                        }
                    }
                }

            }
        });

        getNote();
    }

    public void getNote() {
        SQLiteDatabase db = DatabaseHelper.getInstance(AreaCheckListNoteActivity.this)
                .getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            finalNote = databaseHelper.getNote(db, preferencesKeeper.getGroupId(), tagName,
                    preferencesKeeper.getEmail(), preferencesKeeper.getCustomerId());
            if (!TextUtils.isEmpty(finalNote)) {
                note_area.setText(finalNote);
            } else {
                note_area.setHint("[Tap to Enter a Note]");
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
