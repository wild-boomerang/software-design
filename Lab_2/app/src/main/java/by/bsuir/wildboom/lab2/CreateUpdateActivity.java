package by.bsuir.wildboom.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class CreateUpdateActivity extends AppCompatActivity {
    private Repository repository;
    ArrayList<Item> settings = new ArrayList<>();
    EditSetAdapter adapter;
    TimerInfo timerInfo;
    EditText getTitle;
    ListView settingsList;
    TextView stringTitle;
    Button buttonSave;
    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update);

        repository = new Repository(this);
        stringTitle = (TextView) findViewById(R.id.set_title);
        getTitle = (EditText) findViewById(R.id.text_title);
        buttonSave = (Button) findViewById(R.id.button_save);

        isEdit = false;
        timerInfo = new TimerInfo(20, 30, 10, 40, 3, 2);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            timerInfo = (TimerInfo) arguments.getSerializable(TimerInfo.class.getSimpleName());
            isEdit = true;
        }

        settings = timerInfo.getSimpleList();
        getTitle.setText(timerInfo.getTitle());
        adapter = new EditSetAdapter(this, R.layout.item_add, settings);
        settingsList = (ListView) findViewById(R.id.list_update);
        settingsList.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getTitle.getText().toString();
                String warmUp = settings.get(0).length.toString();
                String workout = settings.get(1).length.toString();
                String rest = settings.get(2).length.toString();
                String cooldown = settings.get(3).length.toString();
                String cycle = settings.get(4).length.toString();
                String setCount = settings.get(5).length.toString();

                if (!isEdit) {
                    Random random = new Random();
                    String red = Integer.toString(random.nextInt(256));
                    String green = Integer.toString(random.nextInt(256));
                    String blue = Integer.toString(random.nextInt(256));
                    repository.addTimer(title, warmUp, workout, rest, cooldown, cycle, setCount,
                            red, green, blue);
                } else {
                    String id = Integer.toString(timerInfo.getId());
                    repository.editTimer(id, title, warmUp, workout, rest, cooldown, cycle, setCount);
                }
                Toast.makeText(CreateUpdateActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}