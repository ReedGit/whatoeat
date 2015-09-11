package com.giot.platform.eat.util;

import java.util.List;
import java.util.Map;


public class RandomBusiness {
	
	final static int product_weight=5,decoration_weight=2,service_weight=3;
	
	public Map<String, String> randomBusiness(List<Map<String, String>> info){
		
		float[] weight=new float[info.size()]; 
		float sum=0;
		for(int i=0;i<info.size();i++){
			float product=Float.parseFloat(info.get(i).get("product_score"));
			float decoration=Float.parseFloat(info.get(i).get("decoration_score"));
			float service=Float.parseFloat(info.get(i).get("service_score"));
			float temp=product_weight*product+decoration_weight*decoration+service_weight*service;
			sum=sum+temp;
			weight[i]=sum;
		}
		float value=(float) (Math.random()*sum);
		int flag=search(weight, value);
		Map<String, String> map=info.get(flag);
		map.put("flag", String.valueOf(flag));
		return map;
		
	}
	
	public static int search(float[] arr, float key) {
	       for(int i=0;i<arr.length;i++){
	    	   if(key<arr[i])
	    		   return i;
	       }
	       return arr.length-1;
	   }

}
