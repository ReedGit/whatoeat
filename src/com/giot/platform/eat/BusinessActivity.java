package com.giot.platform.eat;

import java.util.List;
import java.util.Map;

import com.giot.platform.eat.util.SerializableList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BusinessActivity extends Activity {

	private ListView businessList;
	private List<Map<String, String>> info;
	private int[] id = new int[] { R.id.business_name, R.id.business_price,
			R.id.business_address, R.id.business_product,
			R.id.business_decoration, R.id.business_service,
			R.id.business_categories };
	private String[] words = new String[] { "name", "avg_price", "address",
			"product_score", "decoration_score", "service_score", "categories" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.business_activity);
		businessList=(ListView) findViewById(R.id.businessList);
		Bundle bundle = getIntent().getExtras();
        SerializableList serializableList = (SerializableList) bundle.get("list");
        info=serializableList.getList();
        SimpleAdapter simpleAdapter=new SimpleAdapter(getApplicationContext(), info, R.layout.business_list, words,id);
        businessList.setAdapter(simpleAdapter);
	}
}
