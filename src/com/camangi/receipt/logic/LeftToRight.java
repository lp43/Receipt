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
 * 由左至右的判斷邏輯是︰
 * 1.使用者先輸入全8碼
 * 2.程式先判斷末3碼,若一樣才繼續判斷
 * @author simon
 *
 */
public class LeftToRight {
	private static String tag="tag";
	static String maybeNum;
	static Media media=new Media();

    
    /**
    * 描述 : 當使用者輸入完3碼後，續輸完8碼時，會呼叫這個運算函式
    * @param num
    */
        public static void checkEight(String getnum,Context context,String voice_version){
        	
	         Log.i(tag, "get 8num is: "+ getnum);
	         
	         int i=0;
	         String lastThree=getnum.substring(5, 8);
	         Log.i(tag, "lastThree: "+lastThree);
	        
	        for(String numcheck:Receipt.checknum){
	        	i++;//i幫我判別是特獎、頭獎還是增開獎			  
			    Log.i(tag, "now check to: "+numcheck);
//			    Log.i(tag, "Receipt.checknum.length: "+Receipt.checknum.length);
			    
//			    Log.i(tag, "numcheck lengh: "+numcheck.length());
			    if(numcheck.length()==8){
			    	if(i<4){
			    		
			    		if(numcheck.subSequence(5, 8).equals(lastThree)){
//			    			Log.i(tag, "last3 is the same with SPECIAL");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkRemain5InSPEC(getnum,context,voice_version);
			    			return;
			    		}
			    	}else if(i>3){
			    		if(numcheck.subSequence(5, 8).equals(lastThree)){
//			    			Log.i(tag, "last3 is the same with HEAD");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkRemain5InHEAD(getnum,context,voice_version);
			    			return;
			    		}
				    }
			    }else if(numcheck.length()==3){
			    	if(numcheck.equals(lastThree)){
		    			Log.i(tag, "last3 is the same with NEWADD");
		    			newDialog("恭喜你中了[增開六獎]\n獎金: 200塊","congratulations",context);
		    			media.createMedia("notsimple",context,voice_version);
		    			return;
		    		}else{
//		    			Log.i(tag, "into noany");
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
			    }
			    })
	
	    .show();
        }
        
        /**
         * 條件:末3碼和特獎核對成功
         * 才會進來該Method檢查剩下沒檢查的5碼
         */
        private static void checkRemain5InSPEC(String getnum,Context context,String voice_version){

        	Log.i(tag, "maybeNum: "+maybeNum);
        	String getNumHeadFive=getnum.substring(0, 5);
        	Log.i(tag, "getnum head5: "+getNumHeadFive);
        	String maybeNumHeadFive=maybeNum.substring(0, 5);
        	Log.i(tag, "maybeNum head5: "+maybeNumHeadFive);
        	
        	if(getNumHeadFive.equals(maybeNumHeadFive)){    
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
        private static void checkRemain5InHEAD(String getnum,Context context,String voice_version){

        	Log.i(tag, "maybeNum: "+maybeNum);

        	if(getnum.equals(maybeNum)){
			    
			    newDialog("恭喜你中了[頭獎]\n獎金: 20萬!","th200",context);
			    media.createMedia("th200",context,voice_version);
        	}else if(getnum.subSequence(1, 5).equals(maybeNum.substring(1, 5))){
			    
			    newDialog("恭喜你中了[二獎]\n獎金: 4萬元!","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getnum.subSequence(2, 5).equals(maybeNum.substring(2, 5))){
			    
			    newDialog("恭喜你中了[三獎]\n獎金: 1萬元!","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getnum.subSequence(3, 5).equals(maybeNum.substring(3, 5))){
			    
			    newDialog("恭喜你中了[四獎]\n獎金: 4千塊","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else if(getnum.subSequence(4, 5).equals(maybeNum.substring(4, 5))){
			    
			    newDialog("恭喜你中了[五獎]\n獎金: 1千塊","congratulations",context);
			    media.createMedia("onlycon",context,voice_version);
        	}else {
			    
			    newDialog("恭喜你中了[六獎]\n獎金: 200塊","hundred2",context);
			    media.createMedia("hundred2",context,voice_version);
        	}
        	
        	maybeNum="";
        }
   
}
