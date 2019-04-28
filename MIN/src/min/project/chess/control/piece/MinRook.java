package min.project.chess.control.piece;

import java.awt.Point;

import javax.swing.ImageIcon;

import min.project.chess.control.MinChessEngine;
import min.project.chess.img.MinChessImages;
import min.utils.MinUtils;

/**
 * 개요 : 룩<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinRook extends MinPiece
{
	private ImageIcon image;
	
	public MinRook(String team, Point position, MinChessEngine chessEngine) 
	{
		super(team, position, chessEngine);
	}
	
	@Override
	public ImageIcon getImage() 
	{
		if(image == null)
		{
			if(isBlackTeam())
			{
				image = MinUtils.createImage(MinChessImages.BLACK_ROOK, 31, 64);
			}
			else if(isWhiteTeam())
			{
				image = MinUtils.createImage(MinChessImages.WHITE_ROOK, 31, 64);
			}
		}
		return image;
	}
	
	@Override
	public String getName()
	{
		return "ROOK";
	}
	
	@Override
	public Integer getScore() 
	{
		return MinChessEngine.SCORE_ROOK;
	}
	
}
