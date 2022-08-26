package dev.jacob_ba.timetoswim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.adapters.RequestAdapter;
import dev.jacob_ba.timetoswim.adapters.ShiftAdapter;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.RequestLesson;

public class HandlingRequestsActivity extends AppCompatActivity {
    private RecyclerView rvRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handling_requests);
        bindViews();
        showRecyclerView();
    }

    private void showRecyclerView() {
        Log.i("Info", "=====im here=====");
        ArrayList<RequestLesson> requests = getRelevantRequests();
        RequestAdapter requestAdapter = new RequestAdapter(requests);
        rvRequests.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rvRequests.setItemAnimator(new DefaultItemAnimator());
        rvRequests.setAdapter(requestAdapter);
    }

    private ArrayList<RequestLesson> getRelevantRequests() {
        String shiftDate = Controller.getInstance().getCurrentShift().getStartDate();
        Log.i("Info", "" + shiftDate);
        ArrayList<RequestLesson> requests = Controller.getInstance().getLessonRequestsOfDate(shiftDate);
        return requests;
    }

    private void bindViews() {
        rvRequests = findViewById(R.id.rv_requests);
    }
}