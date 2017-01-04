package thingy;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

public class List_debug extends JFrame {

	private JPanel contentPane;
	
	JList<Bloczek> list = new JList<Bloczek>();
	JList<Pin> list_1 = new JList<Pin>();
	JList<String> list_2 = new JList<String>();
	List<Bloczek> listBlock;
	List<Bloczek> noPinList;
	
	public List_debug(List<Bloczek> listBlock) {
		this.listBlock = listBlock;
		noPinList = new ArrayList<Bloczek>();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 762, 633);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		list.addListSelectionListener(listlistener);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list.setBounds(10, 27, 216, 556);
		contentPane.add(list);
		
		DefaultListModel<Bloczek> model1 = new DefaultListModel<Bloczek>();
		for(Bloczek b: listBlock){
			if(!(b instanceof Pin))
				noPinList.add(b);
		}
		for(Bloczek b: noPinList){
			model1.addElement(b);
		}
		list.setModel(model1);

		list_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list_1.setBounds(237, 27, 266, 556);
		list_1.addListSelectionListener(list1listener);
		contentPane.add(list_1);
		

		list_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list_2.setBounds(513, 27, 223, 556);
		contentPane.add(list_2);
		
		JLabel lblBloczki = new JLabel("Bloczki");
		lblBloczki.setBounds(10, 11, 46, 14);
		contentPane.add(lblBloczki);
		
		JLabel lblPiny = new JLabel("Piny");
		lblPiny.setBounds(237, 11, 46, 14);
		contentPane.add(lblPiny);
		
		JLabel lblInfo = new JLabel("Info:");
		lblInfo.setBounds(513, 11, 46, 14);
		contentPane.add(lblInfo);
		setVisible(true);
		
		
	}
	
	ListSelectionListener listlistener = new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent arg0) {
			DefaultListModel<Pin> model = new DefaultListModel<Pin>();
			for(Pin p : noPinList.get(list.getSelectedIndex()).pinList){
				model.addElement(p);
			}
			list_1.setModel(model);

		}
		
	};
	
	ListSelectionListener list1listener = new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent arg0) {
			Pin sel = list_1.getSelectedValue();
			if(sel != null){
				DefaultListModel<String> model = new DefaultListModel<String>();
				model.addElement("position: ("+sel.getX()+","+sel.y +")");
				model.addElement("type: " + sel.getType().toString());
				model.addElement("style: "+ sel.getStyle());
				model.addElement("connectedTo: "+sel.getConnectedPin());
				model.addElement("value: ");
				list_2.setModel(model);
			}
		}
	};
}
