//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

import javax.swing.text.JTextComponent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class VerifyUtil {
    public VerifyUtil() {
    }

    public static String getWrongChars(String regexOfAllowedChars, String checkLine) {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < checkLine.length(); ++i) {
            if(!Pattern.matches(regexOfAllowedChars, String.valueOf(checkLine.charAt(i)))) {
                result.append(checkLine.charAt(i));
            }
        }

        return result.toString();
    }

    public static String getWrongCharsWithCutRegexpLength(String regexOfAllowedChars, String checkLine) {
        String cutRegexp = regexOfAllowedChars.replaceAll("[{][\\d]+([,][\\d]*)?[}]", "");
        return getWrongChars(cutRegexp, checkLine);
    }

    public static String cutWrongChars(String regexp, String source) {
        String cutResult = source;
        String wrongChars = getWrongCharsWithCutRegexpLength(regexp, source);
        char[] var4 = wrongChars.toCharArray();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            char each = var4[var6];
            cutResult = cutResult.replace(String.valueOf(each), "");
        }

        return cutResult;
    }

    public static boolean isCorrectDatePeriod(String pattern, String dateFrom, String dateTo) {
        return isCorrectDatePeriod((DateFormat)(new SimpleDateFormat(pattern)), dateFrom, dateTo);
    }

    public static boolean isIncorrectDatePeriod(DateFormat dateFormat, JTextComponent from, JTextComponent to) {
        try {
            from.setText(dateFormat.format(dateFormat.parse(from.getText())));
            to.setText(dateFormat.format(dateFormat.parse(to.getText())));
        } catch (ParseException var4) {
            return false;
        }

        return !isCorrectDatePeriod(dateFormat, from.getText(), to.getText());
    }

    public static boolean isCorrectDatePeriod(DateFormat dateFormat, String dateFrom, String dateTo) {
        try {
            Date from = dateFormat.parse(dateFrom);
            Date to = dateFormat.parse(dateTo);
            if(from.after(to)) {
                return false;
            } else {
                Calendar min = Calendar.getInstance();
                min.set(1900, 0, 1);
                if(from.before(min.getTime())) {
                    return false;
                } else {
                    Calendar max = Calendar.getInstance();
                    max.set(2100, 0, 1);
                    return !to.after(max.getTime());
                }
            }
        } catch (Exception var7) {
            return false;
        }
    }

    public static boolean notNull(Object... args) {
        Object[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object arg = var1[var3];
            if(arg == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotEmpty(String st) {
        return st != null && !st.isEmpty();
    }

    public static boolean isNotEmptyTrim(String st) {
        return st != null && !st.trim().isEmpty();
    }

    public static boolean isEmptyTrim(String st) {
        return st == null || st.trim().isEmpty();
    }

    public static boolean isNotEmpty(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String arg = var1[var3];
            if(arg == null || arg.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEmpty(String st) {
        return st == null || st.isEmpty();
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String arg = var1[var3];
            if(arg == null || arg.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static String asString(Enum<?> en) {
        return en == null?null:en.toString();
    }

    public static String asString(Boolean b) {
        return b == null?null:b.toString();
    }

    public static boolean in(Object that, Object... with) {
        Object[] var2 = with;
        int var3 = with.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object each = var2[var4];
            if(each.equals(that)) {
                return true;
            }
        }

        return false;
    }

    public static boolean existsAny(List list, Object... that) {
        return existsAny(list.toArray(), that);
    }

    public static boolean existsAny(Object[] arr, Object... that) {
        Object[] var2 = arr;
        int var3 = arr.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object each = var2[var4];
            if(in(each, that)) {
                return true;
            }
        }

        return false;
    }

    public static boolean in(Object[] with, Object that) {
        Object[] var2 = with;
        int var3 = with.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object each = var2[var4];
            if(each.equals(that)) {
                return true;
            }
        }

        return false;
    }

    public static boolean notIn(Object that, Object... with) {
        Object[] var2 = with;
        int var3 = with.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object each = var2[var4];
            if(each.equals(that)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isToday(Date date) {
        return TimeUnit.MILLISECONDS.toDays((new Date()).getTime()) == TimeUnit.MILLISECONDS.toDays(date.getTime());
    }

    public static boolean isDayAfterToday(Date date) {
        return TimeUnit.MILLISECONDS.toDays(date.getTime()) > TimeUnit.MILLISECONDS.toDays((new Date()).getTime());
    }

    public static boolean isDayBeforeToday(Date date) {
        return TimeUnit.MILLISECONDS.toDays(date.getTime()) < TimeUnit.MILLISECONDS.toDays((new Date()).getTime());
    }

    public static boolean isTodayWithoutGmtDepend(Date d) {
        Calendar req = Calendar.getInstance();
        req.setTime(d);
        Calendar now = Calendar.getInstance();
        return now.get(1) == req.get(1) && now.get(6) == req.get(6);
    }

    public static boolean isDayAfterTodayWithoutGmtDepend(Date d) {
        Calendar req = Calendar.getInstance();
        req.setTime(d);
        Calendar now = Calendar.getInstance();
        return now.get(1) < req.get(1) || now.get(1) == req.get(1) && now.get(6) < req.get(6);
    }

    public static boolean isDayBeforeTodayWithoutGmtDepend(Date d) {
        Calendar req = Calendar.getInstance();
        req.setTime(d);
        Calendar now = Calendar.getInstance();
        return now.get(1) > req.get(1) || now.get(1) == req.get(1) && now.get(6) > req.get(6);
    }

    public static boolean equalsDateWithoutTime(Date date1, Date date2) {
        if(date1 == null && date2 == null) {
            return true;
        } else if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.set(11, 0);
            cal1.set(12, 0);
            cal1.set(13, 0);
            cal1.set(14, 0);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            cal2.set(11, 0);
            cal2.set(12, 0);
            cal2.set(13, 0);
            cal2.set(14, 0);
            return cal1.equals(cal2);
        } else {
            return false;
        }
    }

    public static boolean isDateInInterval(Date date, Date dateFrom, Date dateTo) {
        return (date.after(dateFrom) || date.equals(dateFrom)) && (date.before(dateTo) || date.equals(dateTo));
    }

    public static double round(Double num, int precision) {
        return num == null?0.0D:(new BigDecimal(num.doubleValue())).setScale(2, 4).doubleValue();
    }

    public static boolean notBetween(String expected, String actual, int percentageTolerance) {
        return !between(new BigDecimal(expected), new BigDecimal(actual), percentageTolerance);
    }

    public static boolean between(String expected, String actual, int percentageTolerance) {
        return between(new BigDecimal(expected), new BigDecimal(actual), percentageTolerance);
    }

    public static boolean between(BigDecimal expected, BigDecimal actual, int percentageTolerance) {
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal tolerance = expected.divide(hundred).multiply(new BigDecimal(percentageTolerance));
        return expected.add(tolerance).compareTo(actual) == -1?false:expected.add(tolerance.negate()).compareTo(actual) != 1;
    }

    public static Integer tryGetMaxAllowedLengthOfReqularExpression(String regExp) {
        String resultNumberCandidate = regExp.replaceFirst(".*[]{][\\d]*[,]{1}", "").replaceFirst("}", "");

        try {
            return new Integer(resultNumberCandidate);
        } catch (NumberFormatException var3) {
            return null;
        }
    }

    public static void failOnSqlInjection(String in) throws IllegalStateException {
        if(in != null) {
            String inUpper = in.toUpperCase();
            if(inUpper.contains("SELECT ") || inUpper.contains("UPDATE ") || inUpper.contains("INSERT ") || inUpper.contains("DELETE ") || inUpper.contains("DROP ") || inUpper.contains("TRUNCATE ") || inUpper.contains("CREATE ") || inUpper.contains("EXEC ") || inUpper.contains("UNION ") || inUpper.contains("WHERE ") || inUpper.contains(" OR ") || inUpper.contains(" AND ")) {
                Logger.getAnonymousLogger().severe("error for '" + in + "'");
                throw new IllegalStateException("do you have some honey?");
            }
        }
    }

    public static boolean expiryDayExpired(Date expiryDay) {
        return expiryDayExpired(expiryDay, StringUtil.expiryDayFormat());
    }

    public static boolean expiryDayExpired(Date expiryDay, DateFormat expiryDayFormat) {
        try {
            String nowStr = expiryDayFormat.format(new Date());
            Date now = expiryDayFormat.parse(nowStr);
            return now.after(expiryDay);
        } catch (ParseException var4) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, "in param: expiryDay=" + String.valueOf(expiryDay) + ", expiryDayFormat=" + expiryDayFormat, var4);
            return true;
        }
    }

    public static List<?> listEnumByCode(Class<?> enumType, String... names) {
        if(enumType == null) {
            return null;
        } else {
            List<Object> result = new ArrayList();
            String[] var3 = names;
            int var4 = names.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String name = var3[var5];

                try {
                    Object enumValue = enumType.getMethod("getByCode", new Class[]{String.class}).invoke((Object)null, new Object[]{name});
                    result.add(enumType.cast(enumValue));
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
            }

            return result;
        }
    }

    public static boolean compareFirstSeveralAndLastSeveralChar(String mask, String number, int count) {
        if(!isEmpty(mask) && mask.length() >= count * 2) {
            String firstSeveral = mask.substring(0, count);
            String endSeveral = mask.substring(mask.length() - count, mask.length());
            String regExp = firstSeveral + ".*" + endSeveral;
            return number.matches(regExp);
        } else {
            return false;
        }
    }

    public static boolean compareLastSeveralDigitIfOnlyTheySpecifiyed(String mask, String number, int count) {
        return isNotEmpty(mask) && mask.length() >= count && (mask.length() >= count && number.endsWith(mask) || (!mask.substring(0, count).matches("\\d{4}") || number.startsWith(mask.substring(0, count))) && number.endsWith(mask.substring(mask.length() - count, mask.length())));
    }

    public static String trim(String st) {
        return st == null?null:st.trim();
    }

    public static boolean firstNotNull(Object[] arr) {
        return arr != null && arr.length > 0 && arr[0] != null;
    }

    public static void main(String[] args) {
        System.out.println("VerifyUtil");
    }
}
