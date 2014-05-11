package com.example.hw252bgrecco;

public class TaskObject {
    //String mId;
	int mId;
    String mName;
 
    public TaskObject(int id, String name){
    	mId = id;
    	mName = name;
    }
    
    public TaskObject(String name){
    	mName = name;
    }
    
    public TaskObject(){
    	
    }
    
    public void setID(int id){
    	mId = id;
    }
    
    public void setName(String name){
    	mName = name;
    }
    
    public int getID(){
    	return mId;
    }
    
    public String getName(){
    	return mName;
    }
}
