package by.bsuir.wildboom.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class UnitFragment extends UnitFragmentBase {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onCreateViewHelp(inflater, container);
        return view;
    }
}