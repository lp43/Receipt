package com.funtrigger.receipt;

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
import android.content.SharedPreferences.Editor;
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

	private final static String tag="tag";
	static WifiManager wm;
	static ConnectivityManager cm;

	/**
	 * dataRequest()用來連線財政部，抓取特定字串，轉存到data目錄裡,
	 * 存成當期︰Receipt_head.txt或前一期Receipt_head2.txt裡，供之後每次開啟程式時，產生實體的資料
	 * @param context 要用到data資料夾，就必須先有主體
	 * @param time 時間是指要跟伺服器請求檔案,是當期的,還是前一期的，如果是當期的，傳進來的字串會是"head",否則為"head2"
	 */
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
//				   Log.i(tag, "iwantmonth: "+iwantdate);
				   int dateend=iwantdate.indexOf("統");//字元是"統"的，是這次要抓的字串的最後字元
//				   Log.i(tag, "yearend: "+dateend);
				   String iwantyear=iwantdate.substring(0, dateend);
//				   Log.i(tag, "iwantyear: "+iwantyear);
				   
				   
				   //在計算完伺服器的對獎月份後，存到SharePreference裡，供主UI的Menu→對獎月份裡的字串使用
				   Editor sharedata = context.getSharedPreferences("data", 0).edit();
				   if(time.equals("head")){
					   sharedata.putString("head",iwantyear); 
					   sharedata.commit();  
				   }else if(time.equals("head2")){
					   sharedata.putString("head2",iwantyear); 
					   sharedata.commit();
				   }
				   
				   
				   int periodstartindex,periodendindex=0;
				   periodstartindex=contentBuffer.indexOf("<span class=\"style1\">",periodendindex);
				   periodendindex=contentBuffer.indexOf("</span>",periodstartindex);			   
				   String iwantperiod=contentBuffer.substring(periodstartindex+25, periodendindex);
				   Log.i(tag, "iwantperiod: "+iwantperiod);
//				   int peroidend=iwantdate.indexOf("統");
////				   Log.i(tag, "yearend: "+dateend);
//				   String iwantyear=iwantdate.substring(0, dateend);
//				   Log.i(tag, "iwantyear: "+iwantyear);
				   
				   
				   FileOutputStream fos = context.openFileOutput("receipt"+"_"+time+".txt", context.MODE_PRIVATE);
				   
				   fos.write(iwantyear.getBytes());
				
				   fos.write(System.getProperty("line.separator").getBytes());//寫入換行符
				   
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

				   fos.write(ch.getBytes());
				   fos.write(System.getProperty("line.separator").getBytes());
				   fos.write(iwantperiod.getBytes());
				   fos.close();
			} catch (IOException e) {
				Log.i(tag, e.getMessage());
			}
//			Log.i(tag, "content is: "+contentBuffer);
	}
	
	/**
	 * 檢查3G是否有連線
	 * @param context 請求需有主體
	 * @return 如果有連線,傳回True,否則傳回false
	 */
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
	
	/**
	 * 檢查Wifi是否有連線
	 * @param context 請求需有請求主體
	 * @return 只要WiFi的狀態是Disabled或沒有ip值，傳回false,否則傳回True
	 */
	public static boolean checkEnableingWifiStatus(Context context){
		wm =(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wm.getWifiState()==wm.WIFI_STATE_DISABLED|wm.getConnectionInfo().getIpAddress()==0){
			return false;
		}else{
			Log.i(tag, "ip adress: "+wm.getConnectionInfo().getIpAddress());
			return true;
		}	
	}	
}
