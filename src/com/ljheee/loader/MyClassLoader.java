package com.ljheee.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ljheee.security.Use3DES;

/**
 * 自定义类加载器
 * 对加密后的字节码进行解密
 * @author ljheee
 *
 */
public class MyClassLoader extends ClassLoader {

	/**
	 * 原 字节码路径
	 */
	private String byteCode_Path;
	
	/**
	 * 密钥
	 */
	private byte[] key;
	
	public MyClassLoader(String byteCode_Path, byte[] key) {
		this.byteCode_Path = byteCode_Path;
		this.key = key;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] value = null;
		BufferedInputStream in = null;
		
		try {
			in = new BufferedInputStream(new FileInputStream(byteCode_Path+name+".class"));
			
			value = new byte[in.available()];
			in.read(value);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != in) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//将加密后的字节码--解密
		value = Use3DES.decrypt(key, value);
		
		return defineClass(value, 0, value.length);//将byte数组转化为一个类的Class对象实例
	}
	
	public static void main(String[] args) {
		BufferedInputStream in = null;
		try {
			//把原  字节码文件读到src字节数组。注意：此字节码文件是新建测试类Test后编译后的，一般在工程bin目录下
			in = new BufferedInputStream(new FileInputStream("E:\\GitCode\\MyClassLoader\\bin\\com\\ljheee\\loader\\Test.class"));
			byte[] src = new byte[in.available()];
			in.read(src);
			in.close();
			
			byte[] key = "01234567899876543210abcd".getBytes();//密钥24位
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("E:/GitCode/Test.class"));
			
			//将字节码  加密后，写到"E:\\GitCode"
			out.write(Use3DES.encrypt(key, src));
			out.close();
			
			//创建自定义类加载器，加载目标字节码
			MyClassLoader loader = new MyClassLoader("E:/GitCode/", key);
			System.out.println(loader.loadClass("Test").getClassLoader().getClass().getName());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
