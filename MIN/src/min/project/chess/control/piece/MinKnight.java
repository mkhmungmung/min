package min.project.chess.control.piece;

import java.awt.Point;

import javax.swing.ImageIcon;

import min.project.chess.control.MinChessEngine;
import min.project.chess.img.MinChessImages;
import min.utils.MinUtils;

/**
 * 개요 : 나이트<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinKnight extends MinPiece
{
	private ImageIcon image;
	
	public MinKnight(String team, Point position, MinChessEngine chessEngine) 
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
				image = MinUtils.createImage(MinChessImages.BLACK_KNIGHT, 34, 68);
			}
			else if(isWhiteTeam())
			{
				image = MinUtils.createImage(MinChessImages.WHITE_KNIGHT, 34, 68);
			}
		}
		return image;
	}
	
	@Override
	public String getName()
	{
		return "KNIGHT";
	}
	
	@Override
	public Integer getScore() 
	{
		return MinChessEngine.SCORE_KNIGHT;
	}
	
}
