package edu.aku.hassannaqvi.naunehal.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class UsersContract {

    private static final String TAG = "Users_CONTRACT";
    Long _ID;
    String ROW_USERNAME;
    String ROW_PASSWORD;
    String DESIGNATION;
    String DIST_ID;

    public UsersContract() {
        // Default Constructor
    }

    public UsersContract(String username, String password) {
        this.ROW_PASSWORD = password;
        this.ROW_USERNAME = username;
    }

    public Long getUserID() {
        return this._ID;
    }

    public void setId(int id) {
        this._ID = Long.valueOf(id);
    }


    public String getUserName() {
        return this.ROW_USERNAME;
    }

    public void setUserName(String username) {
        this.ROW_USERNAME = username;
    }


    public String getPassword() {
        return this.ROW_PASSWORD;
    }

    public void setPassword(String password) {
        this.ROW_PASSWORD = password;
    }


    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }


    public String getDIST_ID() {
        return this.DIST_ID;
    }

    public void setDIST_ID(String dist_id) {
        this.DIST_ID = dist_id;
    }


    public UsersContract Sync(JSONObject jsonObject) throws JSONException {
        this.ROW_USERNAME = jsonObject.getString(TableUsers.ROW_USERNAME);
        this.ROW_PASSWORD = jsonObject.getString(TableUsers.ROW_PASSWORD);
        this.DESIGNATION = jsonObject.getString(TableUsers.DESIGNATION);
        this.DIST_ID = jsonObject.getString(TableUsers.DIST_ID);
        return this;

    }

    public UsersContract Hydrate(Cursor cursor) {
        this._ID = cursor.getLong(cursor.getColumnIndex(TableUsers._ID));
        this.ROW_USERNAME = cursor.getString(cursor.getColumnIndex(TableUsers.ROW_USERNAME));
        this.ROW_PASSWORD = cursor.getString(cursor.getColumnIndex(TableUsers.ROW_PASSWORD));
        this.DESIGNATION = cursor.getString(cursor.getColumnIndex(TableUsers.DESIGNATION));
        this.DIST_ID = cursor.getString(cursor.getColumnIndex(TableUsers.DIST_ID));
        return this;

    }


    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TableUsers._ID, this._ID == null ? JSONObject.NULL : this._ID);
        json.put(TableUsers.ROW_USERNAME, this.ROW_USERNAME == null ? JSONObject.NULL : this.ROW_USERNAME);
        json.put(TableUsers.ROW_PASSWORD, this.ROW_PASSWORD == null ? JSONObject.NULL : this.ROW_PASSWORD);
        json.put(TableUsers.DESIGNATION, this.DESIGNATION == null ? JSONObject.NULL : this.DESIGNATION);
        json.put(TableUsers.DIST_ID, this.DIST_ID == null ? JSONObject.NULL : this.DIST_ID);
        return json;
    }

    public static abstract class TableUsers implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String _ID = "id";
        public static final String ROW_USERNAME = "username";
        public static final String ROW_PASSWORD = "password";
        public static final String DESIGNATION = "designation";
        public static final String DIST_ID = "dist_id";

        public static final String _URI = "users.php";
    }
}