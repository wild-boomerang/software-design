package by.bsuir.wildboom.lab1;

import java.io.Serializable;

public class Conversion implements Serializable {

    private String[] values;
    private double[][] conv_kf;

    public Conversion(String[] values, double[] conv_kf) {
        this.values = values;
        this.conv_kf = new double[values.length][values.length];

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                this.conv_kf[i][j] = 1.0;
            }
        }

        int k = 0;
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (k < conv_kf.length) {
                    this.conv_kf[i][j] = conv_kf[k];
                    this.conv_kf[j][i] = 1.0 / conv_kf[k];
                    k++;
                }
            }
        }
    }

    public double Convert(int i, int j, double value) {
        return this.conv_kf[i][j] * value;
    }

    public String[] getValues() {
        return this.values;
    }
}

