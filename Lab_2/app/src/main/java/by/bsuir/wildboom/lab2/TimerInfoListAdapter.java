package by.bsuir.wildboom.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class TimerInfoListAdapter extends ArrayAdapter<TimerInfo> {
    private Context context;
    Repository repository;
    private LayoutInflater inflater;
    private int layout;

    private ArrayList<TimerInfo> timerInfoList;
    private TimerInfo curTimerInfo;

    TimerInfoListAdapter(Context context, int resource, ArrayList<TimerInfo> products) {
        super(context, resource, products);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.timerInfoList = products;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;
        curTimerInfo = timerInfoList.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            convertView.setBackgroundResource(R.drawable.round_corners);

            GradientDrawable drawable = (GradientDrawable) convertView.getBackground();
            int[] colors = curTimerInfo.getColor();
            drawable.setColor(Color.rgb(colors[0], colors[1], colors[2]));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArrayList<Item> values = curTimerInfo.getSimpleList();
        viewHolder.title.setText(curTimerInfo.getTitle());

        Item item = values.get(0);
        viewHolder.text_1.setText(item.name + "   " + item.length.toString());

        item = values.get(1);
        viewHolder.text_2.setText(item.name + "   " + item.length.toString());

        item = values.get(2);
        viewHolder.text_3.setText(item.name + "   " + item.length.toString());

        item = values.get(3);
        viewHolder.text_4.setText(item.name + "   " + item.length.toString());

        item = values.get(4);
        viewHolder.text_5.setText(item.name + "   " + item.length.toString());

        item = values.get(5);
        viewHolder.text_6.setText(item.name + "   " + item.length.toString());

        viewHolder.text_7.setText(context.getString(R.string.total_time) + "   " + curTimerInfo.getTotalTime());

        viewHolder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerInfo localTimer = timerInfoList.get(position);
                String id = Integer.toString(localTimer.getId());
                timerInfoList.remove(position);
                notifyDataSetChanged();
                repository = new Repository(context);
                repository.deleteTimer(id);
            }
        });

        viewHolder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerInfoListAdapter.this.getContext(), CreateUpdateActivity.class);
                TimerInfo localTimerInfo = timerInfoList.get(position);
                intent.putExtra(TimerInfo.class.getSimpleName(), localTimerInfo);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        final ImageView imageDelete, imageEdit;
        final TextView title, text_1, text_2, text_3, text_4, text_5, text_6, text_7;
        ViewHolder(View view) {
            imageDelete = view.findViewById(R.id.timer_set_delege_image);
            imageEdit = view.findViewById(R.id.timer_set_update_image);

            title = view.findViewById(R.id.timer_title);
            text_1 = view.findViewById(R.id.timer_text_1);
            text_2 = view.findViewById(R.id.timer_text_2);
            text_3 = view.findViewById(R.id.timer_text_3);
            text_4 = view.findViewById(R.id.timer_text_4);
            text_5 = view.findViewById(R.id.timer_text_5);
            text_6 = view.findViewById(R.id.timer_text_6);
            text_7 = view.findViewById(R.id.timer_text_7);
        }
    }
}
