package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.adapters.LessonAdapter;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.Shift;

public class ShiftLessonsActivity extends AppCompatActivity {
    RecyclerView rvLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_lessons);
        bindViews();
        showShiftLessons();
    }

    private void showShiftLessons() {
        Log.i("Info", "Im here");
        ArrayList<Lesson> lessons = getShiftLessons();
        Log.i("Info", ""+lessons.toString());
        LessonAdapter lessonAdapter = new LessonAdapter(lessons);
        rvLessons.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rvLessons.setItemAnimator(new DefaultItemAnimator());
        rvLessons.setAdapter(lessonAdapter);
    }

    private ArrayList<Lesson> getShiftLessons() {
        // return lessons of a specific teacher on a specific date
        Shift s = Controller.getInstance().getCurrentShift();
        String shiftDate = s.getStartDate();
        ArrayList<Lesson> lessons = Controller.getInstance().getTeacherLessons(s.getTeacherUid());
        for (Lesson l : lessons) {
            if (!l.getStringDate().equals(shiftDate))
                lessons.remove(l);
        }
        return lessons;
    }

    private void bindViews() {
        rvLessons = findViewById(R.id.rv_teacher_shift_lessons);
    }
}