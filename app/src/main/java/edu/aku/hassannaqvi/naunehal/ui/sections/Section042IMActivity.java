package edu.aku.hassannaqvi.naunehal.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edittextpicker.aliazaz.EditTextPicker;
import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.aku.hassannaqvi.naunehal.R;
import edu.aku.hassannaqvi.naunehal.contracts.IMContract;
import edu.aku.hassannaqvi.naunehal.core.MainApp;
import edu.aku.hassannaqvi.naunehal.database.DatabaseHelper;
import edu.aku.hassannaqvi.naunehal.databinding.ActivitySection042imBinding;

import static edu.aku.hassannaqvi.naunehal.core.MainApp.immunization;

public class Section042IMActivity extends AppCompatActivity {

    ActivitySection042imBinding bi;
    boolean imFlag = false, daysFlag = true;
    boolean im07;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_042im);
        //setTitle(R.string.im05title1);
        setupListeners();
        setupTextWatchers();

    }

    private LocalDate getLocalDate(EditTextPicker[] editTextsArray) {
        if (editTextsArray.length < 3) return null;
        EditTextPicker editTextPicker01 = editTextsArray[0];
        EditTextPicker editTextPicker02 = editTextsArray[1];
        EditTextPicker editTextPicker03 = editTextsArray[2];
        if (TextUtils.isEmpty(editTextPicker01.getText()) || TextUtils.isEmpty(editTextPicker02.getText()) || TextUtils.isEmpty(editTextPicker03.getText()))
            return null;
        String txt01 = editTextPicker01.getText().toString();
        if (txt01.trim().equals("44") || txt01.trim().equals("97") || txt01.trim().equals("66") || txt01.trim().equals("88"))
            return null;
        try {
            String txt02 = editTextPicker02.getText().toString();
            String txt03 = editTextPicker03.getText().toString();
            Instant instant = Instant.parse(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MM-yyyy").parse(
                    txt01 + "-" + txt02 + "-" + txt03
            )) + "T06:24:01Z");
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void clearEditTextFields(ArrayList<EditTextPicker[]> respArrayList) {
        if (respArrayList == null) return;
        for (EditTextPicker[] editItems : respArrayList) {
            for (EditTextPicker items : editItems) {
                items.setText(null);
                items.setError(null);
            }
        }
    }

    private void setupTextWatchers() {

        EditTextPicker[] BCG = new EditTextPicker[]{bi.im0501dd, bi.im0501mm, bi.im0501yy};
        editTextImplementation("BCG", BCG, null, null);

        EditTextPicker[] OPV0 = new EditTextPicker[]{bi.im0502dd, bi.im0502mm, bi.im0502yy};
        EditTextPicker[] OPV1 = new EditTextPicker[]{bi.im0503dd, bi.im0503mm, bi.im0503yy};
        EditTextPicker[] OPV2 = new EditTextPicker[]{bi.im0507dd, bi.im0507mm, bi.im0507yy};
        EditTextPicker[] OPV3 = new EditTextPicker[]{bi.im0511dd, bi.im0511mm, bi.im0511yy};
        editTextImplementation("OPV0", OPV0, null, new ArrayList<EditTextPicker[]>() {
            {
                add(OPV1);
                add(OPV2);
                add(OPV3);
            }
        });
        editTextImplementation("OPV1", OPV1, OPV0, null);
        editTextImplementation("OPV2", OPV2, OPV1, null);
        editTextImplementation("OPV3", OPV3, OPV2, null);

        EditTextPicker[] PENTA1 = new EditTextPicker[]{bi.im0504dd, bi.im0504mm, bi.im0504yy};
        EditTextPicker[] PENTA2 = new EditTextPicker[]{bi.im0508dd, bi.im0508mm, bi.im0508yy};
        EditTextPicker[] PENTA3 = new EditTextPicker[]{bi.im0512dd, bi.im0512mm, bi.im0512yy};
        editTextImplementation("PENTA1", PENTA1, null, new ArrayList<EditTextPicker[]>() {
            {
                add(PENTA2);
                add(PENTA3);
            }
        });
        editTextImplementation("PENTA2", PENTA2, PENTA1, null);
        editTextImplementation("PENTA3", PENTA3, PENTA2, null);

        EditTextPicker[] PCV1 = new EditTextPicker[]{bi.im0505dd, bi.im0505mm, bi.im0505yy};
        EditTextPicker[] PCV2 = new EditTextPicker[]{bi.im0509dd, bi.im0509mm, bi.im0509yy};
        EditTextPicker[] PCV3 = new EditTextPicker[]{bi.im0513dd, bi.im0513mm, bi.im0513yy};
        editTextImplementation("PCV1", PCV1, null, new ArrayList<EditTextPicker[]>() {
            {
                add(PCV2);
                add(PCV3);
            }
        });
        editTextImplementation("PCV2", PCV2, PCV1, null);
        editTextImplementation("PCV3", PCV3, PCV2, null);

        EditTextPicker[] RV1 = new EditTextPicker[]{bi.im0506dd, bi.im0506mm, bi.im0506yy};
        EditTextPicker[] RV2 = new EditTextPicker[]{bi.im0510dd, bi.im0510mm, bi.im0510yy};
        editTextImplementation("RV1", RV1, null, new ArrayList<EditTextPicker[]>() {
            {
                add(RV2);
            }
        });
        editTextImplementation("RV2", RV2, RV1, null);

        EditTextPicker[] IPV = new EditTextPicker[]{bi.im0514dd, bi.im0514mm, bi.im0514yy};
        editTextImplementation("IPV", IPV, null, null);

        EditTextPicker[] MEASLES1 = new EditTextPicker[]{bi.im0515dd, bi.im0515mm, bi.im0515yy};
        EditTextPicker[] MEASLES2 = new EditTextPicker[]{bi.im0516dd, bi.im0516mm, bi.im0516yy};
        editTextImplementation("MEASLES1", MEASLES1, null, new ArrayList<EditTextPicker[]>() {
            {
                add(MEASLES2);
            }
        });
        editTextImplementation("MEASLES2", MEASLES2, MEASLES1, null);
    }

    public void editTextImplementation(String type, EditTextPicker[] editTextsArray, EditTextPicker[] respTextsArray, ArrayList<EditTextPicker[]> respArrayList) {
        if (editTextsArray.length != 3) return;
        EditTextPicker editTextPicker01 = editTextsArray[0];
        EditTextPicker editTextPicker02 = editTextsArray[1];
        EditTextPicker editTextPicker03 = editTextsArray[2];

       /* if (child.getCalculatedDOB() != null) {
            int minYears = child.getCalculatedDOB().getYear();
            int maxYears = child.getLocalDate().getYear();
            editTextPicker03.setMinvalue(minYears);
            editTextPicker03.setMaxvalue(maxYears);
        }*/

        editTextPicker01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPicker03.setText(null);
                editTextPicker03.setError(null);

                editTextPicker02.setEnabled(true);
                editTextPicker03.setEnabled(true);

                daysFlag = true;
                imFlag = true;

                String txt01;
                if (!TextUtils.isEmpty(editTextPicker01.getText())) {
                    txt01 = editTextPicker01.getText().toString();

                    if (txt01.trim().equals("44") || txt01.trim().equals("97") || txt01.trim().equals("66") || txt01.trim().equals("88")) {
                        editTextPicker02.setText(null);
                        editTextPicker03.setText(null);
                        editTextPicker02.setEnabled(false);
                        editTextPicker03.setEnabled(false);
                        editTextPicker01.setRangedefaultvalue(Float.parseFloat(txt01));

                        daysFlag = false;
                        imFlag = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPicker02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPicker03.setText(null);
                editTextPicker03.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*editTextPicker03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String txt01, txt02, txt03;
                    LocalDate respLocalDate = null;
                    if (!daysFlag) return;
                    editTextPicker01.setEnabled(true);
                    editTextPicker02.setEnabled(true);
                    if (!TextUtils.isEmpty(editTextPicker01.getText()) && !TextUtils.isEmpty(editTextPicker02.getText()) && !TextUtils.isEmpty(editTextPicker03.getText())) {
                        txt01 = editTextPicker01.getText().toString();
                        txt02 = editTextPicker02.getText().toString();
                        txt03 = editTextPicker03.getText().toString();
                    } else return;
                    clearEditTextFields(respArrayList);
                    if (respTextsArray != null) {
                        respLocalDate = getLocalDate(respTextsArray);
                        int minYears, maxYears;
                        if (respLocalDate != null) {
                            minYears = respLocalDate.getYear();
                        } else {
                            minYears = child.getCalculatedDOB().getYear();
                        }
                        maxYears = child.getLocalDate().getYear();
                        editTextPicker03.setMinvalue(minYears);
                        editTextPicker03.setMaxvalue(maxYears);
                    }
                    if ((!editTextPicker01.isRangeTextValidate() || txt01.trim().equals("44") || txt01.trim().equals("97") || txt01.trim().equals("66") || txt01.trim().equals("88")) ||
                            (!editTextPicker02.isRangeTextValidate()) ||
                            (!editTextPicker03.isRangeTextValidate()))
                        return;
                    int day = Integer.parseInt(txt01);
                    int month = Integer.parseInt(txt02);
                    int year = Integer.parseInt(txt03);

                    AgeModel age;
                    age = DateRepository.Companion.getCalculatedAge(child.getLocalDate(), Integer.parseInt(editTextsArray[2].getText().toString()), Integer.parseInt(editTextsArray[1].getText().toString()), Integer.parseInt(editTextsArray[0].getText().toString()));
                    if (age == null) {
                        editTextPicker03.setError("Invalid date");
                        imFlag = false;
                        return;
                    }
                    if (respLocalDate != null) {
                        age = DateRepository.Companion.getCalculatedAge(Objects.requireNonNull(getLocalDate(editTextsArray)), Integer.parseInt(respTextsArray[2].getText().toString()), Integer.parseInt(respTextsArray[1].getText().toString()), Integer.parseInt(respTextsArray[0].getText().toString()), false);
                    } else if (child.getCalculatedDOB() != null)
                        age = DateRepository.Companion.getCalculatedAge(Objects.requireNonNull(getLocalDate(editTextsArray)), child.getCalculatedDOB().getYear(), child.getCalculatedDOB().getMonthValue(), child.getCalculatedDOB().getDayOfMonth(), type.equals("BCG") || type.equals("OPV0"));
                    else
                        age = DateRepository.Companion.getCalculatedAge(year, month, day);
                    if (age == null) {
                        editTextPicker03.setError("Invalid date");
                        imFlag = false;
                    } else {
                        imFlag = true;
                        editTextPicker01.setEnabled(false);
                        editTextPicker02.setEnabled(false);
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    private void setupListeners() {

       /* boolean flag = getIntent().getBooleanExtra(IM01FLAG, true);
        if (!flag) imFlag = true;
        Clear.clearAllFields(bi.fldGrpSecChc2, flag);

        if (!flag) {
            bi.imsub.setText(getResources().getString(R.string.im05title3));
            bi.scrollSecCD.post(() -> bi.scrollSecCD.smoothScrollTo(0, bi.fldGrpCVim08.getTop()));
        }*/

        bi.im07.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im0701.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim08, false);
                Clear.clearAllFields(bi.fldGrpSecChc3, false);
                Clear.clearAllFields(bi.fldGrpCVim23, true);
                Clear.clearAllFields(bi.fldGrpCVim23a, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim08, true);
                Clear.clearAllFields(bi.fldGrpSecChc3, true);
                Clear.clearAllFields(bi.fldGrpCVim23, false);
                Clear.clearAllFields(bi.fldGrpCVim23a, false);
            }

        });

        bi.im08.setOnCheckedChangeListener(((radioGroup, i) -> {

            if (i == bi.im0801.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim09, true);
                Clear.clearAllFields(bi.fldGrpCVim10, true);
                Clear.clearAllFields(bi.fldGrpSecChc5, true);
                Clear.clearAllFields(bi.fldGrpSecChc6, true);
                Clear.clearAllFields(bi.fldGrpCVim23, true);
                Clear.clearAllFields(bi.fldGrpCVim23a, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim09, false);
                Clear.clearAllFields(bi.fldGrpCVim10, false);
                Clear.clearAllFields(bi.fldGrpSecChc5, false);
                Clear.clearAllFields(bi.fldGrpSecChc6, false);
                Clear.clearAllFields(bi.fldGrpCVim23, false);
                Clear.clearAllFields(bi.fldGrpCVim23a, false);
                Clear.clearAllFields(bi.fldGrpCVim24, true);
            }

        }));

        bi.im10.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im1001.getId()) {
                Clear.clearAllFields(bi.fldGrpSecChc5, true);
            } else {
                Clear.clearAllFields(bi.fldGrpSecChc5, false);
            }

        });

        bi.im14.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im1401.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim15, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim15, false);
            }
        });

        bi.im16.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im1601.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim17, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim17, false);
            }
        });

        bi.im18.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im1801.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim19, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim19, false);
            }
        });

        bi.im21.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == bi.im2101.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim22, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim22, false);
            }
        });

        bi.im23.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i != bi.im2303.getId()) {
                Clear.clearAllFields(bi.fldGrpCVim24, false);
                Clear.clearAllFields(bi.fldGrpCVim23a, true);
            } else {
                Clear.clearAllFields(bi.fldGrpCVim23a, false);
                Clear.clearAllFields(bi.fldGrpCVim24, true);
            }
        });

        bi.im2498.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im24check, false);
                bi.im24check.setTag("-1");
                bi.im2498.setTag("0");
            } else {
                Clear.clearAllFields(bi.im24check, true);
                bi.im24check.setTag("0");
                bi.im2498.setTag("-1");
            }
        });

        bi.im1298.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im12, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im12, true);
            }
        });

        bi.im12a98.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im12a, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im12a, true);
            }
        });

        bi.im1598.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im15, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im15, true);
            }
        });

        bi.im1798.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im17, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im17, true);
            }
        });

        bi.im1998.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im19, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im19, true);
            }
        });

        bi.im2298.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Clear.clearAllFields(bi.im22, false);
            } else {
                if (im07) return;
                Clear.clearAllFields(bi.im22, true);
            }
        });


        //imo7 Check
        /*im07 = getIntent().getBooleanExtra(IM02CARDSEEN, false);
        if (im07) {
            Clear.clearAllFields(bi.fldGrpCVim07, false);
            bi.im071.setChecked(true);
        }*/

    }

    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesIM(IMContract.IMTable.COLUMN_SIM, immunization.s04IMtoString());
        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("im0501dd", bi.im0501dd.getText().toString());
        json.put("im0501mm", bi.im0501mm.getText().toString());
        json.put("im0501yy", bi.im0501yy.getText().toString());
        json.put("im0502dd", bi.im0502dd.getText().toString());
        json.put("im0502mm", bi.im0502mm.getText().toString());
        json.put("im0502yy", bi.im0502yy.getText().toString());
        json.put("im0503dd", bi.im0503dd.getText().toString());
        json.put("im0503mm", bi.im0503mm.getText().toString());
        json.put("im0503yy", bi.im0503yy.getText().toString());
        json.put("im0504dd", bi.im0504dd.getText().toString());
        json.put("im0504mm", bi.im0504mm.getText().toString());
        json.put("im0504yy", bi.im0504yy.getText().toString());
        json.put("im0505dd", bi.im0505dd.getText().toString());
        json.put("im0505mm", bi.im0505mm.getText().toString());
        json.put("im0505yy", bi.im0505yy.getText().toString());
        json.put("im0506dd", bi.im0506dd.getText().toString());
        json.put("im0506mm", bi.im0506mm.getText().toString());
        json.put("im0506yy", bi.im0506yy.getText().toString());
        json.put("im0507dd", bi.im0507dd.getText().toString());
        json.put("im0507mm", bi.im0507mm.getText().toString());
        json.put("im0507yy", bi.im0507yy.getText().toString());
        json.put("im0508dd", bi.im0508dd.getText().toString());
        json.put("im0508mm", bi.im0508mm.getText().toString());
        json.put("im0508yy", bi.im0508yy.getText().toString());
        json.put("im0509dd", bi.im0509dd.getText().toString());
        json.put("im0509mm", bi.im0509mm.getText().toString());
        json.put("im0509yy", bi.im0509yy.getText().toString());
        json.put("im0510dd", bi.im0510dd.getText().toString());
        json.put("im0510mm", bi.im0510mm.getText().toString());
        json.put("im0510yy", bi.im0510yy.getText().toString());
        json.put("im0511dd", bi.im0511dd.getText().toString());
        json.put("im0511mm", bi.im0511mm.getText().toString());
        json.put("im0511yy", bi.im0511yy.getText().toString());
        json.put("im0512dd", bi.im0512dd.getText().toString());
        json.put("im0512mm", bi.im0512mm.getText().toString());
        json.put("im0512yy", bi.im0512yy.getText().toString());
        json.put("im0513dd", bi.im0513dd.getText().toString());
        json.put("im0513mm", bi.im0513mm.getText().toString());
        json.put("im0513yy", bi.im0513yy.getText().toString());
        json.put("im0514dd", bi.im0514dd.getText().toString());
        json.put("im0514mm", bi.im0514mm.getText().toString());
        json.put("im0514yy", bi.im0514yy.getText().toString());
        json.put("im0515dd", bi.im0515dd.getText().toString());
        json.put("im0515mm", bi.im0515mm.getText().toString());
        json.put("im0515yy", bi.im0515yy.getText().toString());
        json.put("im0516dd", bi.im0516dd.getText().toString());
        json.put("im0516mm", bi.im0516mm.getText().toString());
        json.put("im0516yy", bi.im0516yy.getText().toString());
       /* json.put("im06",
                bi.im061.isChecked() ? "1" :
                        bi.im062.isChecked() ? "2" :
                                bi.im063.isChecked() ? "98" :
                                        "0");*/
        json.put("im07",
                bi.im0701.isChecked() ? "1" :
                        bi.im0702.isChecked() ? "2" :
                                //bi.im0703.isChecked() ? "98" :
                                "0");
        json.put("im08",
                bi.im0801.isChecked() ? "1" :
                        bi.im0802.isChecked() ? "2" :
                                bi.im0803.isChecked() ? "98" :
                                        "0");
        json.put("im09",
                bi.im0901.isChecked() ? "1" :
                        bi.im0902.isChecked() ? "2" :
                                bi.im0903.isChecked() ? "98" :
                                        "0");
        json.put("im10",
                bi.im1001.isChecked() ? "1" :
                        bi.im1002.isChecked() ? "2" :
                                bi.im1003.isChecked() ? "98" :
                                        "0");
        json.put("im11",
                bi.im1101.isChecked() ? "1" :
                        bi.im1102.isChecked() ? "2" :
                                bi.im1103.isChecked() ? "98" :
                                        "0");
        json.put("im12", bi.im12.getText().toString());
        json.put("im12dk", bi.im1298.isChecked() ? "98" : "0");

        json.put("im12a", bi.im12a.getText().toString());
        json.put("im12d", bi.im12a98.isChecked() ? "98" : "0");

/*        json.put("im13",
                bi.im131.isChecked() ? "1" :
                        bi.im132.isChecked() ? "2" :
                                bi.im133.isChecked() ? "98" :
                                        "0");*/
        json.put("im14",
                bi.im1401.isChecked() ? "1" :
                        bi.im1402.isChecked() ? "2" :
                                bi.im1403.isChecked() ? "98" :
                                        "0");
        json.put("im15", bi.im15.getText().toString());
        json.put("im15dk", bi.im1598.isChecked() ? "98" : "0");

        json.put("im16",
                bi.im1601.isChecked() ? "1" :
                        bi.im1602.isChecked() ? "2" :
                                bi.im1603.isChecked() ? "98" :
                                        "0");
        json.put("im17", bi.im17.getText().toString());
        json.put("im17dk", bi.im1798.isChecked() ? "98" : "0");

        json.put("im18", bi.im1801.isChecked() ? "1"
                : bi.im1802.isChecked() ? "2"
                : bi.im1803.isChecked() ? "98"
                : "0");
        json.put("im19", bi.im19.getText().toString());
        json.put("im19dk", bi.im1998.isChecked() ? "98" : "0");

        json.put("im20", bi.im2001.isChecked() ? "1"
                : bi.im2002.isChecked() ? "2"
                : bi.im2003.isChecked() ? "98"
                : "0");

        json.put("im21", bi.im2101.isChecked() ? "1"
                : bi.im2102.isChecked() ? "2"
                : bi.im2103.isChecked() ? "98"
                : "0");
        json.put("im22", bi.im22.getText().toString());
        json.put("im22dk", bi.im2298.isChecked() ? "98" : "0");

        json.put("im23", bi.im2301.isChecked() ? "1"
                : bi.im2302.isChecked() ? "2"
                : bi.im2303.isChecked() ? "3"
                //: bi.im2304.isChecked() ? "4"
                : bi.im2306.isChecked() ? "6"
                : "0");
        json.put("im236x", bi.im2306x.getText().toString());

        json.put("im23a", bi.im23a1.isChecked() ? "1"
                : bi.im23a2.isChecked() ? "2"
                : bi.im23a3.isChecked() ? "3"
                : bi.im23a96.isChecked() ? "96"
                : "0");
        json.put("im23a96x", bi.im23a96x.getText().toString());

        json.put("im2401", bi.im2401.isChecked() ? "1" : "0");
        json.put("im2402", bi.im2402.isChecked() ? "2" : "0");
        json.put("im2403", bi.im2403.isChecked() ? "3" : "0");
        json.put("im2404", bi.im2404.isChecked() ? "4" : "0");
        json.put("im2405", bi.im2405.isChecked() ? "5" : "0");
        json.put("im2406", bi.im2406.isChecked() ? "6" : "0");
        json.put("im2407", bi.im2407.isChecked() ? "7" : "0");
        json.put("im2408", bi.im2408.isChecked() ? "8" : "0");
        json.put("im2409", bi.im2409.isChecked() ? "9" : "0");
        json.put("im2410", bi.im2410.isChecked() ? "10" : "0");
        json.put("im2411", bi.im2411.isChecked() ? "11" : "0");
        json.put("im2412", bi.im2412.isChecked() ? "12" : "0");
        json.put("im2413", bi.im2413.isChecked() ? "13" : "0");
        json.put("im2414", bi.im2414.isChecked() ? "14" : "0");
        json.put("im2415", bi.im2415.isChecked() ? "15" : "0");
        json.put("im2416", bi.im2416.isChecked() ? "16" : "0");
        json.put("im2417", bi.im2417.isChecked() ? "96" : "0");
        json.put("im2498", bi.im2498.isChecked() ? "98" : "0");
        json.put("im2417x", bi.im2417x.getText().toString());


       /* try {
            JSONObject json_merge = JSONUtils.mergeJSONObjects(new JSONObject(child.getsCC()), json);

            child.setsCC(String.valueOf(json_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    private boolean formValidation() {
        if (!imFlag) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return Validator.emptyCheckingContainer(this, bi.fldGrpSectionCHD);
    }

    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (UpdateDB()) {
            finish();
            startActivity(new Intent(this, Section043IMActivity.class));
        } else {
            Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
        }
    }

    public void BtnEnd() {
        /* openChildEndActivity(this);*/
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Press Not Allowed", Toast.LENGTH_SHORT).show();
    }

    public void showTooltip(@NotNull View view) {
        if (view.getId() != View.NO_ID) {
            String package_name = getApplicationContext().getPackageName();

            // Question Number Textview ID must be prefixed with q_ e.g.: 'q_aa12a'
            String infoid = view.getResources().getResourceName(view.getId()).replace(package_name + ":id/q_", "");

            // Question info text must be suffixed with _info e.g.: aa12a_info
            int stringRes = this.getResources().getIdentifier(infoid + "_info", "string", getApplicationContext().getPackageName());

            // Fetch info text from strings.xml
            //String infoText = (String) getResources().getText(stringRes);

            // Check if string resource exists to avoid crash on missing info string
            if (stringRes != 0) {

                // Fetch info text from strings.xml
                String infoText = (String) getResources().getText(stringRes);

                new AlertDialog.Builder(this)
                        .setTitle("Info: " + infoid.toUpperCase())
                        .setMessage(infoText)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            } else {
                Toast.makeText(this, "No information available on this question.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No ID Associated with this question.", Toast.LENGTH_SHORT).show();

        }
    }

}
