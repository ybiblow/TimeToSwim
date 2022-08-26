package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import dev.jacob_ba.timetoswim.R;

public class TeacherShiftActivity extends AppCompatActivity {
    Button btnViewRequests;
    Button btnViewLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_shift);
        bindViews();
        setOnClicks();
    }

    private void bindViews() {
        btnViewRequests = findViewById(R.id.view_requests);
        btnViewLessons = findViewById(R.id.view_lessons);
    }

    private void setOnClicks() {
        btnViewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "btnViewRequests clicked!");
                loadActivity(HandlingRequestsActivity.class);
            }
        });
        btnViewLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "btnViewLessons clicked!");
                loadActivity(ShiftLessonsActivity.class);
            }
        });
    }

    private void loadActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        this.startActivity(intent);
//        this.finish();
    }

}