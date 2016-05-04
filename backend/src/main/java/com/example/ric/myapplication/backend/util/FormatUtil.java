package com.example.ric.myapplication.backend.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String longCentsToStringSansSymbol(Long cents){
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

    public static String timestampToDate(Long timestamp) {
        if (timestamp != null){
            Date date = new Date(timestamp);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }

    public static String truncateDescription(String description){
        if(description.length()>100){
            return description.substring(0, 99)+"...";
        }
        return description;
    }
}
