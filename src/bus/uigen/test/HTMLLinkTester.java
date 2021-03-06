package bus.uigen.test;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

import util.misc.Common;
import util.models.ALabelBeanModel;
import util.models.LabelBeanModel;
import bus.uigen.ObjectEditor;

public class HTMLLinkTester {
	
	public static void main (String[] args) {
//		String address = "http://sourceforge.net/projects/jhyperlink";
		String address = "http://www.google.com";

		URL url = null;
		try {
			Desktop.getDesktop().browse(new URI("http://www.google.com"));
			Desktop.getDesktop().browse(new URI("http://www.bing.com"));
		} catch (IOException | URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		JEditorPane jEditorPane = new JEditorPane();
		try {
			jEditorPane.setPage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.add(jEditorPane);
		frame.setSize(300, 300);
		frame.setVisible(true);
		
		AnHTMLLinkHolder holder = new AnHTMLLinkHolder(url);
		ObjectEditor.edit(holder);
		String html = Common.toBlueColoredUnderlinedHrefHTML(address, "CLICK ");
		String link = "<HTML><FONT color=\"#000099\"><U><ahref=http://sourceforge.net/projects/jhyperlink/>click</a></U></FONT>"
		        + " to go to the Java website.</HTML>";
//		String regexp = "(http\\://[:/?#\\[\\]@!%$&'()*+,;=a-zA-Z0-9._\\-~]+)";
		String regexp2 = "((https|http|ftp|file)\\://[:/?#\\[\\]@!%$&'()*+,;=a-zA-Z0-9._\\-~]+)";
		CompositeExample example = new ACompositeExample();
//		example.setString("<HTML>Click the <FONT color=\"#000099\"><U>http://sourceforge.net/projects/jhyperlink/</U></FONT>"
//        + " to go to the Java website.</HTML>");
//		example.setString(link);
		example.setString(html);

//		example.setString("<html><ahref=http://www.cs.unc.edu/~dewan/401/current/Lectures/AnimationThreadsWaitNotify.pptx>PowerPoint</a></html>");
		Pattern pattern = Pattern.compile(regexp2);
		String[] parts = link.split(regexp2);
//		Matcher matcher = pattern.matcher(link);
		Matcher matcher = pattern.matcher(html);
		
		
		System.out.println(parts);
		if (matcher.find())
		{
		    System.out.println(matcher.group(1));
		}
		ObjectEditor.edit(example);
		ImageIcon icon = new ImageIcon("holygrail2.PNG");
		LabelBeanModel labelModel = new ALabelBeanModel(html);
		ObjectEditor.edit(labelModel);
		
//		 labelModel = new ALabelBeanModel(html, icon);
//		ObjectEditor.edit(labelModel);
	}

	
}
