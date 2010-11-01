package com.camangi.receipt.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import com.camangi.receipt.Receipt;

public class Media {
	private MediaPlayer mediaPlayer01;
	private String tag="tag";
	
	/* private void createMedia(Context context,String userPressed){
		   mediaPlayer01=new MediaPlayer();
	    	try {
	    		Log.i(tag, "get choice is: "+userPressed);

	    			mediaPlayer01=MediaPlayer.create(this, context.getResources().getIdentifier(userPressed+"_"+voice_version, "raw", context.getPackageName()));
	        		mediaPlayer01.start();

	    		
				} catch (IllegalStateException e) {
					Log.i(tag, "IllegalStateException: "+e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					Log.i(tag, "IOException: "+e.getMessage());
					e.printStackTrace();
				}
//				mediaPlayer01.stop();
				
				
				mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
//						Log.i(tag, "into onCompletion()");
						mp.release();
						
					}
				});
				mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
//						Log.i(tag, "into onError()");
						mp.release();
						return false;
					}
				});
	    }*/
	 
	 public void createMedia(String userPressed,Context context,String voice_version){
		 mediaPlayer01=new MediaPlayer();
	    	try {
//	    		Log.i(tag, "get choice is: "+userPressed);
	    		
	    		mediaPlayer01=MediaPlayer.create(context, context.getResources().getIdentifier(userPressed+"_"+voice_version, "raw", context.getPackageName()));
	        		

//				 mediaPlayer01.prepare();	
	    		mediaPlayer01.start();
				} catch (IllegalStateException e) {
					Log.i(tag, "IllegalStateException: "+e.getMessage());
					e.printStackTrace();
				} /*catch (IOException e) {
					Log.i(tag, "IOException: "+e.getMessage());
					e.printStackTrace();
				}*/
//				mediaPlayer01.stop();
				
				
				mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
//						Log.i(tag, "into onCompletion()");
						mp.release();
						
					}
				});
				mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
//						Log.i(tag, "into onError()");
						mp.release();
						return false;
					}
				});
	    }
}
