package com.example.university.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvMessage.setText(message.getText());

        // الوصول للـ Context عشان نجيب الألوان
        android.content.Context context = holder.itemView.getContext();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvMessage.getLayoutParams();

        if (message.isSentByMe()) {
            params.gravity = Gravity.END;
            // لون رسالة المستخدم (بنفسجي فاتح/غامق حسب الثيم)
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_me);
            holder.tvMessage.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    context.getResources().getColor(R.color.accent_color)));
            // لون النص (دائماً أبيض في رسالة المستخدم)
            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            params.gravity = Gravity.START;
            // لون رسالة الـ AI (يتغير مع الدارك مود لو استخدمت @color/card_background)
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_bot);
            holder.tvMessage.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    context.getResources().getColor(R.color.card_background)));
            // لون النص (يأخذ لون النص الأساسي عشان يتغير في الدارك مود)
            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.text_primary));
        }
        holder.tvMessage.setLayoutParams(params);
    }

    public void removeLastItem() {
        if (!messages.isEmpty()) {
            int position = messages.size() - 1;
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }
}