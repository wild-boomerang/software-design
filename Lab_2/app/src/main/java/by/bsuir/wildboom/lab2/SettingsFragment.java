package by.bsuir.wildboom.lab2;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private IDialogUpdate updateUi;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        updateUi = (IDialogUpdate) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_preference, rootKey);
        SwitchPreferenceCompat setDarkMode = (SwitchPreferenceCompat)findPreference("set_night_mode");

        setDarkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean)newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                return true;
            }
        });

        ListPreference setLanguage = (ListPreference)findPreference("set_language");
        setLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String localVal = (String)newValue;
                if (localVal.equals("1")) {
                    Toast.makeText(requireActivity().getBaseContext(), "English", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireActivity().getBaseContext(), "Russian", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        ListPreference setFontSize = (ListPreference)findPreference("set_font_size");
        setFontSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String localVal = (String)newValue;
                if (localVal.equals("1")) {
                    updateUi.fontSizeUpdate("Small");
                } else {
                    updateUi.fontSizeUpdate("Large");
                }
                return true;
            }
        });
    }
}
