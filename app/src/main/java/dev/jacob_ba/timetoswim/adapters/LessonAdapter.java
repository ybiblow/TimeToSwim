package dev.jacob_ba.timetoswim.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.GroupLesson;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.PrivateLesson;

public class LessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Lesson> lessons;

    public LessonAdapter(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LessonViewHolder lessonViewHolder = (LessonViewHolder) holder;
        Lesson lesson = getItem(position);
        Log.i("Info", "=================================================================");
        Log.i("Info", "" + lessonViewHolder.tvDate);
        lessonViewHolder.tvDate.setText(lesson.getStringDate());
        if (lesson.getClass() == PrivateLesson.class)
            lessonViewHolder.tvLessonType.setText("Private");
        if (lesson.getClass() == GroupLesson.class)
            lessonViewHolder.tvLessonType.setText("Group");
        lessonViewHolder.tvStartTime.setText(lesson.getStartTime());
        lessonViewHolder.tvEndTime.setText(lesson.getEndTime());

    }

    Lesson getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvLessonType;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private Lesson lesson;


        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvDate = itemView.findViewById(R.id.tv_lesson_date1);
//            tvLessonType = itemView.findViewById(R.id.tv_lesson_type);
//            tvStartTime = itemView.findViewById(R.id.tv_lesson_start_time1);
//            tvEndTime = itemView.findViewById(R.id.tv_end_time1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Info", "itemView clicked!");
                }
            });
        }
    }
}
