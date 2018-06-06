# 音频分析
- 提供录音进行音频分析
- 提供读取音频文件进行音频分析
## AudioAPI
- AudioManager：音频管理器
  - static getInstance()：获取AudioManager对象
  - getCurrentAudio()：获取当前加载的音频对象
  - getAudioName()：获取当前加载音频的名称（未保存的录音则表示为"录音"，其他情况则为文件名）
  - startRecord()：打开一个线程录音
  - finishRecord()：结束录音，将会替换当前加载的音频对象
  - save(String)：保存当前音频到指定文件
  - openNewAudioFile(String path)：打开音频文件
  - playAudio()：播放当前加载的音频
  - pauseAudio()：暂停当前的音频
  - setNowTime()：设置当前播放的时间
  - addPlayingListener()：添加音频播放监听器
  - deletePlayingListener()：删除音频播放监听器

- AudioObject：音频对象
  - getTotalTime()：获取播放总时长
  - getFrameLength()：获取总帧数
  - getChannels()：获取声道数
  - getData()：获取音频数据（二维数组，假如是单声道则为行数为1，若是双声道则为2）

- PlayingListener：音频进度监听器
  - updateAudio(AudioObject)：可以获取当前AudioObject的播放位置
- AudioPlayingThread：用户异步获取当前播放位置的线程，并将信息发送给监听器
- AudioRecordThread：录音线程，用于提供给AudioManger使用
- PlotPanel：数据可视化的Panel面板
  - plot()：进行绘画
- AudioConfiguration：录音相关配置参数
  - getFormat：获取默认的AudioFormat

## PlotAPI

- model ：图形对象模型类
  - PlotCopy：拷贝接口
  - PlotGraphicAttribute：图形的属性对象
  - PlotGraphicData：图形的参数数据
  - PlotGraphicObject：图形对象
    - 对外提供的接口
- utils：对调用者提供的工具
  - PlotUtils
    - translateToDoubleArray：将int数组转换为double数组
    - getDefaultXData：获取默认的XData数据
- PlotPanel：作为绘画的画板
  - 原理：
    - 每次绘画会根据当前的显示范围进行绘画，所以只需要改变坐标轴的范围，也就是当前屏幕显示的范围，就行进行不同的操作。可以通过图形对象获取最好视野的坐标范围。
  - plot：添加新的图形
  - clear：清空所有图形
  - zoom：进行放大和缩小
  - zoomIn：移 进，也就是放大
  - zoomOut：移出，也就是缩小
  - moveAxis：修改显示轴
  - setCurrentPos：设置当前的坐标
  - showCurrentPos：显示当前位置
- PlotStatementPanel：用于显示不同数据的Panel