package com.ThreadPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


public class CountDownLatchDemo2 {
	
	
	final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    public static void main(String[] args) throws InterruptedException {
    	CountDownLatch latch=new CountDownLatch(2);//两个工人的协作
    	Worker worker1=new Worker("zhang san", 500, latch);
    	Worker worker2=new Worker("li si", 800, latch);
    	worker1.start();//
    	worker2.start();//
    	latch.await();//等待所有工人完成工作
        System.out.println("all work done at "+sdf.format(new Date()));
       int times= worker1.times+worker2.times;
       
       System.out.println("总运行次数："+times);
	}
    
    
    static class Worker extends Thread{
    	int times=0;
    	String workerName; 
    	int workTime;
    	CountDownLatch latch;
    	public Worker(String workerName ,int workTime ,CountDownLatch latch){
    		 this.workerName=workerName;
    		 this.workTime=workTime;
    		 this.latch=latch;
    	}
    	public void run(){
    		
    		System.out.println("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
    		while(true){
    			System.out.println(workerName+" is working");
    			doWork();//工作了
    			if(times==10){
    				
    				break;    				
    			}
    			times++;
    		}
    		System.out.println("Worker "+workerName+" do work complete at "+sdf.format(new Date()));
    		latch.countDown();//工人完成工作，计数器减一

    	}
    	
    	private void doWork(){
    		
    		try {
				Thread.sleep(workTime);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
     
}
