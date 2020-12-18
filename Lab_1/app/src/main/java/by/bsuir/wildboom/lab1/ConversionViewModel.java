package by.bsuir.wildboom.lab1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConversionViewModel extends ViewModel {
    public Conversion conversion;
    private int _spinnerTop;
    private int _spinnerBottom;

    public final MutableLiveData<String> value = new MutableLiveData<>();
    public final MutableLiveData<Integer> spinnerTop = new MutableLiveData<>();
    public final MutableLiveData<Integer> spinnerBottom = new MutableLiveData<>();

    public String selectValue(String s) {
        value.setValue(s);
        return makeConversion(s);
    }

    public void selectSpinnerTop(Integer s) {
        _spinnerTop = s;
        spinnerTop.setValue(s);
    }

    public void selectSpinnerBottom(Integer s) {
        _spinnerBottom = s;
        spinnerBottom.setValue(s);
    }

    public LiveData<String> getSelectedValue() {
        return value;
    }

    public LiveData<Integer> getSelectedSpinnerTop() {
        return spinnerTop;
    }

    public LiveData<Integer> getSelectedSpinnerBottom() {
        return spinnerBottom;
    }

    public String makeConversion(String newLineValue) {
        String answerText = "";
        double num, answerDouble;

        if (!newLineValue.equals("")) {
            try {
                num = Double.parseDouble(newLineValue);
                answerDouble = (double)Math.round(conversion.Convert(_spinnerTop, _spinnerBottom, num) * 1000000);
                answerDouble /= 1000000;
                answerText = Double.toString(answerDouble);
            } catch (Exception e) {
                answerText = "";
            }
        }
        return answerText;
    }
}
