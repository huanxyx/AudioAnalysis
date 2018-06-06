package pers.xyx.gui.plot.utils;

/**
 * @ClassName: PlotUtils
 * @author: Huan
 * @date: 2018��3��23�� ����12:14:15
 * @Description �ṩ���û�ʹ�õĹ�����
 */
public class PlotUtils {
	
	/**
	 * @Description ���ڽ��������͵�����ת��Ϊdouble���͵�����
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
	 * @Description ��ȡĬ�ϵ�XData���ݣ���1��length
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
