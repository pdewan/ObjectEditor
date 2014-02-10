package bus.uigen.view;

import java.io.InputStream;
import java.net.URL;

import util.trace.Tracer;

import com.adobe.acrobat.Viewer;

import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.VirtualFrame;

public class AcrobatRunnable implements Runnable {
	VirtualFrame frame = FrameSelector.createFrame();
	Viewer acrobat;
	URL url;
	public AcrobatRunnable(VirtualFrame theFrame, Viewer theAcrobat, URL theURL) {
		frame = theFrame;
		acrobat = theAcrobat;
		url = theURL;
		
	}
	boolean firstTime = true;
	public void run() {
		int count = 0;
		boolean quit = false;
		int maxCount = 50;
		
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
		long sleepTime = 1000 * (1 + count/2);
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

}
