package min.img;

import java.net.URL;

import javax.swing.ImageIcon;

import min.utils.MinUtils;

/**
 * 개요 : 이미지를 관리합니다.<br>
 * 작성일 : 2014. 7. 31.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinImages 
{
	public static URL NEXT = MinImages.class.getResource("Next.png");
	public static URL NEXT_NEXT = MinImages.class.getResource("NextNext.png");
	public static URL PREV = MinImages.class.getResource("Prev.png");
	public static URL PREV_PREV = MinImages.class.getResource("PrevPrev.png");
	
	public static ImageIcon IMAGE_NEXT = MinUtils.createImage(NEXT);
	public static ImageIcon IMAGE_NEXT_NEXT = MinUtils.createImage(NEXT_NEXT);
	public static ImageIcon IMAGE_PREV = MinUtils.createImage(PREV);
	public static ImageIcon IMAGE_PREV_PREV = MinUtils.createImage(PREV_PREV);
}
