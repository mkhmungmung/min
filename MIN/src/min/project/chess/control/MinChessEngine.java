package min.project.chess.control;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import min.project.chess.MinChessAppMain;
import min.project.chess.control.piece.MinBishop;
import min.project.chess.control.piece.MinKnight;
import min.project.chess.control.piece.MinPawn;
import min.project.chess.control.piece.MinPiece;
import min.project.chess.control.piece.MinQueen;
import min.project.chess.control.piece.MinRook;
import min.project.chess.data.MinChessConfig;

/**
 * 개요 : 체스 엔진<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessEngine 
{
	public static int SCORE_KING = 390;
	public static int SCORE_QUEEN = 90;
	public static int SCORE_ROOK = 50;
	public static int SCORE_BISHOP = 40;
	public static int SCORE_KNIGHT = 30;
	public static int SCORE_PAWN = 10;
	
	private HashMap<String, MinPiece> whitePieceMap;
	private HashMap<String, MinPiece> blackPieceMap; 
	
	// 시뮬레이션 경우의 수를 확인합니다.
	private int simulationCnt;
	
	/**
	 * 생성자
	 * @param whitePieceMap - 흰색 팀
	 * @param blackPieceMap - 흙색 팀
	 */
	public MinChessEngine(HashMap<String, MinPiece> whitePieceMap, HashMap<String, MinPiece> blackPieceMap) 
	{
		this.whitePieceMap = whitePieceMap;
		this.blackPieceMap = blackPieceMap;
	}
	
	/**
	 * 흙색 팀 Play
	 */
	public void playChessEngine()
	{
		long simulationStart = System.currentTimeMillis();
		simulationCnt = 0;
		int [][] pieces = getPiece();
		ArrayList<MinChessMove> moves = new ArrayList<MinChessMove>();
		// 흙색 팀이 처음 이동 가능한 경우의 수를 설정합니다.
		for(MinPiece blackPiece : blackPieceMap.values())
		{
			if(blackPiece.isAlive())
			{
				for(Point movePoint : getMovablePositions(pieces, blackPiece.getPositionX(), blackPiece.getPositionY()))
				{
					simulationCnt++;
					MinChessMove move = new MinChessMove(blackPiece.getPosition(), movePoint, null);
					moves.add(move);
				}
			}
		}
		System.out.println("1 회 시뮬레이션 경우의 수 : " + simulationCnt);
		/**
		 * 흰색 팀과 흙색 팀의 경우의 수를 번갈아 가면서 시뮬레이션 합니다.
		 */
		int attackDirection = -1;
		for(int i=0; i<MinChessConfig.getChessEngineLevel()-1; i++)
		{
			simulator(pieces, moves, attackDirection);
			attackDirection = - attackDirection;
			System.out.println((i+2) + " 회 시뮬레이션 경우의 수 : " + simulationCnt);
		}
		ArrayList<MinChessMove> selectedMovePieces = new ArrayList<MinChessMove>();
		// 시뮬레이션 결과 가장 높은 좀수와 낮은 점수를 체크합니다.
		// 점수가 높을 수록 컴퓨터에게 불리한 수 이므로 가장 낮은 수를 찾아서 선택합니다.
		System.out.println("--------------- 시뮬레이션 결과 ---------------");
		for(MinChessMove move : moves)
		{
			System.out.println("시뮬레이션 결과 점수 : " + (char)(move.getX()+'a')+""+(8-move.getY())+" >> " +(char)(move.getMoveX()+'a')+""+(8-move.getMoveY()) + " : min(" + move.getMinScore() + "), max(" + move.getMaxScroe() + ")");
			if(selectedMovePieces.size() == 0)
			{
				selectedMovePieces.add(move);
			}
			else
			{
				MinChessMove selectPiece = selectedMovePieces.get(0);
				// 최대 점수가 낮은 경우를 먼저 확인합니다.
				if(move.getMaxScroe() < selectPiece.getMaxScroe())
				{
					while(selectedMovePieces.size()>0)
					{
						selectedMovePieces.remove(0);
					}
					selectedMovePieces.add(move);
				}
				// 최대 점수가 동일할 경우 최소 점수가 낮은 경우를 확인합니다.
				else if(move.getMaxScroe() == selectPiece.getMaxScroe() && move.getMinScore() < selectPiece.getMinScore())
				{
					while(selectedMovePieces.size()>0)
					{
						selectedMovePieces.remove(0);
					}
					selectedMovePieces.add(move);
				}
				// 최대 점수와 최소 점수가 동일한 경우 리스트에 추가합니다.
				else if(move.getMaxScroe() == selectPiece.getMaxScroe() && move.getMinScore() == selectPiece.getMinScore())
				{
					selectedMovePieces.add(move);
				}
			}
		}
		System.out.println("시뮬레이션 소요 시간 : " + (System.currentTimeMillis()-simulationStart) + " ms");
		
		// 동일한 score 경우의 수가 여러개일 경우 랜덤하게 처리합니다.
		MinChessMove movePiece = selectedMovePieces.get((int)(Math.random()*selectedMovePieces.size()));
		
		// 흙색 팀의 말을 이동시킵니다.
		MinPiece blackPiece = blackPieceMap.get(movePiece.getY() + "_" + movePiece.getX());
		boolean movable = blackPiece.movePosition(new Point(movePiece.getMoveX(), movePiece.getMoveY()));
		if(movable)
		{
			MinChessAppMain.getWorkspace().repaintPiece();
		}
	}
	
	/**
	 * 판에 있는 말의 정보를 이용하여 이동 가능한 경우를 시뮬레이션 합니다.
	 * @param pieces - 말의 위치에 해당하는 배열 구조에 해당 말 점수를 저장
	 * @param moves - 이동 가능한 경우의 시뮬레이션 리스트
	 * @param attackDirection - 공격 방향 (1 : 흙색 공격, -1 : 흰색 공격)
	 */
	private void simulator(int [][] pieces, ArrayList<MinChessMove> moves, int attackDirection)
	{
		// 이동 가능한 경우의 수만큼 반복합니다.
		for(MinChessMove move : moves)
		{
			// 최 하위 노드인 경우에만 시뮬레이션 합니다.
			if(move.getNodes() != null && move.getNodes().size() > 0)
			{
				simulator(pieces, move.getNodes(), attackDirection);
			}
			else
			{
				MinChessMove parents = move.getParents();
				
				// 시뮬레이션 진행 결과 piece 정보를 설정합니다.
				int [][] simulationPieces = getSimulationPiece(pieces, move);
				
				// 시뮬레이션 진행 결과 위치 정보를 기반으로 모든 말이 이동할 수 있는 경우의 수를 확인합니다.
				ArrayList<MinChessMove> moveNodes = new ArrayList<MinChessMove>();
				for(int x=0; x<8; x++)
				{
					for(int y=0; y<8; y++)
					{
						if(simulationPieces[x][y] != 0 && attackDirection*simulationPieces[x][y] < 0)
						{
							for(Point movePoint : getMovablePositions(simulationPieces, x, y))
							{
								MinChessMove node = new MinChessMove(x, y, movePoint.x, movePoint.y, move);
								int score = getScore(simulationPieces, x, y, movePoint.x, movePoint.y);
								// 시뮬레이션 결과의 점수를 비교하여 최대 최소 점수를 설정합니다.
								if(parents != null)
								{
									if(parents.getMaxScroe() < score)
									{
										parents.setMaxScroe(score);
									}
									if(parents.getMinScore() > score)
									{
										parents.setMinScore(score);
									}
								}
								else
								{
									if(move.getMaxScroe() < score)
									{
										move.setMaxScroe(score);
									}
									if(move.getMinScore() > score)
									{
										move.setMinScore(score);
									}
								}
								moveNodes.add(node);
								simulationCnt++;
							}
						}
					}
				}
				// 이동 가능한 경우의 수를 시뮬레이션 하위 노드로 설정합니다.
				move.setNodes(moveNodes);
			}
		}
	}
	
	/**
	 * 시뮬레이션 진행 결과 piece 정보를 반환합니다. 
	 * @param pieces - 말의 위치에 해당하는 배열 구조에 해당 말 점수를 저장
	 * @param move - 이동 가능한 경우의 시뮬레이션 정보
	 * @return
	 */
	public int[][] getSimulationPiece(int [][] pieces, MinChessMove move)
	{
		// 말의 위치 정보를 별도의 배열에 설정합니다.
		int [][] piecesClone = new int[8][8];
		for(int i=0; i<pieces.length; i++)
		{
			for(int j=0; j<pieces[i].length; j++)
			{
				piecesClone[i][j] = pieces[i][j];
			}
		}
		
		// 최 상위 시뮬레이션부터 최 하위 시뮬레이션까지의 진행 상황 정보 리스트를 설정합니다.
		MinChessMove parents = move.getParents();
		
		ArrayList<MinChessMove> moveHistorys = new ArrayList<MinChessMove>();
		
		// 시뮬레이션 이동 정보를 추가합니다.
		moveHistorys.add(move);
		
		// 상위 시뮬레이션이 없을때까지 시뮬레이션 정보를 역행하며 리스트에 추가합니다.
		while(true)
		{
			if(parents != null)
			{
				moveHistorys.add(0, parents);
				if(parents.getParents() == null)
				{
					break;
				}
				parents = parents.getParents();
			}
			else
			{
				break;
			}
		}
		
		// 시뮬레이션 이동 경로를 적용하여 가상의 말 위치 정보를 설정합니다.
		for(MinChessMove moveHistory : moveHistorys)
		{
			for(int i=0; i<pieces.length; i++)
			{
				for(int j=0; j<pieces[i].length; j++)
				{
					if(moveHistory.getX() == i && moveHistory.getY() == j)
					{
						piecesClone[moveHistory.getMoveX()][moveHistory.getMoveY()] = piecesClone[i][j];
						piecesClone[i][j] = 0;
					}
				}
			}
		}
		return piecesClone;
	}
	
	/**
	 * 말의 위치 정보에 따른 배열 구조에 해당 말의 점수를 가진 배열을 반환합니다. 
	 * @return
	 */
	public int[][] getPiece()
	{
		int [][] pieces = new int[8][8];
		for(MinPiece whitePiece : whitePieceMap.values())
		{
			if(whitePiece.isAlive())
			{
				pieces[whitePiece.getPositionX()][whitePiece.getPositionY()] = whitePiece.getScore();
			}
		}
		for(MinPiece blackPiece : blackPieceMap.values())
		{
			if(blackPiece.isAlive())
			{
				pieces[blackPiece.getPositionX()][blackPiece.getPositionY()] = - blackPiece.getScore();
			}
		}
		return pieces;
	}
	
	/**
	 * 이동하는 위치에 같은 편이 있는지 확인합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param moveX - x 이동 좌표
	 * @param moveY - y 이동 좌표
	 * @return
	 */
	private boolean isOverlapOurPiece(int [][] pieces, int x, int y, int moveX, int moveY)
	{
		if(x == moveX && y == moveY)
		{
			return false;
		}
		if(pieces[x][y] < 0 && pieces[moveX][moveY] < 0)
		{
			return true;
		}
		if(pieces[x][y] > 0 && pieces[moveX][moveY] > 0)
		{
			return true;
		}
		return false; 
	}
	
	/**
	 * 이동하는 위치에 다른 편이 있는지 확인합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param moveX - x 이동 좌표
	 * @param moveY - y 이동 좌표
	 * @return
	 */
	public boolean isOverlapEnemyPiece(int [][] pieces, int x, int y, int moveX, int moveY)
	{
		if(x == moveX && y == moveY)
		{
			return false;
		}
		if(pieces[x][y] < 0 && pieces[moveX][moveY] > 0)
		{
			return true;
		}
		if(pieces[x][y] > 0 && pieces[moveX][moveY] < 0)
		{
			return true;
		}
		return false; 
	}
	
	/**
	 * 이동 시 변경되는 점수를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param moveX - x 이동 좌표
	 * @param moveY - y 이동 좌표
	 * @return
	 */
	public int getScore(int [][] pieces, int x, int y, int moveX, int moveY)
	{
		int score = 0;
		for(int i=0; i<pieces.length; i++)
		{
			for(int j=0; j<pieces[i].length; j++)
			{
				if(i == x && j == y)
				{
					continue;
				}
				else if(i == moveX && j == moveY)
				{
					score += pieces[x][y];
				}
				else
				{
					score += pieces[i][j];
				}
			}
		}
		return score;
	}
	
	/**
	 * Board 안에 있는 위치 정보인지 여부를 반환합니다.
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private boolean positionInBoard(int x, int y)
	{
		return x > -1 && y > -1 && x < 8 && y < 8;
	}
	
	/**
	 * 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	public ArrayList<Point> getMovablePositions(int [][] pieces, int x, int y)
	{
		int score = pieces[x][y];
		if(score < 0)
		{
			score = - score;
		}
		if(SCORE_KING == score)
		{
			return getMovablePositionsKing(pieces, x, y);
		}
		if(SCORE_QUEEN == score)
		{
			return getMovablePositionsQueen(pieces, x, y);
		}
		if(SCORE_ROOK == score)
		{
			return getMovablePositionsRook(pieces, x, y);
		}
		if(SCORE_BISHOP == score)
		{
			return getMovablePositionsBishop(pieces, x, y);
		}
		if(SCORE_KNIGHT == score)
		{
			return getMovablePositionsKnight(pieces, x, y);
		}
		if(SCORE_PAWN == score)
		{
			return getMovablePositionsPawn(pieces, x, y);
		}
		return new ArrayList<Point>();
	}
	
	/**
	 * King 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsKing(int [][] pieces, int x, int y)
	{
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		Point point1 = new Point(x-1, y-1);
		Point point2 = new Point(x, y-1);
		Point point3 = new Point(x+1, y-1);
		Point point4 = new Point(x-1, y);
		Point point5 = new Point(x+1, y);
		Point point6 = new Point(x-1, y+1);
		Point point7 = new Point(x, y+1);
		Point point8 = new Point(x+1, y+1);
		Point point9 = getCastlingLeft(pieces, x, y);
		Point point10 = getCastlingRight(pieces, x, y);
		if(positionInBoard(point1.x, point1.y) && !isOverlapOurPiece(pieces, x, y, point1.x, point1.y))
		{
			movablePositions.add(point1);
		}
		if(positionInBoard(point2.x, point2.y) && !isOverlapOurPiece(pieces, x, y, point2.x, point2.y))
		{
			movablePositions.add(point2);
		}
		if(positionInBoard(point3.x, point3.y) && !isOverlapOurPiece(pieces, x, y, point3.x, point3.y))
		{
			movablePositions.add(point3);
		}
		if(positionInBoard(point4.x, point4.y) && !isOverlapOurPiece(pieces, x, y, point4.x, point4.y))
		{
			movablePositions.add(point4);
		}
		if(positionInBoard(point5.x, point5.y) && !isOverlapOurPiece(pieces, x, y, point5.x, point5.y))
		{
			movablePositions.add(point5);
		}
		if(positionInBoard(point6.x, point6.y) && !isOverlapOurPiece(pieces, x, y, point6.x, point6.y))
		{
			movablePositions.add(point6);
		}
		if(positionInBoard(point7.x, point7.y) && !isOverlapOurPiece(pieces, x, y, point7.x, point7.y))
		{
			movablePositions.add(point7);
		}
		if(positionInBoard(point8.x, point8.y) && !isOverlapOurPiece(pieces, x, y, point8.x, point8.y))
		{
			movablePositions.add(point8);
		}
		if(point9 != null)
		{
			movablePositions.add(point9);
		}
		if(point10 != null)
		{
			movablePositions.add(point10);
		}
		return movablePositions;
	}
	
	/**
	 * Queen 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsQueen(int [][] pieces, int x, int y)
	{
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		int moveX = x;
		int moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
			moveY = moveY - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
			moveY = moveY + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
			moveY = moveY + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
			moveY = moveY - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveY = moveY - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveY = moveY + 1;
		}
		return movablePositions;
	}
	
	/**
	 * Rook 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsRook(int [][] pieces, int x, int y)
	{
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		int moveX = x;
		int moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveY = moveY - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveY = moveY + 1;
		}
		Point castlingLeft = getCastlingLeft(pieces, x, y);
		if(castlingLeft != null)
		{
			movablePositions.add(castlingLeft);
		}
		Point castlingRight = getCastlingRight(pieces, x, y);
		if(castlingRight != null)
		{
			movablePositions.add(castlingRight);
		}
		return movablePositions;
	}
	
	/**
	 * Bishop 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsBishop(int [][] pieces, int x, int y)
	{
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		int moveX = x;
		int moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
			moveY = moveY - 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
			moveY = moveY + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX - 1;
			moveY = moveY + 1;
		}
		moveX = x;
		moveY = y;
		while(true)
		{
			Point movePoint = new Point(moveX, moveY);
			if(!positionInBoard(moveX, moveY))
			{
				break;
			}
			if(isOverlapEnemyPiece(pieces, x, y, moveX, moveY))
			{
				movablePositions.add(movePoint);
				break;
			}
			if(isOverlapOurPiece(pieces, x, y, moveX, moveY))
			{
				break;
			}
			if(moveX != x || moveY != y)
			{
				movablePositions.add(movePoint);
			}
			moveX = moveX + 1;
			moveY = moveY - 1;
		}
		return movablePositions;
	}
	
	/**
	 * Knight 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsKnight(int [][] pieces, int x, int y)
	{
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		int moveX = x - 2;
		int moveY = y - 1;
		Point point1 = new Point(moveX, moveY);
		if(positionInBoard(point1.x, point1.y) && !isOverlapOurPiece(pieces, x, y, point1.x, point1.y))
		{
			movablePositions.add(point1);
		}
		moveX = x - 1;
		moveY = y - 2;
		Point point2 = new Point(moveX, moveY);
		if(positionInBoard(point2.x, point2.y) && !isOverlapOurPiece(pieces, x, y, point2.x, point2.y))
		{
			movablePositions.add(point2);
		}
		moveX = x + 1;
		moveY = y - 2;
		Point point3 = new Point(moveX, moveY);
		if(positionInBoard(point3.x, point3.y) && !isOverlapOurPiece(pieces, x, y, point3.x, point3.y))
		{
			movablePositions.add(point3);
		}
		moveX = x + 2;
		moveY = y - 1;
		Point point4 = new Point(moveX, moveY);
		if(positionInBoard(point4.x, point4.y) && !isOverlapOurPiece(pieces, x, y, point4.x, point4.y))
		{
			movablePositions.add(point4);
		}
		moveX = x + 2;
		moveY = y + 1;
		Point point5 = new Point(moveX, moveY);
		if(positionInBoard(point5.x, point5.y) && !isOverlapOurPiece(pieces, x, y, point5.x, point5.y))
		{
			movablePositions.add(point5);
		}
		moveX = x + 1;
		moveY = y + 2;
		Point point6 = new Point(moveX, moveY);
		if(positionInBoard(point6.x, point6.y) && !isOverlapOurPiece(pieces, x, y, point6.x, point6.y))
		{
			movablePositions.add(point6);
		}
		moveX = x - 1;
		moveY = y + 2;
		Point point7 = new Point(moveX, moveY);
		if(positionInBoard(point7.x, point7.y) && !isOverlapOurPiece(pieces, x, y, point7.x, point7.y))
		{
			movablePositions.add(point7);
		}
		moveX = x - 2;
		moveY = y + 1;
		Point point8 = new Point(moveX, moveY);
		if(positionInBoard(point8.x, point8.y) && !isOverlapOurPiece(pieces, x, y, point8.x, point8.y))
		{
			movablePositions.add(point8);
		}
		return movablePositions;
	}
	
	/**
	 * Pawn 의 이동 가능한 경로 리스트를 반환합니다.
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private ArrayList<Point> getMovablePositionsPawn(int [][] pieces, int x, int y)
	{
		int attackDirection = 1;
		if(pieces[x][y] > 0)
		{
			attackDirection = -1;
		}
		ArrayList<Point> movablePositions = new ArrayList<Point>();
		Point point1 = new Point(x, y+(1*attackDirection));
		Point point2 = new Point(x, y+(2*attackDirection));
		Point point3 = new Point(x-1, y+(1*attackDirection));
		Point point4 = new Point(x+1, y+(1*attackDirection));
		if(positionInBoard(point1.x, point1.y) && !isOverlapOurPiece(pieces, x, y, point1.x, point1.y) && !isOverlapEnemyPiece(pieces, x, y, point1.x, point1.y))
		{
			movablePositions.add(point1);
		}
		if(positionInBoard(point2.x, point2.y) && !isOverlapOurPiece(pieces, x, y, point2.x, point2.y) && !isOverlapEnemyPiece(pieces, x, y, point2.x, point2.y))
		{
			if(pieces[x][y] < 0 && y == 1)
			{
				movablePositions.add(point2);
			}
			else if(pieces[x][y] > 0 && y == 6)
			{
				movablePositions.add(point2);
			}
		}
		if(positionInBoard(point3.x, point3.y) && isOverlapEnemyPiece(pieces, x, y, point3.x, point3.y))
		{
			movablePositions.add(point3);
		}
		if(positionInBoard(point4.x, point4.y) && isOverlapEnemyPiece(pieces, x, y, point4.x, point4.y))
		{
			movablePositions.add(point4);
		}
		return movablePositions;
	}
	
	/**
	 * 좌측으로 Castling 하기 위한 위치를 반환합니다. 
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private Point getCastlingLeft(int [][] pieces, int x, int y)
	{
		Point point = null;
		if(x == 0 && y == 0 && pieces[x][y] == -SCORE_ROOK && pieces[4][0] == -SCORE_KING)
		{
			if(pieces[1][0] == 0 && pieces[2][0] == 0 && pieces[3][0] == 0)
			{
				point = new Point(3, 0);
			}
		}
		if(x == 4 && y == 0 && pieces[x][y] == -SCORE_KING && pieces[0][0] == -SCORE_ROOK )
		{
			if(pieces[1][0] == 0 && pieces[2][0] == 0 && pieces[3][0] == 0)
			{
				point = new Point(2, 0);
			}
		}
		if(x == 0 && y == 7 && pieces[x][y] == SCORE_ROOK && pieces[4][7] == SCORE_KING)
		{
			if(pieces[1][7] == 0 && pieces[2][7] == 0 && pieces[3][7] == 0)
			{
				point = new Point(3, 7);
			}
		}
		if(x == 4 && y == 7 && pieces[x][y] == SCORE_KING && pieces[0][7] == SCORE_ROOK)
		{
			if(pieces[1][7] == 0 && pieces[2][7] == 0 && pieces[3][7] == 0)
			{
				point = new Point(2, 7);
			}
		}
		return point;
	}
	
	/**
	 * 우측으로 Castling 하기 위한 위치를 반환합니다. 
	 * @param pieces - 말의 위치 정보
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @return
	 */
	private Point getCastlingRight(int [][] pieces, int x, int y)
	{
		Point point = null;
		if(x == 7 && y == 0 && pieces[x][y] == -SCORE_ROOK && pieces[4][0] == -SCORE_KING)
		{
			if(pieces[5][0] == 0 && pieces[6][0] == 0)
			{
				point = new Point(5, 0);
			}
		}
		if(x == 4 && y == 0 && pieces[x][y] == -SCORE_KING && pieces[7][0] == -SCORE_ROOK )
		{
			if(pieces[5][0] == 0 && pieces[6][0] == 0)
			{
				point = new Point(6, 0);
			}
		}
		if(x == 7 && y == 7 && pieces[x][y] == SCORE_ROOK && pieces[4][7] == SCORE_KING)
		{
			if(pieces[5][7] == 0 && pieces[6][7] == 0)
			{
				point = new Point(5, 7);
			}
		}
		if(x == 4 && y == 7 && pieces[x][y] == SCORE_KING && pieces[7][7] == SCORE_ROOK)
		{
			if(pieces[5][7] == 0 && pieces[6][7] == 0)
			{
				point = new Point(6, 7);
			}
		}
		return point;
	}
	
	/**
	 * 좌측으로 Castling 가능한지 여부를 반환합니다.
	 * @param piece - 말 정보
	 * @param movePoint - 이동할 위치
	 * @return
	 */
	public boolean isCastlingLeft(MinPiece piece, Point movePoint)
	{
		int [][] pieces = getPiece();
		int x = piece.getPositionX();
		int y = piece.getPositionY();
		Point castlingLeft = getCastlingLeft(pieces, x, y);
		if(castlingLeft != null && movePoint.x == castlingLeft.x && movePoint.y == castlingLeft.y)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 우측으로 Castling 가능한지 여부를 반환합니다.
	 * @param piece - 말 정보
	 * @param movePoint - 이동할 위치
	 * @return
	 */
	public boolean isCastlingRight(MinPiece piece, Point movePoint)
	{
		int [][] pieces = getPiece();
		int x = piece.getPositionX();
		int y = piece.getPositionY();
		Point castlingRight = getCastlingRight(pieces, x, y);
		if(castlingRight != null && movePoint.x == castlingRight.x && movePoint.y == castlingRight.y)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 체크 여부를 반환합니다.
	 * @return
	 */
	public boolean isChecked(MinPiece piece)
	{
		// TODO 체크 여부 확인 코드 필요
		// int [][] pieces = getPiece();
		if(piece.isBlackTeam())
		{
			
		}
		else
		{
			
		}
		return false;
	}
	
	/**
	 * 체크 여부를 반환합니다.
	 * @param piece
	 * @param movePoint
	 * @return
	 */
	public boolean isCheck(MinPiece piece, Point movePoint)
	{
		// TODO 체크 여부 확인 코드 필요
		// int [][] pieces = getPiece().clone();
		if(piece.isBlackTeam())
		{
			
		}
		else
		{
			
		}
		return false;
	}
	
	/**
	 * 체크 메이트 여부를 반환합니다.
	 * @param piece
	 * @return
	 */
	public boolean isCheckMate(MinPiece piece)
	{
		// TODO 체크 여부 확인 코드 필요
		// int [][] pieces = getPiece();
		if(piece.isBlackTeam())
		{
			
		}
		else
		{
			
		}
		return false;
	}
	
	/**
	 * 프로모션 정보를 설정합니다.
	 * @param x - x 좌표
	 * @param y - y 좌표
	 * @param pieceInitials - 프로모션되는 piece 이니셜
	 */
	public void promotion(int x, int y, String pieceInitials)
	{
		if(pieceInitials.equals("Q"))
		{
			if(whitePieceMap.containsKey(y +"_" + x) && whitePieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinQueen(MinPiece.WHITE_TEAM, new Point(x, y), this));
			}
			if(blackPieceMap.containsKey(y +"_" + x) && blackPieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinQueen(MinPiece.BLACK_TEAM, new Point(x, y), this));
			}
		}
		if(pieceInitials.equals("R"))
		{
			if(whitePieceMap.containsKey(y +"_" + x) && whitePieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinRook(MinPiece.WHITE_TEAM, new Point(x, y), this));
			}
			if(blackPieceMap.containsKey(y +"_" + x) && blackPieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinRook(MinPiece.BLACK_TEAM, new Point(x, y), this));
			}
		}
		if(pieceInitials.equals("B"))
		{
			if(whitePieceMap.containsKey(y +"_" + x) && whitePieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinBishop(MinPiece.WHITE_TEAM, new Point(x, y), this));
			}
			if(blackPieceMap.containsKey(y +"_" + x) && blackPieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinBishop(MinPiece.BLACK_TEAM, new Point(x, y), this));
			}
		}
		if(pieceInitials.equals("N"))
		{
			if(whitePieceMap.containsKey(y +"_" + x) && whitePieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinKnight(MinPiece.WHITE_TEAM, new Point(x, y), this));
			}
			if(blackPieceMap.containsKey(y +"_" + x) && blackPieceMap.get(y +"_" + x) instanceof MinPawn)
			{
				whitePieceMap.put(y +"_" + x, new MinKnight(MinPiece.BLACK_TEAM, new Point(x, y), this));
			}
		}
		MinChessAppMain.getWorkspace().repaintPiece();
	}
	
}
