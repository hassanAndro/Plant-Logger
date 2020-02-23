package com.plantlogger.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.plantlogger.ApiCalls.APIClient;
import com.plantlogger.ApiCalls.APIInterface;
import com.plantlogger.ApiResponse.ChangePassResponse;
import com.plantlogger.ApiResponse.FetchUsers.UsersDatum;
import com.plantlogger.ApiResponse.FetchUsers.UsersResponse;
import com.plantlogger.BaseActivity;
import com.plantlogger.R;
import com.plantlogger.database.DatabaseHelper;
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

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.oldPassword)
    EditText oldPassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.back)
    RelativeLayout back;

    APIInterface apiInterface;
    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        preferencesKeeper = new PreferencesKeeper(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(newPassword.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {

                            if (Utils.wifiCheck(ChangePasswordActivity.this)) {
                                String email_pref = preferencesKeeper.getEmail();
                                String removedBraces = email_pref.replaceAll("\\[", "").replaceAll("\\]", "");
                                changePassword(
                                        removedBraces,
                                        oldPassword.getText().toString().trim(),
                                        newPassword.getText().toString().trim(),
                                        confirmPassword.getText().toString().trim());
                            } else {
                                showAlert("", "No internet access.");
                            }


                        } else {
                            showAlert("", "Old Password is required.");
                        }
                    } else {
                        showAlert("", "New Password is required.");
                    }
                } else {
                    showAlert("", "Confirm Password is required.");
                }
            }
        });
    }

    public void changePassword(final String email, final String oldPass, final String newPass, final String confirmPass) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress("Changing Password...");
            }
        });

        Call call = apiInterface.changePass(email, oldPass, newPass, confirmPass);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        ChangePassResponse usersResponse = (ChangePassResponse) response.body();
                        if (usersResponse != null) {
                            if (usersResponse.getStatus() == 200) {
                                Log.e("Users", "response: " + usersResponse.getMessage());
                                fetchUsersCall();
                            } else {
                                if (!TextUtils.isEmpty(usersResponse.getMessage())) {
                                    showAlert("Error", usersResponse.getMessage());
                                } else {
                                    showAlert("Error", "Response was unsuccessful.");
                                }
                            }
                        }

                    } else {
                        showAlert("Failure", "Response was unsuccessful.");
                    }
                }
                dismissProgress();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                dismissProgress();
                showAlert("Failure", t.getMessage());
                Log.e("Users", "response onFailure: " + t.getMessage());
                call.cancel();
            }
        });
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
                                saveUsers(usersResponse.getData());
                                FancyToast.makeText(ChangePasswordActivity.this,
                                        "Password Changed Successfully.", 2000, FancyToast.SUCCESS, false).show();

                            } else {
                                if (!TextUtils.isEmpty(usersResponse.getMessage())) {
                                    showAlert("Error", usersResponse.getMessage());
                                } else {
                                    showAlert("Error", "Response was unsuccessful.");
                                }
                            }
                        }

                    } else {
                        showAlert("Failure", "Response was unsuccessful.");
                    }
                }
                dismissProgress();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                dismissProgress();
                showAlert("Failure", t.getMessage());
                Log.e("Users", "Users response onFailure: " + t.getMessage());
                call.cancel();
            }
        });
    }

    public void saveUsers(ArrayList<UsersDatum> users) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (users != null) {
                databaseHelper.createUsers(db, users);
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
