package pers.xyx.gui.plot.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pers.xyx.gui.plot.PlotPanel;

public class PlotPanelListener implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{
	
	private PlotPanel panel;
	
	private int lastX = 0;					// �϶�ʱ��һ����ĺ�����
	private int lastY = 0;					// �϶�ʱ��һ�����������
	
	public PlotPanelListener(PlotPanel panel) {
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.lastX = e.getX();
		this.lastY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.panel.showCurrentPos(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.panel.showCurrentPos(false);
	}

	//�϶��ı���ʾ������
	@Override
	public void mouseDragged(MouseEvent e) {
		int deltX = e.getX() - this.lastX;
		int deltY = e.getY() - this.lastY;
		this.lastX = e.getX();
		this.lastY = e.getY();
		panel.moveAxis(deltX, deltY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.panel.setCurrentPos(e.getX(), e.getY());
		this.panel.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}
	@Override
	public void keyReleased(KeyEvent e) {

	}

	// ���ֲ���+ctrl�����зŴ������С
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if(e.isControlDown()) {
			if(e.getWheelRotation() == 1) {
				panel.zoomOut(e.getX(), e.getY());
			} else if(e.getWheelRotation() == -1) {
				panel.zoomIn(e.getX(), e.getY());
			} 
		}
	}
	
}
