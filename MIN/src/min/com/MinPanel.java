package min.com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

import javax.swing.JPanel;

import min.utils.MinUtils;


/**
 * 개요 : Panel 정보를 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static String HORIZONTAL = "HORIZONTAL";
	public static String VERTICAL = "VERTICAL";
	
	private ComponentListener componentListener;
	private JPanel panelEast;			// 사이드 여백
	private JPanel panelWest;			// 사이드 여백
	private JPanel panelNorth;		// 사이드 여백
	private JPanel panelSouth;		// 사이드 여백
	
	private String direction = VERTICAL;			// Gradient 방향성
	private boolean gradientPaint;
	private boolean gradientPaintGap;
	private boolean freezeCenter;				// 화면 고정 여부
	private int gradientPaintGapValue;
	
	private Image backgroundImage;
	
	private HashMap<Object, Object> properties;		// Panel 에 별도의 속성 값이 필요한 경우 입력
	
	/**
	 * public MmPanel()<br><br>
	 * FlowLayout 설정된 기본 MmPanel 을 생성합니다.
	 */
	public MinPanel() 
	{
		super();
		ini();
	}
	
	/**
	 * public MmPanel(boolean isDoubleBuffered)<br><br>
	 * Creates a new JPanel with FlowLayout and the specified buffering strategy. If isDoubleBuffered is true, the JPanel will use a double buffer. <br><br>
	 * @param isDoubleBuffered - a boolean, true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 */
	public MinPanel(boolean isDoubleBuffered) 
	{
		super(isDoubleBuffered);
		ini();
	}
	
	/**
	 * public MmPanel(LayoutManager layout)<br><br>
	 * Create a new buffered JPanel with the specified layout manager <br><br>
	 * @param layout - the LayoutManager to use
	 */
	public MinPanel(LayoutManager layout) 
	{
		super(layout);
		ini();
	}
	
	/**
	 * public MmPanel(LayoutManager layout, boolean isDoubleBuffered)<br><br>
	 * Creates a new JPanel with the specified layout manager and buffering strategy.<br><br> 
	 * @param layout - the LayoutManager to use<br>
	 * @param isDoubleBuffered - a boolean, true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 */
	public MinPanel(LayoutManager layout, boolean isDoubleBuffered) 
	{
		super(layout, isDoubleBuffered);
		ini();
	}
	
	/**
	 * public MmPanel(LayoutManager layout, boolean isDoubleBuffered)<br><br>
	 * Creates a new JPanel with the specified layout manager and buffering strategy.<br><br> 
	 * @param layout - the LayoutManager to use<br>
	 * @param comp - add Component
	 */
	public MinPanel(LayoutManager layout, Component comp) 
	{
		super(layout);
		ini();
		add(comp);
	}
	
	/**
	 * 생성자<br><br>
	 * Border Layout 설정 후 center 를 제외한 영역에 여백이 설정된 판낼을 생성합니다.
	 * @param sideGap - 여백 크기
	 */
	public MinPanel(int sideGap) 
	{
		super(new BorderLayout());
		ini(sideGap);
	}
	
	/**
	 * 생성자<br><br>
	 * Border Layout 설정 후 center 를 제외한 영역에 여백이 설정된 판낼을 생성합니다.
	 * @param sideGap - 여백 크기
	 * @param hGap - 가로 Gap
	 * @param vGap - 세로 Gap
	 */
	public MinPanel(int sideGap, int hGap, int vGap) 
	{
		super(new BorderLayout(hGap, vGap));
		ini(sideGap);
	}
	
	/**
	 * 생성자<br><br>
	 * Border Layout 설정 후 center 를 제외한 영역에 여백이 설정된 판낼을 생성합니다.
	 * @param westGap - 여백 크기
	 * @param eastGap - 여백 크기
	 * @param northGap - 여백 크기
	 * @param southGap - 여백 크기
	 */
	public MinPanel(int westGap, int eastGap, int northGap, int southGap) 
	{
		super(new BorderLayout());
		ini(westGap, eastGap, northGap, southGap);
	}
	
	/**
	 * 생성자<br><br>
	 * Border Layout 설정 후 center 를 제외한 영역에 여백이 설정된 판낼을 생성합니다.
	 * @param westGap - 여백 크기
	 * @param eastGap - 여백 크기
	 * @param northGap - 여백 크기
	 * @param southGap - 여백 크기
	 * @param hGap - 가로 Gap
	 * @param vGap - 세로 Gap
	 */
	public MinPanel(int westGap, int eastGap, int northGap, int southGap, int hGap, int vGap) 
	{
		super(new BorderLayout(hGap, vGap));
		ini(westGap, eastGap, northGap, southGap);
	}
	
	/**
	 * 생성자<br><br>
	 * 사이드 여백을 가지는 판낼을 생성합니다.
	 * @param westGap - 여백 크기
	 * @param eastGap - 여백 크기
	 * @param northGap - 여백 크기
	 * @param southGap - 여백 크기
	 * @param comp - Component
	 */
	public MinPanel(int westGap, int eastGap, int northGap, int southGap, Component comp) 
	{
		super(new BorderLayout());
		ini(westGap, eastGap, northGap, southGap);
		add(comp);
	}
	
	/**
	 * 생성자<br><br>
	 * 사이드 여백을 가지는 판낼을 생성합니다.
	 * @param westGap - 여백 크기
	 * @param eastGap - 여백 크기
	 * @param northGap - 여백 크기
	 * @param southGap - 여백 크기
	 * @param hGap - 가로 Gap
	 * @param vGap - 세로 Gap
	 * @param comp - Component
	 */
	public MinPanel(int westGap, int eastGap, int northGap, int southGap, int hGap, int vGap, Component comp) 
	{
		super(new BorderLayout(hGap, vGap));
		ini(westGap, eastGap, northGap, southGap);
		add(comp);
	}
	
	/**
	 * 생성자<br><br>
	 * 사이드 여백을 가지는 판낼을 생성합니다.
	 * @param sideGap - 여백 크기
	 * @param comp - Component
	 */
	public MinPanel(int sideGap, Component comp) 
	{
		super(new BorderLayout());
		ini(sideGap);
		add(comp);
	}
	
	/**
	 * 생성자<br><br>
	 * 사이드 여백을 가지는 판낼을 생성합니다.
	 * @param sideGap - 여백 크기
	 * @param hGap - 가로 Gap
	 * @param vGap - 세로 Gap
	 * @param comp - Component
	 */
	public MinPanel(int sideGap, int hGap, int vGap, Component comp) 
	{
		super(new BorderLayout(hGap, vGap));
		ini(sideGap);
		add(comp);
	}
	
	/**
	 * 초기화
	 */
	private void ini() 
	{
		properties = new HashMap<Object, Object>();
		setBackground(MinUtils.FRAME_BG);
	}

	/**
	 * 사이드 여백 초기화
	 * @param gap - 여백 크기
	 */
	private void ini(int gap) 
	{
		setBackground(MinUtils.FRAME_BG);
		
		panelEast = new JPanel();
		panelEast.setBackground(MinUtils.FRAME_BG);
		panelEast.setPreferredSize(new Dimension(gap, 0));
		panelWest = new JPanel();
		panelWest.setBackground(MinUtils.FRAME_BG);
		panelWest.setPreferredSize(new Dimension(gap, 0));
		panelNorth = new JPanel();
		panelNorth.setBackground(MinUtils.FRAME_BG);
		panelNorth.setPreferredSize(new Dimension(0, gap));
		panelSouth = new JPanel();
		panelSouth.setBackground(MinUtils.FRAME_BG);
		panelSouth.setPreferredSize(new Dimension(0, gap));
		
		add(panelNorth, BorderLayout.NORTH);
		add(panelWest, BorderLayout.WEST);
		add(panelEast, BorderLayout.EAST);
		add(panelSouth, BorderLayout.SOUTH);
	}
	
	/**
	 * 사이드 여백 초기화
	 * @param gap - 여백 크기
	 */
	private void ini(int westGap, int eastGap, int northGap, int southGap) 
	{
		setBackground(MinUtils.FRAME_BG);
		
		panelEast = new JPanel();
		panelEast.setBackground(MinUtils.FRAME_BG);
		panelEast.setPreferredSize(new Dimension(eastGap, 0));
		panelWest = new JPanel();
		panelWest.setBackground(MinUtils.FRAME_BG);
		panelWest.setPreferredSize(new Dimension(westGap, 0));
		panelNorth = new JPanel();
		panelNorth.setBackground(MinUtils.FRAME_BG);
		panelNorth.setPreferredSize(new Dimension(0, northGap));
		panelSouth = new JPanel();
		panelSouth.setBackground(MinUtils.FRAME_BG);
		panelSouth.setPreferredSize(new Dimension(0, southGap));
		
		add(panelNorth, BorderLayout.NORTH);
		add(panelWest, BorderLayout.WEST);
		add(panelEast, BorderLayout.EAST);
		add(panelSouth, BorderLayout.SOUTH);
	}

	/**
	 * Gradient 효과 설정 여부를 반환합니다.
	 * @return
	 */
	public boolean isGradientPaint() {
		return gradientPaint;
	}
	
	/**
	 * Gradient 효과 설정 여부를 반환합니다.
	 * @return
	 */
	public boolean isGradientPaintGap() {
		return gradientPaintGap;
	}
	
	/**
	 * Gradient 효과를 설정합니다.
	 * @param gradientPaint
	 * @param gradientPaintGapValue
	 */
	public void setGradientPaint(boolean gradientPaint, int gradientPaintGapValue) 
	{
		this.gradientPaintGap = gradientPaint;
		this.gradientPaintGapValue = gradientPaintGapValue;
		if(gradientPaint)
		{
			this.setOpaque(false);
		}
		else 
		{
			this.setOpaque(true);
		}
	}
	
	/**
	 * Gradient 효과를 설정합니다.
	 * @param  gradientPaint
	 */
	public void setGradientPaint(boolean gradientPaint) 
	{
		this.gradientPaint = gradientPaint;
		if(gradientPaint)
		{
			this.setOpaque(false);
		}
		else 
		{
			this.setOpaque(true);
		}
	}
	
	/**
	 * Gradient 방향성을 반환합니다.
	 * @return  HORIZONTAL : 수평방향<br>  VERTICAL : 수직방향
	 */
	public String getDirection()
	{
		return direction;
	}

	/**
	 * Gradient 방향성을 설정합니다.
	 * @param direction  - "HORIZONTAL", "VERTICAL"
	 */
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	
	/**
	 *  BorderLayout 으로 설정되 있는 경우 Center 의 Preferred Size 를 유지 하여 가운데 정렬 합니다.
	 * @param freezeCenterWidth
	 * @param freezeCenterHeight
	 */
	public void setFreezeCenter(boolean freezeCenter)
	{
		this.freezeCenter = freezeCenter;
		if(componentListener == null && freezeCenter)
		{
			componentListener = new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent paramComponentEvent) {}
				@Override
				public void componentResized(ComponentEvent paramComponentEvent) 
				{
					repaintFreezePanel();
				}
				@Override
				public void componentMoved(ComponentEvent paramComponentEvent) {}
				@Override
				public void componentHidden(ComponentEvent paramComponentEvent) {}
			};
			addComponentListener(componentListener);
		}
	}
	
	/**
	 * BorderLayout 으로 설정되 있는 경우 Center 의 Preferred Size 의 가운데 정렬을 재설정 합니다.
	 */
	public void repaintFreezePanel()
	{
		if(this.freezeCenter && this.getLayout() instanceof BorderLayout)
		{
			if(panelEast != null && panelNorth != null && panelSouth != null && panelWest != null)
			{
				BorderLayout layout = (BorderLayout)this.getLayout();
				Component centerComp = layout.getLayoutComponent(BorderLayout.CENTER);
				if(centerComp != null && centerComp.getPreferredSize() != null)
				{
					Dimension dimension = this.getSize();
					int freezeCenterWidth = centerComp.getPreferredSize().width;
					int freezeCenterHeight = centerComp.getPreferredSize().height;
					panelEast.setPreferredSize(new Dimension((dimension.width-freezeCenterWidth)/2, 0));
					panelWest.setPreferredSize(new Dimension((dimension.width-freezeCenterWidth)/2, 0));
					panelNorth.setPreferredSize(new Dimension(0, (dimension.height-freezeCenterHeight)/2));
					panelSouth.setPreferredSize(new Dimension(0, (dimension.height-freezeCenterHeight)/2));
					this.repaint();
				}
			}
		}
	}
	
	/**
	 * 배경 이미지를 반환합니다.
	 * @return
	 */
	public Image getBackgroundImage()
	{
		return backgroundImage;
	}

	/**
	 * 배경 이미지를 설정합니다.
	 * @param backgroundImage
	 */
	public void setBackgroundImage(Image backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}

	/**
	 * 속성 값을 설정합니다.
	 * @param key
	 * @param value
	 */
	public void putProperties(Object key, Object value)
	{
		properties.put(key, value);
	}
	
	/**
	 * 속성 값을 반환합니다.
	 * @param key
	 * @return
	 */
	public Object getProperties(Object key)
	{
		return properties.get(key);
	}
	
	/**
	 * 조회중 마우스 커서 설정합니다.
	 * @param wait
	 */
	public void setWaitMouse(boolean wait)
	{
		if(wait)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	@Override
	public void add(Component paramComponent, Object paramObject) 
	{
		super.add(paramComponent, paramObject);
	}
	
	@Override
	public void setOpaque(boolean isOpaque) 
	{
		if(panelEast != null && panelWest != null && panelNorth != null && panelSouth != null)
		{
			panelEast.setOpaque(isOpaque);
			panelWest.setOpaque(isOpaque);
			panelNorth.setOpaque(isOpaque);
			panelSouth.setOpaque(isOpaque);
		}
		super.setOpaque(isOpaque);
	}
	
	@Override
	public void setBackground(Color paramColor) 
	{
		if(panelEast != null && panelWest != null && panelNorth != null && panelSouth != null)
		{
			panelEast.setBackground(paramColor);
			panelWest.setBackground(paramColor);
			panelNorth.setBackground(paramColor);
			panelSouth.setBackground(paramColor);
		}
		super.setBackground(paramColor);
	}
	
	@Override
	protected void paintComponent(Graphics arg0)
	{
		if (isOpaque())
		{
			super.paintComponent(arg0);
		}
		if(this.isGradientPaint())
		{
			GradientPaint paint = null;
			Color gradientColor = this.getBackground().brighter();
			if(HORIZONTAL.equals(this.direction))
			{
				paint = new GradientPaint(0, this.getHeight()/2, gradientColor, this.getWidth(), this.getHeight()/2, this.getBackground(), true);
			}
			else if(VERTICAL.equals(this.direction))
			{
				paint = new GradientPaint(this.getWidth()/2, 0, gradientColor, this.getWidth()/2, this.getHeight(), this.getBackground(), true);
			}
			
			if(paint != null) 
			{
				Graphics2D graphics2D = (Graphics2D)arg0;
				Paint beforePaint = graphics2D.getPaint();
				graphics2D.setPaint(paint);
				graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
				graphics2D.setPaint(beforePaint);
				super.paintComponent(graphics2D);
			}
		}
		else if(this.isGradientPaintGap())
		{
			GradientPaint paint = null;
			Color beforeColor = this.getBackground();
			int r = beforeColor.getRed()+gradientPaintGapValue;
			int g = beforeColor.getGreen()+gradientPaintGapValue;
			int b = beforeColor.getBlue()+gradientPaintGapValue;
			if(r > 255)
			{
				r = 255;
			}
			if(r < 0)
			{
				r = 0;
			}
			if(g > 255)
			{
				g = 255;
			}
			if(g < 0)
			{
				g = 0;
			}
			if(b > 255)
			{
				b = 255;
			}
			if(b < 0)
			{
				b = 0;
			}
			Color gradientColor = new Color(r, g, b);
			if(HORIZONTAL.equals(this.direction))
			{
				paint = new GradientPaint(0, this.getHeight()/2, gradientColor, this.getWidth(), this.getHeight()/2, beforeColor, true);
			}
			else if(VERTICAL.equals(this.direction))
			{
				paint = new GradientPaint(this.getWidth()/2, 0, gradientColor, this.getWidth()/2, this.getHeight(), this.getBackground(), true);
			}
			
			if(paint != null) 
			{
				Graphics2D graphics2D = (Graphics2D)arg0;
				Paint beforePaint = graphics2D.getPaint();
				graphics2D.setPaint(paint);
				graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
				graphics2D.setPaint(beforePaint);
				super.paintComponent(graphics2D);
			}
		}
		else if(backgroundImage != null)
		{
			Graphics2D graphics2D = (Graphics2D)arg0;
			graphics2D.drawImage(backgroundImage, 0, 0, this);
		}
	}
	
	@Override
	public void repaint() 
	{
		super.revalidate();
		super.validate();
		super.repaint();
	}
}
