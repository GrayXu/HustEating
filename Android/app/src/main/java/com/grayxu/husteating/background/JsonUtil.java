package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/12/17.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 本类负责进行Json数据的生成与解析，用来与服务器进行交换数据
 */
public class JsonUtil {
    /**
     * 调用范例：jsonUtil.getEmailAskJson("285293728@qq.com");
     * <p>
     * jsonUtil.getAddUserAskJson("大熊","123456","2852@qq.com","计算机科学与技术","福建");
     * <p>
     * List<String> nameList = new ArrayList<>(Arrays.asList("20171221-1"));
     * List<String> foodList = new ArrayList<>(Arrays.asList("牛肉拉面","可乐"));
     * List<List<String>> foodListList = new ArrayList<>(Arrays.asList(foodList));
     * try {
     * jsonUtil.getAddUserHistory(email,nameList,foodListList);
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     */

    private static String CharSet = "UTF-8";

    /**
     * 提供给外部获得emailAsk所需的Json文件
     *
     * @param email 邮箱
     * @return Json格式的字符串
     */
    public String getEmailAskJson(String email) {
        EmailAsk emailAsk = new EmailAsk(email);
        Gson gson = new Gson();
        return gson.toJson(emailAsk);
    }

    /**
     * 外部生成添加用户的Json请求
     *
     * @param id       用户的昵称
     * @param password 用户的密码
     * @param email    用户的邮箱
     * @param major    用户的专业
     * @param province 用户的省份
     * @return Json格式的字符串
     */
    public String getAddUserAskJson(String id, String password, String email, String sex, String major, String province) {
        AddUserAsk addUserAsk = new AddUserAsk(id, password, email, sex, major, province);
        return new Gson().toJson(addUserAsk);
    }

    /**
     * 外部发起向服务器数据库更新的Json请求
     *
     * @param email        供识别使用的email
     * @param timeList     多记录的时间list
     * @param foodListList 每个时间对应一个该次所选择的食物list，故构成一个双层list
     * @return Json格式的字符串
     * @throws Exception 两个传入的list长度不同
     */
    public String getAddUserHistory(String email, List<String> timeList, List<List<String>> foodListList) throws Exception {
        AddUserRecords addUserRecords = new AddUserRecords(email, timeList, foodListList);
        return new Gson().toJson(addUserRecords);
    }

    /**
     * 获得布尔类型的ReplyJson
     *
     * @param emailReplyJson
     * @return
     */
    public boolean parseEmailReply(String emailReplyJson) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<BoolReply>() {
        }.getType();
        BoolReply boolReply = gson.fromJson(emailReplyJson, type);
        return boolReply.status;
    }

}

/**
 * 请求查询email是否重复Json的生成类
 */
class EmailAsk {
    private String task;
    private String data;

    public EmailAsk(String data) {
        this.task = "emailAsk";
        this.data = data;
    }
}

/**
 * 添加用户的请求Json生成类
 */
class AddUserAsk {
    String task;
    User data;

    public AddUserAsk(String id, String password, String email, String sex, String major, String province) {
        this.task = "addUserAsk";
        data = new User(id, password, email, sex, major, province);
    }
}

class User {
    String name;
    String password;
    String email;
    String sex;
    String major;
    String province;

    public User(String name, String password, String email, String sex, String major, String province) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.major = major;
        this.province = province;
    }
}

/**
 * 客户端发起在服务器上更新吃饭历史记录的请求
 */
class AddUserRecords {
    String task;
    String email;
    List<UserRecord> userRecordList;

    /**
     * 三层构造一个Json对象
     *
     * @param email        识别用户的KEY
     * @param timeList     多记录的时间list
     * @param foodListList 每个时间对应一个该次所选择的食物list，故构成一个双层list
     * @throws Exception 两个列表的长度不同
     */
    public AddUserRecords(String email, List<String> timeList, List<List<String>> foodListList) throws Exception {
        this.task = "addUserHistory";
        this.email = email;

        if (timeList.size() != foodListList.size()) {
            throw new Exception();//这里抛出的错误防止两个列表的长度不同
        }
        userRecordList = new ArrayList<>();

        for (int i = 0; i < timeList.size(); i++) {//双遍历故不用迭代器
            UserRecord userRecord = new UserRecord(timeList.get(i), foodListList.get(i));
            userRecordList.add(userRecord);
        }

    }
}

class UserRecord {
    String time;
    List<FoodJson> foodList;

    /**
     * 两层构造
     *
     * @param time     本次记录的时间
     * @param nameList 本次记录中所有的食物
     */
    public UserRecord(String time, List<String> nameList) {
        this.time = time;
        foodList = new ArrayList<>();
        Iterator iterator = nameList.iterator();
        while (iterator.hasNext()) {
            foodList.add(new FoodJson((String) iterator.next()));//迭代创建一个FoodJson的List
        }
    }
}

class FoodJson {
    String name;

    public FoodJson(String name) {
        this.name = name;
    }
}

class BoolReply {
    boolean status;
}