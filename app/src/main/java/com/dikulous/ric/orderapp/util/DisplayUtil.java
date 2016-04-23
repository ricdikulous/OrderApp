package com.dikulous.ric.orderapp.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by ric on 23/04/16.
 */
public class DisplayUtil {
    public static String listToString(List<String> list){
        return list.toString().replaceAll("\\[|\\]", "").trim();
    }

    public static String bigDecimalToCurrency(BigDecimal bg){
        return NumberFormat.getCurrencyInstance().format(bg);
    }

    public static String longCentsToCurrency(long cents){
        return bigDecimalToCurrency(CurrencyUtil.longCentsToBigDecimal(cents));
    }
}
