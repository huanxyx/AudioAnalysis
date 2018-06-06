package pers.xyx.gui.plot.model;

/**
 * ͼ������
 * @author Administrator
 *
 */
public class PlotGraphicData implements PlotCopy{
	protected double[] xData;			//������
	protected double[] yData;			//������
	
	//���꼫ֵ
	protected double maxX;
	protected double minX;
	protected double maxY;
	protected double minY;
	
	//��Χ
	protected double width;
	protected double height;
	
	/**
	 * ���ݺ�����������괴��PlotGraphicData����
	 * @param x		
	 * 				�����������
	 * @param y		
	 * 				�����������
	 */
	public PlotGraphicData(double[] xData, double[] yData) {
		this.xData = xData.clone();
		this.yData = yData.clone();
		calculateLimitValue();
		calculateRange();
	}
	
	/*
	 * ��������ļ�ֵ
	 */
	private void calculateLimitValue() {
		this.maxX = this.minX = this.xData[0];
		this.maxY = this.minY = this.yData[0];
		
		int len = this.xData.length;
		for(int i = 0; i < len; i++) {
			this.minX = this.minX > this.xData[i] ? this.xData[i] : this.minX;
			this.maxX = this.maxX < this.xData[i] ? this.xData[i] : this.maxX;
			this.minY = this.minY > this.yData[i] ? this.yData[i] : this.minY;
			this.maxY = this.maxY < this.yData[i] ? this.yData[i] : this.maxY;
		}
	}
	/*
	 * ��������ֵ�ķ�Χ
	 */
	private void calculateRange() {
		width = maxX - minX;
		height = maxY - minY;
	}
	
	/**
	 * ʵ�������ݵĿ�¡�����
	 */
	@Override
	public PlotGraphicData copy(){
		 return new PlotGraphicData(this.xData, this.yData);
	}
}
