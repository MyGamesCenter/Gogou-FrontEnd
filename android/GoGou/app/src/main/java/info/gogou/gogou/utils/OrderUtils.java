package info.gogou.gogou.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by lxu on 2016-06-14.
 */
public class OrderUtils {

    private static final String TAG = "OrderUtils";

    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public static final String DATE_LONG_FORMAT = "yyyyMMddHHmmss";

    /**
     * get the unique for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public static String getOrderNumber() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_LONG_FORMAT, Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Log.d(TAG, "date value: " + key);

        Random r = new Random();
        String randomInt = String.valueOf(Math.abs(r.nextInt()));
        Log.d(TAG, "random int: " + randomInt);
        randomInt = randomInt.substring(0, 5);
        Log.d(TAG, "substring of random int: " + randomInt);
        key = key + randomInt;

        return key;
    }
}
