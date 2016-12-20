
public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a=123;
		byte c[]={0x00,0x00,0x00,0x31};	
		
		int n=byte2int(c);
		
		System.out.println(n);
		
		
		
		
		
		
	}
	
	
	public static int byte2int(byte b[]) {
        return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
                | (b[0] & 0xff) << 24;
    }
	
	//调式打印出字节流
	 public static void printHexString( byte[] b)
	    {
	        //System.out.print(hint);
	        for (int i = 0; i < b.length; i++)
	        {
	            String hex = Integer.toHexString(b[i] & 0xFF);
	            if (hex.length() == 1)
	            {
	                hex = '0' + hex;
	            }
	            System.out.print(hex.toUpperCase() + " ");
	        }
	        System.out.println("");
	  }	

}
