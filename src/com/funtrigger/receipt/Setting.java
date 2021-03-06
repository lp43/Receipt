package com.funtrigger.receipt;


import com.funtrigger.receipt.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 設定畫面
 * @author Simon
 *
 */
public class Setting extends Activity {


	final static String tag ="tag";
	private ListView lv;
	/**
	 * 從Receipt.activity傳來的原Intent音量
	 */
	int oldvolume;
	/**
	 * 從Receipt.activity傳來的原Intent聲音版本設定
	 */
	String old_voice_version;
	/**
	 * 從Receipt.activity傳來的原Intent聲音版本設定,
	 * 轉成int值，讓SingleChoiceItems能產生預設值
	 */
	int oldvalue;
	/**
	 * 這個值是從SharePreference傳出來的值
	 */	
	int oldlogicset;
	/**
	 * 設定邏輯的Dialog視窗專用int變數,為onCreateDialog所用
	 */		
	final int SETLOGIC=0;
	/**
	 * 設定語音的Dialog視窗專用int變數,為onCreateDialog所用
	 */	
	final int SETVOICEVERSIONDIALOG=1;
	AlertDialog alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

//		Log.i(tag, "into onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		setTitle("設定");
		
		Bundle bundle =this.getIntent().getExtras();
		oldvolume=bundle.getInt("oldvolume");
		old_voice_version=bundle.getString("voice_version");
		Log.i(tag, "voice_version: "+old_voice_version);
		if(Receipt.am.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
			//判斷音量為0的程式邏輯一定要先寫，因為使用者可能後來自己將音量從實體按鍵調成0，那麼就會變成靜音
			oldvalue=3;
		}else if(old_voice_version.equals("regular")){
			oldvalue=0;
		}else if(old_voice_version.equals("big")){
			oldvalue=1;
		}else if(old_voice_version.equals("tw")){
			oldvalue=2;
		} 
		
		SharedPreferences sharedata = getSharedPreferences("data", 0);  
        String logicdata = sharedata.getString("logic", "LastThree");  
    	if(logicdata.equals("RightToLeft")){
			//判斷音量為0的程式邏輯一定要先寫，因為使用者可能後來自己將音量從實體按鍵調成0，那麼就會變成靜音
    		oldlogicset=0;
		}else if(logicdata.equals("LeftToRight")){
			oldlogicset=1;
		}else if(logicdata.equals("LastThree")){
			oldlogicset=2;
		}
        
        
		lv=(ListView) findViewById(R.id.list);

 		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_list_item_1, new String[]{"對獎邏輯設定","語音設定"});
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 
				switch(position){
				case 0:
//					Log.i(tag, "you press: "+position);
					showDialog(SETLOGIC);
					break;
				case 1:
//					Log.i(tag, "you press: "+position);
					showDialog(SETVOICEVERSIONDIALOG);
					break;
				}
	
				}	
			
		});

		
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			Log.i(tag, "press_keyBack");
//			Log.i(tag, "AlertDialog=null? "+String.valueOf(alert==null));	
				finish();
	 			Intent intent = new Intent();
				intent.setClass(Setting.this, Receipt.class);
				startActivity(intent);
			
		}
		return super.onKeyDown(keyCode, event);	
	}
	


	@Override
	protected Dialog onCreateDialog(int id) {
		final Editor sharedata = getSharedPreferences("data", 0).edit(); 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case SETLOGIC:
			builder
			.setTitle("請選擇你想使用的對獎輸入方式")
			.setSingleChoiceItems(new String[]{"由右至左輸入","由左至右輸入","末3碼輸入(建議)",},oldlogicset,new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					switch(which){
						case 0:
							sharedata.putString("logic","RightToLeft");
							sharedata.commit();
							dismissDialog(SETLOGIC);
							break;
						case 1:		
							sharedata.putString("logic","LeftToRight");
							sharedata.commit();
							dismissDialog(SETLOGIC);
							break;
						case 2:							
							sharedata.putString("logic","LastThree");
							sharedata.commit();
							dismissDialog(SETLOGIC);
							break;
					}
					
				}
				
			})
			.setPositiveButton("取消", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        }
			});
			alert = builder.create();
			return alert;
			
		case SETVOICEVERSIONDIALOG:
/*			final Editor sharedata = getSharedPreferences("data", 0).edit(); 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);*/

			builder
			.setTitle("請選擇語音")
			.setSingleChoiceItems(new String[]{"女聲正音版","我愛搖瑤版","開心大媽版","靜音"},oldvalue,new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					switch(which){
						case 0:
							
							if(Receipt.am.getStreamVolume(AudioManager.STREAM_MUSIC)!=0){
								//如果一開始是別的有聲音的版本，就將音量設成Intent過來的音量
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,oldvolume , 0);
							}else{
								//如果一開始的聲音是靜音，轉成有聲版時，聲音大小設成12
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,12 , 0);
							}
							
							sharedata.putString("voice","regular");
							sharedata.commit();
							dismissDialog(SETVOICEVERSIONDIALOG);
							break;
						case 1:
							if(Receipt.am.getStreamVolume(AudioManager.STREAM_MUSIC)!=0){
								//如果一開始是別的有聲音的版本，就將音量設成Intent過來的音量
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,oldvolume , 0);
							}else{
								//如果一開始的聲音是靜音，轉成有聲版時，聲音大小設成12
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,12 , 0);
							}
							
							sharedata.putString("voice","big");
							sharedata.commit();
							dismissDialog(SETVOICEVERSIONDIALOG);
							break;
						case 2:
							if(Receipt.am.getStreamVolume(AudioManager.STREAM_MUSIC)!=0){
								//如果一開始是別的有聲音的版本，就將音量設成Intent過來的音量
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,oldvolume , 0);
							}else{
								//如果一開始的聲音是靜音，轉成有聲版時，聲音大小設成12
								Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC,12 , 0);
							}
							
							sharedata.putString("voice","tw");
							sharedata.commit();
							dismissDialog(SETVOICEVERSIONDIALOG);
							break;
						case 3:
							Log.i(tag, "AlertDialog=null? "+String.valueOf(alert==null));
							Receipt.am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
							sharedata.putString("voice","mute");
							sharedata.commit();
							dismissDialog(SETVOICEVERSIONDIALOG);
							break;		
							
					}
					
				}
				
			})
			.setPositiveButton("取消", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        }
			});
			alert = builder.create();
			return alert;
		}
		return super.onCreateDialog(id);
	}

		

}
