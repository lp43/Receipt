package com.comangi.receipt;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Receipt extends Activity {
	private String softversion="v1.003";
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
    	
//    	Log.i(tag, "num: "+num);
    	if(textview.getText().length()>0&textview.getText().length()<limit){	
			textview.setText(num+textview.getText());
		}else if(textview.getText().length()==0){
			textview.setText(num);	
			got=false;
		}else if(textview.getText().length()==limit){
			got=false;
			if(limit==1){
				checkLast(textview.getText().toString());
			}else if(limit==3){
				checkThree(textview.getText().toString());
			}
			
			
			if(got==false){
				textview.setText(num);//清空,直接輸入新的	
			}
			
			
		}
    }
    
    private void checkLast(String num){
    	 for(String numcheck:checknum){
    		 if(numcheck.length()==8){
//    			 Log.i(tag, "at8: "+numcheck.substring(7));
    			 if(num.equals(numcheck.substring(7))){
    				 limit=3;
    				 got=true;
        			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
        		 }
    		 }else{//長度為3是特別獎
    			 if(num.equals(numcheck.substring(2))){
    				 limit=3;
    				 got=true;
        			 Toast.makeText(this, "有機會,再來!", Toast.LENGTH_SHORT).show();
        		 } 
    			 
    		 }
    
		   }
    }
    
    private void checkThree(String num){
    	Log.i(tag, "get num is: "+num);
   	 for(String numcheck:checknum){
   		 if(numcheck.length()==8){
   			 Log.i(tag, "at5-7: "+numcheck.substring(5,7));
   			 if(num.equals(numcheck.substring(5,7))){
   				 limit=8;
   				 got=true;
       			 Toast.makeText(this, "有200塊了,再來!", Toast.LENGTH_SHORT).show();
       		 }
   		 }else{//長度為3是特別獎
   			 if(num.equals(numcheck.substring(0,2))){
   				Log.i(tag, "at5-7: "+numcheck.substring(0,2));
   				got=true;
       			 Toast.makeText(this, "有200塊了,恭喜!", Toast.LENGTH_SHORT).show();
       		 } 
   			 
   		 }
   
		   }
   }
    
}