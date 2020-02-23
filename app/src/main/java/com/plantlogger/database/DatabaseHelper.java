package com.plantlogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListDatum;
import com.plantlogger.ApiResponse.EquipmentStatus.EquipmentStatusDatum;
import com.plantlogger.ApiResponse.FetchArea.AreasDatum;
import com.plantlogger.ApiResponse.FetchAreaItems.AreaItemDatum;
import com.plantlogger.ApiResponse.FetchUsers.UsersDatum;
import com.plantlogger.ApiResponse.SelectionSets.SelectionSetsDatum;
import com.plantlogger.ApiResponse.Units.Datum;
import com.plantlogger.models.DBSyncModel.AreaCheckListDBSyncModel;
import com.plantlogger.models.DBSyncModel.EquipmentStatusDBSyncModel;
import com.plantlogger.models.DBSyncModel.NotesDBSyncModel;
import com.plantlogger.models.DBSyncModel.ParamterDataEntryDBSyncModel;
import com.plantlogger.models.SpinnerItemModel;
import com.plantlogger.models.SyncModels.AreaCheckListSyncModel;
import com.plantlogger.models.SyncModels.EquipmentStatusSyncModel;
import com.plantlogger.models.SyncModels.ParameterDataEntrySyncModel;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "plantLogger";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_UNITS = "units";
    private static final String TABLE_AREA_CHECK_LIST = "area_check_list";
    private static final String TABLE_EQUIPMENT_STATUS = "equipment_status";
    private static final String TABLE_SELECTION_SETS = "selection_sets";
    private static final String TABLE_AREAS = "areas";
    private static final String TABLE_AREAS_ITEMS = "areas_items";
    private static final String TABLE_Parameter_Sync = "paramater_sync";
    private static final String TABLE_EquipmentStatus_Sync = "equipmentstatus_sync";
    private static final String TABLE_AreaCheckList_Sync = "areachecklist_sync";
    private static final String TABLE_Notes_Sync = "notes_sync";

    //TABLE_SELECTION_SETS
    private static final String id_ss = "id";
    private static final String sets_idx_ss = "sets_idx";
    private static final String sets_value_ss = "sets_value";
    private static final String multiple_ss = "multiple";


    // areachecklist_sync column names
    private static final String row_id_ns = "row_id";
    private static final String groupd_id_ns = "groupd_id";
    private static final String name_ns = "name";
    private static final String note_ns = "note";
    private static final String adminId_ns = "adminId";
    private static final String date_time_ns = "date_time";
    private static final String email_ns = "email";
    private static final String is_qr_ns = "isQR";

    // paramater_sync column names
    private static final String row_id_ps = "row_id";
    private static final String area_item_id_ps = "area_item_id";
    private static final String name_ps = "name";
    private static final String unit_id_ps = "unit_id";
    private static final String set_id_ps = "set_id";
    private static final String groupd_id_ps = "groupd_id";
    private static final String optimal_range_ps = "optimal_range";
    private static final String absolute_range_ps = "absolute_range";
    private static final String unit_ps = "unit";
    private static final String email_ps = "email";
    private static final String adminId_ps = "adminId";
    private static final String isShown_ps = "isShown";
    private static final String isSynced_ps = "isSynced";
    private static final String user_name_ps = "user_name";
    private static final String date_time_ps = "date_time";
    private static final String is_qr_ps = "isQR";
    private static final String is_checked_ps = "isChecked";

    // equipmentstatus_sync column names
    private static final String row_id_ess = "row_id";
    private static final String equipment_id_ess = "equipment_id";
    private static final String sets_idx_ess = "sets_idx";
    private static final String sets_value_ess = "sets_value";
    private static final String groupd_id_ess = "groupd_id";
    private static final String email_id_ess = "email";
    private static final String adminId_id_ess = "admin_id";
    private static final String dateTime_ess = "date_time";
    private static final String isShown_ess = "isShown";
    private static final String is_qr_ess = "isQR";
    private static final String ID_ess = "ID";
    private static final String name_ess = "name";

    // areachecklist_sync column names
    private static final String row_id_acls = "row_id";
    private static final String checklist_id_acls = "checklist_id";
    private static final String sets_idx_acls = "sets_idx";
    private static final String sets_value_acls = "sets_value";
    private static final String groupd_id_acls = "groupd_id";
    private static final String email_id_acls = "email";
    private static final String adminId_id_acls = "admin_id";
    private static final String dateTime_acls = "date_time";
    private static final String isShown_acls = "isShown";
    private static final String is_qr_acls = "isQR";
    private static final String ID_acls = "ID";
    private static final String name_acls = "name";


    // USERS column names
    private static final String adminId = "adminId";
    private static final String name = "name";
    private static final String username = "username";
    private static final String password = "password";
    private static final String salt = "salt";
    private static final String email = "email";
    private static final String noLogins = "noLogins";
    private static final String lastTime = "lastTime";
    private static final String company_id = "company_id";

    // Areas column names
    private static final String cat_id = "cat_id";
    private static final String cat_name = "cat_name";
    private static final String cat_desc = "cat_desc";
    private static final String cat_father_id = "cat_father_id";
    private static final String adminId_area = "adminId";
    private static final String emailId_areas = "email";
    private static final String barcode_areas = "barcode";
    private static final String qr_string_areas = "qr_string";

    // Units column names
    private static final String unit_id_u = "id";
    private static final String adminId_u = "adminId";
    private static final String name_u = "name";

    // Area_Check_List column names
    private static final String id_acl = "id";
    private static final String sets_idx_acl = "sets_idx";
    private static final String sets_value_acl = "sets_value";
    private static final String multiple_acl = "multiple";


    // Equipment_Status column names
    private static final String id_es = "id";
    private static final String sets_idx_es = "sets_idx";
    private static final String sets_value_es = "sets_value";
    private static final String multiple_es = "multiple";

    // Areas_Items column names
    private static final String id_ai = "id";
    private static final String name_ai = "name";
    private static final String set_id_ai = "set_id";
    private static final String unit_id_ai = "unit_id";
    private static final String absolute_range_flag_ai = "absolute_range_flag";
    private static final String a_min_ai = "a_min";
    private static final String a_max_ai = "a_max";
    private static final String optimal_range_ai = "optimal_range";
    private static final String o_min_ai = "o_min";
    private static final String o_max_ai = "o_max";
    private static final String force_note_ai = "force_note";
    private static final String activites_ai = "activites";
    private static final String description_ai = "description";
    private static final String admin_id_ai = "admin_id";
    private static final String groupd_id_ai = "groupd_id";
    private static final String checklist_idi = "checklist_id";
    private static final String equipment_idi = "equipment_id";
    private static final String emailId_areas_ai = "email";
    private static final String isFilled_ai = "isFilled";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS
            + "("
            + adminId + " TEXT PRIMARY KEY,"
            + name + " TEXT,"
            + username + " TEXT,"
            + password + " TEXT,"
            + salt + " TEXT,"
            + email + " TEXT,"
            + noLogins + " TEXT,"
            + lastTime + " TEXT,"
            + company_id + " TEXT"
            + ")";

    private static final String CREATE_TABLE_UNITS = "CREATE TABLE " + TABLE_UNITS
            + "("
            + unit_id_u + " TEXT PRIMARY KEY,"
            + adminId_u + " TEXT,"
            + name_u + " TEXT"
            + ")";

    private static final String CREATE_TABLE_AREA_CHECK_LIST = "CREATE TABLE " + TABLE_AREA_CHECK_LIST
            + "("
            + id_acl + " TEXT PRIMARY KEY,"
            + sets_idx_acl + " TEXT,"
            + sets_value_acl + " TEXT,"
            + multiple_acl + " TEXT"
            + ")";

    private static final String CREATE_TABLE_SELECTION_SET = "CREATE TABLE " + TABLE_SELECTION_SETS
            + "("
            + id_ss + " TEXT PRIMARY KEY,"
            + sets_idx_ss + " TEXT,"
            + sets_value_ss + " TEXT,"
            + multiple_ss + " TEXT"
            + ")";

    private static final String CREATE_TABLE_EQUIPMENT_STATUS = "CREATE TABLE " + TABLE_EQUIPMENT_STATUS
            + "("
            + id_es + " TEXT PRIMARY KEY,"
            + sets_idx_es + " TEXT,"
            + sets_value_es + " TEXT,"
            + multiple_es + " TEXT"
            + ")";

    private static final String CREATE_TABLE_AREAS = "CREATE TABLE " + TABLE_AREAS
            + "("
            + cat_id + " TEXT PRIMARY KEY,"
            + cat_name + " TEXT,"
            + cat_desc + " TEXT,"
            + cat_father_id + " TEXT,"
            + adminId_area + " TEXT,"
            + barcode_areas + " TEXT,"
            + qr_string_areas + " TEXT,"
            + emailId_areas + " TEXT"
            + ")";

    private static final String CREATE_TABLE_AREAS_ITEMS = "CREATE TABLE " + TABLE_AREAS_ITEMS
            + "("
            + id_ai + " TEXT PRIMARY KEY,"
            + name_ai + " TEXT,"
            + set_id_ai + " TEXT,"
            + unit_id_ai + " TEXT,"
            + absolute_range_flag_ai + " TEXT,"
            + a_min_ai + " TEXT,"
            + a_max_ai + " TEXT,"
            + optimal_range_ai + " TEXT,"
            + o_min_ai + " TEXT,"
            + o_max_ai + " TEXT,"
            + force_note_ai + " TEXT,"
            + activites_ai + " TEXT,"
            + description_ai + " TEXT,"
            + admin_id_ai + " TEXT,"
            + groupd_id_ai + " TEXT,"
            + checklist_idi + " TEXT,"
            + equipment_idi + " TEXT,"
            + isFilled_ai + " TEXT,"
            + emailId_areas_ai + " TEXT"
            + ")";

    private static final String CREATE_TABLE_Parameter_Sync = "CREATE TABLE " + TABLE_Parameter_Sync
            + "("
            + row_id_ps + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + area_item_id_ps + " TEXT,"
            + name_ps + " TEXT,"
            + is_qr_ps + " TEXT,"
            + is_checked_ps + " TEXT,"
            + unit_id_ps + " TEXT,"
            + set_id_ps + " TEXT,"
            + groupd_id_ps + " TEXT,"
            + optimal_range_ps + " TEXT,"
            + absolute_range_ps + " TEXT,"
            + unit_ps + " TEXT,"
            + email_ps + " TEXT,"
            + adminId_ps + " TEXT,"
            + user_name_ps + " TEXT,"
            + date_time_ps + " TEXT,"
            + isShown_ps + " TEXT DEFAULT 0,"
            + isSynced_ps + " TEXT DEFAULT 0"
            + ")";

    private static final String CREATE_TABLE_EquipmentStatus_Sync = "CREATE TABLE " + TABLE_EquipmentStatus_Sync
            + "("
            + row_id_ess + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + equipment_id_ess + " TEXT,"
            + groupd_id_ess + " TEXT,"
            + is_qr_ess + " TEXT,"
            + sets_idx_ess + " TEXT,"
            + sets_value_ess + " TEXT,"
            + email_id_ess + " TEXT,"
            + adminId_id_ess + " TEXT,"
            + dateTime_ess + " TEXT,"
            + ID_ess + " TEXT,"
            + name_ess + " TEXT,"
            + isShown_ess + " TEXT"
            + ")";

    private static final String CREATE_TABLE_AreaCheckList_Sync = "CREATE TABLE " + TABLE_AreaCheckList_Sync
            + "("
            + row_id_acls + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + checklist_id_acls + " TEXT,"
            + is_qr_acls + " TEXT,"
            + groupd_id_acls + " TEXT,"
            + sets_idx_acls + " TEXT,"
            + sets_value_acls + " TEXT,"
            + email_id_acls + " TEXT,"
            + adminId_id_acls + " TEXT,"
            + dateTime_acls + " TEXT,"
            + ID_acls + " TEXT,"
            + name_acls + " TEXT,"
            + isShown_acls + " TEXT"
            + ")";

    private static final String CREATE_TABLE_Notes_Sync = "CREATE TABLE " + TABLE_Notes_Sync
            + "("
            + row_id_ns + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + groupd_id_ns + " TEXT,"
            + name_ns + " TEXT,"
            + is_qr_ns + " TEXT,"
            + adminId_ns + " TEXT,"
            + date_time_ns + " TEXT,"
            + email_ns + " TEXT,"
            + note_ns + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static public synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_AREAS);
        db.execSQL(CREATE_TABLE_UNITS);
        db.execSQL(CREATE_TABLE_AREA_CHECK_LIST);
        db.execSQL(CREATE_TABLE_SELECTION_SET);
        db.execSQL(CREATE_TABLE_EQUIPMENT_STATUS);
        db.execSQL(CREATE_TABLE_AREAS_ITEMS);
        db.execSQL(CREATE_TABLE_Parameter_Sync);
        db.execSQL(CREATE_TABLE_EquipmentStatus_Sync);
        db.execSQL(CREATE_TABLE_AreaCheckList_Sync);
        db.execSQL(CREATE_TABLE_Notes_Sync);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS_ITEMS);
        // create new tables
        onCreate(db);
    }

    /*
     * Creating a USERS
     */
    public void createUsers(SQLiteDatabase database, ArrayList<UsersDatum> users) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            database.execSQL(CREATE_TABLE_USERS);

            for (UsersDatum userObject : users) {

                ContentValues values = new ContentValues();
                values.put(adminId, userObject.getAdminId());
                values.put(name, userObject.getName());
                values.put(username, userObject.getUsername());
                values.put(password, userObject.getPassword());
                values.put(email, "[" + userObject.getEmail() + "]");
                values.put(salt, userObject.getSalt());
                values.put(noLogins, userObject.getNoLogins());
                values.put(lastTime, userObject.getLastTime());
                values.put(company_id, userObject.getCompany_id());

                // insert row
                database.insert(TABLE_USERS, null, values);
            }
            Log.e(LOG, "Users Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "Users SQLiteException: " + e);
        } finally {
            database.close();
        }
    }


    public Boolean usersCount(SQLiteDatabase db) {
        Boolean tag = false;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS;
            c = db.rawQuery(selectQuery, null);
            if (c != null) {
                tag = c.getCount() > 0;
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "Count SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return tag;
    }

    /*
     * get single User
     */
    public UsersDatum getUser(SQLiteDatabase db, String user_company_id) {
        UsersDatum user = null;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + name + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{user_company_id});


            if (c != null) {
                if (c.moveToFirst()) {
                    user = new UsersDatum();
                    user.setAdminId(c.getString(c.getColumnIndex(adminId)));
                    user.setName(c.getString(c.getColumnIndex(name)));
                    user.setUsername(c.getString(c.getColumnIndex(username)));
                    user.setEmail(c.getString(c.getColumnIndex(email)));
                    user.setSalt(c.getString(c.getColumnIndex(salt)));
                    user.setNoLogins(c.getString(c.getColumnIndex(noLogins)));
                    user.setLastTime(c.getString(c.getColumnIndex(lastTime)));
                    user.setPassword(c.getString(c.getColumnIndex(password)));
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "Users SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return user;
    }

    /*
     * get single User
     */
    public UsersDatum getUserByPasswrod(SQLiteDatabase db, String user_company_id, String user_email, String user_password) {
        UsersDatum user = null;
        Cursor c = null;
//        String pass = Utils.decryptIt(user_password);
        String pass = user_password;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + name + " = ? AND " + email + " = ? AND " + password + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{user_company_id, user_email, pass});


            if (c != null) {
                if (c.moveToFirst()) {
                    user = new UsersDatum();
                    user.setAdminId(c.getString(c.getColumnIndex(adminId)));
                    user.setName(c.getString(c.getColumnIndex(name)));
                    user.setUsername(c.getString(c.getColumnIndex(username)));
                    user.setEmail(c.getString(c.getColumnIndex(email)));
                    user.setSalt(c.getString(c.getColumnIndex(salt)));
                    user.setNoLogins(c.getString(c.getColumnIndex(noLogins)));
                    user.setLastTime(c.getString(c.getColumnIndex(lastTime)));
                    user.setCompany_id(c.getString(c.getColumnIndex(company_id)));
                    user.setPassword(c.getString(c.getColumnIndex(password)));
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "Users SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return user;
    }


    /*
     * Creating a Areas
     */
    public void createAreas(SQLiteDatabase database, ArrayList<AreasDatum> areas, String emailId) {

        try {

            database.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS);
            database.execSQL(CREATE_TABLE_AREAS);

            for (AreasDatum areaObject : areas) {

                ContentValues values = new ContentValues();
                values.put(cat_id, areaObject.getCatId());
                values.put(cat_name, areaObject.getCatName());
                values.put(cat_desc, areaObject.getCatDesc());
                values.put(cat_father_id, areaObject.getCatFatherId());
                values.put(adminId_area, areaObject.getAdminId());
                values.put(barcode_areas, areaObject.getBarcode());
                values.put(qr_string_areas, areaObject.getQr_string());
                values.put(emailId_areas, emailId);


                // insert row
                database.insert(TABLE_AREAS, null, values);
            }
            Log.e(LOG, "Areas Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "createAreas SQLiteException: " + e);
        } finally {
            database.close();
        }
    }


    public Boolean areasCount(SQLiteDatabase db) {
        Boolean tag = false;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREAS;
            c = db.rawQuery(selectQuery, null);
            if (c != null) {
                tag = c.getCount() > 0;
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "areasCount SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return tag;
    }

    /*
     * get area User
     */
    public ArrayList<AreasDatum> getAreas(SQLiteDatabase db, String emailId, String father_id, String companyId) {

        Log.e("aksdjk", "email : " + emailId);
        Log.e("aksdjk", "father_id : " + father_id);
        Log.e("aksdjk", "admin : " + companyId);
        ArrayList<AreasDatum> areasDatumArrayList = new ArrayList<AreasDatum>();
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREAS + " WHERE "
                    + emailId_areas + " = ? "
                    + "AND "
                    + cat_father_id + " = ? "
                    + "AND "
                    + adminId_area + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{emailId, father_id, companyId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        AreasDatum area = new AreasDatum();
                        area.setCatId(c.getString(c.getColumnIndex(cat_id)));
                        area.setCatDesc(c.getString(c.getColumnIndex(cat_desc)));
                        area.setCatFatherId(c.getString(c.getColumnIndex(cat_father_id)));
                        area.setCatName(c.getString(c.getColumnIndex(cat_name)));
                        area.setAdminId(c.getString(c.getColumnIndex(adminId)));
                        area.setBarcode(c.getString(c.getColumnIndex(barcode_areas)));
                        area.setQr_string(c.getString(c.getColumnIndex(qr_string_areas)));

                        areasDatumArrayList.add(area);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreas SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return areasDatumArrayList;
    }

    public AreasDatum getAreasFromQR(SQLiteDatabase db, String emailId, String qr_code, String companyId) {
        AreasDatum areasDatumArrayList = null;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREAS + " WHERE "
                    + emailId_areas + " = ? "
                    + "AND "
                    + qr_string_areas + " = ? "
                    + "AND "
                    + adminId_area + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{emailId, qr_code, companyId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        AreasDatum area = new AreasDatum();
                        area.setCatId(c.getString(c.getColumnIndex(cat_id)));
                        area.setCatDesc(c.getString(c.getColumnIndex(cat_desc)));
                        area.setCatFatherId(c.getString(c.getColumnIndex(cat_father_id)));
                        area.setCatName(c.getString(c.getColumnIndex(cat_name)));
                        area.setAdminId(c.getString(c.getColumnIndex(adminId)));
                        area.setBarcode(c.getString(c.getColumnIndex(barcode_areas)));
                        area.setQr_string(c.getString(c.getColumnIndex(qr_string_areas)));

                        areasDatumArrayList = area;


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreas SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return areasDatumArrayList;
    }

    /*
     * Creating a Areas_Items
     */
    public void createAreas_Items(SQLiteDatabase database, ArrayList<AreaItemDatum> areas_items, String emailId) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS_ITEMS);
            database.execSQL(CREATE_TABLE_AREAS_ITEMS);


            for (AreaItemDatum areaObject : areas_items) {

                ContentValues values = new ContentValues();
                values.put(id_ai, areaObject.getId());
                values.put(name_ai, areaObject.getName());
                values.put(set_id_ai, areaObject.getSetId());
                values.put(unit_id_ai, areaObject.getUnitId());
                values.put(absolute_range_flag_ai, areaObject.getAbsoluteRangeFlag());
                values.put(a_min_ai, areaObject.getAMin());
                values.put(a_max_ai, areaObject.getAMax());
                values.put(optimal_range_ai, areaObject.getOptimalRange());
                values.put(o_min_ai, areaObject.getOMin());
                values.put(o_max_ai, areaObject.getOMax());
                values.put(force_note_ai, areaObject.getForceNote());
                values.put(activites_ai, areaObject.getActivites());
                values.put(description_ai, areaObject.getDescription());
                values.put(admin_id_ai, areaObject.getAdminId());
                values.put(groupd_id_ai, areaObject.getGroupdId());
                values.put(checklist_idi, areaObject.getChecklist_id());
                values.put(equipment_idi, areaObject.getEquipment_id());
                values.put(emailId_areas_ai, emailId);
                values.put(isFilled_ai, "0");


                // insert row
                database.insert(TABLE_AREAS_ITEMS, null, values);
            }
            Log.e(LOG, "Areas_Items Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "createAreas_Items SQLiteException: " + e);
        } finally {
            database.close();
        }
    }


    public Boolean areas_itemsCount(SQLiteDatabase db) {
        Boolean tag = false;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS;
            c = db.rawQuery(selectQuery, null);
            if (c != null) {
                tag = c.getCount() > 0;
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "areas_itemsCount SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return tag;
    }

    /*
     * get area User
     */
    public ArrayList<AreaItemDatum> getAreas_Items(SQLiteDatabase db, String emailId, String groupd_id, String companyId, String path) {
        Log.e(LOG, "getAreas_Items groupd_id: " + groupd_id);
        ArrayList<AreaItemDatum> areasDatumArrayList = new ArrayList<AreaItemDatum>();
        Cursor c = null;
        try {
            if (path.equals("param")) {

                String selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                        + " WHERE "
                        + emailId_areas_ai + " = ? "
                        + "AND "
                        + groupd_id_ai + " = ? "
                        + "AND "
                        + admin_id_ai + " = ? "
                        + "AND "
                        + checklist_idi + " = ? "
                        + "AND "
                        + equipment_idi + " = ? ";
                c = db.rawQuery(selectQuery, new String[]{emailId, groupd_id, companyId, "0", "0"});
            } else if (path.equals("check")) {
                String selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                        + " WHERE "
                        + emailId_areas_ai + " = ? "
                        + "AND "
                        + groupd_id_ai + " = ? "
                        + "AND "
                        + admin_id_ai + " = ? "
                        + "AND "
                        + checklist_idi + " > ? ";
                c = db.rawQuery(selectQuery, new String[]{emailId, groupd_id, companyId, "0"});
            } else if (path.equals("equipment")) {
                String selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                        + " WHERE "
                        + emailId_areas_ai + " = ? "
                        + "AND "
                        + groupd_id_ai + " = ? "
                        + "AND "
                        + admin_id_ai + " = ? "
                        + "AND "
                        + equipment_idi + " > ? ";
                c = db.rawQuery(selectQuery, new String[]{emailId, groupd_id, companyId, "0"});
            }


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        AreaItemDatum area = new AreaItemDatum();
                        area.setId(c.getString(c.getColumnIndex(id_ai)));
                        area.setName(c.getString(c.getColumnIndex(name_ai)));
                        area.setSetId(c.getString(c.getColumnIndex(set_id_ai)));
                        area.setUnitId(c.getString(c.getColumnIndex(unit_id_ai)));
                        area.setAbsoluteRangeFlag(c.getString(c.getColumnIndex(absolute_range_flag_ai)));
                        area.setAMin(c.getString(c.getColumnIndex(a_min_ai)));
                        area.setAMax(c.getString(c.getColumnIndex(a_max_ai)));
                        area.setOptimalRange(c.getString(c.getColumnIndex(optimal_range_ai)));
                        area.setOMin(c.getString(c.getColumnIndex(o_min_ai)));
                        area.setOMax(c.getString(c.getColumnIndex(o_max_ai)));
                        area.setForceNote(c.getString(c.getColumnIndex(force_note_ai)));
                        area.setActivites(c.getString(c.getColumnIndex(activites_ai)));
                        area.setDescription(c.getString(c.getColumnIndex(description_ai)));
                        area.setAdminId(c.getString(c.getColumnIndex(admin_id_ai)));
                        area.setGroupdId(c.getString(c.getColumnIndex(groupd_id_ai)));
                        area.setChecklist_id(c.getString(c.getColumnIndex(checklist_idi)));
                        area.setEquipment_id(c.getString(c.getColumnIndex(equipment_idi)));
                        area.setIsFilled(c.getString(c.getColumnIndex(isFilled_ai)));

                        areasDatumArrayList.add(area);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreas_Items SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return areasDatumArrayList;
    }

    public void updateAreas_Items(SQLiteDatabase db, String emailId, String groupd_id, String companyId, String id, String name, String value) {
        Log.e(LOG, "updateAreas_Items emailId: " + emailId);
        Log.e(LOG, "updateAreas_Items groupd_id: " + groupd_id);
        Log.e(LOG, "updateAreas_Items companyId: " + companyId);
        Log.e(LOG, "updateAreas_Items id: " + id);
        Log.e(LOG, "updateAreas_Items name: " + name);

        try {

            ContentValues values = new ContentValues();
            values.put(isFilled_ai, value);


            // update row
            db.update(TABLE_AREAS_ITEMS, values,
                    emailId_areas_ai + " = ? "
                            + "AND "
                            + groupd_id_ai + " = ? "
                            + "AND "
                            + admin_id_ai + " = ? "
                            + "AND "
                            + checklist_idi + " = ? "
                            + "AND "
                            + equipment_idi + " = ? "
                            + "AND "
                            + id_ai + " = ? "
                            + "AND "
                            + name_ai + " = ? "
                    , new String[]{emailId, groupd_id, companyId, "0", "0", id, name});
            Log.e(LOG, "updateAreas_Items Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "updateAreas_Items SQLiteException: " + e);
        } finally {
            db.close();
        }
    }

    /*
     * get area User
     */
    public AreaItemDatum getInputParamters(SQLiteDatabase db, String emailId, String groupd_id
            , String id, String absolute_range_flag, String companyId) {
        Log.e(LOG, "--------------------------------------------------------------------------");
        Log.e(LOG, "getAreas_Items emailId: " + emailId);
        Log.e(LOG, "getAreas_Items groupd_id: " + groupd_id);
        Log.e(LOG, "getAreas_Items id: " + id);
        Log.e(LOG, "getAreas_Items absolute_range_flag: " + absolute_range_flag);
        Log.e(LOG, "getAreas_Items companyId: " + companyId);

        AreaItemDatum area = null;
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                    + " WHERE "
                    + emailId_areas_ai + " = ? "
                    + "AND "
                    + groupd_id_ai + " = ? "
                    + "AND "
                    + absolute_range_flag_ai + " = ? "
                    + "AND "
                    + id_ai + " = ? "
                    + "AND "
                    + admin_id_ai + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{emailId, groupd_id, absolute_range_flag, id, companyId});


            if (c != null) {
                Log.e(LOG, "getAreas_Items count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {
                    area = new AreaItemDatum();
                    area.setId(c.getString(c.getColumnIndex(id_ai)));
                    area.setName(c.getString(c.getColumnIndex(name_ai)));
                    area.setSetId(c.getString(c.getColumnIndex(set_id_ai)));
                    area.setUnitId(c.getString(c.getColumnIndex(unit_id_ai)));
                    area.setAbsoluteRangeFlag(c.getString(c.getColumnIndex(absolute_range_flag_ai)));
                    area.setAMin(c.getString(c.getColumnIndex(a_min_ai)));
                    area.setAMax(c.getString(c.getColumnIndex(a_max_ai)));
                    area.setOptimalRange(c.getString(c.getColumnIndex(optimal_range_ai)));
                    area.setOMin(c.getString(c.getColumnIndex(o_min_ai)));
                    area.setOMax(c.getString(c.getColumnIndex(o_max_ai)));
                    area.setForceNote(c.getString(c.getColumnIndex(force_note_ai)));
                    area.setActivites(c.getString(c.getColumnIndex(activites_ai)));
                    area.setDescription(c.getString(c.getColumnIndex(description_ai)));
                    area.setAdminId(c.getString(c.getColumnIndex(admin_id_ai)));
                    area.setGroupdId(c.getString(c.getColumnIndex(groupd_id_ai)));
                    area.setChecklist_id(c.getString(c.getColumnIndex(checklist_idi)));
                    area.setEquipment_id(c.getString(c.getColumnIndex(equipment_idi)));
                    area.setIsFilled(c.getString(c.getColumnIndex(isFilled_ai)));


                    Log.e(LOG, "--------------------------------------------------------------------------");
                    Log.e(LOG, "RESULT getAreas_Items id: " + c.getString(c.getColumnIndex(id_ai)));
                    Log.e(LOG, "RESULT getAreas_Items name: " + c.getString(c.getColumnIndex(name_ai)));


                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreas_Items SQLiteException: " + e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return area;
    }

    public void createUnits(SQLiteDatabase database, ArrayList<Datum> units) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
            database.execSQL(CREATE_TABLE_UNITS);

            for (Datum unitObject : units) {

                ContentValues values = new ContentValues();
                values.put(unit_id_u, unitObject.getId());
                values.put(adminId_u, unitObject.getAdminId());
                values.put(name_u, unitObject.getName());

                // insert row
                database.insert(TABLE_UNITS, null, values);
            }
            Log.e(LOG, "Units Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "Units SQLiteException: " + e);
        } finally {
            database.close();
        }
    }

    public void createAreaCheckList(SQLiteDatabase database, ArrayList<AreaCheckListDatum> areaCheckListData) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_CHECK_LIST);
            database.execSQL(CREATE_TABLE_AREA_CHECK_LIST);

            for (AreaCheckListDatum areaCheckListDatum : areaCheckListData) {

                ContentValues values = new ContentValues();
                values.put(id_acl, areaCheckListDatum.getId());
                values.put(sets_idx_acl, areaCheckListDatum.getSets_idx());
                values.put(sets_value_acl, areaCheckListDatum.getSets_value());
                values.put(multiple_acl, areaCheckListDatum.getMultiple());

                // insert row
                database.insert(TABLE_AREA_CHECK_LIST, null, values);
            }
            Log.e(LOG, "Area Check List Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "Area Check List SQLiteException: " + e);
        } finally {
            database.close();
        }
    }

    public void createSelectionSet(SQLiteDatabase database, ArrayList<SelectionSetsDatum> selectionSetsData) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTION_SETS);
            database.execSQL(CREATE_TABLE_SELECTION_SET);

            for (SelectionSetsDatum eqObject : selectionSetsData) {

                ContentValues values = new ContentValues();
                values.put(id_ss, eqObject.getId());
                values.put(sets_idx_ss, eqObject.getSetsIdx());
                values.put(sets_value_ss, eqObject.getSetsValue());
                values.put(multiple_ss, eqObject.getMultiple());

                // insert row
                database.insert(TABLE_SELECTION_SETS, null, values);
            }
            Log.e(LOG, "createSelectionSet Status Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "createSelectionSet Status SQLiteException: " + e);
        } finally {
            database.close();
        }
    }


    public void createEquipmentStatus(SQLiteDatabase database, ArrayList<EquipmentStatusDatum> equipmentStatusData) {

        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT_STATUS);
            database.execSQL(CREATE_TABLE_EQUIPMENT_STATUS);

            for (EquipmentStatusDatum eqObject : equipmentStatusData) {

                ContentValues values = new ContentValues();
                values.put(id_es, eqObject.getId());
                values.put(sets_idx_es, eqObject.getSets_idx());
                values.put(sets_value_es, eqObject.getSets_value());
                values.put(multiple_es, eqObject.getMultiple());

                // insert row
                database.insert(TABLE_EQUIPMENT_STATUS, null, values);
            }
            Log.e(LOG, "Equipment Status Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "Equipment Status SQLiteException: " + e);
        } finally {
            database.close();
        }
    }

    public ArrayList<SpinnerItemModel> getSpinnerItems(SQLiteDatabase db, String sets_id) {
        ArrayList<SpinnerItemModel> items = new ArrayList<SpinnerItemModel>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SELECTION_SETS + " WHERE " + sets_idx_ss + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{sets_id});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {
                        SpinnerItemModel spinnerItemModel = new SpinnerItemModel();
                        spinnerItemModel.setItem_id(c.getString(c.getColumnIndex(id_ss)));
                        spinnerItemModel.setItem_name(c.getString(c.getColumnIndex(sets_value_ss)));
                        spinnerItemModel.setMultiple(c.getString(c.getColumnIndex(multiple_ss)));

                        items.add(spinnerItemModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "Units SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public AreaItemDatum getStatusItems(SQLiteDatabase db, String emailId, String groupd_id
            , String adminId, String statusItem) {
        Log.e(LOG, "getStatusItems groupd_id: " + groupd_id);
        AreaItemDatum area = null;
        String selectQuery;
        Cursor c = null;
        try {
            if (statusItem.equals("equipment_status")) {

                selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                        + " WHERE "
                        + emailId_areas_ai + " = ? "
                        + "AND "
                        + groupd_id_ai + " = ? "
                        + "AND "
                        + admin_id_ai + " = ? "
                        + "AND "
                        + equipment_idi + " > ? ";
            } else {

                selectQuery = "SELECT * FROM " + TABLE_AREAS_ITEMS
                        + " WHERE "
                        + emailId_areas_ai + " = ? "
                        + "AND "
                        + groupd_id_ai + " = ? "
                        + "AND "
                        + admin_id_ai + " = ? "
                        + "AND "
                        + checklist_idi + " > ? ";
            }
            c = db.rawQuery(selectQuery, new String[]{emailId, groupd_id, adminId, "0"});


            if (c != null) {
                Log.e(LOG, "getEquipmentStatusItems count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {
                    area = new AreaItemDatum();
                    area.setId(c.getString(c.getColumnIndex(id_ai)));
                    area.setName(c.getString(c.getColumnIndex(name_ai)));
                    area.setSetId(c.getString(c.getColumnIndex(set_id_ai)));
                    area.setUnitId(c.getString(c.getColumnIndex(unit_id_ai)));
                    area.setAbsoluteRangeFlag(c.getString(c.getColumnIndex(absolute_range_flag_ai)));
                    area.setAMin(c.getString(c.getColumnIndex(a_min_ai)));
                    area.setAMax(c.getString(c.getColumnIndex(a_max_ai)));
                    area.setOptimalRange(c.getString(c.getColumnIndex(optimal_range_ai)));
                    area.setOMin(c.getString(c.getColumnIndex(o_min_ai)));
                    area.setOMax(c.getString(c.getColumnIndex(o_max_ai)));
                    area.setForceNote(c.getString(c.getColumnIndex(force_note_ai)));
                    area.setActivites(c.getString(c.getColumnIndex(activites_ai)));
                    area.setDescription(c.getString(c.getColumnIndex(description_ai)));
                    area.setAdminId(c.getString(c.getColumnIndex(admin_id_ai)));
                    area.setGroupdId(c.getString(c.getColumnIndex(groupd_id_ai)));
                    area.setChecklist_id(c.getString(c.getColumnIndex(checklist_idi)));
                    area.setEquipment_id(c.getString(c.getColumnIndex(equipment_idi)));
                    area.setIsFilled(c.getString(c.getColumnIndex(isFilled_ai)));
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getEquipmentStatusItems SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return area;
    }

    public ArrayList<EquipmentStatusDatum> getEquipmentStatusData(SQLiteDatabase db, String sets_idx) {
        Log.e(LOG, "getEquipmentStatusData sets_idx: " + sets_idx);
        ArrayList<EquipmentStatusDatum> items = new ArrayList<EquipmentStatusDatum>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EQUIPMENT_STATUS + " WHERE " + sets_idx_es + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{sets_idx});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        EquipmentStatusDatum equipmentStatusDatum = new EquipmentStatusDatum();
                        equipmentStatusDatum.setId(c.getString(c.getColumnIndex(id_es)));
                        equipmentStatusDatum.setSets_idx(c.getString(c.getColumnIndex(sets_idx_es)));
                        equipmentStatusDatum.setSets_value(c.getString(c.getColumnIndex(sets_value_es)));
                        equipmentStatusDatum.setMultiple(c.getString(c.getColumnIndex(multiple_es)));
                        equipmentStatusDatum.setIsChecked("0");

                        items.add(equipmentStatusDatum);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getEquipmentStatusData SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<AreaCheckListDatum> getAreaCheckListData(SQLiteDatabase db, String sets_idx) {
        Log.e(LOG, "getAreaCheckListData sets_idx: " + sets_idx);
        ArrayList<AreaCheckListDatum> items = new ArrayList<AreaCheckListDatum>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AREA_CHECK_LIST + " WHERE " + sets_idx_acl + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{sets_idx});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        AreaCheckListDatum areaCheckListDatum = new AreaCheckListDatum();
                        areaCheckListDatum.setId(c.getString(c.getColumnIndex(id_acl)));
                        areaCheckListDatum.setSets_idx(c.getString(c.getColumnIndex(sets_idx_acl)));
                        areaCheckListDatum.setSets_value(c.getString(c.getColumnIndex(sets_value_acl)));
                        areaCheckListDatum.setMultiple(c.getString(c.getColumnIndex(multiple_acl)));
                        areaCheckListDatum.setIsChecked("0");

                        items.add(areaCheckListDatum);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreaCheckListData SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    //PARAm SYNC
    public Boolean ParamSyncData(SQLiteDatabase db, String area_item_id, String group_id, String adminId) {
        Boolean tag = false;
        Cursor c = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_Parameter_Sync + " WHERE " + area_item_id_ps + " = ? "
                    + " AND " +
                    groupd_id_ps + " = ?"
                    + " AND " +
                    adminId_ps + " = ?"
                    + " AND " +
                    isShown_ps + " = ?";
            c = db.rawQuery(selectQuery, new String[]{area_item_id, group_id, adminId, "1"});
            if (c != null) {
                tag = c.getCount() > 0;
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "areasCount SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return tag;
    }

    public Boolean createParamSync(SQLiteDatabase database, ParameterDataEntrySyncModel dataEntrySyncModel, String email,
                                   String adminId, String user_name, String date_time, String qrtag) {
        Boolean saved = false;
        if (dataEntrySyncModel != null) {
            try {


                ContentValues values = new ContentValues();
                values.put(area_item_id_ps, dataEntrySyncModel.getArea_item_id());
                values.put(name_ps, dataEntrySyncModel.getName());
                values.put(unit_id_ps, dataEntrySyncModel.getUnit_id());
                values.put(set_id_ps, dataEntrySyncModel.getSet_id());
                values.put(groupd_id_ps, dataEntrySyncModel.getGroupd_id());
                values.put(optimal_range_ps, dataEntrySyncModel.getOptimal_range());
                values.put(absolute_range_ps, dataEntrySyncModel.getAbsolute_range());
                values.put(unit_ps, dataEntrySyncModel.getUnit());
                values.put(email_ps, email);
                values.put(adminId_ps, adminId);
                values.put(user_name_ps, user_name);
                values.put(date_time_ps, date_time);
                values.put(isShown_ps, "1");
                values.put(is_qr_ps, qrtag);
                values.put(is_checked_ps, dataEntrySyncModel.getIsChecked());

                // insert row
                database.insert(TABLE_Parameter_Sync, null, values);
                saved = true;
                Log.e(LOG, "createParamSync Saved");
            } catch (SQLiteException e) {
                saved = false;
                Log.e(LOG, "createParamSync SQLiteException: " + e);
            } finally {
                database.close();
            }
        }
        return saved;
    }


    public Boolean updateParamSync(SQLiteDatabase database, ParameterDataEntrySyncModel dataEntrySyncModel, String email,
                                   String adminId, String user_name, String date_time, String qrtag) {
        Boolean saved = false;
        if (dataEntrySyncModel != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(area_item_id_ps, dataEntrySyncModel.getArea_item_id());
                values.put(name_ps, dataEntrySyncModel.getName());
                values.put(unit_id_ps, dataEntrySyncModel.getUnit_id());
                values.put(set_id_ps, dataEntrySyncModel.getSet_id());
                values.put(groupd_id_ps, dataEntrySyncModel.getGroupd_id());
                values.put(optimal_range_ps, dataEntrySyncModel.getOptimal_range());
                values.put(absolute_range_ps, dataEntrySyncModel.getAbsolute_range());
                values.put(unit_ps, dataEntrySyncModel.getUnit());
                values.put(email_ps, email);
                values.put(adminId_ps, adminId);
                values.put(user_name_ps, user_name);
                values.put(date_time_ps, date_time);
                values.put(is_qr_ps, qrtag);
                values.put(is_checked_ps, dataEntrySyncModel.getIsChecked());
                values.put(isSynced_ps, "0");

                database.update(TABLE_Parameter_Sync, values,
                        area_item_id_ps + " = ? "
                                + "AND "
                                + groupd_id_ps + " = ? "
                                + "AND "
                                + adminId_ps + " = ? "
                                + "AND "
                                + isShown_ps + " = ? "
                        , new String[]{dataEntrySyncModel.getArea_item_id(), dataEntrySyncModel.getGroupd_id(), adminId, "1"});

                saved = true;
                Log.e(LOG, "updateParamSync Saved");
            } catch (SQLiteException e) {
                saved = false;
                Log.e(LOG, "updateParamSync SQLiteException: " + e);
            } finally {
                database.close();
            }
        }
        return saved;
    }

    public void updateMegaParamSync(SQLiteDatabase database, String area_item_id, String group_id, String adminId,
                                    String email) {

        try {
            ContentValues values = new ContentValues();
            values.put(isShown_ps, "0");

            database.update(TABLE_Parameter_Sync, values,
                    area_item_id_ps + " = ? "
                            + "AND "
                            + groupd_id_ps + " = ? "
                            + "AND "
                            + adminId_ps + " = ? "
                            + "AND "
                            + email_ps + " = ? "
                            + "AND "
                            + isShown_ps + " = ? "
                    , new String[]{area_item_id, group_id, adminId, email, "1"});

            Log.e(LOG, "updateParamSync Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "updateParamSync SQLiteException: " + e);
        } finally {
            database.close();
        }

    }

    public ParameterDataEntrySyncModel getParamSync(SQLiteDatabase db, String email, String adminId, String name, String area_id, String groupId) {
        ParameterDataEntrySyncModel dataEntrySyncModel = null;

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_Parameter_Sync
                    + " WHERE "
                    + email_ps + " = ? "
                    + "AND "
                    + adminId_ps + " = ? "
                    + "AND "
                    + name_ps + " = ? "
                    + "AND "
                    + area_item_id_ps + " = ? "
                    + "AND "
                    + groupd_id_ps + " = ? "
                    + "AND "
                    + isShown_ps + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId, name, area_id, groupId, "1"});


            if (c != null) {
                Log.e(LOG, "getParamSync count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {

                    dataEntrySyncModel = new ParameterDataEntrySyncModel();
                    dataEntrySyncModel.setArea_item_id(c.getString(c.getColumnIndex(area_item_id_ps)));
                    dataEntrySyncModel.setName(c.getString(c.getColumnIndex(name_ps)));
                    dataEntrySyncModel.setUnit_id(c.getString(c.getColumnIndex(unit_id_ps)));
                    dataEntrySyncModel.setSet_id(c.getString(c.getColumnIndex(set_id_ps)));
                    dataEntrySyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_ps)));
                    dataEntrySyncModel.setOptimal_range(c.getString(c.getColumnIndex(optimal_range_ps)));
                    dataEntrySyncModel.setAbsolute_range(c.getString(c.getColumnIndex(absolute_range_ps)));
                    dataEntrySyncModel.setUnit(c.getString(c.getColumnIndex(unit_ps)));
                    dataEntrySyncModel.setUser_name(c.getString(c.getColumnIndex(user_name_ps)));
                    dataEntrySyncModel.setDate_time(c.getString(c.getColumnIndex(date_time_ps)));
                    dataEntrySyncModel.setEmail(c.getString(c.getColumnIndex(email_ps)));
                    dataEntrySyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_ps)));
                    dataEntrySyncModel.setIsChecked(c.getString(c.getColumnIndex(is_checked_ps)));

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getParamSync SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return dataEntrySyncModel;
    }


    //Equipment SYNC
    public Boolean createEquipmentSync(SQLiteDatabase database, EquipmentStatusSyncModel dataEntrySyncModel, String email,
                                       String adminId, String date_time, String qrtag, String ID, String name) {
        Boolean saved = false;
        if (dataEntrySyncModel != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(equipment_id_ess, dataEntrySyncModel.getEquipment_id());
                values.put(groupd_id_ess, dataEntrySyncModel.getGroupd_id());
                values.put(sets_idx_ess, dataEntrySyncModel.getSets_idx());
                values.put(sets_value_ess, dataEntrySyncModel.getSets_value());

                values.put(email_id_ess, email);
                values.put(adminId_id_ess, adminId);
                values.put(dateTime_ess, date_time);
                values.put(isShown_ess, "1");
                values.put(is_qr_ess, qrtag);
                values.put(ID_ess, ID);
                values.put(name_ess, name);

                // insert row
                database.insert(TABLE_EquipmentStatus_Sync, null, values);
                saved = true;
                Log.e(LOG, "createEquipmentSync Saved");
            } catch (SQLiteException e) {
                saved = false;
                Log.e(LOG, "createEquipmentSync SQLiteException: " + e);
            } finally {
                database.close();
            }
        }
        return saved;
    }


    public Boolean deleteEquipmentSync(SQLiteDatabase database, String email, String adminId
            , String equipment_id, String group_id, String ID) {
        Log.e(LOG, "deleteEquipmentSync group_id: " + group_id);
        Log.e(LOG, "deleteEquipmentSync email: " + email);
        Log.e(LOG, "deleteEquipmentSync adminId: " + adminId);
        Log.e(LOG, "deleteEquipmentSync equipment_id: " + equipment_id);
        Boolean saved = false;
        try {
            database.delete(TABLE_EquipmentStatus_Sync,
                    groupd_id_ess + " = ? "
                            + "AND "
                            + email_id_ess + " = ? "
                            + "AND "
                            + adminId_id_ess + " = ? "
                            + "AND "
                            + sets_idx_ess + " = ? "
                            + "AND "
                            + ID_ess + " = ? "
                    , new String[]{group_id, email, adminId, equipment_id, ID});

            saved = true;
            Log.e(LOG, "deleteEquipmentSync DELETED");
        } catch (SQLiteException e) {
            saved = false;
            Log.e(LOG, "deleteEquipmentSync SQLiteException: " + e);
        } finally {
            database.close();
        }
        return saved;
    }

    public ArrayList<EquipmentStatusSyncModel> getEquipmentync(SQLiteDatabase db, String email, String adminId, String groupId, String set_id, String ID) {
        ArrayList<EquipmentStatusSyncModel> dataEntrySyncModels = new ArrayList<EquipmentStatusSyncModel>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EquipmentStatus_Sync
                    + " WHERE "
                    + groupd_id_ess + " = ? "
                    + "AND "
                    + email_id_ess + " = ? "
                    + "AND "
                    + adminId_id_ess + " = ? "
                    + "AND "
                    + isShown_ess + " = ? "
                    + "AND "
                    + sets_idx_ess + " = ? "
                    + "AND "
                    + ID_ess + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{groupId, email, adminId, "1", set_id, ID});


            if (c != null) {
                Log.e(LOG, "getEquipmentync count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {

                    do {

                        EquipmentStatusSyncModel dataEntrySyncModel = new EquipmentStatusSyncModel();
                        dataEntrySyncModel.setEquipment_id(c.getString(c.getColumnIndex(equipment_id_ess)));
                        dataEntrySyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_ess)));
                        dataEntrySyncModel.setSets_idx(c.getString(c.getColumnIndex(sets_idx_ess)));
                        dataEntrySyncModel.setSets_value(c.getString(c.getColumnIndex(sets_value_ess)));

                        dataEntrySyncModel.setEmail_id(c.getString(c.getColumnIndex(email_id_ess)));
                        dataEntrySyncModel.setAdminId(c.getString(c.getColumnIndex(adminId_id_ess)));
                        dataEntrySyncModel.setDateTime(c.getString(c.getColumnIndex(dateTime_ess)));
                        dataEntrySyncModel.setIsShown(c.getString(c.getColumnIndex(isShown_ess)));
                        dataEntrySyncModel.setIsShown(c.getString(c.getColumnIndex(isShown_ess)));
                        dataEntrySyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_ess)));
                        dataEntrySyncModel.setID(c.getString(c.getColumnIndex(ID_ess)));
                        dataEntrySyncModel.setName(c.getString(c.getColumnIndex(name_ess)));

                        dataEntrySyncModels.add(dataEntrySyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getEquipmentync SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return dataEntrySyncModels;
    }

    public void updateEquipmentync(SQLiteDatabase db, String email, String adminId, String groupId, String set_id, String qrtag) {

        try {

            ContentValues values = new ContentValues();
            values.put(isShown_ess, "0");
            values.put(is_qr_ess, qrtag);

            db.update(TABLE_EquipmentStatus_Sync, values,
                    groupd_id_ess + " = ? "
                            + "AND "
                            + email_id_ess + " = ? "
                            + "AND "
                            + adminId_id_ess + " = ? "
                            + "AND "
                            + isShown_ess + " = ? "
                            + "AND "
                            + sets_idx_ess + " = ? "
                    , new String[]{groupId, email, adminId, "1", set_id});

        } catch (SQLiteException e) {
            Log.e(LOG, "getEquipmentync SQLiteException: " + e);
        } finally {
            db.close();
        }

    }

    //CheckList SYNC
    public Boolean createCheckListSync(SQLiteDatabase database, AreaCheckListSyncModel dataEntrySyncModel, String email,
                                       String adminId, String date_time, String qrTag, String ID, String name) {
        Boolean saved = false;
        if (dataEntrySyncModel != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(checklist_id_acls, dataEntrySyncModel.getChecklist_id());
                values.put(groupd_id_acls, dataEntrySyncModel.getGroupd_id());
                values.put(sets_idx_acls, dataEntrySyncModel.getSets_idx());
                values.put(sets_value_acls, dataEntrySyncModel.getSets_value());
                values.put(email_id_acls, email);
                values.put(adminId_id_acls, adminId);
                values.put(dateTime_acls, date_time);
                values.put(isShown_acls, "1");
                values.put(is_qr_acls, qrTag);
                values.put(ID_acls, ID);
                values.put(name_acls, name);

                // insert row
                database.insert(TABLE_AreaCheckList_Sync, null, values);
                saved = true;
                Log.e(LOG, "createCheckListSync Saved");
            } catch (SQLiteException e) {
                saved = false;
                Log.e(LOG, "createCheckListSync SQLiteException: " + e);
            } finally {
                database.close();
            }
        }
        return saved;
    }


    public Boolean deleteCheckListSync(SQLiteDatabase database, String email, String adminId
            , String equipment_id, String group_id, String ID) {
        Log.e(LOG, "deleteCheckListSync group_id: " + group_id);
        Log.e(LOG, "deleteCheckListSync email: " + email);
        Log.e(LOG, "deleteCheckListSync adminId: " + adminId);
        Log.e(LOG, "deleteCheckListSync equipment_id: " + equipment_id);
        Boolean saved = false;
        try {
            database.delete(TABLE_AreaCheckList_Sync,
                    groupd_id_acls + " = ? "
                            + "AND "
                            + email_id_acls + " = ? "
                            + "AND "
                            + adminId_id_acls + " = ? "
                            + "AND "
                            + sets_idx_acls + " = ? "
                            + "AND "
                            + ID_acls + " = ? "
                    , new String[]{group_id, email, adminId, equipment_id, ID});

            saved = true;
            Log.e(LOG, "deleteCheckListSync DELETED");
        } catch (SQLiteException e) {
            saved = false;
            Log.e(LOG, "deleteCheckListSync SQLiteException: " + e);
        } finally {
            database.close();
        }
        return saved;
    }

    public ArrayList<AreaCheckListSyncModel> getCheckListync(SQLiteDatabase db, String email
            , String adminId, String groupId, String set_id, String ID) {
        ArrayList<AreaCheckListSyncModel> dataEntrySyncModels = new ArrayList<AreaCheckListSyncModel>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AreaCheckList_Sync
                    + " WHERE "
                    + groupd_id_acls + " = ? "
                    + "AND "
                    + email_id_acls + " = ? "
                    + "AND "
                    + adminId_id_acls + " = ? "
                    + "AND "
                    + isShown_acls + " = ? "
                    + "AND "
                    + sets_idx_acls + " = ? "
                    + "AND "
                    + ID_acls + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{groupId, email, adminId, "1", set_id, ID});


            if (c != null) {
                Log.e(LOG, "getCheckListync count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {

                    do {

                        AreaCheckListSyncModel dataEntrySyncModel = new AreaCheckListSyncModel();
                        dataEntrySyncModel.setChecklist_id(c.getString(c.getColumnIndex(checklist_id_acls)));
                        dataEntrySyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_acls)));
                        dataEntrySyncModel.setSets_idx(c.getString(c.getColumnIndex(sets_idx_acls)));
                        dataEntrySyncModel.setSets_value(c.getString(c.getColumnIndex(sets_value_acls)));

                        dataEntrySyncModel.setEmail_id(c.getString(c.getColumnIndex(email_id_acls)));
                        dataEntrySyncModel.setAdminId(c.getString(c.getColumnIndex(adminId_id_acls)));
                        dataEntrySyncModel.setDateTime(c.getString(c.getColumnIndex(dateTime_acls)));
                        dataEntrySyncModel.setIsShown(c.getString(c.getColumnIndex(isShown_acls)));
                        dataEntrySyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_acls)));
                        dataEntrySyncModel.setID(c.getString(c.getColumnIndex(ID_acls)));
                        dataEntrySyncModel.setName(c.getString(c.getColumnIndex(name_acls)));

                        dataEntrySyncModels.add(dataEntrySyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getCheckListync SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return dataEntrySyncModels;
    }

    public void updateCheckListync(SQLiteDatabase db, String email
            , String adminId, String groupId, String set_id, String qrtag) {


        try {

            ContentValues values = new ContentValues();
            values.put(isShown_acls, "0");
            values.put(is_qr_acls, qrtag);

            db.update(TABLE_AreaCheckList_Sync, values,
                    groupd_id_acls + " = ? "
                            + "AND "
                            + email_id_acls + " = ? "
                            + "AND "
                            + adminId_id_acls + " = ? "
                            + "AND "
                            + isShown_acls + " = ? "
                            + "AND "
                            + sets_idx_acls + " = ? "
                    , new String[]{groupId, email, adminId, "1", set_id});
        } catch (SQLiteException e) {
            Log.e(LOG, "getCheckListync SQLiteException: " + e);
        } finally {

            db.close();
        }
    }

    public boolean createNote(SQLiteDatabase db, String group_id, String noteFrom
            , String note, String email, String admin_id, String dateTime, String qrtag) {
        boolean saved = false;

        try {
            ContentValues values = new ContentValues();
            values.put(groupd_id_ns, group_id);
            values.put(name_ns, noteFrom);
            values.put(adminId_ns, admin_id);
            values.put(date_time_ns, dateTime);
            values.put(email_ns, email);
            values.put(note_ns, note);
            values.put(is_qr_ns, qrtag);

            db.insert(TABLE_Notes_Sync, null, values);
            saved = true;
            Log.e(LOG, "updateNote Saved");
        } catch (SQLiteException e) {
            saved = false;
            Log.e(LOG, "createNote SQLiteException: " + e);
        } finally {
            db.close();
        }

        return saved;
    }

    public boolean updateNote(SQLiteDatabase db, String group_id, String noteFrom
            , String note, String email, String admin_id, String dateTime, String qrtag) {
        boolean saved = false;

        try {
            ContentValues values = new ContentValues();
            values.put(groupd_id_ns, group_id);
            values.put(name_ns, noteFrom);
            values.put(adminId_ns, admin_id);
            values.put(date_time_ns, dateTime);
            values.put(email_ns, email);
            values.put(note_ns, note);
            values.put(is_qr_ns, qrtag);

            db.update(TABLE_Notes_Sync, values,
                    groupd_id_ns + " = ? "
                            + "AND "
                            + name_ns + " = ? "
                            + "AND "
                            + adminId_ns + " = ? "
                            + "AND "
                            + email_ns + " = ? "
                    , new String[]{group_id, noteFrom, admin_id, email});

            saved = true;
            Log.e(LOG, "updateNote Updated");
        } catch (SQLiteException e) {
            saved = false;
            Log.e(LOG, "updateNote SQLiteException: " + e);
        } finally {
            db.close();
        }

        return saved;
    }

    public String getNote(SQLiteDatabase db, String group_id, String noteFrom
            , String email, String admin_id) {
        String finalNote = null;

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_Notes_Sync
                    + " WHERE "
                    + groupd_id_ns + " = ? "
                    + "AND "
                    + name_ns + " = ? "
                    + "AND "
                    + email_ns + " = ? "
                    + "AND "
                    + adminId_ns + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{group_id, noteFrom, email, admin_id});


            if (c != null) {
                Log.e(LOG, "getCheckListync count: " + c.getCount());
                if (!c.moveToFirst()) {
                    return null;
                } else {

                    finalNote = c.getString(c.getColumnIndex(note_ns));
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getNote SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return finalNote;
    }


    //SYNCING SEND
    public ArrayList<ParamterDataEntryDBSyncModel> getParamDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<ParamterDataEntryDBSyncModel> items = new ArrayList<>();

        Log.e("asdhkas", " " + adminId);
        Log.e("asdhkas", " " + email);

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_Parameter_Sync
                    + " WHERE "
                    + email_ps + " = ? "
                    + " AND "
                    + adminId_ps + " = ? "
                    + " AND "
                    + isSynced_ps + " != ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId, "1"});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        ParamterDataEntryDBSyncModel parameterDataEntrySyncModel = new ParamterDataEntryDBSyncModel();
                        parameterDataEntrySyncModel.setArea_item_id(c.getString(c.getColumnIndex(area_item_id_ps)));
                        parameterDataEntrySyncModel.setName(c.getString(c.getColumnIndex(name_ps)));
                        parameterDataEntrySyncModel.setSet_id(c.getString(c.getColumnIndex(set_id_ps)));
                        parameterDataEntrySyncModel.setUnit_id(c.getString(c.getColumnIndex(unit_id_ps)));
                        parameterDataEntrySyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_ps)));
                        parameterDataEntrySyncModel.setOptimal_range(c.getString(c.getColumnIndex(optimal_range_ps)));
                        parameterDataEntrySyncModel.setAbsolute_range(c.getString(c.getColumnIndex(absolute_range_ps)));
                        parameterDataEntrySyncModel.setUnit(c.getString(c.getColumnIndex(unit_ps)));
                        parameterDataEntrySyncModel.setEmail(c.getString(c.getColumnIndex(email_ps)));
                        parameterDataEntrySyncModel.setAdminId(c.getString(c.getColumnIndex(adminId_ps)));
                        parameterDataEntrySyncModel.setUser_name(c.getString(c.getColumnIndex(user_name_ps)));
                        parameterDataEntrySyncModel.setDate_time(c.getString(c.getColumnIndex(date_time_ps)));
                        parameterDataEntrySyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_ps)));
                        parameterDataEntrySyncModel.setIsChecked(c.getString(c.getColumnIndex(is_checked_ps)));

                        items.add(parameterDataEntrySyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getParamSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<ParamterDataEntryDBSyncModel> deleteParamDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<ParamterDataEntryDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "DELETE * FROM " + TABLE_Parameter_Sync
                    + " WHERE "
                    + email_ps + " = ? "
                    + " AND "
                    + adminId_ps + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "deleteParamDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<EquipmentStatusDBSyncModel> getEquipmentDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<EquipmentStatusDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EquipmentStatus_Sync
                    + " WHERE "
                    + email_id_ess + " = ? "
                    + " AND "
                    + adminId_id_ess + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        EquipmentStatusDBSyncModel equipmentStatusDBSyncModel = new EquipmentStatusDBSyncModel();
                        equipmentStatusDBSyncModel.setEquipment_id(c.getString(c.getColumnIndex(equipment_id_ess)));
                        equipmentStatusDBSyncModel.setSets_idx(c.getString(c.getColumnIndex(sets_idx_ess)));
                        equipmentStatusDBSyncModel.setSets_value(c.getString(c.getColumnIndex(sets_value_ess)));
                        equipmentStatusDBSyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_ess)));
                        equipmentStatusDBSyncModel.setEmail(c.getString(c.getColumnIndex(email_id_ess)));
                        equipmentStatusDBSyncModel.setAdmin_id(c.getString(c.getColumnIndex(adminId_id_ess)));
                        equipmentStatusDBSyncModel.setDate_time(c.getString(c.getColumnIndex(dateTime_ess)));
                        equipmentStatusDBSyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_ess)));
                        equipmentStatusDBSyncModel.setID(c.getString(c.getColumnIndex(ID_ess)));
                        equipmentStatusDBSyncModel.setName(c.getString(c.getColumnIndex(name_ess)));

                        items.add(equipmentStatusDBSyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getEquipmentDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<EquipmentStatusDBSyncModel> deleteEquipmentDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<EquipmentStatusDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "DELETE * FROM " + TABLE_EquipmentStatus_Sync
                    + " WHERE "
                    + email_id_ess + " = ? "
                    + " AND "
                    + adminId_id_ess + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "deleteEquipmentDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<AreaCheckListDBSyncModel> getAreaCheckListDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<AreaCheckListDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AreaCheckList_Sync
                    + " WHERE "
                    + email_id_acls + " = ? "
                    + " AND "
                    + adminId_id_acls + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        AreaCheckListDBSyncModel areaCheckListDBSyncModel = new AreaCheckListDBSyncModel();
                        areaCheckListDBSyncModel.setChecklist_id(c.getString(c.getColumnIndex(checklist_id_acls)));
                        areaCheckListDBSyncModel.setSets_idx(c.getString(c.getColumnIndex(sets_idx_acls)));
                        areaCheckListDBSyncModel.setSets_value(c.getString(c.getColumnIndex(sets_value_acls)));
                        areaCheckListDBSyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_acls)));
                        areaCheckListDBSyncModel.setEmail(c.getString(c.getColumnIndex(email_id_acls)));
                        areaCheckListDBSyncModel.setAdmin_id(c.getString(c.getColumnIndex(adminId_id_acls)));
                        areaCheckListDBSyncModel.setDate_time(c.getString(c.getColumnIndex(dateTime_acls)));
                        areaCheckListDBSyncModel.setDate_time(c.getString(c.getColumnIndex(dateTime_acls)));
                        areaCheckListDBSyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_acls)));
                        areaCheckListDBSyncModel.setID(c.getString(c.getColumnIndex(ID_acls)));
                        areaCheckListDBSyncModel.setName(c.getString(c.getColumnIndex(name_acls)));

                        items.add(areaCheckListDBSyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreaCheckListDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<AreaCheckListDBSyncModel> deleteAreaCheckListDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<AreaCheckListDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "DELETE * FROM " + TABLE_AreaCheckList_Sync
                    + " WHERE "
                    + email_id_acls + " = ? "
                    + " AND "
                    + adminId_id_acls + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "deleteAreaCheckListDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public ArrayList<NotesDBSyncModel> getNotesDBSyncModel(SQLiteDatabase db, String email, String adminId) {
        ArrayList<NotesDBSyncModel> items = new ArrayList<>();

        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_Notes_Sync
                    + " WHERE "
                    + email_ns + " = ? "
                    + " AND "
                    + adminId_ns + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});


            if (c != null) {

                if (!c.moveToFirst()) {
                    return null;
                } else {
                    do {

                        NotesDBSyncModel notesDBSyncModel = new NotesDBSyncModel();
                        notesDBSyncModel.setGroupd_id(c.getString(c.getColumnIndex(groupd_id_ns)));
                        notesDBSyncModel.setName(c.getString(c.getColumnIndex(name_ns)));
                        notesDBSyncModel.setNote(c.getString(c.getColumnIndex(note_ns)));

                        notesDBSyncModel.setEmail(c.getString(c.getColumnIndex(email_ns)));
                        notesDBSyncModel.setAdminId(c.getString(c.getColumnIndex(adminId_ns)));
                        notesDBSyncModel.setDate_time(c.getString(c.getColumnIndex(date_time_ns)));
                        notesDBSyncModel.setQr_tag(c.getString(c.getColumnIndex(is_qr_ns)));


                        items.add(notesDBSyncModel);


                    } while (c.moveToNext());

                }
            }
        } catch (SQLiteException e) {
            Log.e(LOG, "getAreaCheckListDBSyncModel SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return items;
    }

    public String getMaxDateTime(SQLiteDatabase db, String emailId, String companyId) {
        String date_time = null;

        Cursor c = null;
        try {
            String selectQuery = "SELECT MAX(date_time) FROM " + TABLE_Parameter_Sync
                    + " WHERE "
                    + email_ns + " = ? "
                    + " AND "
                    + adminId_ns + " = ? ";
            c = db.rawQuery(selectQuery, new String[]{email, adminId});
            Log.e("asjhdj", "column : " + c.getColumnIndex(date_time_ps));
            if (c != null) {
                if (!c.moveToFirst()) {
                    return null;
                } else {
                    if (c.getColumnIndex(date_time_ps) != -1) {
                        date_time = c.getString(c.getColumnIndexOrThrow(date_time_ps));
                        Log.e(LOG, "getMaxDateTime Saved");
                    }
                }
            }

            Log.e(LOG, "getMaxDateTime Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "getMaxDateTime SQLiteException: " + e);
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return date_time;
    }

    public void deleteAfterSync(SQLiteDatabase database, String email, String adminId, String date_time) {
        try {

            if (!TextUtils.isEmpty(date_time)) {
                database.delete(TABLE_Parameter_Sync,
                        email_ps + " = ? "
                                + "AND "
                                + adminId_ps + " = ? "
                                + "AND "
                                + date_time_ps + " < "
                        , new String[]{email, adminId, date_time});
            } else {
//                database.delete(TABLE_Parameter_Sync,
//                        email_ps + " = ? "
//                                + "AND "
//                                + adminId_ps + " = ? "
//                        , new String[]{email, adminId});
            }

            database.delete(TABLE_EquipmentStatus_Sync,
                    email_id_ess + " = ? "
                            + "AND "
                            + adminId_id_ess + " = ? "
                    , new String[]{email, adminId});

            database.delete(TABLE_AreaCheckList_Sync,
                    email_id_acls + " = ? "
                            + "AND "
                            + adminId_id_acls + " = ? "
                    , new String[]{email, adminId});

            database.delete(TABLE_Notes_Sync,
                    email_ns + " = ? "
                            + "AND "
                            + adminId_ns + " = ? "
                    , new String[]{email, adminId});

            Log.e("asdas", "deleteAfterSync DELETED ALL");
        } catch (SQLiteException e) {
            Log.e("asdas", "deleteAfterSync SQLiteException: " + e);
        } finally {
            database.close();
        }
    }


    public void setIsSynced(SQLiteDatabase db, String emailId, String companyId) {

        try {

            Log.e("SyncData", " email : " + emailId);
            Log.e("SyncData", " companyId : " + companyId);
            ContentValues values = new ContentValues();
            values.put(isSynced_ps, "1");


            // update row
            db.update(TABLE_Parameter_Sync, values,
                    email_ps + " = ? "
                            + "AND "
                            + adminId_ps + " = ? "
                    , new String[]{emailId, companyId});
            Log.e("SyncData", " saved success");
        } catch (SQLiteException e) {
            Log.e("SyncData", " saved failure : " + e.toString());
        } finally {
            db.close();
        }
    }

    public void updateAreas_ItemsIsFilled(SQLiteDatabase db, String emailId, String companyId) {

        try {

            ContentValues values = new ContentValues();
            values.put(isFilled_ai, "0");


            // update row
            db.update(TABLE_AREAS_ITEMS, values,
                    emailId_areas_ai + " = ? "
                            + "AND "
                            + admin_id_ai + " = ? "
                    , new String[]{emailId, companyId});
            Log.e(LOG, "updateAreas_ItemsIsFilled Saved");
        } catch (SQLiteException e) {
            Log.e(LOG, "updateAreas_ItemsIsFilled SQLiteException: " + e);
        } finally {
            db.close();
        }
    }

    public void deleteParam_Sync(SQLiteDatabase db) {

        try {

            // update row
            if (!db.isOpen()) {

            }
            db.execSQL("delete from " + TABLE_Parameter_Sync);
            Log.e(LOG, "deleteParam_Sync deleted");
        } catch (SQLiteException e) {
            Log.e(LOG, "deleteParam_Sync SQLiteException: " + e);
        } finally {
            db.close();
        }
    }

}