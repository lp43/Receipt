package com.comangi.receipt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class BackStage {

	private final static String tag="tag";

	public static void dataRequest(String time){
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
				 
				 int startindex = contentBuffer.indexOf("<span class=\"number\">");
				 Log.i(tag, "<span class=\"number\">: "+String.valueOf(startindex));
				 
				 int endindex=contentBuffer.indexOf("</span>");
				 Log.i(tag, "</span>: "+String.valueOf(endindex));
				 String iwant=contentBuffer.substring(startindex, endindex);
				 Log.i(tag, "iwant is: "+iwant);
				   br.close();
				   uc.disconnect();
			
			} catch (IOException e) {
				Log.i(tag, e.getMessage());
			}
			Log.i(tag, "content is: "+contentBuffer);
	}
		
}
