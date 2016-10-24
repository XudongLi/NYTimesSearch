package com.lixudong.android.nytarticlesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.lixudong.android.nytarticlesearch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditFilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATEPICKER_TAG = "datepicker";

    EditText etDate;
    SimpleDateFormat dateFormatter;
    String datePicked;
    Spinner spSortOrder;
    String sortOrderPicked;
    Map<String, Boolean> newsDeskPicked = new HashMap<String, Boolean>(){{
        put("arts", false);
        put("fashion", false);
        put("sports", false);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_filter);
        setupViews();
    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        });
        etDate = (EditText) findViewById(R.id.etDate);

        setupDatePickerDialog();
        setupSpinner();
        setupCheckBox();
    }

    private void setupDatePickerDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
    }

    private void setupSpinner() {
        spSortOrder = (Spinner) findViewById(R.id.spSortOrder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_order_array,
                android.R.layout.simple_spinner_item);
        spSortOrder.setAdapter(adapter);

        spSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortOrderPicked = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), sortOrderPicked, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupCheckBox() {
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                CheckBox checkBox = (CheckBox) view;
                switch(view.getId()) {
                    case R.id.checkbox_arts:
                        if (checkBox.isChecked()) {
                            newsDeskPicked.put("arts", true);
                        } else {
                            newsDeskPicked.put("arts", false);
                        }
                        break;
                    case R.id.checkbox_fashion:
                        if (checkBox.isChecked()) {
                            newsDeskPicked.put("fashion", true);
                        } else {
                            newsDeskPicked.put("fashion", false);
                        }
                        break;
                    case R.id.checkbox_sports:
                        if (checkBox.isChecked()) {
                            newsDeskPicked.put("sports", true);
                        } else {
                            newsDeskPicked.put("sports", false);
                        }
                        break;
                }
            }
        };
        CheckBox arts = (CheckBox) findViewById(R.id.checkbox_arts);
        CheckBox fashion = (CheckBox) findViewById(R.id.checkbox_fashion);
        CheckBox sports = (CheckBox) findViewById(R.id.checkbox_sports);
        arts.setOnCheckedChangeListener(checkListener);
        fashion.setOnCheckedChangeListener(checkListener);
        sports.setOnCheckedChangeListener(checkListener);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        datePicked = dateFormatter.format(newDate.getTime());
        etDate.setText(datePicked);
    }
}
