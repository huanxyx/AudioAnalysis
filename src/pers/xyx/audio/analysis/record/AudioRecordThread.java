package pers.xyx.audio.analysis.record;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import pers.xyx.audio.analysis.conf.AudioConfiguration;

/**
 * ¼���߳�
 * @author Huan
 *
 */
public class AudioRecordThread extends Thread{
	
	public AudioRecordThread() {
		super("¼���߳�");
	}
	
	private TargetDataLine targetDataLine;					//¼������
	private byte[] audioRecordBuffer;				//¼�����ݻ�������

	
	// ��ȡ¼������
	public byte[] getRecordBuffer() {
		return audioRecordBuffer;
	}
	
	// �ر�¼��
	public void finish() {
		targetDataLine.stop();
		targetDataLine.close();
	}
	
	// �߳�ִ�����壬���ϵĻ�ȡ��˷�����ݣ��������������ByteArrayOutputStream��
	@Override
	public void run() {
		// ��ȡTargetDataLine��������ȡ��Ƶ����
		try {
			targetDataLine = AudioSystem.getTargetDataLine(AudioConfiguration.getFormat());
			targetDataLine.open(AudioConfiguration.getFormat());
		} catch (LineUnavailableException e) {
			throw new RuntimeException("����TargetDataLineʧ�ܣ�");
		}
		
		// ��ʼ¼��
		targetDataLine.start();
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		while((len = targetDataLine.read(buffer, 0, buffer.length)) > 0) {
			bufferOutputStream.write(buffer, 0, len);
		}
		// ����¼��
		try {
			bufferOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("¼����ϣ�");
		
		// ת��Ϊ��������
		audioRecordBuffer = bufferOutputStream.toByteArray();
	}
}
