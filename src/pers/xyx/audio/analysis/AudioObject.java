package pers.xyx.audio.analysis;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * ��Ƶ����,ʹ�õ���ģʽ��ֻ֤��һ��AudioObject��Ҳ�Ǳ�ֻ֤��һ��Clip
 * ���⿪����߳�
 * @author Huan
 *
 */
public class AudioObject {
	
	private Clip clip;				// ���������������ţ���ͣ�Ȳ���
	private int[][] audioData;		// ��Ƶ��������Ϣ
	
	private static AudioObject obj = new AudioObject();
	protected static AudioObject getInstance(AudioInputStream is) {
			try {
			// ��ȡ��������
			if(obj.clip == null) {
				obj.clip = AudioSystem.getClip();
			} else {
				obj.clip.stop();
				obj.clip.close();
			}
				
			// ��Ϊ��������������ȡ��������������Ҫʹ��reset�������»ص���ʼ��λ��
			int streamLengthInBytes = (int) (is.getFrameLength() * is.getFormat().getFrameSize());
			is.mark(streamLengthInBytes);
			obj.clip.open(is);
			is.reset();
			
			// ��ȡ������Ϣ
			obj.parseInputStream(is);
		} catch (IOException |LineUnavailableException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
//	public AudioObject(AudioInputStream is) {
//		try {
//			// ��ȡ��������(��Ϊ��������������ȡ��������������Ҫʹ��reset�������»ص���ʼ��λ��)
//			int streamLengthInBytes = (int) (is.getFrameLength() * is.getFormat().getFrameSize());
//			is.mark(streamLengthInBytes);
//
//			clip = AudioSystem.getClip();
//			clip.open(is);
//			is.reset();
//			
//			// ��ȡ������Ϣ
//			this.parseInputStream(is);
//		} catch (IOException |LineUnavailableException e) {
//			e.printStackTrace();
//		}
//	}

	
	// ����AudioInputStream��ȡ������Ϣ
	private void parseInputStream(AudioInputStream is) throws IOException {
		AudioFormat format = is.getFormat();
		// 1.��ȡÿ�β��������ֽ���
		int sampleSize = format.getSampleSizeInBits() / 8;
		// 2.��ȡ��֡��
		long frameLength = is.getFrameLength();
		// 3.��ȡ������
		int channels = format.getChannels();
		
		// 4.�����������ݵ�����
		audioData = new int[channels][(int) frameLength];
		byte[] buffer = new byte[channels * sampleSize];	//ÿ��ֻ��ȡһ֡����
		// 5.����ÿһ֡����
		for(int i = 0; i < frameLength; i++) {
			is.read(buffer);
			
			for(int j = 0; j < channels; j++) {
				audioData[j][i] = this.parseByteArray(buffer, j*sampleSize, sampleSize);
			}
		}
	}
	
	// �ӻ����ֽ������л�ȡһ֡��������Ϣ
	private int parseByteArray(byte[] buffer, int start, int sampleSize) {
		if(sampleSize == 1) {
			return buffer[start] & 0xff;
		} else if(sampleSize == 2) {
			return (buffer[start] & 0xff) | (buffer[start+1] << 8);
		}
		throw new RuntimeException();
	}
	
	
	/**
	 * ������Ƶ��ز���,
	 * �û�ֻ��ͨ��AudioManager����ӵ���
	 */
	// ����
	protected void playAudio() {
		clip.start();
	}
	// ��ͣ
	protected void pauseAudio() {
		clip.stop();
	}
	// ���ò���ʱ��(����ֵ)
	protected void setNowTime(long position) {
		clip.setMicrosecondPosition(position * 1000);
	}

	// ��ȡ����ʱ��(����ֵ)
	public long getTotalTime() {
		return clip.getMicrosecondLength() / 1000;
	}
	// ��ȡ��֡��
	public int getFrameLength() {
		return clip.getFrameLength();
	}
	// ��ȡ������
	public int getChannels() {
		return clip.getFormat().getChannels();
	}
	// ��ȡ��Ƶ��Ϣ
	public int[][] getData() {
		return audioData;
	}
	// ��ȡ��ǰ���ŵ���λ�ã�����ֵ��
	public long getNowTime() {
		return clip.getMicrosecondPosition() / 1000;
	}
}
