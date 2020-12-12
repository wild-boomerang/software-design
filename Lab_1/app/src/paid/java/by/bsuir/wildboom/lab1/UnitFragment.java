package by.bsuir.wildboom.lab1;

import android.R.layout;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

public class UnitFragment extends Fragment {
    EditText lineTop;
    EditText lineBottom;
    Spinner spinnerTop;
    Spinner spinnerBottom;

    ConversionViewModel conversionViewModel;
    SharedViewModel sharedViewModel;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    public UnitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit, container, false);

        lineTop = view.findViewById(R.id.textView_top);
        lineTop.setInputType(InputType.TYPE_NULL);
        lineBottom = view.findViewById(R.id.textView_bottom);
        lineBottom.setInputType(InputType.TYPE_NULL);
        spinnerTop = view.findViewById(R.id.spinner_top);
        spinnerBottom = view.findViewById(R.id.spinner_bottom);

        conversionViewModel = new ViewModelProvider(requireActivity()).get(ConversionViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.select("");

        conversionViewModel.getSelectedValue().observe(getViewLifecycleOwner(), value -> lineTop.setText(value));
        sharedViewModel.getSelected().observe(getViewLifecycleOwner(), this::setNewVal);

        conversionViewModel.getSelectedSpinnerTop().observe(getViewLifecycleOwner(), value -> {
            String[] values = conversionViewModel.conversion.getValues();
            ArrayAdapter<String> adapterTop = new ArrayAdapter<>(requireActivity().getBaseContext(),
                                                                 layout.simple_spinner_item, values);
            adapterTop.setDropDownViewResource(layout.simple_spinner_dropdown_item);
            spinnerTop.setAdapter(adapterTop);
            spinnerTop.setSelection(value);
        });

        conversionViewModel.getSelectedSpinnerBottom().observe(getViewLifecycleOwner(), value -> {
            String[] values = conversionViewModel.conversion.getValues();
            ArrayAdapter<String> adapterDown = new ArrayAdapter<>(requireActivity().getBaseContext(),
                                                                  layout.simple_spinner_item, values);
            adapterDown.setDropDownViewResource(layout.simple_spinner_dropdown_item);
            spinnerBottom.setAdapter(adapterDown);
            spinnerBottom.setSelection(value);
        });

        spinnerTop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                conversionViewModel.selectSpinnerTop(position);
                lineBottom.setText(conversionViewModel.makeConversion(lineTop.getText().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerBottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                conversionViewModel.selectSpinnerBottom(position);
                lineBottom.setText(conversionViewModel.makeConversion(lineTop.getText().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button buttonCopyTop = view.findViewById(R.id.button_copy_top);
        buttonCopyTop.setOnClickListener(v -> {
            String text = "Nothing";
            if (!lineTop.getText().toString().equals("")) {
                clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                text = lineTop.getText().toString();
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
            }
            text += " was copied";
            Toast toast = Toast.makeText(requireActivity().getBaseContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        });

        Button buttonCopyBottom = view.findViewById(R.id.button_copy_bottom);
        buttonCopyBottom.setOnClickListener(v -> {
            String text = "Nothing";
            if (!lineBottom.getText().toString().equals("")) {
                clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                text = lineBottom.getText().toString();
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
            }
            text += " was copied";
            Toast toast = Toast.makeText(requireActivity().getBaseContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        });

        ImageButton buttonSwap = view.findViewById(R.id.button_swap);
        buttonSwap.setOnClickListener(v -> {
            String newTopText = lineBottom.getText().toString();
            int id = (int) spinnerTop.getSelectedItemId();
            spinnerTop.setSelection((int) spinnerBottom.getSelectedItemId());
            spinnerBottom.setSelection(id);
            lineTop.setText(newTopText);
            conversionViewModel.selectValue(newTopText);
        });

        return view;
    }

    public void setNewVal(String item) {
        String lineValue = lineTop.getText().toString();
        if (item != null) {
            if (!item.equals("|"))
                lineValue += item;
            else {
                String answer = "";
                if (lineValue.length() != 0)
                    answer = lineValue.substring(0, lineValue.length() - 1);
                lineValue = answer;
            }
        }
        lineTop.setText(lineValue);
        String text_down = conversionViewModel.selectValue(lineValue);
        lineBottom.setText(text_down);
    }
}