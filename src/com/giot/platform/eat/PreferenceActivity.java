package com.giot.platform.eat;


import java.util.Map;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class PreferenceActivity extends Activity implements SeekBar.OnSeekBarChangeListener{

	private static final String TAG = "PreferenceActivity";
	

	// 与“系统默认SeekBar”对应的TextView
    private TextView rangeText, priceText;
    // “系统默认SeekBar”
    private SeekBar seekBarRange, seekBarPrice;
    
    private Switch sw;
    
    private Button saveButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_activity);
        
        rangeText = (TextView) findViewById(R.id.range_value);
        seekBarRange = (SeekBar) findViewById(R.id.seekbar_range);
        seekBarRange.setOnSeekBarChangeListener(this);
        //seekBarRange.setProgress(PreferenceUtils.DEFAULT_RANGE);
        
        priceText = (TextView) findViewById(R.id.price_value);
        seekBarPrice = (SeekBar) findViewById(R.id.seekbar_price);
        seekBarPrice.setOnSeekBarChangeListener(this);
        //seekBarPrice.setProgress(PreferenceUtils.DEFAULT_PRICE);
       
        sw = (Switch) findViewById(R.id.recent_sw);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				save();
			}
        	
        });
      
        load();
    }
    
    
    /**
     * 在布局中按钮控件指定的onClick方法
     * @param v
     */
    public void save() {
    	int range = seekBarRange.getProgress()+300;
    	int price = seekBarPrice.getProgress();
    	Boolean swChecked =  sw.isChecked();
    	
    
    	PreferenceUtils.save(range, price, swChecked, this);
    	Toast.makeText(this, "保存当前设置", Toast.LENGTH_SHORT).show();
    	finish();
    }
    
    private void load()
    {
    	Map<String, Object> map = PreferenceUtils.getPerferences(this);
    	int range = (Integer) map.get(PreferenceUtils.RANGE_STR);
    	int price = (Integer) map.get(PreferenceUtils.PRICE_STR);
    	boolean exChecked = (Boolean) map.get(PreferenceUtils.EX_REC);
    	seekBarRange.setProgress(range-300);
    	seekBarPrice.setProgress(price);
    	sw.setChecked(exChecked);
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        
    }

    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        Log.d(TAG, "seekid:"+seekBar.getId()+", progess"+progress);
        switch(seekBar.getId()) {
            case R.id.seekbar_range:{
                // 设置“与系统默认SeekBar对应的TextView”的值
            	rangeText.setText(String.valueOf((seekBar.getProgress() + 300) + getResources().getString(R.string.range_metre)));
                break;
            }
            case R.id.seekbar_price: {
                // 设置“与自定义SeekBar对应的TextView”的值                
                priceText.setText("￥"+ seekBar.getProgress());
                break;
            }
            default:
                break;
        }
    }
}



