package com.example.university.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.university.R;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup languageGroup, themeGroup;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        languageGroup = findViewById(R.id.language_group);
        themeGroup = findViewById(R.id.theme_group);

        setupThemeSelection();
        setupLanguageSelection();
    }

    private void setupThemeSelection() {
        int savedTheme = prefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (savedTheme == AppCompatDelegate.MODE_NIGHT_NO) themeGroup.check(R.id.radio_light);
        else if (savedTheme == AppCompatDelegate.MODE_NIGHT_YES) themeGroup.check(R.id.radio_dark);
        else themeGroup.check(R.id.radio_system);

        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int mode;
            if (checkedId == R.id.radio_light) mode = AppCompatDelegate.MODE_NIGHT_NO;
            else if (checkedId == R.id.radio_dark) mode = AppCompatDelegate.MODE_NIGHT_YES;
            else mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

            AppCompatDelegate.setDefaultNightMode(mode);
            prefs.edit().putInt("theme", mode).apply();
        });
    }

    private void setupLanguageSelection() {
        String currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        if (currentLang.equals("ar")) {
            languageGroup.check(R.id.radio_ar);
        } else if (currentLang.equals("en")) {
            languageGroup.check(R.id.radio_en);
        } else {
            languageGroup.check(R.id.radio_system_lang);
        }

        languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
            LocaleListCompat locales;

            if (checkedId == R.id.radio_ar) {
                locales = LocaleListCompat.forLanguageTags("ar");
            } else if (checkedId == R.id.radio_en) {
                locales = LocaleListCompat.forLanguageTags("en");
            } else {
                locales = LocaleListCompat.getEmptyLocaleList();
            }

            AppCompatDelegate.setApplicationLocales(locales);
        });
    }
}