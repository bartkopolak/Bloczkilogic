package thingy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class PropertiesWindow extends JFrame {
	private JTextField NameField;
	private JSpinner heightSpinner;
	private JSpinner widthSpinner;
	Color color;
	Bloczek targetBloczek;

	public PropertiesWindow(Bloczek b) {
		if(b == null) System.out.println("Pusty bloczek?");
		targetBloczek = b;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 470);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		color = b.getOrigColor();
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(OKAction);
		
		JButton btnCancel = new JButton("Anuluj");
		btnCancel.addActionListener(CancelAction);
		panel.add(btnCancel);
		panel.add(btnOK);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {80, 250};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblNazwa = new JLabel("Nazwa");
		GridBagConstraints gbc_lblNazwa = new GridBagConstraints();
		gbc_lblNazwa.insets = new Insets(0, 0, 5, 5);
		gbc_lblNazwa.anchor = GridBagConstraints.EAST;
		gbc_lblNazwa.gridx = 0;
		gbc_lblNazwa.gridy = 0;
		panel_1.add(lblNazwa, gbc_lblNazwa);
		
		NameField = new JTextField();
		NameField.setText(b.getName());
		GridBagConstraints gbc_NameField = new GridBagConstraints();
		gbc_NameField.insets = new Insets(0, 0, 5, 0);
		gbc_NameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_NameField.gridx = 1;
		gbc_NameField.gridy = 0;
		panel_1.add(NameField, gbc_NameField);
		NameField.setColumns(10);
		
		JLabel lblSzeroko = new JLabel("Szerokość");
		GridBagConstraints gbc_lblSzeroko = new GridBagConstraints();
		gbc_lblSzeroko.anchor = GridBagConstraints.EAST;
		gbc_lblSzeroko.insets = new Insets(0, 0, 5, 5);
		gbc_lblSzeroko.gridx = 0;
		gbc_lblSzeroko.gridy = 1;
		panel_1.add(lblSzeroko, gbc_lblSzeroko);
		
		widthSpinner = new JSpinner();
		widthSpinner.setModel(new SpinnerNumberModel(5, 5, 100, 1));
		widthSpinner.setValue(b.width);
		GridBagConstraints gbc_widthSpinner = new GridBagConstraints();
		gbc_widthSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_widthSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_widthSpinner.gridx = 1;
		gbc_widthSpinner.gridy = 1;
		panel_1.add(widthSpinner, gbc_widthSpinner);
		
		JLabel lblWysoko = new JLabel("Wysokość");
		GridBagConstraints gbc_lblWysoko = new GridBagConstraints();
		gbc_lblWysoko.anchor = GridBagConstraints.EAST;
		gbc_lblWysoko.insets = new Insets(0, 0, 5, 5);
		gbc_lblWysoko.gridx = 0;
		gbc_lblWysoko.gridy = 2;
		panel_1.add(lblWysoko, gbc_lblWysoko);
		
		heightSpinner = new JSpinner();
		heightSpinner.setModel(new SpinnerNumberModel(10, 5, 100, 1));
		heightSpinner.setValue(b.height);
		GridBagConstraints gbc_heightSpinner = new GridBagConstraints();
		gbc_heightSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_heightSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_heightSpinner.gridx = 1;
		gbc_heightSpinner.gridy = 2;
		panel_1.add(heightSpinner, gbc_heightSpinner);
		
		JLabel lblKolor = new JLabel("Kolor");
		GridBagConstraints gbc_lblKolor = new GridBagConstraints();
		gbc_lblKolor.anchor = GridBagConstraints.EAST;
		gbc_lblKolor.insets = new Insets(0, 0, 0, 5);
		gbc_lblKolor.gridx = 0;
		gbc_lblKolor.gridy = 3;
		panel_1.add(lblKolor, gbc_lblKolor);
		
		JButton btnWybierz = new JButton("Wybierz");
		btnWybierz.setBackground(color);
		btnWybierz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame guiFrame = new JFrame();
				Color selectedColor = JColorChooser.showDialog(guiFrame, "Pick a Color"
				                , color);
				if(selectedColor != null){
					color = selectedColor;
					btnWybierz.setBackground(color);
				}
				
			}
		});
		GridBagConstraints gbc_btnWybierz = new GridBagConstraints();
		gbc_btnWybierz.gridx = 1;
		gbc_btnWybierz.gridy = 3;
		panel_1.add(btnWybierz, gbc_btnWybierz);
		setVisible(true);
	}

	AbstractAction OKAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			targetBloczek.setName(NameField.getText());
			targetBloczek.height = (int)heightSpinner.getModel().getValue();
			targetBloczek.width = (int)widthSpinner.getModel().getValue();
			targetBloczek.setOrigColor(color);
			targetBloczek.setColor(color.darker());
			dispose();
			setVisible(false);
		}
	};
	
	AbstractAction CancelAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			setVisible(false);
		}
	};
}
