package com.ljheee.loader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * CustomClassLoader
 * @author ljheee
 *
 */
public class MyClassLoader extends ClassLoader{
	private String byteCode_Path;

	public MyClassLoader(String byteCode_Path) {
		this.byteCode_Path = byteCode_Path;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte value[] = null;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(byteCode_Path+name+".class"));
			value = new byte[in.available()];
			in.read(value);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{//释放资源
			try {
				if(null != in)  in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//将byte数组转化为一个类的Class对象实例
		return defineClass(value, 0, value.length);
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		MyClassLoader loader = new MyClassLoader("E:\\GitCode\\MyClassLoader\\bin\\com\\ljheee\\loader\\");
		System.out.println("当前类的父类加载器"+loader.getParent().getClass().getName());
		System.out.println("加载目标类的类加载器"+loader.loadClass("Test").getClassLoader().getClass().getName());
	}
	
}
