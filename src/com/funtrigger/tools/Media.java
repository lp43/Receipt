package com.funtrigger.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.funtrigger.receipt.Receipt;

/**
 * 針對每一種中獎反應，給予各個聲音回饋
 * 這個類別用來播放每一種音效檔
 * @author simon
 *
 */
public class Media {
	private MediaPlayer mediaPlayer01;
	private String tag="tag";
	
	 
	 public void createMedia(String userPressed,Context context,String voice_version){
//		 mediaPlayer01=new MediaPlayer();
	    	try {
//	    		Log.i(tag, "get choice is: "+userPressed);
//	    		if(mediaPlayer01!=null){//在對一個物件做事情時,一定要確定有產生出來才能繼續執行,否則之前我遇到了NullPointerException
	    			mediaPlayer01=MediaPlayer.create(context, context.getResources().getIdentifier(userPressed+"_"+voice_version, "raw", context.getPackageName()));
		    		mediaPlayer01.start();
//	    		}
	    			
	    		
	    		
				} catch (IllegalStateException e) {
					Log.i(tag, "IllegalStateException: "+e.getMessage());
				} catch (NullPointerException e) {
					Log.i(tag, "NullPointerException: "+e.getMessage());
				}
				
				if(mediaPlayer01!=null){
						mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
//							Log.i(tag, "into onCompletion()");
							mp.release();
							
						}
					});
					
					mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {

						@Override
						public boolean onError(MediaPlayer mp, int what, int extra) {
//							Log.i(tag, "into onError()");
							mp.release();
							return false;
						}
					});				
				}
					
				
			
				
	    }
}
