package com.eka.supplierconnect.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import org.apache.commons.lang3.time.FastDateFormat;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;

public class DateUtility {

	final static Logger logger = ESAPI.getLogger(DateUtility.class);

	/**
	 * Formats are added to the array "In order of preference"
	 */
	private static FastDateFormat[] defaultDateFormats = { FastDateFormat.getInstance("EEE MMM dd HH:mm:ss Z yyyy"),
			FastDateFormat.getInstance("MM/dd/yyyy"), FastDateFormat.getInstance("MM-dd-yyyy"),
			FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss"), FastDateFormat.getInstance("MM-dd-yyyy HH:mm:ss"),
			FastDateFormat.getInstance("dd/MM/yyyy"), FastDateFormat.getInstance("dd-MM-yyyy"),
			FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss"), FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss") };

	public boolean isNull(Date obj) {
		return obj == null;
	}

	/**
	 * Builds and returns date object of the input date string
	 * 
	 * @param date       Date as string
	 * @param dataFormat Format of the Dates
	 * 
	 * @throws ParseException
	 */
	public Date toDate(String date, String dateFormat) throws ParseException {
		return getDateFormatter(dateFormat).parse(date);
	}

	public String format(String date, String fromFormat, String toFormat) throws ParseException {
		return getDateFormatter(fromFormat).format(toDate(date, toFormat));
	}

	public String format(Date date, String dateFormat) {
		return getDateFormatter(dateFormat).format(date);
	}

	public boolean after(Date dateAfter, Date date) {
		return dateAfter.after(date);
	}

	public boolean before(Date dateBefore, Date date) {
		return dateBefore.before(date);
	}

	public int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public String getMonthName(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return Month.of(cal.get(Calendar.MONTH) + 1).name();
	}

	public int getMonth(String dateStr, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(dateStr, dateFormat));
		return cal.get(Calendar.MONTH) + 1;
	}

	public String getMonthName(String dateStr, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(dateStr, dateFormat));
		return Month.of(cal.get(Calendar.MONTH) + 1).name();
	}

	public int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public int getYear(String date, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(date, dateFormat));
		return cal.get(Calendar.YEAR);
	}

	public int getDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}

	public int getDate(String date, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(date, dateFormat));
		return cal.get(Calendar.DATE);
	}

	public int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public int getWeekOfMonth(String date, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(date, dateFormat));
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public int getWeekOfYear(String date, String dateFormat) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate(date, dateFormat));
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public long differenceInDays(Date date1, Date date2) {
		LocalDateTime localDate1 = getLocalDateTime(date1);
		LocalDateTime localDate2 = getLocalDateTime(date2);
		return ChronoUnit.DAYS.between(localDate1, localDate2);
	}

	public long differenceInMonths(Date date1, Date date2) {
		LocalDateTime localDate1 = getLocalDateTime(date1);
		LocalDateTime localDate2 = getLocalDateTime(date2);
		return ChronoUnit.MONTHS.between(localDate1, localDate2);
	}

	public long differenceInYears(Date date1, Date date2) {
		LocalDateTime localDate1 = getLocalDateTime(date1);
		LocalDateTime localDate2 = getLocalDateTime(date2);
		return ChronoUnit.YEARS.between(localDate1, localDate2);
	}

	public Date minusDays(Date date, long days) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.minusDays(days));
	}

	public Date minusMonths(Date date, long months) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.minusMonths(months));
	}

	public Date minusYears(Date date, long years) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.minusYears(years));
	}

	public Date plusDays(Date date, long days) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.plusDays(days));
	}

	public Date plusMonths(Date date, long months) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.plusMonths(months));
	}

	public Date plusYears(Date date, long years) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.plusYears(years));
	}

	private LocalDateTime getLocalDateTime(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	private Date getDateInstance(LocalDateTime dateTime) {
		Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	public Date getMonthAndYear(Date date) throws ParseException {
		FastDateFormat formatter = getDateFormatter("MM-yyyy");
		return toDate(formatter.format(date), "MM-yyyy");
	}

	public Date getMonthAndYear(String date, String dateFormat) throws ParseException {
		FastDateFormat formatter = getDateFormatter("MM-yyyy");
		return toDate(formatter.format(toDate(date, dateFormat)), "MM-yyyy");
	}

	public Date minusMinutes(Date date, long minutes) throws ParseException {
		LocalDateTime localDateTime = getLocalDateTime(date);
		return getDateInstance(localDateTime.minusMinutes(minutes));
	}

	private FastDateFormat getDateFormatter(String dateFormat) {
		return FastDateFormat.getInstance(dateFormat);
	}

	public Date getLastDayOfTheMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DATE, lastDate);
		logger.debug(Logger.EVENT_SUCCESS, "Last Day : " + calendar.getTime());
		return calendar.getTime();
	}

	public static String dateFormat(String dtt) {
		try {
			String date = dtt.substring(0, dtt.length() - 6);
			String offset = dtt.substring(dtt.length() - 6, dtt.length());
			LocalDateTime dat = LocalDateTime.parse(date);
			// Instant ist = dat.toInstant(ZoneOffset.of("+05:30"));
			Instant ist = dat.toInstant(ZoneOffset.of(offset));
			Date dt = Date.from(ist);
			logger.debug(Logger.EVENT_SUCCESS, "stringDate:" + dt);
			SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
			logger.debug(Logger.EVENT_SUCCESS, sf.format(dt));
			return sf.format(dt);
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE,
					ESAPI.encoder().encodeForHTML("Unsupported Date Format Exception" + e.getLocalizedMessage()), e);
			return null;
		}
	}
}
