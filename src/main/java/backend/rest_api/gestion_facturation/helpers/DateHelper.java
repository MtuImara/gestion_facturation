package backend.rest_api.gestion_facturation.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateHelper {
    private static String textConversion(Date date, String type) {
        String strDate = "";
        String format = "yyyy-MM-dd";

        if (type.equalsIgnoreCase("time")) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (date != null) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                strDate = dateFormat.format(date);
            } catch (Exception e) {
                System.out.println("can not be converted to text:" + e.getMessage());
            }

        }
        return strDate;
    }

    public static Date toDate(String date_as_string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date convert = null;
        try {
            if (date_as_string != null && !date_as_string.equals("")) {
                convert = format.parse(date_as_string);
            }

        } catch (ParseException e) {
            System.out.println("Fail to convert to date: " + e.getMessage());
        }
        return convert;
    }

    public static Date toDateWithHeureMinSec(String date_as_string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date convert = null;
        try {
            if (date_as_string != null && !date_as_string.equals("")) {
                convert = format.parse(date_as_string);
            }

        } catch (ParseException e) {
            System.out.println("Fail to convert to date: " + e.getMessage());
        }
        return convert;
    }

    public static String toText(String string) {
        String strDate = "";
        if (string != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            strDate = dateFormat.format(string);
        }

        return strDate;
    }

    public static String convertDateTimeFormat(Object date) {
        String strDate = "";

        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            strDate = sdf.format(date);
        }

        return strDate;
    }

    public static String toText(Date date) {
        return textConversion(date, "");
    }

    public static String toText(Date date, String time) {
        return textConversion(date, time);
    }

    public static Date now() {
        return new Date();
    }

    private static Date getDateResult(Date date, int number, String timer, String operation) {
        Long timemillis = date == null ? now().getTime() : date.getTime();
        number = number > 0 ? number : 1;
        if (operation.equalsIgnoreCase("+")) {
            switch (timer) {
                case "sec":
                    timemillis += ((1000) * number);
                    break;
                case "min":
                    timemillis += (1000 * 60 * number);
                    break;

                case "hour":
                    timemillis += (1000 * 3600 * number);
                    break;
                case "day":
                    timemillis += (1000 * 3600 * 24 * number);
                    break;
                case "week":
                    timemillis += (3600000 * 24 * 7 * number);
                case "month":
                    timemillis += (3600000 * 24 * 30 * number);
                    break;

                default:
                    timemillis += 0;
            }
        }

        return new Date(timemillis);
    }

    private static Date getCalendarResult(Date date, int number, String timer) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        switch (timer) {
            case "year":
                cal.add(Calendar.YEAR, number);
                break;
            case "month":
                cal.add(Calendar.MONTH, number);
                break;
            default:
                cal.add(Calendar.DATE, number);
        }

        Date date_result = cal.getTime();

        return date_result;
    }

    public static Date addYear(Date date) {
        return getCalendarResult(date, 1, "year");
    }

    public static Date addYear(Date date, int number) {
        return getCalendarResult(date, number, "year");
    }

    public static Date add(int number, String time) {
        return getDateResult(null, number, time, "+");
    }

    public static Date add(String timer) {
        return getDateResult(null, 0, timer, "+");
    }

    public static Date add(Date date, String timer) {
        return getDateResult(date, 0, timer, "+");
    }

    public static Long getDiff(String date1, String date2, String type, boolean time) {

        String format = "yyyy-MM-dd";

        if (time) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat date_format = new SimpleDateFormat(format);

        Date firstDate = null;
        Date secondDate = null;
        Long diff_time = Long.parseLong("1");

        try {
            firstDate = date_format.parse(date1);
            secondDate = date_format.parse(date2);
        } catch (Exception e) {
            diff_time = Long.parseLong("0");
            System.out.println("Errrur date : " + e.getMessage());
        }

        if (diff_time > 0) {
            long diffInMillies = (secondDate.getTime() - firstDate.getTime());
            if (type.equalsIgnoreCase("sec")) {
                diff_time = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            } else if (type.equalsIgnoreCase("min")) {
                diff_time = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            } else if (type.equalsIgnoreCase("hour")) {
                diff_time = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            } else {
                diff_time = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            }

        }
        return diff_time;
    }

    public static Long diffInSec(String date1, String date2) {
        return getDiff(date1, date2, "sec", true);
    }

    public static Long diffInSec(Date date1, Date date2) {
        String format_date1 = toText(date1);
        String format_date2 = toText(date2);
        return getDiff(format_date1, format_date2, "sec", true);
    }

    public static Long diffInSecFromNow(String date) {
        String current = toText(now());
        return getDiff(current, date, "sec", true);
    }

    public static Long diffInSecFromNow(Date date) {
        String current = toText(now());
        String date_to_text = toText(date);
        return getDiff(current, date_to_text, "sec", true);
    }

    public static Long diffInMin(String date1, String date2) {
        return getDiff(date1, date2, "min", true);
    }

    public static Long diffInMin(Date date1, Date date2) {
        String format_date1 = toText(date1);
        String format_date2 = toText(date2);
        return getDiff(format_date1, format_date2, "min", true);
    }

    public static Long diffInMinFromNow(String date) {
        String current = toText(now());
        return getDiff(current, date, "min", true);
    }

    public static Long diffInMinFromNow(Date date) {
        String current = toText(now());
        String date_to_text = toText(date);
        return getDiff(current, date_to_text, "min", true);
    }

    public static Long diffInHour(String date1, String date2) {
        return getDiff(date1, date2, "hour", true);
    }

    public static Long diffInHour(Date date1, Date date2) {
        String format_date1 = toText(date1);
        String format_date2 = toText(date2);
        return getDiff(format_date1, format_date2, "hour", true);
    }

    public static Long diffInHourFromNow(String date) {
        String current = toText(now());
        return getDiff(current, date, "hour", true);
    }

    public static Long diffInHourFromNow(Date date) {
        String current = toText(now());
        String date_to_text = toText(date);
        return getDiff(current, date_to_text, "hour", true);
    }

    public static Long diffInDay(String date1, String date2) {
        return getDiff(date1, date2, "day", false);
    }

    public static Long diffInDay(Date date1, Date date2) {
        String format_date1 = toText(date1);
        String format_date2 = toText(date2);
        return getDiff(format_date1, format_date2, "day", false);
    }

    public static Long diffInDayFromNow(String date) {
        String current = toText(now());
        return getDiff(current, date, "day", true);
    }

    public static Long diffInDayFromNow(Date date) {
        String current = toText(now());
        String date_to_text = toText(date);
        return getDiff(current, date_to_text, "day", true);
    }

    public static Long diffInMonth(String date1, String date2) {
        try {
            long diff_months = ChronoUnit.MONTHS.between(LocalDate.parse(date1).withDayOfMonth(1),
                    LocalDate.parse(date2).withDayOfMonth(1));
            return diff_months;
        } catch (Exception e) {
            return null;
        }
    }

    public static Long diffInMonth(Date date1, Date date2) {
        try {
            long diff_months = ChronoUnit.MONTHS.between(LocalDate.parse(toText(date1)).withDayOfMonth(1),
                    LocalDate.parse(toText(date2)).withDayOfMonth(1));
            return diff_months;
        } catch (Exception e) {
            return null;
        }
    }

    private static String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String LocalDateTimetoString(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(PATTERN_DATE_TIME, Locale.GERMANY).format(localDateTime);
    }

    public static LocalDateTime StringToLocalDateTime(final String string) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(PATTERN_DATE_TIME));
    }
}
