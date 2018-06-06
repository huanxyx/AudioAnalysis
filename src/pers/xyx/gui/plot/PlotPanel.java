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
 * 	作为绘画的画板
 * 	原理：
 * 		每次绘画会根据当前的显示范围进行绘画，所以只需要
 * 		改变坐标轴的范围，也就是当前屏幕显示的范围，就行进行不同的操作。
 * 		
 * 		可以通过图形对象获取最好视野的坐标范围。
 * 		
 * </pre>
 * @author Administrator
 *
 */
public class PlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// 保存当前画板中所有的图形
	protected List<PlotGraphicObject> graphicObjectList = 
			new ArrayList<PlotGraphicObject>();
	
	// 记录坐标轴在画板中开始的位置以及长度
	private double axis_x = 0;
	private double axis_y = 0;
	private double axis_w = 15;
	private double axis_h = 15;
	
	// 当前画板的长宽
	private int width;
	private int height;
	
	// 是否显示当前的坐标
	private boolean showCoordinate = false;
	// 当前鼠标的坐标
	private double currentX;			
	private double currentY;
	
	// 不同数据的panel显示
	private PlotStatementPanel statementPanel;
	
	public PlotPanel() {
		// 绑定鼠标，键盘，监听事件
		PlotPanelListener listener = new PlotPanelListener(this);
		this.addMouseWheelListener(listener);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		// 设置背景色
		this.setBackground(Color.WHITE);
		
		// 添加声明panel
		statementPanel = new PlotStatementPanel(this);
		this.add(statementPanel);
		this.setLayout(null);
	} 
	
	/**
	 * 添加新的图形
	 * @param obj
	 */
	public void plot(PlotGraphicObject obj) {
		this.graphicObjectList.add(obj);
		this.suit();
		this.repaint();
	}
	
	/**
	 * @Description 清空绘图
	 */
	public void clear() {
		graphicObjectList.clear();
	}
	
	/*
	 * 覆盖paintComponent方法来处理绘画
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 更新StatementPanel的位置和大小
		statementPanel.updatePosition();
		
		this.width = this.getWidth();
		this.height = this.getHeight();
		// 1.绘制坐标轴
		this.paintAxis((Graphics2D) g);
		// 2.绘制所有的图形
		for(int i = 0; i < graphicObjectList.size(); i++) {
			this.paintGraphicObject((Graphics2D) g, graphicObjectList.get(i));
		}
		// 3.绘制当前鼠标的坐标
		this.drawCoordinateString(g);
	}
	
	// 绘制图形对象
	private void paintGraphicObject(Graphics2D g, PlotGraphicObject obj) {
		double[] xData = obj.getXData();
		double[] yData = obj.getYData();
		
		// 1.设置颜色
		g.setColor(obj.getColor());
		
		// 2.开始绘制
		for(int i = 0; i < xData.length - 1; i++) {
			// 3.1判断该点是否位于显示范围的左边
			if(xData[i] < axis_x) {
				if(xData[i + 1] < axis_x) {
					continue;
				}
			}
			// 3.2判断该点是否位于显示范围的右边
			if(xData[i] > (axis_x + axis_w)) {
				break;
			}
			// 3.3绘制
			this.drawLine(g, xData[i], yData[i], xData[i+1], yData[i+1]);
		}
	}

	// 绘制坐标轴
	private void paintAxis(Graphics2D g) {
		g.setColor(Color.red);
		// 绘制x轴
		this.drawLine(g, this.axis_x, 0, this.axis_x + this.axis_w, 0);
		// 绘制y轴
		this.drawLine(g, 0, this.axis_y, 0, this.axis_y + this.axis_h);
	}
	
	// 绘制坐标文本
	private void drawCoordinateString(Graphics g) {
		if(this.showCoordinate) {
			g.setColor(Color.DARK_GRAY);
			
			String x = String.format("%.4f", currentX);
			String y = String.format("%.4f", currentY);
			g.drawString("(" + x + "," + y  + ")", 10, 20);
		}
	}
	
	
	// 绘制线条
	private void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
		g.drawLine(mappingX(x1), mappingY(y1), mappingX(x2), mappingY(y2));
	}
	
	// 坐标系中的位置对应画板中的位置
	private int mappingX(double x) {
		return this.disMappingX(x - this.axis_x);
	}
	private int mappingY(double y) {
		return this.height - this.disMappingY(y - this.axis_y);
	}
	
	// 坐标系中的距离对应画板中的距离
	private int disMappingX(double disX) {
		return (int) (disX * (this.width / this.axis_w));
	}
	private int disMappingY(double disY) {
		return (int) (disY * (this.height / this.axis_h));
	}
	
	// 画板上的位置映射到坐标系的坐标
	private double inMappingX(int x) {
		return this.axis_x + inDisMappingX(x);
	}
	private double inMappingY(int y) {
		return this.axis_y + inDisMappingY(this.height - y);
	}
	
	// 画板上的距离映射到坐标系上的距离
	private double inDisMappingX(int disX) {
		return disX * (this.axis_w / this.width);
	}
	private double inDisMappingY(int disY) {
		return disY * (this.axis_h / this.height);
	}
	
	
	// 计算最佳视野下的坐标轴的范围
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
	
	// 设置坐标轴的范围
	private void axis(double startX, double endX, double startY, double endY) {
		axis_x = startX;
		axis_y = startY;
		axis_h = endY - startY;
		axis_w = endX - startX;
	}
	
	
	/**
	 * 进行缩放和放大
	 * @param x
	 * 					参考横坐标
	 * @param y
	 * 					参考纵坐标
	 * @param scaleX
	 * 					横向倍数，1为不缩放，>1放大，<1缩小
	 * @param scaleY
	 * 					纵向倍数，1为不缩放，>1放大，<1缩小
	 */
	public void zoom(double x, double y, double scaleX, double scaleY) {
		double newW = this.axis_w / scaleX;
		double newH = this.axis_h / scaleY;
		y = this.height - y;
		/*
		 * newW:新的坐标轴的宽
		 * newH:新的坐标轴的高
		 * tempScaleX:点击位置的x坐标在总位置的比值（从左往右）
		 * tempScaleY:点击位置的y坐标在总位置的比值（从下往上）
		 */
		double tempScaleX = x / this.width;
		double tempScaleY = y / this.height;
		
		this.axis_x -= tempScaleX * (newW - this.axis_w);
		this.axis_y -= tempScaleY * (newH - this.axis_h);
		this.axis_h = newH;
		this.axis_w = newW;
		this.repaint();
	}
	
	// 参考点为中心点。scale均匀放大倍数
	public void zoom(double scale) {
		this.zoom(scale, scale);
	}
	
	// 参考点为中心点。scaleX-横坐标放大倍数，scaleY-纵坐标放大倍数
	public void zoom(double scaleX, double scaleY) {
		this.zoom(this.width / 2.0, this.height / 2.0, scaleX, scaleY);
	}
	
	// x为参考横坐标，y为参考纵坐标，scale为均匀放大倍数。
	public void zoom(int x, int y, double scale) {
		this.zoom(x, y, scale, scale);
	}
	
	// 单位滚轮放大
	public void zoomIn(int x, int y) {
		this.zoom(x, y, 2.0);
	}
	// 单位滚轮缩放
	public void zoomOut(int x, int y) {
		this.zoom(x, y, 0.5);
	}
	
	/**
	 * 移动显示范围（坐标轴的表示范围）
	 * @param deltX				
	 * 					横坐标移动的距离，>0表示向左移动，<0表示向右移动
	 * @param deltY
	 * 					纵坐标移动的距离，>0表示向上移动，<0表示向下移动
	 */
	public void moveAxis(int deltX, int deltY) {
		this.axis_x -= this.inDisMappingX(deltX);
		this.axis_y += this.inDisMappingY(deltY);
		this.repaint();
	}
	
	

	/**
	 * 
	 * @param x
	 * 				当前位置的横坐标
	 * @param y
	 * 				当前位置的纵坐标
	 */
	public void setCurrentPos(int x, int y) {
		this.currentX = inMappingX(x);
		this.currentY = inMappingY(y);
	}
	
	// 是否显示当前的坐标
	public void showCurrentPos(boolean b) {
		this.showCoordinate = b;
	}
	
	public void debug() {
		System.out.println(axis_x + ":" + axis_y + ":" + axis_h + ":" + axis_w);
	}
}
