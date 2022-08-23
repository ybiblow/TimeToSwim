package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.adapters.ShiftAdapter;
import dev.jacob_ba.timetoswim.callbacks.CallbackShift;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Shift;
import dev.jacob_ba.timetoswim.model.Student;

public class AddLessonActivity extends AppCompatActivity {
    private MaterialTextView tvDate;
    private MaterialButton btnPickDate;
    private RecyclerView rvShowShifts;
    private Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        currentActivity = this;
        bindViews();
        setOnClicks();

    }

    private void setOnClicks() {
        // Buttons
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDialog();
            }
        });
    }

    private void showDatePickDialog() {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                // get string-date from the selection
                Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utc.setTimeInMillis((long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy"); // yyyy-MM-dd
                String formatted = format.format(utc.getTime());
                Log.i("Info", "" + formatted);
                tvDate.setText("" + formatted);
                displayRelevantShifts(formatted);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "TAG");
    }

    private void displayRelevantShifts(String date) {
        // We need to display shifts that are in a specific date and the teacher has the relevant teaching style
        // requirements: 1) spesific date 2) teacher has relevant teaching style
        Student student = Controller.getInstance().getCurrentStudent();
        ArrayList<Shift> shiftsOfDateAndStyle = Controller.getInstance().getShiftsOnDateAndStyle(date, student.getSwimStyle());

        showRecyclerView(shiftsOfDateAndStyle);
    }

    private void showRecyclerView(ArrayList<Shift> shifts) {
        CallbackShift callbackShift = new CallbackShift() {
            @Override
            public void loadActivity() {
                Intent intent = new Intent(currentActivity, StudentShiftActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        };
        ShiftAdapter shiftAdapter = new ShiftAdapter(shifts, callbackShift);
        rvShowShifts.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rvShowShifts.setItemAnimator(new DefaultItemAnimator());
        rvShowShifts.setAdapter(shiftAdapter);
    }

    private void bindViews() {
        tvDate = findViewById(R.id.tv_show_date);
        btnPickDate = findViewById(R.id.btn_pick_date_1);
        rvShowShifts = findViewById(R.id.rv_show_shifts);
    }
}