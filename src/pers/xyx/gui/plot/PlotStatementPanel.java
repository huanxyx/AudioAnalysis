package pers.xyx.gui.plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pers.xyx.gui.plot.model.PlotGraphicObject;

/**
 * ���ڽ��ܲ�ͬ���ݵ�panel��
 * @author Huan
 *
 */
public class PlotStatementPanel extends JPanel{
	private static final long serialVersionUID = 2496068777384522847L;
	
	private PlotPanel plotPanel;
	
	public PlotStatementPanel(PlotPanel plotPanel) {
		this.plotPanel = plotPanel;
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	/*
	 * ͨ����ȡPlotPanel�ĳ�������panel��PlotPanel��λ��
	 */
	public void updatePosition() {
		int height = this.plotPanel.graphicObjectList.size() * 30;
		this.setBounds(plotPanel.getWidth() - 120, 10, 100, height);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(int i = 0; i < this.plotPanel.graphicObjectList.size(); i++) {
			PlotGraphicObject obj = this.plotPanel.graphicObjectList.get(i);
			String name = obj.getName();
			Color color = obj.getColor();
			
			g.setFont(new Font("����", Font.BOLD, 16));
			g.setColor(Color.BLACK);
			g.drawString(name, 5, 20 + i*30);
			g.setColor(color);
			g.drawLine(60, 15 + i*30, 90, 15 + i*30);
		}
	}
}
