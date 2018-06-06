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
 * ��Ƶ�������ͨ���ö�����Ի�ȡ��ǰ����ʹ�õ���Ƶ����,�Լ�����Ƶ���в�����
 *  	1.���ż��ص����е���Ƶ������ʹ¼���ģ�Ҳ��������Ƶ�ļ���
 *  	2.¼��
 *  	3.���õ�ǰ����Ƶ�Ĳ���λ��
 *  	4.��ͣ����
 *  	5.��������ע�ᵽ���У������첽��ȡ��ǰ���ڲ��ŵ���Ƶ�Ľ��ȣ������̣߳�ÿ0.1���ȡһ�ε�ǰ���ȣ�
 *  	6.����ǰ¼�����ݴ洢���ļ���
 *  
 * @author Huan
 */
public class AudioManager {
	/*
	 * һ��Ӧ��ֻ��Ψһ��һ��AudioManager
	 * �������κδ������Ի�ȡ��Manager 
	 */
	private AudioManager() {
		playingThread.start();						//��ʵʱͬ����ȡ���ȵ��߳�
	}
	private static AudioManager manager = new AudioManager();
	public static AudioManager getInstance() {
		return manager;
	}
	
	/*
	 * ��ǰʹ�õ�Audio��Ƶ����
	 * �ṩgetCurrentAudio������ȡ
	 */
	private AudioObject currentAudio;	
	public AudioObject getCurrentAudio() {
		return currentAudio;
	}
	
	/*
	 * ��ǰʹ�õ�Audio��Ƶ������
	 */
	private static final String DEFAULT_RECORD = "¼��";
	private String audioName = "";
	public String getAudioName() {
		return audioName;
	}
	// �жϵ�ǰ����ʹ�õ���Ƶ�ļ��Ƿ���¼��
	public boolean isRecordAudio() {
		return audioName == DEFAULT_RECORD;
	}
	
	/*
	 * �ж�¼���Ƿ��Ѿ�����
	 */
	private boolean hasSaved = false;
	public boolean hasSaved() {
		return hasSaved;
	}
	
	// �޸ĵ�ǰʹ�õ���Ƶ��ʱ����Ҫ���õ�һϵ�в���
	private void changeCurrentAudio(String currentName, boolean hasSaved) {
		// �����Ž���ͬ���̰߳��µ���Ƶ����
		playingThread.setAudioObject(currentAudio);
		// �޸ĵ�ǰ��Ƶ������
		this.audioName = currentName;
		// �޸ĵ�ǰ��Ƶ�Ƿ񱣴��״̬
		this.hasSaved = hasSaved;
	}
	
	/*
	 * ʹ��һ���߳̽���¼��
	 */
	private AudioRecordThread recordThread;					//����¼�����߳�
	private byte[] audioRecordBuffer;						//���ڴ洢¼����������Ƶ����(��ʱ����)
	
	// ��ʼ¼��
	public void startRecord() {
		recordThread = new AudioRecordThread();
		recordThread.start();
	}
	// ����¼��
	public void finishRecord() {
		// 1.����¼���̵߳ķ���������¼��
		recordThread.finish();
		
		// 2.����getRecordBuffer��¼���߳��л�ȡ¼������
		audioRecordBuffer = recordThread.getRecordBuffer();
		
		// 3.��ĵ�ǰ����ʹ�õ���Ƶ����Ϊ¼������Ƶ
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
	

	//����¼�����ļ�
	public void save(String path) throws IOException {
		// 1.��¼���Ļ����л�ȡAudioInputStream
		AudioFormat format = AudioConfiguration.getFormat();
		AudioInputStream input = new AudioInputStream(
				new ByteArrayInputStream(audioRecordBuffer), 
				format, 
				audioRecordBuffer.length / format.getFrameSize()
				);
		// 2.д�뵽�ļ�
		File outputFile = new File(path);
		AudioSystem.write(input, AudioFileFormat.Type.WAVE, outputFile);
		
		this.changeCurrentAudio(outputFile.getName(), true);
	}

	
	/*
	 *  ���µ���Ƶ�ļ�
	 */
	public void openAudioFile(String path) throws UnsupportedAudioFileException, IOException {
		File file = new File(path);
		if(!file.exists()) {
			throw new RuntimeException();
		}
		this.openAudioFile(file);
	}
	public void openAudioFile(File file) throws UnsupportedAudioFileException, IOException {
		// ʹ��BufferedInputStreamװ������Ϊ��Ҫʹ��reset��mark
		AudioInputStream input = AudioSystem.getAudioInputStream(
				new BufferedInputStream(new FileInputStream(file))
				); 	
//		currentAudio = new AudioObject(input);
		currentAudio = AudioObject.getInstance(input);
		input.close();
		
		this.changeCurrentAudio(file.getName(), true);
	}

	
	/*
	 * ������Ƶ��ز���
	 */
	// ����
	public void playAudio() {
		currentAudio.playAudio();
	}
	// ��ͣ
	public void pauseAudio() {
		currentAudio.pauseAudio();
	}
	// ���ò���ʱ��(����ֵ)
	public void setNowTime(long position) {
		currentAudio.setNowTime(position);
	}
	

	/*
	 * ������ȡ��ǰ���Ž��ȵ��߳�
	 */
	private AudioPlayingThread playingThread = new AudioPlayingThread("���Ž���ͬ���߳�");
	// ��Ӽ���,ÿ�θ��̻߳�ȡ�µ���Ƶλ�õ�ʱ�򣬸������߷���һ����Ϣ
	public void addPlayingListener(PlayingListener listener) {
		playingThread.addPlayingListener(listener);
	}
	// ɾ������
	public void deletePlayingListener(PlayingListener listener) {
		playingThread.deletePlayingListener(listener);
	}
	
	
	/*
	 * ��ȡ��ǰAudio��ز���
	 */
	// ��ȡ����ʱ��(����ֵ)
	public long getTotalTime() {
		return currentAudio.getTotalTime();
	}
	// ��ȡ��֡��
	public int getFrameLength() {
		return currentAudio.getFrameLength();
	}
	// ��ȡ������
	public int getChannels() {
		return currentAudio.getChannels();
	}
	// ��ȡ��Ƶ��Ϣ
	public int[][] getData() {
		return currentAudio.getData();
	}
	// ��ȡ��ǰ���ŵ���λ�ã�����ֵ��
	public long getNowTime() {
		return currentAudio.getNowTime();
	}

}
