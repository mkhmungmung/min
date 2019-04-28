package min.com;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;

/**
 * 개요 : Frame 정보를 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private MinPanel panelCenter;
	
	private Component workspace;
	private Image icon;
	
	private String title;
	private int width;
	private int height;

	/**
	 * 생성자
	 * @param title
	 */
	public MinFrame(String title) 
	{
		super();
		this.title = title;
		width = 1024;
		height = 800;
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param width
	 * @param height
	 */
	public MinFrame(String title, int width, int height) 
	{
		super();
		this.title = title;
		this.width = width;
		this.height = height;
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param icon
	 */
	public MinFrame(String title, Image icon)
	{
		super();
		this.title = title;
		this.icon = icon;
		width = 1024;
		height = 800;
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param width
	 * @param height
	 * @param icon
	 */
	public MinFrame(String title, int width, int height, Image icon)
	{
		super();
		this.title = title;
		this.icon = icon;
		this.width = width;
		this.height = height;
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param undecorated
	 */
	public MinFrame(String title, boolean undecorated) 
	{
		super();
		this.title = title;
		width = 1024;
		height = 800;
		setUndecorated(undecorated);
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param undecorated
	 * @param icon
	 */
	public MinFrame(String title, boolean undecorated, Image icon) 
	{
		super();
		this.title = title;
		this.icon = icon;
		width = 1024;
		height = 800;
		setUndecorated(undecorated);
		ini();
	}

	/**
	 * 생성자
	 * @param title
	 * @param width
	 * @param height
	 * @param undecorated
	 * @param icon
	 */
	public MinFrame(String title, int width, int height, boolean undecorated, Image icon) 
	{
		super();
		this.title = title;
		this.icon = icon;
		this.width = width;
		this.height = height;
		setUndecorated(undecorated);
		ini();
	}
	
	/**
	 * 생성자
	 * @param title
	 * @param width
	 * @param height
	 * @param undecorated
	 */
	public MinFrame(String title, int width, int height, boolean undecorated) 
	{
		super();
		this.title = title;
		this.width = width;
		this.height = height;
		setUndecorated(undecorated);
		ini();
	}

	/**
	 * 화면에 반영되는 영역의 Panel 을 반환합니다.
	 * @return
	 */
	public MinPanel getWorkspace() 
	{
		return panelCenter;
	}

	/**
	 * 작업 영역을 설정합니다.
	 * @param comp
	 */
	public void setWorkspace(Component comp) 
	{
		if (this.workspace != null) 
		{
			panelCenter.remove(workspace);
		}
		this.workspace = comp;
		panelCenter.add(comp);
	}

	/**
	 * 기본 설정을 정의합니다.
	 */
	private void ini()
	{
		panelCenter = new MinPanel(0);
		add(panelCenter);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (icon != null) 
		{
			setIconImage(icon);
		}
		setTitle(title);
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void repaint() 
	{
		super.validate();
		super.repaint();
	}
}
