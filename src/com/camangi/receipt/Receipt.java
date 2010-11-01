package com.camangi.receipt;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.camangi.receipt.logic.*;
import com.camangi.receipt.media.Media;

public class Receipt extends Activity {
	private String softVersion="v1.010b3";
    Button button0,button1,button2,button3,button4,button5,
    button6,button7,button8,button9,button_clear;
    public static TextView textview,textfirst,textfive;
	private String tag="tag";
	Media media;
	/**
	 * 使用者可以設定對獎所要輸入的號碼數︰2碼或3碼
	 */
	public static int limit=1;
	/**
	 * 裡面置放7組檢查碼
	 */
	public static String[] checknum;
	public static boolean got;
	/*
	 * 從SharePreference讀出預設的的logic運算模式,存放在此變數
	 */
	private String logic;

	/*
	 * 播放音效的版本變數
	 */
	public static String voice_version="regular";
	static AudioManager am;//用來調整音量Stream大小的啟始變數
	Toast toast;//用來調整音量的toast變數
	 /* static String getMonth;*/
	File f;//檔案
	/*
	 * 記錄對獎方式的邏輯變數<br/>
	 * 0:由右至左
	 * 1:由左至右
	 * 2:先輸入末3碼，再輸入剩餘8碼
	 */

	String month;//從txt檔裡抓出來的月份字串
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        
        //定義螢幕UI
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int ScreenHeight=display.getHeight();
		Log.i(tag, "getScreenHeight: "+String.valueOf(ScreenHeight));
		if(ScreenHeight==800|ScreenHeight==854){
			setContentView(R.layout.mainlayout800);
		}else if(ScreenHeight==480){
			setContentView(R.layout.mainlayout480);
		}else if(ScreenHeight==320){
			setContentView(R.layout.mainlayout320);
		}else{
			setContentView(R.layout.mainlayout800);
		}
        
     
        
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
       
        SharedPreferences sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
//        Log.i(tag,"data="+voicedata);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
        	voice_version=voicedata;
        }
        
        
        textview=(TextView) findViewById(R.id.text);
        textfirst=(TextView) findViewById(R.id.title_firstline);
      
        textfive=(TextView) findViewById(R.id.title_fiveline);
        
        
        button0=(Button) findViewById(R.id.button_0);
        button1=(Button) findViewById(R.id.button_1);
        button2=(Button) findViewById(R.id.button_2);
        button3=(Button) findViewById(R.id.button_3);
        button4=(Button) findViewById(R.id.button_4);
        button5=(Button) findViewById(R.id.button_5);
        button6=(Button) findViewById(R.id.button_6);
        button7=(Button) findViewById(R.id.button_7);
        button8=(Button) findViewById(R.id.button_8);
        button9=(Button) findViewById(R.id.button_9);
        button_clear=(Button) findViewById(R.id.button_clear);
        
        button0.setOnClickListener(new OnClickListener(){
			

			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("0", Receipt.this);
				}else if(logic.equals("LeftToRight")){
				    limit=8; //由左至右因為全部都要輸入，所以直接將界定值定為8
				    Type.leftToRight("0", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("0", Receipt.this);
				}
					
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("zero",Receipt.this,voice_version);				
			} 	
        });
        button1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("1", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("1", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("1", Receipt.this);
				}
				
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("one",Receipt.this,voice_version);	
			} 	
        });
        button2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("2", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("2", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("2", Receipt.this);
				}
				
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("two",Receipt.this,voice_version);	
			} 	
        });
        button3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("3", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("3", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("3", Receipt.this);
				}
				
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("three",Receipt.this,voice_version);	
			} 	
        });
        button4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("4", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("4", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("4", Receipt.this);
				}
						
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("four",Receipt.this,voice_version);	
			} 	
        });
        button5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("5", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("5", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("5", Receipt.this);
				}
					
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("five",Receipt.this,voice_version);	
			} 	
        });
        button6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("6", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("6", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("6", Receipt.this);
				}
			
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("six",Receipt.this,voice_version);	
			} 	
        });
        button7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("7", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("7", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("7", Receipt.this);
				}
					
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("seven",Receipt.this,voice_version);	
			} 	
        });
        button8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("8", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("8", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("8", Receipt.this);
				}
				
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("eight",Receipt.this,voice_version);	
			} 	
        });
        button9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("9", Receipt.this);
				}else if(logic.equals("LeftToRight")){
					Type.leftToRight("9", Receipt.this);
				}else if(logic.equals("LastThree")){
					Type.lastThree("9", Receipt.this);
				}
				
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("nine",Receipt.this,voice_version);	
			} 	
        });
        button_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				textview.setText("");
				Type.numtotal="";//將累積的數值暫存變數清空
				Type.first5total="";//將末三碼驗證的專屬變數︰前5碼暫存清除
				got=false;
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				media.createMedia("clear",Receipt.this,voice_version);	
			} 	
        });
        
        f= new File(this.getFilesDir()+"/receipt_head.txt");
        
        if(!f.exists()){
        	Log.i(tag, "into f.exist==false");
        	Log.i(tag, "BackStage.check3GConnectStatus(this)= "+String.valueOf(BackStage.check3GConnectStatus(this)));
        	Log.i(tag, "BackStage.checkWIFIStatus(this)= "+String.valueOf(BackStage.checkEnableingWifiStatus(this)));
        	 if (BackStage.check3GConnectStatus(this)==false&BackStage.checkEnableingWifiStatus(this)==false){
            	 
            	
//        		 Log.i(tag, "into new");
             	new AlertDialog.Builder(this)
     	    	.setTitle("沒有\"新的\"中獎號碼資料!")
     			.setIcon(R.drawable.warning)
     			.setMessage("系統必須連上網路取得資料...\n一有資料，\n之後就暫時不用再連線了")
     			.setPositiveButton("離開程式", new DialogInterface.OnClickListener() {

     				@Override
     				public void onClick(DialogInterface dialog, int which) {
     					finish();
     				}
     				})
     			
     			.show();	
             }else{
            	 BackStage.dataRequest(this,"head"); 
            	 generateEntity();
             }
        }else{
        	Log.i(tag, "f.exist=true");
        	generateEntity();
        }
       
    }


	@Override
	protected void onResume() {
//		Log.i(tag, "into onResume()");
		
		super.onResume();
		SharedPreferences sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

        }else{
        	voice_version=voicedata;
        }
        logic = sharedata.getString("logic", "RightToLeft");
        Log.i(tag, "get SharePreferences logic: "+logic);
       
        if(logic.equals("RightToLeft")){
//        	Log.i(tag, "into setText5: RightToLeft");
        	textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"最右邊\"");
            textfive.setText(finaltext5);
        }else if(logic.equals("LeftToRight")){	
//        	Log.i(tag, "into setText5: LeftToRight");
        	textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"最左邊\"");
            textfive.setText(finaltext5);
        }else if(logic.equals("LastThree")){
//        	Log.i(tag, "into setText5: LastThree");
        	textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"末三碼\"");
            textfive.setText(finaltext5);
        }
        Type.numtotal="";
        textview.setText("");
        media= new Media();
	}
 
    
    @Override//覆寫音量放大或縮小鍵為控制媒體音量鍵
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode(); 

            switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
//                    Log.i(tag, "volume_up");
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
//                    Log.i(tag, "getStreamvolume: "+String.valueOf(am.getStreamVolume(AudioManager.STREAM_MUSIC)));
//                    Log.i(tag,"getStreamMaxVolume: "+String.valueOf(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
                 	
                    if(toast!=null){
//                    	toast.cancel();
                    	toast.setText("最大音量︰ "+am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"\n目前音量︰ "+am.getStreamVolume(AudioManager.STREAM_MUSIC));
                    }else{
                    	toast=Toast.makeText(this, "最大音量︰ "+am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"\n目前音量︰ "+am.getStreamVolume(AudioManager.STREAM_MUSIC), Toast.LENGTH_SHORT);	
                    }
                    

                    toast.show();
                }
                //調整音量時還要再寫進SharedPreferences是因為有可能使用者在設定裡調成靜音，但後來調成有音量時，因為系統會在[設定][語音設定]設回靜音前的版本
                //如果沒有在調整音量時，馬上將SharedPreferences原為靜音設為之前的版本，會產生錯亂
                Editor sharedata = getSharedPreferences("data", 0).edit();
                sharedata.putString("voice",voice_version);
                sharedata.commit();
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
//                	Log.i(tag, "volume_down");
                	am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
                	
                	 if(toast!=null){
//                     	toast.cancel();
                		 toast.setText("最大音量︰ "+am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"\n目前音量︰ "+am.getStreamVolume(AudioManager.STREAM_MUSIC));
                     }else{
                     toast=Toast.makeText(this, "最大音量︰ "+am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"\n目前音量︰ "+am.getStreamVolume(AudioManager.STREAM_MUSIC), Toast.LENGTH_SHORT);
                     }
                	 toast.show();
                }
//                Log.i(tag, "action: "+action);
                //調整音量時還要再寫進SharedPreferences是因為有可能使用者在設定裡調成靜音，但後來調成有音量時，因為系統會在[設定][語音設定]設回靜音前的版本
                //如果沒有在調整音量時，馬上將SharedPreferences原為靜音設為之前的版本，會產生錯亂
                Editor sharedata1 = getSharedPreferences("data", 0).edit();
                sharedata1.putString("voice",voice_version);
                sharedata1.commit();
                return true;
            default:
                return super.dispatchKeyEvent(event);
            }
            
        }
    
    /**
     * 將文字檔轉化成checknum陣列
     */
    private void generateEntity(){
//    	Log.i(tag, "into generateEntity()");
		 FileInputStream fi = null;
         try {
 			fi=new FileInputStream(f);
 		} catch (FileNotFoundException e) {
 			Log.i(tag, "Exception: "+e.getMessage());
 		}
 		InputStreamReader is=new InputStreamReader(fi);
 	   BufferedReader br =new BufferedReader(is);
 	   String getnum = null;
         try {
        	month=br.readLine();
 			getnum=br.readLine();
 			Log.i(tag, "getnum: "+getnum);
 		} catch (IOException e) {
 			Log.i(tag, "IOException: "+e.getMessage());
 		}
 		checknum=getnum.split(",");
 		
 		 //month變數從txt檔裡讀出來以後，才可以將月份取代掉
        String finaltext1=textfirst.getText().toString().replace("#date", month);
        textfirst.setText(finaltext1);
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, 0, 0, "設定");
		menu.add(0, 1, 1, "關於");
		menu.getItem(0).setIcon(R.drawable.setting);
		menu.getItem(1).setIcon(R.drawable.about);
		

		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**描述 : 建立Menu清單的觸發事件*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				Intent intent=new Intent();
				int oldvolume=am.getStreamVolume(AudioManager.STREAM_MUSIC);
				
				Bundle bundle=new Bundle();
				bundle.putInt("oldvolume", oldvolume);
				bundle.putString("voice_version", voice_version);
				intent.putExtras(bundle);
				intent.setClass(this, Setting.class);
				startActivity(intent);
				break;
			case 1:
				new AlertDialog.Builder(this)
				.setMessage(getString(R.string.app_name)+" "+ softVersion +"\n作者 Camangi Corporation\n\n版權 2010")
				.setIcon(R.drawable.icon)
				.setTitle("關於")
				.setPositiveButton("問題回報", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"simon@camangi.com"}); 
						sendIntent.putExtra(Intent.EXTRA_TEXT, "請將意見填寫於此");
						sendIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name)+" 意見回報");
						sendIntent.setType("message/rfc822");
						startActivity(Intent.createChooser(sendIntent, "Title:"));
					}
				})
				.setNeutralButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
		
					}
				})
				.show();
				break;
	
		}
		return super.onOptionsItemSelected(item);
	}
}