package min.project.chess.control.piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import min.project.chess.MinChessAppMain;
import min.project.chess.control.MinChessEngine;

/**
 * 개요 : 체스 말<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
abstract public class MinPiece
{
	public static String WHITE_TEAM = "WHITE";
	public static String BLACK_TEAM = "BLACK";
	
	private MinChessEngine chessEngine;
	
	private HashMap<String, MinPiece> whitePieceMap;
	private HashMap<String, MinPiece> blackPieceMap;
	
	private StringBuffer log;
	
	private String team;
	
	private Point position;
	
	private boolean alive;
	private boolean selected;
	
	abstract public ImageIcon getImage();
	abstract public String getName();
	abstract public Integer getScore();
	
	/**
	 * 생성자
	 * @param team : WHITE, BLACK
	 * @param position : 말의 위치
	 * @param chessEngine : 체스 말의 이동, 점수, Play 등을 관리하는 엔진
	 */
	public MinPiece(String team, Point position, MinChessEngine chessEngine)
	{
		this.team = team;
		this.position = position;
		this.alive = true;
		this.selected = false;
		this.chessEngine = chessEngine;
	}
	
	/**
	 * 위치 정보를 합성해 만든 문자열을 다시 위치 정보로 반환합니다.
	 * @param positionStr
	 * @return
	 */
	public Point getPosition(String positionStr)
	{
		String [] positionStrSplit = positionStr.split("_");
		if(positionStrSplit != null && positionStrSplit.length == 2)
		{
			try
			{
				int x = Integer.valueOf(positionStrSplit[1]);
				int y = Integer.valueOf(positionStrSplit[0]);
				return new Point(x, y);
			}
			catch(Exception e) {}
		}
		return null;
	}
	
	/**
	 * 말의 위치를 이동하고 이동 여부를 반환합니다.<br>
	 * Castling 가능한 이동일 경우 Castling 여부를 확인하고 Castling 처리해줍니다.
	 * @param movePoint - 이동 위치
	 * @return
	 */
	public boolean movePosition(Point movePoint)
	{
		// 이동하기 전에 체크 여부를 확인합니다. 체크인 경우엔 경고문구를 보여주고 이동 처리는 하지 않습니다.
		if(chessEngine.isCheck(this, movePoint))
		{
			JOptionPane.showMessageDialog(null, "체크입니다.");
			return false;
		}
		// 좌측 Castling 여부를 확인 후 처리합니다.
		else if(chessEngine.isCastlingLeft(this, movePoint))
		{
			return castlingLeft(movePoint);
		}
		// 우측 Castling 여부를 확인 후 처리합니다.
		else if(chessEngine.isCastlingRight(this, movePoint))
		{
			return castlingRight(movePoint);
		}
		// 기본 이동 정보를 처리합니다.
		else
		{
			movePositionPiece(movePoint);
			return true;
		}
	}
	
	/**
	 * Left Castling 을 처리합니다.
	 * @param movePoint
	 * @return
	 */
	public boolean castlingLeft(Point movePoint)
	{
		// yes : Castling 처리, no : 일반 이동, cancel : 이동 취소
		int option = JOptionPane.showConfirmDialog(MinChessAppMain.getInstance(), "Castling 하시겠습니까?", "", JOptionPane.YES_NO_CANCEL_OPTION);
		if(option == JOptionPane.YES_OPTION)
		{
			moveCastlingLeftPositionPiece(movePoint);
			return true;
		}
		else if(option == JOptionPane.NO_OPTION)
		{
			// Castling 하지 않을 경우 Rook 은 이동이 가능합니다.
			if(getScore() != MinChessEngine.SCORE_KING)
			{
				movePositionPiece(movePoint);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Right Castling 을 처리합니다.
	 * @param movePoint
	 * @return
	 */
	public boolean castlingRight(Point movePoint)
	{
		// yes : Castling 처리, no : 일반 이동, cancel : 이동 취소
		int option = JOptionPane.showConfirmDialog(MinChessAppMain.getInstance(), "Castling 하시겠습니까?", "", JOptionPane.YES_NO_CANCEL_OPTION);
		if(option == JOptionPane.YES_OPTION)
		{
			moveCastlingRightPositionPiece(movePoint);
			return true;
		}
		else if(option == JOptionPane.NO_OPTION)
		{
			// Castling 하지 않을 경우 Rook 은 이동이 가능합니다.
			if(getScore() != MinChessEngine.SCORE_KING)
			{
				movePositionPiece(movePoint);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 좌측 Castling 처리하여 말의 위치를 이동합니다.<br>
	 * @param movePoint - 이동 위치
	 */
	public void moveCastlingLeftPositionPiece(Point movePoint)
	{
		// King 을 이동하여 Castling 합니다.
		int castlingPieceKingX = 4;
		int castlingPieceKingY = getPositionY();
		// Castling 하기 위해 King 의 위치를 이동시킵니다.
		if(isBlackTeam())
		{
			MinPiece castlingRook = blackPieceMap.get(castlingPieceKingY + "_" + castlingPieceKingX);
			castlingRook.movePositionPiece(new Point(2, castlingPieceKingY));
		}
		else if(isWhiteTeam())
		{
			MinPiece castlingRook = whitePieceMap.get(castlingPieceKingY + "_" + castlingPieceKingX);
			castlingRook.movePositionPiece(new Point(2, castlingPieceKingY));
		}
		
		// Rook 을 이동하여 Castling 합니다.
		int castlingPieceRookX = 0;
		int castlingPieceRookY = getPositionY();
		// Castling 하기 위해 Rook 의 위치를 이동시킵니다.
		if(isBlackTeam())
		{
			MinPiece castlingRook = blackPieceMap.get(castlingPieceRookY + "_" + castlingPieceRookX);
			castlingRook.movePositionPiece(new Point(3, castlingPieceRookY));
		}
		else if(isWhiteTeam())
		{
			MinPiece castlingRook = whitePieceMap.get(castlingPieceRookY + "_" + castlingPieceRookX);
			castlingRook.movePositionPiece(new Point(3, castlingPieceRookY));
		}
	}
	
	/**
	 * 우측 Castling 처리하여 말의 위치를 이동합니다.<br>
	 * @param movePoint - 이동 위치
	 */
	public void moveCastlingRightPositionPiece(Point movePoint)
	{
		// King 을 이동하여 Castling 합니다.
		int castlingPieceKingX = 4;
		int castlingPieceKingY = getPositionY();
		// Castling 하기 위해 King 의 위치를 이동시킵니다.
		if(isBlackTeam())
		{
			MinPiece castlingKing = blackPieceMap.get(castlingPieceKingY + "_" + castlingPieceKingX);
			castlingKing.movePositionPiece(new Point(6, castlingPieceKingY));
		}
		else if(isWhiteTeam())
		{
			MinPiece castlingKing = whitePieceMap.get(castlingPieceKingY + "_" + castlingPieceKingX);
			castlingKing.movePositionPiece(new Point(6, castlingPieceKingY));
		}
		
		// Rook 을 이동하여 Castling 합니다.
		int castlingPieceRookX = 7;
		int castlingPieceRookY = getPositionY();
		// Castling 하기 위해 Rook 의 위치를 이동시킵니다.
		if(isBlackTeam())
		{
			MinPiece castlingRook = blackPieceMap.get(castlingPieceRookY + "_" + castlingPieceRookX);
			castlingRook.movePositionPiece(new Point(5, castlingPieceRookY));
		}
		else if(isWhiteTeam())
		{
			MinPiece castlingRook = whitePieceMap.get(castlingPieceRookY + "_" + castlingPieceRookX);
			castlingRook.movePositionPiece(new Point(5, castlingPieceRookY));
		}
	}
	
	/**
	 * 말의 위치를 이동합니다.<br>
	 * 로그 파일에 이동 정보를 저장합니다.<br>
	 * 이동 위치에 적팀의 말이 있을 경우 해당 말을 제거합니다.
	 * @param movePoint - 이동 위치
	 */
	public void movePositionPiece(Point movePoint)
	{
		try
		{
			// 로그 정보에서 몇번째 턴인지 확인합니다.
			// 캐슬링 정보는 1턴으로 진행하기 때문에 이전 턴이 상대팀인지도 확인합니다.
			ArrayList<String> turnString = new ArrayList<String>();
			String [] logLineSplits = log.toString().split("\n");
			if(logLineSplits != null && logLineSplits.length>0)
			{
				for(String logLine : logLineSplits)
				{
					String [] logColumnSplit = logLine.split(" ");
					if(logColumnSplit != null && logColumnSplit.length > 2)
					{
						if(logColumnSplit[0].contains(".WHITE"))
						{
							if(turnString.size() > 1)
							{
								if(turnString.get(turnString.size()-1).equals("BLACK"))
								{
									turnString.add("WHITE");
								}
							}
							else
							{
								turnString.add("WHITE");
							}
						}
						else if(logColumnSplit[0].contains(".BLACK"))
						{
							if(turnString.get(turnString.size()-1).equals("WHITE"))
							{
								turnString.add("BLACK");
							}
						}
					}
				}
				if(turnString.size() == 0 || !turnString.get(turnString.size()-1).equals(getTeam()))
				{
					turnString.add(getTeam());
				}
			}
			log.append((turnString.size()) + "." + getTeam() + " " + getName() + " 이동\n");
			if(isBlackTeam())
			{
				log.append("[" + (char)(position.x+'a')+(8-position.y)+"] >> " + "["+(char)(movePoint.x+'a')+(8-movePoint.y)+"]\n");
				blackPieceMap.remove(getPositionStr());
				position.setLocation(movePoint.x, movePoint.y);
				blackPieceMap.put(getPositionStr(), this);
				if(whitePieceMap.containsKey(getPositionStr()))
				{
					MinPiece enemy = whitePieceMap.get(getPositionStr());
					log.append(enemy.getName() + "[" + (char)(enemy.getPositionX()+'a')+(8-enemy.getPositionY())+"] 을 잡았습니다.\n");
					enemy.setAlive(false);
					whitePieceMap.remove(enemy.getPositionStr());
					whitePieceMap.put("-1_-1_" + System.currentTimeMillis(), enemy);
				}
			}
			else if(isWhiteTeam())
			{
				log.append("[" + (char)(position.x+'a')+(8-position.y)+"] >> " + "["+(char)(movePoint.x+'a')+(8-movePoint.y)+"]\n");
				whitePieceMap.remove(getPositionStr());
				position.setLocation(movePoint.x, movePoint.y);
				whitePieceMap.put(getPositionStr(), this);
				if(blackPieceMap.containsKey(getPositionStr()))
				{
					MinPiece enemy = blackPieceMap.get(getPositionStr());
					log.append(enemy.getName() + "[" + (char)(enemy.getPositionX()+'a')+(8-enemy.getPositionY())+"] 을 잡았습니다.\n");
					enemy.setAlive(false);
					blackPieceMap.remove(enemy.getPositionStr());
					blackPieceMap.put("-1_-1_" + System.currentTimeMillis(), enemy);
				}
			}
			MinChessAppMain.getWorkspace().initLog();
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * 이동 가능한지 여부를 반환합니다.
	 * @param movePoint - 이동 위치
	 * @return
	 */
	public boolean isMovable(Point movePoint)
	{
		for(Point movablePoint : chessEngine.getMovablePositions(chessEngine.getPiece(), position.x, position.y))
		{
			if(movablePoint.x == movePoint.x && movablePoint.y == movePoint.y)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Board 안의 위치 정보인지 여부를 반환합니다.
	 * @param movePoint - 이동 위치
	 * @return
	 */
	public boolean positionInBoard(Point movePoint)
	{
		if(movePoint != null)
		{
			return movePoint.x > 0 && movePoint.y > 0 && movePoint.x < 9 && movePoint.y < 9;
		}
		return false;
	}
	
	/**
	 * 백색 팀 여부를 반환합니다.
	 * @return
	 */
	public boolean isWhiteTeam()
	{
		return WHITE_TEAM.equals(team);
	}
	
	/**
	 * 흙색 팀 여부를 반환합니다.
	 * @return
	 */
	public boolean isBlackTeam()
	{
		return BLACK_TEAM.equals(team);
	}
	
	/**
	 * 팀 이름을 반환합니다.
	 * @return
	 */
	public String getTeam()
	{
		return team;
	}

	/**
	 * 팀 이름을 설정합니다.
	 * @param team
	 */
	public void setTeam(String team)
	{
		this.team = team;
	}

	/**
	 * x 축 위치 정보를 반환합니다.
	 * @return
	 */
	public int getPositionX() 
	{
		return position.x;
	}

	/**
	 * y 축 위치 정보를 반환합니다.
	 * @return
	 */
	public int getPositionY()
	{
		return position.y;
	}
	
	/**
	 * 위치 정보를 합성한 문자열을 반환합니다.<br>
	 * 각 팀의 말 정보를 확인하기 위한 key 로 사용되며<br>
	 * 말 이미지의 표현 시 layer 를 설정하기 위한 정열에도 사용됩니다.<br>
	 * 정열 시 y 축에 따라 상위에 표시되어야 하기 때문에 y 축 기준으로 먼저 설정합니다.
	 * @return
	 */
	public String getPositionStr()
	{
		String positionStr = null;
		if(position != null)
		{
			positionStr = position.y + "_" + position.x;
		}
		return positionStr;
	}
	
	/**
	 * 위치 정보를 반환합니다.
	 * @return
	 */
	public Point getPosition()
	{
		return position;
	}
	
	/**
	 * 백색 팀 말 정보를 반환합니다.
	 * @return
	 */
	public HashMap<String, MinPiece> getWhitePieceMap()
	{
		return whitePieceMap;
	}

	/**
	 * 백색 팀 말 정보를 설정합니다.
	 * @param whitePieceMap
	 */
	public void setWhitePieceMap(HashMap<String, MinPiece> whitePieceMap)
	{
		this.whitePieceMap = whitePieceMap;
	}

	/**
	 * 흙색 팀 말 정보를 반환합니다.
	 * @return
	 */
	public HashMap<String, MinPiece> getBlackPieceMap()
	{
		return blackPieceMap;
	}

	/**
	 * 흙색 팀 말 정보를 설정합니다.
	 * @param blackPieceMap
	 */
	public void setBlackPieceMap(HashMap<String, MinPiece> blackPieceMap)
	{
		this.blackPieceMap = blackPieceMap;
	}

	/**
	 * 말이 살아있는지 여부를 반환합니다.
	 * @return
	 */
	public boolean isAlive() 
	{
		return alive;
	}

	/**
	 * 말이 살아있는지 여부를 설정합니다.
	 * @param alive
	 */
	public void setAlive(boolean alive) 
	{
		this.alive = alive;
	}
	
	/**
	 * 말이 선택되었는지 여부를 반환합니다.
	 * @return
	 */
	public boolean isSelected() 
	{
		return selected;
	}
	
	/**
	 * 말이 선택되었는지 여부를 설정합니다.
	 * @param selected
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	/**
	 * Chess Engine 를 반환합니다.
	 * @return
	 */
	public MinChessEngine getChessEngine()
	{
		return chessEngine;
	}
	
	/**
	 * 로그 문자열을 반환합니다.
	 * @return
	 */
	public StringBuffer getLog() 
	{
		return log;
	}
	
	/**
	 * 로그 문자열을 설정합니다.
	 * @param log
	 */
	public void setLog(StringBuffer log)
	{
		this.log = log;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof MinPiece)
		{
			MinPiece piece = (MinPiece)obj;
			if(this.getTeam().equals(piece.getTeam()))
			{
				if(this.getName().equals(piece.getName()))
				{
					if(this.isAlive() == piece.isAlive())
					{
						if(this.getPositionX() == piece.getPositionX() && this.getPositionY() == piece.getPositionY())
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
