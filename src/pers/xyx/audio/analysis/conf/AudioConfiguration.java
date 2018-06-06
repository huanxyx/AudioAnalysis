package pers.xyx.audio.analysis.conf;

import javax.sound.sampled.AudioFormat;

/**
 * 音频相关的配置参数
 * @author Huan
 *
 */
public class AudioConfiguration {
	
	// 默认的录音AudioForamt
	private static AudioFormat defaultFormat;
	public static AudioFormat getFormat() {
		if(defaultFormat == null) {
			defaultFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,		// 编码格式
					44100.0F,								// 采样率
					16,										// 每次采样所需要的bit数
					1,										// 声道数
					2,										// 每一帧的大小（声道数*采样大小）
					44100.0F,								// 帧率
					false
					);
		}
		return defaultFormat;
	}
}
