package min.project.chess.com;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * 개요 : CHESS Menu Bar 를 구성합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	
	private JMenu menuFile;
	private JMenu menuOption;
	private JMenu menuHelp;

	public MinChessMenuBar()
	{
		super();
		
		ini();
	}
	
	private void ini()
	{
		menuFile = new JMenu("File");
		
		menuOption = new JMenu("Option");
		
		menuHelp = new JMenu("Help");
		
		add(menuFile);
		add(menuOption);
		add(menuHelp);
	}
}
