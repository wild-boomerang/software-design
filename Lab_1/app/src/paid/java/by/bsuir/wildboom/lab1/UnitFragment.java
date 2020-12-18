package by.bsuir.wildboom.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;

public class UnitFragment extends UnitFragmentBase {
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onCreateViewHelp(inflater, container);

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
}