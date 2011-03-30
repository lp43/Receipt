package com.funtrigger.receipt;

import com.google.ads.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.funtrigger.receipt.logic.*;
import com.funtrigger.tools.Media;
import com.funtrigger.tools.ResponseDialog;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdRequest.ErrorCode;


/**
 * [統一發票愛我]程式主檔
 * @author simon
 *
 */
public class Receipt extends Activity {

	private String softVersion="v1.0.9";
    Button button0,button1,button2,button3,button4,button5,
    button6,button7,button8,button9,button_clear;
    public static TextView textview,textfirst,textfive;
	private static String tag="tag";
	Media media;
	/**
	 * 裡面放置7組檢查碼
	 */
	public static String[] checknum;
	/**
	 * 這個變數專用在末三碼核對時，
	 * 若末三碼相符，got值會為true,
	 * 否則預設為0
	 */
	private static boolean got;
	/**
	 * 從SharedPreferences讀出預設的的logic運算模式,存放在此變數
	 */
	private static String logic;

	/**
	 * 播放音效的版本變數
	 */
	public static String voice_version="regular";
	/**
	 * 用來調整音量Stream大小的啟始變數
	 */
	static AudioManager am;
	/**
	 * 用來調整音量的toast變數
	 */
	Toast toast;
	File f;//檔案
	/**
	 * 從txt檔裡抓出來的月份字串
	 */
	String month;
	/**
	 * 取得領獎期間
	 */
	String getmoneyperoid;
	/**
	 * 想要對獎的日期,當期為head,上一期為head2
	 */
	String iwantcheckmonth;
	/**
	 * onCreateDiloag視窗的第1個ID值視窗︰設定月份視窗,
	 * 設為final 0
	 */
	private final int SETMONTH=0;
	/**
	 * onCreateDiloag視窗的第2個ID值視窗︰更新畫面時提示用,
	 * 設為final 1
	 */
	private final int UPDATENUM=1;
	/**
	 * onCreateDiloag視窗的第3個ID值視窗︰初始資料下載畫面時提示用,
	 * 設為final 2
	 */	
	private final int DOWNLOAD=2;
	/**
	 * 從sharePreference取出來的欲核對的發票月份
	 */
	private int oldmonthsetted;
	/**
	 * 拿來給onCreateDiloag產生視窗用的值
	 */
	AlertDialog alert;
	/**
	 * SharedPreferences屬性的sharedata設為公用變數
	 */
	SharedPreferences sharedata;
	Handler handler;
	/**
	 * 讓onCreateDialog()建立ProgressDialog時用
	 * 這個文檔裡被UPDATENUM和DOWNLOAD兩個值使用
	 */
	ProgressDialog progressdialog;
	/**
	 * 讓10個數字Button放進來該Map，
	 * 好讓setBadButton時，可以用迴圈快速設定
	 */
	static Map<String,Button> buttonMap;
	/**
	 * 該變數裡放著XX圖形
	 */
	static Drawable drawable;
	/**
	 * cutter = 特獎的最後一筆是第幾筆？起始值為1(一般這個值為3，因為有3筆特獎)
	 * 
	 * 告訴程式這次特獎(號碼需全中)與頭獎(末3碼即可中)的實體切割點
	 * 一般來說，都是前3筆(<4)是特獎，第4筆(>3)開始是頭獎，
	 * 但2011-03-25很特別，改成前2筆是特獎，後3筆是頭獎，
	 * 所以在這裡增加一個變數，讓我以後只要改這組數值，就可以輕鬆告訴程式
	 * 哪邊是特獎和頭獎的切割點
	 */
	public static int cutter = 0;
	AdView  adView = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        drawable=this.getResources().getDrawable(R.drawable.x);
        
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
		

		
		callAdView();
		
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
       
        sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        
        //如果從SharePreference裡取出的voicedata值是mute,則調成最小聲,否則將值指派給voice_version變數
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
        
        buttonMap=new HashMap<String,Button>();
        buttonMap.put("0", button0);
        buttonMap.put("1", button1);
        buttonMap.put("2", button2);
        buttonMap.put("3", button3);
        buttonMap.put("4", button4);
        buttonMap.put("5", button5);
        buttonMap.put("6", button6);
        buttonMap.put("7", button7);
        buttonMap.put("8", button8);
        buttonMap.put("9", button9);
        
        button0.setOnClickListener(new OnClickListener(){


			@Override
			public void onClick(View v) {
				ResponseDialog.cancelToast();
				setNormalButton();
				if(logic.equals("RightToLeft")){
					Type.rightToLeft("0", Receipt.this);
				}else if(logic.equals("LeftToRight")){
//				    limit=8; //由左至右因為全部都要輸入，所以直接將界定值定為8
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setNormalButton();
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
				ResponseDialog.cancelToast();
				setBadButton();
				textview.setText("");
				Type.resetNumTotal();//將累積的數值暫存變數清空
				Type.first5total="";//將末三碼驗證的專屬變數︰前5碼暫存清除
				got=false;//將LastThree專屬的中末3碼的got變數回復為false
				
				resetTextfive();
				
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
       
    }


	@Override
	protected void onResume() {
		Log.i(tag, "into onResume()");
		handler =new Handler();
		
		sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
        	voice_version=voicedata;
        }
        logic = sharedata.getString("logic", "LastThree");
        Log.i(tag, "get SharePreferences logic: "+logic);
       
        resetTextfive();
        Type.resetNumTotal();
        textview.setText("");//數字框清空
        media= new Media();//建立media檔
        
        String iwantcheckmonth = sharedata.getString("iwantcheckmonth", "head");
        
        f= new File(this.getFilesDir()+"/receipt_"+iwantcheckmonth+".txt");
        
        if(!f.exists()){
        	Log.i(tag, "into f.exist==false");
        	Log.i(tag, "BackStage.check3GConnectStatus(this)= "+String.valueOf(BackStage.check3GConnectStatus(this)));
        	Log.i(tag, "BackStage.checkWIFIStatus(this)= "+String.valueOf(BackStage.checkEnableingWifiStatus(this)));
        	 if (BackStage.check3GConnectStatus(this)==false&BackStage.checkEnableingWifiStatus(this)==false){
            	 
            	
//        		 Log.i(tag, "into new");
             	new AlertDialog.Builder(this)
     	    	.setTitle("沒有中獎號碼資料!")
     			.setIcon(R.drawable.warning)
     			.setMessage("請連上網路以取得資料...")
     			.setOnKeyListener(new OnKeyListener(){

					@Override//防呆專用，限制使用者不能按其它的實體按鍵返回沒有更新的主畫面
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if(keyCode==KeyEvent.KEYCODE_SEARCH|keyCode==KeyEvent.KEYCODE_BACK){
							finish();
						}
						
						return true;
					}
     				
     			})
     			.setPositiveButton("離開程式", new DialogInterface.OnClickListener() {

     				@Override
     				public void onClick(DialogInterface dialog, int which) {
     					finish();
     				}
     				})
     			
     			.show();	
             }else{
            	 
         		if(checkServerMessage()==false){
         			showDialog(DOWNLOAD);
               	 new Thread(){
               		 
               		 public void run(){
               			 
               			 
               			 //只要一檢查到沒有2個檔案，馬上一次將2筆資料請求下來
                       	 BackStage.dataRequest(Receipt.this,"head");
                       	 BackStage.dataRequest(Receipt.this,"head2");
                       	 File head= new File(Receipt.this.getFilesDir()+"/receipt_head.txt");
    		            	 File head2= new File(Receipt.this.getFilesDir()+"/receipt_head2.txt");
    		            	 while(head.exists()!=true&head2.exists()!=true){
    		            		Log.i(tag, "wait for head & head2");
    		            	}
    		            	generateEntity();
    		            	
    		            	//請求主Thread將非中獎號打XX
    		            	Receipt.this.runOnUiThread(new Runnable(){
   							@Override
   							public void run() {
   								setBadButton();
   							}	
    		            	});
    		            	
    		            	dismissDialog(DOWNLOAD);
               		 }
               	 }.start();
        		}           	 
            	 

            	
             }
        }else{
        	Log.i(tag, "f.exist=true");
        	generateEntity();
        	setBadButton();

        }
        
        super.onResume();
	}
 
	@Override
	protected void onPause() {
		Log.i(tag, "Receipt.finish");
		finish();
		super.onPause();
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
 			getmoneyperoid=br.readLine();
 			Log.i(tag, "getnum: "+getnum);
 		} catch (IOException e) {
 			Log.i(tag, "IOException: "+e.getMessage());
 		}
 		
 		checknum=getnum.split(",");
 		
 		
 		handler.post(new Runnable(){

			@Override
			public void run() {
//				Log.i(tag, "back main Thread");
		 		textfirst.setText("中華民國#date份");//先設回初始值,以助日後使用者改到選擇月份時,能即時將UI的月份畫面更新
		 		 //month變數從txt檔裡讀出來以後，才可以將月份取代掉
		 		
				String month_buffer="<font color='green'><b>"+month+"</b></font>";
		        String finaltext1=textfirst.getText().toString().replace("#date", month_buffer);
				textfirst.setText(Html.fromHtml(finaltext1), TextView.BufferType.SPANNABLE);
			}
 			
 		});
 		
 		checkCutter();
    }
    
    /**
     * 將所有Button的XX都取消
     */
    public void setNormalButton(){

    	for(int i=0;i<buttonMap.size();i++){
    		
//    		buttonMap.get(String.valueOf(i)).setBackgroundResource(R.drawable.button_background);
    		buttonMap.get(String.valueOf(i)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    	}
    }
    
    /**
     * 這個函式在取得[使用者選用的邏輯]的1或3或8碼<br/>
     * 然後將沒中的號碼打上XX
     */
    public static void setBadButton(){
    		//首先，先將按鈕全部畫XX
    		for(int i=0;i<buttonMap.size();i++){
        		
//        		buttonMap.get(String.valueOf(i)).setBackgroundResource(R.drawable.button_no_background);
    			
    			buttonMap.get(String.valueOf(i)).setCompoundDrawablesWithIntrinsicBounds(null,drawable , null, null);
        	}
        	
        	
        	//因為三種運算邏輯，各別的第1碼位置不同(分別別第1、第6、和第8[尾]碼)
        	if(logic.equals("RightToLeft")){
//        		Log.i(tag, "into logic RightToLeft");
        		
    			for(String get8:checknum){
    				if(get8.length()==8){
//    					Log.i(tag, "BadButton8: "+get8.substring(7));
//    					buttonMap.get(get8.substring(7)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(get8.substring(7)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    				}else if(get8.length()==3){
//    					Log.i(tag, "BadButton3: "+get8.substring(2));
//    					buttonMap.get(get8.substring(2)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(get8.substring(2)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    				}		
    			}
    			
    		}else if(logic.equals("LeftToRight")){
    			
    			for(String get1:checknum){
    				if(get1.length()==8){
//    					Log.i(tag, "BadButton8: "+get1.substring(0,1));
//    					buttonMap.get(get1.substring(0,1)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(get1.substring(0,1)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    				}else if(get1.length()==3){
//    					Log.i(tag, "BadButton3: "+get1.substring(0,1));
//    					buttonMap.get(get1.substring(0,1)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(get1.substring(0,1)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    				}		
    			}
    			
    		}else if(logic.equals("LastThree")){
            	
    			for(String getL3:checknum){
    				if(getL3.length()==8){
//    					Log.i(tag, "BadButton8: "+getL3.substring(5,6));
//    					buttonMap.get(getL3.substring(5,6)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(getL3.substring(5,6)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);

    				}else if(getL3.length()==3){
//    					Log.i(tag, "BadButton3: "+getL3.substring(0,1));
//    					buttonMap.get(getL3.substring(0,1)).setBackgroundResource(R.drawable.button_background);
    					buttonMap.get(getL3.substring(0,1)).setCompoundDrawablesWithIntrinsicBounds(null,null , null, null);
    				}		
    			}
    		}
    	
    	
    }
    

    

    
    /**
     * 將第5行︰提示使用者從何位置開始輸入發票的字樣還原成預設字樣
     */
	public static void resetTextfive() {
		if(logic.equals("RightToLeft")){
			textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"最右邊\"");
            textfive.setText(finaltext5);
		}else if(logic.equals("LeftToRight")){
        	textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"最左邊\"");
            textfive.setText(finaltext5);
		}else if(logic.equals("LastThree")){
        	textfive.setText("▲ 請從發票 #message 開始輸入！");
        	String finaltext5=textfive.getText().toString().replace("#message", "\"末三碼\"");
            textfive.setText(finaltext5);
		}
	}
    
	/**
	 * 教育使用者新功能的訊息視窗
	 */
	private void teachDialog(){
    	String text="";
    	if(logic.equals("LastThree")){
    		text=" [第6位數] ";
    	}else if(logic.equals("RightToLeft")){
    		text=" [最末碼] ";
    	}else if(logic.equals("LeftToRight")){
    		text=" [第1位數] ";
    	}
    	ResponseDialog.newNotifyDialog(Receipt.this, softVersion+" 新增功能", "●懶人輸入法\n為了增加對獎速度，\n如果發票的"+text+"是XX\n發票就可以直接扔了！\n●為了2011-03月的特別開獎做更新", "warning");
	}
	
	/**
	 * Got是記錄是否有可能中獎的旗標
	 * @param value 使用者如果要設被有中獎,傳進來的值就會是true,否則為false
	 */
	public static void setterGot(boolean value){
		got=value==true?true:false;
		Log.i(tag, "setGot="+String.valueOf(got));
	}
	
	/**
	 * 請求獲知Got記錄是否是中獎的狀態
	 * @return 如果被設成可能中獎狀態，回傳true
	 */
	public static boolean getterGot(){
		return got;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "對獎月份");
		menu.add(0, 1, 1, "查看中獎號");
		menu.add(0, 2, 2, "更新");
		menu.add(0, 3, 3, "設定");
		menu.add(0, 4, 4, "關於");
		
		
		menu.getItem(0).setIcon(R.drawable.setmonth);
		menu.getItem(1).setIcon(R.drawable.targetnum);
		menu.getItem(2).setIcon(R.drawable.refresh);
		menu.getItem(3).setIcon(R.drawable.setting);
		menu.getItem(4).setIcon(R.drawable.about);
	
		return super.onCreateOptionsMenu(menu);
	}
	



	/**描述 : 建立Menu清單的觸發事件*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				String str_oldmonthsetted=sharedata.getString("iwantcheckmonth", "head");
				if(str_oldmonthsetted.equals("head")){
					oldmonthsetted=0;
				}else if(str_oldmonthsetted.equals("head2")){
					oldmonthsetted=1;
				}
				showDialog(SETMONTH);
				break;
			case 1:
//				Log.i("tag", "press show Dialog, cutter = "+cutter);
				 if(cutter == 2){//2011-03月的特別表格
					 Log.i("tag", "into cutter =2, show Dialog");
						 LayoutInflater factory = LayoutInflater.from(this);
						    //一般的Table Form是from，而2011-03-25特別版為form2
				            final View form = factory.inflate(R.layout.form2, null);
						    Builder AA=new AlertDialog.Builder(Receipt.this).setView(form);        
				               
				            TextView text_title=(TextView) form.findViewById(R.id.text_title);
				            
				            //2011-03-25新增超級特別獎
				            TextView text_super_spec=(TextView) form.findViewById(R.id.text_super_spec);
							TextView text_spec=(TextView) form.findViewById(R.id.text_spec);
							TextView text_head=(TextView) form.findViewById(R.id.text_head);
							TextView text_addnew=(TextView) form.findViewById(R.id.text_addnew);
							TextView text_period=(TextView) form.findViewById(R.id.text_period);
							
							//設定表格的開頭月份
							text_title.setText(text_title.getText().toString().replace("#date", month));


		//======================================================					
							//設定超級特獎文字的紅色字體
							String change_char_super_spe=new String();	
															
								String origin_super_spe=checknum[0];
								Log.i("tag", "checknum["+0+"]: "+origin_super_spe);

								if(change_char_super_spe.equals("")){
									change_char_super_spe=Character.toString(origin_super_spe.charAt(0))+Character.toString(origin_super_spe.charAt(1))+Character.toString(origin_super_spe.charAt(2))+Character.toString(origin_super_spe.charAt(3))+Character.toString(origin_super_spe.charAt(4))+"<font color='red'><b>"+origin_super_spe.charAt(5)+"</b></font>"+Character.toString(origin_super_spe.charAt(6))+Character.toString(origin_super_spe.charAt(7))+"<br>";
//									Log.i("tag", "change_char_spe: "+change_char_spe);
								}
						
							text_super_spec.setText(Html.fromHtml(change_char_super_spe), TextView.BufferType.SPANNABLE);
							
		//======================================================					
							//設定特獎文字的紅色字體
							String change_char_spe=new String();

//							for(int i=1;i<2;i++){
								
								String origin_spe=checknum[1];
								Log.i("tag", "checknum["+1+"]: "+origin_spe);

								if(change_char_spe.equals("")){
									change_char_spe=Character.toString(origin_spe.charAt(0))+Character.toString(origin_spe.charAt(1))+Character.toString(origin_spe.charAt(2))+Character.toString(origin_spe.charAt(3))+Character.toString(origin_spe.charAt(4))+"<font color='red'><b>"+origin_spe.charAt(5)+"</b></font>"+Character.toString(origin_spe.charAt(6))+Character.toString(origin_spe.charAt(7))+"<br>";
//									Log.i("tag", "change_char_spe: "+change_char_spe);
								}
							text_spec.setText(Html.fromHtml(change_char_spe), TextView.BufferType.SPANNABLE);

				//==========================================
							//設定頭獎文字的紅色字體
							String change_char_head="";
							for(int i=2;i<=4;i++){
								
								String origin_head=checknum[i];
								if(change_char_head.equals("")){
									change_char_head=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7))+"<br>";
								}else{
									if(i==4){
										change_char_head+=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7));
									}else{
										change_char_head+=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7))+"<br>";
									}
									
								}									
								
							}
							text_head.setText(Html.fromHtml(change_char_head), TextView.BufferType.SPANNABLE);						
							
							//=======================================
							//檢查有幾組特別號的紅色字體

							String change_char = "";
								for(int i=5;i<checknum.length;i++){
									Log.i("tag", "checknum.length "+checknum.length);
									
									String origin=checknum[i];
									
									if(i == checknum.length-1){
										change_char+="<font color='red'><b>"+origin.charAt(0)+"</b></font>"+origin.charAt(1)+origin.charAt(2);
										Log.i("tag", "i= "+i + change_char);
									}else{
										change_char+="<font color='red'><b>"+origin.charAt(0)+"</b></font>"+origin.charAt(1)+origin.charAt(2)+"<br>";
										Log.i("tag", "i= "+i + change_char);
									}
									
									text_addnew.setText(Html.fromHtml(change_char), TextView.BufferType.SPANNABLE);
								}
								
							
							
							text_period.setText(text_period.getText().toString().replace("#period", getmoneyperoid+"\n"));
							
							AA.show();
					}else if(cutter ==3){//3特3頭的普通表格
						 Log.i("tag", "into cutter =3, show Dialog");
						LayoutInflater factory = LayoutInflater.from(this);
					    //一般的Table Form是from，而2011-03-25特別版為form2
			            final View form = factory.inflate(R.layout.form, null);
					    Builder AA=new AlertDialog.Builder(Receipt.this).setView(form);        
			               
			            TextView text_title=(TextView) form.findViewById(R.id.text_title);
			            TextView text_spec=(TextView) form.findViewById(R.id.text_spec);
						TextView text_head=(TextView) form.findViewById(R.id.text_head);
						TextView text_addnew=(TextView) form.findViewById(R.id.text_addnew);
						TextView text_period=(TextView) form.findViewById(R.id.text_period);
						
						//設定表格的開頭月份
						text_title.setText(text_title.getText().toString().replace("#date", month));
						//======================================================					
						//設定特獎文字的紅色字體
						String change_char_spe=new String();

						for(int i=0;i<3;i++){
							
							String origin_spe=checknum[i];
							Log.i("tag", "checknum["+i+"]: "+origin_spe);

							if(change_char_spe.equals("")){
								change_char_spe=Character.toString(origin_spe.charAt(0))+Character.toString(origin_spe.charAt(1))+Character.toString(origin_spe.charAt(2))+Character.toString(origin_spe.charAt(3))+Character.toString(origin_spe.charAt(4))+"<font color='red'><b>"+origin_spe.charAt(5)+"</b></font>"+Character.toString(origin_spe.charAt(6))+Character.toString(origin_spe.charAt(7))+"<br>";
//								Log.i("tag", "change_char_spe: "+change_char_spe);
							}else{
								if(i==2){
									change_char_spe+=Character.toString(origin_spe.charAt(0))+Character.toString(origin_spe.charAt(1))+Character.toString(origin_spe.charAt(2))+Character.toString(origin_spe.charAt(3))+Character.toString(origin_spe.charAt(4))+"<font color='red'><b>"+origin_spe.charAt(5)+"</b></font>"+Character.toString(origin_spe.charAt(6))+Character.toString(origin_spe.charAt(7));
								}else{
									change_char_spe+=Character.toString(origin_spe.charAt(0))+Character.toString(origin_spe.charAt(1))+Character.toString(origin_spe.charAt(2))+Character.toString(origin_spe.charAt(3))+Character.toString(origin_spe.charAt(4))+"<font color='red'><b>"+origin_spe.charAt(5)+"</b></font>"+Character.toString(origin_spe.charAt(6))+Character.toString(origin_spe.charAt(7))+"<br>";
								}
								
							}

//							Log.i("tag", "origin_spe: "+origin_spe);
						}
//						Log.i("tag", "change_char_spe: "+change_char_spe);
					
						text_spec.setText(Html.fromHtml(change_char_spe), TextView.BufferType.SPANNABLE);
				
			//==========================================
						//設定頭獎文字的紅色字體
						String change_char_head="";
						for(int i=3;i<=5;i++){
							
							String origin_head=checknum[i];
							if(change_char_head.equals("")){
								change_char_head=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7))+"<br>";
							}else{
								if(i==5){
									change_char_head+=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7));
								}else{
									change_char_head+=Character.toString(origin_head.charAt(0))+Character.toString(origin_head.charAt(1))+Character.toString(origin_head.charAt(2))+Character.toString(origin_head.charAt(3))+Character.toString(origin_head.charAt(4))+"<font color='red'><b>"+origin_head.charAt(5)+"</b></font>"+Character.toString(origin_head.charAt(6))+Character.toString(origin_head.charAt(7))+"<br>";
								}
								
							}
							
							
											
							
						}
						text_head.setText(Html.fromHtml(change_char_head), TextView.BufferType.SPANNABLE);						
						
						//=======================================
						//檢查有幾組特別號的紅色字體

						try{
							String change_char="";
							for(int i=6;i<checknum.length;i++){

								Log.i("tag", "last is: "+i);
								String origin=checknum[i];
								
								if(i == checknum.length-1){
									change_char+="<font color='red'><b>"+origin.charAt(0)+"</b></font>"+origin.charAt(1)+origin.charAt(2);
								}else{
									change_char+="<font color='red'><b>"+origin.charAt(0)+"</b></font>"+origin.charAt(1)+origin.charAt(2)+"<br>";
								}
								text_addnew.setText(Html.fromHtml(change_char), TextView.BufferType.SPANNABLE);
							}
							
						}catch(ArrayIndexOutOfBoundsException e){			

								text_addnew.setText(text_addnew.getText().toString().replace("#addnew", "無\n"));

						}
						
						text_period.setText(text_period.getText().toString().replace("#period", getmoneyperoid+"\n"));
						
						AA.show();
					}
				   
					
				break;
			case 2://先將原檔名更名成tem,下載完後,再將tem刪除
//				Log.i(tag, "into showDialog");
				if(checkServerMessage()==false){
					showDialog(UPDATENUM);
					final Handler handler=new Handler();
					
					new Thread(){
						public void run(){
							File head= new File(Receipt.this.getFilesDir()+"/receipt_head.txt");
							 if(head.exists()==true){
								 File temhead=new File(Receipt.this.getFilesDir()+"/temreceipt_head.txt");
								 head.renameTo(temhead);	 
							 }
							 File head2= new File(Receipt.this.getFilesDir()+"/receipt_head2.txt");
							 if(head2.exists()==true){
								 File temhead2=new File(Receipt.this.getFilesDir()+"/temreceipt_head2.txt");
								 head2.renameTo(temhead2);	
							 }
							 
							 if (BackStage.check3GConnectStatus(Receipt.this)==false&BackStage.checkEnableingWifiStatus(Receipt.this)==false){		            	
								 handler.post(new Runnable(){

									@Override
									public void run() {
										dismissDialog(UPDATENUM);
										new AlertDialog.Builder(Receipt.this)
						     	    	.setTitle("沒有中獎號碼資料!")
						     			.setIcon(R.drawable.warning)
						     			.setMessage("請連上網路以取得資料...")
						     			.setOnKeyListener(new OnKeyListener(){

											@Override//防呆專用，限制使用者不能按其它的實體按鍵返回沒有更新的主畫面
											public boolean onKey(DialogInterface dialog, int keyCode,
													KeyEvent event) {
												if(keyCode==KeyEvent.KEYCODE_SEARCH|keyCode==KeyEvent.KEYCODE_BACK){
													finish();
												}
												
												return true;
											}
						     				
						     			})
						     			.setPositiveButton("取消", new DialogInterface.OnClickListener() {

						     				@Override
						     				public void onClick(DialogInterface dialog, int which) {
						     				
						     					File temhead=new File(Receipt.this.getFilesDir()+"/temreceipt_head.txt");
						    					File temhead2=new File(Receipt.this.getFilesDir()+"/temreceipt_head2.txt");
						    					if(temhead.exists()==true){
						    						temhead.renameTo(new File(Receipt.this.getFilesDir()+"/receipt_head.txt"));
						    					}
						    					if(temhead2.exists()==true){
						    						temhead2.renameTo(new File(Receipt.this.getFilesDir()+"/receipt_head2.txt"));
						    					}
						     				}
						     				})
						     			
						     			.show();	
										
									}
									 
								 });
				             	
				             }else{
				            	 //跟伺服器要求更新
				            	 BackStage.dataRequest(Receipt.this,"head");
				            	 BackStage.dataRequest(Receipt.this,"head2"); 
				            	 
				             }
							 
							 
							 
							 if(head.exists()==true){							 
								 File temhead=new File(Receipt.this.getFilesDir()+"/temreceipt_head.txt");
								 temhead.delete();
							 }
							 if(head2.exists()==true){
								 File temhead2=new File(Receipt.this.getFilesDir()+"/temreceipt_head2.txt");
								 temhead2.delete();
								 
							 }
							 if(head.exists()==true){
							 handler.post(new Runnable(){

								@Override
								public void run() {
										Log.i(tag, "update success!");
										Toast.makeText(Receipt.this, "更新成功！", Toast.LENGTH_SHORT).show();
										if(progressdialog!=null){
											//AnMarket回報錯誤這段，加上if判斷式維護
											dismissDialog(UPDATENUM);
										}
										
										onResume();				
								}							 
							 });
							 }						 
						}
					}.start();
				}
							 
				break;
			case 3:
				Intent intent=new Intent();
				int oldvolume=am.getStreamVolume(AudioManager.STREAM_MUSIC);
				
				Bundle bundle=new Bundle();
				bundle.putInt("oldvolume", oldvolume);
				bundle.putString("voice_version", voice_version);
				intent.putExtras(bundle);
				intent.setClass(this, Setting.class);
				startActivity(intent);
				break;
			case 4:
				new AlertDialog.Builder(this)
				.setMessage(getString(R.string.app_name)+" "+ softVersion +"\n作者 FunTrigger\n\n版權 2011")
				.setIcon(R.drawable.icon)
				.setTitle("關於")
				.setPositiveButton("問題回報", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lp43simon@gmail.com"}); 
						sendIntent.putExtra(Intent.EXTRA_TEXT, "請將意見填寫於此");
						sendIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name)+softVersion+" 意見回報");
						sendIntent.setType("message/rfc822");
						startActivity(Intent.createChooser(sendIntent, "Title:"));
					}
				})
				.setNeutralButton("查看功能", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						teachDialog();
					}
				})
				
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				
				.show();
				break;
	
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected Dialog onCreateDialog(int id) {
		final Editor sharedata = getSharedPreferences("data", 0).edit(); 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case SETMONTH:


			builder
			.setTitle("請選擇你想對獎的發票時間")
			.setSingleChoiceItems(new String[]{Receipt.this.sharedata.getString("head", "當期"),Receipt.this.sharedata.getString("head2", "上一期")/*"1","2"*/},oldmonthsetted,new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					switch(which){
						case 0:
							sharedata.putString("iwantcheckmonth","head");
							sharedata.commit();
							dismissDialog(SETMONTH);
							onResume();
							break;
						case 1:		
							sharedata.putString("iwantcheckmonth","head2");
							sharedata.commit();
							dismissDialog(SETMONTH);
							onResume();
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
			
		case UPDATENUM:
			progressdialog=new ProgressDialog(this) ;
			progressdialog.setTitle("請稍候");
			progressdialog.setMessage("資料更新中...");
			progressdialog.setButton(DialogInterface.BUTTON_POSITIVE,"取消",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(UPDATENUM);
					File temhead=new File(Receipt.this.getFilesDir()+"/temreceipt_head.txt");
					File temhead2=new File(Receipt.this.getFilesDir()+"/temreceipt_head2.txt");
					if(temhead.exists()==true){
						temhead.renameTo(new File(Receipt.this.getFilesDir()+"/receipt_head.txt"));
					}
					if(temhead2.exists()==true){
						temhead2.renameTo(new File(Receipt.this.getFilesDir()+"/receipt_head2.txt"));
					}
					
				}
				
			});
			
			return progressdialog;
		case DOWNLOAD:
			progressdialog=new ProgressDialog(this) ;
			progressdialog.setTitle("請稍候");
			progressdialog.setMessage("資料下載中...");
			progressdialog.setButton(DialogInterface.BUTTON_POSITIVE,"離開",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
			 		finish();				
				}
				
			});
			
			return progressdialog;
		}
		return super.onCreateDialog(id);
	}
	
	/**
	 * 呼叫AdMob
	 */
	private void callAdView(){
//		Log.i("tag", "callAdView");
	    // Create the adView
	    adView = new AdView(this, AdSize.BANNER, "a14cd12a9d5c65a");
	    // Lookup your LinearLayout assuming it’s been given
	    // the attribute android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)findViewById(R.id.adView);

	    // Add the adView to it
	    layout.addView(adView);
	    
	    // Initiate a generic request to load it with an ad
	    AdRequest ar = new AdRequest();
//	//    ar.setTesting(true);
	    adView.loadAd(ar);
//	    Log.i("tag", "isAdReady? "+adView.isReady());
//	    Log.i("tag", "isAdRefresh? "+adView.isRefreshing());
	    adView.setAdListener(new AdListener(){

			@Override
			public void onDismissScreen(Ad arg0) {
				Log.i("tag", "onDismissScreen, "+arg0.toString());
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				Log.i("tag", "onFailedToReceiveAd, "+arg1.toString());
				
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				Log.i("tag", "onLeaveApplication, "+arg0.toString());
				
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				Log.i("tag", "onPresentScreen, "+arg0.toString());
				
			}

			@Override
			public void onReceiveAd(Ad arg0) {
				Log.i("tag", "onReceiveAd, "+arg0.toString());
				
			}
	    	
	    });
	}
	
	/**
	 * 算看看指定月份裡，長度為8的中獎號碼有幾筆，如果是6筆，假定為正常對獎模式
	 * 倘若只有5筆，判斷為2011-03月特別版
	 */
	private void checkCutter(){
    	int count = 0;
    	for(int i=0;i<checknum.length;i++){
    		Log.i("tag","checknum["+i+"].length(): "+checknum[i].length());
    		if(checknum[i].length()==8)count++;
    	}
    	
		if(count>5){
			cutter = 3;
		}else{
			cutter = 2;
		}
	}
	
	/**
	 * 檢查是否有從Camangi傳出去的訊息
	 * 這個視窗主要目的是因為近期一直不斷亂改對獎邏輯
	 * 為了安撫使用者，我做出一個可以即時從我這方告訴使用者
	 * 我有沒有更新的一個訊息，可以告訴使用者暫停使用之類的
	 */
	private boolean checkServerMessage(){
		Log.i("tag", "checkServerMessage");
		boolean return_value = false;
		HttpURLConnection uc = null;
		URL url = null;
		String buffera,contentBuffer="";
		 
		
		 
		 if (BackStage.check3GConnectStatus(Receipt.this)==false&BackStage.checkEnableingWifiStatus(Receipt.this)==false){		            	
			 handler.post(new Runnable(){

				@Override
				public void run() {
					dismissDialog(UPDATENUM);
					new AlertDialog.Builder(Receipt.this)
	     	    	.setTitle("注意")
	     			.setIcon(R.drawable.warning)
	     			.setMessage("請先連上網路...")
	     			.setOnKeyListener(new OnKeyListener(){

						@Override//防呆專用，限制使用者不能按其它的實體按鍵返回沒有更新的主畫面
						public boolean onKey(DialogInterface dialog, int keyCode,
								KeyEvent event) {
							if(keyCode==KeyEvent.KEYCODE_SEARCH|keyCode==KeyEvent.KEYCODE_BACK){
								finish();
							}
							
							return true;
						}
	     				
	     			})
	     			.show();
				}
			 });
		 }else{
			 try {
					url = new URL("http://frank.camangi.com/APK/invoice/maintain.html");
					uc = (HttpURLConnection)url.openConnection();
					while((uc.getResponseCode()!=HttpURLConnection.HTTP_OK)){
						uc.disconnect();
						uc  = (HttpURLConnection)url.openConnection();	
						}
					
					InputStream is = uc.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					   do{
						   buffera = br.readLine();
		  					   if(buffera!=null){	   
		  						 contentBuffer+=new String(buffera.getBytes());   
		  					   } 					   
					   } while(buffera !=null);
					   
				} catch (MalformedURLException e1) {
					Log.i("tag", "MalformedURLException: "+e1.getMessage());
				} catch (IOException e) {
					Log.i("tag", "IOException: "+e.getMessage());
				}
				
				
				Log.i("tag", "contentBuffer "+contentBuffer);
				
				if(!contentBuffer.equals("")){
					return_value =true;
					new AlertDialog.Builder(this)
					.setTitle("即時訊息")
					.setMessage(contentBuffer)
					.setPositiveButton("確認", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
							
						}
						
					})
					.setOnKeyListener(new OnKeyListener(){

						@Override//防呆專用，限制使用者不能按其它的實體按鍵返回沒有更新的主畫面
						public boolean onKey(DialogInterface dialog, int keyCode,
								KeyEvent event) {
							if(keyCode==KeyEvent.KEYCODE_SEARCH|keyCode==KeyEvent.KEYCODE_BACK){
								finish();
							}
							
							return true;
						}
					})
					.show();
				}		
			 
			}
					
		
		return return_value;

	}
	
	
}