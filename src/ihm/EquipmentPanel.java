package ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;
import security.Equipement;
import sockets.Client;
import sockets.Server;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;

public class EquipmentPanel extends WebPanel {
	
	private Equipement eq;
	private WebPanel header = new WebPanel(new MigLayout("alignx center,aligny center"));
	private WebPanel toolsPanel = new WebPanel(new MigLayout(""));
	private WebPanel displayPanel = new WebPanel(new MigLayout(""));
	public static WebTextArea console = new WebTextArea();
	private WebButton details = new WebButton("Equipment details");
	private WebButton ca = new WebButton("Certificate Authorities");
	private WebButton da = new WebButton("Derivate Authorities");
	private WebButton insertServer = new WebButton("Insertion as Server");
	private WebButton insertClient = new WebButton("Insertion as Client");
	private WebButton synchronizeServer = new WebButton("Synchronize as Server");
	private WebButton synchronizeClient = new WebButton("Synchronize as Client");
	
	
	
	public EquipmentPanel(Equipement eq)
	{
		this.eq=eq;
		//this.setBackground(Color.black);
		console.setLineWrap(true);
		buildLayout();
		buildListeners();
	}
	
	private void buildLayout()
	{
		this.setLayout(new MigLayout("insets 1 1 1 1"));
		this.add(header,"w 100%,h 10%,wrap");
		WebPanel innerPanel = new WebPanel(new MigLayout("insets 0 0 0 0"));
		innerPanel.add(toolsPanel,"w 15%,h 100%");
		innerPanel.add(displayPanel,"w 85%,h 100%");
		this.add(innerPanel,"w 100%,h 90%");
		
		
		header.setUndecorated(false);
		toolsPanel.setUndecorated(false);
		displayPanel.setUndecorated(false);
		
		
		WebLabel name = new WebLabel(eq.monNom());
		name.setFontSize(30);
		header.add(name);
		toolsPanel.add(details,"wrap");
		toolsPanel.add(ca,"wrap");
		toolsPanel.add(da,"wrap");
		toolsPanel.add(insertServer,"wrap");
		toolsPanel.add(insertClient, "wrap");
		toolsPanel.add(synchronizeServer,"wrap");
		toolsPanel.add(synchronizeClient);
		
		
		console.setBackground(Color.black);
		console.setForeground(Color.green);
		displayPanel.add(console,"w 100%,h 100%");
	}
	private void buildListeners()
	{
		details.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eq.affichage();
				
			}
			
		});
		ca.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eq.affichage_ca();
				
			}
			
		});
		da.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eq.affichage_da();
				
			}
			
		});
		insertServer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						Server s = eq.createServer();
						eq.insertAsServer(s);
						eq.closeServer(s);
						
						return null;
					}
					
				};
				worker.execute();
				
				
				
			}
			
		});
		insertClient.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						Client c = eq.createClient();
						eq.insertAsClient(c);;
						eq.closeClient(c);
						
						return null;
					}
					
				};
				worker.execute();
			}
			
		});
		synchronizeServer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						Server s = eq.createServer();
						eq.synchronizeAsServer(s);
						eq.closeServer(s);
						return null;
					}
					
				};
				worker.execute();
				
				
			}
			
		});
		synchronizeClient.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						Client c = eq.createClient();
						eq.synchronizeAsClient(c);;
						eq.closeClient(c);
						return null;
					}
					
				};
				worker.execute();
			}
			
		});
		
	}
	

}
