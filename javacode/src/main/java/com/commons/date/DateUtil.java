package com.commons.date;

import com.sohu.spaces.job.SynCdnJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getXDaysBefore(int i, String format) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -i);
		Date date = c.getTime();
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 得到多少天前0点的格林威治时间，比如dayBefore为0，得到今天的格林威治时间
	 * 
	 * @param dayBefore
	 * @return
	 */
	public static Long greenwich(int dayBefore) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, 0 - dayBefore);
		Long xDayBeforeTime = cal.getTimeInMillis();
		return xDayBeforeTime;
	}

	/**
	 * 将形如2014-01-03这样的时间转为比今天早几天的字符，比如今天是 2014-01-02，转为“1”
	 * defaultDay是转化失败后默认的天数(几天前)
	 * 
	 * @return
	 */
	public static String format1(String date, int defaultDay) {
		try {
			Date d = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.MILLISECOND, 0);
			int diff = (int) ((today.getTimeInMillis() - cal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
			return diff + "";
		} catch (Exception e) {
			// e.printStackTrace();
			return defaultDay + "";
		}
	}

	/**
	 * 将形如2014-01-03这样的时间校验一下，如果正确返回，不正确返回defaultDay天前的yyyy-MM-dd格式日期
	 * defaultDay是转化失败后默认的天数(几天前)
	 * 
	 * @return
	 */
	public static String check1(String date, int defaultDay) {
		try {
			sdf.parse(date);
			return date;
		} catch (Exception e) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0 - defaultDay);
			return sdf.format(cal.getTime());
		}
	}

	public static String getCompleteDay() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		String day = dateformat.format(new Date());
		return day;
	}

	@SuppressWarnings("deprecation")
	public static String getHour() {
		Date date = new Date();
		return String.valueOf(date.getHours());
	}

	/**
	 * 根据传入参数返回日期格式yyyy-MM-dd 00:00:00
	 * 
	 * @param i
	 *            设置提前多少天
	 * @return
	 */
	public static String getFormatDay(int i) {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		Date day = current.getTime();

		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String str = dateformat1.format(day);
		return str;
	}

	/**
	 * 根据传入参数返回日期格式yyyy-MM-dd
	 * 
	 * @param i
	 *            设置ref提前多少天
	 * @return
	 */
	public static String getFormatDay(String refDate, int i) {
		SimpleDateFormat dateformat0 = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		try {
			d1 = dateformat0.parse(refDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar current = Calendar.getInstance();
		current.setTime(d1);
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		Date day = current.getTime();

		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateformat1.format(day);
		return str;
	}

	/**
	 * 根据传入参数返回日期格式yyyyMMdd
	 * 
	 * @param i
	 *            设置提前多少天
	 * @return
	 */
	public static String getSimpleFormatDay(int i) {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		Date day = current.getTime();

		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateformat1.format(day);
		return str;
	}

	// 返回前一天开始的时间数
	public static long getSimpleFormatNumber(int i) {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		long time = current.getTimeInMillis();
		return time;
	}

	/**
	 * 输入秒值，返回具体日期
	 * 
	 * @param i
	 * @return yyyy-MM-dd HH:mm:ss String格式
	 */
	public static String getDateStr(long i) {
		Date date = new Date(i);
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String str = dateformat1.format(date);
		return str;
	}

	/**
	 * 输入i值返回i天前日期
	 * 
	 * @param i
	 * @return
	 */
	public static Date getFormatDate(int i) {

		Calendar current = Calendar.getInstance();
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		Date day = current.getTime();
		// SimpleDateFormat dateformat1 = new SimpleDateFormat(
		// // "yyyy-MM-dd HH:mm:ss");
		// "yyyy-MM-dd");
		// String str = dateformat1.format(day);
		// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		// try {
		// day=sdf.parse(str);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return day;

	}

	public static String get2HourAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -2);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date day = calendar.getTime();
		String str = dateformat1.format(day);
		return str;

	}

	public static String getHourTime() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		long timenow = System.currentTimeMillis();

		calendar.setTimeInMillis(timenow);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date day = calendar.getTime();
		String str = dateformat1.format(day);

		return str;

	}

	public static String getSimpleFormatDay(int i, String format) {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.DATE, current.get(Calendar.DATE) - i); // 设置为前一天
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		Date day = current.getTime();

		SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
		String str = dateformat1.format(day);
		return str;
	}

	// date转化为时间戳
	public static Long dateToTime(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(dateStr);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSimpleFormatNextDay(Date day, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.set(Calendar.DATE, 1); // 设置为前一天
		SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
		String str = dateformat1.format(calendar.getTime());
		return str;
	}

	public static String getSimpleFormatDay(Date day, String format) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
		String str = dateformat1.format(day);
		return str;
	}
	
	/**
     * <p>
     * Description: 返回前i小时的毫秒数
     * </p>
     *
     */
    public static long getMillisecondNumber(int i) {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) - i);// 设置为前i小时
        long time = current.getTimeInMillis();
        return time;
    }
    
    public static long getLastNhour(int n) {
        Date date = new Date();
        long time = date.getTime() - 1000L * 3600 * n;
        return time;
    }
    
    /**
     * 字符串转日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
        	log.error("can`t parse:" + dateStr + " format:" + formatStr, e);
        }
        return date;
    }
    
    /**
     * @param i
     * @return 距离i天的时间戳
     */
    public static Long getSimpleFormatNumber(String date, int i) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar current = Calendar.getInstance();
            current.setTime(sdf.parse(date));
            if (i >= 0) {
                current.set(Calendar.DATE, current.get(Calendar.DATE) + i); // 设置为后i天
            } else {
                current.set(Calendar.DATE, current.get(Calendar.DATE) - (-i)); // 设置为前i天
            }
            current.set(Calendar.MILLISECOND, 0);
            current.set(Calendar.HOUR_OF_DAY, 0);
            current.set(Calendar.MINUTE, 0);
            current.set(Calendar.SECOND, 0);
            Long time = current.getTimeInMillis();
            return time;
        } catch (Exception e) {
            log.error("can`t parse:" + date + " format:yyyy-MM-dd", e);
        }
        return null;
    }

    /**
     * @param i
     * @return 距离i小时的时间戳
     */
    public static Long getHourNumber(int i) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            log.error("err:", e);
        }
        return null;
    }
    
    /**
     * <p>
     * Description: 获取前i小时字符串
     * </p>
     *
     */
    public static String getHour(int i) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return df.format(calendar.getTime());
        } catch (Exception e) {
            log.error("err:", e);
        }
        return null;
    }
    
    public static String getCurrentTime(long t) {
        Date date = new Date(t);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    /**
     * 获取当前时间字符
     * @return
     */
    public static String getCurrentTimeStr() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    
    /**
     * 获取10分钟前的时间字符串
     * @param t
     * @return
     */
    public static String getTimeBeforeMinute(long t) {
        long ms = new Date().getTime() - (t*60*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(ms));
    }
    
    /**
     * @param day(like 2013-04-02)
     * @return day(like 2013-04-01)
     */
    public static String getYesterday(String day) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(day));
            cal.add(Calendar.DAY_OF_MONTH, -1);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            log.error("can`t parse:" + day + " format:yyyy-MM-dd", e);
        }
        return null;
    }
    
    public static String getTomorrowDay(String day) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(day));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            log.error("can`t parse:" + day + " format:yyyy-MM-dd", e);
        }
        return null;
    } 

	//获得前12个小时的时间字符串
	public static String getDate12HoursBefore() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -12);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String getDate12HoursBeforeBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -12);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	public static String getDate12HoursBeforeEnd() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date());
	}
	
	//前12小时到前一小时
	public static String getDateHoursBefore(int x) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -x);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getDateHoursBeforeEnd(int x) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -x);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}
	
	
	public static String getDateXHoursBefore(int x){
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -x);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
    }
	
	public static String getDateXDaysBefore(int x){
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -x);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
    }
	
	public static String getDateXMinutesBefore(int x){
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -x);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
    }
	
	public static Date getNowDate(){
		Date nowDate = new Date();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Date date = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			nowDate = sdf.parse(sdf.format(date)+":00");
		} catch (ParseException e) {
			log.error("parse exception");
		}
		return nowDate;
	}
	
	public static String getToday() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	//获得今天零点日期
	public static String getThisDay(){
		Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR, 0);  
        todayStart.set(Calendar.MINUTE, 0);  
        todayStart.set(Calendar.SECOND, 0);  
        todayStart.set(Calendar.MILLISECOND, 0);
		Date date = todayStart.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	
	public static String getDateStr(Date date){
	    try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } catch (Exception e) {
            log.error("getDateStr error",e);
            return null;
        }
	}
	
	public static Date getDayBeforeWhole(int n){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dfWhole = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date day = df.parse(getFormatDay(n));
            return day;
        } catch (Exception e) {
            log.info("getDayBeforeWhole n= " + n + " error",e);
            return null;
        }
    }
	public static void main(String[] args) {
        System.out.println(getDayBeforeWhole(1));
    }
}


