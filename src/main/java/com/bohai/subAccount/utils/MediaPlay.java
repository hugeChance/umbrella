package com.bohai.subAccount.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * 音乐播放
 * @author caojia
 */
public class MediaPlay {
	
	static Logger logger = Logger.getLogger(MediaPlay.class);
	
	public final static String MEDIA_FILE = "deal.wav";
	
	public static void play(){ 
		try{ 
			// 用输入流打开一音频文件 
			InputStream in = new FileInputStream(MEDIA_FILE);//FIlename 是你加载的声音文件如(“game.wav”) 
			// 从输入流中创建一个AudioStream对象 
			AudioStream as = new AudioStream(in); 
			AudioPlayer.player.start(as);//用静态成员player.start播放音乐 
			//AudioPlayer.player.stop(as);//关闭音乐播放 
			//如果要实现循环播放，则用下面的三句取代上面的“AudioPlayer.player.start(as);”这句 
			/*AudioData data = as.getData(); 
			ContinuousAudioDataStream gg= new ContinuousAudioDataStream (data); 
			AudioPlayer.player.start(gg);// Play audio. 
			*/ 
			//如果要用一个 URL 做为声音流的源(source)，则用下面的代码所示替换输入流来创建声音流： 
			/*AudioStream as = new AudioStream (url.openStream()); 
			*/ 
		} catch(FileNotFoundException e){ 
			logger.error("打开音频失败",e);
		} catch(IOException e){ 
			logger.error("打开音频失败",e);
		} catch (Exception e) {
			logger.error("打开音频失败",e);
		}
	}
	
	public static void play1(){
        String fileurl = "notify.wav";
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileurl));
            AudioFormat aif = ais.getFormat();
            SourceDataLine sdl = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);
            sdl = (SourceDataLine)AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            
            //play
            int nByte = 0;
            byte[] buffer = new byte[128];
            while(nByte != -1){
                nByte = ais.read(buffer,0,128);
                if(nByte >= 0){
                    int oByte = sdl.write(buffer, 0, nByte);
                    //System.out.println(oByte);
                }
            }
            sdl.stop();
        }catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动产生 catch 区块
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO 自动产生 catch 区块
            e.printStackTrace();
        }catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		
		MediaPlay.play();
		//MediaPlay.play("test.wav");
		//MediaPlay.Play();
	}

}
