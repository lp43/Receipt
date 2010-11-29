package com.funtrigger.tools;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.funtrigger.receipt.Receipt;

/**
 * 這個類別專用來產生和管理各種中獎的Dialog視窗
 * @author simon
 *
 */
public class ResponseDialog {

	private static String tag="tag"; 
	public static Toast toast;
	
	/**
	 * 如果發票有中會呼叫這個函式，並接收傳來的參數<br/>
	 * @param context 產生此視窗的主體
	 * @param message 想要顯示的訊息內容
	 * @param icon 想要顯示的icon
	 */
	public static void newDialog(Context context/*,final Class getClass*/,String message,String icon){
		Log.i(tag, "into CongraturationsDialog.newDialog");
        new AlertDialog.Builder(context)
            .setTitle("恭喜你")
		    .setIcon(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()))
		    .setMessage(message)
		    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
		
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
//		    Receipt.textview.setText("");
		    Receipt.resetTextfive();
		    Receipt.setBadButton();
		    
//			try {
//				//呼叫該傳來的Class裡的函式
//				Method method = getClass.getMethod("resetTextfive", null);
//				method.invoke(null, null);				
//			} catch (SecurityException e) {
//				Log.i(tag, "newDialog.SecurityException");
//			} catch (NoSuchMethodException e) {
//				Log.i(tag, "newDialog.NoSuchMethodException");
//			} catch (IllegalArgumentException e) {
//				Log.i(tag, "newDialog.IllegalArgumentException");
//			} catch (IllegalAccessException e) {
//				Log.i(tag, "newDialog.IllegalAccessException");
//			} catch (InvocationTargetException e) {
//				Log.i(tag, "newDialog.InvocationTargetException");
//			}
		    }
		    })

   .show();
   }
	
	/**
	 * 果發票沒有中會呼叫這個函式，並接收傳來的參數<br/>
	 * @param context 產生此視窗的主體
	 * @param message 想要顯示的訊息內容
	 * @param icon 想要顯示的icon
	 */
	public static void newBadDialog(Context context,String message,String icon){
		Log.i(tag, "into CongraturationsDialog.newBadDialog");
        new AlertDialog.Builder(context)
            .setTitle("真可惜")
		    .setIcon(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()))
		    .setMessage(message)
		    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
		
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
//		    Receipt.textview.setText("");
			Receipt.resetTextfive();
			Receipt.setBadButton();
		    }
		    })

		    
   .show();
	}
	
	/**
	 * 該視窗是Receipt裡最基本的Dialog視窗
	 * @param context 產生此視窗的主體
	 * @param title 想要顯示的視窗標題
	 * @param message 想要顯示的訊息內容
	 * @param icon 想要產生的icon
	 */
	public static void newNotifyDialog(Context context,String title,String message,String icon){
		Log.i(tag, "into CongraturationsDialog.newNotifyDialog");
        new AlertDialog.Builder(context)
            .setTitle(title)
		    .setIcon(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()))
		    .setMessage(message)
		    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
		
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
//		    Receipt.textview.setText("");
			Receipt.resetTextfive();
			Receipt.setBadButton();
		    }
		    })

		    
   .show();
	}
	
	/**
	 * 該視窗是Receipt裡，當發票有中時的Toast視窗
	 * @param context 產生此視窗的主體
	 * @param message 想要顯示的訊息內容
	 * @param icon 想要產生的icon
	 */
	public static void newToast(Context context,String message,int icon){
		
		if(toast==null){
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		    View originView=toast.getView();
		    LinearLayout layout= new LinearLayout(context);
		    layout.setOrientation(LinearLayout.VERTICAL);
		    ImageView view = new ImageView(context);
//		    view.setImageResource(R.drawable.again);
		    view.setImageResource(icon);
		    layout.addView(view);
		    layout.addView(originView);
		    toast.setView(layout);
		    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		    toast.show();
		    
		    
		}
		
	}
	
	/**
	 * 該視窗是Receipt裡，當發票有沒有中時的Toast視窗
	 * @param context 產生此視窗的主體
	 * @param message 想要顯示的訊息內容
	 * @param icon 想要產生的icon
	 */
	public static void newBadToast(Context context,String message,int icon){
		
		if(toast==null){
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		    View originView=toast.getView();
		    LinearLayout layout= new LinearLayout(context);
		    layout.setOrientation(LinearLayout.VERTICAL);
		    ImageView view = new ImageView(context);
//		    view.setImageResource(R.drawable.again);
		    view.setImageResource(icon);
		    layout.addView(view);
		    layout.addView(originView);
		    toast.setView(layout);
		    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		    toast.show();
		    
		    Receipt.setBadButton();
		}
		
	}
	
	/**
	 * 這個函式是給由右至左對獎邏輯專用，
	 * 因為由右至左出現沒中的Toast的機率很高，
	 * 所以設計這個小款純文字、沒有圖案的Toast，
	 * 讓使用感覺更良好
	 * @param context 產生此視窗的主體
	 * @param message 想要顯示的訊息內容
	 */
	public static void newSingleToast(Context context,String message){
		if(toast==null){
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
			Receipt.setBadButton();
		}
	}
	
    /**
     * 這個函式專用來清除已顯示中的橘子Toast
     */
    public static void cancelToast(){
    	
		if(toast!=null){
			toast.cancel();
			Log.i(tag, "into cancelToast");
			toast=null;
			Receipt.setBadButton();
		}
    
    }
}
