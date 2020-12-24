package by.bsuir.wildboom.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Repository repository;
    ArrayList<TimerInfo> timers = new ArrayList<>();
    TextView textView;
    ListView timersList;
    ImageView addImageView;
    TimerInfoListAdapter timerInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new Repository(this);
        timers = repository.setTimers();

        textView = findViewById(R.id.test_text);
        textView.setText("");
        timersList = findViewById(R.id.list_timer_sets);
        addImageView = findViewById(R.id.image_add_timer);

        timerInfoListAdapter = new TimerInfoListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(timerInfoListAdapter);
        timersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                TimerInfo localTimerInfo = timers.get(position);
                intent.putExtra(TimerInfo.class.getSimpleName(), localTimerInfo);
                startActivity(intent);
            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateUpdateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        timers = repository.setTimers();
        TimerInfoListAdapter newTimerInfoListAdapter = new TimerInfoListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(newTimerInfoListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}