# ��Ƶ����
- �ṩ¼��������Ƶ����
- �ṩ��ȡ��Ƶ�ļ�������Ƶ����
## AudioAPI
- AudioManager����Ƶ������
  - static getInstance()����ȡAudioManager����
  - getCurrentAudio()����ȡ��ǰ���ص���Ƶ����
  - getAudioName()����ȡ��ǰ������Ƶ�����ƣ�δ�����¼�����ʾΪ"¼��"�����������Ϊ�ļ�����
  - startRecord()����һ���߳�¼��
  - finishRecord()������¼���������滻��ǰ���ص���Ƶ����
  - save(String)�����浱ǰ��Ƶ��ָ���ļ�
  - openNewAudioFile(String path)������Ƶ�ļ�
  - playAudio()�����ŵ�ǰ���ص���Ƶ
  - pauseAudio()����ͣ��ǰ����Ƶ
  - setNowTime()�����õ�ǰ���ŵ�ʱ��
  - addPlayingListener()�������Ƶ���ż�����
  - deletePlayingListener()��ɾ����Ƶ���ż�����

- AudioObject����Ƶ����
  - getTotalTime()����ȡ������ʱ��
  - getFrameLength()����ȡ��֡��
  - getChannels()����ȡ������
  - getData()����ȡ��Ƶ���ݣ���ά���飬�����ǵ�������Ϊ����Ϊ1������˫������Ϊ2��

- PlayingListener����Ƶ���ȼ�����
  - updateAudio(AudioObject)�����Ի�ȡ��ǰAudioObject�Ĳ���λ��
- AudioPlayingThread���û��첽��ȡ��ǰ����λ�õ��̣߳�������Ϣ���͸�������
- AudioRecordThread��¼���̣߳������ṩ��AudioMangerʹ��
- PlotPanel�����ݿ��ӻ���Panel���
  - plot()�����л滭
- AudioConfiguration��¼��������ò���
  - getFormat����ȡĬ�ϵ�AudioFormat

## PlotAPI

- model ��ͼ�ζ���ģ����
  - PlotCopy�������ӿ�
  - PlotGraphicAttribute��ͼ�ε����Զ���
  - PlotGraphicData��ͼ�εĲ�������
  - PlotGraphicObject��ͼ�ζ���
    - �����ṩ�Ľӿ�
- utils���Ե������ṩ�Ĺ���
  - PlotUtils
    - translateToDoubleArray����int����ת��Ϊdouble����
    - getDefaultXData����ȡĬ�ϵ�XData����
- PlotPanel����Ϊ�滭�Ļ���
  - ԭ��
    - ÿ�λ滭����ݵ�ǰ����ʾ��Χ���л滭������ֻ��Ҫ�ı�������ķ�Χ��Ҳ���ǵ�ǰ��Ļ��ʾ�ķ�Χ�����н��в�ͬ�Ĳ���������ͨ��ͼ�ζ����ȡ�����Ұ�����귶Χ��
  - plot������µ�ͼ��
  - clear���������ͼ��
  - zoom�����зŴ����С
  - zoomIn���� ����Ҳ���ǷŴ�
  - zoomOut���Ƴ���Ҳ������С
  - moveAxis���޸���ʾ��
  - setCurrentPos�����õ�ǰ������
  - showCurrentPos����ʾ��ǰλ��
- PlotStatementPanel��������ʾ��ͬ���ݵ�Panel