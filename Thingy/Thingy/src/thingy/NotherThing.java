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

public class NotherThing extends JFrame implements ComponentListener{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel iloscLabel;
	private JToggleButton tglbtnLine;
	private JToggleButton tglbtnSelect;
	Bloczek selectedBlock;		//do przekazania bloczka do okna wlasciwosci
	thingy.PlotnoPanel.DrawingMode mode;
	PlotnoPanel canvas;
	BufferedImage area;
	Graphics g;
	Point mousexy;
	Point mouseStart;
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
		
		//ustaw tryb plotna na rysowanie linii
		mode = PlotnoPanel.DrawingMode.LINE;
		
		tglbtnLine = new JToggleButton("Line");
		tglbtnLine.addActionListener(LineModeAction);
		tglbtnLine.setSelected(true);
		panel.add(tglbtnLine);
		
		tglbtnSelect = new JToggleButton("Select");
		tglbtnSelect.addActionListener(SelectModeAction);
		panel.add(tglbtnSelect);
		
		iloscLabel= new JLabel("ILOSC:");
		panel.add(iloscLabel);
		
		//stworz bitmape
		area = new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
						
		//pobranie obiektu Graphics z bitmapy i wyczysczenie 
		g = area.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, area.getWidth(), area.getHeight());
		
		//tworznie scrollPane do plotna
		
		//tworzenie obiektu płotna, na ktorym wyswietlona bedzie bitmapa
		canvas = new PlotnoPanel(area, listaBloczkuf, selectedBlock, mode);
		canvas.setSize(area.getWidth(), area.getHeight());
		canvas.repaint();
		//odswiez plotno
		canvas.repaint();
		JScrollPane scrollPane = new JScrollPane(canvas);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		//uruchom wątek odswiezajacy plotno
		Thread mouseThread = new Thread(mouseRunnable);
		mouseThread.start();
	}
	
	
	public void paintBloczki(){
		g.setColor(Color.white);
		g.fillRect(0, 0, area.getWidth(), area.getHeight());
		
		
		for (Bloczek b: listaBloczkuf){
			g.setColor(b.getColor());
			g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
		for(Bloczek b: listaBloczkuf){
			g.setColor(Color.BLACK);
			g.drawString(b.getName(), b.getX(), b.getY()+b.getHeight()+12);
		}
		
		DrawingHandler();
		canvas.repaint();
	}
	
	//Wątek odswiezający płótno
	
	Runnable mouseRunnable = new Runnable(){

		@Override
		public synchronized void run() {
			
			while(true){
			paintBloczki();
			canvas.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		 
			
		}
		
	};
	


	void DrawingHandler(){
		if(canvas.isDragged() && !canvas.isEditMode()){
			canvas.setLineDrawingMode(true);
			g.setColor(Color.black);
			
			mouseStart = canvas.mouseStart;
			mousexy = canvas.mousexy;
			
			if(mode == PlotnoPanel.DrawingMode.SELECT){
				Rectangle selection = new Rectangle();
				if(mouseStart.x <= mousexy.x && mouseStart.y <= mousexy.y)
					selection.setBounds(mouseStart.x, mouseStart.y, mousexy.x-mouseStart.x, mousexy.y-mouseStart.y);
				else if(mouseStart.x > mousexy.x && mouseStart.y <= mousexy.y)
					selection.setBounds(mousexy.x, mouseStart.y, mouseStart.x-mousexy.x, mousexy.y-mouseStart.y);
				else if(mouseStart.x <= mousexy.x && mouseStart.y > mousexy.y)
					selection.setBounds(mouseStart.x, mousexy.y, mousexy.x-mouseStart.x, mouseStart.y-mousexy.y);
				else if(mouseStart.x > mousexy.x && mouseStart.y > mousexy.y)
					selection.setBounds(mousexy.x, mousexy.y, mouseStart.x-mousexy.x, mouseStart.y-mousexy.y);
				
				g.drawRect(selection.x, selection.y, selection.width, selection.height);
				for(Bloczek b : listaBloczkuf){
					if(selection.contains( new Point((b.getX() + b.getWidth()/2), (b.getY() + b.getHeight()/2)) )){
							
							b.setSelected(true);
							b.Highlight();
					}
					else{
						b.setSelected(false);
						b.DeHighlight();
					}
				}
			}
			else if(mode == PlotnoPanel.DrawingMode.LINE){
				g.drawLine(mouseStart.x, mouseStart.y, mousexy.x, mousexy.y);
			}
		}
		else ;
	}

	
	//-----------------------ACTIONS-----------------------------
	
	AbstractAction LineModeAction = new AbstractAction() {
		
		/**
		 * ustawia tryb plotna w tryb rysowania linii
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			mode = PlotnoPanel.DrawingMode.LINE;
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
			mode = PlotnoPanel.DrawingMode.SELECT;
			tglbtnLine.setSelected(false);
			tglbtnSelect.setSelected(true);
			
		}
	};
	
	AbstractAction addButtonAction = new AbstractAction(){

		private static final long serialVersionUID = 2L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			listaBloczkuf.add(new Bloczek(canvas.getSize()));
			paintBloczki();
			iloscLabel.setText("ILOSC: " + Bloczek.iloscBloczkuf + " a w liscie do cholery jest: " + listaBloczkuf.size());
			
		}
		
	};
	
	
	AbstractAction SaveAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Na razie nieobsługiwane.");
			
		}
	};
	
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
