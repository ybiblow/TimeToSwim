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
import dev.jacob_ba.timetoswim.callbacks.CallbackShift;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Shift;

public class ShiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Shift> shifts;
    private CallbackShift callbackShift;

    public ShiftAdapter(ArrayList<Shift> shifts, CallbackShift callbackShift) {
        this.shifts = shifts;
        this.callbackShift = callbackShift;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_item, parent, false);
        return new ShiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShiftViewHolder shiftViewHolder = (ShiftViewHolder) holder;
        Shift shift = getItem(position);
        shiftViewHolder.date.setText("" + shift.getStartDate());
        shiftViewHolder.startTime.setText("" + shift.getStartTime());
        shiftViewHolder.endTime.setText("" + shift.getEndTime());
        shiftViewHolder.teacherName.setText("" + Controller.getInstance().getTeacher(shift.getTeacherUid()).getFullName());
        shiftViewHolder.shift = shift;

    }

    Shift getItem(int position) {
        return shifts.get(position);
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public class ShiftViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView startTime;
        private TextView endTime;
        private TextView teacherName;
        private Shift shift;


        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_shift_date);
            startTime = itemView.findViewById(R.id.tv_shift_start_time);
            endTime = itemView.findViewById(R.id.tv_shift_end_time);
            teacherName = itemView.findViewById(R.id.tv_teacher_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Info", "itemView clicked!");
                    Controller.getInstance().setCurrentShift(shift);
                    callbackShift.loadActivity();
                }
            });
        }
    }
}
