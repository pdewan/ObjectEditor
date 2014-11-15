package bus.uigen.test;

import java.net.URL;

import javax.swing.JEditorPane;

import util.annotations.PreferredWidgetClass;

public class AnHTMLLinkHolder {
	URL url;

	public AnHTMLLinkHolder(URL url) {
		
		this.url = url;
	}
	
	

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	

}
