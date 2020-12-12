package by.bsuir.wildboom.lab1;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class KeyboardFragment extends Fragment {
    private SharedViewModel model;

    public KeyboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Button button0 = view.findViewById(R.id.button_num_0);
        button0.setOnClickListener(v -> model.select("0"));

        Button button1 = view.findViewById(R.id.button_num_1);
        button1.setOnClickListener(v -> model.select("1"));

        Button button2 = view.findViewById(R.id.button_num_2);
        button2.setOnClickListener(v -> model.select("2"));

        Button button3 = view.findViewById(R.id.button_num_3);
        button3.setOnClickListener(v -> model.select("3"));

        Button button4 = view.findViewById(R.id.button_num_4);
        button4.setOnClickListener(v -> model.select("4"));

        Button button5 = view.findViewById(R.id.button_num_5);
        button5.setOnClickListener(v -> model.select("5"));

        Button button6 = view.findViewById(R.id.button_num_6);
        button6.setOnClickListener(v -> model.select("6"));

        Button button7 = view.findViewById(R.id.button_num_7);
        button7.setOnClickListener(v -> model.select("7"));

        Button button8 = view.findViewById(R.id.button_num_8);
        button8.setOnClickListener(v -> model.select("8"));

        Button button9 = view.findViewById(R.id.button_num_9);
        button9.setOnClickListener(v -> model.select("9"));

        Button button_dot = view.findViewById(R.id.button_dot);
        button_dot.setOnClickListener(v -> model.select("."));

        Button button_delete = view.findViewById(R.id.button_delete);
        button_delete.setOnClickListener(v -> model.select("|"));

        return view;
    }
}