package com.example.university.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;
import com.example.university.utils.WebClickListener;
import com.example.university.data.UniversityEntity;
import com.example.university.data.APIResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnivAdapter extends RecyclerView.Adapter<UnivViewHolder> {
    Context context;
    List<APIResponse> list;
    WebClickListener listener;
    private final UnivViewModel viewModel;
    private Set<String> favoriteNames = new HashSet<>();
    private List<UniversityEntity> favoriteList = new ArrayList<>();

    public UnivAdapter(Context context, List<APIResponse> list, WebClickListener listener, UnivViewModel viewModel) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.viewModel = viewModel;
    }

    public void setFavorites(List<UniversityEntity> favorites) {
        this.favoriteList = favorites;
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

        String webUrl = (university.getWebPages() != null && !university.getWebPages().isEmpty())
                ? university.getWebPages().get(0) : "";

        if (!webUrl.isEmpty()) {
            holder.textView_province.setText(webUrl);
        } else {
            holder.textView_province.setText("No web page available");
        }

        // --- 1. برمجة زرار الموقع (Web) ---
        holder.button_web.setOnClickListener(view -> {
            if (!webUrl.isEmpty()) {
                listener.OnClicked(webUrl);
            }
        });

        // --- 2. برمجة زرار المشاركة (Share) ---
        holder.btn_share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this university: " + universityName + "\n" + webUrl);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Share via"));
        });

        // --- 3. برمجة زرار الخريطة (Map) ---
        holder.btn_map.setOnClickListener(v -> {
            // بنعمل Encode للاسم عشان لو فيه مسافات أو رموز خاصة
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(universityName));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps"); // محاولة فتح خرائط جوجل

            // التأكد من وجود تطبيق لفتح الخريطة
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                // لو مفيش تطبيق خرائط، نفتح اللينك في المتصفح كبديل
                Intent browserMapIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(universityName)));
                context.startActivity(browserMapIntent);
            }
        });

        // --- 4. منطق المفضلة ---
        final boolean isFavorite = favoriteNames.contains(universityName.trim().toLowerCase());

        if (isFavorite) {
            holder.button_favorite.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.button_favorite.setImageResource(R.drawable.ic_star_border);
        }

        holder.button_favorite.setOnClickListener(v -> {
            UniversityEntity entity = new UniversityEntity(university.getName(), university.getCountry(), webUrl);
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