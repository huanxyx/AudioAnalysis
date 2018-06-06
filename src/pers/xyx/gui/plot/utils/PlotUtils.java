package pers.xyx.gui.plot.utils;

/**
 * @ClassName: PlotUtils
 * @author: Huan
 * @date: 2018年3月23日 上午12:14:15
 * @Description 提供给用户使用的工具类
 */
public class PlotUtils {
	
	/**
	 * @Description 用于将其他类型的数组转换为double类型的数组
	 * @param array
	 * @return 
	 */
	public static double[] translateToDoubleArray(int[] array) {
		double[] temp = new double[array.length];
		for(int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		}
		return temp;
	}
	
	/**
	 * @Description 获取默认的XData数据，从1到length
	 * @return
	 */
	public static double[] getDefaultXData(int length) {
		double[] xData = new double[length];
		for (int i = 0; i < length; i++)  {
			xData[i] = i+1;
		}
		return xData;
	}
}
