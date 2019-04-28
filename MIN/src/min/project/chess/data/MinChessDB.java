package min.project.chess.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;

import min.project.chess.control.MinChessEngine;
import min.project.chess.control.MinChessNotation;

/**
 * 개요 : 기보 정보 파일을 Save/Load 합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessDB 
{
	public static URL CHESS_DATA = MinChessDB.class.getResource("ChessDB.pgn");
	
	/**
	 * 프로그램에서 저장한 기보 정보를 불러옵니다.
	 * @param chessEngine
	 * @return
	 */
	public static ArrayList<MinChessNotation> loadChessDB(MinChessEngine chessEngine)
	{
		ArrayList<MinChessNotation> chessNotations = new ArrayList<MinChessNotation>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(CHESS_DATA.toURI())));
			String readStr = "";
			StringBuffer stringBuffer = new StringBuffer();
			while((readStr = reader.readLine()) != null)
			{
				stringBuffer.append(readStr+"\n");
			}
			String [] logs = stringBuffer.toString().split("\n\n");
			for(int i=0; i<logs.length-1; i=i+2)
			{
				String log = logs[i] + "\n\n" + logs[i+1];
				chessNotations.add(new MinChessNotation(new StringBuffer(log), chessEngine));
			}
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return chessNotations;
	}
}
