package pers.xyx.gui.plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pers.xyx.gui.plot.listener.PlotPanelListener;
import pers.xyx.gui.plot.model.PlotGraphicObject;

/**
 * <pre>
 * 	��Ϊ�滭�Ļ���
 * 	ԭ��
 * 		ÿ�λ滭����ݵ�ǰ����ʾ��Χ���л滭������ֻ��Ҫ
 * 		�ı�������ķ�Χ��Ҳ���ǵ�ǰ��Ļ��ʾ�ķ�Χ�����н��в�ͬ�Ĳ�����
 * 		
 * 		����ͨ��ͼ�ζ����ȡ�����Ұ�����귶Χ��
 * 		
 * </pre>
 * @author Administrator
 *
 */
public class PlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// ���浱ǰ���������е�ͼ��
	protected List<PlotGraphicObject> graphicObjectList = 
			new ArrayList<PlotGraphicObject>();
	
	// ��¼�������ڻ����п�ʼ��λ���Լ�����
	private double axis_x = 0;
	private double axis_y = 0;
	private double axis_w = 15;
	private double axis_h = 15;
	
	// ��ǰ����ĳ���
	private int width;
	private int height;
	
	// �Ƿ���ʾ��ǰ������
	private boolean showCoordinate = false;
	// ��ǰ��������
	private double currentX;			
	private double currentY;
	
	// ��ͬ���ݵ�panel��ʾ
	private PlotStatementPanel statementPanel;
	
	public PlotPanel() {
		// ����꣬���̣������¼�
		PlotPanelListener listener = new PlotPanelListener(this);
		this.addMouseWheelListener(listener);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		// ���ñ���ɫ
		this.setBackground(Color.WHITE);
		
		// �������panel
		statementPanel = new PlotStatementPanel(this);
		this.add(statementPanel);
		this.setLayout(null);
	} 
	
	/**
	 * ����µ�ͼ��
	 * @param obj
	 */
	public void plot(PlotGraphicObject obj) {
		this.graphicObjectList.add(obj);
		this.suit();
		this.repaint();
	}
	
	/**
	 * @Description ��ջ�ͼ
	 */
	public void clear() {
		graphicObjectList.clear();
	}
	
	/*
	 * ����paintComponent����������滭
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// ����StatementPanel��λ�úʹ�С
		statementPanel.updatePosition();
		
		this.width = this.getWidth();
		this.height = this.getHeight();
		// 1.����������
		this.paintAxis((Graphics2D) g);
		// 2.�������е�ͼ��
		for(int i = 0; i < graphicObjectList.size(); i++) {
			this.paintGraphicObject((Graphics2D) g, graphicObjectList.get(i));
		}
		// 3.���Ƶ�ǰ��������
		this.drawCoordinateString(g);
	}
	
	// ����ͼ�ζ���
	private void paintGraphicObject(Graphics2D g, PlotGraphicObject obj) {
		double[] xData = obj.getXData();
		double[] yData = obj.getYData();
		
		// 1.������ɫ
		g.setColor(obj.getColor());
		
		// 2.��ʼ����
		for(int i = 0; i < xData.length - 1; i++) {
			// 3.1�жϸõ��Ƿ�λ����ʾ��Χ�����
			if(xData[i] < axis_x) {
				if(xData[i + 1] < axis_x) {
					continue;
				}
			}
			// 3.2�жϸõ��Ƿ�λ����ʾ��Χ���ұ�
			if(xData[i] > (axis_x + axis_w)) {
				break;
			}
			// 3.3����
			this.drawLine(g, xData[i], yData[i], xData[i+1], yData[i+1]);
		}
	}

	// ����������
	private void paintAxis(Graphics2D g) {
		g.setColor(Color.red);
		// ����x��
		this.drawLine(g, this.axis_x, 0, this.axis_x + this.axis_w, 0);
		// ����y��
		this.drawLine(g, 0, this.axis_y, 0, this.axis_y + this.axis_h);
	}
	
	// ���������ı�
	private void drawCoordinateString(Graphics g) {
		if(this.showCoordinate) {
			g.setColor(Color.DARK_GRAY);
			
			String x = String.format("%.4f", currentX);
			String y = String.format("%.4f", currentY);
			g.drawString("(" + x + "," + y  + ")", 10, 20);
		}
	}
	
	
	// ��������
	private void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
		g.drawLine(mappingX(x1), mappingY(y1), mappingX(x2), mappingY(y2));
	}
	
	// ����ϵ�е�λ�ö�Ӧ�����е�λ��
	private int mappingX(double x) {
		return this.disMappingX(x - this.axis_x);
	}
	private int mappingY(double y) {
		return this.height - this.disMappingY(y - this.axis_y);
	}
	
	// ����ϵ�еľ����Ӧ�����еľ���
	private int disMappingX(double disX) {
		return (int) (disX * (this.width / this.axis_w));
	}
	private int disMappingY(double disY) {
		return (int) (disY * (this.height / this.axis_h));
	}
	
	// �����ϵ�λ��ӳ�䵽����ϵ������
	private double inMappingX(int x) {
		return this.axis_x + inDisMappingX(x);
	}
	private double inMappingY(int y) {
		return this.axis_y + inDisMappingY(this.height - y);
	}
	
	// �����ϵľ���ӳ�䵽����ϵ�ϵľ���
	private double inDisMappingX(int disX) {
		return disX * (this.axis_w / this.width);
	}
	private double inDisMappingY(int disY) {
		return disY * (this.axis_h / this.height);
	}
	
	
	// ���������Ұ�µ�������ķ�Χ
	private void suit() {
		PlotGraphicObject obj = graphicObjectList.get(0);
		
		if(obj == null) {
			return ;
		}
		
		double startX = obj.suitableStartX();
		double endX = obj.suitableEndX();
		double startY = obj.suitableStartY();
		double endY = obj.suitableEndY();
		
		for(int i = 1; i < graphicObjectList.size(); i++) {
			PlotGraphicObject temp = graphicObjectList.get(i);
			if(startX > temp.suitableStartX()) {
				startX = temp.suitableStartX();
			}
			if(endX < temp.suitableEndX()) {
				endX = temp.suitableEndX();
			}
			if(startY > temp.suitableStartY()) {
				startY = temp.suitableStartY();
			}
			if(endY < temp.suitableEndY()) {
				endY = temp.suitableEndY();
			}
		}
		this.axis(startX, endX, startY, endY);
	}
	
	// ����������ķ�Χ
	private void axis(double startX, double endX, double startY, double endY) {
		axis_x = startX;
		axis_y = startY;
		axis_h = endY - startY;
		axis_w = endX - startX;
	}
	
	
	/**
	 * �������źͷŴ�
	 * @param x
	 * 					�ο�������
	 * @param y
	 * 					�ο�������
	 * @param scaleX
	 * 					��������1Ϊ�����ţ�>1�Ŵ�<1��С
	 * @param scaleY
	 * 					��������1Ϊ�����ţ�>1�Ŵ�<1��С
	 */
	public void zoom(double x, double y, double scaleX, double scaleY) {
		double newW = this.axis_w / scaleX;
		double newH = this.axis_h / scaleY;
		y = this.height - y;
		/*
		 * newW:�µ�������Ŀ�
		 * newH:�µ�������ĸ�
		 * tempScaleX:���λ�õ�x��������λ�õı�ֵ���������ң�
		 * tempScaleY:���λ�õ�y��������λ�õı�ֵ���������ϣ�
		 */
		double tempScaleX = x / this.width;
		double tempScaleY = y / this.height;
		
		this.axis_x -= tempScaleX * (newW - this.axis_w);
		this.axis_y -= tempScaleY * (newH - this.axis_h);
		this.axis_h = newH;
		this.axis_w = newW;
		this.repaint();
	}
	
	// �ο���Ϊ���ĵ㡣scale���ȷŴ���
	public void zoom(double scale) {
		this.zoom(scale, scale);
	}
	
	// �ο���Ϊ���ĵ㡣scaleX-������Ŵ�����scaleY-������Ŵ���
	public void zoom(double scaleX, double scaleY) {
		this.zoom(this.width / 2.0, this.height / 2.0, scaleX, scaleY);
	}
	
	// xΪ�ο������꣬yΪ�ο������꣬scaleΪ���ȷŴ�����
	public void zoom(int x, int y, double scale) {
		this.zoom(x, y, scale, scale);
	}
	
	// ��λ���ַŴ�
	public void zoomIn(int x, int y) {
		this.zoom(x, y, 2.0);
	}
	// ��λ��������
	public void zoomOut(int x, int y) {
		this.zoom(x, y, 0.5);
	}
	
	/**
	 * �ƶ���ʾ��Χ��������ı�ʾ��Χ��
	 * @param deltX				
	 * 					�������ƶ��ľ��룬>0��ʾ�����ƶ���<0��ʾ�����ƶ�
	 * @param deltY
	 * 					�������ƶ��ľ��룬>0��ʾ�����ƶ���<0��ʾ�����ƶ�
	 */
	public void moveAxis(int deltX, int deltY) {
		this.axis_x -= this.inDisMappingX(deltX);
		this.axis_y += this.inDisMappingY(deltY);
		this.repaint();
	}
	
	

	/**
	 * 
	 * @param x
	 * 				��ǰλ�õĺ�����
	 * @param y
	 * 				��ǰλ�õ�������
	 */
	public void setCurrentPos(int x, int y) {
		this.currentX = inMappingX(x);
		this.currentY = inMappingY(y);
	}
	
	// �Ƿ���ʾ��ǰ������
	public void showCurrentPos(boolean b) {
		this.showCoordinate = b;
	}
	
	public void debug() {
		System.out.println(axis_x + ":" + axis_y + ":" + axis_h + ":" + axis_w);
	}
}
