package by.bsuir.wildboom.lab1;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

abstract class UnitFragmentBase extends Fragment {
    protected EditText lineTop;
    protected EditText lineBottom;
    protected Spinner spinnerTop;
    protected Spinner spinnerBottom;

    protected ConversionViewModel conversionViewModel;
    protected SharedViewModel sharedViewModel;

    public UnitFragmentBase() {
        // Required empty public constructor
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected View onCreateViewHelp(LayoutInflater inflater, ViewGroup container)
    {
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
            ArrayAdapter<String> adapterTop = new ArrayAdapter<>(
                    requireActivity().getBaseContext(),
                    android.R.layout.simple_spinner_item, values);
            adapterTop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTop.setAdapter(adapterTop);
            spinnerTop.setSelection(value);
        });

        conversionViewModel.getSelectedSpinnerBottom().observe(getViewLifecycleOwner(), value -> {
            String[] values = conversionViewModel.conversion.getValues();
            ArrayAdapter<String> adapterBottom = new ArrayAdapter<>(
                    requireActivity().getBaseContext(),
                    android.R.layout.simple_spinner_item, values);
            adapterBottom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBottom.setAdapter(adapterBottom);
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

        return view;
    }

    public final void setNewVal(String item) {
        String lineValue = lineTop.getText().toString();
        if (item != null) {
            if (!item.equals("|"))
                lineValue += item;
            else {
                String answer = "";
                if (lineValue.length() != 0) {
                    answer = lineValue.substring(0, lineValue.length() - 1);
                }
                lineValue = answer;
            }
        }
        lineTop.setText(lineValue);
        String text_down = conversionViewModel.selectValue(lineValue);
        lineBottom.setText(text_down);
    }
}
