package com.giot.platform.eat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.LocationClient;
import com.giot.platform.eat.ShakeListener.OnShakeListener;
import com.giot.platform.eat.util.JSonData;
import com.giot.platform.eat.util.RandomBusiness;
import com.giot.platform.eat.util.SerializableList;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChooseActivity extends Activity {

	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	JSonData jSonData = new JSonData();
	private LocationClient mLocationClient;

	private int price, range;
	private boolean exChecked;

	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private ImageButton filterButton, dzdpButton;

	private SoundPool sndPool;
	private HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
	private List<Map<String, String>> info;
	private List<Map<String, String>> businessList = new ArrayList<Map<String, String>>();
	private Map<String, String> resultMap = new HashMap<String, String>();
	private Map<String, Object> map;
	private float latitude, longitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_activity);

		mLocationClient = ((EatApplication) getApplication()).mLocationClient;
		mLocationClient.start();
		latitude = ((EatApplication) getApplication()).latitude;
		longitude = ((EatApplication) getApplication()).longitude;
		Log.e("oncreat", latitude + "," + longitude);
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);

		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);

		filterButton = (ImageButton) findViewById(R.id.filter_button);
		filterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ChooseActivity.this,
						PreferenceActivity.class);
				startActivity(intent);
			}

		});
		dzdpButton = (ImageButton) findViewById(R.id.dzdp_id);
		dzdpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChooseActivity.this,
						BusinessActivity.class);
				SerializableList serializableList = new SerializableList();
				serializableList.setList(businessList);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", serializableList);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

		loadSound();
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				// Toast.makeText(getApplicationContext(),
				// "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
				startAnim(); // 开始 摇一摇手掌动画
				mShakeListener.stop();
				sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 0,
						(float) 1.2);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// Toast.makeText(getApplicationContext(),
						// "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！",
						// 500).setGravity(Gravity.CENTER,0,0).show();
						map = PreferenceUtils.getPerferences(getApplicationContext());
						range = (Integer) map.get(PreferenceUtils.RANGE_STR);
						price = (Integer) map.get(PreferenceUtils.PRICE_STR);
						exChecked = (Boolean) map.get(PreferenceUtils.EX_REC);

						latitude = ((EatApplication) getApplication()).latitude;
						longitude = ((EatApplication) getApplication()).longitude;

						info = jSonData.search(latitude, longitude, range,price);
						mVibrator.cancel();
						mShakeListener.start();
						if (info.isEmpty()) {
							Toast.makeText(getApplicationContext(), "没有摇到哦",
									Toast.LENGTH_SHORT).show();
							sndPool.play(soundPoolMap.get(2), (float) 1,
									(float) 1, 0, 0, (float) 1.0);
							return;
						}
						sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1,
								0, 0, (float) 1.0);
						RandomBusiness result = new RandomBusiness();
						resultMap = result.randomBusiness(info);
						if (exChecked && !businessList.isEmpty()) {
							if (resultMap.get("name").equals(
									businessList.get(businessList.size() - 1)
											.get("name"))) {
								info.remove(Integer.parseInt(resultMap
										.get("flag")));
								resultMap = result.randomBusiness(info);
							}
							if (businessList.size() >= 2) {
								if (resultMap.get("name").equals(
										businessList.get(
												businessList.size() - 2).get(
												"name"))) {
									info.remove(Integer.parseInt(resultMap
											.get("flag")));
									resultMap = result.randomBusiness(info);
								}
							}
						}
						businessList.add(resultMap);
						Toast.makeText(getApplicationContext(),
								resultMap.get("name"), Toast.LENGTH_LONG)
								.show();

					}
				}, 500);
			}
		});

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public void onResume() {
		super.onResume();
		mShakeListener.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		mShakeListener.stop();
	}

	private void loadSound() {

		sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		new Thread() {
			public void run() {
				try {
					soundPoolMap.put(
							0,
							sndPool.load(
									getAssets().openFd(
											"sound/shake_sound_male.mp3"), 1));

					soundPoolMap.put(1, sndPool.load(
							getAssets().openFd("sound/shake_match.mp3"), 1));
					soundPoolMap.put(
							2,
							sndPool.load(
									getAssets().openFd(
											"sound/shake_nomatch.mp3"), 1));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato() { // 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

	public void shake_activity_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void linshi(View v) { // 标题栏
		startAnim();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

}
