package com.camangi.receipt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
/**
 * 這個後臺被拿來判斷是否有網路連線,
 * 如果有連線,也向伺服器請求對獎號碼資料,
 * 並將下載下來的資料,轉成txt型式,存在機器裡的data資料夾
 * @author simon
 *
 */
public class BackStage {
	public static String[] nums;
	private final static String tag="tag";
	static WifiManager wm;
	static ConnectivityManager cm;

	public static void dataRequest(Context context,String time){
		 URL url = null;
		 HttpURLConnection uc = null;
		 String buffera,contentBuffer="";
		
		 try {
			 if(time.equals("head")){			
					url = new URL("http://invoice.etax.nat.gov.tw/etaxinfo_1.htm");
				
			 }else if(time.equals("head2")){
				 url = new URL("http://invoice.etax.nat.gov.tw/etaxinfo_2.htm");
			 }
		 } catch (MalformedURLException e) {
				Log.i(tag, e.getMessage());
		 }
		 
		
		try {
			uc = (HttpURLConnection)url.openConnection();
	
				while((uc.getResponseCode()!=HttpURLConnection.HTTP_OK)){
				uc.disconnect();
				uc  = (HttpURLConnection)url.openConnection();
				}
				  InputStream is = uc.getInputStream();
				  InputStreamReader isr = new InputStreamReader(is);
				  BufferedReader br = new BufferedReader(isr);
				
				   do{
					   buffera = br.readLine();
	  					   if(buffera!=null){	   
	  						 contentBuffer+=new String(buffera.getBytes());   
	  					   } 					   
				   } while(buffera !=null);
				 
				   String iwant="";
				   
				   int monthstartindex,monthendindex=0;
				   monthstartindex=contentBuffer.indexOf("<div class=\"caption\">",monthendindex);
				   monthendindex=contentBuffer.indexOf("</div>",monthstartindex);			   
				   String iwantdate=contentBuffer.substring(monthstartindex+21, monthendindex);
				   Log.i(tag, "iwantmonth: "+iwantdate);
				   int dateend=iwantdate.indexOf("統");
				   Log.i(tag, "yearend: "+dateend);
				   String iwantyear=iwantdate.substring(0, dateend);
				   Log.i(tag, "iwantyear: "+iwantyear);
				   FileOutputStream fos = context.openFileOutput("receipt"+"_"+time+".txt", context.MODE_PRIVATE);
				   
				   fos.write(iwantyear.getBytes());
				
				   fos.write(System.getProperty("line.separator").getBytes());
				   
				   int startindex,endindex=0;
				   startindex= contentBuffer.indexOf("<span class=\"number\">",endindex);
				   endindex=contentBuffer.indexOf("</span>",endindex);
				   while(startindex!=-1){					   
//					   Log.i(tag, "startindex: "+startindex);
					   
//					   Log.i(tag, "endindex: "+endindex);
					   String iwantbuffer=contentBuffer.substring(startindex+21, endindex);
					   iwant+=iwantbuffer+"、";
//					   Log.i(tag, "iwant: "+iwant);
					  
					   //繼續往下找還有沒有數值
					   startindex= contentBuffer.indexOf("<span class=\"number\">",endindex);
					   endindex=contentBuffer.indexOf("</span>",startindex);
				   }
			 
				   br.close();
				   uc.disconnect();
				   
				   String ch=iwant.replace("、", ",");
				   nums = ch.split(",");

//				   FileOutputStream fos = context.openFileOutput("receipt"+"_"+time+".txt", context.MODE_PRIVATE);

				   fos.write(ch.getBytes());
				   fos.close();
			} catch (IOException e) {
				Log.i(tag, e.getMessage());
			}
//			Log.i(tag, "content is: "+contentBuffer);
	}
	
	public static boolean check3GConnectStatus(Context context){
		boolean net3g_status=false;
		
		cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni=cm.getActiveNetworkInfo();
//		Log.i(tag, "NetworkInfo status: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState());
		if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.DISCONNECTED|cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.UNKNOWN){		
			net3g_status=false;
		}else{
			net3g_status=true;
		}
		return net3g_status;
	}
	
	public static boolean checkEnableingWifiStatus(Context context){
		wm =(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wm.getWifiState()==wm.WIFI_STATE_DISABLED|wm.getConnectionInfo().getIpAddress()==0){
			return false;
		}else{
			return true;
		}	
	}	
}
