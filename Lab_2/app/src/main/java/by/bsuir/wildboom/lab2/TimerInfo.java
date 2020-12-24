package by.bsuir.wildboom.lab2;

import java.io.Serializable;
import java.util.ArrayList;

public class TimerInfo implements Serializable {
    private String timerSetText;
    private int id;
    private int[] colors;
    private ArrayList<Item> itemList = new ArrayList<>();
    private ArrayList<Item> simpleItemList = new ArrayList<>();

    public int warmUpTime;
    private int workoutTime;
    private int restTime;
    private int cooldownTime;
    private int cycleCount; // work and rest repeat
    private int setCount; // 3 types repeat

    public TimerInfo(int warmUpTime, int workoutTime, int restTime, int cooldownTime, int cycleCount,
                     int setCount)
    {
        this.warmUpTime = warmUpTime;
        this.workoutTime = workoutTime;
        this.restTime = restTime;
        this.cooldownTime = cooldownTime;
        this.cycleCount = cycleCount;
        this.setCount = setCount;
        this.colors = new int[] {0, 0, 0};

        updateList();
        updateSimpleList();
    }

    public TimerInfo(int id, int warmUpTime, int workoutTime, int restTime, int cooldownTime,
                     int cycleCount, int setCount)
    {
        this.id = id;
        this.warmUpTime = warmUpTime;
        this.workoutTime = workoutTime;
        this.restTime = restTime;
        this.cooldownTime = cooldownTime;
        this.cycleCount = cycleCount;
        this.setCount = setCount;

        updateList();
        updateSimpleList();
    }

    private void updateList() {
        itemList.add(new Item("warm_up", this.warmUpTime));
        for (int i = 0; i < setCount; i++) {
            for (int j = 0; j < cycleCount; j++) {
                itemList.add(new Item("workout", this.workoutTime));
                itemList.add(new Item("rest", this.restTime));
            }
            itemList.add(new Item("cooldown", this.cooldownTime));
        }
    }

    private void updateSimpleList() {
        simpleItemList.add(new Item("warm_up", this.warmUpTime));
        simpleItemList.add(new Item("workout", this.workoutTime));
        simpleItemList.add(new Item("rest", this.restTime));
        simpleItemList.add(new Item("cooldown", this.cooldownTime));
        simpleItemList.add(new Item("cycle count", this.cycleCount));
        simpleItemList.add(new Item("set count", this.setCount));
    }

    public int getTotalTime() {
        int time = 0;
        time += this.warmUpTime;
        for (int i = 0; i < setCount; i++) {
            for (int j = 0; j < cycleCount; j++) {
                time += workoutTime;
                time += restTime;
            }
            time += cooldownTime;
        }
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String timerSetText) {
        this.timerSetText = timerSetText;
    }

    public String getTitle() {
        return this.timerSetText;
    }

    public void setColor(int[] colors) {
        this.colors = colors;
    }

    public int[] getColor() {
        return this.colors;
    }

    public ArrayList<Item> getList() {
        return this.itemList;
    }

    public ArrayList<Item> getSimpleList() {
        return this.simpleItemList;
    }
}
