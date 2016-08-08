package org.gradle.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtils {
	private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);
	public static String DATE_PATTERN = "yyyy-MM-dd";
	public static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static String DEFAULT_TIMEZONE = "UTC";
	
	public static Date getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
		String currentDateTimeString = DateFormatUtils.formatUTC(cal.getTime(), DATETIME_PATTERN);
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
		try {
			return sdf.parse(currentDateTimeString);
		} catch (ParseException e) {
			LOG.warn("parse date time format error.", e);
			return null;
		}
	}

	public static String milliSecondToHourString(int milliSecond) {
		String hourStr = "";

		double _hours = Math.round(Double.valueOf(milliSecond).doubleValue() / 1000 / 60 / 60);

		String[] _token = Double.toString(_hours).split("\\.");
		if (_token == null || _token.length < 1) {
			return "+00:00";
		}

		if (_token.length < 2) {
			String _h = _token[0];
			_token = new String[] { _h, "0" };
		}

		String _hour_str = _token[0];
		String _mins_str = String.valueOf((int) Math.round(Double.parseDouble("0." + _token[1]) * 60));
		hourStr = String.format("%02d", Integer.parseInt(_hour_str)) + ":"
				+ String.format("%02d", Integer.parseInt(_mins_str));

		if (_hours >= 0) {
			hourStr = "+" + hourStr;
		}

		return hourStr;
	}

	/**
	 * according to the range of assign date and the type of groupBy issue -day, week and month.
	 * 
	 * @param dateFrom
	 * @param toDate
	 * @param dateType
	 *            d for day, w for week, m from month
	 * @return Year_DisplayStr_DateStr, concatenate with three string, could be split by '_' into string array.
	 */
	public static List<String> getDatesBetweenDates(String dateFrom, String dateTo, String dateType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if (dateFrom == null || "".equalsIgnoreCase(dateFrom)) {
			dateFrom = sdf.format(new Date());
		}
		if (dateTo == null) {
			dateTo = sdf.format(new Date());
		}
		if (dateType == null || "".equalsIgnoreCase(dateType.trim())) {
			dateType = "d";
		}

		List<String> lastDateSet = new LinkedList<>();

		try {
			Date _fromDate = sdf.parse(dateFrom);
			Date _toDate = sdf.parse(dateTo);
			if (_fromDate.compareTo(_toDate) > 0) {
				return null;
			}

			while (_fromDate.compareTo(_toDate) < 1) {
				String lastDate = sdf.format(_fromDate);
				String displayStr = lastDate;
				
				// get last date by thisDate
				Calendar cal = Calendar.getInstance();
				cal.setTime(_fromDate);

				switch (dateType) {
				case "d":
					lastDate = sdf.format(_fromDate);
					displayStr = lastDate;
					break;

				case "w": {
					int assignDay = cal.get(Calendar.DAY_OF_WEEK);
					int leftDays = Calendar.SATURDAY - assignDay;
					cal.add(Calendar.DATE, leftDays);

					lastDate = sdf.format(cal.getTime());					
					displayStr = String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
				}
					break;

				case "m": {
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

					lastDate = sdf.format(cal.getTime());
					displayStr = cal.get(Calendar.YEAR) + "/" + String.format("%02d", cal.get(Calendar.MONTH) + 1);
				}
					break;

				default:
					break;
				}

				String returnStr = cal.get(Calendar.YEAR) + "_" + displayStr + "_" + lastDate ;

				// put the last date into lastDateSet
				if (!lastDateSet.contains(returnStr)) {
					lastDateSet.add(returnStr);
				}

				// add one date
				Calendar c = Calendar.getInstance();
				c.setTime(_fromDate);
				c.add(Calendar.DATE, 1);
				_fromDate = c.getTime();
			}
		} catch (ParseException e) {
			LOG.warn("parse date time format error.", e);
		}

		return lastDateSet;
	}
	
	public static String getPreCheckDate(String dateFrom, String groupBy) {
		String _preLastDate = "";		

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date _date = sdf.parse(dateFrom);

			Calendar cal = Calendar.getInstance();
			cal.setTime(_date);
			
			switch (groupBy) {
			case "d": 
				cal.add(Calendar.DATE, -1);
				break;
			case "w": 
				cal.add(Calendar.DATE, -7);
				break;
			case "m": 
				cal.add(Calendar.DATE, -31);
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				break;
			default:
				break;
			}
			
			_preLastDate = sdf.format(cal.getTime());
		} catch (ParseException e) {
			LOG.error(e.getMessage());
		}
		
		return _preLastDate;
	}
	
	public static String convertToDateText(Date date) {
        if(date == null) {
            return null;
        }
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(date);
    }
}
