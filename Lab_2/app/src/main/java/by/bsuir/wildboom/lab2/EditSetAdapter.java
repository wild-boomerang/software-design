package by.bsuir.wildboom.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class EditSetAdapter extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> itemsList;
    private LayoutInflater inflater;
    private int layout;

    EditSetAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource, items);

        this.context = context;
        this.itemsList = items;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Item product = itemsList.get(position);
        final Integer length = product.length;
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.number.setText(length.toString());
        viewHolder.name.setText(product.name);
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num = Integer.parseInt(viewHolder.number.getText().toString());
                    if (num > 0) { num--; }
                    Item localItem = itemsList.get(position);
                    localItem.length = num;
                    itemsList.set(position, localItem);
                    viewHolder.number.setText(Integer.toString(num));
                }
                catch (Exception e) {

                }
            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num = Integer.parseInt(viewHolder.number.getText().toString());
                    num++;
                    Item localItem = itemsList.get(position);
                    localItem.length = num;
                    itemsList.set(position, localItem);
                    viewHolder.number.setText(Integer.toString(num));
                }
                catch (Exception e) {

                }
            }
        });

        String name = null;
        if (product.name.equals("warm_up"))     { name = "warm_up_foreground"; }
        if (product.name.equals("workout"))     { name = "workout_foreground"; }
        if (product.name.equals("rest"))        { name = "rest_foreground"; }
        if (product.name.equals("cooldown"))    { name = "cooldown_foreground"; }
        if (product.name.equals("cycle count")) { name = "cycle_foreground"; }
        if (product.name.equals("set count"))   { name = "set_foreground"; }

        String pkgName = context.getPackageName();
        if (name != null) {
            int resID = context.getResources().getIdentifier(name, "mipmap", pkgName);
            viewHolder.name.setText(product.name);
            viewHolder.icon.setImageResource(resID);
        }

        return convertView;
    }

    private static class ViewHolder {
        final ImageView icon, plus, minus;
        final TextView name, number;
        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.setting_icon);
            plus = (ImageView) view.findViewById(R.id.image_plus);
            minus = (ImageView) view.findViewById(R.id.image_minus);
            number = (TextView) view.findViewById(R.id.num_section);
            name = (TextView) view.findViewById(R.id.name_section);
        }
    }
}

