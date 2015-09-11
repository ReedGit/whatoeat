package com.giot.platform.eat;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

	private static final String FILE_NAME = "search_params";
	
	public static final String RANGE_STR = "range";
	
	public static final String PRICE_STR = "price";
	
	public static final String EX_REC = "ex_checked";
	
	public static final int DEFAULT_RANGE = 2;
	
	public static final int DEFAULT_PRICE = 2;
	/**
	 * 保存参数
	 */
	public static void save(int range, int price, boolean ex,Context context) {
		//获得SharedPreferences对象
		SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.putInt(RANGE_STR, range);
		editor.putInt(PRICE_STR, price);
		editor.putBoolean(EX_REC, ex);
		editor.commit();
	}

	/**
	 * 获取各项参数
	 * @return
	 */
	public static Map<String, Object> getPerferences(Context context) {
		Map<String, Object> params = new HashMap<String, Object>();
		SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		params.put(RANGE_STR, preferences.getInt(RANGE_STR, DEFAULT_RANGE));
		params.put(PRICE_STR, preferences.getInt(PRICE_STR, DEFAULT_PRICE));
		params.put(EX_REC, preferences.getBoolean(EX_REC, false));
		return params;
	}
	
}

