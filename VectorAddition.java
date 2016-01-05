//***********************************************************
//* VectorAddition.java Created By Kian Gorgichuk           *
//* Copyright (c) 2014 Kian Gorgichuk. All rights reserved. *
//***********************************************************

public class VectorAddition
{
	private Vector[] _vectorArray = null;
	private int _index;
	
	public VectorAddition(int i)
	{
		_vectorArray = new Vector[i];
		_index = 0;
	}
	
	public int getIndex()
	{
		return _index;
	}
	public void addVector(double magnitude, double direction, boolean isRadians)
	{
		_vectorArray[_index] = new Vector(magnitude, direction, isRadians);
		_index++;
	}
	
	public Vector calculateAnswer()
	{
		Vector answer = new Vector(0, 0, true);
		int i = 0;
		while(i != _index)
		{
			answer = Vector.addVectors(answer, _vectorArray[i]);
			i++;
		}
		return answer;
	}
}