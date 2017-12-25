package com.grayxu.husteating.background;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2017/12/22.
 */

public class Client {

    private String IP;
    private int port;
    private Socket sockClient;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String sendJson;
    private StringBuilder getJsonSb;
    private Handler handler;

    public Client(String IP, int port, String sendJson, Handler handler) {
        this.IP = IP;
        this.port = port;
        this.sendJson = sendJson;
        this.handler = handler;
    }

    public void exchangeMessage() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("开始交换数据", "!");
                    sockClient = new Socket(IP, port);//新建一个Socket

                    writer = new BufferedWriter(
                            new OutputStreamWriter(
                                    sockClient.getOutputStream(), "UTF-8"));
                    reader = new BufferedReader(
                            new InputStreamReader(
                                    sockClient.getInputStream(), "UTF-8"));

                    if (writer == null) {
                        Log.e("exchangeMessage", "writer为null");
                    } else if (reader == null) {
                        Log.e("exchangeMessage", "reader为null");
                    } else {
                        Log.d("exchangeMessage","读写器一切正常");

                        String line;
                        getJsonSb = new StringBuilder("");
                        while ((line = reader.readLine()) != null) {
                            getJsonSb.append(line);
                        }

                        writer.close();
                        reader.close();

                        writer = null;
                        reader = null;
                        //done
                        Message message = new Message();
                        message.what = 1;
                        message.arg2 = 3;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void sendMessage(){
        if (writer != null){
            Log.d("sendMessage","writer不是null");
            try {
                writer.write(sendJson);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.d("sendMessage","writer是null");
        }
    }
    /**
     * 合适的时候才能调用
     *
     * @return
     */
    public boolean getBoolReply() {
        String getJsonString = getJsonSb.toString();
        JsonUtil jsonUtil = new JsonUtil();
        return jsonUtil.parseEmailReply(getJsonString);
    }

}
