package pers.xyx.test;

import java.io.IOException;

import pers.xyx.audio.analysis.AudioManager;

public class TestAudioManagerRecord {
	public static void main(String[] args) throws IOException {
		AudioManager audioManager = AudioManager.getInstance();
		
		audioManager.startRecord();
		
		System.in.read();
		audioManager.finishRecord();
		
		audioManager.playAudio();
	}
}
