package com.camangi.receipt.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.camangi.receipt.R;
import com.camangi.receipt.Receipt;
import com.camangi.receipt.media.Media;

/**
 * 末3碼的判斷邏輯是︰
 * 1.使用者先輸入末3碼
 * 2.程式判斷末3碼,若一樣才繼續判斷前5碼
 * @author simon
 *
 */
public class LastThree {
	private static String tag="tag";
	static String maybeNum;
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

    
    /**
    * 描述 : 當使用者輸入完3碼後，續輸完8碼時，會呼叫這個運算函式
    * @param num
    */
        public static void checkThree(String getlast3num,Context context,String voice_version){
	         Log.i(tag, "get last 3num is: "+ getlast3num);
	     
	         int i=0;
	        
	        for(String numcheck:Receipt.checknum){
	        	i++;//i幫我判別是特獎、頭獎還是增開獎			  
			    Log.i(tag, "now check to: "+numcheck);
//			    Log.i(tag, "Receipt.checknum.length: "+Receipt.checknum.length);
			    
//			    Log.i(tag, "numcheck lengh: "+numcheck.length());
			    if(numcheck.length()==8){
			    	if(i<4){
			    		
			    		if(numcheck.subSequence(5, 8).equals(getlast3num)){
//			    			Log.i(tag, "last3 is the same with SPECIAL");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkEightType=EIGHTINSPECIAL;
			    			Log.i(tag, "maybeNum is: "+maybeNum);
			    			Receipt.textfive.setText("請繼續\"由左至右\"輸入前面5碼！");
			    			
			    			Toast toast = Toast.makeText(context, "請繼續\"由左至右\"輸入前面5碼！", Toast.LENGTH_SHORT);
			    		    View originView=toast.getView();
			    		    LinearLayout layout= new LinearLayout(context);
			    		    layout.setOrientation(LinearLayout.VERTICAL);
			    		    ImageView view = new ImageView(context);
			    		    view.setImageResource(R.drawable.again);
			    		    layout.addView(view);
			    		    layout.addView(originView);
			    		    toast.setView(layout);
			    		    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			    		    toast.show();
			    		    
			    		    media.createMedia("again",context,voice_version);
			    		    Receipt.got=true;
			    			return;
			    		}
			    	}else if(i>3){
			    		if(numcheck.subSequence(5, 8).equals(getlast3num)){
//			    			Log.i(tag, "last3 is the same with HEAD");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkEightType=EIGHTINHEAD;
			    			Log.i(tag, "maybeNum is: "+maybeNum);
			    			Receipt.textfive.setText("請繼續\"由左至右\"輸入前面5碼！");
			    			
			    			Toast toast = Toast.makeText(context, "請繼續\"由左至右\"輸入前面5碼！", Toast.LENGTH_SHORT);
			    		    View originView=toast.getView();
			    		    LinearLayout layout= new LinearLayout(context);
			    		    layout.setOrientation(LinearLayout.VERTICAL);
			    		    ImageView view = new ImageView(context);
			    		    view.setImageResource(R.drawable.again);
			    		    layout.addView(view);
			    		    layout.addView(originView);
			    		    toast.setView(layout);
			    		    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			    		    toast.show();
			    		    
			    		    media.createMedia("again",context,voice_version);
			    		    Receipt.got=true;
			    			return;
			    		}
				    }
			    }else if(numcheck.length()==3){
			    	if(numcheck.equals(getlast3num)){
//		    			Log.i(tag, "last3 is the same with NEWADD");
		    			newDialog("恭喜你中了[增開六獎]\n獎金: 200塊","congratulations",context);
		    			media.createMedia("notsimple",context,voice_version);
		    			return;
		    		}else{
				    	 Toast toast = Toast.makeText(context, "沒中...", Toast.LENGTH_SHORT);
				         View originView=toast.getView();
				         LinearLayout layout= new LinearLayout(context);
				         layout.setOrientation(LinearLayout.VERTICAL);
				         ImageView view = new ImageView(context);
				         view.setImageResource(R.drawable.no);
				         layout.addView(view);
				         layout.addView(originView);
				         toast.setView(layout);
				         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				         toast.show();
				         media.createMedia("noany",context,voice_version);
				         return;
				    }

			    }	   
	        }
	        Toast toast = Toast.makeText(context, "沒中...", Toast.LENGTH_SHORT);
	         View originView=toast.getView();
	         LinearLayout layout= new LinearLayout(context);
	         layout.setOrientation(LinearLayout.VERTICAL);
	         ImageView view = new ImageView(context);
	         view.setImageResource(R.drawable.no);
	         layout.addView(view);
	         layout.addView(originView);
	         toast.setView(layout);
	         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	         toast.show();
	         media.createMedia("noany",context,voice_version);
	         return;
        }
        
        private static void newDialog(String message,String icon,Context context){
	         new AlertDialog.Builder(context)
	         .setTitle("恭喜你")
			    .setIcon(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()))
			    .setMessage(message)
			    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
			
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    Receipt.textview.setText("");
			    Receipt.textfive.setText("▲ 請從發票 \"末三碼\" 開始輸入！");
			    }
			    })
	
	    .show();
        }
        
        public static void checkRemain5(String getnum,Context context,String voice_version){
        	if(checkEightType==EIGHTINSPECIAL){
        		checkRemain5InSPEC(getnum,context,voice_version);
        	}else if(checkEightType==EIGHTINHEAD){
        		checkRemain5InHEAD(getnum,context,voice_version);
        	}
        }
        
        /**
         * 條件:末3碼和特獎核對成功
         * 才會進來該Method檢查剩下沒檢查的5碼
         */
        private static void checkRemain5InSPEC(String getfirst5num,Context context,String voice_version){
//        	String getNumHeadFive=getnum.substring(0, 5);
//        	Log.i(tag, "getnum head5: "+getNumHeadFive);
//        	String maybeNumHeadFive=maybeNum.substring(0, 5);
//        	Log.i(tag, "maybeNum head5: "+maybeNumHeadFive);
        	Log.i(tag, "into checkRemain5InSPEC()");
        	Log.i(tag, "getfirst5num: "+getfirst5num);
        	
        	if(getfirst5num.equals(maybeNum.substring(0, 5))){    
			    newDialog("恭喜你中了[特獎]\n獎金: 200萬!","million2",context);
			    media.createMedia("million2",context,voice_version);
        	}else{
        		 Toast toast = Toast.makeText(context, "沒中...", Toast.LENGTH_SHORT);
		         View originView=toast.getView();
		         LinearLayout layout= new LinearLayout(context);
		         layout.setOrientation(LinearLayout.VERTICAL);
		         ImageView view = new ImageView(context);
		         view.setImageResource(R.drawable.no);
		         layout.addView(view);
		         layout.addView(originView);
		         toast.setView(layout);
		         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		         toast.show();
		         media.createMedia("noany",context,voice_version);
        	}
        	
        	maybeNum="";
        }
        
        
        /**
         * 條件:末3碼和頭獎核對成功
         * 才會進來該Method檢查剩下沒檢查的5碼
         */
        private static void checkRemain5InHEAD(String getfirst5num,Context context,String voice_version){
//        	Log.i(tag, "maybeNum: "+maybeNum);
        	Log.i(tag, "into checkRemain5InHEAD()");
        	Log.i(tag, "getfirst5num: "+getfirst5num);
        	
        	if(getfirst5num.equals(maybeNum.substring(0,5))){
			    
			    newDialog("恭喜你中了[頭獎]\n獎金: 20萬!","th200",context);
			    media.createMedia("th200",context,voice_version);
        	}else if(getfirst5num.substring(1, 5).equals(maybeNum.substring(1, 5))){
			    
			    newDialog("恭喜你中了[二獎]\n獎金: 4萬元!","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(2, 5).equals(maybeNum.substring(2, 5))){
			    
			    newDialog("恭喜你中了[三獎]\n獎金: 1萬元!","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(3, 5).equals(maybeNum.substring(3, 5))){
			    
			    newDialog("恭喜你中了[四獎]\n獎金: 4千塊","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(4, 5).equals(maybeNum.substring(4, 5))){
			    
			    newDialog("恭喜你中了[五獎]\n獎金: 1千塊","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else {
			    
			    newDialog("恭喜你中了[六獎]\n獎金: 200塊","hundred2",context);
			    media.createMedia("hundred2",context,voice_version);
        	}
        	
        	maybeNum="";
        }
   
}
