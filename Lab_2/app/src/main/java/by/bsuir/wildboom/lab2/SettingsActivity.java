package by.bsuir.wildboom.lab2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements IDialogInteraction, IDialogUpdate
{
    Button deleteAllButton;
    Repository repository;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        deleteAllButton = findViewById(R.id.button);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("dialog", getResources().getString(R.string.delete_all_timers_dialog));

                DeleteAllTimersDialog dialog = new DeleteAllTimersDialog();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");
            }
        });
        repository = new Repository(this);
    }

    @Override
    public void fontSizeUpdate(String name) {
        if (name.equals("Small")) {
            deleteAllButton.setTextSize(14);
        } else {
            deleteAllButton.setTextSize(25);
        }
    }

    @Override
    public void remove(String name) {
        repository.deleteAllTimers();
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String localFontSize = sharedPreferences.getString("set_font_size", "1");
        if (localFontSize.equals("1")) {
            deleteAllButton.setTextSize(14);
        } else {
            deleteAllButton.setTextSize(25);
        }
    }
}