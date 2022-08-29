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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.adapters.ShiftAdapter;
import dev.jacob_ba.timetoswim.callbacks.CallbackShift;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Shift;

public class AddShiftActivity extends AppCompatActivity {
    private MaterialTextView tvDate;
    private MaterialButton btnPickDate;
    private MaterialButton btnAddShift;
    private TextInputEditText tietStartTime;
    private TextInputEditText tietEndTime;
    private RecyclerView addShiftRV;
    private Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        currentActivity = this;
        bindViews();
        setOnClicks();
    }

    private void showRecyclerView(ArrayList<Shift> shifts) {
        CallbackShift callbackShift = new CallbackShift() {
            @Override
            public void loadActivity() {
                Intent intent = new Intent(currentActivity, TeacherShiftActivity.class);
                currentActivity.startActivity(intent);
                //currentActivity.finish();
            }
        };
        ShiftAdapter shiftAdapter = new ShiftAdapter(shifts, callbackShift);
        addShiftRV.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        addShiftRV.setItemAnimator(new DefaultItemAnimator());
        addShiftRV.setAdapter(shiftAdapter);
    }

    private void bindViews() {
        tvDate = findViewById(R.id.tv_date);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnAddShift = findViewById(R.id.btn_add_shift);
        tietStartTime = findViewById(R.id.tiet_start_time);
        tietEndTime = findViewById(R.id.tiet_end_time);
        addShiftRV = findViewById(R.id.rv_addShift);
    }

    private void setOnClicks() {

        // Buttons
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDialog();
            }
        });
        btnAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "Add Shift!");
                Calendar calendar = GregorianCalendar.getInstance();
//                Log.i("Info", "" + tvDate.getText().toString());

                long dateStartInMillis = getDateInMilliseconds(tvDate.getText().toString(), tietStartTime.getText().toString());
                long dateEndInMillis = getDateInMilliseconds(tvDate.getText().toString(), tietEndTime.getText().toString());

//                calendar.setTimeInMillis(dateStartInMillis);
//                int a = calendar.get(Calendar.HOUR_OF_DAY);
//                Log.i("Info", "" + dateStartInMillis);
                Shift shift = new Shift(Controller.getInstance().getCurrentUser().getUid(), dateStartInMillis, dateEndInMillis);
                Log.i("Info", "" + shift.getStartDate());
                Log.i("Info", "" + shift.getStartTime());
                updateShifts(shift);
                showTeacherShiftsOfDate(tvDate.getText().toString());
            }
        });

        // Text inputs
        tietStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker picker =
                        new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setHour(12)
                                .setMinute(10).setTitleText("Select start time").setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                                .build();
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hour = String.format("%02d", picker.getHour());
                        String minute = String.format("%02d", 5 * Math.round(picker.getMinute() / 5));

                        tietStartTime.setText(hour + ":" + minute);

                        Log.i("Info", "Ending shift at: " + hour + ":" + minute);
                    }
                });
                picker.show(getSupportFragmentManager(), "TAG");
            }
        });
        tietEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker picker =
                        new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setHour(12)
                                .setMinute(10).setTitleText("Select end time").setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                                .build();
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hour = String.format("%02d", picker.getHour());
                        String minute = String.format("%02d", 5 * Math.round(picker.getMinute() / 5));

                        tietEndTime.setText(hour + ":" + minute);

                        Log.i("Info", "Ending shift at: " + hour + ":" + minute);

                    }
                });
                picker.show(getSupportFragmentManager(), "TAG");
            }
        });
    }

    private void updateShifts(Shift shift) {
        Controller.getInstance().addShiftToDatabase(shift);
    }

    long getDateInMilliseconds(String date, String time) {
        String strDate = date + " " + time + ":" + "00";
        LocalDateTime localDateTime = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
                showTeacherShiftsOfDate(formatted);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "TAG");
    }

    private void showTeacherShiftsOfDate(String formatted) {
        ArrayList<Shift> shiftsOnDate = new ArrayList<>();
        for (Shift shift : Controller.getInstance().getTeacherShifts(Controller.getInstance().getCurrentUser().getUid())) {
            if (isSameDate(formatted, shift.getStartDate()))
                shiftsOnDate.add(shift);
        }
        showRecyclerView(shiftsOnDate);
    }

    private boolean isSameDate(String formatted, String startDate) {
        return formatted.equals(startDate);
    }
}