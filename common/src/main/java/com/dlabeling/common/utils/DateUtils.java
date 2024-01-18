package com.dlabeling.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/17
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyyMMdd", "HH:mm:ss"};
    private static Date now;

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String currentDate(Date now) {
        return DateFormatUtils.format(now, "yyyyMMdd");
    }


    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static long dayBetween(Date date1, Date date2) {
        if (date1 == null) {
            return date2 == null ? 0 : (date2.getTime() / TimeUnit.DAYS.toMillis(1));
        }
        if (date2 == null) {
            return -date1.getTime() / TimeUnit.DAYS.toMillis(1);
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        int y1 = c1.get(Calendar.YEAR);
        int y2 = c2.get(Calendar.YEAR);
        int d1 = c1.get(Calendar.DAY_OF_YEAR);
        int d2 = c2.get(Calendar.DAY_OF_YEAR);
        if (y2 == y1) {
            return d2 - d1;
        } else if (y2 > y1) {
            int total = c1.getActualMaximum(Calendar.DAY_OF_YEAR) - d1 + c2.get(Calendar.DAY_OF_YEAR);
            c1.set(Calendar.DAY_OF_MONTH, 1);
            for (int y = y1 + 1; y < y2; y++) {
                c1.set(Calendar.YEAR, y);
                total += c1.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            return total;
        }
        int total = c2.getActualMaximum(Calendar.DAY_OF_YEAR) - d2 + c1.get(Calendar.DAY_OF_YEAR);
        c2.set(Calendar.DAY_OF_MONTH, 1);
        for (int y = y2 + 1; y < y1; y++) {
            c2.set(Calendar.YEAR, y);
            total += c2.getActualMaximum(Calendar.DAY_OF_YEAR);
        }
        return -total;
    }

    /**
     * 计算两个日期的时间差(不会是负数,只会计算时间差)
     * @param startTime     开始日期
     * @param endTime       结束日期
     * @return
     */
    public static Long differenceDays(Date startTime,Date endTime){
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(startTime);
//
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(endTime);
//        long time = startTime.getTime();


        // 计算日期差值
//        long differenceInMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
//        long differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000);
        long start = startTime.getTime();
        long end = endTime.getTime();

        long differenceInMillis = Math.abs(end - start);
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

//        System.out.println("日期差值为：" + differenceInDays + "天");
        return differenceInDays;
    }

    /**
     * 计算两个日期的时间差(当开始时间大于结束时间时,结果负数)
     * @param startTime     开始日期
     * @param endTime       结束日期
     * @return
     */
    public static Long dateDifferenceDays(Date startTime,Date endTime){
        LocalDate localStartDate = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 计算日期间隔
        long daysBetween = ChronoUnit.DAYS.between(localStartDate, localEndDate);
        return daysBetween;
    }

    /**
     * 计算两个日期的年份差
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long differenceYears(Date startTime, Date endTime) {
        LocalDate localStartDate = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 计算年份差距
        long yearDifference = ChronoUnit.YEARS.between(localStartDate, localEndDate);
        return yearDifference;
    }

    /**
     * 当前日期 + 1个月
     * @param date
     * @return
     * @throws ParseException
     */
    public static String subMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    /**
     * 当前日期 + 1个月
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date subMonth(Date date) throws ParseException {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    /**
     * 日期 减少 几个月
     * @param date 时间
     * @param month 减少的月份
     * @return 减少几月后的月份
     */
    public static Date subMonth(Date date,Integer month) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, -month);
        return rightNow.getTime();
    }

    /**
     * 当前日期 + N天
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date subDay(Date date,Integer day) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DATE, -day);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    /**
     * 当前日期 + N年
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date,Integer year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,year);
        return calendar.getTime();
    }

    /**
     * 将String类型时间转换成Date
     * @param stringDate    时间
     * @param format        格式
     * @return
     * @throws ParseException
     */
    public static Date StringByDate(String stringDate,String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( format);
        Date date =  formatter.parse(stringDate);
        return date;
    }

    /**
     * 当前日期是否在16点之前
     * @return
     */
    public static Boolean is16points(){
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 设置16:00作为参考时间
        LocalDateTime referenceTime = now.withHour(16).withMinute(0).withSecond(0).withNano(0);

        // 比较当前时间和参考时间
        if (now.isBefore(referenceTime)) {
            System.out.println("现在是16点之前！");
            return Boolean.TRUE;
        } else {
            System.out.println("现在是16点之后！");
            return Boolean.FALSE;
        }
    }

    /**
     * 当前日期是否在周一到周五
     * @return
     */
    public static Boolean isMondayToFriday(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 判断当前日期是否是周一到周五
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            System.out.println("今天是周末！");
            return Boolean.FALSE;
        } else {
            System.out.println("今天是工作日（周一到周五）！");
            return Boolean.TRUE;
        }
    }

    /**
     * 当前日期是否是周五
     * @return
     */
    public static Boolean isFriday(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 判断当前日期是否是周一到周五
        if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
            System.out.println("今天是周五！");
            return Boolean.TRUE;
        } else {
            System.out.println("今天不是周五！");
            return Boolean.FALSE;
        }
    }

    /**
     * 获取明天10点的时间戳
     * @return
     */
    public static long getTomorrow10PointsTimestamp(){
        // 获取当前日期和时间
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        // 将时间设置为明天上午10点
        calendar.add(Calendar.DAY_OF_MONTH, 1); // 添加一天
        calendar.set(Calendar.HOUR_OF_DAY, 10); // 设置小时为10
        calendar.set(Calendar.MINUTE, 0);       // 设置分钟为0
        calendar.set(Calendar.SECOND, 0);       // 设置秒数为0
        calendar.set(Calendar.MILLISECOND, 0);  // 设置毫秒数为0

        // 获取时间戳（以毫秒为单位）
        long timestamp = calendar.getTimeInMillis();

        System.out.println("明天10点的时间戳：" + timestamp);
        return timestamp;
    }

    /**
     * 获取下周一10点的时间戳
     * @return
     */
    public static long getNextMonday10PointsTimestamp(){
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        // 设置时间为下周一
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        // 设置时间为上午10点
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取时间戳（以毫秒为单位）
        long timestamp = calendar.getTimeInMillis();
        System.out.println("下周一10点的时间戳：" + timestamp);
        return timestamp;
    }


    /**
     * 获取距离指定星期几的天数
     * @param targetDay
     * @return
     */
    private static int getDaysUntilNextDayOfWeek(DayOfWeek targetDay) {
        LocalDateTime now = LocalDateTime.now();
        int currentDayOfWeekValue = now.getDayOfWeek().getValue();
        int targetDayValue = targetDay.getValue();
        int daysUntilNextDayOfWeek = targetDayValue - currentDayOfWeekValue;
        if (daysUntilNextDayOfWeek <= 0) {
            daysUntilNextDayOfWeek += 7; // 下周的同一天
        }
        return daysUntilNextDayOfWeek;
    }

    /**
     * 将时间戳转成 yyyy-MM-dd HH:mm:ss格式
     * @param timestamp     时间戳
     * @return
     */
    public static String getDateStringByTimestamp(Long timestamp){
        // 创建一个日期对象，使用时间戳作为参数
        Date date = new Date(timestamp);
        // 定义日期格式
        String pattern = "yyyy-MM-dd HH:mm:ss"; // 自定义日期格式，你可以根据需要修改
        // 创建一个SimpleDateFormat对象，并将日期格式传递给它
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        // 使用SimpleDateFormat的format()方法将日期转换为字符串
        String dateString = sdf.format(date);
        System.out.println("时间戳 " + timestamp + " 对应的日期时间是：" + dateString);
        return dateString;
    }

    /**
     * 获取当前时间年份
     * @return
     */
    public static int getNowYear(){
        return LocalDate.now().getYear();
    }

    /**
     * 获取上个月1号12点的时间
     * @return
     */
    public static Date getLastMonthDate(){
        // 获取当前日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 获取上个月的1号
        LocalDateTime previousMonthFirstDay = currentDateTime.minusMonths(1).withDayOfMonth(1);

        // 设置时间为12点0分0秒
        previousMonthFirstDay = previousMonthFirstDay.withHour(12).withMinute(0).withSecond(0).withNano(0);

        // 转换为Date类型
        Date previousMonthFirstDayDate = java.util.Date.from(previousMonthFirstDay.atZone(java.time.ZoneId.systemDefault()).toInstant());

        // 输出上个月1号12点0分0秒的Date类型时间
        System.out.println("上个月1号12点0分0秒的Date类型时间是：" + previousMonthFirstDayDate);
        return previousMonthFirstDayDate;
    }

    /**
     * 获取上个月的月份
     * @return
     */
    public static int getLastMonth() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取上个月的日期
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        // 获取上个月的月份
        int previousMonth = previousMonthDate.getMonthValue();
        // 输出上个月的月份
        System.out.println("上个月的月份是：" + previousMonth);
        return previousMonth;
    }

    /**
     * 获取时间年份 (如果传参为空，则返回当前时间年份)
     * @param date
     * @return
     */
    public static int getYear(Date date){
        LocalDate localDate;
        if (Objects.isNull(date)){
            localDate = LocalDate.now();
            return localDate.getYear();
        }
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    /**
     * 将年份改变为 指定年份
     * @return
     */
    public static Date converDateToNowYear(Date originalDate,String year){
        LocalDateTime orignalLocalDate = originalDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime currentDateTime = LocalDateTime.now()
                .withYear(Integer.parseInt(year))
                .withMonth(orignalLocalDate.getMonthValue())
                .withDayOfMonth(orignalLocalDate.getDayOfMonth());

        return Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 转换时间
     * @param year 年份
     * @param month 月份
     * @return 该年该月的第一天
     */
    public static Date convertToDate(Integer year,Integer month){
        LocalDate localDate = LocalDate.of(year, month, 1);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 判断日期是否正确
     * @param inputDate
     * @param dateFormatPattern
     * @return
     */
    public static boolean isValidDateFormat(String inputDate, String dateFormatPattern) {
        // 使用正则表达式匹配日期格式
        String regexPattern = "^" + dateFormatPattern.replace("yyyy", "\\d{4}").replace("MM", "\\d{2}").replace("dd", "\\d{2}") + "$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(inputDate);
        boolean matches = matcher.matches();
        if(!matches){
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
        try {
            LocalDate date = LocalDate.parse(inputDate, formatter);

            // 进一步根据年份判断日期是否合法
            int year = date.getYear();

            // 根据实际需求，这里可以根据情况对年份的范围进行限制
            if (year < 1000 || year > 9999) {
                return false;
            }

            // 判断月份和天数的范围
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            if (month < 1 || month > 12 || day < 1 || day > date.lengthOfMonth()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将 double类型 转换为 时间类型
     * @param dVal
     * @return
     */
    public static Date DoubleToDate(Double dVal){
        Date oDate = new Date();

//        @SuppressWarnings("deprecation")
        long localOffset = oDate.getTimezoneOffset() * 60000L; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        oDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return oDate;
    }

    /**
     * 获取年月对应的数字编号
     * @param date
     * @return
     */
    public static String getCurrentTimeOrderSn(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String orderSn = simpleDateFormat.format(date);
        return orderSn;
    }
}
