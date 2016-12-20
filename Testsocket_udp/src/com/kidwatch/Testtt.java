package com.kidwatch;

public class Testtt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Header header=new Header(1, 10);
		
		Login_req login_req = new Login_req(header, 50);
		
		System.out.println(login_req.head.len);
	}

}

class Header{
	int version;
	int len;
	
	public Header(int version, int len){
		this.version=version;
		this.len=len;
	}
	
}

class Login_req{
	Header head;
	
	int data;
	
	public Login_req(Header head,int data){
		this.head=head;
		
		this.data=data;
	}
	
}
