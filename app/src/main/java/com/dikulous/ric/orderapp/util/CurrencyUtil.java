package com.dikulous.ric.orderapp.util;

import java.math.BigDecimal;

/**
 * Created by ric on 7/04/16.
 */
public class CurrencyUtil {
    public static BigDecimal longCentsToBigDecimal(long cents){
        BigDecimal bd = new BigDecimal(cents);
        bd = bd.divide(new BigDecimal(100));
        return bd;
    }
}
