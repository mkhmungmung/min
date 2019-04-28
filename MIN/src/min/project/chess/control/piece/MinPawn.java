package min.project.chess.control.piece;

import java.awt.Point;

import javax.swing.ImageIcon;

import min.project.chess.control.MinChessEngine;
import min.project.chess.img.MinChessImages;
import min.utils.MinUtils;

/**
 * 개요 : 폰<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinPawn extends MinPiece
{
	private ImageIcon image;
	
	public MinPawn(String team, Point position, MinChessEngine chessEngine) 
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
				image = MinUtils.createImage(MinChessImages.BLACK_PAWN, 34, 61);
			}
			else if(isWhiteTeam())
			{
				image = MinUtils.createImage(MinChessImages.WHITE_PAWN, 34, 61);
			}
		}
		return image;
	}
	
	@Override
	public String getName()
	{
		return "PAWN";
	}
	
	@Override
	public Integer getScore() 
	{
		return MinChessEngine.SCORE_PAWN;
	}
	
}
