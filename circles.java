package circles;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

public class circles extends JFrame implements MouseListener,
	MouseMotionListener, MouseWheelListener, ActionListener
{
	/**
	 * This is a small program(not really small, for ones like me:_) generating a picture 
		that combined with circles.
	 * Actually, you have to make it blur to make it clear.
	 */
	private static final long serialVersionUID = 1L;
	String status_text = "default message";
	int windowsize = 640;
	int unit_len = 16;
	int mws;
	
	int default_cir_size = 6;
	int full_cir_size = 5;
	
	int maxt;

	Image iBuffer;
	Graphics gBuffer;
	private boolean repaint_full_flag;
	boolean dragging;
	
	int []cir_size;
	int []cir_x;
	int []cir_y;
	int ctmp = 0;
	
	int mx, my;

	public enum pic_type {Rect, Hexagon}
	pic_type pt = pic_type.Rect;
	//varibles
	
	void resize()
	{
		this.setSize(windowsize, windowsize+30);
	}
	
	void showStatus(String str)
	{
		status_text = str;
	}
	
	public static void main(String[] args)
	{
		new circles();
	}
	
	void init_cir_pos()
	{
		switch(pt)
		{
		case Rect:
			maxt = square(windowsize/unit_len+10);
			cir_size = new int[maxt];
			cir_x = new int[maxt];
			cir_y = new int[maxt];
			for(int i = 0; i < maxt; i++)
			{
				cir_size[i] = default_cir_size;
				cir_x[i] = -10000;
				cir_y[i] = -10000;
			}
			ctmp = 0;
		    for(int i = 0; i < windowsize/2; i+=unit_len)
		    {
		    	for(int j = 0; j < windowsize/2; j+=unit_len)
			    {
		    		cir_x[ctmp] = i;
		    		cir_y[ctmp] = j;
			    	ctmp++;
			    	
			    	cir_x[ctmp] = -i;
		    		cir_y[ctmp] = j;
			    	ctmp++;
			    	
			    	cir_x[ctmp] = i;
		    		cir_y[ctmp] = -j;
			    	ctmp++;
			    	
			    	cir_x[ctmp] = -i;
		    		cir_y[ctmp] = -j;
		    		
			    	ctmp++;
			    }
		    }
		    break;
		case Hexagon:
			maxt = square(windowsize/unit_len*3);
			cir_size = new int[maxt];
			cir_x = new int[maxt];
			cir_y = new int[maxt];
			for(int i = 0; i < maxt; i++)
			{
				cir_size[i] = default_cir_size;
				cir_x[i] = -10000;
				cir_y[i] = -10000;
			}
			ctmp = 0;
		    for(int i = 0; i < windowsize/2*3; i+=unit_len)
		    {
		    	for(int j = 0; j < windowsize/2*3; j+=unit_len)
			    {
		    		cir_x[ctmp] = i-j/2;
		    		cir_y[ctmp] = (int)(j*Math.sqrt(3)/2);
			    	ctmp++;
			    	
			    	cir_x[ctmp] = -i+j/2;
		    		cir_y[ctmp] = -(int)(j*Math.sqrt(3)/2);
			    	ctmp++;
			    	
			    	cir_x[ctmp] = j-i/2;
		    		cir_y[ctmp] = -(int)(i*Math.sqrt(3)/2);
			    	ctmp++;
			    	
			    	cir_x[ctmp] = -j+i/2;
		    		cir_y[ctmp] = (int)(i*Math.sqrt(3)/2);
			    	ctmp++;
			    }
		    }
		    break;
		default:
			System.out.println("Missing picture type:"+pt.name());
			break;
		}
	}
	
	circles()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.resize();
		this.setBackground(new Color(17,18,125));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		
		MenuBar mbar = new MenuBar();
		this.setMenuBar(mbar);
		
		Menu file = new Menu("File");
		MenuItem item1_1;
		file.add(item1_1 = new MenuItem("Save to Clipboard"));
		item1_1.addActionListener(this);
		mbar.add(file);
		
		Menu window = new Menu("Window");
		MenuItem item2_1, item2_2, item2_3, item2_4;
		window.add(item2_1 = new MenuItem("Clear"));
		window.add(item2_2 = new MenuItem("Display Shortcut"));
		window.add(item2_3 = new MenuItem("Use Default setting"));
		window.add(item2_4 = new MenuItem("Professional..."));
		item2_1.addActionListener(this);
		item2_2.addActionListener(this);
		item2_3.addActionListener(this);
		item2_4.addActionListener(this);
		mbar.add(window);
		
		Menu picture = new Menu("Picture");
		for(pic_type pt : pic_type.values())
		{
			MenuItem tmp;
			picture.add(tmp = new MenuItem(pt.name()));
			tmp.addActionListener(this);
		}
		mbar.add(picture);
		
		Menu help = new Menu("Help");
		MenuItem item3_1, item3_2;
		help.add(item3_1 = new MenuItem("Help..."));
		help.add(item3_2 = new MenuItem("About..."));
		item3_1.addActionListener(this);
		item3_2.addActionListener(this);
		mbar.add(help);
		//menus
		
		this.init_cir_pos();
	    //initalize circles position
		
		repaint_full_flag = true;
		mx = my = -10;
		mws = 10; //default mouse circle size
		
		this.setVisible(true);
	}
	
	void draw()
	{
		if(repaint_full_flag)
	    {
	       iBuffer=createImage(windowsize,windowsize);
	       gBuffer=iBuffer.getGraphics();
	       repaint_full_flag = false;
	    }
	    gBuffer.setColor(getBackground());
	    gBuffer.fillRect(0,0,windowsize,windowsize);
	    gBuffer.setColor(Color.lightGray);
	    
	    //gBuffer.drawLine(1, 3, 134, 436);
	    for(int i = 0; i < ctmp; i++)
	    {
    		gBuffer.fillOval(cir_x[i]+windowsize/2-cir_size[i],
    				cir_y[i]+windowsize/2-cir_size[i], 2*cir_size[i], 2*cir_size[i]);
	    }
	    
	}
	
	public void paint(Graphics g)
	{
		if(repaint_full_flag)
		{
			draw();
			repaint_full_flag = false;
		}
		g.drawImage(iBuffer, 0, 20, this);
		//paint from buffer
		
		g.setColor(Color.gray);
		if(dragging)
		{
			g.fillOval(mx-mws, my-mws, mws*2, mws*2);
			dragging = false;
		}
		else
			g.drawOval(mx-mws, my-mws, mws*2, mws*2);
		//showing status text
		
		g.clearRect(0, windowsize+20, windowsize, 10);
		g.setFont(new Font("dialog", 10, 10));
		g.setColor(Color.white);
		g.drawString(status_text, 10, this.getHeight()-10);
		//repaint();
	}

	public void update(Graphics g)
	{
		paint(g);
	}
	@Override
	public void mouseDragged(MouseEvent me) 
	{
		mx = me.getX();
		my = me.getY();
		showStatus("Mouse at x:"+mx+"\t  y:"+my);
		if(me.getButton()==MouseEvent.BUTTON1)
			engage(true);
		else if(me.getButton()==MouseEvent.BUTTON3)
			engage(false);
		repaint();
	}
	
	public void mouseMoved(MouseEvent me) 
	{
		mx = me.getX();
		my = me.getY();
		showStatus("Mouse at x:"+mx+"\t  y:"+my);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) 
	{
		if(me.getButton()==MouseEvent.BUTTON1)
			engage(true);
		else if(me.getButton()==MouseEvent.BUTTON3)
			engage(false);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		//showStatus("Mouse is not in window");
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) 
	{
		mws+=-mwe.getWheelRotation();
		if(mws<2)
			mws = 2;
		if(mws>800)
			mws = 800;
		repaint();
	}
	
	private int square(int x)
	{
		return x*x;
	}
	
	void engage(boolean type)
	{
		dragging = true;
		for(int i = 0; i < ctmp; i++)
		{
			if(square(cir_x[i]+windowsize/2-mx)+square(cir_y[i]+windowsize/2+20-my)<=square(mws))
			{
				System.out.println("Update circle no."+i+"\tat  x:"+cir_x[i]+"  \ty:"+cir_y[i]);
				if(type == true)
					cir_size[i] = full_cir_size;
				if(type == false)
					cir_size[i] = default_cir_size;
			}
		}
		draw();
	}

	@SuppressWarnings("serial")
	class shortcut extends JFrame
	{
		Image img;
		shortcut(Image i)
		{
			img = i;
			this.setSize(200, 220);
			this.setVisible(true);
			this.setTitle("Shortcut");
		}
		public void paint(Graphics g)
		{
			g.drawImage(img, 0, 20, 200, 200, this);
		}
	}
	
	@SuppressWarnings("serial")
	class help_window extends JFrame
	{
		help_window()
		{
			this.setSize(400,200);
			setLayout(new FlowLayout(FlowLayout.CENTER));
			this.setTitle("Help");
		    String val = "This is not much hard to try to use this program.\n"
		    		 +"You can paint whatever you want by dragging your mouse,\n"
		    		 +"\tor erase them using right clicks.\n"
		    		 +"And you can save the picture by screeshot software,\n"
		    		 +"\tor press File>Save to clipboard.\n\n"
		    		 +"Enjoy!";
		    TextArea text = new TextArea(val, 10, 40);
		    add(text); 
		    text.setEditable(false);
		    this.setResizable(false);
		    this.setVisible(true);
		}
	}
	
	@SuppressWarnings("serial")
	class about_window extends JFrame
	{
		about_window()
		{
			this.setSize(200,100);
			setLayout(new FlowLayout(FlowLayout.CENTER));
			this.setTitle("About");
			Label title = new Label("               ");
		    add(title);
		    add(new Label("Created by vitualize in 2016")); 
		    add(new Label("    All rights reserved.    ")); 
		    this.setResizable(false);
		    this.setVisible(true);
		}
	}
	@SuppressWarnings("serial")
	class settings extends JFrame implements ActionListener
	{
		TextField windowsizet = new TextField(4);
		TextField unitt = new TextField(4);
		TextField defaultt = new TextField(4);
		TextField fullt = new TextField(4);
		circles circ;
		settings(circles cir)
		{
			circ = cir;
			this.setSize(200,320);
			this.setTitle("Settings");
			setLayout(new FlowLayout(FlowLayout.LEFT));
			Label windowsizep = new Label("Window size: \t", Label.RIGHT);
			Label unitp = new Label("Unit length:   \t", Label.RIGHT);
			Label defaultp = new Label("Default length: \t", Label.RIGHT);
			Label fullp = new Label("Full length:    \t", Label.RIGHT);
			windowsizet.setText(""+cir.windowsize);
			unitt.setText(""+cir.unit_len);
			defaultt.setText(""+cir.default_cir_size);
			fullt.setText(""+cir.full_cir_size);
			add(windowsizep);
			add(windowsizet);
			add(unitp);
			add(unitt);
			add(defaultp);
			add(defaultt);
			add(fullp);
			add(fullt);
			
			JButton j = new JButton("Apply");
			add(j);
			j.addActionListener(this);
			
			windowsizet.addActionListener(cir);
			unitt.addActionListener(cir);
			defaultt.addActionListener(cir);
			fullt.addActionListener(cir);
			
		    this.setResizable(false);
		    this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent ae)
		{
			if(ae.getActionCommand().equals("Apply"))
			{
				circ.windowsize = Integer.parseInt(windowsizet.getText());
				circ.unit_len = Integer.parseInt(unitt.getText());
				circ.default_cir_size = Integer.parseInt(defaultt.getText());
				circ.full_cir_size = Integer.parseInt(fullt.getText());
				circ.repaint_full_flag = true;
				circ.resize();
				circ.init_cir_pos();
				circ.draw();
				circ.repaint();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		String command = ae.getActionCommand();
		if(command.equals("Save to Clipboard"))
		{
			circles.setImageClipboard(iBuffer);
			this.showStatus("Saved to clipboard");
		}
		else if(command.equals("Clear"))
		{
			for(int i = 0; i < ctmp; i++)
				cir_size[i] = default_cir_size;
			draw();
			this.showStatus("Window cleared");
		}
		else if(command.equals("Display Shortcut"))
		{
			new shortcut(iBuffer);
		}
		else if(command.equals("Help..."))
		{
			new help_window();
		}
		else if(command.equals("About..."))
		{
			new about_window();
		}
		else if(command.equals("Professional..."))
		{
			new settings(this);
		}
		else if(command.equals("Use Default setting"))
		{
			this.pt = pic_type.Rect;
			this.windowsize = 640;
			this.unit_len = 16;
			this.default_cir_size = 6;
			this.full_cir_size = 5;
			this.repaint_full_flag = true;
			this.resize();
			this.init_cir_pos();
			this.draw();
			this.repaint();
		}
		else
		{
			for(pic_type pwt : pic_type.values())
			{
				if(pwt.name().equals(command))
				{
					pt = pwt;
					this.repaint_full_flag = true;
					this.resize();
					this.init_cir_pos();
					this.draw();
					this.repaint();
					this.showStatus("Picture type set to: "+pt);
				}
			}
		}
		repaint();
	}	
	public static void setImageClipboard(Image image) 
	{   
		//this part is configring with saving to clipboard
	    ImageSelection imgSel = new ImageSelection(image);   
	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);   
	}	  
	public static class ImageSelection implements Transferable 
	{   
	    private Image image;   
	    public ImageSelection(Image image) 
	    {
	    	this.image = image;
	    }   
	        
	    public DataFlavor[] getTransferDataFlavors() 
	    {   
	        return new DataFlavor[]{DataFlavor.imageFlavor};   
	    }   
	        
	    public boolean isDataFlavorSupported(DataFlavor flavor) 
	    {   
	        return DataFlavor.imageFlavor.equals(flavor);   
	    }  
	        
	    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	    {   
	        if (!DataFlavor.imageFlavor.equals(flavor)) 
	        {
	        	throw new UnsupportedFlavorException(flavor);
	        }   
	        return image;   
	    }  
	}

}
