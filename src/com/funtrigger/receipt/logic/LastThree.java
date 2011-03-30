package com.funtrigger.receipt.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.funtrigger.receipt.R;
import com.funtrigger.receipt.Receipt;
import com.funtrigger.tools.Media;
import com.funtrigger.tools.ResponseDialog;

/**
 * 末3碼的判斷邏輯是︰
 * 1.使用者先輸入末3碼
 * 2.程式判斷末3碼,若一樣才繼續判斷前5碼
 * @author simon
 *
 */
public class LastThree{
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
    * 描述 : 當使用者輸入完3碼後，要要求使用者繼續輸完8碼時，會呼叫這個運算函式
    * @param num
    */
        public static void checkThree(String getlast3num,Context context,String voice_version){
//	         Log.i(tag, "get last 3num is: "+ getlast3num);
	     
	         int i=0;
	        
	        for(String numcheck:Receipt.checknum){
	        	i++;//i幫我判別是特獎、頭獎還是增開獎			  
			    Log.i(tag, "now check to: "+numcheck);
//			    Log.i(tag, "Receipt.checknum.length: "+Receipt.checknum.length);
			    
//			    Log.i(tag, "numcheck lengh: "+numcheck.length());
			    //i的範圍以內是特別獎
			    if(numcheck.length()==8){
			
			    	if(i<Receipt.cutter+1){
			    		
			    		if(numcheck.subSequence(5, 8).equals(getlast3num)){
//			    			Log.i(tag, "last3 is the same with SPECIAL");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkEightType=EIGHTINSPECIAL;
			    			Log.i(tag, "maybeNum is: "+maybeNum);
			    			Receipt.textfive.setText("請繼續\"由左至右\"輸入前面5碼！");
			    			

			    			ResponseDialog.newGoodToast(context, "請繼續\"由左至右\"輸入前面5碼！", R.drawable.again);
			    		    
			    		    media.createMedia("again",context,voice_version);
			    		    Receipt.setterGot(true);
			    			return;
			    		}
			    	}else if(i>Receipt.cutter){
			    		if(numcheck.subSequence(5, 8).equals(getlast3num)){
//			    			Log.i(tag, "last3 is the same with HEAD");
			    			maybeNum=numcheck;//將核對到的可能中獎號碼存進maybeNum
			    			checkEightType=EIGHTINHEAD;
			    			Log.i(tag, "maybeNum is: "+maybeNum);
			    			Receipt.textfive.setText("請繼續\"由左至右\"輸入前面5碼！");
		    		    
			    		    ResponseDialog.newGoodToast(context, "請繼續\"由左至右\"輸入前面5碼！", R.drawable.again);
			    		    
			    		    media.createMedia("again",context,voice_version);
			    		    Receipt.setterGot(true);
			    			return;
			    		}
				    }
			    }else if(numcheck.length()==3){		    	
			    	
			    	if(numcheck.equals(getlast3num)){
//		    			Log.i(tag, "last3 is the same with NEWADD");

			    		ResponseDialog.newGoodDialog(context, "恭喜你中了[增開六獎]\n獎金: 200塊", "congratulations");
		    			media.createMedia("notsimple",context,voice_version);
		    			return;
		    		}

			    }	   
	        }

	         
	         ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
	         
	         media.createMedia("nono",context,voice_version);
	         return;
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
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[特獎]\n獎金: 200萬!(含以上)", "million2");

			    media.createMedia("million2",context,voice_version);
        	}else{

        		
        		 ResponseDialog.newBadToast(context, "沒中...", R.drawable.no);
        		
		         Receipt.resetTextfive();
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
        		ResponseDialog.newGoodDialog(context,"恭喜你中了[頭獎]\n獎金: 20萬!", "th200");

			    media.createMedia("th200",context,voice_version);
        	}else if(getfirst5num.substring(1, 5).equals(maybeNum.substring(1, 5))){
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[二獎]\n獎金: 4萬元!", "congratulations");

			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(2, 5).equals(maybeNum.substring(2, 5))){
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[三獎]\n獎金: 1萬元!", "congratulations");

			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(3, 5).equals(maybeNum.substring(3, 5))){
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[四獎]\n獎金: 4千塊!", "congratulations");

			    media.createMedia("onlycon",context,voice_version);
        	}else if(getfirst5num.substring(4, 5).equals(maybeNum.substring(4, 5))){
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[五獎]\n獎金: 1千塊!", "congratulations");

			    media.createMedia("onlycon",context,voice_version);
        	}else {
        		ResponseDialog.newGoodDialog(context, "恭喜你中了[六獎]\n獎金: 200塊", "hundred2");

			    media.createMedia("hundred2",context,voice_version);
        	}
        	
        	maybeNum="";
        }
        

		
}
