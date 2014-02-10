package bus.uigen.view;

import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.net.URL;

import util.trace.Tracer;

import com.adobe.acrobat.Viewer;

import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.VirtualFrame;

public class AcrobatFrame  implements WindowListener {
	VirtualFrame frame = FrameSelector.createFrame();
	Viewer acrobat;
	public AcrobatFrame() {
		try {
			acrobat = new Viewer();			
			((Container)frame.getContentPane().getPhysicalComponent()).add(acrobat);
			frame.setSize(800, 1000);
			frame.addWindowListener(this);
		} catch (Exception e) {
			Tracer.error("Could not open Acrobat Viewer.");
			e.printStackTrace();
			
		}
		
	}
	boolean firstTime = true;
	public void showPDFURL(URL url) {
		new Thread(new AcrobatRunnable(frame, acrobat, url)).start();
	}
	public void syncShowPDFURL(URL url) {
		int count = 0;
		boolean quit = false;
		int maxCount = 10;
		
		try {
		 
		} catch (Exception e) {
			Tracer.error("Could not open: " + url);
			e.printStackTrace();
		}
		while (!quit) {
			//InputStream urlStream = null;
		try {
			
		//InputStream urlStream = url.openStream();
		InputStream urlStream = url.openStream();
		long sleepTime = 2000;
		Tracer.userMessage("Sleeping for" +  sleepTime  + "  milliseconds. Current # of bytes available:" + urlStream.available());
		Thread.sleep(sleepTime);
		Tracer.userMessage("Finished Sleeping. Current # of bytes available:" + urlStream.available());
		/*
		if (firstTime) {
			
			Message.userMessage("Sleeping for 10 seconds. Current # of bytes available:" + urlStream.available());
			Thread.sleep(20000);
			Message.userMessage("Finished Sleeping for 10 seconds. Current # of bytes available:" + urlStream.available());
		}
		*/
		
		acrobat.setDocumentInputStream(urlStream);
		quit = true;
		/*
		for (int i = 0; i < acrobat.getPageCount(); i++) {
			try {
			    acrobat.renderPage(i);
			} catch (Exception x) {
			    System.out.println("Unable to render page: " + i + " of file " + url);
			    x.printStackTrace();
			}
		}
		*/
		
		//Message.userMessage("set document" );
		//acrobat.activate();		
		//frame.setVisible(true);
		//firstTime = false;
		urlStream.close();
		} catch (Exception e) {
			
			count++;
			if (count == maxCount) {
				quit = true;
				Tracer.error("Could not open: " + url);
				e.printStackTrace();
				//urlStream.close();
			}
		}
		}
		try {
		acrobat.activate();		
		frame.setVisible(true);
		firstTime = false;
		} catch (Exception e) {
			Tracer.error("Could not open: " + url);
			e.printStackTrace();
		}
	}
	public void windowClosing(WindowEvent e) { 
	    
	    
		
		acrobat.deactivate();
	    
	    frame.setVisible(false) ;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
    };
	


