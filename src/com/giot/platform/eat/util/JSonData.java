package com.giot.platform.eat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSonData {

	static private String apiUrl = "http://api.dianping.com/v1/business/find_businesses";
	static private String appKey = "4729999379";
	static private String secret = "05cc4ac27a97411a8619508eeaaecc74";

	private Map<String, String> infoMap = new HashMap<String, String>();
	private List<Map<String, String>> businessInfo = new ArrayList<Map<String,String>>();
	private Map<String, Float> location = new HashMap<String, Float>();

	public List<Map<String, String>> search(float latitude,
			float longitude, int range,int price) {
		if (!businessInfo.isEmpty()) {
			float t1 = location.get("latitude");
			float t2 = location.get("longitude");
			if (GetDistance(t2, t1, longitude, latitude) <= 100) {
				return businessInfo;
			}
		}
		location.put("latitude", latitude);
		location.put("longitude", longitude);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", String.valueOf(latitude));
		paramMap.put("longitude", String.valueOf(longitude));
		paramMap.put("category", "美食");
		paramMap.put("limit", "20");
		paramMap.put("radius", String.valueOf(range));
		paramMap.put("offset_type", "1");
		paramMap.put("sort", "7");
		paramMap.put("format", "json");
		paramMap.put("platform", "2");

		String requestResult = DemoApiTool.requestApi(apiUrl, appKey, secret,
				paramMap);
		Log.e("info",requestResult);
		try {
			JSONObject dataJson = new JSONObject(requestResult);
			JSONArray businesses = dataJson.getJSONArray("businesses");
			for (int i = 0; i < businesses.length(); i++) {
				JSONObject info = businesses.getJSONObject(i);
				String avgPrice = info.getString("avg_price");
				if(Integer.valueOf(avgPrice)>price)
					continue;
				String nameString = info.getString("name");
				String name = nameString.substring(0, nameString.indexOf("("));
				String address = info.getString("address");
				String productScore = info.getString("product_score");
				String decorationScore = info.getString("decoration_score");
				String serviceScore = info.getString("service_score");
				String categories = info.getString("categories");
				infoMap = new HashMap<String, String>();
				infoMap.put("name", name);
				infoMap.put("avg_price", avgPrice);
				infoMap.put("address", address);
				infoMap.put("product_score", productScore);
				infoMap.put("decoration_score", decorationScore);
				infoMap.put("service_score", serviceScore);
				infoMap.put("categories", categories);
				businessInfo.add(infoMap);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return businessInfo;
	}

	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

}
