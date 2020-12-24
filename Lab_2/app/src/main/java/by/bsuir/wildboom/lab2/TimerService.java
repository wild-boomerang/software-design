package by.bsuir.wildboom.lab2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private Timer timer;
    private TimerInfo timerInfo;
    private CountTimerTask countTimerTask;

    private ArrayList<Item> items;
    private int num;
    private int count;

    Service service;

    public TimerService() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        service = this;
        Bundle arguments = intent.getExtras();
        if (arguments != null) {
            num = (int) arguments.getSerializable(TimerActivity.VAL_PARAM);
            count = (int) arguments.getSerializable(TimerActivity.POS_PARAM);
            timerInfo = (TimerInfo) arguments.getSerializable(TimerActivity.TIMER_PARAM);

            Item item = (Item) arguments.getSerializable(TimerActivity.WARM_UP_PARAM);
            items = timerInfo.getList();
            items.add(0, item);
        }
        countTimerTask = new CountTimerTask();
        timer = new Timer();
        timer.schedule(countTimerTask, 1000, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class CountTimerTask extends TimerTask
    {
        @Override
        public void run() {
            if (num != 0) {
                num--;
                Intent intent = new Intent(TimerActivity.BROADCAST_ACTION);
                intent.putExtra(TimerActivity.VAL_PARAM, num);
                intent.putExtra(TimerActivity.POS_PARAM, count);
                intent.putExtra(TimerActivity.ID_TIMER_SET_PARAM, timerInfo.getId());
                sendBroadcast(intent);
            } else {
                if (count < timerInfo.getList().size() - 1) {
                    count++;
                    num = items.get(count).length;
                    timer.cancel();
                    timer.purge();
                    timer =  new Timer();
                    countTimerTask = new CountTimerTask();
                    timer.schedule(countTimerTask, 1000, 1000);

                } else {
                    timer.cancel();
                    timer.purge();
                    stopSelf();
                }
            }
        }
    }
}
