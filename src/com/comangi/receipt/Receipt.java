package com.comangi.receipt;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Receipt extends Activity {
	private String softversion="v1.004";
    Button button0,button1,button2,button3,button4,button5,
    button6,button7,button8,button9,button_clear;
    TextView textview ;
	private String tag="tag";
	/**
	 * 使用者可以設定對獎所要輸入的號碼數︰2碼或3碼
	 */
	private int limit=1;
	String[] checknum;
	private boolean got;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard);
        textview=(TextView) findViewById(R.id.text);
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
					
			} 	
        });
        button1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("1");		
			} 	
        });
        button2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("2");	
			} 	
        });
        button3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("3");		
			} 	
        });
        button4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("4");		
			} 	
        });
        button5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("5");		
			} 	
        });
        button6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("6");		
			} 	
        });
        button7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("7");		
			} 	
        });
        button8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("8");		
			} 	
        });
        button9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type("9");		
			} 	
        });
        button_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				textview.setText("");		
			} 	
        });
        File f= new File(this.getFilesDir()+"/receipt_now.txt");
        
        if(!f.exists()){
        	Log.i(tag, "f.exist=false");
        	BackStage.dataRequest(this,"now");
        }else{
        	Log.i(tag, "f.exist=true");
        }
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
//			Log.i(tag, "getnum: "+getnum);
		} catch (IOException e) {
			Log.i(tag, "IOException: "+e.getMessage());
		}
		checknum=getnum.split(",");
		 
      /*  new AlertDialog.Builder(Receipt.this)
		
		.setTitle("何取得對獎號的方式？")
		.setIcon(R.drawable.question)
		.setItems(new String[]{"自動上網搜尋","手動輸入"}, new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
		
					break;
							
				case 1:		
					Intent intent = new Intent();
					intent.setClass(Receipt.this, InsertNum.class);
					startActivity(intent);					
					break;

				}	
			};
		})
		.show();*/
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
    		checkLast(textview.getText().toString());	
    	}else if(textview.getText().length()>0&textview.getText().length()<limit-1){
    		got=false;
    		textview.setText(num+textview.getText());	
    	}else if(textview.getText().length()==limit-1){
    		got=false;
    		textview.setText(num+textview.getText());
    		if(textview.getText().length()==3){
    			Log.i(tag, "into length 3");
    			checkThree(textview.getText().toString());
    		}else if(textview.getText().length()==8){
    			Log.i(tag, "into length 8");
    			checkEight(textview.getText().toString());
    		}
    		
    	}

    }
    
    /**
     * 描述 : 當使用者一輸入最尾碼時，進入該計算公式
     * @param num 使用者所輸入的單一號碼
     */
    private void checkLast(String num){
    	Log.i(tag, "get num is: "+ num);
    	 for(String numcheck:checknum){//for迴圈會將file目錄裡的文字檔讀進來，並一一放進陣列裡,再用這個for迴圈,
    		 //一次讀出一筆的特性，去計算出是否有中獎
    		 if(numcheck.length()==8){
    			 //如果for迴圈讀出來的數值是8碼，代表是特獎或頭獎的原始資料
//    			 Log.i(tag, "at8: "+numcheck.substring(7));
    			 if(num.equals(numcheck.substring(7))){//陣列的碼數是從0開始算，所以最尾碼是7
    				 limit=3;
    				 got=true;
        			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
        			 return;
        		 }
    		 }else if(numcheck.length()==3){ //如果for迴圈出來的長度是3，代表是增開獎的獎項
    		 	if(num.equals(numcheck.substring(2))){//陣列的碼數是從0開始算，所以最尾碼是2
    			 limit=3;
				 got=true;
    			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
    			 return;
    		 }else{
    			 //如果最尾碼根本就沒有對到任何數字，就清除文字框
    			 limit=1;
    			 got=false;
    			 textview.setText("");
    			 Toast.makeText(this, "沒中...", Toast.LENGTH_SHORT).show(); 
    			 return;
    		 	  }	 
    		 }
		   }
    }
    
    /**
     * 描述 : 當使用者輸的數字是3筆時，呼叫這個函式做運算
     * @param num 使用者所輸入的3組號碼
     */
    private void checkThree(String num){
    	Log.i(tag, "get 3num is: "+ num);
    	 for(String numcheck:checknum){
    		
    		 if(numcheck.length()==8){//從file目錄抓出來的txt檔，轉為for迴圈後，會有6組8個號碼和1組3個號碼,當長度為8,也就是頭獎和特獎時
//    			 Log.i(tag, "numlength 8:"+numcheck);
    			 if(num.equals(numcheck.substring(5,8))){//當傳進來的3碼和特、頭獎的第5~8碼數值一樣時
    				 limit=8;
    				 got=true;
        			 Toast.makeText(this, "再來,再來!", Toast.LENGTH_SHORT).show();
        			 return;
        		 }
    		 }else if(numcheck.length()==3){ //當for迴圈跑到最後一筆3碼時
//    			 Log.i(tag, "numlength 3:"+numcheck);   			 
    		 	if(num.equals(numcheck.substring(0,3))){//當傳進來的碼和for迴圈的尾碼相吻合時
	    			limit=1;
				    got=true;
				    new AlertDialog.Builder(this)
						.setTitle("恭喜你中了增開六獎")
						.setIcon(R.drawable.warning)
						.setMessage("獎金: 200塊")
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
    			 Toast.makeText(this, "沒中...", Toast.LENGTH_SHORT).show(); 
    			 return;
    		 	}	 
    		 }
		   }
   }
 
    /**
     * 描述 : 當使用者輸入完3碼後，續輸完8碼時，會呼叫這個運算函式
     * @param num
     */
    private void checkEight(String num){
    	Log.i(tag, "get 8num is: "+ num);
    	int i=0;
    	 for(String numcheck:checknum){
    		i++;//i幫我判別是特獎、頭獎還是增開獎
//    		Log.i(tag, "i: "+i);
    		 if(numcheck.length()==8){
    			 
//    			 Log.i(tag, "numlength 8:"+numcheck);
    			 limit=1;
    			 if(num.equals(numcheck)){//如果傳來的8組數字和迴圈的6個一樣
    				 Log.i(tag, "into equal 0-8");
    				 got=true;
    				 if(i<4){//如果是在for迴圈的前3筆,代表中了最大獎
    					 new AlertDialog.Builder(this)
 						.setTitle("恭喜你中了特獎")
 						.setIcon(R.drawable.warning)
 						.setMessage("獎金: 200萬!")
 						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

 							@Override
 							public void onClick(DialogInterface dialog, int which) {
 								textview.setText("");
 							}
 							})
 						
 						.show();	
    					 return;
    				 }//如果是前3組，就是特獎的結束區塊
    				 else if(i>=4){//如果是for迴圈的後四組代表中了20萬
    					 new AlertDialog.Builder(this)
  						.setTitle("恭喜你中了頭獎")
  						.setIcon(R.drawable.warning)
  						.setMessage("獎金: 20萬!")
  						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

  							@Override
  							public void onClick(DialogInterface dialog, int which) {
  								textview.setText("");
  							}
  							})
  						.show(); 
    					 return;
    				 }//否則就是頭獎的結束區塊
    				 else if(num.substring(1,8).equals(numcheck.substring(1,8))&i>=4){
        				 //如果傳進來的末7碼是頭獎的7碼一樣，代表中六獎
            			 Log.i(tag, "into not equal 0-8");
            			 new AlertDialog.Builder(this)
    						.setTitle("恭喜你中了二獎")
    						.setIcon(R.drawable.warning)
    						.setMessage("獎金: 4萬元!")
    						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								textview.setText("");
    							}
    							})
    						
    						.show();	
     					 return;
            		 } 
    				 else if(num.substring(2,8).equals(numcheck.substring(2,8))&i>=4){
        				 //如果傳進來的末6碼是頭獎的6碼一樣，代表中六獎
            			 Log.i(tag, "into not equal 0-8");
            			 new AlertDialog.Builder(this)
    						.setTitle("恭喜你中了三獎")
    						.setIcon(R.drawable.warning)
    						.setMessage("獎金: 1萬元!")
    						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								textview.setText("");
    							}
    							})
    						
    						.show();	
     					 return;
            		 } 
    				 else if(num.substring(3,8).equals(numcheck.substring(3,8))&i>=4){
        				 //如果傳進來的末5碼是頭獎的5碼一樣，代表中六獎
            			 Log.i(tag, "into not equal 0-8");
            			 new AlertDialog.Builder(this)
    						.setTitle("恭喜你中了四獎")
    						.setIcon(R.drawable.warning)
    						.setMessage("獎金: 4,000塊")
    						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								textview.setText("");
    							}
    							})
    						
    						.show();	
     					 return;
            		 } 
    				 else if(num.substring(4,8).equals(numcheck.substring(4,8))&i>=4){
        				 //如果傳進來的末4碼是頭獎的4碼一樣，代表中六獎
            			 Log.i(tag, "into not equal 0-8");
            			 new AlertDialog.Builder(this)
    						.setTitle("恭喜你中了五獎")
    						.setIcon(R.drawable.warning)
    						.setMessage("獎金: 1,000塊")
    						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								textview.setText("");
    							}
    							})
    						
    						.show();	
     					 return;
            		 } 
    				 else if(num.substring(5,8).equals(numcheck.substring(5,8))&i>=4){
        				 //如果傳進來的末3碼是頭獎的3碼一樣，代表中六獎
            			 Log.i(tag, "into not equal 0-8");
            			 new AlertDialog.Builder(this)
    						.setTitle("恭喜你中了六獎")
    						.setIcon(R.drawable.warning)
    						.setMessage("獎金: 200塊")
    						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								textview.setText("");
    							}
    							})
    						
    						.show();	
     					 return;
            		 }
    				 else if(num.substring(5,8).equals(numcheck.substring(5,8))&i<4){
    	    				//如果傳進來的8組的末3碼是特獎的末3碼，沒中
    	        			 Log.i(tag, "into not equal 0-8");
    	        			 new AlertDialog.Builder(this)
    							.setTitle("真可惜")
    							.setIcon(R.drawable.warning)
    							.setMessage("沒中...")
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
    				 else{
    				     //如果不是特獎也不是頭獎，就是普通獎
    					 new AlertDialog.Builder(this)
  						.setTitle("恭喜你中了六獎")
  						.setIcon(R.drawable.warning)
  						.setMessage("獎金: 200塊")
  						.setPositiveButton("確認", new DialogInterface.OnClickListener() {

  							@Override
  							public void onClick(DialogInterface dialog, int which) {
  								textview.setText("");
  							}
  							})
  						
  						.show();	
    					 return;
    				 }
        			 
        			 
        		 }//如果進來的數字和8碼相符的結束區塊
    			
    			
    			
    			
    			 
    			
    			 
    		 }//跳出所有數字了的區塊
		   }//for結束區塊
   }
}