package comzzz;

public class Test11 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str="5";
		byte b[]=str.getBytes();
		
		printHexString(b);
		
		int n=5;
		byte t=(byte) n;
		byte bb[]=new byte[1];
		bb[0]=t;
		printHexString(bb);
		
		char ch='5';
		int ch1=ch;
		  String hex = Integer.toHexString(ch1 & 0xFF);
          if (hex.length() == 1)
          {
              hex = '0' + hex;
          }
          System.out.print(hex.toUpperCase() + " ");
		
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
