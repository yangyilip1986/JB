package com.jiubang.p2p.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConvUtils {

	/**
	 * 转换值为String型。
	 *
	 * @param obj 值
	 * @return String型
	 */
	public static String convToString(Object obj) {
		if (obj == null) {
			return "";
		}
		if (StringUtils.isEmpty(obj.toString())) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 转换值为boolean型。
	 *
	 * @param obj 值
	 * @return boolean型
	 */
	public static boolean convToBool(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof byte[]) {
			return convBitToBool((byte[]) obj);
		}
		if ("1".equals(obj.toString())) {
			return true;
		} else if ("0".equals(obj.toString())) {
			return false;
		} else if ("true".equalsIgnoreCase(obj.toString())) {
			return true;
		} else if ("false".equalsIgnoreCase(obj.toString())) {
			return false;
		}
		return false;
	}

	/**
	 * 转换bit为boolean型。
	 *
	 * @param bytes byte
	 * @return boolean型
	 */
	public static boolean convBitToBool(byte[] bytes) {
		if (bytes == null) {
			return false;
		}
		return bytes[0] == 0x01 ? true : false;
	}

	/**
	 * 转换值为int型。
	 *
	 * @param obj 值
	 * @return int型
	 */
	public static int convToInt(Object value) {
		int intValue = 0;
		if (value == null) {
			return intValue;
		} else {
			try {
				intValue = Integer.parseInt(value.toString().trim().replace(",", ""));
			} catch (NumberFormatException ex) {
				return intValue;
			}
		}
		return intValue;
	}

	/**
	 * 转换值为int型。
	 *
	 * @param obj 值
	 * @return int型
	 */
	public static long convToLong(Object value) {
		long longValue = 0L;
		if (value == null) {
			return longValue;
		} else {
			try {
				longValue = Long.parseLong(value.toString().trim().replace(",", ""));
			} catch (NumberFormatException ex) {
				return longValue;
			}
		}
		return longValue;
	}

	/**
	 * 转换值为double型。
	 *
	 * @param obj 值
	 * @return double型
	 */
	public static double convToDouble(Object pValue) {
		double dblValue = 0D;
		if (pValue == null) {
			return dblValue;
		}
		try {
			dblValue = Double.parseDouble(String.valueOf(pValue).trim().replace(",", ""));
		} catch (Exception ex) {
			return dblValue;
		}
		return dblValue;
	}

	/**
	 * 转换值为BigDecimal型。
	 *
	 * @param obj 值
	 * @return BigDecimal型
	 */
	public static BigDecimal convToDecimal(Object value) {
		BigDecimal dec = new BigDecimal("0");
		if (value == null) {
			return dec;
		} else if (value instanceof BigDecimal) {
			dec = (BigDecimal) value;
		} else {
			try {
				dec = new BigDecimal(String.valueOf(value).trim().replace(",", ""));
			} catch (Exception ex) {
				return dec;
			}
		}
		return dec;
	}

	public static String convToMoney(Object pValue) {
		return String.format("%1$,.2f", convToDecimal(pValue));
	}

	/**
	 * 小数类型的String转int
	 * */
	public static int stringToInt(String string) {
		String str = string.substring(0, string.indexOf("."));
		int intgeo = Integer.parseInt(str);
		return intgeo;
	}

	/**
	 * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
	 *
	 * @param str
	 *            无逗号的数字
	 * @return 加上逗号的数字
	 */
	public static String fmtMicrometer(String text) {
		DecimalFormat df = null;
		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 计算两个日期之间相差的天数
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 根据身份证判断男女
	 * @return int 1:男 0:女
	 * */
	public static int sexForCard(String id_card) {
		if (id_card.length() == 18) {// 如果是18位身份证
			int i = Integer.parseInt(id_card.substring(16, 17));
			if (i % 2 == 0) {
				return 0;
			} else {
				return 1;
			}
		} else {// 如果是15位身份证
			int i = Integer.parseInt(id_card.substring(14, 15));
			if (i % 2 == 0) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
