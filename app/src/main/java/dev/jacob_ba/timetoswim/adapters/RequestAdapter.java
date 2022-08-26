package dev.jacob_ba.timetoswim.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.RequestLesson;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RequestLesson> requests;

    public RequestAdapter(ArrayList<RequestLesson> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvLessonType;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private Lesson lesson;
        private MaterialButtonToggleGroup toggleGroup;


        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_request_date);
            tvLessonType = itemView.findViewById(R.id.tv_request_lesson_type);
            tvStartTime = itemView.findViewById(R.id.tv_request_lesson_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_request_lesson_end_time);
            toggleGroup = itemView.findViewById(R.id.tg_request_btns);
            toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (checkedId == R.id.btn_accept_request)
                        Log.i("Info", "btn_accept_request");
                    if (checkedId == R.id.btn_deny_request)
                        Log.i("Info", "btn_deny_request");
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Info", "itemView clicked!");
                }
            });
        }
    }
}
