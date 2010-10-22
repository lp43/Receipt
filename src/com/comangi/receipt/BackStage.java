package com.comangi.receipt;

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
import android.util.Log;

public class BackStage {
	public static String[] nums;
	private final static String tag="tag";

	public static void dataRequest(Context context,String time){
		 URL url = null;
		 HttpURLConnection uc = null;
		 String buffera,contentBuffer="";
		
		 try {
			 if(time.equals("now")){			
					url = new URL("http://invoice.etax.nat.gov.tw/etaxinfo_1.htm");
				
			 }else{
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
				   int startindex,endindex=0;
				  
				   startindex= contentBuffer.indexOf("<span class=\"number\">",endindex);
				   endindex=contentBuffer.indexOf("</span>",endindex);
				   while(startindex!=-1){
//					   Log.i(tag, "startindex: "+startindex);
					   
//					   Log.i(tag, "endindex: "+endindex);
					   String iwantbuffer=contentBuffer.substring(startindex+21, endindex);
					   iwant+=iwantbuffer+"、";
//					   Log.i(tag, "iwant: "+iwant);
					  
					   startindex= contentBuffer.indexOf("<span class=\"number\">",endindex);
					   endindex=contentBuffer.indexOf("</span>",startindex);
				   }
			 
				   br.close();
				   uc.disconnect();
				   
				   String ch=iwant.replace("、", ",");
				   nums = ch.split(",");

				   FileOutputStream fos = context.openFileOutput("receipt"+"_"+time+".txt", context.MODE_PRIVATE);
//				   for(String num:nums){
//					   Log.i(tag, num);	     			
//				   }
				   fos.write(ch.getBytes());
				   fos.close();
			} catch (IOException e) {
				Log.i(tag, e.getMessage());
			}
			Log.i(tag, "content is: "+contentBuffer);
	}
		
}
