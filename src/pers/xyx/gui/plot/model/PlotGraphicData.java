package pers.xyx.gui.plot.model;

/**
 * 图形数据
 * @author Administrator
 *
 */
public class PlotGraphicData implements PlotCopy{
	protected double[] xData;			//横坐标
	protected double[] yData;			//纵坐标
	
	//坐标极值
	protected double maxX;
	protected double minX;
	protected double maxY;
	protected double minY;
	
	//范围
	protected double width;
	protected double height;
	
	/**
	 * 根据横坐标和纵坐标创建PlotGraphicData对象
	 * @param x		
	 * 				纵坐标的数组
	 * @param y		
	 * 				横坐标的数组
	 */
	public PlotGraphicData(double[] xData, double[] yData) {
		this.xData = xData.clone();
		this.yData = yData.clone();
		calculateLimitValue();
		calculateRange();
	}
	
	/*
	 * 计算坐标的极值
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
	 * 计算坐标值的范围
	 */
	private void calculateRange() {
		width = maxX - minX;
		height = maxY - minY;
	}
	
	/**
	 * 实现了数据的克隆，深复制
	 */
	@Override
	public PlotGraphicData copy(){
		 return new PlotGraphicData(this.xData, this.yData);
	}
}
