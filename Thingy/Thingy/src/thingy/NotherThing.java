package thingy;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import thingy.Plotno.DrawingMode;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.ScrollPaneConstants;
import javax.swing.JToolBar;

public class NotherThing extends JFrame implements ComponentListener{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel iloscLabel;
	private JToggleButton tglbtnLine;
	private JToggleButton tglbtnSelect;
	Bloczek selectedBlock;		//do przekazania bloczka do okna wlasciwosci
	PlotnoPanel canvas;

	List<Bloczek> listaBloczkuf = new ArrayList<Bloczek>();
	
	JMenuBar menuBar;
	JMenu mnPlik;
	JMenuItem mntmZapisz;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotherThing frame = new NotherThing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NotherThing() {
		
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 866, 704);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);
		
		mntmZapisz = new JMenuItem("Zapisz");
		mntmZapisz.addActionListener(SaveAction);
		
		mntmDebug = new JMenuItem("Debug");
		mntmDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new List_debug(listaBloczkuf);
			}
		});
		mnPlik.add(mntmDebug);
		mnPlik.add(mntmZapisz);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		//toolbar
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		//przycisk dodawania nowych bloczków
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(addButtonAction);
		panel.add(btnNewButton);
		
		toolBar = new JToolBar();
		panel.add(toolBar);
		
		
		tglbtnLine = new JToggleButton("Line");
		toolBar.add(tglbtnLine);
		tglbtnLine.addActionListener(LineModeAction);
		tglbtnLine.setSelected(true);
		
		tglbtnSelect = new JToggleButton("Select");
		toolBar.add(tglbtnSelect);
		tglbtnSelect.addActionListener(SelectModeAction);
		
		iloscLabel= new JLabel("ILOSC:");
		panel.add(iloscLabel);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		

		
		//tworzenie obiektu płotna, na ktorym wyswietlona bedzie bitmapa
		canvas = new PlotnoPanel(listaBloczkuf, selectedBlock);
		canvas.setBounds(0, 0, 642, 482);
		panel_1.add(canvas);
		canvas.setDoubleBuffered(true);
		canvas.setAlignmentX(Component.LEFT_ALIGNMENT);
		canvas.setAlignmentY(Component.TOP_ALIGNMENT);
		//canvas.setSize(area.getWidth(), area.getHeight());
		//odswiez plotno
		canvas.repaint();
		
		//uruchom wątek odswiezajacy plotno
		Thread mouseThread = new Thread(mouseRunnable);
		mouseThread.start();
	}
	
	//Wątek odswiezający płótno
	
	Runnable mouseRunnable = new Runnable(){

		@Override
		public synchronized void run() {
			
			while(true){
			canvas.repaint();
			if(canvas != null)
				iloscLabel.setText("isDragged = " + canvas.isDragged.toString() + ", blockDragMode = " + canvas.blockDragMode.toString() + ", linedrawingMode= " + Boolean.toString(canvas.isLineDrawingMode()) + ", isdrawingLine = " + Boolean.toString(canvas.isDrawingLine));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		 
			
		}
		
	};

	
	//-----------------------ACTIONS-----------------------------
	
	AbstractAction LineModeAction = new AbstractAction() {
		
		/**
		 * ustawia tryb plotna w tryb rysowania linii
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			canvas.drawMode = PlotnoPanel.DrawingMode.LINE;
			tglbtnLine.setSelected(true);
			tglbtnSelect.setSelected(false);
			
		}
	};
	AbstractAction SelectModeAction = new AbstractAction() {
		
		/**
		 * ustawia tryb plotna w tryb rysowania linii
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			canvas.drawMode = PlotnoPanel.DrawingMode.SELECT;
			tglbtnLine.setSelected(false);
			tglbtnSelect.setSelected(true);
			
		}
	};
	
	AbstractAction addButtonAction = new AbstractAction(){

		private static final long serialVersionUID = 2L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new TestBloczek(canvas.getSize(), listaBloczkuf, 1, 5);
			//paintBloczki();
			iloscLabel.setText("w liscie do cholery jest: " + listaBloczkuf.size());
			
		}
		
	};
	
	
	AbstractAction SaveAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Na razie nieobsługiwane.");
			
		}
	};
	private JPanel panel_1;
	private JToolBar toolBar;
	private JMenuItem mntmDebug;
	
	//-----------------------------------------EVENTS----------------------------------------------------

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		//BufferedImage area = new BufferedImage(canvas.getWidth(), canvas.getHeight(), )
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
