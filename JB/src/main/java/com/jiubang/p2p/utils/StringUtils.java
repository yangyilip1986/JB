package com.jiubang.p2p.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.CharRange;
import org.apache.commons.lang.CharSet;


/**
 * 字符串的实用程序类。
 *
 */
public class StringUtils {

	/**
	 * ASCII 文字（数字）
	 */
	private static CharRange digit = new CharRange('\u0030', '\u0039');

	/**
	 * ASCII 文字（英数字）
	 */
	private static CharSet alphaNumChar = CharSet.getInstance("0-9A-Za-z");

	/**
	 * 许可的mail地址
	 */
	private static CharSet allowedMailAddress = CharSet.getInstance(".@_0-9a-zA-Z-");

	private StringUtils() {
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static int toInt(String str, int defaultVal) {
		int ret = defaultVal;
		try {
			ret = Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return ret;
	}

	public static long toLong(String str, long defaultVal) {
		long ret = defaultVal;
		try {
			ret = Long.parseLong(str);
		} catch (NumberFormatException e) {
		}
		return ret;
	}

	public static String fill(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; ++i) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * Get hex string from byte array
	 */
	public static String toHexString(byte[] res) {
		StringBuffer sb = new StringBuffer(res.length << 1);
		for (int i = 0; i < res.length; i++) {
			String digit = Integer.toHexString(0xFF & res[i]);
			if (digit.length() == 1) {
				digit = '0' + digit;
			}
			sb.append(digit);
		}
		return sb.toString();
	}

	public static String getDirectoryDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	public static String denominateAppID(String contextPath) {

		if (StringUtils.isEmpty(contextPath)) {
			contextPath = "root";
		} else {
			contextPath = contextPath.substring(1);
		}

		return contextPath;
	}

	/**
	 * 对参数的字符串进行  native2ascii 处理的方法。<br>
	 * 第二个参数设置 null的话、使用系统属性 file.encoding设定。
	 *
	 * @param str 变换的字符串
	 * @param encoding 字符编码
	 * @return native2ascii 字符串
	 * @throws UnsupportedEncodingException 指定不受支持的字符编码
	 */
	public static String native2Ascii(String str)
		throws UnsupportedEncodingException {
		StringBuffer buf = new StringBuffer(str.length() * 6);
		char[] carray = str.toCharArray();
		for (int i = 0; i < carray.length; i++) {
			if (carray[i] > '\u007f') {
				buf.append("\\u");
				String hex4 = Integer.toHexString((int)carray[i]);
				if (hex4.length() == 3) {
					hex4 = "0" + hex4;
				} else if (hex4.length() == 2) {
					hex4 = "00" + hex4;
				} else if (hex4.length() == 1) {
					hex4 = "000" + hex4;
				}
				buf.append(hex4);
			}
			else {
				buf.append(carray[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * 通过传递指定的编码返回转换后的 Unicode 转义字符串的方法。
	 *
	 * @param str 转义的 Unicode ASCII 字符串
	 * @param encoding 字符编码
	 * @return ascii 变换的字符串
	 * @throws UnsupportedEncodingException 指定不受支持的字符编码
	 */
	public static String ascii2Native(String str, String encoding)
		throws UnsupportedEncodingException {
		String sysEnc = System.getProperty("file.encoding");
		if (encoding == null) {
			encoding = sysEnc;
		}
		System.setProperty("file.encoding", encoding);

		StringBuffer buf = new StringBuffer();
		char[] carray = str.toCharArray();
		for (int i = 0; i < carray.length; i++) {
			// 字符串是 \u9999 格式的情况
			if (carray[i] == '\\'
				&& i + 1 < carray.length
				&& carray[i + 1] == 'u' && i + 5 < carray.length
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 2]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 3]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 4]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 5]) > -1
			) {
				StringBuffer hex4 = new StringBuffer(4);
				for (int k = i + 2; k < i + 6; k++) {
					hex4.append(carray[k]);
				}
				buf.append((char)Integer.parseInt(hex4.toString(), 16));
				i = i + 5;
			} else {
				buf.append(carray[i]);
			}
		}

		System.setProperty("file.encoding", sysEnc);
		return buf.toString();
	}

	/**
	 * 取得UUID。
	 *
	 * @return UUID
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * 生成随机登录密码（默认8位长度）
	 * @return 登录密码的字符串
	 */
	public static String genUserPassword() {
		return genRandomNum(8);
	}

	/**
	 * 生成随机支付密码（默认8位长度）
	 * @return 支付密码的字符串
	 */
	public static String genPaymentPassword() {
		return genRandomNum(8);
	}

	/**
	 * 生成随机密码
	 * @param pwdLen 生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwdLen) {
		//35是因为数组是从0开始的，26个字母+10个 数字
		final int maxNum = 36;
		int i;  //生成的随机数
		int count = 0; //生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while(count < pwdLen){
			//生成随机数，取绝对值，防止 生成负数，
			i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				if (i%2 ==0) {
					pwd.append(Character.toUpperCase(str[i]));
				} else {
					pwd.append(str[i]);
				}
				count ++;
			}
		}

		return pwd.toString();
	}

	/**
	 * 字符串是数字的判定方法。
	 *
	 * @param str 字符串
	 * @return 数字 0-9 true
	 */
	public static boolean isDigit(String str) {
		boolean isdigit = false;
		if (StringUtils.isEmpty(str)) {
			return isdigit;
		}
		isdigit = true;
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (!digit.contains(cs[i])) {
				isdigit = false;
			}
		}
		return isdigit;
	}

	/**
	 * 字符串前后空格（全角，半角）的trim
	 *
	 * @param orgstr 文字列
	 * @return trim后的文字列
	 */
	public static String trim(String orgstr) {
		while (orgstr.startsWith(" ") || orgstr.startsWith("　")) {
			orgstr = orgstr.substring(1);
		}
		while (orgstr.endsWith(" ") || orgstr.endsWith("　")) {
			orgstr = orgstr.substring(0, orgstr.length() - 1);
		}
		return orgstr;
	}

	public static boolean isSettingNum(String str) {
		String regex = "^([1-9]\\d*)$|^(0|[1-9]\\d*)\\.(\\d{1,2})$|^0$";
		return match(regex, str);
	}

	public static boolean isAnnualRate(String str) {
//		String regex = "^[1-9][0-9]?(\\.\\d)?$";
//		String regex = "^[1-9][0-9]?([.]{1}[0-9]{1,2}){0,1}$";
//		String regex = "^[1-9]?[0-9]{1}([.]{1}[0-9]{1,2}){0,1}$";
		String regex = "^([1-9]\\d{0,1})$|^(0|[1-9]\\d{0,1})\\.(\\d{1,2})$|^0$|^100$";
		return match(regex, str);
	}

	public static boolean isPermillage(String str) {
//		String regex = "^[1-9][0-9]?(\\.\\d)?$";
//		String regex = "^[1-9][0-9]?([.]{1}[0-9]{1,2}){0,1}$";
		String regex = "^([1-9]\\d{0,2})$|^(0|[1-9]\\d{0,2})\\.(\\d{1,2})$|^0$|^1000$";
		return match(regex, str);
	}

	/**
	* @param regex
	* 正则表达式字符串
	* @param str
	* 要匹配的字符串
	* @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	*/
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean isLoginId(String str) {
		String regex = "^[a-zA-z][a-zA-Z0-9_]{3,15}$";
		return match(regex, str);
	}

	public static boolean isPasswordStrength(String str) {
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?-]+$)[`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?0-9a-zA-Z-]{8,20}$";
		return match(regex, str);
	}
	/**
	 * 密码验证
	 * @param str 输入的密码
	 * @param len 密码最小长度
	 * @return
	 */
	public static boolean isPasswordStrength(String str, int len) {

		if (len < 1) {
			len = 8;
		}
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?-]+$)[`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?0-9a-zA-Z-]{" + len +",20}$";
		return match(regex, str);
	}

	/**
	 * 功能：身份证的有效验证
	 *
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	public static boolean isIdCard(String IDStr)  {
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String Ai = "";
			try {
			// ================ 号码的长度 15位或18位 ================
			if (IDStr.length() != 15 && IDStr.length() != 18) {
				return false;
			}
			// =======================(end)========================

			// ================ 数字 除最后以为都为数字 ================
			if (IDStr.length() == 18) {
				Ai = IDStr.substring(0, 17);
			} else if (IDStr.length() == 15) {
				Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
			}
			// 身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。
			if (isAsciiAlphaNumCharOnly(Ai) == false) {
				return false;
			}
			// =======================(end)========================

			// ================ 出生年月是否有效 ================
			String strYear = Ai.substring(6, 10);// 年份
			String strMonth = Ai.substring(10, 12);// 月份
			String strDay = Ai.substring(12, 14);// 月份
			if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
				// 身份证生日无效
				return false;
			}
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				// 身份证生日不在有效范围。
				return false;
			}

			if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
				// 身份证月份无效
				return false;
			}
			if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
				// 身份证日期无效
				return false;
			}
			// =====================(end)=====================

			// ================ 地区码时候有效 ================
			Hashtable<String, String> h = getAreaCode();
			if (h.get(Ai.substring(0, 2)) == null) {
				// 身份证地区编码错误。
				return false;
			}
			// ==============================================

			// ================ 判断最后一位的值 ================
			int TotalmulAiWi = 0;
			for (int i = 0; i < 17; i++) {
				TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
			}
			int modValue = TotalmulAiWi % 11;
			String strVerifyCode = ValCodeArr[modValue];
			Ai = Ai + strVerifyCode;

			if (IDStr.length() == 18) {
				if (Ai.equals(IDStr.toLowerCase()) == false) {
					// 身份证无效，不是合法的身份证号码
					return false;
				}
			}
		} catch (NumberFormatException e) {
			return false;
		} catch (ParseException e) {
			return false;
		}
		// =====================(end)=====================
		return true;
	}

	/**
	 * 字符串是英数字的判定方法。
	 *
	 * @param str 字符串
	 * @return 英数字 true
	 */
	public static boolean isAsciiAlphaNumCharOnly(String str) {
		boolean asciiOnly = true;
		if (StringUtils.isEmpty(str)) {
			return asciiOnly;
		}
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (!alphaNumChar.contains(cs[i])) {
				asciiOnly = false;
			}
		}
		return asciiOnly;
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobileNum(String str) {
		String regex = "^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
		return match(regex, str);
	}

	/**
	 * 判断是否为特殊字符
	 * @param str
	 * @return
	 */
	public static boolean isSpecialChar(String str) {
		  String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(str);
		  return m.find();
	}

	/**
	 * 功能：设置地区编码
	 *
	 * @return Hashtable 对象
	 */
	private static Hashtable<String, String> getAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * mail地址是否许可的检查。
	 *
	 * @param mailAddress mail地址
	 * @return 许可  true
	 */
	public static boolean isAllowedMailAddress(String mailAddress) {
		if (StringUtils.isEmpty(mailAddress)) {
			return false;
		}
		boolean allowed = true;
		char[] cs = mailAddress.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (!allowedMailAddress.contains(cs[i])) {
				allowed = false;
				break;
			}
		}
		return allowed;
	}

	public static boolean isMoney(String str) {
		String regex = "^[0-9]+(.[0-9]{1,2})?$";
		return match(regex, str);
	}

	/**
	 * 检查充值金额是否正确的方法。<br />
	 * 充值的金额，100的倍数，不大于10,000,000。
	 *
	 * @param str 要验证的字符串
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isRechargeAmount(String str) {
		String regex = "^(\\d{1,5}|100000)0{2}$";
		return match(regex, str);
	}

	public static boolean isInArray(String[] arr, String val) {
		return Arrays.asList(arr).contains(val);
	}

	/**
	 * 字符串是数值(含小数)的判定方法。
	 *
	 * @param str 字符串
	 * @return 数值(含小数) true
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 字符串替换。
	 *
	 * @return 替换后字符串
	 */
	public static String replaceStr(String strInput, String strBefore, String strAfter) {
		if (strInput == null || "".equals(strInput)) {
			return "";
		} else {
			return strInput.replace(strBefore, strAfter);
		}
	}

	public static boolean isUrl(String str) {
		String regex = "^(\\w+:\\/\\/)?\\w+(\\.\\w+)+.*$";
		return match(regex, str);
	}

	public static boolean isTelNum(String str) {
		String regex = "\\d{3,5}-\\d{7,8}(-\\d{1,})?$";
		return match(regex, str);
	}

	/**
	 * 手机号中间4位用*显示。
	 *
	 * @return 替换后手机号
	 */
	public static String encryptMobile(String mobile) {
		if (mobile == null || "".equals(mobile)) {
			return "";
		} else {
			String ret = mobile.substring(0,3);
			ret = ret + "****";
			ret = ret + mobile.substring(7,11);
			return ret;
		}
	}

	public static String encryptEmail(String email) {
		if (email == null || "".equals(email)) {
			return "";
		} else {
			String ret = email.substring(0,1);
			ret = ret + "****";
			ret = ret + email.substring(email.length() - 1);
			return ret;
		}
	}

	public static String encryptIdCard(String idCard) {
		if (idCard == null || "".equals(idCard)) {
			return "";
		} else {
			String ret = idCard.substring(0,3);
			ret = ret + "***************";
			return ret;
		}
	}

	public static String newEncryptIdCard(String idCard) {
		if (idCard == null || "".equals(idCard)) {
			return "";
		} else {
			String ret = idCard.substring(0,6);
			ret = ret + "********";
			ret = ret + idCard.substring(14);
			return ret;
		}
	}

	public static String encryptBankCard(String bankCard) {
		if (bankCard == null || "".equals(bankCard)) {
			return "";
		} else {
			int len = bankCard.length();
			if(len > 7) {
				String ret = bankCard.substring(0,4);
				ret = ret + "******";
				ret = ret + bankCard.substring(len-3,len);
				return ret;
			}
			return bankCard;
		}
	}

	/**
	 * 字符串截取固定Byte。
	 *
	 * @return 截取后字符串
	 */
	public static String getSubString(String str, int length) {
		int count = 0;
		int offset = 0;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i + 1) + "...";
				}
			}
			if ((count == length + 1 && offset == 2)) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i) + "...";
				}
			}
		}
		return str;
	}

	public static String digitalUnitDisplay(BigDecimal digital) {
		String digitalStyleDisp = "元";
		if (digital.compareTo(new BigDecimal("0")) == 0) {
			return digitalStyleDisp;
		}

		// 大于亿的场合,以亿为单位;大于万的以万为单位;否则以元为单位
		if (digital.compareTo(new BigDecimal("100000000")) >= 0) {
			digitalStyleDisp = "亿";
		} else if (digital.compareTo(new BigDecimal("10000")) >= 0) {
			digitalStyleDisp = "万";
		} else {
			digitalStyleDisp = "元";
		}

		return digitalStyleDisp;
	}

	public static boolean isEmail(String str) {
		String regex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		return match(regex, str);
	}

	public static String getPeriodDisplayValue(String periodType) {
		String str = "";
		if (StringUtils.isEmpty(periodType)) {
			return str;
		}
		switch (periodType) {
			case "1":
				str = "个月";
				break;
			case "2":
				str = "个季度";
				break;
			case "3":
				str = "天";
				break;
			default:
				str = "个月";
				break;
		}
		return str;
	}

	/**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证 <br>
     *
     * key：  sex         男，女
     * key：  sexValue    1 ，0
     * key：  age
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCarInfo(String CardCode)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String year = CardCode.substring(6).substring(0, 4);// 得到年份
        String yue = CardCode.substring(10).substring(0, 2);// 得到月份
        // String day=CardCode.substring(12).substring(0,2);//得到日
        String sex = "";
        String sexValue = "";
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
            sexValue = "1";
        } else {
            sex = "男";
            sexValue = "0";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        // String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("sex", sex);
        map.put("sexValue", sexValue);
        map.put("age", age);
        return map;
    }

	/**
	 * 设置金额格式(去.00或.X0)。
	 * @param amount 金额
	 * @return 金额
	 */
    public static String setAmountStyle(Object amount) {
    	if(amount == null) {
    		return "0";
    	}

    	if(ConvUtils.convToString(amount).indexOf(".") < 0) {
    		return ConvUtils.convToString(amount);
    	}

		String[] arrayAmount = ConvUtils.convToString(amount).split("\\.");
		String strAmount = "";
		if(arrayAmount.length == 2) {
			if("00".equals(arrayAmount[1])) {
				strAmount = arrayAmount[0];
			} else if("0".equals(arrayAmount[1].substring(1,2))) {
				strAmount = arrayAmount[0] + "." + arrayAmount[1].substring(0,1);
			} else {
				strAmount = ConvUtils.convToString(amount);
			}
		} else {
			strAmount = ConvUtils.convToString(amount);
		}
		return strAmount;
	}

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isJsonEmpty(CharSequence input) {
        if (input == null || "".equals(input)||"null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    
	/**
	 * 字符串截取固定Byte。（国庆改版用）
	 *
	 * @return 截取后字符串
	 */
	public static String getSubStringForActive(String str, int length) {
		int count = 0;
		int offset = 0;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain ==0) {
					return str;
				} else {
					return str.substring(0, i + 1) + "...";
				}
			}
			if ((count == length + 1 && offset == 2)) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain ==0) {
					return str;
				} else {
					return str.substring(0, i) + "...";
				}
			}
		}
		return str;
	}
}
