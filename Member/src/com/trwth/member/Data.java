package com.trwth.member;

import java.io.Serializable;

public class Data implements Serializable{
	String name;
	String age;
	String gender;
	String path;
	
	public Data(String a,String b,String c,String e){
		name=a;
		age=b;
		gender=c;
		path = e;
	}
	public Data() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name=name;		
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age){
		this.age=age;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender){
		this.gender=gender;		
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path=path;
	}
	
}
