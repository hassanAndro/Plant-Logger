package com.plantlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plantlogger.ApiCalls.APIClient;
import com.plantlogger.ApiCalls.APIInterface;
import com.plantlogger.ApiResponse.FetchUsers.UsersDatum;
import com.plantlogger.ApiResponse.FetchUsers.UsersResponse;
import com.plantlogger.ApiResponse.LoginResponse;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.companyId)
    EditText companyId;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.signup)
    RelativeLayout signup;

    APIInterface apiInterface;
    PreferencesKeeper preferencesKeeper;
    DatabaseHelper databaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesKeeper = new PreferencesKeeper(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setListeners();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setListeners() {
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        signup.setOnClickListener(this);

        InitializeSQLCipher();

//        companyId.setText("AT Power");
//        email.setText("arbab@atpower.com");
//        password.setText("arbab");

//        companyId.setText("GEC Power");
//        email.setText("sarwer@gecpower.com");
//        password.setText("sarwer");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (!TextUtils.isEmpty(companyId.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(email.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(password.getText().toString().trim())) {
                            if (Utils.isValidEmail(email.getText().toString().trim())) {
                                //Find in db
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showProgress("Logging In...");
                                    }
                                });
                                getUser(companyId.getText().toString().trim(), email.getText().toString().trim(),
                                        password.getText().toString().trim());

                            } else {
                                showAlert("", "Please enter a valid email address.");
                            }
                        } else {
                            showAlert("", "Password is required.");
                        }
                    } else {
                        showAlert("", "Email is required.");
                    }
                } else {
                    showAlert("", "Company Id is required.");
                }
                break;
            case R.id.forget:
                //code..
                break;
            case R.id.signup:
                //code..
                break;
        }
    }

    private void InitializeSQLCipher() {
        SQLiteDatabase.loadLibs(this);
    }

    public void getUser(String companyId, String email, String password) {

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (databaseHelper.usersCount(db)) {
                db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                UsersDatum user_main = databaseHelper.getUser(db, companyId);
                if (user_main != null) {
                    db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
                    String final_password = Utils.md5(Utils.md5(user_main.getSalt()) + Utils.md5(password));
                    UsersDatum user = databaseHelper.getUserByPasswrod(db, companyId, "[" + email + "]", final_password);
                    if (user != null) {
                        Log.e("Login", "matched in db ");
                        if (!TextUtils.isEmpty(user.getName())) {
                            preferencesKeeper.setName(user.getUsername());
                        }
                        if (!TextUtils.isEmpty(user.getEmail())) {
                            preferencesKeeper.setEmail(user.getEmail());
                        }
                        if (!TextUtils.isEmpty(user.getAdminId())) {
                            preferencesKeeper.setID(user.getAdminId());
                        }


                        if (user.getCompany_id().equals("0")) {
                            if (!TextUtils.isEmpty(user.getAdminId())) {
                                preferencesKeeper.setCustomerId(user.getAdminId());
                            }
                        } else {
                            if (!TextUtils.isEmpty(user.getCompany_id())) {
                                preferencesKeeper.setCustomerId(user.getCompany_id());
                            }
                        }
                        preferencesKeeper.setLoginStatus(true);
                        FancyToast.makeText(this, "Login Successful", 2000, FancyToast.SUCCESS, false).show();
                        Intent intent = new Intent(LoginActivity.this, HomeAreasActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissProgress();
                            }
                        });
                        if (Utils.wifiCheck(this)) {
                            login(email, password);
                        } else {
                            showAlert("", "Email or Password did not match. Please connect to internet to login from cloud.");
                        }
                    }
                } else {
                    if (Utils.wifiCheck(this)) {
                        login(email, password);
                    } else {
                        showAlert("", "Email or Password did not match. Please connect to internet to login from cloud.");
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgress();
                    }
                });
            } else {
                if (Utils.wifiCheck(this)) {
                    login(email, password);
                } else {
                    showAlert("", "Email or Password did not match. Please connect to internet to login from cloud.");
                }
            }
        }
    }

    public void login(String email, String password) {

        showProgress("Logging In...");
        Call call = apiInterface.login(email, password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<LoginResponse> loginResponses = (ArrayList<LoginResponse>) response.body();
                Log.e("Login", "response: " + loginResponses.size());
                if (loginResponses != null) {
                    if (loginResponses.size() > 0) {

                        if (loginResponses.get(0).getCode().equals("200")) {
                            users(loginResponses.get(0).getUsername(), loginResponses.get(0).getEmail(), loginResponses.get(0).getCustomerId());
                        } else {
                            dismissProgress();
                            if (!TextUtils.isEmpty(loginResponses.get(0).getMessage())) {
                                showAlert("Error", loginResponses.get(0).getMessage());
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

            @Override
            public void onFailure(Call call, Throwable t) {
                dismissProgress();
                Log.e("Login", "response onFailure: " + t.getMessage());
                call.cancel();
            }
        });
    }

    public void users(final String name, final String email, final String companyId) {

        Call call = apiInterface.users();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        UsersResponse usersResponse = (UsersResponse) response.body();
                        if (usersResponse != null) {
                            if (usersResponse.getStatus() == 200) {
                                Log.e("Users", "Users Result: " + usersResponse.getData());
                                saveUsers(usersResponse.getData(), name, email, companyId);
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

    public void saveUsers(ArrayList<UsersDatum> users, String name, String email, String companyId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase(AppConstants.db_password);
        if (db != null) {
            if (users != null) {
                databaseHelper.createUsers(db, users);
                if (!TextUtils.isEmpty(name)) {
                    preferencesKeeper.setName(name);
                }
                if (!TextUtils.isEmpty(email)) {
                    preferencesKeeper.setEmail(email);
                }
                if (!TextUtils.isEmpty(companyId)) {
                    preferencesKeeper.setID(companyId);
                }


                for (int i = 0; i < users.size(); i++) {
                    UsersDatum datum = users.get(i);
                    Log.e("asddax", " " + datum.getCompany_id());
                    Log.e("asddax", " " + datum.getAdminId());
                    Log.e("asddax", " " + datum.getUsername());
                    Log.e("asddax", " " + datum.getEmail());
                    Log.e("asddax", "''''''''''''''''''''''''''''''''''''''''''");
                    if (datum.getUsername().equals(name) && datum.getEmail().equals(email)) {
                        if (datum.getCompany_id().equals("0")) {
                            preferencesKeeper.setCustomerId(datum.getAdminId());
                        } else {
                            preferencesKeeper.setCustomerId(datum.getCompany_id());
                        }
                        break;
                    }
                }

            }
            preferencesKeeper.setLoginStatus(true);
            FancyToast.makeText(LoginActivity.this, "Login Successful", 2000, FancyToast.SUCCESS, false).show();
            Intent intent = new Intent(LoginActivity.this, HomeAreasActivity.class);
            startActivity(intent);
            finish();

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
