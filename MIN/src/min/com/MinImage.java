package min.com;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 개요 : BufferedImage 에 Transferable 이 implement 된 이미지를 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinImage extends BufferedImage implements Transferable
{
	/**
	 * 생성자 : BufferedImage 를 받아서 데이터를 설정합니다.
	 * @param image
	 */
	public MinImage(BufferedImage image)
	{
		super(image.getWidth(), image.getHeight(), image.getType());
		setData(image.getData());
	}
	@Override
	public DataFlavor[] getTransferDataFlavors() 
	{
		return new DataFlavor[] {DataFlavor.imageFlavor};
	}
	@Override
	public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
	{
		return DataFlavor.imageFlavor.equals(paramDataFlavor);
	}
	@Override
	public Object getTransferData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException, IOException
	{
		return this;
	}
}
