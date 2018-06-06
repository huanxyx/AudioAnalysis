package pers.xyx.audio.analysis.record;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import pers.xyx.audio.analysis.conf.AudioConfiguration;

/**
 * 录音线程
 * @author Huan
 *
 */
public class AudioRecordThread extends Thread{
	
	public AudioRecordThread() {
		super("录音线程");
	}
	
	private TargetDataLine targetDataLine;					//录音对象
	private byte[] audioRecordBuffer;				//录音数据缓存数组

	
	// 获取录音数据
	public byte[] getRecordBuffer() {
		return audioRecordBuffer;
	}
	
	// 关闭录音
	public void finish() {
		targetDataLine.stop();
		targetDataLine.close();
	}
	
	// 线程执行主体，不断的获取麦克风的数据，并将数据输出到ByteArrayOutputStream中
	@Override
	public void run() {
		// 获取TargetDataLine对象来获取音频数据
		try {
			targetDataLine = AudioSystem.getTargetDataLine(AudioConfiguration.getFormat());
			targetDataLine.open(AudioConfiguration.getFormat());
		} catch (LineUnavailableException e) {
			throw new RuntimeException("创建TargetDataLine失败！");
		}
		
		// 开始录音
		targetDataLine.start();
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		while((len = targetDataLine.read(buffer, 0, buffer.length)) > 0) {
			bufferOutputStream.write(buffer, 0, len);
		}
		// 结束录音
		try {
			bufferOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("录音完毕！");
		
		// 转换为数据数组
		audioRecordBuffer = bufferOutputStream.toByteArray();
	}
}
