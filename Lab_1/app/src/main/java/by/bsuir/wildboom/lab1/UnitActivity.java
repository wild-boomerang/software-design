package by.bsuir.wildboom.lab1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class UnitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            Conversion conv = (Conversion)arguments.getSerializable(Conversion.class.getSimpleName());
            ConversionViewModel conversionViewModel = new ViewModelProvider(this).get(ConversionViewModel.class);

            if (conversionViewModel.conversion == null) {
                conversionViewModel.conversion = conv;
                conversionViewModel.selectValue("");
                conversionViewModel.selectSpinnerTop(0);
                conversionViewModel.selectSpinnerBottom(1);
            }
        }

        setContentView(R.layout.activity_unit);
    }
}