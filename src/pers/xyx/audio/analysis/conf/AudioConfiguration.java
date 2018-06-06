package pers.xyx.audio.analysis.conf;

import javax.sound.sampled.AudioFormat;

/**
 * ��Ƶ��ص����ò���
 * @author Huan
 *
 */
public class AudioConfiguration {
	
	// Ĭ�ϵ�¼��AudioForamt
	private static AudioFormat defaultFormat;
	public static AudioFormat getFormat() {
		if(defaultFormat == null) {
			defaultFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,		// �����ʽ
					44100.0F,								// ������
					16,										// ÿ�β�������Ҫ��bit��
					1,										// ������
					2,										// ÿһ֡�Ĵ�С��������*������С��
					44100.0F,								// ֡��
					false
					);
		}
		return defaultFormat;
	}
}
