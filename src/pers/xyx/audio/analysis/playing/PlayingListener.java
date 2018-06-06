package pers.xyx.audio.analysis.playing;

import pers.xyx.audio.analysis.AudioObject;

/**
 * 音频播放监听器，可以获取正在播放的音频信息
 * @author Huan
 *
 */
public interface PlayingListener {
	public abstract void updateAudio(AudioObject obj);
}
