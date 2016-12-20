package com.crc;

public class TestCrc {

	static int POLYNOMIAL=0x04c11db7;
	static byte g_First=0x01;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub {0x04,(byte) 0xc1,0x1d,(byte) 0xb7}

	}
	
	
	
	static int  g_CrcTable[]=new int[256];
	
	
 static void GenCrcTable()
	{
	 	int	i, j;
		
		int crc_accum;

		for( i = 0;  i < 256;  i++ )
		{
			crc_accum =  ( i << 24 );
		
			for( j = 0;  j < 8;  j++ )
			{
				if( (crc_accum & 0x80000000L)!=0 )
					crc_accum = ( crc_accum << 1 ) ^ POLYNOMIAL;
				else
					crc_accum = ( crc_accum << 1 );
			}
			
			g_CrcTable[i] = crc_accum;
		}
	}
	
	
	static int CRC_Calculate(int crc, byte buf, int len)
	{
	    int  crc_table[]=new int[256];
//		crc = crc ^ 0xffffffffL;
		//if(g_First)
		{
			//GenCrcTable();
	        short i, j;
		
	    	int crc_accum;

	    	for( i = 0;  i < 256;  i++ )
	    	{
	    		crc_accum =  ( i << 24 );
	    	
	    		for( j = 0;  j < 8;  j++ )
	    		{
	    			if( (crc_accum & 0x80000000L)!=0 )
	    				crc_accum = ( crc_accum << 1 ) ^ POLYNOMIAL;
	    			else
	    				crc_accum = ( crc_accum << 1 );
	    		}
	    		
	    		crc_table[i] = crc_accum;
	    	}

			g_First = 0x00;
		}
		
		while( len >0 )
		{
			crc = (crc << 8) ^ crc_table[((crc >> 24) ^ buf++) & 0xff];
			len--;
		}
		//return crc ^ 0xffffffffL;
	    return crc;
	}
	
	
	
	
	int kwp_get_crc32( int uiCrc, byte buf, int nLength)
	{
	    int crc=uiCrc;

		if(g_First>0)
		{
			GenCrcTable();

			g_First = 0;
		}
		while( nLength>0 )
		{
			crc = (crc << 8) ^ g_CrcTable[((crc >> 24) ^ buf++) & 0xff];
			nLength--;
		}

		return crc;
	}
	
	
	
	

}



