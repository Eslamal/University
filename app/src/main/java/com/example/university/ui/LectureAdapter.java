package com.example.university.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Lecture;
import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {

    private List<Lecture> lectures;
    private OnLectureClickListener listener;

    // تحديث الـ Interface ليدعم الضغط على الكارت بالكامل + تحديث الغياب
    public interface OnLectureClickListener {
        void onLectureClick(Lecture lecture);
        void onAbsenceUpdated(Lecture lecture); // الدالة الجديدة لمتتبع الغياب
    }

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
        Context context = holder.itemView.getContext();

        // 1. تعيين البيانات الأساسية للمحاضرة
        holder.tvCourseName.setText(lecture.courseName);
        holder.tvRoom.setText(lecture.room);
        holder.tvProfessor.setText(lecture.professor);
        holder.tvDay.setText(lecture.day);
        holder.tvTime.setText(lecture.time);
        holder.viewColorStrip.setBackgroundColor(lecture.color);

        // 2. تعيين بيانات متتبع الغياب
        int currentAbsence = lecture.getAbsenceCount();
        holder.tvAbsenceCount.setText(String.valueOf(currentAbsence));

        // تغيير اللون للأحمر للتحذير لو الغياب 4 مرات أو أكتر
        if (currentAbsence >= 4) {
            holder.tvAbsenceCount.setTextColor(context.getResources().getColor(R.color.red_dark));
        } else {
            holder.tvAbsenceCount.setTextColor(context.getResources().getColor(R.color.text_primary));
        }

        // 3. برمجة زرار الزيادة (+)
        holder.btnPlusAbsence.setOnClickListener(v -> {
            int newAbsence = lecture.getAbsenceCount() + 1;
            lecture.setAbsenceCount(newAbsence);
            holder.tvAbsenceCount.setText(String.valueOf(newAbsence));

            // تحديث اللون فوراً لو وصل لـ 4
            if (newAbsence >= 4) {
                holder.tvAbsenceCount.setTextColor(context.getResources().getColor(R.color.red_dark));
            }

            // إرسال التحديث للـ Activity عشان يتحفظ في الـ Room DB
            if (listener != null) {
                listener.onAbsenceUpdated(lecture);
            }
        });

        // 4. برمجة زرار النقصان (-)
        holder.btnMinusAbsence.setOnClickListener(v -> {
            int current = lecture.getAbsenceCount();
            if (current > 0) { // عشان الرقم مينزلش للسالب
                int newAbsence = current - 1;
                lecture.setAbsenceCount(newAbsence);
                holder.tvAbsenceCount.setText(String.valueOf(newAbsence));

                // إرجاع اللون للأساس لو الرقم قل عن 4
                if (newAbsence < 4) {
                    holder.tvAbsenceCount.setTextColor(context.getResources().getColor(R.color.text_primary));
                }

                // إرسال التحديث للـ Activity
                if (listener != null) {
                    listener.onAbsenceUpdated(lecture);
                }
            }
        });

        // حدث الضغط على الكارت نفسه (مثلاً للتعديل أو الحذف)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLectureClick(lecture);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lectures != null ? lectures.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvRoom, tvProfessor, tvDay, tvTime, tvAbsenceCount;
        View viewColorStrip;
        ImageButton btnPlusAbsence, btnMinusAbsence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // ربط العناصر الأساسية
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvProfessor = itemView.findViewById(R.id.tv_professor);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvTime = itemView.findViewById(R.id.tv_time);
            viewColorStrip = itemView.findViewById(R.id.view_color_strip);

            // ربط عناصر متتبع الغياب
            tvAbsenceCount = itemView.findViewById(R.id.tv_absence_count);
            btnPlusAbsence = itemView.findViewById(R.id.btn_plus_absence);
            btnMinusAbsence = itemView.findViewById(R.id.btn_minus_absence);
        }
    }
}