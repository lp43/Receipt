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
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.view.LayoutInflater;
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
	private String softVersion="v1.014";
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
	/**
	 * 從SharePreference讀出預設的的logic運算模式,存放在此變數
	 */
	private String logic;

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
	 * 記錄對獎方式的邏輯變數<br/>
	 * 0:由右至左
	 * 1:由左至右
	 * 2:先輸入末3碼，再輸入剩餘8碼
	 */

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
	 * 預設為0
	 */
	private final int SETMONTH=0;
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
       
        sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
        	voice_version=voicedata;
        }
//        String iwantcheckmonth = sharedata.getString("iwantcheckmonth", "head"); 
        
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
				got=false;//將LastThree專屬的中末3碼的got變數回復為false
				
				if(logic.equals("RightToLeft")){
					textfive.setText("▲ 請從發票 \"最右邊\" 開始輸入！");
				}else if(logic.equals("LeftToRight")){
					textfive.setText("▲ 請從發票 \"最左邊\" 開始輸入！");
				}else if(logic.equals("LastThree")){
					textfive.setText("▲ 請從發票 \"末三碼\" 開始輸入！");
				}
				
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
        
//        f= new File(this.getFilesDir()+"/receipt_"+iwantcheckmonth+".txt");
//        
//        if(!f.exists()){
//        	Log.i(tag, "into f.exist==false");
//        	Log.i(tag, "BackStage.check3GConnectStatus(this)= "+String.valueOf(BackStage.check3GConnectStatus(this)));
//        	Log.i(tag, "BackStage.checkWIFIStatus(this)= "+String.valueOf(BackStage.checkEnableingWifiStatus(this)));
//        	 if (BackStage.check3GConnectStatus(this)==false&BackStage.checkEnableingWifiStatus(this)==false){
//            	 
//            	
////        		 Log.i(tag, "into new");
//             	new AlertDialog.Builder(this)
//     	    	.setTitle("沒有\"新的\"中獎號碼資料!")
//     			.setIcon(R.drawable.warning)
//     			.setMessage("系統必須連上網路取得資料...\n一有資料，\n之後就暫時不用再連線了")
//     			.setPositiveButton("離開程式", new DialogInterface.OnClickListener() {
//
//     				@Override
//     				public void onClick(DialogInterface dialog, int which) {
//     					finish();
//     				}
//     				})
//     			
//     			.show();	
//             }else{
//            	 BackStage.dataRequest(this,iwantcheckmonth); 
//            	 generateEntity();
//             }
//        }else{
//        	Log.i(tag, "f.exist=true");
//        	generateEntity();
//        }
       
    }


	@Override
	protected void onResume() {
//		Log.i(tag, "into onResume()");
		
		
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
        textview.setText("");//數字框清空
        media= new Media();
        
        String iwantcheckmonth = sharedata.getString("iwantcheckmonth", "head");
        
        f= new File(this.getFilesDir()+"/receipt_"+iwantcheckmonth+".txt");
        
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
            	 //只要一檢查到沒有2個檔案，馬上一次將2筆資料請求下來
            	 BackStage.dataRequest(this,"head");
            	 BackStage.dataRequest(this,"head2"); 
            	 //但是只產生被設定的月份的實體
            	 generateEntity();
             }
        }else{
        	Log.i(tag, "f.exist=true");
        	generateEntity();
        }
        
        super.onResume();
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
 		
 		
 		
 		textfirst.setText("中華民國#date份");//先設回初始值,以助日後使用者改到選擇月份時,能即時將UI的月份畫面更新
 		 //month變數從txt檔裡讀出來以後，才可以將月份取代掉
        String finaltext1=textfirst.getText().toString().replace("#date", month);
        textfirst.setText(finaltext1);
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
				
				    LayoutInflater factory = LayoutInflater.from(this);
		            final View form = factory.inflate(R.layout.form, null);
				    Builder AA=new AlertDialog.Builder(Receipt.this).setView(form);        
		               
		            TextView text_title=(TextView) form.findViewById(R.id.text_title);
					TextView text_spec=(TextView) form.findViewById(R.id.text_spec);
					TextView text_head=(TextView) form.findViewById(R.id.text_head);
					TextView text_addnew=(TextView) form.findViewById(R.id.text_addnew);
					TextView text_period=(TextView) form.findViewById(R.id.text_period);
					
					text_title.setText(text_title.getText().toString().replace("#date", month));				
					text_spec.setText(text_spec.getText().toString().replace("test1\ntest2\ntest3", checknum[0]+"\n"+checknum[1]+"\n"+checknum[2]));
					text_head.setText(text_head.getText().toString().replace("test1\ntest2\ntest3", checknum[3]+"\n"+checknum[4]+"\n"+checknum[5]));
					try{
						text_addnew.setText(text_addnew.getText().toString().replace("#addnew", checknum[6]+"\n"));
					}catch(ArrayIndexOutOfBoundsException e){		
						text_addnew.setText(text_addnew.getText().toString().replace("#addnew", "無\n"));
					}
					
					text_period.setText(text_period.getText().toString().replace("#period", getmoneyperoid+"\n"));
					
					AA.show();
					
				break;
			case 2://先刪除原檔案，再呼叫onResume()重新抓取資料
				 File head= new File(this.getFilesDir()+"/receipt_head.txt");
				 if(head.exists()==true){
					 head.delete();	
				 }
				 File head2= new File(this.getFilesDir()+"/receipt_head2.txt");
				 if(head2.exists()==true){
					 head2.delete();	
				 }
				 onResume();//onResume()會檢查有沒有中獎txt檔案,如果沒有,會呼叫dataRequest(),並建立起實體generateEntity()
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
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		final Editor sharedata = getSharedPreferences("data", 0).edit(); 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case SETMONTH:


			builder
			.setTitle("請選擇你想對獎的發票時間")
			.setSingleChoiceItems(new String[]{"當期","上一期"},oldmonthsetted,new DialogInterface.OnClickListener(){

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
		}
		return super.onCreateDialog(id);
	}
}