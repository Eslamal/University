package com.example.university.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Lecture;
import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {

    private List<Lecture> lectures;
    private OnLectureClickListener listener; // واجهة للتفاعل

    // 1. تعريف واجهة (Interface) للضغط
    public interface OnLectureClickListener {
        void onLectureClick(Lecture lecture);
    }

    // 2. تعديل الكونستركتور لاستقبال الليسنر
    public LectureAdapter(List<Lecture> lectures, OnLectureClickListener listener) {
        this.lectures = lectures;
        this.listener = listener;
    }

    public void updateList(List<Lecture> newLectures) {
        this.lectures = newLectures;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);

        holder.tvCourseName.setText(lecture.courseName);
        holder.tvRoom.setText(lecture.room);
        holder.tvProfessor.setText(lecture.professor);
        holder.tvDay.setText(lecture.day);
        holder.tvTime.setText(lecture.time);
        holder.viewColorStrip.setBackgroundColor(lecture.color);

        // 3. عند الضغط على الكارت، نرسل المحاضرة للـ Activity
        holder.itemView.setOnClickListener(v -> listener.onLectureClick(lecture));
    }

    @Override
    public int getItemCount() {
        return lectures != null ? lectures.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvRoom, tvProfessor, tvDay, tvTime;
        View viewColorStrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvProfessor = itemView.findViewById(R.id.tv_professor);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvTime = itemView.findViewById(R.id.tv_time);
            viewColorStrip = itemView.findViewById(R.id.view_color_strip);
        }
    }
}