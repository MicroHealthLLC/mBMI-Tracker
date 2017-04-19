package com.microhealthllc.mbmicalc;

import android.content.Context;
import android.util.Log;

public class BMIUtils {
    static double bmiresults=0.0;
    public static double   calcBMI(double height, double weight, Context ctx, int metric) {
        if (height <= 0 || weight <= 0) {
            throw new IllegalArgumentException(ctx.getString(R.string.error_less_than_zero));
        }
        else{
         bmiresults=   (weight*703)/ (height*height);
        }
        Log.i("result",bmiresults+"");
        return bmiresults ;

    }

    public static int getJudgement(double bmi) {
        return bmi < 18.5 ? R.string.underweight :
               bmi < 25.0 ? R.string.normal :
               bmi < 30.0 ? R.string.overweight : R.string.obese;
    }
}
