package com.funtrigger.receipt;

import android.content.Context;
import android.util.Log;

import com.funtrigger.receipt.logic.LastThree;
import com.funtrigger.receipt.logic.LeftToRight;
import com.funtrigger.receipt.logic.RightToLeft;

/**
 * Type類別是依據傳來的邏輯判斷方式,
 * 設定各種的螢幕上方數字方塊的顯示方式,
 * 另外還呼叫了各種不同的運算邏輯
 */
public class Type {
	private static String tag="tag";
	/**
	 * numtotal將使用者輸入的每一筆數值，
	 * 一筆一筆累加,最多會有8個數字被存放
	 */
	private static String numtotal="";
	public static String first5total="";
	/**
     * 描述 : 頂頭描述框的計算公式,且邏輯是由右至左<br/>
     * @param num 當要使用這個函式時，會將要在文字框印出的數字傳進來，再做判斷，然後顯示
     */
    public static void rightToLeft(String num,Context context){
//    	Log.i(tag, "get length: "+Receipt.textview.getText().length());
////    	Log.i(tag, "num: "+num);
//    	if(Receipt.textview.getText().length()==0){    	
//    		Receipt.textview.setText(num);
//    			RightToLeft.checkLast(Receipt.textview.getText().toString(),context,Receipt.voice_version);
//			
//    	}else if(Receipt.textview.getText().length()>0&Receipt.textview.getText().length()<Receipt.limit-1){
//
//    		Receipt.textview.setText(num+Receipt.textview.getText());	
//    	}else if(Receipt.textview.getText().length()==Receipt.limit-1){
//
//    		Receipt.textview.setText(num+Receipt.textview.getText());
//    		if(Receipt.textview.getText().length()==3){
//    			Log.i(tag, "into length 3");
//    				RightToLeft.checkThree(Receipt.textview.getText().toString(),context,Receipt.voice_version);
//        		
//    			
//    		}else if(Receipt.textview.getText().length()==8){
//    			Log.i(tag, "into length 8");
//    				RightToLeft.checkEight(Receipt.textview.getText().toString(),context,Receipt.voice_version);
//    			
//    		}
//    		
//    	}
    	
//    	 if(numtotal.equals("")){    
//     		Receipt.textview.setText("???????"+num);
//     	} else if(numtotal.length()==1){
//     		Receipt.textview.setText("??????"+num+numtotal);
//     	}else if(numtotal.length()==2){    
//     		Receipt.textview.setText("?????"+num+numtotal);
//     		RightToLeft.checkThree(num+numtotal,context,Receipt.voice_version);
//     	}else if(numtotal.length()==3){    
//     		Receipt.textview.setText("????"+num+numtotal);
//     	}else if(numtotal.length()==4){    
//     		Receipt.textview.setText("???"+num+numtotal);
//     	}else if(numtotal.length()==5){    
//     		Receipt.textview.setText("??"+num+numtotal);
//     	}else if(numtotal.length()==6){    
//     		Receipt.textview.setText("?"+num+numtotal);
//     	}else if(numtotal.length()==7){    
//     		Receipt.textview.setText(num+numtotal);
//     		LeftToRight.checkEight(num+numtotal,context,Receipt.voice_version);
//     	}else if(numtotal.length()==8){
// 			Log.i(tag, "into length 8");
// 			Receipt.textview.setText("???????"+num);
// 			numtotal="";	
// 		}	 
//    	 numtotal=num+numtotal;
     
   	 numtotal=num+numtotal;
   	 if(numtotal.equals("")){    
  		Receipt.textview.setText("????????"+num);
  	} else if(numtotal.length()==1){
  		Receipt.textview.setText("???????"+numtotal);
  	}else if(numtotal.length()==2){    
  		Receipt.textview.setText("??????"+numtotal);
  		
  	}else if(numtotal.length()==3){    
  		Receipt.textview.setText("?????"+numtotal);
  		RightToLeft.checkThree(numtotal,context,Receipt.voice_version);
  	}else if(numtotal.length()==4){    
  		Receipt.textview.setText("????"+numtotal);
  	}else if(numtotal.length()==5){    
  		Receipt.textview.setText("???"+numtotal);
  	}else if(numtotal.length()==6){    
  		Receipt.textview.setText("??"+numtotal);
  	}else if(numtotal.length()==7){    
  		Receipt.textview.setText("?"+numtotal);
  		
  	}else if(numtotal.length()==8){
  		Receipt.textview.setText(numtotal);
			Log.i(tag, "into length 8");
			LeftToRight.checkEight(numtotal,context,Receipt.voice_version);
			numtotal="";
				
		}	 
 
     	Log.i(tag, "now numtotal: "+numtotal);

    }
    
	/**
     * 描述 : 頂頭描述框的計算公式,且邏輯是由左至右<br/>
     * @param num 當要使用這個函式時，會將要在文字框印出的數字傳進來，再做判斷，然後顯示
     */
    public static void leftToRight(String num,Context context){

    	 if(numtotal.equals("")){    
    		Receipt.textview.setText(num+"???????");
    	} else if(numtotal.length()==1){    
    		Receipt.textview.setText(numtotal+num+"??????");
    	}else if(numtotal.length()==2){    
    		Receipt.textview.setText(numtotal+num+"?????");
    	}else if(numtotal.length()==3){    
    		Receipt.textview.setText(numtotal+num+"????");
    	}else if(numtotal.length()==4){    
    		Receipt.textview.setText(numtotal+num+"???");
    	}else if(numtotal.length()==5){    
    		Receipt.textview.setText(numtotal+num+"??");
    	}else if(numtotal.length()==6){    
    		Receipt.textview.setText(numtotal+num+"?");
    	}else if(numtotal.length()==7){    
    		Receipt.textview.setText(numtotal+num);
    		LeftToRight.checkEight(Receipt.textview.getText().toString(),context,Receipt.voice_version);
    	}else if(numtotal.length()==8){
			Log.i(tag, "into length 8");
			Receipt.textview.setText(num+"???????");
			numtotal="";	
		}	 
    	numtotal+=num;

    	Log.i(tag, "now numtotal: "+numtotal);
    }
    
	/**
     * 描述 : 頂頭描述框的計算公式,且邏輯是輸入末3碼<br/>
     * @param num 當要使用這個函式時，會將要在文字框印出的數字傳進來，再做判斷，然後顯示
     */
    public static void lastThree(String num,Context context){
    	 if(Receipt.getterGot()==false){
        	 if(numtotal.equals("")){
        		 Receipt.setterGot(false);
        		 
        		 Receipt.textview.setText("?????"+num+"??");
        	 }else if(numtotal.length()==1){
        		 Receipt.textview.setText("?????"+numtotal+num+"?");
        	 }else if(numtotal.length()==2){
        		 Receipt.textview.setText("?????"+numtotal+num);
        		 LastThree.checkThree(numtotal+num,context,Receipt.voice_version);
        	 }else if(numtotal.length()==3){
        		 Receipt.textview.setText("?????"+num+"??");
        		 Receipt.setterGot(false);
        		 numtotal="";
        	 }
        	 numtotal+=num;	
    	 }else if(Receipt.getterGot()==true){
    		 if(numtotal.length()==3){
    			 if(first5total.equals("")){			
    				 first5total+=num;
            		 Receipt.textview.setText(num+"????"+numtotal);
            	 } else if(first5total.length()==1){
            		 first5total+=num;
            		 Receipt.textview.setText(first5total+"???"+numtotal);
            	 }else if(first5total.length()==2){
            		 first5total+=num;
            		 Receipt.textview.setText(first5total+"??"+numtotal);
            	 }else if(first5total.length()==3){
            		 first5total+=num;
            		 Receipt.textview.setText(first5total+"?"+numtotal);
            	 }else if(first5total.length()==4){
            		 
            		 Receipt.textview.setText(first5total+num+numtotal);
            		 LastThree.checkRemain5(first5total+num, context, Receipt.voice_version);
            		 Receipt.setterGot(false);	 
            		 first5total="";
            	 }		 
        		 
        	 }
    	 }

    }
    
    /**
     * 將numtotal清空
     * @return 若成功清空，回傳true,否則回傳false
     */
    public static boolean resetNumTotal(){
    	boolean returnvalue=false;
    	if(!numtotal.equals("")){
    		Log.i(tag, "Type.resetNumTotal()");
    		numtotal="";
    		returnvalue=true;
    	}
		return returnvalue;
    }
}
