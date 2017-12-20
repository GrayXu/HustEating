# 吃在华科——*华科食堂选择工具*

--------

![LOGO](https://github.com/GrayXu/HustEating/raw/master/Android/app/src/main/res/drawable/main.png)

这是一款根据用户喜好推荐食堂菜肴的移动应用。

Feature: 作为一款解决选择困难的工具，应该最大程度上减少用户每次选择的时间损耗。

*本仓库暂时为私密仓库，是否开源后期决定。*
*定期更新LOG与TODO LIST*

暂定名：**吃在华科**



目标功能
-----

1. [ ] 智能推荐食堂菜肴。
2. [ ] 服务器同步用户数据。
3. [ ] 用户评论交流功能。

TODO LIST
-----

1. 收集食堂数据，需要包括 菜肴的如下属性：名称，价格，肉的程度，口味。（急）或者先填充伪数据，先完成一个模板。
2. 完成推荐菜肴的算法设计（需要考虑用户的历史喜好，最近的饮食需求，营养均衡，菜肴的热门程度）。*见附1*

Log
-----
12.20 基本完成登录注册逻辑与界面。bug:邮件发送存在链接不上smtp服务器的问题。

12.17 注册登录逻辑待服务器添加，过场逻辑完成。JSON_Sample建立。

11.13 修BUG，info活动的UI初步搭建，推荐算法可以匹配时间。

11.9 修复申请权限问题，设计出了logo，然后继续完善代码

10.?? 完成界面色调切换功能。*因为为了实现离线也可以保持推荐的功能，故菜肴信息保存在本地，需要时再进行更新。*

10.29   优化多食堂添加方式，修复ToolBar遮蔽的问题。（通过设置其LinearLayout为垂直方向）

10.28   UI部分重构，抽离了设置界面，意图再次简化用户操作，也方便后期加入服务器模块。

10.26   完成本地数据库搭建

10.14   修改地图定位的几个BUG。重新设计为三层活动，完成了第二层用户临时设置的UI界面设置。

10.12   完成了主界面的UI搭建，全屏的地图基于高德SDK。第二层的具体推荐界面的设计需要进一步完善。

-----
E-mail: grayxu@hust.edu.cn

附
-----
#1. 有关推荐算法：
	考虑如下因素： 近期吃饭的历史记录、预算、口味偏好、荤素均衡、主副菜均衡。多因素进行权重分配后，选取前几个进行概率摇选。
	
#2. 前后端交互逻辑：
	前后端通过Socket传输json文件或csv文件进行数据沟通。总的来说，有三种数据传输需求：用户配置信息、食堂菜肴信息、评论信息。用户配置信息包括**用户的历史吃饭记录**以及**口味以及价格区间的偏好**，基本可以通过**json**满足键值对传输需求；食堂菜肴信息可以通过传输一个新的**csv**，让前端去完成更新本地的数据库；评论信息则通过多次的传输json文件来完成 **服务器端数据库的更新** 以及 **客户端从服务器端获得评论信息** 这两个功能。（*完成顺序即123*）。具体的格式见根目录的JSON_Sample文件夹。

#3. 依赖第三方库：
	JavaCSV, EasyPermissions, LitePal, JavaMail, android-gif-drawable, AMap.
