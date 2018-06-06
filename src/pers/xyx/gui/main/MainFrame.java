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
 * �û���������
 * @author Huan
 *
 */
public class MainFrame extends JFrame implements ActionListener, PlayingListener{
	private static final long serialVersionUID = 5308636391499450505L;
	public MainFrame() {
		init();
	}
	
	private AudioManager manager;								//��Ƶ�������
	
	private JPanel container;									//����������������
	
	private PlotPanel plotPanel;								//���ݿ��ӻ����
	
	private JButton showWaveButton;								//��ʾ����
	private JButton startRecordButton;							//��ʼ¼��
	private JButton finishRecordButton;							//����¼��
	private JButton saveButton;									//����
	
	private JButton playButton;									//��������
	private JButton pauseButton;								//��ͣ����
	private JButton replayButton;								//���²���
	private JButton openFileButton;								//����Ƶ�ļ�
	
	private JLabel progressLabel;									//��Ƶ��ǰ״̬
	
	private FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Wave(.wav)", "wav"
			);
	private JFileChooser chooser;
	

	/**
	 * @Description ���ڳ�ʼ��Frame�͸������
	 */
	private void init() {
		manager = AudioManager.getInstance();
		manager.addPlayingListener(this);
		chooser = new JFileChooser(".");	//�ļ�ѡ��
		chooser.setFileFilter(filter); 		//�ļ�����
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// 0.setContentPane
		container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setBackground(Color.GREEN);
		this.setContentPane(container);
		
		// 1.��ʼ��PlotPanel
		initPlotPanel();
		
		// 2.��ʼ����ť
		initButtonPanel();
		
		// 3.��ʼ��JFrame
		this.setTitle("��Ƶ����");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.pack();											//��������Ӧ�ڲ�����Ĵ�С������֮���ֵ�����setSize�����ʧЧ
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation( (screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}
	
	// ��ʼ��PlotPanel
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
	
	// ��ʼ����ť
	private void initButtonPanel() {
		
		// ��ʼ����ť1
		showWaveButton = new JButton("��ʾ����");
		startRecordButton = new JButton("��ʼ¼��");
		finishRecordButton = new JButton("����¼��");
		saveButton = new JButton("����¼��");
		
		// ��ʼ����ť2
		playButton = new JButton("��������");
		replayButton = new JButton("���²���");
		pauseButton = new JButton("��ͣ����");
		openFileButton = new JButton("���ļ�");
		
		// ��ǰ��Ƶ����
		progressLabel = new JLabel("00 / 00");
		
		// Ϊ��ť��Ӽ�����
		addListener();
		
		// ���ð�ť�ĳ�ʼ״̬
		initButtonState();
		
		// ������
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
		
		// ��ӵ������
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		south.add(wrap1, BorderLayout.NORTH);
		south.add(wrap2, BorderLayout.CENTER);
		south.add(wrap3, BorderLayout.SOUTH);
		container.add(south, BorderLayout.CENTER);
	}
	
	// Ϊ��ť��Ӽ�����
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
	
	// ���ð�ť�ĳ�ʼ״̬
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
	 * @Description ��ʾ����ͼ
	 */
	private void showWave() {
		int[][] data = manager.getData();
		double[] yData = PlotUtils.translateToDoubleArray(data[0]);
		double[] xData = getXData();
		
		plotPanel.clear();
		plotPanel.plot(new PlotGraphicObject(xData, yData));
	}
	// ͨ��֡������ʱ���ȡÿһ֡�ĺ�������
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
	
	// ���¼������ļ�����¼����������ô��������а�ť��״̬
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
	// ��������
	private void updateFrameTitle() {
		String title = manager.getAudioName();
		if (!manager.hasSaved()) title += "(δ����)";
		this.setTitle(title);
	}
	
	/**
	 * ��ʼ¼��
	 */
	private void startRecord() {
		manager.startRecord();
		startRecordButton.setEnabled(false);
		finishRecordButton.setEnabled(true);
	}
	/**
	 * ����¼��
	 */
	private void finishRecord() {
		manager.finishRecord();
		finishRecordButton.setEnabled(false);
		startRecordButton.setEnabled(true);
		
		updateButtonState();
		updateFrameTitle();
	}
	/**
	 * ��ʼ����
	 */
	private void play() {
		manager.playAudio();
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
	}
	/**
	 * ��ͣ����
	 */
	private void pause() {
		manager.pauseAudio();
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
	}
	/**
	 * ���²���
	 */
	private void replay() {
		manager.setNowTime(0);
		manager.playAudio();
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
	}

	/**
	 * ����Ƶ�ļ�
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
	 * ����¼��
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

	
	// ���ְ�ť���¼�
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
	 * @Description ����
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

	// �������µ�ǰ�Ľ���
	public void updateAudio(AudioObject obj) {
		if(obj == null) return;
		// ������ϵ�ʱ��
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
