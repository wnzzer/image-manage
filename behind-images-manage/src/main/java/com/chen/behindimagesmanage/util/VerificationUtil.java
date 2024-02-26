package com.chen.behindimagesmanage.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 15031
 */
public class VerificationUtil {
    public static boolean isValidFileName(String fileName) {
        // 定义一个正则表达式，匹配键盘上的特殊字符（除小数点）
        String regex = "/[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,<>\\/?~`]+/";

        // 使用正则表达式测试输入字符串，同时排除小数点
        String newInput = fileName.replace(".", "");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newInput);
        for(int i =0, count = 0; i < fileName.length(); i++){
            if('.' == fileName.charAt(i)){
                count++;
                if(count >= 2){
                    return  false;
                }
            }
        }
        return !matcher.find();
    }
    public static boolean isValidFilePath(String path) {
        // 定义一个正则表达式，匹配键盘上的特殊字符（除小数点）
        String regex = "/[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,<>?~`]+/";

        // 使用正则表达式测试输入字符串，同时排除小数点
        String newInput = path.replace(".", "");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newInput);
        for(int i =0, count = 0; i < path.length(); i++){
            if('.' == path.charAt(i)){
                count++;
                if(count >= 2){
                    return  false;
                }
            }
        }
        return !matcher.find();
    }
}
