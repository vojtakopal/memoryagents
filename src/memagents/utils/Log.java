package memagents.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Log {
	static PrintStream output;
	
	static public void print(String str) {
		if (output == null) 
		{
			output = System.out;
		}
		
		output.print(str);
	}
	
	static public void println() {
		if (output == null) 
		{
			output = System.out;
		}
		
		output.println();
	}
	
	static public void println(String line)
	{
		if (output == null) 
		{
			output = System.out;
		}
		
		output.println(line);
	}
	
	static public void intoFile(String path)
	{
		try {
			File f = new File(path);
			output = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
