package pers.xyx.gui.plot.model;

import java.awt.Color;

public class PlotGraphicObject implements PlotCopy{
	
	/*
	 * ��ȡĬ�ϵ����֣�ͼ�� + ��� 
	 */
	private static final String DEFAULT_NAME = "ͼ��";
	private static int count = 0;
	private static String getDefaultName() {
		return DEFAULT_NAME + count++;
	}
	/*
	 * ��ȡĬ�ϵ���ɫ�����ݵ�ǰ��ɫ��Ż�ȡ��ɫ
	 */
	private static int colorCount = 0;
	private static Color[] colors = new Color[]{
			Color.BLACK, Color.BLUE, Color.DARK_GRAY, Color.GRAY
			};
	private static Color getDefaultColor() {
		return colors[(colorCount++) % colors.length];
	}

	
	private PlotGraphicData graphicData;						//ͼ�ζ��������		
	private PlotGraphicAttribute graphicAttribute;				//ͼ�ζ��������
	
	/*
	 * ���췽��
	 * @param xData	
	 * 					x�������
	 * @param yData
	 * 					y�������
	 * @param name
	 * 					ͼ�ε�����
	 * @param color
	 * 					ͼ�ε���ɫ
	 * @param style
	 * 					ͼ�ε���ʽ
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
	 * ��¡��һ����ͼ�ζ���
	 */
	@Override
	public PlotGraphicObject copy() {
		return new PlotGraphicObject(this.graphicAttribute.copy(), this.graphicData.copy());
	}
	
	/*
	 *  ��ȡ����ļ�ֵ
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
	 * ��ȡ����ķ�Χ
	 */
	public double getWidth() {
		return this.graphicData.width;
	}
	public double getHeight() {
		return this.graphicData.height;
	}
	
	/*
	 * ��ȡ��������
	 */
	public double[] getXData() {
		return this.graphicData.xData;
	}
	public double[] getYData() {
		return this.graphicData.yData;
	}
	
	/*
	 * ��ȡ������Ϣ
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
	 * ��ȡ�����Ұλ��
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
