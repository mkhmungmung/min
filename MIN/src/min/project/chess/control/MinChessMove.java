package min.project.chess.control;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 개요 : 체스 기보, 시뮬레이션을 위한 이동 정보를 설정합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessMove
{
	private int x;
	private int y;
	private int moveX;
	private int moveY;
	private int minScore;
	private int maxScroe;
	private String promotionPieceInitials;
	private String pgn;
	private MinChessMove parents;
	private ArrayList<MinChessMove> nodes;
	
	/**
	 * 생성자
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param moveX - x 이동 좌표
	 * @param moveY - y 이동 좌표
	 * @param parents - 상위 시뮬레이션
	 */
	public MinChessMove(int x, int y, int moveX, int moveY, MinChessMove parents)
	{
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.parents = parents;
	}
	
	/**
	 * 생성자
	 * @param point - 좌표
	 * @param movePoint - 이동 좌표
	 * @param parents - 상위 시뮬레이션
	 */
	public MinChessMove(Point point, Point movePoint, MinChessMove parents)
	{
		this.x = point.x;
		this.y = point.y;
		this.moveX = movePoint.x;
		this.moveY = movePoint.y;
		this.parents = parents;
	}
	
	/**
	 * 생성자
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param moveX - x 이동 좌표
	 * @param moveY - y 이동 좌표
	 * @param parents - 상위 시뮬레이션
	 * @param promotionPieceInitials - 프로모션 Piece 이니셜
	 */
	public MinChessMove(int x, int y, int moveX, int moveY, MinChessMove parents, String promotionPieceInitials)
	{
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.parents = parents;
		this.promotionPieceInitials = promotionPieceInitials;
	}
	
	/**
	 * 생성자
	 * @param point - 좌표
	 * @param movePoint - 이동 좌표
	 * @param parents - 상위 시뮬레이션
	 * @param promotionPieceInitials - 프로모션 Piece 이니셜
	 */
	public MinChessMove(Point point, Point movePoint, MinChessMove parents, String promotionPieceInitials)
	{
		this.x = point.x;
		this.y = point.y;
		this.moveX = movePoint.x;
		this.moveY = movePoint.y;
		this.parents = parents;
		this.promotionPieceInitials = promotionPieceInitials;
	}
	
	/**
	 * x 좌표 반환
	 * @return
	 */
	public int getX() 
	{
		return x;
	}
	/**
	 * x 좌표 설정
	 * @param x
	 */
	public void setX(int x) 
	{
		this.x = x;
	}
	/**
	 * y 좌표 반환
	 * @return
	 */
	public int getY()
	{
		return y;
	}
	/**
	 * y 좌표 설정
	 * @param y
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	/**
	 * x 이동 좌표 반환
	 * @return
	 */
	public int getMoveX()
	{
		return moveX;
	}
	/**
	 * x 이동 좌표 설정
	 * @param moveX
	 */
	public void setMoveX(int moveX)
	{
		this.moveX = moveX;
	}
	/**
	 * y 이동 좌표 반환
	 * @return
	 */
	public int getMoveY() 
	{
		return moveY;
	}
	/**
	 * y 이동 좌표 설정
	 * @param moveY
	 */
	public void setMoveY(int moveY)
	{
		this.moveY = moveY;
	}
	/**
	 * 시뮬레이션 최소 점수 반환
	 * @return
	 */
	public int getMinScore()
	{
		return minScore;
	}
	/**
	 * 시뮬레이션 최소 점수 설정
	 * @param minScore
	 */
	public void setMinScore(int minScore)
	{
		this.minScore = minScore;
	}
	/**
	 * 시뮬레이션 최대 점수 반환
	 * @return
	 */
	public int getMaxScroe() 
	{
		return maxScroe;
	}
	/**
	 * 시뮬레이션 최대 점수 설정
	 * @param maxScroe
	 */
	public void setMaxScroe(int maxScroe)
	{
		this.maxScroe = maxScroe;
	}
	/**
	 * 상위 시뮬레이션 반환
	 * @return
	 */
	public MinChessMove getParents() 
	{
		return parents;
	}
	/**
	 * 상위 시뮬레이션 설정
	 * @param parents
	 */
	public void setParents(MinChessMove parents) 
	{
		this.parents = parents;
	}
	/**
	 * 하위 시뮬레이션 반환
	 * @return
	 */
	public ArrayList<MinChessMove> getNodes()
	{
		return nodes;
	}
	/**
	 * 하위 시뮬레이션 설정
	 * @param nodes
	 */
	public void setNodes(ArrayList<MinChessMove> nodes)
	{
		this.nodes = nodes;
	}
	/**
	 * 프로모션 Piece 이니셜 반환
	 * @return
	 */
	public String getPromotionPieceInitials() 
	{
		return promotionPieceInitials;
	}
	/**
	 * 프로모션 Piece 이니셜 설정 
	 * @param promotionPieceInitials
	 */
	public void setPromotionPieceInitials(String promotionPieceInitials) 
	{
		this.promotionPieceInitials = promotionPieceInitials;
	}
	/**
	 * 기보 정보 반환
	 * @return
	 */
	public String getPgn() 
	{
		return pgn;
	}
	/**
	 * 기보 정보 설정
	 * @param pgn
	 */
	public void setPgn(String pgn)
	{
		this.pgn = pgn;
	}
	
	
}
