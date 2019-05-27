package com.imooc.utils;
/*
 *生成唯一的主键
 * 时间+随机数
 */

import java.util.Random;

public class KeyUtil {

    public static String getUniqueKey(){
        Random random = new Random();

        Integer number = random.nextInt(900000) +100000;

        return System.currentTimeMillis()+String.valueOf(number);
    }

    public static Integer getUniqueType(){
        Random random = new Random();
        Integer number = random.nextInt(900000) +100000;
        return number;
    }
}
