package min.utils;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * 개요 : 자주 사용하는 유용한 기능을 정의합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinUtils 
{
	/**
	 * white color 을 반환합니다.
	 */
	public static Color FRAME_BG = Color.white;
	
	public static ImageIcon createImage(URL url)
	{
		try
		{
			return new ImageIcon(url);
		}
		catch(Exception e) {}
		return null;
	}
	
	public static ImageIcon createImage(URL url, int width, int height)
	{
		try
		{
			return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
		catch(Exception e) {}
		return null;
	}
}
