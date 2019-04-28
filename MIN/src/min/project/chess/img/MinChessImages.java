package min.project.chess.img;

import java.net.URL;

import javax.swing.ImageIcon;

import min.utils.MinUtils;

/**
 * 개요 : 이미지를 관리합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessImages
{
	public static URL TITLE = MinChessImages.class.getResource("chesstitle.png");
	public static URL BACKGROUND = MinChessImages.class.getResource("background.png");
	public static URL BOARD = MinChessImages.class.getResource("board.png");
	public static URL WHITE_KING = MinChessImages.class.getResource("WhiteKing.png");
	public static URL BLACK_KING = MinChessImages.class.getResource("BlackKing.png");
	public static URL WHITE_QUEEN = MinChessImages.class.getResource("WhiteQueen.png");
	public static URL BLACK_QUEEN = MinChessImages.class.getResource("BlackQueen.png");
	public static URL WHITE_BISHOP = MinChessImages.class.getResource("WhiteBishop.png");
	public static URL BLACK_BISHOP = MinChessImages.class.getResource("BlackBishop.png");
	public static URL WHITE_ROOK = MinChessImages.class.getResource("WhiteRook.png");
	public static URL BLACK_ROOK = MinChessImages.class.getResource("BlackRook.png");
	public static URL WHITE_KNIGHT = MinChessImages.class.getResource("WhiteKnight.png");
	public static URL BLACK_KNIGHT = MinChessImages.class.getResource("BlackKnight.png");
	public static URL WHITE_PAWN = MinChessImages.class.getResource("WhitePawn.png");
	public static URL BLACK_PAWN = MinChessImages.class.getResource("BlackPawn.png");
	public static URL BLUE_BOX = MinChessImages.class.getResource("BlueBox.png");
	public static URL RED_BOX = MinChessImages.class.getResource("RedBox.png");
	public static URL RED_LINE_BOX = MinChessImages.class.getResource("RedLineBox.png");
	
	public static ImageIcon IMAGE_BOARD = MinUtils.createImage(BOARD, 720, 720);
	public static ImageIcon IMAGE_TITLE = MinUtils.createImage(TITLE, 720, 510);
}
