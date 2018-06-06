package pers.xyx.audio.analysis.playing;

import java.util.ArrayList;
import java.util.List;

import pers.xyx.audio.analysis.AudioObject;

/**
 * 用户异步获取当前播放位置的线程，并将信息发送给注册的监听器
 * @author Huan
 *
 */
public class AudioPlayingThread extends Thread{
	public AudioPlayingThread(String name) {
		super(name);
	}
	
	/*
	 * 绑定的音频对象
	 */
	private AudioObject audioObject;
	public void setAudioObject(AudioObject obj) {
		this.audioObject = obj;
	}
	
	/*
	 * 监听器
	 */
	private List<PlayingListener> registeredListener = new ArrayList<PlayingListener>();
	// 添加监听器
	public void addPlayingListener(PlayingListener listener) {
		registeredListener.add(listener);
	}

	// 删除监听器
	public void deletePlayingListener(PlayingListener listener) {
		registeredListener.remove(listener);
	}
	
	// 发送消息给监听器
	public void notifyPlayingListeners() {
		for(PlayingListener listener : registeredListener) {
			listener.updateAudio(audioObject);
		}
	}
	
	/*
	 *  不断获取当前播放音频的播放位置,并将消息发送给注册的监听器
	 */
	public void run() {
		super.run();
		try {
			while(true) {
				Thread.sleep(100);
				this.notifyPlayingListeners();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
