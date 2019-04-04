package com.jiubang.p2p.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeArray {
    public static String[] Repayment_Method = new String[] {
            "0", "到期还本付息", "每月等额本息", "按月付息到期还本"
    };

    public static String[] Guarantee_Mode = new String[] {
            "0", "本金保障", "每月等额本息", "本息保障"
    };

    public static String[] status = new String[] {
            "0", "还款中", "满标", "预约", "已结束", "抢购", "已还款", "审核中", "转让成功", ""
    };
    
    private static String[] type_string = new String[] { "不限", "直投直贷专区", "活动专区","债权转让专区" };
    private static String[] date_string = new String[] { "不限", "1-90天", "91-180天","181-360天", "361天以上" };
    private static String[] huibao_string = new String[] { "不限", "5%-7%", "7%-10%","10%-12%", "12%-15%" };
    private static String[] mode_string = new String[] { "不限", "一次性还本付息","按月付息到期还本", "等额本息" };
    private static String[] startmoney_string = new String[] { "不限", "1-100元", "100-500元","500-1000元", "1000元以上" };
    
    public static List<Map<String, String>> getFilterList(String str) {
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    	switch (str) {
		case "type":
			for (int i = 0; i < TypeArray.type_string.length; i++) {
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("item", TypeArray.type_string[i]);
				list.add(mapTemp);
			}
			break;
		case "date":
			for (int i = 0; i < TypeArray.date_string.length; i++) {
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("item", TypeArray.date_string[i]);
				list.add(mapTemp);
			}
			break;
		case "huibao":
			for (int i = 0; i < TypeArray.huibao_string.length; i++) {
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("item", TypeArray.huibao_string[i]);
				list.add(mapTemp);
			}
			break;
		case "mode":
			for (int i = 0; i < TypeArray.mode_string.length; i++) {
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("item", TypeArray.mode_string[i]);
				list.add(mapTemp);
			}
			break;
		case "startmoney":
			for (int i = 0; i < TypeArray.startmoney_string.length; i++) {
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("item", TypeArray.startmoney_string[i]);
				list.add(mapTemp);
			}
			break;
		}
    	return list;
    }

}
