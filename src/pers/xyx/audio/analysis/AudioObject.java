package pers.xyx.audio.analysis;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * 音频对象,使用单利模式保证只有一个AudioObject，也是保证只有一个Clip
 * 避免开多个线程
 * @author Huan
 *
 */
public class AudioObject {
	
	private Clip clip;				// 剪辑对象，用来播放，暂停等操作
	private int[][] audioData;		// 音频的数据信息
	
	private static AudioObject obj = new AudioObject();
	protected static AudioObject getInstance(AudioInputStream is) {
			try {
			// 获取剪辑对象
			if(obj.clip == null) {
				obj.clip = AudioSystem.getClip();
			} else {
				obj.clip.stop();
				obj.clip.close();
			}
				
			// 因为创建剪辑对象会读取输入流，所以需要使用reset将流重新回到开始的位置
			int streamLengthInBytes = (int) (is.getFrameLength() * is.getFormat().getFrameSize());
			is.mark(streamLengthInBytes);
			obj.clip.open(is);
			is.reset();
			
			// 获取数据信息
			obj.parseInputStream(is);
		} catch (IOException |LineUnavailableException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
//	public AudioObject(AudioInputStream is) {
//		try {
//			// 获取剪辑对象(因为创建剪辑对象会读取输入流，所以需要使用reset将流重新回到开始的位置)
//			int streamLengthInBytes = (int) (is.getFrameLength() * is.getFormat().getFrameSize());
//			is.mark(streamLengthInBytes);
//
//			clip = AudioSystem.getClip();
//			clip.open(is);
//			is.reset();
//			
//			// 获取数据信息
//			this.parseInputStream(is);
//		} catch (IOException |LineUnavailableException e) {
//			e.printStackTrace();
//		}
//	}

	
	// 解析AudioInputStream获取数据信息
	private void parseInputStream(AudioInputStream is) throws IOException {
		AudioFormat format = is.getFormat();
		// 1.获取每次采样所用字节数
		int sampleSize = format.getSampleSizeInBits() / 8;
		// 2.获取总帧数
		long frameLength = is.getFrameLength();
		// 3.获取声道数
		int channels = format.getChannels();
		
		// 4.创建保存数据的数组
		audioData = new int[channels][(int) frameLength];
		byte[] buffer = new byte[channels * sampleSize];	//每次只读取一帧数据
		// 5.解析每一帧数据
		for(int i = 0; i < frameLength; i++) {
			is.read(buffer);
			
			for(int j = 0; j < channels; j++) {
				audioData[j][i] = this.parseByteArray(buffer, j*sampleSize, sampleSize);
			}
		}
	}
	
	// 从缓存字节数组中获取一帧的数据信息
	private int parseByteArray(byte[] buffer, int start, int sampleSize) {
		if(sampleSize == 1) {
			return buffer[start] & 0xff;
		} else if(sampleSize == 2) {
			return (buffer[start] & 0xff) | (buffer[start+1] << 8);
		}
		throw new RuntimeException();
	}
	
	
	/**
	 * 播放音频相关操作,
	 * 用户只能通过AudioManager来间接调用
	 */
	// 播放
	protected void playAudio() {
		clip.start();
	}
	// 暂停
	protected void pauseAudio() {
		clip.stop();
	}
	// 设置播放时间(毫秒值)
	protected void setNowTime(long position) {
		clip.setMicrosecondPosition(position * 1000);
	}

	// 获取播放时长(毫秒值)
	public long getTotalTime() {
		return clip.getMicrosecondLength() / 1000;
	}
	// 获取总帧数
	public int getFrameLength() {
		return clip.getFrameLength();
	}
	// 获取声道数
	public int getChannels() {
		return clip.getFormat().getChannels();
	}
	// 获取音频信息
	public int[][] getData() {
		return audioData;
	}
	// 获取当前播放到的位置（毫秒值）
	public long getNowTime() {
		return clip.getMicrosecondPosition() / 1000;
	}
}
