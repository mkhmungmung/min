package min.project.chess.com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import min.com.MinImagePanel;
import min.com.MinPanel;
import min.img.MinImages;
import min.project.chess.MinChessAppMain;
import min.project.chess.control.MinChessEngine;
import min.project.chess.control.MinChessMove;
import min.project.chess.control.MinChessNotation;
import min.project.chess.control.piece.MinBishop;
import min.project.chess.control.piece.MinKing;
import min.project.chess.control.piece.MinKnight;
import min.project.chess.control.piece.MinPawn;
import min.project.chess.control.piece.MinPiece;
import min.project.chess.control.piece.MinQueen;
import min.project.chess.control.piece.MinRook;
import min.project.chess.data.MinChessConfig;
import min.project.chess.data.MinChessDB;
import min.project.chess.img.MinChessImages;
import min.utils.MinProperties;
import min.utils.MinUtils;

/**
 * 개요 : CHESS 프로그램의 workspace 를 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessWorkspace extends MinPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel labelWhite;
	private JLabel labelBlack;
	private JButton buttonPrevPrev;
	private JButton buttonNextNext;
	private JButton buttonPrev;
	private JButton buttonNext;
	private JButton buttonStart;
	private JButton buttonSave;
	private JButton buttonLoad;
	private DefaultMutableTreeNode modelChessDB;
	private JTree treeChessDB;
	private JTextArea textAreaLog;
	private JScrollPane scrollChessDB;
	private JScrollPane scrollLog;
	
	private MinPanel panelEast;
	private MinPanel panelWest;
	private MinPanel panelNorth;
	private MinPanel panelSouth;
	private MinPanel panelCenter;
	private MinPanel panelWhite;
	private MinPanel panelBlack;
	private MinPanel panelWhiteCenter;
	private MinPanel panelBlackCenter;
	private MinPanel panelWhiteTitle;
	private MinPanel panelBlackTitle;
	private MinPanel panelWhiteDeadPiece;
	private MinPanel panelBlackDeadPiece;
	private MinPanel panelButtonNotation;
	private MinPanel panelButtonPlay;
	private MinPanel panelStart;

	private MinImagePanel imagePanelBoard;
	
	private MinPiece selectedPiece;
	private MinChessEngine chessEngine;
	
	private MinPiece blackRook1;
	private MinPiece blackKnight1;
	private MinPiece blackBishop1;
	private MinPiece blackQueen;
	private MinPiece blackKing;
	private MinPiece blackBishop2;
	private MinPiece blackKnight2;
	private MinPiece blackRook2;
	private MinPiece blackPawn1;
	private MinPiece blackPawn2;
	private MinPiece blackPawn3;
	private MinPiece blackPawn4;
	private MinPiece blackPawn5;
	private MinPiece blackPawn6;
	private MinPiece blackPawn7;
	private MinPiece blackPawn8;
	
	private MinPiece whiteRook1;
	private MinPiece whiteKnight1;
	private MinPiece whiteBishop1;
	private MinPiece whiteQueen;
	private MinPiece whiteKing;
	private MinPiece whiteBishop2;
	private MinPiece whiteKnight2;
	private MinPiece whiteRook2;
	private MinPiece whitePawn1;
	private MinPiece whitePawn2;
	private MinPiece whitePawn3;
	private MinPiece whitePawn4;
	private MinPiece whitePawn5;
	private MinPiece whitePawn6;
	private MinPiece whitePawn7;
	private MinPiece whitePawn8;
	
	private MinChessNotation notationInfo;
	
	private HashMap<String, MinPiece> whitePieceMap;
	private HashMap<String, MinPiece> blackPieceMap;
	
	private ArrayList<MinChessNotation> loadChessDB;
	
	private StringBuffer log;
	
	private String playerName;
	
	private int notationIndex;

	/**
	 * 생성자
	 */
	public MinChessWorkspace()
	{
		super(0);
		
		ini();
		
		// initPiece();
		
		initListener();
		
		initChessDB("Name");
		
	}
	
	/**
	 * 초기화
	 */
	private void ini()
	{
		// [사용자 이름]
		playerName = MinChessConfig.getPlayerName();
		if(playerName == null || playerName.isEmpty())
		{
			new MinChessUserDialog();
		}
		
		// [화면 상단의 팀 구분 초기화]
		labelWhite = new JLabel("W h i t e", JLabel.CENTER);
		labelWhite.setFont(new Font("", Font.PLAIN, 15));
		labelBlack = new JLabel("B l a c k", JLabel.CENTER);
		labelBlack.setFont(new Font("", Font.PLAIN, 15));
		panelWhiteTitle = new MinPanel(0, labelWhite);
		panelWhiteTitle.setGradientPaint(true);
		panelWhiteTitle.setBackground(new Color(212, 212, 212));
		panelWhiteTitle.setBorder(BorderFactory.createLineBorder(new Color(239, 239, 239), 2));
		panelWhiteTitle.setPreferredSize(new Dimension(0, 35));
		panelBlackTitle = new MinPanel(0, labelBlack);
		panelBlackTitle.setGradientPaint(true);
		panelBlackTitle.setBackground(new Color(212, 212, 212));
		panelBlackTitle.setBorder(BorderFactory.createLineBorder(new Color(239, 239, 239), 2));
		panelBlackTitle.setPreferredSize(new Dimension(0, 35));
		
		// [버튼 초기화]
		buttonPrev = new JButton(MinImages.IMAGE_PREV);
		buttonPrev.setPreferredSize(new Dimension(40, 23));
		buttonPrev.setToolTipText("1수 이전");
		buttonPrev.setEnabled(false);
		buttonNext = new JButton(MinImages.IMAGE_NEXT);
		buttonNext.setPreferredSize(new Dimension(40, 23));
		buttonNext.setToolTipText("1수 이후");
		buttonNext.setEnabled(false);
		buttonPrevPrev = new JButton(MinImages.IMAGE_PREV_PREV);
		buttonPrevPrev.setPreferredSize(new Dimension(40, 23));
		buttonPrevPrev.setEnabled(false);
		buttonPrevPrev.setToolTipText("2수 이전");
		buttonNextNext = new JButton(MinImages.IMAGE_NEXT_NEXT);
		buttonNextNext.setPreferredSize(new Dimension(40, 23));
		buttonNextNext.setToolTipText("2수 이후");
		buttonNextNext.setEnabled(false);
		panelButtonNotation = new MinPanel();
		panelButtonNotation.setOpaque(false);
		panelButtonNotation.add(buttonPrevPrev);
		panelButtonNotation.add(buttonPrev);
		panelButtonNotation.add(buttonNext);
		panelButtonNotation.add(buttonNextNext);
		
		buttonStart = new JButton("Play");
		buttonStart.setToolTipText("Play");
		buttonStart.setFont(new Font("", Font.BOLD, 11));
		buttonSave = new JButton("Save");
		buttonSave.setToolTipText("Save");
		buttonSave.setFont(new Font("", Font.BOLD, 11));
		buttonLoad = new JButton("Load");
		buttonLoad.setToolTipText("Load");
		buttonLoad.setFont(new Font("", Font.BOLD, 11));
		panelStart = new MinPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelStart.setOpaque(false);
		panelStart.add(buttonLoad);
		panelStart.add(buttonSave);
		panelStart.add(buttonStart);
		
		panelButtonPlay = new MinPanel(10, 10, 0, 10);
		panelButtonPlay.add(panelStart);
		
		// [죽은 말 표시 영역 초기화]
		panelWhiteDeadPiece = new MinPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
		panelWhiteDeadPiece.setBorder(BorderFactory.createEtchedBorder());
		panelWhiteDeadPiece.setPreferredSize(new Dimension(0, 330));
		panelWhiteDeadPiece.setGradientPaint(true);
		panelWhiteDeadPiece.setBackground(new Color(193, 193, 193));
		panelBlackDeadPiece = new MinPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
		panelBlackDeadPiece.setBorder(BorderFactory.createEtchedBorder());
		panelBlackDeadPiece.setPreferredSize(new Dimension(0, 330));
		panelBlackDeadPiece.setGradientPaint(true);
		panelBlackDeadPiece.setBackground(new Color(193, 193, 193));
		
		// [로그 영역 초기화]
		log = new StringBuffer();
		textAreaLog = new JTextArea();
		scrollLog = new JScrollPane(textAreaLog);
		scrollLog.getViewport().setBackground(Color.white);
		
		// [기보 정보 영역 초기화]
		modelChessDB = new DefaultMutableTreeNode("Chess Data");
		treeChessDB = new JTree(modelChessDB);
		treeChessDB.setShowsRootHandles(true);
		treeChessDB.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollChessDB = new JScrollPane(treeChessDB);
		scrollChessDB.getViewport().setBackground(Color.white);
		
		// [좌, 우 사이드 영역 초기화]
		panelWhiteCenter = new MinPanel(new BorderLayout(5, 5));
		panelWhiteCenter.setBackground(new Color(247, 247, 247));
		panelWhiteCenter.add(scrollLog);
		panelWhiteCenter.add(panelWhiteDeadPiece, BorderLayout.SOUTH);
		panelBlackCenter = new MinPanel(new BorderLayout(5, 5));
		panelBlackCenter.setBackground(new Color(247, 247, 247));
		panelBlackCenter.add(scrollChessDB);
		panelBlackCenter.add(panelBlackDeadPiece, BorderLayout.NORTH);
		panelBlackCenter.add(panelButtonNotation, BorderLayout.SOUTH);
		panelWhite = new MinPanel(5, 5, 5);
		panelWhite.setBackground(new Color(247, 247, 247));
		panelWhite.setBorder(BorderFactory.createEtchedBorder());
		panelWhite.add(panelWhiteCenter);
		panelWhite.add(panelWhiteTitle, BorderLayout.NORTH);
		panelWhite.add(panelButtonPlay, BorderLayout.SOUTH);
		panelBlack = new MinPanel(5, 5, 5);
		panelBlack.setBackground(new Color(247, 247, 247));
		panelBlack.setBorder(BorderFactory.createEtchedBorder());
		panelBlack.add(panelBlackCenter);
		panelBlack.add(panelBlackTitle, BorderLayout.NORTH);
		
		// [Board 영역 초기화]
		imagePanelBoard = new MinImagePanel();
		imagePanelBoard.setPreferredSize(new Dimension(720, 720));
		imagePanelBoard.addImage(MinChessImages.IMAGE_BOARD);
		imagePanelBoard.addImage(MinChessImages.IMAGE_TITLE);
		
		// [전체 영역 초기화]
		panelEast = new MinPanel(10, panelWhite);
		panelEast.setOpaque(false);
		panelEast.setPreferredSize(new Dimension(237, 0));
		panelWest = new MinPanel(10, panelBlack);
		panelWest.setOpaque(false);
		panelWest.setPreferredSize(new Dimension(237, 0));
		panelNorth = new MinPanel(5);
		panelNorth.setOpaque(false);
		panelNorth.setPreferredSize(new Dimension(0, 10));
		panelSouth = new MinPanel(5);
		panelSouth.setOpaque(false);
		panelSouth.setPreferredSize(new Dimension(0, 10));
		panelCenter = new MinPanel(0);
		panelCenter.setOpaque(false);
		panelCenter.add(imagePanelBoard);
		panelCenter.add(panelNorth, BorderLayout.NORTH);
		panelCenter.add(panelSouth, BorderLayout.SOUTH);
		
		add(panelCenter);
		add(panelEast, BorderLayout.EAST);
		add(panelWest, BorderLayout.WEST);
		
		setBackgroundImage(MinUtils.createImage(MinChessImages.BACKGROUND).getImage());
	}
	
	/**
	 * 이벤트를 설정합니다.
	 */
	private void initListener()
	{
		imagePanelBoard.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				// 선택된 말이 있는지 확인합니다.
				if(selectedPiece != null)
				{
					// 선택된 말을 마우스 이동 경로로 이동 시킵니다.
					boolean movable = movePiece(e.getPoint());
					// 이동 했는지 확인 후 흙색 팀 play 진행 합니다.
					if(movable)
					{
						MinChessAppMain.getWorkspace().setCursor(new Cursor(Cursor.WAIT_CURSOR));
						chessEngine.playChessEngine();
						MinChessAppMain.getWorkspace().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
					// 말의 정보를 재 설정합니다.
					repaintPiece();
					// 선택했던 말을 해제합니다.
					selectedPiece = null;
				}
			}
			@Override
			public void mousePressed(MouseEvent e) 
			{
				// 선택된 말을 설정합니다.
				selectedPiece(e.getPoint());
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		imagePanelBoard.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent paramMouseEvent) {}
			@Override
			public void mouseDragged(MouseEvent paramMouseEvent)
			{
				// 선택된 말이 있는지 여부를 확인하고 마우스 이동 경로에 표시합니다.
				if(selectedPiece != null)
				{
					setPiece(selectedPiece, paramMouseEvent.getPoint());
				}
			}
		});
		buttonStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) 
			{
				int confirm = JOptionPane.showConfirmDialog(MinChessAppMain.getInstance(), "Chess 대국을 시작하시겠습니까?", "Min Chess", JOptionPane.YES_OPTION);
				if(confirm == JOptionPane.YES_OPTION)
				{
					notationIndex = 0;
					notationInfo = new MinChessNotation(playerName, "Min Chess Engine", chessEngine);
					initNotationLog();
					initPiece(true);
				}
			}
		});
		buttonNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				notationIndex++;
				playNotation();
			}
		});
		buttonNextNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				notationIndex+=2;
				playNotation();
			}
		});
		buttonPrev.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				notationIndex--;
				playNotation();
			}
		});
		buttonPrevPrev.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				notationIndex-=2;
				playNotation();
			}
		});
		treeChessDB.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if(arg0.getClickCount() == 2)
				{
					if(treeChessDB.getLastSelectedPathComponent() != null && treeChessDB.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode)
					{
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeChessDB.getLastSelectedPathComponent();
						if(node.isLeaf())
						{
							int option = JOptionPane.showConfirmDialog(MinChessAppMain.getInstance(), "Chess 기보를 불러오시겠습니까?", "", JOptionPane.YES_OPTION);
							if(option == JOptionPane.YES_OPTION)
							{
								setChessNotation((MinChessNotation)node.getUserObject());
							}
						}
					}
				}
			}
		});
	}
	
	/**
	 * 모든 말의 정보를 초기화 합니다.
	 */
	public void initPiece(boolean effect)
	{
		imagePanelBoard.removeImageAll();
		imagePanelBoard.addImage(MinChessImages.IMAGE_BOARD);
		
		blackPieceMap = new HashMap<String, MinPiece>();
		whitePieceMap = new HashMap<String, MinPiece>();
		chessEngine = new MinChessEngine(whitePieceMap, blackPieceMap);
		
		blackRook1 = new MinRook(MinPiece.BLACK_TEAM, new Point(0, 0), chessEngine);
		blackKnight1 = new MinKnight(MinPiece.BLACK_TEAM, new Point(1, 0), chessEngine);
		blackBishop1 = new MinBishop(MinPiece.BLACK_TEAM, new Point(2, 0), chessEngine);
		blackQueen = new MinQueen(MinPiece.BLACK_TEAM, new Point(3, 0), chessEngine);
		blackKing = new MinKing(MinPiece.BLACK_TEAM, new Point(4, 0), chessEngine);
		blackBishop2 = new MinBishop(MinPiece.BLACK_TEAM, new Point(5, 0), chessEngine);
		blackKnight2 = new MinKnight(MinPiece.BLACK_TEAM, new Point(6, 0), chessEngine);
		blackRook2 = new MinRook(MinPiece.BLACK_TEAM, new Point(7, 0), chessEngine);
		blackPawn1 = new MinPawn(MinPiece.BLACK_TEAM, new Point(0, 1), chessEngine);
		blackPawn2 = new MinPawn(MinPiece.BLACK_TEAM, new Point(1, 1), chessEngine);
		blackPawn3 = new MinPawn(MinPiece.BLACK_TEAM, new Point(2, 1), chessEngine);
		blackPawn4 = new MinPawn(MinPiece.BLACK_TEAM, new Point(3, 1), chessEngine);
		blackPawn5 = new MinPawn(MinPiece.BLACK_TEAM, new Point(4, 1), chessEngine);
		blackPawn6 = new MinPawn(MinPiece.BLACK_TEAM, new Point(5, 1), chessEngine);
		blackPawn7 = new MinPawn(MinPiece.BLACK_TEAM, new Point(6, 1), chessEngine);
		blackPawn8 = new MinPawn(MinPiece.BLACK_TEAM, new Point(7, 1), chessEngine);
		
		blackPieceMap.put(blackRook1.getPositionStr(), blackRook1);
		blackPieceMap.put(blackKnight1.getPositionStr(), blackKnight1);
		blackPieceMap.put(blackBishop1.getPositionStr(), blackBishop1);
		blackPieceMap.put(blackQueen.getPositionStr(), blackQueen);
		blackPieceMap.put(blackKing.getPositionStr(), blackKing);
		blackPieceMap.put(blackBishop2.getPositionStr(), blackBishop2);
		blackPieceMap.put(blackKnight2.getPositionStr(), blackKnight2);
		blackPieceMap.put(blackRook2.getPositionStr(), blackRook2);
		blackPieceMap.put(blackPawn1.getPositionStr(), blackPawn1);
		blackPieceMap.put(blackPawn2.getPositionStr(), blackPawn2);
		blackPieceMap.put(blackPawn3.getPositionStr(), blackPawn3);
		blackPieceMap.put(blackPawn4.getPositionStr(), blackPawn4);
		blackPieceMap.put(blackPawn5.getPositionStr(), blackPawn5);
		blackPieceMap.put(blackPawn6.getPositionStr(), blackPawn6);
		blackPieceMap.put(blackPawn7.getPositionStr(), blackPawn7);
		blackPieceMap.put(blackPawn8.getPositionStr(), blackPawn8);
		
		whiteRook1 = new MinRook(MinPiece.WHITE_TEAM, new Point(0, 7), chessEngine);
		whiteKnight1 = new MinKnight(MinPiece.WHITE_TEAM, new Point(1, 7), chessEngine);
		whiteBishop1 = new MinBishop(MinPiece.WHITE_TEAM, new Point(2, 7), chessEngine);
		whiteQueen = new MinQueen(MinPiece.WHITE_TEAM, new Point(3, 7), chessEngine);
		whiteKing = new MinKing(MinPiece.WHITE_TEAM, new Point(4, 7), chessEngine);
		whiteBishop2 = new MinBishop(MinPiece.WHITE_TEAM, new Point(5, 7), chessEngine);
		whiteKnight2 = new MinKnight(MinPiece.WHITE_TEAM, new Point(6, 7), chessEngine);
		whiteRook2 = new MinRook(MinPiece.WHITE_TEAM, new Point(7, 7), chessEngine);
		whitePawn1 = new MinPawn(MinPiece.WHITE_TEAM, new Point(0, 6), chessEngine);
		whitePawn2 = new MinPawn(MinPiece.WHITE_TEAM, new Point(1, 6), chessEngine);
		whitePawn3 = new MinPawn(MinPiece.WHITE_TEAM, new Point(2, 6), chessEngine);
		whitePawn4 = new MinPawn(MinPiece.WHITE_TEAM, new Point(3, 6), chessEngine);
		whitePawn5 = new MinPawn(MinPiece.WHITE_TEAM, new Point(4, 6), chessEngine);
		whitePawn6 = new MinPawn(MinPiece.WHITE_TEAM, new Point(5, 6), chessEngine);
		whitePawn7 = new MinPawn(MinPiece.WHITE_TEAM, new Point(6, 6), chessEngine);
		whitePawn8 = new MinPawn(MinPiece.WHITE_TEAM, new Point(7, 6), chessEngine);
		
		whitePieceMap.put(whiteRook1.getPositionStr(), whiteRook1);
		whitePieceMap.put(whiteKnight1.getPositionStr(), whiteKnight1);
		whitePieceMap.put(whiteBishop1.getPositionStr(), whiteBishop1);
		whitePieceMap.put(whiteQueen.getPositionStr(), whiteQueen);
		whitePieceMap.put(whiteKing.getPositionStr(), whiteKing);
		whitePieceMap.put(whiteBishop2.getPositionStr(), whiteBishop2);
		whitePieceMap.put(whiteKnight2.getPositionStr(), whiteKnight2);
		whitePieceMap.put(whiteRook2.getPositionStr(), whiteRook2);
		whitePieceMap.put(whitePawn1.getPositionStr(), whitePawn1);
		whitePieceMap.put(whitePawn2.getPositionStr(), whitePawn2);
		whitePieceMap.put(whitePawn3.getPositionStr(), whitePawn3);
		whitePieceMap.put(whitePawn4.getPositionStr(), whitePawn4);
		whitePieceMap.put(whitePawn5.getPositionStr(), whitePawn5);
		whitePieceMap.put(whitePawn6.getPositionStr(), whitePawn6);
		whitePieceMap.put(whitePawn7.getPositionStr(), whitePawn7);
		whitePieceMap.put(whitePawn8.getPositionStr(), whitePawn8);
		
		// 흙색 팀의 말에 각 팀 정보와 로그를 설정하고 화면에 표시합니다.
		Object [] blackKeys = blackPieceMap.keySet().toArray();
		for(Object key : blackKeys)
		{
			MinPiece piece = blackPieceMap.get(key);
			piece.setWhitePieceMap(whitePieceMap);
			piece.setBlackPieceMap(blackPieceMap);
			piece.setLog(log);
			if(!effect)
			{
				setPiece(piece);
			}
		}
		// 흰색 팀의 말에 각 팀 정보와 로그를 설정하고 화면에 표시합니다.
		Object [] whiteKeys = whitePieceMap.keySet().toArray();
		for(Object key : whiteKeys)
		{
			MinPiece piece = whitePieceMap.get(key);
			piece.setWhitePieceMap(whitePieceMap);
			piece.setBlackPieceMap(blackPieceMap);
			piece.setLog(log);
			if(!effect)
			{
				setPiece(piece);
			}
		}
		if(effect)
		{
			displayPiece(effect);
		}
	}
	
	/**
	 * 말의 정보를 화면에 표시합니다.
	 * @param effect ; 하나씩 표시하는 효과 사용 여부
	 */
	public void displayPiece(final boolean effect)
	{
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>()
		{
			@Override
			protected Integer doInBackground() throws Exception 
			{
				try
				{
					// 흙색 팀의 말에 각 팀 정보와 로그를 설정하고 화면에 표시합니다.
					Object [] blackKeys = blackPieceMap.keySet().toArray();
					Arrays.sort(blackKeys);
					for(Object key : blackKeys)
					{
						MinPiece piece = blackPieceMap.get(key);
						setPiece(piece);
						if(effect)
						{
							Thread.sleep(30);
							imagePanelBoard.repaint();
						}
					}
					// 흰색 팀의 말에 각 팀 정보와 로그를 설정하고 화면에 표시합니다.
					Object [] whiteKeys = whitePieceMap.keySet().toArray();
					Arrays.sort(whiteKeys);
					for(Object key : whiteKeys)
					{
						MinPiece piece = whitePieceMap.get(key);
						setPiece(piece);
						if(effect)
						{
							Thread.sleep(30);
							imagePanelBoard.repaint();
						}
					}
					repaintPiece();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
				System.out.println("end");
				return -1;
			}
		};
		int n = 1000;
		ExecutorService threadPool = Executors.newFixedThreadPool(n);
		threadPool.submit(worker);
		worker.execute();
	}
	
	/**
	 * 속성값 기준으로 Tree 에 Chess 기보 정보를 입력합니다.
	 * @param properties
	 */
	public void initChessDB(String properties)
	{
		loadChessDB = MinChessDB.loadChessDB(chessEngine);
		HashMap<String, ArrayList<MinChessNotation>> nodeInfosMap = new HashMap<String, ArrayList<MinChessNotation>>();
		for(MinChessNotation notation : loadChessDB)
		{
			String key = notation.getProperties(properties);
			if(properties.equals("Name"))
			{
				for(MinProperties propertie : notation.getProperties())
				{
					if(propertie.getKey().toUpperCase().equals("WHITE"))
					{
						key = propertie.getStringValue();
					}
					if(propertie.getKey().toUpperCase().equals("BLACK"))
					{
						key = propertie.getStringValue();
					}
					if(key != null)
					{
						if(nodeInfosMap.containsKey(key))
						{
							nodeInfosMap.get(key).add(notation);
						}
						else
						{
							ArrayList<MinChessNotation> nodeInfos = new ArrayList<MinChessNotation>();
							nodeInfos.add(notation);
							nodeInfosMap.put(key, nodeInfos);
						}
						key = null;
					}
				}
			}
		}
		Object [] keys = nodeInfosMap.keySet().toArray();
		Arrays.sort(keys);
		for(Object key : keys)
		{
			ArrayList<MinChessNotation> nodeInfos = nodeInfosMap.get(key);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
			for(MinChessNotation notation : nodeInfos)
			{
				String white = null;
				String black = null;
				for(MinProperties propertie : notation.getProperties())
				{
					if(propertie.getKey().toUpperCase().equals("WHITE"))
					{
						white = propertie.getStringValue();
					}
					if(propertie.getKey().toUpperCase().equals("BLACK"))
					{
						black = propertie.getStringValue();
					}
				}
				DefaultMutableTreeNode gameNode = null;
				if(white != null && black != null)
				{
					gameNode = new DefaultMutableTreeNode("\"" + white + "\"" +  " VS " + "\"" + black + "\"");
				}
				else
				{
					gameNode = new DefaultMutableTreeNode(notation.getProperties("Date"));
				}
				gameNode.setUserObject(notation);
				node.add(gameNode);
				modelChessDB.add(node);
			}
		}
		treeChessDB.expandRow(0);
	}
	
	/**
	 * Chess 기보 정보를 설정합니다.
	 * @param notation
	 */
	private void setChessNotation(MinChessNotation notationInfo)
	{
		this.notationIndex = 0;
		this.notationInfo = notationInfo;
		initPiece(false);
		buttonPrevPrev.setEnabled(true);
		buttonPrev.setEnabled(true);
		buttonNextNext.setEnabled(true);
		buttonNext.setEnabled(true);
		// 기보 정보를 초기화 합니다.
		if(notationInfo.getPgnLog() != null)
		{
			notationInfo.setChessEngine(chessEngine);
			notationInfo.initNotation();
		}
		initNotationLog();
	}
	
	/**
	 * 기보 속성 정보를 로그에 설정합니다.
	 */
	private void initNotationLog()
	{
		log.delete(0, log.length());
		log.append("===========================\n");
		for(MinProperties properties : notationInfo.getProperties())
		{
			log.append(properties.getKey() + " : " + properties.getStringValue() + "\n");
		}
		log.append("===========================\n");
		initLog();
	}
	
	/**
	 * 마우스 포인트에 위치한 말을 선택합니다.
	 * @param point - mouse point
	 */
	private void selectedPiece(Point point)
	{
		removeBox();
		removeLineBox();
		ImageIcon imageIcon = imagePanelBoard.getImageIcon(point);
		for(MinPiece piece : whitePieceMap.values())
		{
			piece.setSelected(false);
			if(piece.getImage().equals(imageIcon))
			{
				selectedPiece = piece;
				selectedPiece.setSelected(true);
				setBlueBox(selectedPiece.getPosition());
				
				for(Point movablePoint : chessEngine.getMovablePositions(chessEngine.getPiece(), selectedPiece.getPositionX(), selectedPiece.getPositionY()))
				{
					if(chessEngine.isOverlapEnemyPiece(chessEngine.getPiece(), selectedPiece.getPositionX(), selectedPiece.getPositionY(), movablePoint.x, movablePoint.y))
					{
						setRedBox(movablePoint);
					}
					else
					{
						setBlueBox(movablePoint);
					}
				}
			}
		}
		repaint();
	}
	
	/**
	 * 마우스 포인트에 위치한 말을 선택합니다.
	 * @param point - mouse point
	 */
	private void selectedRedLineBox(Point point)
	{
		removeLineBox();
		if(selectedPiece != null && selectedPiece.isSelected())
		{
			setRedLineBox(point);
			repaint();
		}
	}
	
	/**
	 * 마우스 포인트에 이동 가능한지 여부를 반환합니다.
	 * @param point
	 * @return
	 */
	private boolean movePiece(Point point)
	{
		removeBox();
		removeLineBox();
		int x = (point.x-40)/80;
		int y = (point.y-40)/80;
		Point movePoint = new Point(x, y);
		for(MinPiece piece : whitePieceMap.values())
		{
			if(piece.isSelected() && piece.isMovable(movePoint))
			{
				boolean movable = piece.movePosition(movePoint);
				piece.setSelected(false);
				if(movable)
				{
					initLog();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 말의 변경된 정보를 화면에 표시합니다.
	 */
	public void repaintPiece()
	{
		if(blackPieceMap != null && whitePieceMap != null)
		{
			panelBlackDeadPiece.removeAll();
			panelWhiteDeadPiece.removeAll();
			Object [] blackKeys = blackPieceMap.keySet().toArray();
			Object [] whiteKeys = whitePieceMap.keySet().toArray();
			Object [] keys = new Object[blackKeys.length+whiteKeys.length];
			for(int i=0; i<blackKeys.length; i++)
			{
				keys[i] = blackKeys[i];
			}
			for(int i=0; i<whiteKeys.length; i++)
			{
				keys[blackKeys.length+i] = whiteKeys[i];
			}
			Arrays.sort(keys);
			for(Object key : keys)
			{
				MinPiece blackPiece = blackPieceMap.get(key);
				MinPiece whitePiece = whitePieceMap.get(key);
				if(blackPiece != null)
				{
					if(blackPiece.isAlive())
					{
						setPiece(blackPiece);
					}
					else
					{
						removePiece(blackPiece);
						if(blackPiece instanceof MinKing)
						{
							JOptionPane.showMessageDialog(null, "You Win!!");
							return;
						}
					}
				}
				else if(whitePiece != null)
				{
					if(whitePiece.isAlive())
					{
						setPiece(whitePiece);
					}
					else
					{
						removePiece(whitePiece);
						if(whitePiece instanceof MinKing)
						{
							JOptionPane.showMessageDialog(null, "You Lose!!");
							return;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 말을 화면에 표시합니다.
	 * @param piece
	 */
	public void setPiece(MinPiece piece)
	{
		int x = (piece.getPositionX())*80 + ((80-piece.getImage().getIconWidth())/2) + 37;
		int y = (piece.getPositionY())*80 + (110-piece.getImage().getIconHeight());
		addImage(piece.getImage(), x, y);
	}
	
	/**
	 * 말을 화면의 point 위치에 표시합니다.
	 * @param piece
	 * @param point
	 */
	public void setPiece(MinPiece piece, Point point)
	{
		int x = point.x;
		int y = point.y;
		addImage(piece.getImage(), x-20, y-20);
		selectedRedLineBox(point);
	}
	
	/**
	 * 화면에 표시된 말을 제거합니다.
	 * @param piece
	 */
	public void removePiece(MinPiece piece)
	{
		if(piece.getTeam().equals(MinPiece.BLACK_TEAM))
		{
			panelBlackDeadPiece.add(new JLabel(piece.getImage()));
		}
		else if(piece.getTeam().equals(MinPiece.WHITE_TEAM))
		{
			panelWhiteDeadPiece.add(new JLabel(piece.getImage()));
		}
		removeImage(piece.getImage());
	}
	
	/**
	 * point 위치에 파란색 상자를 표시합니다.
	 * @param point
	 */
	public void setBlueBox(Point point)
	{
		ImageIcon blueBox = MinUtils.createImage(MinChessImages.BLUE_BOX, 80, 80);
		blueBox.setDescription("MOVE");
		addImage(blueBox, (point.x)*80+38, (point.y)*80+38);
		repaint();
	}
	
	/**
	 * point 위치에 붉은색 상자를 표시합니다.
	 * @param point
	 */
	public void setRedBox(Point point)
	{
		ImageIcon redBox = MinUtils.createImage(MinChessImages.RED_BOX, 80, 80);
		redBox.setDescription("ATTACK");
		addImage(redBox, (point.x)*80+38, (point.y)*80+38);
		repaint();
	}
	
	/**
	 * point 위치에 붉은색 점선 상자를 표시합니다.
	 * @param point
	 */
	public void setRedLineBox(Point point)
	{
		int x = point.x;
		int y = point.y;
		x = (x - 38)/80;
		y = (y - 38)/80;
		
		ImageIcon redlineBox = MinUtils.createImage(MinChessImages.RED_LINE_BOX, 80, 80);
		redlineBox.setDescription("LINE");
		if(x < 8 && y < 8)
		{
			addImage(redlineBox, x*80+38, y*80+38);
			repaint();
		}
	}
	
	/**
	 * 상자 표시를 제거합니다.
	 */
	public void removeBox()
	{
		ArrayList<ImageIcon> dotBoxs = new ArrayList<ImageIcon>();
		for(ImageIcon image : imagePanelBoard.getImageIcons())
		{
			if(image.getDescription() != null)
			{
				if(image.getDescription().equals("MOVE") || image.getDescription().equals("ATTACK"))
				{
					dotBoxs.add(image);
				}
			}
		}
		for(ImageIcon image : dotBoxs)
		{
			imagePanelBoard.removeImage(image);
		}
	}
	
	/**
	 * 상자 표시를 제거합니다.
	 */
	public void removeLineBox()
	{
		ArrayList<ImageIcon> dotBoxs = new ArrayList<ImageIcon>();
		for(ImageIcon image : imagePanelBoard.getImageIcons())
		{
			if(image.getDescription() != null)
			{
				if(image.getDescription().equals("LINE"))
				{
					dotBoxs.add(image);
				}
			}
		}
		for(ImageIcon image : dotBoxs)
		{
			imagePanelBoard.removeImage(image);
		}
	}
	
	/**
	 * x, y 좌표에 이미지를 추가합니다.
	 * @param image
	 * @param x
	 * @param y
	 */
	public void addImage(ImageIcon image, int x, int y)
	{
		if(imagePanelBoard != null)
		{
			if(imagePanelBoard.containsImage(image))
			{
				imagePanelBoard.removeImage(image);
			}
			imagePanelBoard.addImage(image, x, y);
		}
		repaint();
	}
	
	/**
	 * 이미지를 제거합니다.
	 * @param image
	 */
	public void removeImage(ImageIcon image)
	{
		if(imagePanelBoard != null)
		{
			imagePanelBoard.removeImage(image);
		}
		repaint();
	}
	
	/**
	 * 로그를 화면에 표시합니다.
	 */
	public void initLog()
	{
		textAreaLog.setText(log.toString());
	}
	
	/**
	 * 기보 정보를 Play 합니다.
	 */
	public void playNotation()
	{
		// 뒤로 가기 앞으로 가기 등 처리를 간단히 하기 위해서 
		// 초기화 한 후 해당 notation 까지 진행하는 방식으로 처리합니다.
		initPiece(false);
		initNotationLog();
		MinChessMove move = notationInfo.getMove();
		for(int i=0; i<notationIndex; i++)
		{
			if(move.getNodes() != null)
			{
				move = move.getNodes().get(0);
				if(whitePieceMap.containsKey(move.getY() + "_" + move.getX()))
				{
					whitePieceMap.get(move.getY() + "_" + move.getX()).movePositionPiece(new Point(move.getMoveX(), move.getMoveY()));
				}
				else if(blackPieceMap.containsKey(move.getY() + "_" + move.getX()))
				{
					blackPieceMap.get(move.getY() + "_" + move.getX()).movePositionPiece(new Point(move.getMoveX(), move.getMoveY()));
				}
			}
			else
			{
				repaintPiece();
				JOptionPane.showMessageDialog(null, "게임이 종료되었습니다.");
				return;
			}
			try
			{
				Thread.sleep(5);
			}
			catch(Exception e) { e.printStackTrace(); }
		}
		repaintPiece();
	}
	
	public void addNotation(MinPiece piece, MinChessMove move)
	{
		// TODO
	}
	
	@Override
	public void repaint() 
	{
		super.repaint();
	}
}
