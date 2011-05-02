package memagents.environment;

import java.util.ArrayList;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class Matrix<T> 
{
	
	protected int N;
	protected int M;
	protected ArrayList<T> items;
	
	public Matrix(int N, int M, T defaultValue)
	{
		this.N = N;
		this.M = M;
		this.items = new ArrayList<T>(M*N);
		
		for (int i = 0; i < N*M; i++) { this.items.add(defaultValue); }
	}

	public Matrix(int N, int M)
	{
		this.N = N;
		this.M = M;
		this.items = new ArrayList<T>(M*N);
		
		for (int i = 0; i < N*M; i++) { this.items.add(null); }
	}
	
	public void set(int i, int j, T value)
	{
		this.items.set(i*M + j, value);
	}
	
	public T get(int i, int j)
	{		
		return this.items.get(i*M + j);
	}
	
}
