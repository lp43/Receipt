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

public class Receipt extends Activity {
	private String softVersion="v1.010b1";
    Button button0,button1,button2,button3,button4,button5,
    button6,button7,button8,button9,button_clear;
    TextView textview,textfive;
	private String tag="tag";
	/**
	 * 使用者可以設定對獎所要輸入的號碼數︰2碼或3碼
	 */
	private int limit=1;
	/**
	 * 裡面置放7組檢查碼
	 */
	String[] checknum;
	private boolean got;
	/**
	 * 記錄目前8碼的末3碼是特獎還是頭獎的型態變數
	 */
	private static int checkEightType;
	/**
	 * 若程式判斷末3碼是和特獎末3碼一樣，則判斷變數為該變數
	 */
	private static final int eightInSpecial=1;
	/**
	 * 若程式判斷末3碼是和頭獎末3碼一樣，則判斷變數為該變數
	 */
	private static final int eightInHead=2;
	private MediaPlayer mediaPlayer01;
	/*
	 * 播放音效的版本變數
	 */
	private String voice_version="regular";
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
	static int logic;  
	  
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
        
        mediaPlayer01=new MediaPlayer();
        
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
       
        SharedPreferences sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
        	voice_version=voicedata;
        }
        
        
        textview=(TextView) findViewById(R.id.text);
        textfive=(TextView) findViewById(R.id.title_fiveline);
        String finaltext5=textfive.getText().toString().replace("#message", "\"最末碼\"");
        textfive.setText(finaltext5);
        
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
				type("0");	
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("zero",voice_version);				
			} 	
        });
        button1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("1");
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("one",voice_version);	
			} 	
        });
        button2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("2");
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("two",voice_version);	
			} 	
        });
        button3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("3");
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("three",voice_version);	
			} 	
        });
        button4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("4");		
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("four",voice_version);	
			} 	
        });
        button5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("5");		
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("five",voice_version);	
			} 	
        });
        button6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("6");	
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("six",voice_version);	
			} 	
        });
        button7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("7");	
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("seven",voice_version);	
			} 	
        });
        button8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("8");	
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("eight",voice_version);	
			} 	
        });
        button9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("9");	
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("nine",voice_version);	
			} 	
        });
        button_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				textview.setText("");
				//因為大奶妹和正規女音的數字是共用的，所以要將大奶妹的數字鍵導到regular
				String voice_version="";
				if(Receipt.this.voice_version.equals("big")){
					voice_version="regular";
				}else{
					voice_version=Receipt.this.voice_version;
				}
				createMedia("clear",voice_version);	
			} 	
        });
        f= new File(this.getFilesDir()+"/receipt_now.txt");
        
        if(!f.exists()){
        	Log.i(tag, "into f.exist==false");
        	Log.i(tag, "BackStage.check3GConnectStatus(this)= "+String.valueOf(BackStage.check3GConnectStatus(this)));
        	Log.i(tag, "BackStage.checkWIFIStatus(this)= "+String.valueOf(BackStage.checkEnableingWifiStatus(this)));
        	 if (BackStage.check3GConnectStatus(this)==false&BackStage.checkEnableingWifiStatus(this)==false){
            	 
            	
//        		 Log.i(tag, "into new");
             	new AlertDialog.Builder(this)
     	    	.setTitle("沒有中獎號碼的資料!")
     			.setIcon(R.drawable.warning)
     			.setMessage("當第1次使用時\n系統必須連上網路\n才能下載中獎號碼...")
     			.setPositiveButton("離開程式", new DialogInterface.OnClickListener() {

     				@Override
     				public void onClick(DialogInterface dialog, int which) {
     					finish();
     				}
     				})
     			
     			.show();	
             }else{
            	 BackStage.dataRequest(this,"now"); 
            	 generateEntity();
             }
        }else{
        	Log.i(tag, "f.exist=true");
        	generateEntity();
        }
       
    }


	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedata = getSharedPreferences("data", 0);  
        String voicedata = sharedata.getString("voice", "regular");  
        Log.i(tag,"data="+voicedata);
        if(voicedata.equals("mute")){
        	am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

        }else{
        	voice_version=voicedata;
        }
	}


	/**
     * 描述 : 頂頭描述框的計算公式<br/>
     * @param num 當要使用這個函式時，會將要在文字框印出的數字傳進來，再做判斷，然後顯示
     */
    private void type(String num){
    	Log.i(tag, "get length: "+textview.getText().length());
//    	Log.i(tag, "num: "+num);
    	if(textview.getText().length()==0){
    		got=false;
    		textview.setText(num);
    		checkLastR2L(textview.getText().toString());	
    	}else if(textview.getText().length()>0&textview.getText().length()<limit-1){
    		got=false;
    		textview.setText(num+textview.getText());	
    	}else if(textview.getText().length()==limit-1){
    		got=false;
    		textview.setText(num+textview.getText());
    		if(textview.getText().length()==3){
    			Log.i(tag, "into length 3");
    			checkThreeR2L(textview.getText().toString());
    		}else if(textview.getText().length()==8){
    			Log.i(tag, "into length 8");
    			checkEightR2L(textview.getText().toString());
    		}
    		
    	}

    }
    
    /**
     * 描述 : 當使用者一輸入最尾碼時，進入該計算公式
     * @param num 使用者所輸入的單一號碼
     */
    private void checkLastR2L(String num){
    	Log.i(tag, "get num is: "+ num);
    	 for(String numcheck:checknum){//for迴圈會將file目錄裡的文字檔讀進來，並一一放進陣列裡,再用這個for迴圈,
    		 //一次讀出一筆的特性，去計算出是否有中獎
    		 if(numcheck.length()==8){
    			 //如果for迴圈讀出來的數值是8碼，代表是特獎或頭獎的原始資料
//    			 Log.i(tag, "at8: "+numcheck.substring(7));
    			 if(num.equals(numcheck.substring(7))){//陣列的碼數是從0開始算，所以最尾碼是7
    				 limit=3;
    				 got=true;
//        			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
    				
        			 createMedia("again");
        			 return;
        		 }
    		 }else if(numcheck.length()==3){ //如果for迴圈出來的長度是3，代表是增開獎的獎項
    		 	if(num.equals(numcheck.substring(2))){//陣列的碼數是從0開始算，所以最尾碼是2
    			 limit=3;
				 got=true;
    			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
    			 createMedia("again");
    			 return;
    		 }else{
    			 //如果最尾碼根本就沒有對到任何數字，就清除文字框
    			 limit=1;
    			 got=false;
    			 textview.setText("");
//    			 Toast.makeText(this, "沒中...", Toast.LENGTH_SHORT).show();
    			 createMedia("no");
    			 return;
    		 	  }	 
    		 }
		   }
    }
    
    /**
     * 描述 : 當使用者輸的數字是3筆時，呼叫這個函式做運算
     * @param num 使用者所輸入的3組號碼
     */
    private void checkThreeR2L(String num){
    	Log.i(tag, "get 3num is: "+ num);
    	int i=0;
    	 for(String numcheck:checknum){
    		i++;
    		 if(numcheck.length()==8){//從file目錄抓出來的txt檔，轉為for迴圈後，會有6組8個號碼和1組3個號碼,當長度為8,也就是頭獎和特獎時
    			 
//    			 Log.i(tag, "numlength 8:"+numcheck);
    			 if(num.equals(numcheck.substring(5,8))){//當傳進來的3碼和特、頭獎的第5~8碼數值一樣時
    				 limit=8;
    				 got=true;
        			 
    				  Toast toast = Toast.makeText(this, "再來！再來！", Toast.LENGTH_SHORT);
	   			      View originView=toast.getView();
	   			      LinearLayout layout= new LinearLayout(this);
	   			      layout.setOrientation(LinearLayout.VERTICAL);
	   			      ImageView view = new ImageView(this);
	   			      view.setImageResource(R.drawable.again);
	   			      layout.addView(view);
	   			      layout.addView(originView);
	   			      toast.setView(layout);
	   			      toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	   			      toast.show();
   			      
        			 createMedia("again");
        			 if(i<4){
        				 checkEightType=eightInSpecial;
        			 }else if(i>=4){
        				 checkEightType=eightInHead;
        			 }
        			 return;
        		 }
    		 }else if(numcheck.length()==3){ //當for迴圈跑到最後一筆3碼時
//    			 Log.i(tag, "numlength 3:"+numcheck);   			 
    		 	if(num.equals(numcheck.substring(0,3))){//當傳進來的碼和for迴圈的尾碼相吻合時
	    			limit=1;
				    got=true;
				    createMedia("notsimple");
				    new AlertDialog.Builder(this)
				    	.setTitle("恭喜你")
						.setIcon(R.drawable.congratulations)
						.setMessage("恭喜你中了[增開六獎]\n獎金: 200塊")
						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								textview.setText("");
							}
							})
						
						.show();	
	    			return;
    		    }else{
    			 limit=1;
    			 got=false;
    			 textview.setText("");
    			
    			      Toast toast = Toast.makeText(this, "沒中...", Toast.LENGTH_SHORT);
    			      View originView=toast.getView();
    			      LinearLayout layout= new LinearLayout(this);
    			      layout.setOrientation(LinearLayout.VERTICAL);
    			      ImageView view = new ImageView(this);
    			      view.setImageResource(R.drawable.no);
    			      layout.addView(view);
    			      layout.addView(originView);
    			      toast.setView(layout);
    			      toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    			      toast.show();

    			     
    			 createMedia("nono");
    			 return;
    		 	}	 
    		 }
		   }
   }
 
    /**
     * 描述 : 當使用者輸入完3碼後，續輸完8碼時，會呼叫這個運算函式
     * @param num
     */
    private void checkEightR2L(String num){
    	Log.i(tag, "get 8num is: "+ num);
    	Log.i(tag, "getEightType is: "+checkEightType);
    	int i=0;


    	
   	 for(String numcheck:checknum){
   		i++;//i幫我判別是特獎、頭獎還是增開獎
//   		Log.i(tag, "i: "+i);
   		
   	
		 
//		 Log.i(tag, "numlength 8:"+numcheck);
		 limit=1;
		 if(checkEightType==eightInSpecial){
			 Log.i(tag, "now check to: "+numcheck);
			 if(num.equals(numcheck)){//如果傳來的8組數字和迴圈的6個一樣
				 Log.i(tag, "into special equal 0-8");
				 got=true;
				 createMedia("million2");
					 new AlertDialog.Builder(this)
				    	.setTitle("恭喜你")
						.setIcon(R.drawable.million2)
						.setMessage("恭喜你中了[特獎]\n獎金: 200萬!")
						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								textview.setText("");
							}
							})
						
						.show();	
		 
					 return;
			 }else if(i==4){
				//如果傳進來的8位數沒有全部一樣，代表沒中
				 Log.i(tag, "into special no equal");
				 Log.i(tag, "now check to: "+numcheck);
				 createMedia("noany");
    			 new AlertDialog.Builder(this)
			    	.setTitle("真可惜")				    	
					.setIcon(R.drawable.noany)
					.setMessage("真可惜\n沒中...")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
							limit=1;
	        			 	got=false;
						}
						})
					
					.show();	
    			
					 return;
			 }
		 }else if(checkEightType==eightInHead){
			 if(num.equals(numcheck)){//如果傳來的8組數字和迴圈的6個一樣
				 Log.i(tag, "into Head equal 0-8");
				 got=true;
				 createMedia("th200");
					 new AlertDialog.Builder(this)
					 	.setTitle("恭喜你")
						.setIcon(R.drawable.th200)
						.setMessage("恭喜你中了[頭獎]\n獎金: 20萬!")
						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								textview.setText("");
							}
							})
						
						.show();	
		 
					 return;
			 } else if(num.substring(1,8).equals(numcheck.substring(1,8))){
				 //如果傳進來的末7碼是頭獎的7碼一樣，代表中六獎
				 Log.i(tag, "into Head equal 1-8");
				 createMedia("onlycon");
    			 new AlertDialog.Builder(this)
    			 	.setTitle("恭喜你")
					.setIcon(R.drawable.congratulations)
					.setMessage("恭喜你中了[二獎]\n獎金: 4萬元!")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
						}
						})
					
					.show();	
					 return;
    		 } 
			 else if(num.substring(2,8).equals(numcheck.substring(2,8))){
				 //如果傳進來的末6碼是頭獎的6碼一樣，代表中六獎
				 Log.i(tag, "into Head equal 2-8");
				 createMedia("onlycon");
    			 new AlertDialog.Builder(this)
    			 	.setTitle("恭喜你")
					.setIcon(R.drawable.congratulations)
					.setMessage("恭喜你中了[三獎]\n獎金: 1萬元!")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
						}
						})
					
					.show();	
					 return;
    		 } 
			 else if(num.substring(3,8).equals(numcheck.substring(3,8))){
				 //如果傳進來的末5碼是頭獎的5碼一樣，代表中六獎
				 Log.i(tag, "into Head equal 3-8");
				 createMedia("onlycon");
    			 new AlertDialog.Builder(this)
    			 	.setTitle("恭喜你")
					.setIcon(R.drawable.congratulations)
					.setMessage("恭喜你中了[四獎]\n獎金: 4千塊")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
						}
						})
					
					.show();	
					 return;
    		 } 
			 else if(num.substring(4,8).equals(numcheck.substring(4,8))){
				 //如果傳進來的末4碼是頭獎的4碼一樣，代表中六獎
				 Log.i(tag, "into Head equal 4-8");
				 createMedia("onlycon");
    			 new AlertDialog.Builder(this)
    			 	.setTitle("恭喜你")
					.setIcon(R.drawable.congratulations)
					.setMessage("恭喜你中了[五獎]\n獎金: 1千塊")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
						}
						})
					
					.show();	
					 return;
    		 } 
			 else if(num.substring(5,8).equals(numcheck.substring(5,8))){
				 //如果傳進來的末3碼是頭獎的3碼一樣，代表中六獎
				 Log.i(tag, "into Head equal 5-8");
				 createMedia("hundred2");				 
    			 new AlertDialog.Builder(this)
    			 	.setTitle("恭喜你")
					.setIcon(R.drawable.hundred2)
					.setMessage("恭喜你中了[六獎]\n獎金: 200塊")
					.setPositiveButton("確認", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							textview.setText("");
						}
						})
					
					.show();	
					 return;
    		 }
	   	
		 }

   		
   		
   		
	   }//for結束區塊
   }
    private void createMedia(String userPressed,String voice_version){
    	
    	
		
    	try {
//    		Log.i(tag, "get choice is: "+userPressed);
    		
    		mediaPlayer01=MediaPlayer.create(Receipt.this, Receipt.this.getResources().getIdentifier(userPressed+"_"+voice_version, "raw", this.getPackageName()));
        		

//			 mediaPlayer01.prepare();	
    		mediaPlayer01.start();
			} catch (IllegalStateException e) {
				Log.i(tag, "IllegalStateException: "+e.getMessage());
				e.printStackTrace();
			} /*catch (IOException e) {
				Log.i(tag, "IOException: "+e.getMessage());
				e.printStackTrace();
			}*/
//			mediaPlayer01.stop();
			
			
			mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
//					Log.i(tag, "into onCompletion()");
					mp.release();
					
				}
			});
			mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
//					Log.i(tag, "into onError()");
					mp.release();
					return false;
				}
			});
    }
 
    
    private void createMedia(String userPressed){
	
    	try {
    		Log.i(tag, "get choice is: "+userPressed);

    			mediaPlayer01=MediaPlayer.create(Receipt.this, Receipt.this.getResources().getIdentifier(userPressed+"_"+voice_version, "raw", this.getPackageName()));
        		mediaPlayer01.start();

    		
			} catch (IllegalStateException e) {
				Log.i(tag, "IllegalStateException: "+e.getMessage());
				e.printStackTrace();
			} /*catch (IOException e) {
				Log.i(tag, "IOException: "+e.getMessage());
				e.printStackTrace();
			}*/
//			mediaPlayer01.stop();
			
			
			mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
//					Log.i(tag, "into onCompletion()");
					mp.release();
					
				}
			});
			mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
//					Log.i(tag, "into onError()");
					mp.release();
					return false;
				}
			});
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
    	Log.i(tag, "into generateEntity()");
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
 			getnum=br.readLine();
 			Log.i(tag, "getnum: "+getnum);
 		} catch (IOException e) {
 			Log.i(tag, "IOException: "+e.getMessage());
 		}
 		checknum=getnum.split(",");
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