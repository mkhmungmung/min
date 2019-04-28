package min.com;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * 개요 : 이미지 설정 가능한 Panel 을 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinImagePanel extends MinPanel
{
	private static final long serialVersionUID = 1L;

	private boolean autoResizeImage;
	private boolean autoFullsizeImage;
	
	private ArrayList<ImageIcon> imageIcons;
	private ArrayList<Point> pointList;
	private ArrayList<Boolean> centerPosition;
	
	/**
	 * 생성자
	 */
	public MinImagePanel()
	{
		super(5);
		
		ini();
	}
	
	/**
	 * 기본 설정
	 */
	private void ini()
	{
		imageIcons = new ArrayList<ImageIcon>();
		pointList = new ArrayList<Point>();
		centerPosition = new ArrayList<Boolean>();
		
		setOpaque(false);
		setFreezeCenter(true);
	}
	
	/**
	 * Panel 에 이미지가 포함되었는지 여부를 반환합니다.
	 * @param imageIcon
	 * @return
	 */
	public boolean containsImage(ImageIcon imageIcon)
	{
		return imageIcons.contains(imageIcon);
	}
	
	/**
	 * Panel 에 이미지 정보를 추가합니다.
	 * @param imageIcon
	 */
	public void addImage(ImageIcon imageIcon)
	{
		if(!imageIcons.contains(imageIcon))
		{
			imageIcons.add(imageIcon);
			pointList.add(new Point(-1, -1));
			centerPosition.add(true);
		}
	}
	
	/**
	 * Panel 에 이미지 정보를 추가합니다.
	 * @param index
	 * @param imageIcon
	 */
	public void addImage(int index, ImageIcon imageIcon)
	{
		if(!imageIcons.contains(imageIcon))
		{
			imageIcons.add(index, imageIcon);
			pointList.add(index, new Point(-1, -1));
			centerPosition.add(index, true);
		}
	}
	
	/**
	 * Panel 에 이미지 정보를 추가합니다.
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public void addImage(ImageIcon imageIcon, int x, int y)
	{
		if(!imageIcons.contains(imageIcon))
		{
			imageIcons.add(imageIcon);
			pointList.add(new Point(x, y));
			centerPosition.add(false);
		}
	}
	
	/**
	 * Panel 에 이미지 정보를 추가합니다.
	 * @param index
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public void addImage(int index, ImageIcon imageIcon, int x, int y)
	{
		if(!imageIcons.contains(imageIcon))
		{
			imageIcons.add(index, imageIcon);
			pointList.add(index, new Point(x, y));
			centerPosition.add(index, false);
		}
	}
	
	/**
	 * 이미지 정보를 layer 설정으로 이동시킵니다. 
	 * @param layer
	 * @param imageIcon
	 */
	public void moveLayerImage(int layer, ImageIcon imageIcon)
	{
		if(imageIcons.contains(imageIcon))
		{
			imageIcons.set(layer, imageIcon);
			pointList.set(layer, new Point(-1, -1));
			centerPosition.set(layer, true);
		}
	}
	
	/**
	 * 이미지 정보를 삭제합니다.
	 * @param imageIcon
	 */
	public void removeImage(ImageIcon imageIcon)
	{
		for(int i=0; i<imageIcons.size(); i++)
		{
			if(imageIcons.get(i).equals(imageIcon))
			{
				imageIcons.remove(i);
				pointList.remove(i);
				centerPosition.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 이미지 정보를 삭제합니다.
	 */
	public void removeImageAll()
	{
		imageIcons.clear();
		pointList.clear();
		centerPosition.clear();
	}
	
	/**
	 * 자동 이미지 조정 여부를 반환합니다.
	 * @return the autoResizeImage
	 */
	public boolean isAutoResizeImage() 
	{
		return autoResizeImage;
	}

	/**
	 * 자동 이미지 조정 여부를 설정합니다.
	 * @param autoResizeImage the autoResizeImage to set
	 */
	public void setAutoResizeImage(boolean autoResizeImage)
	{
		this.autoResizeImage = autoResizeImage;
	}
	
	/**
	 * position 에 해당하는 마지막 image icon 을 반환합니다.
	 * @param position
	 * @return
	 */
	public ImageIcon getImageIcon(Point position)
	{
		ImageIcon imageIcon = null;
		for(int i=0; i<imageIcons.size(); i++)
		{
			int x = pointList.get(i).x;
			int y = pointList.get(i).y;
			if(imageIcons.get(i) != null)
			{
				if(position.x >= x && position.x <= x+imageIcons.get(i).getIconWidth())
				{
					if(position.y >= y && position.y <= y+imageIcons.get(i).getIconHeight())
					{
						imageIcon = imageIcons.get(i);
					}
				}
			}
		}
		return imageIcon;
	}
	
	/**
	 * position 에 해당하는 image icon 리스트를 반환합니다.
	 * @param position
	 * @return
	 */
	public ArrayList<ImageIcon> getImageIcons(Point position)
	{
		ArrayList<ImageIcon> containImageIcons = new ArrayList<ImageIcon>();
		for(int i=0; i<imageIcons.size(); i++)
		{
			int x = pointList.get(i).x;
			int y = pointList.get(i).y;
			if(imageIcons.get(i) != null)
			{
				if(position.x >= x && position.x <= x+imageIcons.get(i).getIconWidth())
				{
					if(position.y >= y && position.y <= y+imageIcons.get(i).getIconHeight())
					{
						containImageIcons.add(imageIcons.get(i));
					}
				}
			}
		}
		return containImageIcons;
	}
	
	/**
	 * position 에 해당하는 image icon 리스트를 반환합니다.
	 * @param position
	 * @return
	 */
	public ArrayList<ImageIcon> getImageIcons()
	{
		return imageIcons;
	}

	@Override
	public void paint(Graphics g) 
	{
		for(int i=0; i<imageIcons.size(); i++)
		{
			ImageIcon imageIcon = null;
			Point point = pointList.get(i);
			if(imageIcons.get(i) != null)
			{
				if(autoFullsizeImage)
				{
					if(getSize().width < imageIcons.get(i).getIconWidth() || getSize().height < imageIcons.get(i).getIconHeight())
					{
						imageIcon = new ImageIcon(imageIcons.get(i).getImage().getScaledInstance(getSize().width-10, getSize().height-10, Image.SCALE_SMOOTH));
					}
					else
					{
						imageIcon = imageIcons.get(i);
					}
				}
				else if(autoResizeImage)
				{
					double resizeRate = 1d;
					if(getSize().width -10 < imageIcons.get(i).getIconWidth())
					{
						resizeRate = (double)(getSize().width)/imageIcons.get(i).getIconWidth();
					}
					if(getSize().height -10 < imageIcons.get(i).getIconHeight())
					{
						if(resizeRate > (double)(getSize().height)/imageIcons.get(i).getIconHeight())
						{
							resizeRate = (double)(getSize().height)/imageIcons.get(i).getIconHeight();
						}
					}
					if(resizeRate != 1d)
					{
						int width = (int)((imageIcons.get(i).getIconWidth()-10)*resizeRate)-10;
						int height = (int)((imageIcons.get(i).getIconHeight()-10)*resizeRate)-10;
						imageIcon = new ImageIcon(imageIcons.get(i).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
					}
					else
					{
						imageIcon = imageIcons.get(i);
					}
				}
				else 
				{
					imageIcon = imageIcons.get(i);
				}
			}
			if(imageIcon != null)
			{
				if(centerPosition.get(i))
				{
					imageIcon.paintIcon(this, g, (getSize().width - imageIcon.getIconWidth())/2, (getSize().height - imageIcon.getIconHeight())/2);
				}
				else
				{
					imageIcon.paintIcon(this, g, point.x, point.y);
				}
			}
		}
		super.paint(g);
	}
}
