package com.soundlooper.gui.jsoundlooperslider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class JSoundLooperSliderUI extends ComponentUI implements MouseMotionListener, MouseListener {

	private final int SLIDER_HEIGHT = 4;
	private final int LEFT_MARGIN = 15;
	private final int RIGHT_MARGIN = 15;
	private final int CURSOR_WIDTH = 14;
	
	
	private JSoundLooperSlider soundLooperSlider;
	
	private Map<Integer, Rectangle> valuesRectangleMap =  new HashMap<Integer, Rectangle>();
	private Polygon cursor = new Polygon();
	
	// The object that is currently under the mouse
	private Object underMouseElement = null;
	
	//To know the gap between dragged element and mouse position at the begin of the drag (to know where the cursor was catched)
	private int mouseGapValueDragPx;
	
	//The object that is currently dragged
	private Object dragElement = null;
	
	//slider bound rectangle
	private Rectangle sliderRectangle = new Rectangle();

	
	public JSoundLooperSliderUI(JSoundLooperSlider soundLooperSlider) {
		super();
		this.soundLooperSlider = soundLooperSlider;
		this.soundLooperSlider.addMouseListener(this);
		this.soundLooperSlider.addMouseMotionListener(this);
	}
	
	private int getBottomDisplayedValue() {
		return soundLooperSlider.getHeight() - 2;
	}

	@Override
	public void update(Graphics graphics_Arg, JComponent comp) {
		this.paint(graphics_Arg);
	}
	
	public void paint(Graphics g) {
		Font font = this.soundLooperSlider.getFont();
		Font fontGras = new Font(font.getName(), Font.BOLD, font.getSize());
		g.setFont(font);
		
		Graphics2D graphics2d = (Graphics2D) g;
		RenderingHints renderingHints = new RenderingHints(null);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2d.setRenderingHints(renderingHints);
		
		//Background display
//		g.setColor(soundLooperSlider.getBackground());
//		g.fillRect(0, 0, soundLooperSlider.getWidth(), soundLooperSlider.getHeight());
		
		//Slider display
		g.setColor(Color.GRAY);
		sliderRectangle.setBounds(LEFT_MARGIN, getSliderY(), getWidhtSlider(), SLIDER_HEIGHT);
		g.fillRoundRect(sliderRectangle.x, sliderRectangle.y, sliderRectangle.width, sliderRectangle.height,5,5);
		
		//Values displayedMap display
		g.setColor(Color.BLACK);
		
		List<Integer> listDisplayedValue = soundLooperSlider.getListDisplayedValue();
		for (Integer integer : listDisplayedValue) {
			//get the point of the value on the slider
			int xOnComponent = getPixelFromValue(integer);
			
			Rectangle2D bound = g.getFontMetrics().getStringBounds(integer.toString(), g);
			int width = g.getFontMetrics().stringWidth(integer.toString());
			
			//calculate the x left point on the text
			int x = xOnComponent - width/2;
			
			if (x + width > soundLooperSlider.getWidth()) {
				x = soundLooperSlider.getWidth() - width;
			}
			if (x < 0) {
				x = 0;
			}
			
			int y = getBottomDisplayedValue() ;
			if( !valuesRectangleMap.containsKey(integer)) {
				Rectangle rectangleBound = new Rectangle(x, y- new Double(bound.getHeight()).intValue(), new Double(bound.getWidth()).intValue(), new Double(bound.getHeight()).intValue());
				valuesRectangleMap.put(integer, rectangleBound);
			}
			if (valuesRectangleMap.get(integer) == underMouseElement) {
				g.setFont(fontGras);
			} 
			
			g.drawString(integer.toString(), x, y);
			g.setFont(font);
			
		}
		
		//Cursor display
		int currentValuePx = getPixelFromValue(this.soundLooperSlider.getValue());
		int delta = CURSOR_WIDTH / 2;
		cursor.reset();
		cursor.addPoint(currentValuePx - delta + 1, getSliderY() + 2);
		cursor.addPoint(currentValuePx, getSliderY() + SLIDER_HEIGHT + 5);
		cursor.addPoint(currentValuePx + delta - 1,getSliderY() + 2);
		cursor.addPoint(currentValuePx + delta - 1,getSliderY() - 5);
		cursor.addPoint(currentValuePx - delta + 1, getSliderY() - 5);
		
		
		g.setColor(SystemColor.control);
		if (this.dragElement == cursor) {
			g.setColor(MetalLookAndFeel.getPrimaryControlShadow());
		} else if (this.underMouseElement == cursor) {
			g.setColor(MetalLookAndFeel.getControlHighlight());
		}
		g.fillPolygon(cursor);

		//DESSIN DES CONTOURS
		g.setColor(Color.BLACK);
		g.drawPolygon(cursor);
		
	}

	private int getSliderY() {
		return soundLooperSlider.getHeight()/2 - SLIDER_HEIGHT/2 - 5;
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {
		Object underMouseElementTemp = null;
		Set<Integer> keySet = valuesRectangleMap.keySet();
		for (Integer integer : keySet) {
			if (valuesRectangleMap.get(integer).contains(event.getX(), event.getY())) {
				underMouseElementTemp = valuesRectangleMap.get(integer);
				break;
			}
		}

		if (underMouseElementTemp == null && cursor.contains(event.getX(), event.getY())) {
			underMouseElementTemp = cursor;
		}
		
		if (underMouseElementTemp == null && sliderRectangle.contains(event.getX(), event.getY())) {
			underMouseElementTemp = sliderRectangle;
		}
			
		this.underMouseElement = underMouseElementTemp;
		
		this.soundLooperSlider.repaint();

	}
	
	private int getPixelFromValue(Integer integer) {
		int minValue = soundLooperSlider.getMinValue();
		int maxValue = soundLooperSlider.getMaxValue();
		int widhtSlider = getWidhtSlider();
		float pixelPerUnity = widhtSlider / new Float(maxValue - minValue);
		return new Float(pixelPerUnity * (integer-minValue)).intValue() + LEFT_MARGIN;
	}
	
	private int getValueFromPixel(Integer pxValue) {
		int pxValueOnSlider = pxValue - LEFT_MARGIN;
		int minValue = soundLooperSlider.getMinValue();
		int maxValue = soundLooperSlider.getMaxValue();
		int widhtSlider = getWidhtSlider();
		float pixelPerUnity = widhtSlider / new Float(maxValue - minValue);
		
		return new Float(minValue + (pxValueOnSlider / pixelPerUnity)).intValue();
	}

	private int getWidhtSlider() {
		return soundLooperSlider.getWidth() - LEFT_MARGIN - RIGHT_MARGIN;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (underMouseElement == sliderRectangle) {
			int clickGap = soundLooperSlider.getModel().getClickGap();
			if (e.isShiftDown()) {
				clickGap = clickGap * 3;
			}
			if (getValueFromPixel(e.getX()) > soundLooperSlider.getValue()) {
				soundLooperSlider.changeValue(soundLooperSlider.getValue() + clickGap);
			} else if (getValueFromPixel(e.getX()) < soundLooperSlider.getValue()) {
				soundLooperSlider.changeValue(soundLooperSlider.getValue() - clickGap);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.soundLooperSlider.requestFocus();
		if (valuesRectangleMap.containsValue(underMouseElement)) {
			Set<Integer> keySet = valuesRectangleMap.keySet();
			for (Integer integer : keySet) {
				if (valuesRectangleMap.get(integer) == underMouseElement) {
					soundLooperSlider.changeValue(integer);
					break;
				}
			}
		} else if (underMouseElement == cursor) {
			dragElement = cursor;
			this.mouseGapValueDragPx = e.getX() - getPixelFromValue(this.soundLooperSlider.getValue());
		} 
		this.soundLooperSlider.repaint();
		e.consume();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.dragElement = null;	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Nothing to do
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		underMouseElement = null;
		this.soundLooperSlider.repaint();
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragElement == cursor) {
			int minValue = soundLooperSlider.getMinValue();
			int maxValue = soundLooperSlider.getMaxValue();
			int newPositionPx = e.getX() - mouseGapValueDragPx;
			int newValue = getValueFromPixel(newPositionPx);
			if (newValue < minValue) {
				newValue = minValue;
			} else if (newValue > maxValue) {
				newValue = maxValue;
			}
			soundLooperSlider.changeValue(newValue);
			this.soundLooperSlider.repaint();
		}
		
	}
}
