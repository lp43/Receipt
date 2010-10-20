package com.comangi.receipt;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Receipt extends Activity {
	private String softversion="v1.001";
    Button button0,button1,button2,button3,button4,button5,
    button6,button7,button8,button9,button_clear;
    TextView textview ;
	private String tag="tag";
	private int limit=2;
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
        
        BackStage.dataRequest("now");
        
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
    	Log.i(tag, "num: "+num);
    	if(textview.getText().length()<limit){	
			textview.setText(textview.getText()+num);
		}else{
			textview.setText(num);	
		}
    }
}