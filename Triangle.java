package Renderer;

import java.awt.Color;
import java.util.*;


public class Triangle 
{
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Color color;
	

	// triangle made from 3 coordinates in a set set colour
	Triangle(Vertex v1, Vertex v2, Vertex v3, Color color)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
	
	public static ArrayList<Triangle> shape = new ArrayList<Triangle>();

	public static void addToList() 
	{
		shape.add(new Triangle(new Vertex(100, 100, 100),
							   new Vertex(-100, -100, 100),
							   new Vertex(-100, 100, -100),
							   Color.white));
		shape.add(new Triangle(new Vertex(100, 100, 100),
							   new Vertex(-100, -100, 100),
							   new Vertex(100, -100, -100),
							   Color.orange));
		shape.add(new Triangle(new Vertex(-100, 100, -100),
							   new Vertex(100, -100, -100),
							   new Vertex(100, 100, 100),
							   Color.magenta));
		shape.add(new Triangle(new Vertex(-100, 100, -100),
							   new Vertex(100, -100, -100),
							   new Vertex(-100, -100, 100),
							   Color.green));
	}
	
	
}
