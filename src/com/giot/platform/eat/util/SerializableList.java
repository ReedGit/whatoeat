package com.giot.platform.eat.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableList implements Serializable{
	private List<Map<String, String>> list;

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	

}
