package com.camangi.receipt;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	
public class Setting extends Activity {


	final static String tag ="tag";
	private ListView lv;
	int oldvolume;//從Receipt傳來的原音量
	String old_voice_version;//從Receipt傳來的原聲音設定版本
	int oldvalue;//將Receipt傳來的原聲音設定版本轉成int值，讓SingleChoiceItems能有預設值
	final int SETLOGIC=0;
	final int SETVOICEVERSIONDIALOG=1;
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
		if(old_voice_version.equals("regular")){
			oldvalue=0;
		}else if(old_voice_version.equals("big")){
			oldvalue=1;
		}else if(old_voice_version.equals("tw")){
			oldvalue=2;
		}else if(old_voice_version.equals("mute")){
			oldvalue=3;
		}
		
		
		lv=(ListView) findViewById(R.id.list);

 		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_list_item_1, new String[]{"對獎方式設定","語音設定"});
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 
				switch(position){
				case 0:
//					Log.i(tag, "you press: "+position);
					
					break;
				case 1:
//					Log.i(tag, "you press: "+position);
					showDialog(SETVOICEVERSIONDIALOG);
					break;
				}
	
				}	
			
		});

		
	}
	
/*	@Override
	protected void onResume() {
		super.onResume();
		
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
				finish();
	 			Intent intent = new Intent();
				intent.setClass(Setting.this, Receipt.class);
				startActivity(intent);

		return super.onKeyDown(keyCode, event);	
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		/*case SETLOGIC:
			
			break;*/
		case SETVOICEVERSIONDIALOG:
			final Editor sharedata = getSharedPreferences("data", 0).edit(); 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder
			.setTitle("請選擇語音")
			.setSingleChoiceItems(new String[]{"女聲正音版","大胸搖瑤版","開喜大媽版","靜音"},oldvalue,new DialogInterface.OnClickListener(){

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
			AlertDialog alert = builder.create();
			return alert;
		}
		return super.onCreateDialog(id);
	}

		

}
