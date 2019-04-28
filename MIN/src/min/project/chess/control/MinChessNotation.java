package min.project.chess.control;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import min.utils.MinProperties;

/**
 * 개요 : 체스 경기의 기보 정보를 관리합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessNotation 
{
	private String white;
	private String black;
	
	private StringBuffer log;
	private StringBuffer pgnLog;
	
	private MinChessMove move;
	private MinChessEngine chessEngine;
	
	private ArrayList<MinProperties> properties;
	
	/**
	 * 팀 명 기준으로 체스 경기 정보를 초기화 합니다.
	 * @param white - white 팀 명
	 * @param black - black 팀 명
	 */
	public MinChessNotation(String white, String black, MinChessEngine chessEngine)
	{
		this.chessEngine = chessEngine;
		this.white = white;
		this.black = black;
		initLog(white, black);
	}
	
	/**
	 * 기보 데이터를 기준으로 체스 경기 정보를 초기화 합니다.
	 * @param pgnLog - 기보 데이터
	 */
	public MinChessNotation(StringBuffer pgnLog, MinChessEngine chessEngine)
	{
		this.chessEngine = chessEngine;
		initPropertiesLog(pgnLog);
	}
	
	/**
	 * 팀 명 기준으로 데이터를 설정합니다.
	 * @param white
	 * @param black
	 */
	public void initLog(String white, String black)
	{
		properties = new ArrayList<MinProperties>();
		move = new MinChessMove(-1, -1, -1, -1, null);
		
		properties.add(new MinProperties("Event", "MIN Chess."));
		properties.add(new MinProperties("Site", "South Korea."));
		properties.add(new MinProperties("Date", new SimpleDateFormat("yyyy.MM.dd").format(new Date())));
		properties.add(new MinProperties("Round", "1"));
		properties.add(new MinProperties("White", white));
		properties.add(new MinProperties("Black", black));
		properties.add(new MinProperties("Result", "*"));
	}
	
	/**
	 * 기보 데이터 기준으로 속성 정보를 설정합니다.<br>
	 * 이동 정보 설정 시 데이터 파일 사이즈가 클 경우 초기화에 너무 많은 시간이 소요되므로
	 * 속성 정보만 표시하고 실제 기보를 불러오는 이벤트 시 이동 정보를 별도로 초기화 합니다.
	 * @param pgnLog
	 */
	public void initPropertiesLog(StringBuffer pgnLog)
	{
		this.pgnLog = pgnLog;
		
		properties = new ArrayList<MinProperties>();
		
		if(pgnLog != null && pgnLog.toString().split("\n\n").length > 1)
		{
			String [] logSplit = pgnLog.toString().split("\n\n");
			String [] logProperties = logSplit[0].split("\n");
			for(String propertieStr : logProperties)
			{
				String [] propertie = propertieStr.replaceAll("\\[", "").replaceAll("\\]", "").split("\"");
				String key = propertie[0].trim();
				String value = propertie[1].trim();
				properties.add(new MinProperties(key, value));
				if(key.equals("white"))
				{
					this.white = value;
				}
				if(key.equals("black"))
				{
					this.black = value;
				}
			}
		}
	}
	
	/**
	 * 기보 데이터 기준으로 이동 정보를 설정합니다.
	 * @param pgnLog
	 */
	public void initNotation()
	{
		move = new MinChessMove(-1, -1, -1, -1, null);
		if(pgnLog != null && pgnLog.toString().split("\n\n").length > 1)
		{
			String [] logSplit = pgnLog.toString().split("\n\n");
			this.log = new StringBuffer(logSplit[1]);
			// 기보의 이동 정보를 턴 기준으로 배열에 저장합니다.
			// 행변환 되어 있는 문자열을 하줄로 변환 후 각각의 이동 정보로 분할합니다.
			String [] moveArray = log.toString().replaceAll("\n", " ").replaceAll("  ", " ").replaceAll("\\. ", "\\.").split("\\.");

			// 최 상위 노드를 설정합니다.
			MinChessMove parentNode = move;
			// 기보 정보 기반으로 하위 노드를 설정합니다.
			// 이동 정보로 분할 시 첫번째 내용에는 순번만 저장되기 때문에 두번째 순번부터 데이터를 확인합니다.
			for(int i=1; i<moveArray.length; i++)
			{
				// 흰팀 이동 정보
				String whiteMove = moveArray[i].split(" ")[0].replaceAll("!", "").replaceAll("\\?", "").replaceAll("\\+", "").replaceAll("#", "").trim();
				// 흙팀 이동 정보
				String blackMove = moveArray[i].split(" ")[1].replaceAll("!", "").replaceAll("\\?", "").replaceAll("\\+", "").replaceAll("#", "").trim();
				
				if(whiteMove.length() > 1 && blackMove.length() > 1)
				{
					MinChessMove childNode = createNodeInfo(whiteMove, parentNode, -1);
					ArrayList<MinChessMove> whiteChildNodes = new ArrayList<MinChessMove>();
					whiteChildNodes.add(childNode);
					parentNode.setNodes(whiteChildNodes);
					// 캐슬링 시 하위 node 에 ROOK 의 이동 정보가 있기 때문에 하위 node 를 마지막 부모로 설정합니다.
					if(whiteMove.equals("O-O") || whiteMove.equals("O-O-O"))
					{
						parentNode = childNode.getNodes().get(0);
					}
					else 
					{
						parentNode = childNode;
					}
					
					childNode = createNodeInfo(blackMove, parentNode, 1);
					ArrayList<MinChessMove> blackChildNodes = new ArrayList<MinChessMove>();
					blackChildNodes.add(childNode);
					parentNode.setNodes(blackChildNodes);
					
					// 캐슬링 시 하위 node 에 ROOK 의 이동 정보가 있기 때문에 하위 node 를 마지막 부모로 설정합니다.
					if(blackMove.equals("O-O") || blackMove.equals("O-O-O"))
					{
						parentNode = childNode.getNodes().get(0);
					}
					else
					{
						parentNode = childNode;
					}
				}
			}
		}
	}
	
	/**
	 * 기보의 이동 정보를 프로그램 상의 이동 정보로 변환합니다.
	 * @param moveInfo
	 * @param parents
	 * @param attackDirection
	 * @return
	 */
	public MinChessMove createNodeInfo(String moveInfo, MinChessMove parents, int attackDirection)
	{
		// 킹 캐슬링 정보를 설정합니다.
		if(moveInfo.equals("O-O"))
		{
			int y = 7;
			int kingX = 4;
			int kingMoveX = 6;
			int rookX = 7;
			int rookMoveX = 5;
			if(attackDirection == 1)
			{
				y = 0;
			}
			MinChessMove king = new MinChessMove(kingX, y, kingMoveX, y, parents);
			king.setPgn(moveInfo);
			ArrayList<MinChessMove> rook = new ArrayList<MinChessMove>();
			rook.add(new MinChessMove(rookX, y, rookMoveX, y, king));
			rook.get(0).setPgn(moveInfo);
			king.setNodes(rook);
			return king;
		}
		// 퀸 캐슬링 정보를 설정합니다.
		else if(moveInfo.equals("O-O-O"))
		{
			int y = 7;
			int kingX = 4;
			int kingMoveX = 2;
			int rookX = 0;
			int rookMoveX = 3;
			if(attackDirection == 1)
			{
				y = 0;
			}
			MinChessMove king = new MinChessMove(kingX, y, kingMoveX, y, parents);
			king.setPgn(moveInfo);
			ArrayList<MinChessMove> rook = new ArrayList<MinChessMove>();
			rook.add(new MinChessMove(rookX, y, rookMoveX, y, king));
			rook.get(0).setPgn(moveInfo);
			king.setNodes(rook);
			return king;
		}
		// 캐슬링을 제외한 모든 경우의 정보를 설정합니다.
		else
		{
			String moveStr = moveInfo;
			// 프로모션인 경우 이동 정보만 추출합니다.
			if(moveInfo.contains("="))
			{
				moveStr = moveInfo.split("=")[0];
			}
			String pieceInitials = moveStr.substring(0, 1);
			String notationX = moveStr.substring(moveStr.length()-2, moveStr.length()-1);
			String notationY = moveStr.substring(moveStr.length()-1);
			
			// 이니셜 정보 기준으로 프로그램상의 점수를 설정합니다.
			int score = MinChessEngine.SCORE_PAWN;
			if(pieceInitials.equals("K"))
			{
				score = MinChessEngine.SCORE_KING;
			}
			else if(pieceInitials.equals("Q"))
			{
				score = MinChessEngine.SCORE_QUEEN;
			}
			else if(pieceInitials.equals("R"))
			{
				score = MinChessEngine.SCORE_ROOK;
			}
			else if(pieceInitials.equals("B"))
			{
				score = MinChessEngine.SCORE_BISHOP;
			}
			else if(pieceInitials.equals("N"))
			{
				score = MinChessEngine.SCORE_KNIGHT;
			}
			score = score * attackDirection * -1;
			
			int [][] pieces = chessEngine.getPiece();
			
			MinChessMove move = parents;
			
			while(move.getParents() != null)
			{
				move = move.getParents();
			}
			
			// 상위 이동 정보를 시뮬레이션 한 결과를 이용하여 마지막 말 위치 정보를 설정합니다.
			while(move.getNodes() != null && move.getNodes().size() > 0)
			{
				move = move.getNodes().get(0);
			}
			int [][] simulationPieces = chessEngine.getSimulationPiece(pieces, move);
			
			int x = -1;
			int y = -1;
			int moveX = notationX.toCharArray()[0] - 'a';
			int moveY = 8 - Integer.valueOf(notationY);
			
			// 모든 말의 정보를 확인합니다.
			// 기보 규칙...아놕!!이동되는 정보의 형식이 상황에 따라 가변이라 처리해 줘야 하는 부분이 복잡합니다.
			for(int i=0; i<8; i++)
			{
				for(int j=0; j<8; j++)
				{
					// 시뮬레이션 결과 중 동일한 말 정보를 확인합니다.
					if(score == simulationPieces[i][j])
					{
						// 이동 가능한 영역을 확인합니다.
						for(Point point : chessEngine.getMovablePositions(simulationPieces, i, j))
						{
							// 실제 이동 정보와 이동 가능한 정보가 동일한 말을 확인합니다.
							if(point.x == moveX && point.y == moveY)
							{
								if(x == -1 && y == -1)
								{
									x = i;
									y = j;
								}
								else
								{
									// 이동 가능한 말이 이미 설정된 경우 기보상의 기존 말 위치 정보를 확인합니다.
									int baseX = -1;
									int baseY = -1;
									// 기존 말 위치 정보의 첫번째 문자가 숫자인 경우와 문자인 경우로 x 축, y 축을 구분합니다.
									try
									{
										baseY = Integer.valueOf(moveStr.substring(1, 2));
									}
									catch(Exception e)
									{
										baseX = moveStr.substring(1, 2).toCharArray()[0] - 'a';
										// 기존 말 위치 정보의 첫번째 문자가 x 축인 경우 이후 y 축 정보가 있는지 다시 확인합니다.
										try
										{
											baseY = Integer.valueOf(moveStr.substring(2, 3));
										}
										catch(Exception ex) {};
									}
									// x 축과 y 축 모두 있는 경우를 설정합니다.
									if(baseX != -1 && baseY != -1)
									{
										if(i == baseX && j == baseY)
										{
											x = i;
											y = j;
										}
									}
									// x 축만 있는 경우를 설정합니다.
									else if(baseX != -1)
									{
										if(i == baseX)
										{
											x = i;
											y = j;
										}
									}
									// y 축만 있는 경우를 설정합니다.
									else if(baseY != -1)
									{
										if(j == baseY)
										{
											x = i;
											y = j;
										}
									}
								}
							}
						}
					}
				}
			}
			// 프로모션 정보를 처리합니다.
			MinChessMove returnMove = new MinChessMove(x, y, moveX, moveY, parents);
			if(moveInfo.contains("="))
			{
				String promotionPieceInitials = moveInfo.split("=")[1];
				returnMove = new MinChessMove(x, y, moveX, moveY, parents, promotionPieceInitials);
			}
			returnMove.setPgn(moveInfo);
			return returnMove;
		}
	}
	
	/**
	 * 기보의 속성 정보를 반환합니다.
	 * @param key - Event, Site, Date, White, Black . . .
	 * @return
	 */
	public String getProperties(String key)
	{
		for(MinProperties propertie : properties)
		{
			if(propertie.getKey().equals(key))
			{
				return propertie.getStringValue();
			}
		}
		return null;
	}
	
	/**
	 * 기보의 속성 정보 리스트를 반환합니다.
	 * @return
	 */
	public ArrayList<MinProperties> getProperties()
	{
		return properties;
	}
	
	/**
	 * 기보의 속성 정보 리스트를 설정합니다.
	 * @param properties
	 */
	public void setProperties(ArrayList<MinProperties> properties) 
	{
		this.properties = properties;
	}
	
	/**
	 * 기보의 이동 정보를 반환합니다.
	 * @return
	 */
	public MinChessMove getMove()
	{
		return move;
	}

	/**
	 * 기보의 이동 정보를 설정합니다.
	 * @param move
	 */
	public void setMove(MinChessMove move) 
	{
		this.move = move;
	}

	/**
	 * 기보의 이동 정보 영역 데이터를 반환합니다.
	 * @return
	 */
	public StringBuffer getLog()
	{
		return log;
	}

	/**
	 * 기보의 이동 정보 영역 데이터를 설정합니다.
	 * @param log
	 */
	public void setLog(StringBuffer log) 
	{
		this.log = log;
	}

	/**
	 * 기보의 원본 데이터를 반환합니다.
	 * @return
	 */
	public StringBuffer getPgnLog() 
	{
		return pgnLog;
	}

	/**
	 * 기보의 원본 데이터를 설정합니다.
	 * @param pgnLog
	 */
	public void setPgnLog(StringBuffer pgnLog)
	{
		this.pgnLog = pgnLog;
	}

	/**
	 * white player 이름 반환
	 * @return
	 */
	public String getWhite()
	{
		return white;
	}
	/**
	 * white player 이름 설정
	 * @param white
	 */
	public void setWhite(String white) 
	{
		this.white = white;
	}
	/**
	 * black player 이름
	 * @return
	 */
	public String getBlack() 
	{
		return black;
	}
	/**
	 * black player 이름
	 * @param black
	 */
	public void setBlack(String black)
	{
		this.black = black;
	}
	/**
	 * chess engine 반환
	 * @return
	 */
	public MinChessEngine getChessEngine() 
	{
		return chessEngine;
	}
	/**
	 * chess engine 설정
	 * @param chessEngine
	 */
	public void setChessEngine(MinChessEngine chessEngine)
	{
		this.chessEngine = chessEngine;
	}
	
	@Override
	public String toString() 
	{
		return getProperties("White")+ " vs " + getProperties("Black");
	}
}
