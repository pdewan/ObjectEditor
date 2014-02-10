package bus.uigen.jung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import javax.swing.CellRendererPane;
import javax.swing.Icon;

import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class AGraphicsDecoratorTrapper extends GraphicsDecorator implements GraphicsDecoratorTrapper{
	GraphicsDecorator graphicsDecorator;
	
	public void setGraphicsDecorator(GraphicsDecorator newVal) {
		
		graphicsDecorator = newVal;
	}
	
    public GraphicsDecorator setGraphicsDecorator() {		
		return graphicsDecorator;
	}
    
    public GraphicsDecorator getGraphicsDecorator() {		
		return graphicsDecorator;
	}
	

	public void draw(Icon icon, Component c, Shape clip, int x, int y) {
		if (graphicsDecorator == null) {
			super.draw(icon, c, clip, x, y);
		} else

		graphicsDecorator.draw(icon, c, clip, x, y);
	}

	public void draw(Component c, CellRendererPane rendererPane, int x, int y,
			int w, int h, boolean shouldValidate) {
		if (graphicsDecorator == null) {
			super.draw(c, rendererPane, x, y, w, h, shouldValidate);
		} else
		graphicsDecorator.draw(c, rendererPane, x, y, w, h, shouldValidate);
		
	}

	public void setDelegate(Graphics2D delegate) {
		if (graphicsDecorator == null) {
			super.setDelegate(delegate);
		} else	{
			graphicsDecorator.setDelegate(delegate);
		}
	}

	public Graphics2D getDelegate() {
		if (graphicsDecorator == null) {
			return super.getDelegate();
		} else {
		return graphicsDecorator.getDelegate();
		}
	}

	public void addRenderingHints(Map hints) {
		if (graphicsDecorator == null) {
			super.addRenderingHints(hints);
		} else {
		   graphicsDecorator.addRenderingHints(hints);
		}
	}

	public void clearRect(int x, int y, int width, int height) {
		if (graphicsDecorator == null) {
			super.clearRect(x, y, width, height);
		} else

		graphicsDecorator.clearRect(x, y, width, height);
	}

	public void clip(Shape s) {
		if (graphicsDecorator == null)
			super.clip(s);
		else
			graphicsDecorator.clip(s);
	}

	public void clipRect(int x, int y, int width, int height) {
		if (graphicsDecorator == null)
			super.clipRect(x, y, width, height);
		else

			graphicsDecorator.clipRect(x, y, width, height);
	}

	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		graphicsDecorator.copyArea(x, y, width, height, dx, dy);
	}

	public Graphics create() {
		return graphicsDecorator.create();
	}

	public Graphics create(int x, int y, int width, int height) {
		return graphicsDecorator.create(x, y, width, height);
	}

	public void dispose() {
		
		graphicsDecorator.dispose();
	}

	public void draw(Shape s) {
		if (graphicsDecorator == null) 
			super.draw(s);
		else
		graphicsDecorator.draw(s);
	}

	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		if (graphicsDecorator == null) 
			super.draw3DRect(x, y, width, height, raised);
		else
		graphicsDecorator.draw3DRect(x, y, width, height, raised);
	}

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		graphicsDecorator.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	public void drawBytes(byte[] data, int offset, int length, int x, int y) {
		graphicsDecorator.drawBytes(data, offset, length, x, y);
	}

	public void drawChars(char[] data, int offset, int length, int x, int y) {
		graphicsDecorator.drawChars(data, offset, length, x, y);
	}

	public void drawGlyphVector(GlyphVector g, float x, float y) {
		graphicsDecorator.drawGlyphVector(g, x, y);
	}

	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		graphicsDecorator.drawImage(img, op, x, y);
	}

	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return graphicsDecorator.drawImage(img, xform, obs);
	}

	public boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		return graphicsDecorator.drawImage(img, x, y, bgcolor, observer);
	}

	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return graphicsDecorator.drawImage(img, x, y, observer);
	}

	public boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		return graphicsDecorator.drawImage(img, x, y, width, height, bgcolor,
				observer);
	}

	public boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		return graphicsDecorator.drawImage(img, x, y, width, height, observer);
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		return graphicsDecorator.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1,
				sx2, sy2, bgcolor, observer);
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return graphicsDecorator.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1,
				sx2, sy2, observer);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		graphicsDecorator.drawLine(x1, y1, x2, y2);
	}

	public void drawOval(int x, int y, int width, int height) {
		graphicsDecorator.drawOval(x, y, width, height);
	}

	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		graphicsDecorator.drawPolygon(xPoints, yPoints, nPoints);
	}

	public void drawPolygon(Polygon p) {
		graphicsDecorator.drawPolygon(p);
	}

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		graphicsDecorator.drawPolyline(xPoints, yPoints, nPoints);
	}

	public void drawRect(int x, int y, int width, int height) {
		graphicsDecorator.drawRect(x, y, width, height);
	}

	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		graphicsDecorator.drawRenderableImage(img, xform);
	}

	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		graphicsDecorator.drawRenderedImage(img, xform);
	}

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		graphicsDecorator.drawRoundRect(x, y, width, height, arcWidth,
				arcHeight);
	}

	public void drawString(AttributedCharacterIterator iterator, float x,
			float y) {
		graphicsDecorator.drawString(iterator, x, y);
	}

	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		graphicsDecorator.drawString(iterator, x, y);
	}

	public void drawString(String s, float x, float y) {
		graphicsDecorator.drawString(s, x, y);
	}

	public void drawString(String str, int x, int y) {
		graphicsDecorator.drawString(str, x, y);
	}

	public boolean equals(Object obj) {
		return graphicsDecorator.equals(obj);
	}

	public void fill(Shape s) {
		if (graphicsDecorator == null) 
			super.fill(s);
		else
		graphicsDecorator.fill(s);
	}

	public void fill3DRect(int x, int y, int width, int height, boolean raised) {
		if (graphicsDecorator == null) 
			super.fill3DRect(x, y, width, height, raised);
		else
		graphicsDecorator.fill3DRect(x, y, width, height, raised);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		if (graphicsDecorator == null) 
			super.fillArc(x, y, width, height, startAngle, arcAngle);
		else
		graphicsDecorator.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	public void fillOval(int x, int y, int width, int height) {
		if (graphicsDecorator == null) 
			super.fillOval(x, y, width, height);
		else
		graphicsDecorator.fillOval(x, y, width, height);
	}

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		
		graphicsDecorator.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void fillPolygon(Polygon p) {
		graphicsDecorator.fillPolygon(p);
	}

	public void fillRect(int x, int y, int width, int height) {
		graphicsDecorator.fillRect(x, y, width, height);
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		graphicsDecorator.fillRoundRect(x, y, width, height, arcWidth,
				arcHeight);
	}

	public void finalize() {
		graphicsDecorator.finalize();
	}

	public Color getBackground() {
		return graphicsDecorator.getBackground();
	}

	public Shape getClip() {
		return graphicsDecorator.getClip();
	}

	public Rectangle getClipBounds() {
		return graphicsDecorator.getClipBounds();
	}

	public Rectangle getClipBounds(Rectangle r) {
		return graphicsDecorator.getClipBounds(r);
	}

	public Rectangle getClipRect() {
		return graphicsDecorator.getClipRect();
	}

	public Color getColor() {
		return graphicsDecorator.getColor();
	}

	public Composite getComposite() {
		return graphicsDecorator.getComposite();
	}

	public GraphicsConfiguration getDeviceConfiguration() {
		return graphicsDecorator.getDeviceConfiguration();
	}

	public Font getFont() {
//		return graphicsDecorator.getDefaultFont();
		return graphicsDecorator.getFont();

	}

	public FontMetrics getFontMetrics() {
		return graphicsDecorator.getFontMetrics();
	}

	public FontMetrics getFontMetrics(Font f) {
		return graphicsDecorator.getFontMetrics(f);
	}

	public FontRenderContext getFontRenderContext() {
		return graphicsDecorator.getFontRenderContext();
	}

	public Paint getPaint() {
		if (graphicsDecorator == null)
			return super.getPaint();
		return graphicsDecorator.getPaint();
	}

	public Object getRenderingHint(Key hintKey) {
		if (graphicsDecorator == null)
			return getRenderingHint(hintKey);
		return graphicsDecorator.getRenderingHint(hintKey);
	}

	public RenderingHints getRenderingHints() {
		return graphicsDecorator.getRenderingHints();
	}

	public Stroke getStroke() {
		if (graphicsDecorator == null)
			return super.getStroke();
		return graphicsDecorator.getStroke();
	}

	public AffineTransform getTransform() {
		if (graphicsDecorator == null)
			return super.getTransform();
		return graphicsDecorator.getTransform();
	}

	public int hashCode() {
		if (graphicsDecorator == null)
			return super.hashCode();
		return graphicsDecorator.hashCode();
	}

	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		if (graphicsDecorator == null)
			return super.hit(rect, s, onStroke);
		return graphicsDecorator.hit(rect, s, onStroke);
	}

	public boolean hitClip(int x, int y, int width, int height) {
		if (graphicsDecorator == null)
			super.hitClip(x, y, width, height);
		return graphicsDecorator.hitClip(x, y, width, height);
	}

	public void rotate(double theta, double x, double y) {
		if (graphicsDecorator == null)
			super.rotate(theta, x, y);
		else
		graphicsDecorator.rotate(theta, x, y);
	}

	public void rotate(double theta) {
		if (graphicsDecorator == null)
			super.rotate(theta);
		else
		graphicsDecorator.rotate(theta);
	}

	public void scale(double sx, double sy) {
		graphicsDecorator.scale(sx, sy);
	}

	public void setBackground(Color color) {
		if (graphicsDecorator == null) 
			super.setBackground(color);
		else
		graphicsDecorator.setBackground(color);
	
	}

	public void setClip(int x, int y, int width, int height) {
		if (graphicsDecorator == null) 
			super.setClip(x, y, width, height);
		else
		graphicsDecorator.setClip(x, y, width, height);
	}

	public void setClip(Shape clip) {
		graphicsDecorator.setClip(clip);
	}

	public void setColor(Color c) {
		graphicsDecorator.setColor(c);
	}

	public void setComposite(Composite comp) {
		graphicsDecorator.setComposite(comp);
	}

	public void setFont(Font font) {
		graphicsDecorator.setFont(font);
	}

	public void setPaint(Paint paint) {
		if (graphicsDecorator == null)
			super.setPaint(paint);
		else
		graphicsDecorator.setPaint(paint);
	}

	public void setPaintMode() {
		graphicsDecorator.setPaintMode();
	}

	public void setRenderingHint(Key hintKey, Object hintValue) {
		graphicsDecorator.setRenderingHint(hintKey, hintValue);
	}

	public void setRenderingHints(Map hints) {
		graphicsDecorator.setRenderingHints(hints);
	}

	public void setStroke(Stroke s) {
		if (graphicsDecorator == null)
			super.setStroke(s);
		else
		graphicsDecorator.setStroke(s);
	}

	public void setTransform(AffineTransform Tx) {
		if (graphicsDecorator == null)
			super.setTransform(Tx);
		else
		graphicsDecorator.setTransform(Tx);
	}

	public void setXORMode(Color c1) {
		graphicsDecorator.setXORMode(c1);
	}

	public void shear(double shx, double shy) {
		graphicsDecorator.shear(shx, shy);
	}

	public String toString() {
		if (graphicsDecorator == null)
			return  super.toString();
		return graphicsDecorator.toString();
	}

	public void transform(AffineTransform Tx) {
		graphicsDecorator.transform(Tx);
	}

	public void translate(double tx, double ty) {
		graphicsDecorator.translate(tx, ty);
	}

	public void translate(int x, int y) {
		graphicsDecorator.translate(x, y);
	}
	

}
