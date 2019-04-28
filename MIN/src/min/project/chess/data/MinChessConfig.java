package min.project.chess.data;

import java.net.URL;

import min.utils.MinXml;

/**
 * 개요 : 환경 설정 정보를 관리합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessConfig 
{
	public static URL CONFIG = MinChessDB.class.getResource("config.txt");
	
	/**
	 * 사용자 이름을 반환합니다.
	 * @return
	 */
	public static String getPlayerName()
	{
		return new MinXml(CONFIG).searchChildNode("Player").getTextContent();
	}
	
	/**
	 * Chess Engine Level 을 반환합니다.<br>
	 * 몇수 앞까지 시뮬레이션할지 설정합니다.<br>
	 * 테스트 결과 5수 이상은 OutOfMemoryError 에러가 발생하기 때문에 4까지로 제한합니다.
	 * @return
	 */
	public static int getChessEngineLevel()
	{
		try
		{
			int level = Integer.valueOf(new MinXml(CONFIG).searchChildNode("Level").getTextContent());
			return level;
//			if(level < 4)
//			{
//				return level;
//			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 4;
	}
	
	/**
	 * Player 이름을 설정합니다.
	 * @param player
	 */
	public static void setPlayerName(String player)
	{
		String level = String.valueOf(getChessEngineLevel());
		MinXml xml = new MinXml("Config");
		xml.createChildNode("Player", player);
		xml.createChildNode("Level", level);
		xml.write(CONFIG);
	}
	
	/**
	 * Chess Engine Level 을 설정합니다.
	 * @param level
	 */
	public static void setChessEngineLevel(String level)
	{
		String player = getPlayerName();
		MinXml xml = new MinXml("Config");
		xml.createChildNode("Player", player);
		xml.createChildNode("Level", level);
		xml.write(CONFIG);
	}
}
