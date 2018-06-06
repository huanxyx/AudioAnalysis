package pers.xyx.gui.plot.model;

import java.awt.Color;

/**
 * 图形的属性对象
 * @author Administrator
 *
 */
public class PlotGraphicAttribute implements PlotCopy{

	/*
	 * 属性信息
	 */
	protected String graphicName;					//名字
	protected Color graphicColor;					//颜色
	protected PlotLineStyle graphicSylte;			//样式

	/**
	 * 构造方法
	 */
	public PlotGraphicAttribute(String name, Color color, PlotLineStyle style) {
		this.graphicName = name;
		this.graphicColor = color;
		this.graphicSylte = style;
	}
	
	/**
	 * 克隆出一样的属性对象
	 */
	@Override
	public PlotGraphicAttribute copy(){
		return new PlotGraphicAttribute(this.graphicName, this.graphicColor, this.graphicSylte);
	}
	
	/**
	 * 线的样式
	 * @author Administrator
	 *
	 */
	public enum PlotLineStyle{
		SOLID,				//虚线
		DASHED;				//实线
	}
}
