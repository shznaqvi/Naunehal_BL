package edu.aku.hassannaqvi.naunehal.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import edu.aku.hassannaqvi.naunehal.CONSTANTS;
import edu.aku.hassannaqvi.naunehal.R;
import edu.aku.hassannaqvi.naunehal.adapters.SyncListAdapter;
import edu.aku.hassannaqvi.naunehal.contracts.ChildContract;
import edu.aku.hassannaqvi.naunehal.contracts.ChildInformationContract;
import edu.aku.hassannaqvi.naunehal.contracts.FormsContract;
import edu.aku.hassannaqvi.naunehal.contracts.IMContract;
import edu.aku.hassannaqvi.naunehal.database.DatabaseHelper;
import edu.aku.hassannaqvi.naunehal.databinding.ActivitySyncBinding;
import edu.aku.hassannaqvi.naunehal.models.BLRandom;
import edu.aku.hassannaqvi.naunehal.models.Clusters;
import edu.aku.hassannaqvi.naunehal.models.Districts;
import edu.aku.hassannaqvi.naunehal.models.SyncModel;
import edu.aku.hassannaqvi.naunehal.models.UCs;
import edu.aku.hassannaqvi.naunehal.models.Users;
import edu.aku.hassannaqvi.naunehal.models.VersionApp;
import edu.aku.hassannaqvi.naunehal.workers.DataDownWorkerALL;
import edu.aku.hassannaqvi.naunehal.workers.DataUpWorkerALL;

import static edu.aku.hassannaqvi.naunehal.utils.AndroidUtilityKt.isNetworkConnected;
import static edu.aku.hassannaqvi.naunehal.utils.AppUtilsKt.dbBackup;


public class SyncActivity extends AppCompatActivity {
    private static final String TAG = "SyncActivity";
    DatabaseHelper db;
    SyncListAdapter syncListAdapter;
    ActivitySyncBinding bi;
    List<SyncModel> uploadTables;
    List<SyncModel> downloadTables;
    Boolean listActivityCreated;
    Boolean uploadlistActivityCreated;
    String distCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_sync);
        bi.setCallback(this);
        uploadTables = new ArrayList<>();
        downloadTables = new ArrayList<>();

        // Set tables to UPLOAD
        uploadTables.add(new SyncModel(FormsContract.FormsTable.TABLE_NAME));
        uploadTables.add(new SyncModel(ChildInformationContract.ChildInfoTable.TABLE_NAME));
        uploadTables.add(new SyncModel(ChildContract.ChildTable.TABLE_NAME));
        uploadTables.add(new SyncModel(IMContract.IMTable.TABLE_NAME));

        //bi.noItem.setVisibility(View.VISIBLE);
        bi.noDataItem.setVisibility(View.VISIBLE);
        listActivityCreated = true;
        uploadlistActivityCreated = true;

        db = new DatabaseHelper(this);
        dbBackup(this);

        boolean sync_flag = getIntent().getBooleanExtra(CONSTANTS.SYNC_LOGIN, false);
        if (sync_flag) {
            distCode = getIntent().getStringExtra(CONSTANTS.SYNC_DISTRICTID_LOGIN);
            downloadTables.add(new SyncModel(BLRandom.TableRandom.TABLE_NAME));
        } else {
            // Set tables to DOWNLOAD
            downloadTables.add(new SyncModel(Users.UsersTable.TABLE_NAME));
            downloadTables.add(new SyncModel(VersionApp.VersionAppTable.TABLE_NAME));
            downloadTables.add(new SyncModel(Districts.TableDistricts.TABLE_NAME));
            downloadTables.add(new SyncModel(UCs.TableUCs.TABLE_NAME));
            downloadTables.add(new SyncModel(Clusters.TableClusters.TABLE_NAME));
        }
    }


    void setAdapter(List<SyncModel> tables) {
        syncListAdapter = new SyncListAdapter(tables);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        bi.rvUploadList.setLayoutManager(mLayoutManager2);
        bi.rvUploadList.setItemAnimator(new DefaultItemAnimator());
        bi.rvUploadList.setAdapter(syncListAdapter);
        syncListAdapter.notifyDataSetChanged();
        if (syncListAdapter.getItemCount() > 0) {
            bi.noDataItem.setVisibility(View.GONE);
        } else {
            bi.noDataItem.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }


    @SuppressLint("NonConstantResourceId")
    public void ProcessStart(View view) {

        if (!isNetworkConnected(this))
            return;

        switch (view.getId()) {

            case R.id.btnUpload:
                setAdapter(uploadTables);
                BeginUpload();
                break;
            case R.id.btnSync:
                setAdapter(downloadTables);
                BeginDownload();
                break;

        }
    }


    private void BeginDownload() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();

        for (int i = 0; i < downloadTables.size(); i++) {
            Data.Builder data = new Data.Builder()
                    .putString("table", downloadTables.get(i).gettableName())
                    .putInt("position", i)
                    //.putString("columns", "_id, sysdate")
                    // .putString("where", where)
                    ;
            if (downloadTables.get(i).gettableName().equals(BLRandom.TableRandom.TABLE_NAME)) {
                data.putString("where", BLRandom.TableRandom.COLUMN_DIST_CODE + "='" + distCode + "'");
            }
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DataDownWorkerALL.class)
                    .addTag(String.valueOf(i))
                    .setInputData(data.build()).build();
            workRequests.add(workRequest);

        }

        // FOR SIMULTANEOUS WORKREQUESTS (ALL TABLES DOWNLOAD AT THE SAME TIME)
        WorkManager wm = WorkManager.getInstance();
        WorkContinuation wc = wm.beginWith(workRequests);
        wc.enqueue();

        wc.getWorkInfosLiveData().observe(this, new Observer<List<WorkInfo>>() {


            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                Log.d(TAG, "workInfos: " + workInfos.size());
                for (WorkInfo workInfo : workInfos) {
                    Log.d(TAG, "workInfo: getState " + workInfo.getState());
                    Log.d(TAG, "workInfo: data " + workInfo.getOutputData().getString("data"));
                    Log.d(TAG, "workInfo: error " + workInfo.getOutputData().getString("error"));
                    Log.d(TAG, "workInfo: position " + workInfo.getOutputData().getInt("position", 0));
                }
                for (WorkInfo workInfo : workInfos) {
                    int position = workInfo.getOutputData().getInt("position", 0);
                    String tableName = downloadTables.get(position).gettableName();

                            /*String progress = workInfo.getProgress().getString("progress");
                            bi.wmError.setText("Progress: " + progress);*/

                    if (workInfo.getState() != null &&
                            workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                        String result = workInfo.getOutputData().getString("data");
//Do something with the JSON string
                        if (result != null) {
                            if (result.length() > 0) {
                                DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
                                try {
                                    JSONArray jsonArray = new JSONArray();
                                    int insertCount = 0;
                                    switch (tableName) {
                                        case Users.UsersTable.TABLE_NAME:
                                            jsonArray = new JSONArray(result);
                                            insertCount = db.syncUser(jsonArray);
                                            break;
                                        case VersionApp.VersionAppTable.TABLE_NAME:
                                            insertCount = db.syncVersionApp(new JSONObject(result));
                                            if (insertCount == 1) jsonArray.put("1");
                                            break;
                                        case UCs.TableUCs.TABLE_NAME:
                                            jsonArray = new JSONArray(result);
                                            insertCount = db.syncUCs(jsonArray);
                                            Log.d(TAG, "onChanged: " + tableName + " " + workInfo.getOutputData().getInt("position", 0));
                                            break;
                                        case Districts.TableDistricts.TABLE_NAME:
                                            jsonArray = new JSONArray(result);
                                            insertCount = db.syncDistricts(jsonArray);
                                            Log.d(TAG, "onChanged: " + tableName + " " + workInfo.getOutputData().getInt("position", 0));
                                            break;
                                        case Clusters.TableClusters.TABLE_NAME:
                                            jsonArray = new JSONArray(result);
                                            insertCount = db.syncCluster(jsonArray);
                                            Log.d(TAG, "onChanged: " + tableName + " " + workInfo.getOutputData().getInt("position", 0));
                                            break;
                                        case BLRandom.TableRandom.TABLE_NAME:
                                            jsonArray = new JSONArray(result);
                                            insertCount = db.syncBLRandom(jsonArray);
                                            Log.d(TAG, "onChanged: " + tableName + " " + workInfo.getOutputData().getInt("position", 0));
                                            break;

                                    }

                                    downloadTables.get(position).setmessage("Received: " + jsonArray.length() + ", Saved: " + insertCount);
                                    downloadTables.get(position).setstatus(insertCount == 0 ? "Unsuccessful" : "Successful");
                                    downloadTables.get(position).setstatusID(insertCount == 0 ? 1 : 3);
                                    syncListAdapter.updatesyncList(downloadTables);

//                    pd.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    downloadTables.get(position).setstatus("Failed");
                                    downloadTables.get(position).setstatusID(1);
                                    downloadTables.get(position).setmessage(result);
                                    syncListAdapter.updatesyncList(downloadTables);
                                }
                            } else {
                                downloadTables.get(position).setmessage("Received: " + result.length() + "");
                                downloadTables.get(position).setstatus("Successful");
                                downloadTables.get(position).setstatusID(3);
                                syncListAdapter.updatesyncList(downloadTables);
//                pd.show();
                            }
                        } else {
                            downloadTables.get(position).setstatus("Failed");
                            downloadTables.get(position).setstatusID(1);
                            downloadTables.get(position).setmessage("Server not found!");
                            syncListAdapter.updatesyncList(downloadTables);
//            pd.show();
                        }
                    }
                    //mTextView1.append("\n" + workInfo.getState().name());
                    if (workInfo.getState() != null &&
                            workInfo.getState() == WorkInfo.State.FAILED) {
                        String message = workInfo.getOutputData().getString("error");
                        downloadTables.get(position).setstatus("Failed 1");
                        downloadTables.get(position).setstatusID(1);
                        downloadTables.get(position).setmessage(message);
                        syncListAdapter.updatesyncList(downloadTables);

                    }
                }
            }
        });
    }


    private void BeginUpload() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();

        for (int i = 0; i < uploadTables.size(); i++) {
            Data data = new Data.Builder()
                    .putString("table", uploadTables.get(i).gettableName())
                    .putInt("position", i)
                    //.putString("columns", "_id, sysdate")
                    // .putString("where", where)
                    .build();
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DataUpWorkerALL.class)
                    .addTag(String.valueOf(i))
                    .setInputData(data).build();
            workRequests.add(workRequest);

        }

        // FOR SIMULTANEOUS WORKREQUESTS (ALL TABLES DOWNLOAD AT THE SAME TIME)
        WorkManager wm = WorkManager.getInstance();
        WorkContinuation wc = wm.beginWith(workRequests);
        wc.enqueue();

        // FOR WORKREQUESTS CHAIN (ONE TABLE DOWNLOADS AT A TIME)
/*        WorkManager wm = WorkManager.getInstance();
        WorkContinuation wc = wm.beginWith(workRequests.get(0));
        for (int i=1; i < workRequests.size(); i++ ) {
            wc = wc.then(workRequests.get(i));
        }
        wc.enqueue();*/


        wc.getWorkInfosLiveData().observe(this, new Observer<List<WorkInfo>>() {


            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                Log.d(TAG, "workInfos: " + workInfos.size());
                for (WorkInfo workInfo : workInfos) {
                    Log.d(TAG, "workInfo: getState " + workInfo.getState());
                    Log.d(TAG, "workInfo: data " + workInfo.getTags());
                    Log.d(TAG, "workInfo: data " + workInfo.getOutputData().getString("data"));
                    Log.d(TAG, "workInfo: error " + workInfo.getOutputData().getString("error"));
                    Log.d(TAG, "workInfo: position " + workInfo.getOutputData().getInt("position", 0));
                }
                for (WorkInfo workInfo : workInfos) {
                    int position = workInfo.getOutputData().getInt("position", 0);
                    String tableName = uploadTables.get(position).gettableName();

                            /*String progress = workInfo.getProgress().getString("progress");
                            bi.wmError.setText("Progress: " + progress);*/

                    if (workInfo.getState() != null &&
                            workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                        String result = workInfo.getOutputData().getString("data");

                        int sSynced = 0;
                        int sDuplicate = 0;
                        StringBuilder sSyncedError = new StringBuilder();
                        JSONArray json;


                        try {
                            Log.d(TAG, "onPostExecute: " + result);
                            json = new JSONArray(result);

                            DatabaseHelper db = new DatabaseHelper(SyncActivity.this); // Database Helper

                            Method method = null;
                            for (Method method1 : db.getClass().getDeclaredMethods()) {
                                if (method1.getName().equals("insert" + tableName)) {
                                    method = method1;
                                    break;
                                }
                            }

                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonObject = new JSONObject(json.getString(i));

                                if (jsonObject.getString("status").equals("1") && jsonObject.getString("error").equals("0")) {
                                    method.invoke(db, jsonObject.getString("id"));
                                    sSynced++;
                                } else if (jsonObject.getString("status").equals("2") && jsonObject.getString("error").equals("0")) {
                                    method.invoke(db, jsonObject.getString("id"));
                                    sDuplicate++;
                                } else {
                                    sSyncedError.append("\nError: ").append(jsonObject.getString("message"));
                                }
                            }
                            Toast.makeText(SyncActivity.this, tableName + " synced: " + sSynced + "\r\n\r\n Errors: " + sSyncedError, Toast.LENGTH_SHORT).show();

                            if (sSyncedError.toString().equals("")) {
                                uploadTables.get(position).setmessage(tableName + " synced: " + sSynced + "\r\n\r\n Duplicates: " + sDuplicate + "\r\n\r\n Errors: " + sSyncedError);
                                uploadTables.get(position).setstatus("Completed");
                                uploadTables.get(position).setstatusID(3);
                                syncListAdapter.updatesyncList(uploadTables);
                            } else {
                                uploadTables.get(position).setmessage(tableName + " synced: " + sSynced + "\r\n\r\n Duplicates: " + sDuplicate + "\r\n\r\n Errors: " + sSyncedError);
                                uploadTables.get(position).setstatus("Failed 4");
                                uploadTables.get(position).setstatusID(1);
                                syncListAdapter.updatesyncList(uploadTables);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SyncActivity.this, "Sync Result:  " + result, Toast.LENGTH_SHORT).show();

                            if (result.equals("No new records to sync.")) {
                                uploadTables.get(position).setmessage(result /*+ " Open Forms" + String.format("%02d", unclosedForms.size())*/);
                                uploadTables.get(position).setstatus("Not processed");
                                uploadTables.get(position).setstatusID(4);
                                syncListAdapter.updatesyncList(uploadTables);
                            } else {
                                uploadTables.get(position).setmessage(result);
                                uploadTables.get(position).setstatus("Failed 3");
                                uploadTables.get(position).setstatusID(1);
                                syncListAdapter.updatesyncList(uploadTables);
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            uploadTables.get(position).setstatus("Failed 2");
                            uploadTables.get(position).setstatusID(1);
                            uploadTables.get(position).setmessage(e.getMessage());
                            syncListAdapter.updatesyncList(uploadTables);
                        }
                    }
                    //mTextView1.append("\n" + workInfo.getState().name());
                    if (workInfo.getState() != null &&
                            workInfo.getState() == WorkInfo.State.FAILED) {
                        String message = workInfo.getOutputData().getString("error");
                        uploadTables.get(position).setstatus("Failed 1");
                        uploadTables.get(position).setstatusID(1);
                        uploadTables.get(position).setmessage(message);
                        syncListAdapter.updatesyncList(uploadTables);

                    }
                }
            }
        });

    }


    public void uploadPhotos(View view) {
    }
}
