package circles;

import java.awt.*;
import javax.swing.JFrame;

public class expend extends JFrame
{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	int fps = 40;
	int speed = 10;
	int unit = 40;
	
	
	public static void main(String args[])
	{
		System.out.println("Expend: test");
		System.out.println("Created by virtualize at "+System.currentTimeMillis());
		System.out.println("");
		new expend();
	}
	
	clock_class clock = new clock_class(this);
	
	expend()
	{
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		clock.start();
	}
	
	boolean started = false;
	
	String status1 = "started.", status2 = "", status3 = "";
	void showStatus(String msg)
	{
		status3 = status2;
		status2 = status1;
		status1 = msg;
	}
	
	public void paint(Graphics g)
	{
		started = true;
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.black);
		g.fillRect(30, 30, 25+(int)(25.0*Math.sin((double)clock.c/speed)), 50);
		
		g.setFont(new Font("dialog", 10, 10));
		g.setXORMode(Color.darkGray);
		g.drawString(status1, 10, this.getHeight()-10);
		g.setXORMode(Color.gray);
		g.drawString(status2, 10, this.getHeight()-25);
		g.setXORMode(Color.lightGray);
		g.drawString(status3, 10, this.getHeight()-40);
		g.setPaintMode();
		//repaint();
	}
	
	class clock_class implements Runnable
	{
		/**clock:
		 * This is the handler of repainting.
		 */
		Thread t;
		long c;
		expend ex;
		clock_class(expend e)
		{
			ex = e;
			t = new Thread(this);
			c = 0;
		}
		public void run()
		{
			while(true)
			{
				try {
					Thread.sleep(1000/fps);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				c++;
				//System.out.println("clock: "+c);
				if(ex.started)
					ex.repaint();
			}
		}
		public void start()
		{
			t.start();
		}
	}
}
