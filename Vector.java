//***********************************************************
//* Vector.java Created By Kian Gorgichuk                   *
//* Copyright (c) 2014 Kian Gorgichuk. All rights reserved. *
//***********************************************************

import java.lang.Math;

public class Vector
{
	private double _magnitude = 0; 
	private double _directionRad = 0;
	private double _directionDeg = 0;
	
	//Constructors
	public Vector()
	{
		setMagnitude(0);
		setDirection(0, false);
	}
	public Vector(double magnitude, double direction, boolean isRadian)
	{
		setMagnitude(magnitude);
		setDirection(direction, isRadian);
	}
	
	//Setters
	public void setMagnitude(double magnitude)
	{
		_magnitude = magnitude;
	}
	public void setDirection(double direction, boolean isRadian)
	{
		if(isRadian)
		{
			_directionRad = direction;
			_directionDeg = Math.toDegrees(direction);
		}
		else
		{
			_directionDeg = direction;
			_directionRad = Math.toRadians(direction);
		}
	}
	
	//Getters
	public double getMagnitude()
	{
		return _magnitude;
	}
	public double getDirectionRad()
	{
		return _directionRad;
	}
	public double getDirectionDeg()
	{
		return _directionDeg;
	}
	public double getXComponent()
	{
		if((_directionDeg == 90) || (_directionDeg == 270))
			return 0;
		else if(_directionDeg == 0)
			return _magnitude;
		else if(_directionDeg == 180)
			return _magnitude * -1;
		else
			return _magnitude * Math.cos(_directionRad);
	}
	public double getYComponent()
	{
		if((_directionDeg == 0) || (_directionDeg == 180 ))
			return 0;
		else if(_directionDeg == 90)
			return _magnitude;
		else if(_directionDeg == 270)
			return _magnitude * -1;
		else
			return _magnitude * Math.sin(_directionRad);
	}
	
	public static Vector addVectors(Vector A, Vector B)
	{
		
		//Add Vector Components Together
		double x = A.getXComponent() + B.getXComponent();
		double y = A.getYComponent() + B.getYComponent();

		//Actual Variables Used in Determining Vector
		double tempMag = 0;
		double tempRefDir = 0;
		double tempDir = 0;
		
		//Exception Handling - Magnitude is Zero
		if( x == 0 && y == 0)
		{
			tempDir = 0;
			tempMag = 0;
		}

		//Determine Which Quadrant if X or Y Equals Zero
		else if( x > 0 && y == 0)
		{
			tempDir = 0;
			tempMag = x;
		}
		else if( x == 0 && y > 0)
		{
			tempDir = Math.PI / 2;
			tempMag = y;
		}
		else if( x < 0 && y == 0)
		{
			tempDir = Math.PI;
			tempMag = x;
		}
		else if( x == 0 && y < 0)
		{
			tempDir = (3 * Math.PI) / 2;
			tempMag = y;
		}

		//Calculate X & Y of new Vector
		else
		{	
			tempMag = Math.sqrt( (x * x) + (y * y) );
			tempRefDir = Math.abs(Math.atan(y / x));
	
			//Determine Actual Angle using the Reference Angle
			if(x > 0 && y > 0)
				tempDir = tempRefDir;
			else if(x < 0 && y > 0)
				tempDir = Math.PI - tempRefDir;	
			else if(x < 0 && y < 0)
				tempDir = tempRefDir + Math.PI;
			else if(x >	0 && y < 0)
				tempDir = (2 * Math.PI) - tempRefDir;
		}
		
		//Create New Vector
		Vector v = new Vector(Math.abs(tempMag), tempDir, true );

		return v;
	}
	
	public static String getStringDeg(Vector v)
	{
		StringBuilder builder  = new StringBuilder();
		
		builder.append(v.getMagnitude());
		builder.append(" [");
		builder.append(v.getDirectionDeg());
		builder.append(']');
		
		return builder.toString();
		
	}

	public static String getStringRads(Vector v)
	{
		StringBuilder builder  = new StringBuilder();
		
		builder.append(v.getMagnitude());
		builder.append(" with a heading of: ");
		builder.append(v.getDirectionRad());
		builder.append(" rads");
		
		return builder.toString();		
	}	
}
