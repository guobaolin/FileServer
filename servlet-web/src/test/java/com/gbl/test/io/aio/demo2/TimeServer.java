package com.gbl.test.io.aio.demo2;

import java.io.IOException;

/**
 * Created by guobaolin on 2018/12/17.
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        //首先创建异步的时间服务器处理类，然后启动线程将AsyncTimeServerHandler启动
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
