package com.example.ric.myapplication.backend.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by ric on 3/05/16.
 */
public class FormatUtil {
    public static String listToString(List<String> list){
        if(list != null && list.size()>0) {
            return list.toString().replaceAll("\\[|\\]", "").trim();
        } else {
            return "";
        }
    }

    public static String bigDecimalToCurrency(BigDecimal bg){
        return NumberFormat.getCurrencyInstance().format(bg);
    }

    public static String longCentsToCurrency(long cents){
        return bigDecimalToCurrency(longCentsToBigDecimal(cents));
    }

    public static String longCentsToStringWithoutSign(Long cents){
        if(cents != null) {
            String currency = bigDecimalToCurrency(longCentsToBigDecimal(cents));
            return currency.replaceAll("\\$", "");
        } else {
            return "";
        }
    }

    public static BigDecimal longCentsToBigDecimal(long cents){
        BigDecimal bd = new BigDecimal(cents);
        bd = bd.divide(new BigDecimal(100));
        return bd;
    }
}
