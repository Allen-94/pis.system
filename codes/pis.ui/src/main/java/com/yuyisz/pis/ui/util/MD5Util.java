package com.yuyisz.pis.ui.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.shiro.crypto.hash.SimpleHash;

/** 
 * 采用MD5加密解密 
 * @author tfq 
 * @datetime 2011-10-13 
 */  
public class MD5Util {  
  
	 /** 
     * 对字符串md5加密(小写+字母) 
     * 
     * @param str 传入要加密的字符串 
     * @return  MD5加密后的字符串 
     */  
    public static String getMD5(String str) {  
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            return new BigInteger(1, md.digest()).toString(16);  
        } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
        }  
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }  
  
    // 测试主函数  
    public static void main(String args[]) {  
        /*String s = new String("root");  
        System.out.println("原始：" + s);
        String ss = convertMD5(s);
        System.out.println("MD5后：" + getMD5(s));  
        System.out.println("加密的：" + ss);  
        System.out.println("解密的：" + convertMD5(convertMD5(s)));  */
    	
    	String hashAlgorithmName = "MD5";
        String credentials = "123456";
        int hashIterations = 1;
        Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
        System.out.println(obj);
  
    }  
}  
