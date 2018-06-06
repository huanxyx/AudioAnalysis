package pers.xyx.gui.plot.model;

import java.awt.Color;

/**
 * ͼ�ε����Զ���
 * @author Administrator
 *
 */
public class PlotGraphicAttribute implements PlotCopy{

	/*
	 * ������Ϣ
	 */
	protected String graphicName;					//����
	protected Color graphicColor;					//��ɫ
	protected PlotLineStyle graphicSylte;			//��ʽ

	/**
	 * ���췽��
	 */
	public PlotGraphicAttribute(String name, Color color, PlotLineStyle style) {
		this.graphicName = name;
		this.graphicColor = color;
		this.graphicSylte = style;
	}
	
	/**
	 * ��¡��һ�������Զ���
	 */
	@Override
	public PlotGraphicAttribute copy(){
		return new PlotGraphicAttribute(this.graphicName, this.graphicColor, this.graphicSylte);
	}
	
	/**
	 * �ߵ���ʽ
	 * @author Administrator
	 *
	 */
	public enum PlotLineStyle{
		SOLID,				//����
		DASHED;				//ʵ��
	}
}
