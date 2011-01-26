package com.funtrigger.receipt.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.funtrigger.receipt.R;
import com.funtrigger.receipt.Receipt;
import com.funtrigger.receipt.Type;
import com.funtrigger.tools.Media;
import com.funtrigger.tools.ResponseDialog;

public class RightToLeft{
	private static String tag="tag";
	/**
	 * 記錄目前8碼的末3碼是特獎還是頭獎的型態變數
	 */
	private static int checkEightType;
	/**
	 * 若程式判斷末3碼是和特獎末3碼一樣，則判斷變數為該變數
	 */
	private static final int EIGHTINSPECIAL=1;
	/**
	 * 若程式判斷末3碼是和頭獎末3碼一樣，則判斷變數為該變數
	 */
	private static final int EIGHTINHEAD=2;
	static Media media=new Media();
	
//	/**
//     * 描述 : 當使用者一輸入最尾碼時，進入該計算公式
//     * @param num 使用者所輸入的單一號碼
//     */
//    public static void checkLast(String num,Context context,String voice_version){
//    	Log.i(tag, "get num is: "+ num);
//    	 for(String numcheck:Receipt.checknum){//for迴圈會將file目錄裡的文字檔讀進來，並一一放進陣列裡,再用這個for迴圈,
//    		 //一次讀出一筆的特性，去計算出是否有中獎
//    		 if(numcheck.length()==8){
//    			 //如果for迴圈讀出來的數值是8碼，代表是特獎或頭獎的原始資料
////    			 Log.i(tag, "at8: "+numcheck.substring(7));
//    			 if(num.equals(numcheck.substring(7))){//陣列的碼數是從0開始算，所以最尾碼是7
//    				 Receipt.limit=3;
//    				 
//    				 ResponseDialog.newToast(context, "有機會,再來!", R.drawable.havechance);
//    				 media.createMedia("again",context,voice_version);
//        			 return;
//        		 }
//    		 }else if(numcheck.length()==3){ //如果for迴圈出來的長度是3，代表是增開獎的獎項
//    		 	if(num.equals(numcheck.substring(2))){//陣列的碼數是從0開始算，所以最尾碼是2
//    		 		Receipt.limit=3;
//		
////    			 Toast.makeText(context, "請再輸入剩餘的5碼!", Toast.LENGTH_SHORT).show();
//    			 
//    			 Receipt.textfive.setText("請再輸入剩餘的5碼！");
//    			 ResponseDialog.newToast(context, "請再輸入剩餘的5碼！", R.drawable.again);
//    			 media.createMedia("again",context,voice_version);
//    			 return;
//    		 }else{
//    			 //如果最尾碼根本就沒有對到任何數字，就清除文字框
//    			 Receipt.limit=1;
//    	
//    			 Receipt.textview.setText("");
//
//    			 ResponseDialog.newSingleToast(context, "沒中...");
////    			 ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
//    			 media.createMedia("no",context,voice_version);
//    			 return;
//    		 	  }	 
//    		 }
//		   }
//    	 Receipt.limit=1;
//     	
//		 Receipt.textview.setText("");
//
//		 ResponseDialog.newSingleToast(context, "沒中...");
////		 ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
//		 media.createMedia("no",context,voice_version);
//		 return;
//    }
    
    /**
    * 描述 : 當使用者輸的數字是3筆時，呼叫這個函式做運算
    * @param num 使用者所輸入的3組號碼
    */
    public static void checkThree(String num,Context context,String voice_version){
         Log.i(tag, "get 3num is: "+ num);
         int i=0;
         for(String numcheck:Receipt.checknum){
         i++;
         if(numcheck.length()==8){//從file目錄抓出來的txt檔，轉為for迴圈後，會有6組8個號碼和1組3個號碼,當長度為8,也就是頭獎和特獎時
        
    // Log.i(tag, "numlength 8:"+numcheck);
         if(num.equals(numcheck.substring(5,8))){//當傳進來的3碼和特、頭獎的第5~8碼數值一樣時
//        	 Receipt.limit=8; 
            
            Receipt.textfive.setText("請再輸入剩餘的5碼！");
            
//            Toast toast = Toast.makeText(context, "請再輸入剩餘的5碼！", Toast.LENGTH_SHORT);
//		    View originView=toast.getView();
//		    LinearLayout layout= new LinearLayout(context);
//		    layout.setOrientation(LinearLayout.VERTICAL);
//		    ImageView view = new ImageView(context);
//		    view.setImageResource(R.drawable.again);
//		    layout.addView(view);
//		    layout.addView(originView);
//		    toast.setView(layout);
//		    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//		    toast.show();
		    
		    ResponseDialog.newGoodToast(context, "請再輸入剩餘的5碼！", R.drawable.again);   
		    media.createMedia("again",context,voice_version);
		    
             if(i<4){
             checkEightType=EIGHTINSPECIAL;
             }else if(i>=4){
             checkEightType=EIGHTINHEAD;
             }
             return;
             }
         }else if(numcheck.length()==3){ //當for迴圈跑到最後一筆3碼時
		    // Log.i(tag, "numlength 3:"+numcheck);
		         if(num.equals(numcheck.substring(0,3))){//當傳進來的碼和for迴圈的尾碼相吻合時
//		        	 Receipt.limit=1;
//		        newDialog("恭喜你中了[增開六獎]\n獎金: 200塊","congratulations",context);
		        ResponseDialog.newGoodDialog(context, "恭喜你中了[增開六獎]\n獎金: 200塊", "congratulations");
		        media.createMedia("notsimple",context,voice_version);
		        Type.resetNumTotal();
			    return;
			         }/*else{
//			        	 Receipt.limit=1;
			     
//			         Receipt.textview.setText("");
			         Type.resetNumTotal();
//			         Toast toast = Toast.makeText(context, "沒中...", Toast.LENGTH_SHORT);
//			         View originView=toast.getView();
//			         LinearLayout layout= new LinearLayout(context);
//			         layout.setOrientation(LinearLayout.VERTICAL);
//			         ImageView view = new ImageView(context);
//			         view.setImageResource(R.drawable.no);
//			         layout.addView(view);
//			         layout.addView(originView);
//			         toast.setView(layout);
//			         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//			         toast.show();
			
			         ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
			         media.createMedia("nono",context,voice_version);
			         return;
			         }*/
			         }
			    }
//    	 Receipt.limit=1;
	     
//         Receipt.textview.setText("");
         Type.resetNumTotal();
//         Toast toast = Toast.makeText(context, "沒中...", Toast.LENGTH_SHORT);
//         View originView=toast.getView();
//         LinearLayout layout= new LinearLayout(context);
//         layout.setOrientation(LinearLayout.VERTICAL);
//         ImageView view = new ImageView(context);
//         view.setImageResource(R.drawable.no);
//         layout.addView(view);
//         layout.addView(originView);
//         toast.setView(layout);
//         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//         toast.show();

         ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
         media.createMedia("nono",context,voice_version);
         return;
	       }  
    
    /**
    * 描述 : 當使用者輸入完3碼後，續輸完8碼時，會呼叫這個運算函式
    * @param num 傳進來欲對獎的參數
    */
        public static void checkEight(String num,Context context,String voice_version){
	         Log.i(tag, "get 8num is: "+ num);
	         Log.i(tag, "getEightType is: "+checkEightType);
	         int i=0;	
	        
	        for(String numcheck:Receipt.checknum){
	        i++;//i幫我判別是特獎、頭獎還是增開獎
			    // Log.i(tag, "i: "+i);
		
			    // Log.i(tag, "numlength 8:"+numcheck);
//	        Receipt.limit=1;
			    if(checkEightType==EIGHTINSPECIAL){
			    Log.i(tag, "now check to: "+numcheck);
			    if(num.equals(numcheck)){//如果傳來的8組數字和迴圈的6個一樣
			    Log.i(tag, "into special equal 0-8");
			    
//			    newDialog("恭喜你中了[特獎]\n獎金: 200萬!","million2",context);
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[特獎]\n獎金: 200萬!", "million2");
			    media.createMedia("million2",context,voice_version);

			    
			
			    return;
			    }else if(i==4){
			    //如果傳進來的8位數沒有全部一樣，代表沒中
			    Log.i(tag, "into special no equal");
			    Log.i(tag, "now check to: "+numcheck);
			    
			    ResponseDialog.newBadDialog(context, "真可惜\n沒中...", "noany");
			    media.createMedia("noany",context,voice_version);
//			    Receipt.limit=1;
			    
//			         new AlertDialog.Builder(context)
//			    .setTitle("真可惜")
//			    .setIcon(R.drawable.noany)
//			    .setMessage("真可惜\n沒中...")
//			    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//			
//			    @Override
//			    public void onClick(DialogInterface dialog, int which) {
//			    	Receipt.textview.setText("");
//			    	Receipt.limit=1;
//			    	Receipt.textfive.setText("▲ 請從發票 \"最右邊\" 開始輸入！");	
//			    }
//			    })
//			
//			    .show();
			        
			    return;
			    }
			    }else if(checkEightType==EIGHTINHEAD){
			    if(num.equals(numcheck)){//如果傳來的8組數字和迴圈的6個一樣
			    Log.i(tag, "into Head equal 0-8");
			    
//			    newDialog("恭喜你中了[頭獎]\n獎金: 20萬!","th200",context);
			    
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[頭獎]\n獎金: 20萬!", "th200");
			    media.createMedia("th200",context,voice_version);
			    
//			    Receipt.limit=1;
			
			    return;
			    } else if(num.substring(1,8).equals(numcheck.substring(1,8))){
			    //如果傳進來的末7碼是頭獎的7碼一樣，代表中六獎
			    Log.i(tag, "into Head equal 1-8");
			    
//			    newDialog("恭喜你中了[二獎]\n獎金: 4萬元!","congratulations",context);
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[二獎]\n獎金: 4萬元!", "congratulations");
			    media.createMedia("onlycon",context,voice_version);
			     
			         
			    return;
			         }
			    else if(num.substring(2,8).equals(numcheck.substring(2,8))){
			    //如果傳進來的末6碼是頭獎的6碼一樣，代表中六獎
			    Log.i(tag, "into Head equal 2-8");
			    
//			    newDialog("恭喜你中了[三獎]\n獎金: 1萬元!","congratulations",context);
			    
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[三獎]\n獎金: 1萬元!", "congratulations");
			    media.createMedia("onlycon",context,voice_version);
			    
			        
			    return;
			         }
			    else if(num.substring(3,8).equals(numcheck.substring(3,8))){
			    //如果傳進來的末5碼是頭獎的5碼一樣，代表中六獎
			    Log.i(tag, "into Head equal 3-8");
			    
//			    newDialog("恭喜你中了[四獎]\n獎金: 4千塊","congratulations",context);
			    
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[四獎]\n獎金: 4千塊!", "congratulations"); 
			    media.createMedia("onlycon",context,voice_version);
			    
			         
			    return;
			         }
			    else if(num.substring(4,8).equals(numcheck.substring(4,8))){
			    //如果傳進來的末4碼是頭獎的4碼一樣，代表中六獎
			    Log.i(tag, "into Head equal 4-8");
			    
//			    newDialog("恭喜你中了[五獎]\n獎金: 1千塊","congratulations",context);
			    
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[五獎]\n獎金: 1千塊!", "congratulations");
			    media.createMedia("onlycon",context,voice_version);
			    
			        
			    return;
			         }
			    else if(num.substring(5,8).equals(numcheck.substring(5,8))){
			    //如果傳進來的末3碼是頭獎的3碼一樣，代表中六獎
			    Log.i(tag, "into Head equal 5-8");
			    
//			    newDialog("恭喜你中了[六獎]\n獎金: 200塊","hundred2",context);
			    
			    ResponseDialog.newGoodDialog(context, "恭喜你中了[六獎]\n獎金: 200塊", "hundred2");
			    media.createMedia("hundred2",context,voice_version);
			    
			        
			    return;
			         }
			
			    }
      
			    }//for結束區塊
			 }
        
//        private static void newDialog(String message,String icon,Context context){
//	         new AlertDialog.Builder(context)
//	         .setTitle("恭喜你")
//			    .setIcon(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()))
//			    .setMessage(message)
//			    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//			
//			    @Override
//			    public void onClick(DialogInterface dialog, int which) {
//			    Receipt.textview.setText("");
////			    Receipt.textfive.setText("▲ 請從發票 \"最右邊\" 開始輸入！");
//			    }
//			    })
//	
//	    .show();
//        }

}
