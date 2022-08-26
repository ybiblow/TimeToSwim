package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.GroupLesson;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.PrivateLesson;
import dev.jacob_ba.timetoswim.model.RequestLesson;
import dev.jacob_ba.timetoswim.model.Shift;
import dev.jacob_ba.timetoswim.model.Student;

public class RequestLessonActivity extends AppCompatActivity {
    private TextView lessonDate;
    private TextView lessonStartTime;
    private TextView lessonEndTime;
    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton btnSetStartTime;
    private MaterialButton btnRequestLesson;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lesson);
        bindViews();
        setOnClicks();
        lessonDate.setText(Controller.getInstance().getCurrentShift().getStartDate());
        student = Controller.getInstance().getCurrentStudent();
    }

    private void setOnClicks() {
        // Buttons
        btnSetStartTime.setOnClickListener(new View.OnClickListener() {
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
                        lessonStartTime.setText(hour + ":" + minute);
                        if (toggleGroup.getCheckedButtonId() == R.id.lesson_request_private_button)
                            setEndTime(45);
                        else
                            setEndTime(60);
                    }
                });
                picker.show(getSupportFragmentManager(), "TAG");
            }
        });
        btnRequestLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Making a lesson request
                Log.i("Info", "btnRequestLesson clicked!");
                RequestLesson rl = null;
                Shift shift = Controller.getInstance().getCurrentShift();
                String strDate = lessonDate.getText().toString();
                String startTime = lessonStartTime.getText().toString();
                String teacherUid = shift.getTeacherUid();
                long dateStartInMillis = getDateInMilliseconds(strDate, startTime);
                String lessonType = getLessonType();
                rl = new RequestLesson(dateStartInMillis, teacherUid, student.getUid(), lessonType, 0);
                updateRequestLessons(rl);
                Toast.makeText(RequestLessonActivity.this, "Your request has been submitted!", Toast.LENGTH_SHORT).show();
                loadNewActivity();
            }
        });
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (!isChecked)
                    return;
                Log.i("Info", "checkedId=" + checkedId);
                if (checkedId == R.id.lesson_request_private_button) {
                    setEndTime(45);
                }
                if (checkedId == R.id.lesson_request_group_button) {
                    setEndTime(60);
                }
            }
        });
    }

    private String getLessonType() {
        int id = toggleGroup.getCheckedButtonId();
        if (id == R.id.lesson_request_private_button)
            return PrivateLesson.class.getSimpleName();
        if (id == R.id.lesson_request_group_button)
            return GroupLesson.class.getSimpleName();
        return null;
    }

    private void setEndTime(int c) {
        String time = lessonStartTime.getText().toString();
        String timeArr[] = time.split(":");
        int minute = (Integer.parseInt(timeArr[1]) + c) % 60;
        int hour = Integer.parseInt(timeArr[0]) + (Integer.parseInt(timeArr[1]) + c) / 60;
        lessonEndTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
    }

    private void loadNewActivity() {
        Intent intent = new Intent(this, StudentShiftActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void updateRequestLessons(RequestLesson rl) {
        // Add request lesson to database
        Controller.getInstance().addRequestLessonToDatabase(rl);
    }

    long getDateInMilliseconds(String date, String time) {
        String strDate = date + " " + time + ":" + "00";
        LocalDateTime localDateTime = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private void bindViews() {
        lessonDate = findViewById(R.id.tv_lesson_date);
        lessonStartTime = findViewById(R.id.tv_lesson_start_time);
        lessonEndTime = findViewById(R.id.tv_lesson_end_time);
        btnSetStartTime = findViewById(R.id.btn_set_start_time);
        btnRequestLesson = findViewById(R.id.btn_request_lesson);
        toggleGroup = findViewById(R.id.btn_toggle_group);
    }
}