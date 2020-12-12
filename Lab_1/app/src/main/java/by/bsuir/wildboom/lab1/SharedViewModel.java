package by.bsuir.wildboom.lab1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    public final MutableLiveData<String> selected = new MutableLiveData<>();

    public void select(String s) {
        selected.setValue(s);
    }

    public LiveData<String> getSelected() {
        return selected;
    }
}

