package com.codehuan.utils;

/**
 * @Author ZhangHuan created on 2022/2/25 23:53
 * @Email codehuan@163.com
 * @Description 睡眠工具类
 * @Version 1.0
 */
public class SleepUtil {

    public static void sleep(int second) {

        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
