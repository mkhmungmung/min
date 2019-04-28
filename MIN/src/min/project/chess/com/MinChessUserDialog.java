package min.project.chess.com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import min.com.MinPanel;
import min.project.chess.MinChessAppMain;
import min.project.chess.data.MinChessConfig;

/**
 * 개요 : <br>
 * 작성일 : 2014. 8. 25.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinChessUserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JButton buttonOk;
	private JButton buttonCancel;
	
	private MinPanel panelCenter;
	private MinPanel panelSouth;
	private MinPanel panelPlayerName;
	
	private JLabel labelPlayerName;
	
	private JTextField textPlayerName;
	
	/**
	 * 생성자
	 * @param analyzer
	 */
	public MinChessUserDialog()
	{
		super(MinChessAppMain.getInstance(), "Player Name");
		
		ini();
	}
	
	/**
	 * 초기화
	 */
	private void ini()
	{
		labelPlayerName = new JLabel("Player Name", JLabel.CENTER);
		textPlayerName = new JTextField(10);
		
		panelPlayerName = new MinPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panelPlayerName.add(labelPlayerName);
		panelPlayerName.add(textPlayerName);
		
		panelCenter = new MinPanel(5);
		panelCenter.add(panelPlayerName);
		
		buttonOk = new JButton("OK");
		buttonOk.setPreferredSize(new Dimension(90, 20));
		buttonCancel = new JButton("Cancel");
		buttonCancel.setPreferredSize(new Dimension(90, 20));
		panelSouth = new MinPanel(new FlowLayout(FlowLayout.CENTER));
		panelSouth.add(buttonOk);
		panelSouth.add(buttonCancel);
		
		add(panelCenter);
		add(panelSouth, BorderLayout.SOUTH);
		
		setModal(true);
		setLocationRelativeTo(null);
		setSize(new Dimension(250, 120));
		
		initListener();
		
		setVisible(true);
	}
	
	private void initListener()
	{
		buttonOk.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) 
			{
				MinChessConfig.setPlayerName(textPlayerName.getText());
				MinChessUserDialog.this.dispose();
			}
		});
		buttonCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) 
			{
				MinChessUserDialog.this.dispose();
			}
		});
	}
}
