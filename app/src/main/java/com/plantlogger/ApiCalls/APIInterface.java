package com.plantlogger.ApiCalls;

import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListResponse;
import com.plantlogger.ApiResponse.ChangePassResponse;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusResponse;
import com.plantlogger.ApiResponse.FetchArea.AreasResponse;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemsResponse;
import com.plantlogger.ApiResponse.LoginResponse;
import com.plantlogger.ApiResponse.FetchUsers.UsersResponse;
import com.plantlogger.ApiResponse.SelectionSets.SelectionSetsResponse;
import com.plantlogger.ApiResponse.SyncResponse;
import com.plantlogger.ApiResponse.Units.UnitsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hassan_Hameed on 8/18/2017.
 */

public interface APIInterface {

    @GET("mobile_login.php")
    Call<ArrayList<LoginResponse>> login(@Query("email") String email, @Query("password") String password);

    @GET("companies.php")
    Call<UsersResponse> users();

    @GET("change_password.php")
    Call<ChangePassResponse> changePass(
            @Query("email") String email,
            @Query("oldPass") String oldPass,
            @Query("newPass") String newPass,
            @Query("confirmPass") String confirmPass
    );

    @GET("units.php")
    Call<UnitsResponse> units(@Query("email") String email);

    @GET("selection_sets.php")
    Call<SelectionSetsResponse> selection_sets(@Query("email") String email);

    @GET("area_check_list.php")
    Call<AreaCheckListResponse> area_check_list(@Query("email") String email);

    @GET("equipment_status.php")
    Call<EquipmentStatusResponse> equipment_status(@Query("email") String email);

    @GET("fetch_area.php")
    Call<AreasResponse> areas(@Query("email") String email);

    @GET("area-items.php")
    Call<AreaItemsResponse> areas_items(@Query("email") String email);

    @FormUrlEncoded()
    @POST("getdata.php")
    Call<SyncResponse> sendSyncData(@Query("email") String email, @Field("data") String data);

}
