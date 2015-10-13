package ihm;

import net.miginfocom.swing.MigLayout;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;

public class InitPanel extends WebPanel {
	
	private WebButton createEquipment = new WebButton();
	private WebTextField idName = new WebTextField(20);
	private WebTextField port = new WebTextField(6);
	
	
	public InitPanel()
	{
		
	}
	
	public void buildLayout()
	{
		WebLabel lidName = new WebLabel("Nom : ");
		WebLabel lport = new WebLabel("Port : ");
		setLayout(new MigLayout());
	}

}
