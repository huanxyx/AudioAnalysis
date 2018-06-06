package pers.xyx.gui.plot.model;

import java.awt.Color;

public class PlotGraphicObject implements PlotCopy{
	
	/*
	 * 获取默认的名字：图形 + 序号 
	 */
	private static final String DEFAULT_NAME = "图形";
	private static int count = 0;
	private static String getDefaultName() {
		return DEFAULT_NAME + count++;
	}
	/*
	 * 获取默认的颜色：根据当前颜色编号获取颜色
	 */
	private static int colorCount = 0;
	private static Color[] colors = new Color[]{
			Color.BLACK, Color.BLUE, Color.DARK_GRAY, Color.GRAY
			};
	private static Color getDefaultColor() {
		return colors[(colorCount++) % colors.length];
	}

	
	private PlotGraphicData graphicData;						//图形对象的数据		
	private PlotGraphicAttribute graphicAttribute;				//图形对象的属性
	
	/*
	 * 构造方法
	 * @param xData	
	 * 					x轴的坐标
	 * @param yData
	 * 					y轴的坐标
	 * @param name
	 * 					图形的名称
	 * @param color
	 * 					图形的颜色
	 * @param style
	 * 					图形的样式
	 */
	public PlotGraphicObject(double[] xData, double[] yData, String name, Color color, PlotGraphicAttribute.PlotLineStyle style) {
		this.graphicData = new PlotGraphicData(xData, yData);
		this.graphicAttribute = new PlotGraphicAttribute(name, color, style);
	}
	public PlotGraphicObject(double[] xData, double[] yData, String name, Color color) {
		this(xData, yData, name, color, PlotGraphicAttribute.PlotLineStyle.SOLID);
	}
	public PlotGraphicObject(double[] xData, double[] yData, String name) {
		this(xData, yData, name, getDefaultColor());
	}
	public PlotGraphicObject(double[] xData, double[] yData) {
		this(xData, yData, getDefaultName());
	}
	public PlotGraphicObject(PlotGraphicAttribute attr, PlotGraphicData data) {
		this.graphicAttribute = attr;
		this.graphicData = data;
	}

	/*
	 * 克隆出一样的图形对象
	 */
	@Override
	public PlotGraphicObject copy() {
		return new PlotGraphicObject(this.graphicAttribute.copy(), this.graphicData.copy());
	}
	
	/*
	 *  获取坐标的极值
	 */
	public double getMaxX() {
		return this.graphicData.maxX;
	} 
	public double getMinX() {
		return this.graphicData.minX;
	}
	public double getMaxY() {
		return this.graphicData.maxY;
	}
	public double getMinY() {
		return this.graphicData.minY;
	}
	
	/*
	 * 获取坐标的范围
	 */
	public double getWidth() {
		return this.graphicData.width;
	}
	public double getHeight() {
		return this.graphicData.height;
	}
	
	/*
	 * 获取坐标数据
	 */
	public double[] getXData() {
		return this.graphicData.xData;
	}
	public double[] getYData() {
		return this.graphicData.yData;
	}
	
	/*
	 * 获取属性信息
	 */
	public String getName() {
		return this.graphicAttribute.graphicName;
	}
	public Color getColor() {
		return this.graphicAttribute.graphicColor;
	}
	public PlotGraphicAttribute.PlotLineStyle getStyle() {
		return this.graphicAttribute.graphicSylte;
	}
	
	/*
	 * 获取最佳视野位置
	 */
	private static double SUITABLE_FIELD = 0.9;
	public double suitableStartX() {
		return getMinX() - getWidth() * (1 - SUITABLE_FIELD) / 2;
	}
	public double suitableEndX() {
		return getMaxX() + getWidth() * (1 - SUITABLE_FIELD) / 2;
	}
	public double suitableStartY() {
		return getMinY() - getHeight() * (1 - SUITABLE_FIELD) / 2;
	}
	public double suitableEndY() {
		return getMaxY() + getHeight() * (1 - SUITABLE_FIELD) / 2;
	}
}
