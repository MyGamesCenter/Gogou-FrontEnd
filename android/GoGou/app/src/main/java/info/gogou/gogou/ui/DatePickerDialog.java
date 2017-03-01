package info.gogou.gogou.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.gogou.gogou.R;

/**
 * Created by lxu on 2015-12-27.
 */
public class DatePickerDialog implements DatePicker.OnDateChangedListener
{

    private DatePicker datePicker;

    private AlertDialog ad;
    private String setDateTime;
    private String currentTime;
    private SimpleDateFormat dateFormat;

    private String initDateTime;
    private Activity activity;
    public  Long time;

    public DatePickerDialog(Activity activity, String initDateTime)
    {
        this.activity = activity;
        this.initDateTime = initDateTime;
    }

    private void init(DatePicker datePicker)
    {
        Calendar calendar = Calendar.getInstance();

        if (null != initDateTime && !"".equals(initDateTime))
        {
            calendar = getCalendarByInintData(initDateTime);
        }
        else
        {
            initDateTime = calendar.get(Calendar.YEAR) + activity.getResources().getString(R.string.year)
                            + calendar.get(Calendar.MONTH) + activity.getResources().getString(R.string.month)
                            + calendar.get(Calendar.DAY_OF_MONTH) + activity.getResources().getString(R.string.day);
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        this);
        dateFormat = new SimpleDateFormat(activity.getResources().getString(R.string.date_format));
    }

    //弹出日期时间选择框方法
    public AlertDialog datePickerDialog(final TextView inputDate)
    {

        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_datetime, null);

        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        init(datePicker);

        ad = new AlertDialog.Builder(activity)
                .setTitle(R.string.select_time)
                .setView(dateTimeLayout)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText(setDateTime);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText("");
                    }
                }).show();

        onDateChanged(null, 0, 0, 0);
        return ad;
    }

    public SimpleDateFormat getDateFormate() {
        return dateFormat;
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();

        currentTime = dateFormat.format(calendar.getTime());

        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        setDateTime = dateFormat.format(calendar.getTime());
        Date setDate = null;
        Date currentDate = null;
        try
        {
            setDate = dateFormat.parse(setDateTime);
            currentDate = dateFormat.parse(currentTime);
        }catch( ParseException e){
            Log.i("ParseException","parseException");
        }

        if(setDate.before(currentDate)){
            Toast.makeText(activity, R.string.notif_currentTime_before_selectTime, Toast.LENGTH_SHORT).show();
        }
        else {
            time = setDate.getTime();
        }
    }

    //实现将初始日期时间2012年07月02日拆分成年 月 日并赋值给calendar
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日拆分成年 月 日

        String yearStr = spliteString(initDateTime, activity.getResources().getString(R.string.year_format), "index", "front"); // 年份
        String monthAndDay = spliteString(initDateTime, activity.getResources().getString(R.string.year_format), "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, activity.getResources().getString(R.string.month_format), "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, activity.getResources().getString(R.string.month_format), "index", "back"); // 日

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay);
        return calendar;
    }

    //截取子串
    private  String spliteString(String srcStr, String pattern,
                                String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }
}
