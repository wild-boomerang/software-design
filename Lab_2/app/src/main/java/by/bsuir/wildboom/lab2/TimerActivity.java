package by.bsuir.wildboom.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    public final static String TIMER_PARAM        = TimerInfo.class.getSimpleName();
    public final static String WARM_UP_PARAM      = "warm_up";
    public final static String POS_PARAM          = "position";
    public final static String ID_TIMER_SET_PARAM = "id_timer_set";
    public final static String BROADCAST_ACTION   = "by.bsuir.wildboom.lab2.s34578servicebackbroadcast";
    public final static String VAL_PARAM          = "value";

    TimerReceiver timerReceiver;
    private String status;

    private Timer timer;
    private UpdateTimerTask updateTimerTask;
    private UpdatePrevTimerTask updatePrevTimerTask;
    private SetTimerTask setTimerTask;

    ArrayList<Item> settings = new ArrayList<>();
    ArrayList<Item> prevSettings = new ArrayList<>();
    ListView settingsList;
    TimerAdapter newAdapter;
    Item curItem;
    int curPos;
    int curVal;

    ImageView next;
    ImageView prev;
    TimerInfo timerInfo;
    ImageView playStop;
    EditText title;
    TextView serviceText;
    public EditText curTime;

    MediaPlayer mediaPlayer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = MediaPlayer.create(this, R.raw.short_sound);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
            }
        });
        status = null;
        setContentView(R.layout.activity_timer);

        timerInfo = new TimerInfo(20, 30, 10, 40, 3, 2);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            timerInfo = (TimerInfo) arguments.getSerializable(TimerInfo.class.getSimpleName());
        }
        settings = timerInfo.getList();
        curItem = settings.get(0);
        prevSettings.add(settings.get(0));
        settings.remove(0);
        curVal = curItem.length;
        curPos = 0;

        curTime = (EditText) findViewById(R.id.cur_time);
        curTime.setText(curItem.length.toString());

        title = (EditText) findViewById(R.id.text_title);
        title.setText(curItem.name);

        serviceText = (TextView) findViewById(R.id.service_text);

        playStop = (ImageView) findViewById(R.id.stop_play_image);
        playStop.setOnClickListener(onClickListener);

        next = (ImageView) findViewById(R.id.next_image);
        next.setOnClickListener(NextClickListener);

        prev = (ImageView) findViewById(R.id.prev_image);
        prev.setOnClickListener(PrevClickListener);

        newAdapter = new TimerAdapter(this, R.layout.item_show, settings);
        settingsList = (ListView) findViewById(R.id.list_create_update);
        settingsList.setAdapter(newAdapter);

        timerReceiver = new TimerReceiver(new Handler());
        registerReceiver(timerReceiver, new IntentFilter(BROADCAST_ACTION));

        curTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (curTime.getText().toString().equals("0")) {
                    if (!settings.isEmpty()) {
                        timer.cancel();
                        timer.purge();

                        updateTimerTask = new UpdateTimerTask();
                        setTimerTask = new SetTimerTask();

                        timer = new Timer();
                        timer.schedule(updateTimerTask, 1000);
                        timer.schedule(setTimerTask, 2000, 1000);
                    } else {
                        setFinish();
                    }
                    mediaStart();
                    mediaPlayer.start();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (serviceText.getText().toString().equals(getString(R.string.paused))) {
                playStop.setImageResource(R.mipmap.stop_foreground);
                if (!curTime.getText().toString().equals(getString(R.string.finish)))
                    serviceText.setText(getString(R.string.running));

                setTimerTask = new SetTimerTask();
                timer = new Timer();
                timer.schedule(setTimerTask, 1000, 1000);
            } else {
                if (!curTime.getText().toString().equals(getString(R.string.finish))) {
                    serviceText.setText(getString(R.string.paused));
                }

                playStop.setImageResource(R.mipmap.start_foreground);

                timer.cancel();
                timer.purge();
            }
        }
    };

    private View.OnClickListener NextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!settings.isEmpty()) {
                timer.cancel();
                timer.purge();

                updateTimerTask = new UpdateTimerTask();
                setTimerTask = new SetTimerTask();

                timer = new Timer();
                timer.schedule(updateTimerTask, 50);
                timer.schedule(setTimerTask, 1000, 1000);
            } else {
                setFinish();
            }
        }
    };

    private View.OnClickListener PrevClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();
            timer.purge();

            updatePrevTimerTask = new UpdatePrevTimerTask();
            setTimerTask = new SetTimerTask();

            timer = new Timer();
            timer.schedule(updatePrevTimerTask, 50);
            timer.schedule(setTimerTask, 1000, 1000);
        }
    };

    class SetTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String localString = curTime.getText().toString();
                    try {
                        int num = Integer.parseInt(localString);
                        if (num > 0) {
                            localString = Integer.toString(--num);
                        }
                        curVal = num;
                    }
                    catch (Exception ignored) {
                    }
                    curTime.setText(localString);
                }
            });
        }
    }

    class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    prevSettings.add(0, curItem);
                    curItem = settings.get(0);
                    curVal = curItem.length;
                    settings.remove(0);
                    title.setText(curItem.name);
                    curTime.setText(curItem.length.toString());
                    newAdapter.notifyDataSetChanged();
                    curPos++;
                }
            });
        }
    }

    class UpdatePrevTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    if (!prevSettings.isEmpty()) {
                        if (!curItem.name.equals("warm_up")) {
                            settings.add(0, curItem);
                        }
                        curItem = prevSettings.get(0);
                        curVal = curItem.length;
                        prevSettings.remove(0);
                        curPos--;
                    }
                    title.setText(curItem.name);
                    curTime.setText(curItem.length.toString());
                    newAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void setFinish() {
        curTime.setText(getString(R.string.finish));
        curTime.setTextSize(100);
        serviceText.setText("");
        title.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(POS_PARAM, curPos);
        serviceIntent.putExtra(TIMER_PARAM, timerInfo);
        serviceIntent.putExtra(VAL_PARAM, curVal);
        serviceIntent.putExtra(WARM_UP_PARAM, new Item("warm_up", timerInfo.warmUpTime));
        startService(serviceIntent);

        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        status = "not used";
        this.finish();
    }

    private void mediaStart() {
        mediaPlayer = MediaPlayer.create(this, R.raw.short_sound);
    }

    private class TimerReceiver extends BroadcastReceiver {
        private final Handler handler;

        public TimerReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (status == null) {
                final int id = intent.getIntExtra(ID_TIMER_SET_PARAM, 0);
                if (id == timerInfo.getId()) {
                    final int value = intent.getIntExtra(VAL_PARAM, 0);
                    final int position = intent.getIntExtra(POS_PARAM, 0);

                    for (int i = 0; i < position; i++) {
                        prevSettings.add(0, curItem);
                        curItem = settings.get(0);
                        curVal = curItem.length;
                        settings.remove(0);
                        curPos++;
                    }
                    curVal = value;
                    handler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (title != null) { title.setText(curItem.name); }
                            if (newAdapter != null) { newAdapter.notifyDataSetChanged(); }
                            if (curTime != null) { curTime.setText(Integer.toString(value)); }
                        }
                    });
                }
                status = "received";
                stopService(new Intent(TimerActivity.this, TimerService.class));
            }
        }
    }
}

