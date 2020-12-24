package by.bsuir.wildboom.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class TimerAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> itemsList;
    private LayoutInflater inflater;
    private int layout;

    TimerAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource, items);

        this.itemsList = items;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Item product = itemsList.get(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(product.name + " " + product.length);
        return convertView;
    }

    private static class ViewHolder {
        final TextView text;

        public ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text_show);
        }
    }
}
