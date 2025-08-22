package com.example.university;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet; // استيراد HashSet
import java.util.List;
import java.util.Set; // استيراد Set

public class UnivAdapter extends RecyclerView.Adapter<UnivViewHolder> {
    Context context;
    List<APIResponse> list;
    WebClickListener listener;
    private final UnivViewModel viewModel;
    // هنستخدم Set للبحث السريع
    private Set<String> favoriteNames = new HashSet<>();
    // هنحتفظ بالليستة الأصلية عشان نعرف نحذف منها
    private List<UniversityEntity> favoriteList = new ArrayList<>();


    public UnivAdapter(Context context, List<APIResponse> list, WebClickListener listener, UnivViewModel viewModel) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.viewModel = viewModel;
    }

    public void setFavorites(List<UniversityEntity> favorites) {
        this.favoriteList = favorites;
        // امسح الـ Set القديم وضيف فيه أسماء الجامعات الجديدة
        this.favoriteNames.clear();
        for (UniversityEntity entity : favorites) {
            this.favoriteNames.add(entity.getName().trim().toLowerCase());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UnivViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnivViewHolder(LayoutInflater.from(context).inflate(R.layout.list_univ, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnivViewHolder holder, int position) {
        APIResponse university = list.get(position);
        String universityName = university.getName();

        holder.textView_name.setText(universityName);
        holder.textView_country.setText(university.getCountry() + " - " + university.getAlphaTwoCode());
        if (university.getWebPages() != null && !university.getWebPages().isEmpty()) {
            holder.textView_province.setText(university.getWebPages().get(0));
        } else {
            holder.textView_province.setText("No web page available");
        }
        holder.button_web.setOnClickListener(view -> {
            if (university.getWebPages() != null && !university.getWebPages().isEmpty()) {
                listener.OnClicked(university.getWebPages().get(0));
            }
        });

        // --- منطق المفضلة المحسّن ---
        // البحث الآن سريع جداً باستخدام الـ Set
        final boolean isFavorite = favoriteNames.contains(universityName.trim().toLowerCase());

        if (isFavorite) {
            holder.button_favorite.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.button_favorite.setImageResource(R.drawable.ic_star_border);
        }

        holder.button_favorite.setOnClickListener(v -> {
            String webPage = (university.getWebPages() != null && !university.getWebPages().isEmpty()) ? university.getWebPages().get(0) : "";
            // لاحظ أننا هنستخدم اسم الجامعة الأصلي وليس الـ lowercase
            UniversityEntity entity = new UniversityEntity(university.getName(), university.getCountry(), webPage);

            if (isFavorite) {
                viewModel.deleteFavorite(entity);
            } else {
                viewModel.insertFavorite(entity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}