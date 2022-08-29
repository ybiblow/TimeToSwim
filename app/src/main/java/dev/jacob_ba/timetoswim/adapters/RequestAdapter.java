package dev.jacob_ba.timetoswim.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.GroupLesson;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.PrivateLesson;
import dev.jacob_ba.timetoswim.model.RequestLesson;
import dev.jacob_ba.timetoswim.model.Shift;

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
        RequestViewHolder requestViewHolder = (RequestViewHolder) holder;
        RequestLesson rl = getItem(position);
        requestViewHolder.tvDate.setText(rl.getStringDate());
        requestViewHolder.tvStartTime.setText(rl.getStartTime());
        requestViewHolder.tvEndTime.setText(rl.getEndTime());
        String lessonType = (rl.getLessonType() == 0) ? "PrivateLesson" : "GroupLesson";
        requestViewHolder.tvLessonType.setText(lessonType);
        requestViewHolder.position = position;
        requestViewHolder.lessonType = rl.getLessonType();
        requestViewHolder.startDate = rl.getDate();
        long addTime = (rl.getLessonType() == 0) ? 45 * 60 * 1000 : 60 * 60 * 1000;
        requestViewHolder.endDate = rl.getDate() + addTime;
        requestViewHolder.tvStudentName.setText(Controller.getInstance().getStudentDisplayName(rl.getStudentUid()));

    }

    RequestLesson getItem(int position) {
        return requests.get(position);
    }

    void removeRequestLesson(int position) {
        // removes the request from the array and from the recycler view, need to add remove from database;
        Controller.getInstance().removeRequestLessonFromDatabase(requests.get(position));
        requests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, requests.size());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private int lessonType;
        private long startDate;
        private long endDate;
        private TextView tvStudentName;
        private TextView tvDate;
        private TextView tvLessonType;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private MaterialButtonToggleGroup toggleGroup;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_request_date);
            tvLessonType = itemView.findViewById(R.id.tv_request_lesson_type);
            tvStartTime = itemView.findViewById(R.id.tv_request_lesson_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_request_lesson_end_time);
            toggleGroup = itemView.findViewById(R.id.tg_request_btns);
            tvStudentName = itemView.findViewById(R.id.tv_request_lesson_student);
            toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (checkedId == R.id.btn_accept_request) {
                        Log.i("Info", "btn_accept_request");
                        if (!isAvailable(itemView, startDate, endDate)) {
                            toggleGroup.uncheck(toggleGroup.getCheckedButtonId());
                            Log.i("Info", "Lesson is colliding with other lesson");
                            Toast.makeText(itemView.getContext(), "Lesson is colliding with other lesson", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (lessonType == 0)
                            createPrivateLesson(position);
                        if (lessonType == 1)
                            createGroupLesson(position);
                    }
                    if (checkedId == R.id.btn_deny_request)
                        Log.i("Info", "btn_deny_request");
                    removeRequestLesson(position);
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

    private boolean isAvailable(View itemView, long startDateInMillis, long endDateInMillis) {
        Shift currentShift = Controller.getInstance().getCurrentShift();
        ArrayList<Lesson> shiftLessons = Controller.getInstance().getLessonsOfShift(currentShift.getTeacherUid(), currentShift.getStartDate());
        for (Lesson lesson : shiftLessons) {
            long addTime = (lesson instanceof PrivateLesson) ? 45 * 60 * 1000 : 60 * 60 * 1000;
            long lessonStartDateMillis = lesson.getDate();
            long lessonEndDateMillis = lessonStartDateMillis + addTime;
            boolean isColliding = false;
            if (startDateInMillis <= lessonStartDateMillis && endDateInMillis <= lessonEndDateMillis)
                isColliding = true;

            if (startDateInMillis > lessonStartDateMillis && startDateInMillis < lessonEndDateMillis && endDateInMillis > lessonEndDateMillis) {
                isColliding = true;
            }

            if (startDateInMillis <= lessonStartDateMillis && endDateInMillis >= lessonEndDateMillis)
                isColliding = true;

            if (isColliding) {
                Log.i("Info", "Requested Lesson is colliding with other lesson!");
                Toast.makeText(itemView.getContext(), "Requested Lesson is colliding with other lesson", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void createGroupLesson(int position) {
        RequestLesson currentRequest = requests.get(position);
        ArrayList<String> students = new ArrayList<>();
        students.add(currentRequest.getStudentUid());
        GroupLesson gl = new GroupLesson(currentRequest.getDate(), currentRequest.getTeacherUid(), students);
        Controller.getInstance().addGroupLessonToDatabase(gl);
    }

    private void createPrivateLesson(int position) {
        RequestLesson currentRequest = requests.get(position);
        PrivateLesson pl = new PrivateLesson(currentRequest.getDate(), currentRequest.getTeacherUid(), currentRequest.getStudentUid());
        Controller.getInstance().addPrivateLessonToDatabase(pl);
    }
}
