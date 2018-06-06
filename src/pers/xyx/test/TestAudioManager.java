package pers.xyx.test;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import pers.xyx.audio.analysis.AudioManager;

public class TestAudioManager {
	public static void main(String[] args) {
		AudioManager manager = AudioManager.getInstance();
//		
//		printInformation( "解析音频文件之前", manager);
//		
//		boolean result =  false;
//		try {
//			manager.openNewAudioFile("wav_40_16_2_pcm.wav");
//			result = true;
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		if(!result) {
//			System.out.println("解析失败！");
//			return ;
//		}
//		
//		printInformation( "解析音频文件之之后", manager);
//		manager.playAudio();
		try {
			manager.openAudioFile("wav_20_8_1_pcm.wav");
			manager.playAudio();
			pause();
			manager.startRecord();
			System.out.println("录音");
			pause();
			System.out.println("播放");
			manager.finishRecord();
			manager.playAudio();
			manager.save("./out.wav");
			printData(manager.getData());
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void printInformation(String time , AudioManager manager) {
		System.out.println(time);
		System.out.println("AudioName：" + manager.getAudioName());
		System.out.println("当前是否是录音" + manager.isRecordAudio());
		System.out.println("录音是否保存" + manager.hasSaved());
		System.out.println("-----------------------------------");
	}
	
	public static void printData(int[][] data) {
		for(int i = 0; i < data[0].length; i++) {
			System.out.print(data[0][i] + " ");
			if((i+1) % 5 == 0) {
				System.out.println();
			}
		}
	}
	
	public static void pause() {
		try {
			System.in.read();
			System.in.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
