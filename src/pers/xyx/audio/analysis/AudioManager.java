package pers.xyx.audio.analysis;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import pers.xyx.audio.analysis.conf.AudioConfiguration;
import pers.xyx.audio.analysis.playing.AudioPlayingThread;
import pers.xyx.audio.analysis.playing.PlayingListener;
import pers.xyx.audio.analysis.record.AudioRecordThread;

/**
 * 音频管理对象，通过该对象可以获取当前正在使用的音频对象,以及对音频进行操作：
 *  	1.播放加载到其中的音频（可以使录音的，也可以是音频文件）
 *  	2.录音
 *  	3.设置当前的音频的播放位置
 *  	4.暂停播放
 *  	5.将监听器注册到其中，可以异步获取当前正在播放的音频的进度（利用线程，每0.1秒获取一次当前进度）
 *  	6.将当前录音数据存储到文件中
 *  
 * @author Huan
 */
public class AudioManager {
	/*
	 * 一个应用只有唯一的一个AudioManager
	 * 便于在任何处都可以获取该Manager 
	 */
	private AudioManager() {
		playingThread.start();						//打开实时同步获取进度的线程
	}
	private static AudioManager manager = new AudioManager();
	public static AudioManager getInstance() {
		return manager;
	}
	
	/*
	 * 当前使用的Audio音频对象
	 * 提供getCurrentAudio方法获取
	 */
	private AudioObject currentAudio;	
	public AudioObject getCurrentAudio() {
		return currentAudio;
	}
	
	/*
	 * 当前使用的Audio音频的名字
	 */
	private static final String DEFAULT_RECORD = "录音";
	private String audioName = "";
	public String getAudioName() {
		return audioName;
	}
	// 判断当前正在使用的音频文件是否是录音
	public boolean isRecordAudio() {
		return audioName == DEFAULT_RECORD;
	}
	
	/*
	 * 判断录音是否已经保存
	 */
	private boolean hasSaved = false;
	public boolean hasSaved() {
		return hasSaved;
	}
	
	// 修改当前使用的音频的时候需要调用的一系列操作
	private void changeCurrentAudio(String currentName, boolean hasSaved) {
		// 将播放进度同步线程绑定新的音频对象
		playingThread.setAudioObject(currentAudio);
		// 修改当前音频的名称
		this.audioName = currentName;
		// 修改当前音频是否保存的状态
		this.hasSaved = hasSaved;
	}
	
	/*
	 * 使用一个线程进行录音
	 */
	private AudioRecordThread recordThread;					//用于录音的线程
	private byte[] audioRecordBuffer;						//用于存储录音下来的音频数据(临时数组)
	
	// 开始录音
	public void startRecord() {
		recordThread = new AudioRecordThread();
		recordThread.start();
	}
	// 结束录音
	public void finishRecord() {
		// 1.调用录音线程的方法，结束录音
		recordThread.finish();
		
		// 2.调用getRecordBuffer从录音线程中获取录音数据
		audioRecordBuffer = recordThread.getRecordBuffer();
		
		// 3.想改当前正在使用的音频对象为录音的音频
		AudioFormat format = AudioConfiguration.getFormat();
		AudioInputStream input = new AudioInputStream(
				new ByteArrayInputStream(audioRecordBuffer), 
				format, 
				audioRecordBuffer.length / format.getFrameSize()
				);
//		currentAudio = new AudioObject(input);
		currentAudio = AudioObject.getInstance(input);

		this.changeCurrentAudio(DEFAULT_RECORD, false);
	}
	

	//保存录音到文件
	public void save(String path) throws IOException {
		// 1.从录音的缓冲中获取AudioInputStream
		AudioFormat format = AudioConfiguration.getFormat();
		AudioInputStream input = new AudioInputStream(
				new ByteArrayInputStream(audioRecordBuffer), 
				format, 
				audioRecordBuffer.length / format.getFrameSize()
				);
		// 2.写入到文件
		File outputFile = new File(path);
		AudioSystem.write(input, AudioFileFormat.Type.WAVE, outputFile);
		
		this.changeCurrentAudio(outputFile.getName(), true);
	}

	
	/*
	 *  打开新的音频文件
	 */
	public void openAudioFile(String path) throws UnsupportedAudioFileException, IOException {
		File file = new File(path);
		if(!file.exists()) {
			throw new RuntimeException();
		}
		this.openAudioFile(file);
	}
	public void openAudioFile(File file) throws UnsupportedAudioFileException, IOException {
		// 使用BufferedInputStream装饰是因为需要使用reset和mark
		AudioInputStream input = AudioSystem.getAudioInputStream(
				new BufferedInputStream(new FileInputStream(file))
				); 	
//		currentAudio = new AudioObject(input);
		currentAudio = AudioObject.getInstance(input);
		input.close();
		
		this.changeCurrentAudio(file.getName(), true);
	}

	
	/*
	 * 播放音频相关操作
	 */
	// 播放
	public void playAudio() {
		currentAudio.playAudio();
	}
	// 暂停
	public void pauseAudio() {
		currentAudio.pauseAudio();
	}
	// 设置播放时间(毫秒值)
	public void setNowTime(long position) {
		currentAudio.setNowTime(position);
	}
	

	/*
	 * 用来获取当前播放进度的线程
	 */
	private AudioPlayingThread playingThread = new AudioPlayingThread("播放进度同步线程");
	// 添加监听,每次该线程获取新的音频位置的时候，给监听者发送一个消息
	public void addPlayingListener(PlayingListener listener) {
		playingThread.addPlayingListener(listener);
	}
	// 删除监听
	public void deletePlayingListener(PlayingListener listener) {
		playingThread.deletePlayingListener(listener);
	}
	
	
	/*
	 * 获取当前Audio相关操作
	 */
	// 获取播放时长(毫秒值)
	public long getTotalTime() {
		return currentAudio.getTotalTime();
	}
	// 获取总帧数
	public int getFrameLength() {
		return currentAudio.getFrameLength();
	}
	// 获取声道数
	public int getChannels() {
		return currentAudio.getChannels();
	}
	// 获取音频信息
	public int[][] getData() {
		return currentAudio.getData();
	}
	// 获取当前播放到的位置（毫秒值）
	public long getNowTime() {
		return currentAudio.getNowTime();
	}

}
