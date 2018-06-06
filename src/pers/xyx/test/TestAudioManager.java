package pers.xyx.test;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import pers.xyx.audio.analysis.AudioManager;

public class TestAudioManager {
	public static void main(String[] args) {
		AudioManager manager = AudioManager.getInstance();
//		
//		printInformation( "������Ƶ�ļ�֮ǰ", manager);
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
//			System.out.println("����ʧ�ܣ�");
//			return ;
//		}
//		
//		printInformation( "������Ƶ�ļ�֮֮��", manager);
//		manager.playAudio();
		try {
			manager.openAudioFile("wav_20_8_1_pcm.wav");
			manager.playAudio();
			pause();
			manager.startRecord();
			System.out.println("¼��");
			pause();
			System.out.println("����");
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
		System.out.println("AudioName��" + manager.getAudioName());
		System.out.println("��ǰ�Ƿ���¼��" + manager.isRecordAudio());
		System.out.println("¼���Ƿ񱣴�" + manager.hasSaved());
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
