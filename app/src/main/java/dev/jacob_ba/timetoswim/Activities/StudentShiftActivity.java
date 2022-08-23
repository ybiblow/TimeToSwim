package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.adapters.LessonAdapter;
import dev.jacob_ba.timetoswim.adapters.ShiftAdapter;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.Shift;
import dev.jacob_ba.timetoswim.model.Teacher;

public class StudentShiftActivity extends AppCompatActivity {
    TextView tvTeacherName;
    TextView tvShiftDate;
    TextView tvShiftStartTime;
    TextView tvShiftEndTime;
    RecyclerView rvShiftLessons;
    ExtendedFloatingActionButton exFab;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_shift);
        activity = this;
        bindViews();
        setOnClicks();
        fillShiftValues();
        displayRecyclerView();
    }

    private void displayRecyclerView() {
        ArrayList<Lesson> lessons = getLessonsInShift();
        LessonAdapter lessonAdapter = new LessonAdapter(lessons);
        rvShiftLessons.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rvShiftLessons.setItemAnimator(new DefaultItemAnimator());
        rvShiftLessons.setAdapter(lessonAdapter);
    }

    private ArrayList<Lesson> getLessonsInShift() {
        ArrayList<Lesson> lessons = new ArrayList<>();
        // get current shift
        Shift shift = Controller.getInstance().getCurrentShift();
        // get all lessons taught by the teacher
        lessons = getTeacherLessons(shift.getTeacherUid());
        // filter lessons by date
        filerLessonsByDate(lessons, shift.getStartDate());
        return lessons;
    }

    private void filerLessonsByDate(ArrayList<Lesson> lessons, String date) {
        for (Lesson lesson : lessons) {
            if (!lesson.getStringDate().equals(date))
                lessons.remove(lesson);
        }
    }

    private ArrayList<Lesson> getTeacherLessons(String teacherUid) {
        ArrayList<Lesson> lessons = Controller.getInstance().getTeacherLessons(teacherUid);
        return lessons;
    }

    private void setOnClicks() {
        exFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RequestLessonActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    private void fillShiftValues() {
        Shift shift = Controller.getInstance().getCurrentShift();
        Teacher t = Controller.getInstance().getTeacher(shift.getTeacherUid());
        tvTeacherName.setText(t.getFullName());
        tvShiftDate.setText(shift.getStartDate());
        tvShiftStartTime.setText(shift.getStartTime());
        tvShiftEndTime.setText(shift.getEndTime());
    }

    private void bindViews() {
        tvTeacherName = findViewById(R.id.tv_teacher_name1);
        tvShiftDate = findViewById(R.id.tv_date1);
        tvShiftStartTime = findViewById(R.id.tv_shift_start_time1);
        tvShiftEndTime = findViewById(R.id.tv_shift_end_time1);
        rvShiftLessons = findViewById(R.id.rv_shift_lessons);
        exFab = findViewById(R.id.ext_fab_request_lesson);
    }
}