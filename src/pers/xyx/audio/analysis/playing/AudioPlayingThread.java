package pers.xyx.audio.analysis.playing;

import java.util.ArrayList;
import java.util.List;

import pers.xyx.audio.analysis.AudioObject;

/**
 * �û��첽��ȡ��ǰ����λ�õ��̣߳�������Ϣ���͸�ע��ļ�����
 * @author Huan
 *
 */
public class AudioPlayingThread extends Thread{
	public AudioPlayingThread(String name) {
		super(name);
	}
	
	/*
	 * �󶨵���Ƶ����
	 */
	private AudioObject audioObject;
	public void setAudioObject(AudioObject obj) {
		this.audioObject = obj;
	}
	
	/*
	 * ������
	 */
	private List<PlayingListener> registeredListener = new ArrayList<PlayingListener>();
	// ��Ӽ�����
	public void addPlayingListener(PlayingListener listener) {
		registeredListener.add(listener);
	}

	// ɾ��������
	public void deletePlayingListener(PlayingListener listener) {
		registeredListener.remove(listener);
	}
	
	// ������Ϣ��������
	public void notifyPlayingListeners() {
		for(PlayingListener listener : registeredListener) {
			listener.updateAudio(audioObject);
		}
	}
	
	/*
	 *  ���ϻ�ȡ��ǰ������Ƶ�Ĳ���λ��,������Ϣ���͸�ע��ļ�����
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
