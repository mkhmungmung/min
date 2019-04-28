package min.com;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 개요 : 회오리 모양으로 중심부터 component 를 배치하는 Layout 입니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinTornadoLayout implements LayoutManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 배치 순서에 따른 위치 정보
	 */
	private HashMap<Integer, Point> locationMap;
	
	/**
	 * 시계 방향 배치 여부
	 */
	private boolean isClockwise;
	
	/**
	 * 가로 Gap
	 */
	private int hgap;
	/**
	 * 세로 Gap
	 */
	private int vgap;
	
	/**
	 * 생성자
	 */
	public MinTornadoLayout()
	{
		isClockwise = true;
	}
	
	/**
	 * 생성자
	 * @param hgap : 가로 gap 을 설정합니다.
	 * @param vgap : 세로 gap 을 설정합니다.
	 */
	public MinTornadoLayout(int hgap, int vgap)
	{
		isClockwise = true;
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	/**
	 * 생성자
	 * @param isClockwise : 시계 방향으로 배치할지 여부를 설정합니다.
	 */
	public MinTornadoLayout(boolean isClockwise)
	{
		this.isClockwise = isClockwise;
	}
	
	/**
	 *  * 생성자
	 * @param isClockwise : 시계 방향으로 배치할지 여부를 설정합니다.
	 * @param hgap : 가로 gap 을 설정합니다.
	 * @param vgap : 세로 gap 을 설정합니다.
	 */
	public MinTornadoLayout(boolean isClockwise, int hgap, int vgap)
	{
		this.isClockwise = isClockwise;
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	@Override
	public void layoutContainer(Container paramContainer) 
	{
		synchronized (paramContainer.getTreeLock()) 
		{
			locationMap = new HashMap<Integer, Point>();
			
			int componentCnt = paramContainer.getComponentCount();
			int containerWidth = paramContainer.getWidth();
			int containerHeight = paramContainer.getHeight();
			int width = 0;						// component 넓이
			int height = 0;						// component 높이
			int horizontalCnt = 1;				// component 가로 배치 개수
			int verticalCnt = 1;					// component 세로 배치 개수
			int horizontalIncrease = 0;		// component 가로 배치 증가량
			int verticalIncrease = 0;			// component 세로 배치 증가량
			int horizontalIncreaseIndex = 2;	// component 가로 배치 증가량 index
			int verticalIncreaseIndex = 1;		// component 세로 배치 증가량 index
			int locationIndex = 0;				// component 배치 순서
			
			// 가로 축 정보를 계산합니다.
			while(horizontalIncrease < componentCnt)
			{
				horizontalCnt = horizontalIncreaseIndex/2;
				horizontalIncrease += horizontalIncreaseIndex;
				horizontalIncreaseIndex += 2;
			}
			
			// 세로 축 정보를 계산합니다.
			while(verticalIncrease < componentCnt)
			{
				verticalCnt = verticalIncreaseIndex/2 + 1;
				verticalIncrease += verticalIncreaseIndex;
				verticalIncreaseIndex += 2;
			}
			
			// component 의 가로 세로 값을 계산합니다.
			width = containerWidth/horizontalCnt - (hgap*2);
			height = containerHeight/verticalCnt - (vgap*2);
			
			// 배치 순서의 최대치를 계산합니다.
			locationIndex = horizontalCnt * verticalCnt;
			
			int x = 0;
			int y = 0;
			if(isClockwise)
			{
				// 세로 영역을 처리할 때 방향성 + 처리를 먼저 하기 때문에 -1부터 시작합니다.
				y = -1;
			}
			else
			{
				// 가로 영역을 처리할 때 방향성 + 처리를 먼저 하기 때문에 -1부터 시작합니다.
				x = -1;
			}
			int xDirection = 1;		// x 축 증가/감소 처리 방향을 계산하는데 사용됩니다.
			int yDirection = 1;		// y 축 증가/감소 처리 방향을 계산하는데 사용됩니다.
			
			if(isClockwise)
			{
				// 가로 영역은 세로 영역을 먼저 처리하면서 1개가 감소되기 때문에 미리 감소처리 합니다.
				horizontalCnt--;
			}
			else
			{
				// 세로 영역은 가로 영역을 먼저 처리하면서 1개가 감소되기 때문에 미리 감소처리 합니다.
				verticalCnt--;
			}
			// 모든 영역의 배치 순서에 따른 위치 정보를 설정합니다.
			while(locationIndex > 0)
			{
				if(isClockwise)
				{
					// 세로 영역 설정
					for(int i=0; i<verticalCnt; i++)
					{
						y = y + yDirection;
						locationMap.put(locationIndex-1, new Point(x*(width+hgap*2)+hgap, y*(height+vgap*2)+vgap));
						locationIndex--;
					}
					yDirection = -yDirection;
					verticalCnt--;
					// 가로 영역 설정
					for(int i=0; i<horizontalCnt; i++)
					{
						x = x + xDirection;
						locationMap.put(locationIndex-1, new Point(x*(width+hgap*2)+hgap, y*(height+vgap*2)+vgap));
						locationIndex--;
					}
					xDirection = -xDirection;
					horizontalCnt--;
				}
				else
				{
					// 가로 영역 설정
					for(int i=0; i<horizontalCnt; i++)
					{
						x = x + xDirection;
						locationMap.put(locationIndex-1, new Point(x*(width+hgap*2)+hgap, y*(height+vgap*2)+vgap));
						locationIndex--;
					}
					xDirection = -xDirection;
					horizontalCnt--;
					// 세로 영역 설정
					for(int i=0; i<verticalCnt; i++)
					{
						y = y + yDirection;
						locationMap.put(locationIndex-1, new Point(x*(width+hgap*2)+hgap, y*(height+vgap*2)+vgap));
						locationIndex--;
					}
					yDirection = -yDirection;
					verticalCnt--;
				}
			}
			// component 순서에 따라서 해당 영역의 배치 위치를 설정합니다.
			for(int i=0; i<componentCnt; i++)
			{
				Component comp = paramContainer.getComponent(i);
				comp.setSize(new Dimension(width, height));
				comp.setLocation(locationMap.get(i));
			}
		}
	}

	@Override
	public void addLayoutComponent(String paramString, Component paramComponent) {}

	@Override
	public void removeLayoutComponent(Component paramComponent) {}

	@Override
	public Dimension preferredLayoutSize(Container paramContainer)
	{
		synchronized (paramContainer.getTreeLock())
		{
			int componentCnt = paramContainer.getComponentCount();
			int horizontalCnt = 1;					// component 가로 배치 개수
			int verticalCnt = 1;						// component 세로 배치 개수
			int horizontalIncrease = 0;			// component 가로 배치 증가량
			int verticalIncrease = 0;				// component 세로 배치 증가량
			int horizontalIncreaseIndex = 2;		// component 가로 배치 증가량 index
			int verticalIncreaseIndex = 1;			// component 세로 배치 증가량 index
			
			// 가로 축 정보를 계산합니다.
			while(horizontalIncrease < componentCnt)
			{
				horizontalCnt = horizontalIncreaseIndex/2;
				horizontalIncrease += horizontalIncreaseIndex;
				horizontalIncreaseIndex += 2;
			}
			
			// 세로 축 정보를 계산합니다.
			while(verticalIncrease < componentCnt)
			{
				verticalCnt = verticalIncreaseIndex/2 + 1;
				verticalIncrease += verticalIncreaseIndex;
				verticalIncreaseIndex += 2;
			}
			
			// 기본 사이즈는 component 당 50*50 으로 설정합니다.
			Dimension localDimension1 = new Dimension(horizontalCnt*50, verticalCnt*50);
			return localDimension1;
	    }
	}

	@Override
	public Dimension minimumLayoutSize(Container paramContainer)
	{
		synchronized (paramContainer.getTreeLock())
		{
			Dimension localDimension1 = new Dimension(10, 10);
			return localDimension1;
	    }
	}
}
