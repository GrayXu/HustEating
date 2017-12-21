email 将作为用户的唯一标示，

用户的信息暂时只更新 口味、价格、吃的历史记录

emailAsk：客户端询问服务器该邮箱是否有注册历史

email***Reply：服务器查询数据库后进行回复

addUserAsk：客户端检测验证码无误后，向服务器发起注册请求

addUser***Reply：服务器返回注册结果

addUserHistory：客户端发起在服务器上更新吃饭历史记录的请求**（本项的Reply Json规范由后端制定）**

食堂菜肴数据见：https://github.com/GrayXu/HustEating/blob/master/Android/app/src/main/res/raw/foods.csv 规范见其父文件夹readme

用户在服务器上存储的数据有 昵称、邮箱、密码、学院、省份、口味喜好、价格喜好、至少近三天的历史记录
