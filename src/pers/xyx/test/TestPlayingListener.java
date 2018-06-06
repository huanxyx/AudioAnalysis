package pers.xyx.test;

import pers.xyx.audio.analysis.AudioObject;
import pers.xyx.audio.analysis.playing.PlayingListener;

public class TestPlayingListener implements PlayingListener{

	@Override
	public void updateAudio(AudioObject obj) {
		System.out.println(obj.getNowTime());
	}

}
