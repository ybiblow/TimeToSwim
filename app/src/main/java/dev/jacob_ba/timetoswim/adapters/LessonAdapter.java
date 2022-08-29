package dev.jacob_ba.timetoswim.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.GroupLesson;
import dev.jacob_ba.timetoswim.model.Lesson;
import dev.jacob_ba.timetoswim.model.PrivateLesson;

public class LessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Lesson> lessons;

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
        Lesson l = getItem(position);
        Log.i("Info", "" + lessonViewHolder.tvDate);

        lessonViewHolder.tvDate.setText(l.getStringDate());
        lessonViewHolder.tvLessonType.setText(l instanceof PrivateLesson ? "Private" : "Group");
        lessonViewHolder.tvStartTime.setText(l.getStartTime());
        lessonViewHolder.tvEndTime.setText(l.getEndTime());
        String sName = "";
        if (l instanceof PrivateLesson)
            sName = Controller.getInstance().getStudentDisplayName(((PrivateLesson) l).getStudentUid());
        if (l instanceof GroupLesson)
            sName = Controller.getInstance().getStudentDisplayName(((GroupLesson) l).getStudentsUids().get(0));
        lessonViewHolder.tvStudentName.setText(sName);
        lessonViewHolder.lesson = l;
        lessonViewHolder.showJoinButton();
    }

    private Lesson getItem(int position) {
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
        private TextView tvStudentName;
        private Button btnJoin;
        private Lesson lesson;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_lesson_date1);
            tvLessonType = itemView.findViewById(R.id.tv_lesson_type1);
            tvStartTime = itemView.findViewById(R.id.tv_lesson_start_time1);
            tvEndTime = itemView.findViewById(R.id.tv_lesson_end_time1);
            tvStudentName = itemView.findViewById(R.id.tv_lesson_student_name);
            btnJoin = itemView.findViewById(R.id.tv_lesson_join_group_lesson);
            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Info", "something something!");
                    ((GroupLesson) lesson).getStudentsUids().add(Controller.getInstance().getCurrentStudent().getUid());
                    Controller.getInstance().updateLessonsInDatabase();
                    btnJoin.setVisibility(View.GONE);
                }
            });
        }

        public void showJoinButton() {
            if (Controller.getInstance().getCurrentTeacher() != null)
                return;
            Controller.getInstance().getCurrentStudent().getUid();
            if (lesson instanceof PrivateLesson)
                btnJoin.setVisibility(View.GONE);
            if (lesson instanceof GroupLesson) {
                for (String studentUid : ((GroupLesson) lesson).getStudentsUids()) {
                    if (studentUid.equals(Controller.getInstance().getCurrentStudent().getUid()))
                        return;
                }
                btnJoin.setVisibility(View.VISIBLE);
            }
        }
    }
}
