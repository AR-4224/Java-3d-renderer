package Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class ViewBox 
{

	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
		
		// slider for horizontal rotation
		JSlider H_Slider = new JSlider(0, 360, 180);
		pane.add(H_Slider, BorderLayout.SOUTH);
		
		// slider for vertical rotation
		JSlider V_Slider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
		pane.add(V_Slider, BorderLayout.EAST);
		
		Triangle.addToList(); // populating array
		
		
		
		//Panel for rendering
		JPanel panel = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.fillRect(0, 0, getWidth(), getHeight());
				
				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				double Horizontal = Math.toRadians(H_Slider.getValue());
				Matrix3 transformX = new Matrix3(new double[] {
						Math.cos(Horizontal), 0, -Math.sin(Horizontal),
						0, 1, 0,
						Math.sin(Horizontal), 0, Math.cos(Horizontal)
				});
				
				double Vertical = Math.toRadians(V_Slider.getValue());
				Matrix3 transformY = new Matrix3(new double[] {
						1, 0, 0,
						0, Math.cos(Vertical), Math.sin(Vertical),
						0, -Math.sin(Vertical), Math.cos(Vertical)
				});
				
				double[] zBuffer = new double[img.getWidth() * img.getHeight()];
				for(int i = 0; i < zBuffer.length; i++)
				{
					zBuffer[i] = Double.NEGATIVE_INFINITY;
				}
				
				Matrix3 transform = transformX.multiply(transformY);
				
				
				for (Triangle t : Triangle.shape)
				{
					Vertex v1 = transform.transform(t.v1);
					Vertex v2 = transform.transform(t.v2);
					Vertex v3 = transform.transform(t.v3);
					
					v1.x += getWidth() / 2;
					v1.y += getHeight() / 2;
					
					v2.x += getWidth() / 2;
					v2.y += getHeight() / 2;
					
					v3.x += getWidth() / 2;
					v3.y += getHeight() / 2;
					
					// computing bounds for triangle
					int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
					int maxX = (int) Math.min(img.getWidth() - 1,
												Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
					
					int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
					int maxY = (int) Math.min(img.getHeight() - 1,
												Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
					
					double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
					
					for(int y = minY; y <= maxY; y++)
					{
						for(int x = minX; x <= maxX; x++)
						{
							double bound1 = 
									((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
							double bound2 = 
									((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
							double bound3 = 
									((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
							
							if(bound1 >=0 && bound1 <= 1 && bound2 >= 0 && bound2 <= 1 && bound3 >= 0 && bound3 <= 1)
							{
								double depth = bound1 * v1.z + bound2 * v2.z + bound3 * v3.z;
								int zIndex = y * img.getWidth() + x;
								if(zBuffer[zIndex] < depth)
								{
									img.setRGB(x, y, t.color.getRGB());
									zBuffer[zIndex] = depth;
								}							
							}
						}
						
					}
				}
				g2.drawImage(img, 0, 0, null);
			}
		};
		
		
		pane.add(panel, BorderLayout.CENTER);
		
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		H_Slider.addChangeListener(e -> panel.repaint());
		V_Slider.addChangeListener(e -> panel.repaint());
	}

}
