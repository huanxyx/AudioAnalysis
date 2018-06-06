package pers.xyx.gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import pers.xyx.audio.analysis.AudioManager;
import pers.xyx.audio.analysis.AudioObject;
import pers.xyx.audio.analysis.playing.PlayingListener;
import pers.xyx.gui.plot.PlotPanel;
import pers.xyx.gui.plot.model.PlotGraphicObject;
import pers.xyx.gui.plot.utils.PlotUtils;

/**
 * 用户的主界面
 * @author Huan
 *
 */
public class MainFrame extends JFrame implements ActionListener, PlayingListener{
	private static final long serialVersionUID = 5308636391499450505L;
	public MainFrame() {
		init();
	}
	
	private AudioManager manager;								//音频管理对象
	
	private JPanel container;									//容纳所有组件的面板
	
	private PlotPanel plotPanel;								//数据可视化面板
	
	private JButton showWaveButton;								//显示波形
	private JButton startRecordButton;							//开始录音
	private JButton finishRecordButton;							//结束录音
	private JButton saveButton;									//保存
	
	private JButton playButton;									//播放声音
	private JButton pauseButton;								//暂停播放
	private JButton replayButton;								//重新播放
	private JButton openFileButton;								//打开音频文件
	
	private JLabel progressLabel;									//音频当前状态
	
	private FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Wave(.wav)", "wav"
			);
	private JFileChooser chooser;
	

	/**
	 * @Description 用于初始化Frame和各种组件
	 */
	private void init() {
		manager = AudioManager.getInstance();
		manager.addPlayingListener(this);
		chooser = new JFileChooser(".");	//文件选择
		chooser.setFileFilter(filter); 		//文件过滤
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// 0.setContentPane
		container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setBackground(Color.GREEN);
		this.setContentPane(container);
		
		// 1.初始化PlotPanel
		initPlotPanel();
		
		// 2.初始化按钮
		initButtonPanel();
		
		// 3.初始化JFrame
		this.setTitle("音频分析");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.pack();											//让容器适应内部组件的大小，若是之后又调用了setSize，则会失效
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation( (screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}
	
	// 初始化PlotPanel
	private void initPlotPanel() {
		plotPanel = new PlotPanel();
		plotPanel.setBackground(new Color(204, 232, 207));
		plotPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JPanel wrap = new JPanel();
		wrap.setLayout(new BorderLayout());
		wrap.add(plotPanel, BorderLayout.CENTER);
		wrap.setPreferredSize(new Dimension( 600, 400));
		
		container.add(wrap, BorderLayout.NORTH);
	}
	
	// 初始化按钮
	private void initButtonPanel() {
		
		// 初始化按钮1
		showWaveButton = new JButton("显示波形");
		startRecordButton = new JButton("开始录音");
		finishRecordButton = new JButton("结束录音");
		saveButton = new JButton("保存录音");
		
		// 初始化按钮2
		playButton = new JButton("播放声音");
		replayButton = new JButton("重新播放");
		pauseButton = new JButton("暂停播放");
		openFileButton = new JButton("打开文件");
		
		// 当前音频进度
		progressLabel = new JLabel("00 / 00");
		
		// 为按钮添加监听器
		addListener();
		
		// 设置按钮的初始状态
		initButtonState();
		
		// 容纳器
		JPanel wrap1 = new JPanel();
		wrap1.setLayout(new FlowLayout());
		wrap1.add(showWaveButton);
		wrap1.add(startRecordButton);
		wrap1.add(finishRecordButton);
		wrap1.add(saveButton);
		
		JPanel wrap2 = new JPanel();
		wrap2.add(playButton);
		wrap2.add(pauseButton);
		wrap2.add(replayButton);
		wrap2.add(openFileButton);
		
		JPanel wrap3 = new JPanel();
		wrap3.add(progressLabel);
		
		// 添加到主面板
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		south.add(wrap1, BorderLayout.NORTH);
		south.add(wrap2, BorderLayout.CENTER);
		south.add(wrap3, BorderLayout.SOUTH);
		container.add(south, BorderLayout.CENTER);
	}
	
	// 为按钮添加监听器
	private void addListener() {
		showWaveButton.addActionListener(this);
		startRecordButton.addActionListener(this);
		finishRecordButton.addActionListener(this);
		saveButton.addActionListener(this);
		
		openFileButton.addActionListener(this);
		playButton.addActionListener(this);
		pauseButton.addActionListener(this);
		replayButton.addActionListener(this);
	}
	
	// 设置按钮的初始状态
	private void initButtonState() {
		showWaveButton.setEnabled(false);
		startRecordButton.setEnabled(true);
		finishRecordButton.setEnabled(false);
		saveButton.setEnabled(false);
		playButton.setEnabled(false);
		pauseButton.setEnabled(false);
		replayButton.setEnabled(false);
	}
	
	/**
	 * @Description 显示波形图
	 */
	private void showWave() {
		int[][] data = manager.getData();
		double[] yData = PlotUtils.translateToDoubleArray(data[0]);
		double[] xData = getXData();
		
		plotPanel.clear();
		plotPanel.plot(new PlotGraphicObject(xData, yData));
	}
	// 通过帧数和总时间获取每一帧的横轴坐标
	private double[] getXData() {
		int frameLength = manager.getFrameLength();
		long totalTime = manager.getTotalTime();
		double perFrameTime = (double)totalTime / frameLength / 1000;
		
		double[] xData = new double[frameLength];
		for(int i = 0; i < frameLength; i++) {
			xData[i] = perFrameTime * i;
		}
		return xData;
	}
	
	// 当新加载了文件或者录音结束，那么会更新所有按钮的状态
	private void updateButtonState() {
		showWaveButton.setEnabled(true);
		startRecordButton.setEnabled(true);
		finishRecordButton.setEnabled(false);
		if(manager.isRecordAudio()) {
			saveButton.setEnabled(true);
		} else {
			saveButton.setEnabled(false);
		}
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		replayButton.setEnabled(true);
	}
	// 更新名称
	private void updateFrameTitle() {
		String title = manager.getAudioName();
		if (!manager.hasSaved()) title += "(未保存)";
		this.setTitle(title);
	}
	
	/**
	 * 开始录音
	 */
	private void startRecord() {
		manager.startRecord();
		startRecordButton.setEnabled(false);
		finishRecordButton.setEnabled(true);
	}
	/**
	 * 结束录音
	 */
	private void finishRecord() {
		manager.finishRecord();
		finishRecordButton.setEnabled(false);
		startRecordButton.setEnabled(true);
		
		updateButtonState();
		updateFrameTitle();
	}
	/**
	 * 开始播放
	 */
	private void play() {
		manager.playAudio();
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
	}
	/**
	 * 暂停播放
	 */
	private void pause() {
		manager.pauseAudio();
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
	}
	/**
	 * 重新播放
	 */
	private void replay() {
		manager.setNowTime(0);
		manager.playAudio();
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
	}

	/**
	 * 打开音频文件
	 */
	private void openFile() {	
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File openFile = chooser.getSelectedFile();
			try {
				manager.openAudioFile(openFile);
				updateButtonState();
				updateFrameTitle();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 保存录音
	 */
	private void save() {
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File outFile = chooser.getSelectedFile();
			try {
				manager.save(outFile.getAbsolutePath() + ".wav");
				updateButtonState();
				updateFrameTitle();
			} catch (Exception e) {
			}
		}
	}

	
	// 各种按钮的事件
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if(btn == startRecordButton) {
			startRecord();
		} else if(btn == finishRecordButton) {
			finishRecord();
		} else if(btn == saveButton) {
			save();
		} else if(btn == showWaveButton) {
			showWave();
		} else if(btn == openFileButton) {
			openFile();
		} else if(btn == replayButton) {
			replay();
		} else if(btn == pauseButton) {
			pause();
		} else if(btn == playButton) {
			play();
		} 
	}
	
	
	/**
	 * @Description 测试
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

	// 用来更新当前的进度
	public void updateAudio(AudioObject obj) {
		if(obj == null) return;
		// 播放完毕的时候
		if (obj.getNowTime() == obj.getTotalTime()) {
			manager.setNowTime(0);
			pause();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				long totalTime = obj.getTotalTime() / 1000;
				long nowTime = obj.getNowTime() / 1000;
				
				String nowStr = nowTime < 10 ? ("0" + nowTime) : nowTime+"";
				String totalStr = totalTime < 10 ? ("0" + totalTime) : totalTime+"";
				
				progressLabel.setText(nowStr + " / " + totalStr);
			}
		});
	}
}
