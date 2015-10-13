package ihm;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import security.Equipement;
import main.Main;
import net.miginfocom.swing.MigLayout;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;

public class PrincipalPanel extends WebPanel implements ActionListener{
	
	private WebButton createEquipment = new WebButton("Create Equipment");
	private WebPanel eqPanel = new WebPanel(new MigLayout("insets 0 0 0 0"));
	private WebPanel initPanel = new WebPanel();
	private WebTextField idName = new WebTextField(20);
	private WebTextField port = new WebTextField(6);
	
	
	public PrincipalPanel()
	{
		buildLayout();
		createEquipment.addActionListener(this);
		
	}
	
	public void buildLayout()
	{
		this.setLayout(new CardLayout());
		this.add(initPanel);
		this.add(eqPanel);
		initPanel.setVisible(true);
		eqPanel.setVisible(false);
		
		WebLabel lidName = new WebLabel("Name : ");
		WebLabel lport = new WebLabel("Port : ");
		initPanel.setLayout(new MigLayout("alignx center,aligny center"));
		WebPanel innerPanel1 = new WebPanel(new MigLayout("insets 0 0 0 0"));
		WebPanel innerPanel2 = new WebPanel(new MigLayout("insets 0 0 0 0"));
		WebPanel innerPanel3 = new WebPanel(new MigLayout("insets 0 0 0 0"));
		WebPanel innerPanel4 = new WebPanel(new MigLayout("alignx center,aligny center"));
		innerPanel1.add(lidName,"wrap");
		innerPanel1.add(idName);
		innerPanel2.add(lport,"wrap");
		innerPanel2.add(port);
		innerPanel3.add(innerPanel1);
		innerPanel3.add(innerPanel2);
		innerPanel4.add(innerPanel3,"wrap");
		innerPanel4.add(createEquipment,"alignx center");
		innerPanel4.setUndecorated(false);
		initPanel.add(innerPanel4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int port = Integer.parseInt(this.port.getText());
		String idName = this.idName.getText();
		
		if(port<1||port>65535)
		{
			final WebPopOver popOver = new WebPopOver (Main.frame);
            popOver.setCloseOnFocusLoss ( true );
            popOver.setMargin ( 10 );
            popOver.setLayout ( new VerticalFlowLayout () );
            WebLabel label = new WebLabel("Wrong Port !");
            label.setHorizontalAlignment(WebLabel.CENTER);
            popOver.add ( label );
            popOver.show ( ( WebButton ) e.getSource () );
            return;
		}
		
		Equipement eq = null;
		try {
			eq = new Equipement(idName,port);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EquipmentPanel ep = new EquipmentPanel(eq);
		eqPanel.add(ep,"w 100%,h 100%");
		eqPanel.setVisible(true);
		initPanel.setVisible(false);
		
		
		
	}

}
