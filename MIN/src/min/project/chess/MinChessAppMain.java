package min.project.chess;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import min.com.MinFrame;
import min.project.chess.com.MinChessMenuBar;
import min.project.chess.com.MinChessWorkspace;


/**
 * 개요 : CHESS 프로그램을 시작합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessAppMain 
{
	private static MinChessWorkspace workspace;
	private static MinChessMenuBar menuBar;
	private static MinFrame frame;
	
	/**
	 * 생성자
	 * @param args
	 */
	public MinChessAppMain(String [] args)
	{
		ini();
	}
	
	/**
	 * 초기화
	 */
	private void ini()
	{
		Integer width = 1200;
		Integer height = 790;
		
		menuBar = new MinChessMenuBar();
		
		frame = new MinFrame("MIN Chess", width, height);
		frame.setDefaultCloseOperation(MinFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				int confirm = JOptionPane.showConfirmDialog(MinChessAppMain.getInstance(), "Min Chess 프로그램을 종료하시겠습니까?", "Min Chess", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			}
		});
		frame.setJMenuBar(menuBar);
		frame.setResizable(false);
		workspace = new MinChessWorkspace();
		frame.setWorkspace(workspace);
		frame.repaint();
	}
	
	/**
	 * Frame 을 반환합니다.
	 * @return
	 */
	public static MinFrame getInstance()
	{
		return frame;
	}
	
	/**
	 * 작업영역을 반환합니다.
	 * @return
	 */
	public static MinChessWorkspace getWorkspace()
	{
		return workspace;
	}
	
	/**
	 * 메뉴를 반환합니다.
	 * @return
	 */
	public static MinChessMenuBar getMenuBar()
	{
		return menuBar;
	}
	
	/**
	 * Frame 의 repaint 를 실행합니다.
	 */
	public void repaint()
	{
		frame.repaint();
	}
	
	public static void main(String [] args)
	{
		try 
		{ 
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
		}
		catch(Exception e) { e.printStackTrace(); }
		new MinChessAppMain(args);
	}
}
