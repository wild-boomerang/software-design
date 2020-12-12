package by.bsuir.wildboom.lab1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Conversion conversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_weight = findViewById(R.id.button_weight);
        Button button_distance = findViewById(R.id.button_distance);
        Button button_currency = findViewById(R.id.button_currency);
        button_weight.setOnClickListener(this);
        button_distance.setOnClickListener(this);
        button_currency.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, UnitActivity.class);

        switch (view.getId()) {
            case R.id.button_weight:
                this.conversion = new Conversion(new String[] { getString(R.string.kilograms),
                                                                getString(R.string.puonds),
                                                                getString(R.string.ounces)},
                                                                new double[] { 2.2046226218, 35.2739619496, 16 });
                break;
            case R.id.button_distance:
                this.conversion = new Conversion(new String[] { getString(R.string.kilometers),
                                                                getString(R.string.miles),
                                                                getString(R.string.feet)},
                                                                new double[] { 0.6213711922, 3280.8398950131, 5280 });
                break;
            case R.id.button_currency:
                this.conversion = new Conversion(new String[] { getString(R.string.BYN),
                                                                getString(R.string.USD),
                                                                getString(R.string.EUR)},
                                                                new double[] { 0.3901829958, 0.331279401, 0.8490078511 });
                break;
        }
        i.putExtra(Conversion.class.getSimpleName(), this.conversion);
        startActivity(i);
    }
}

