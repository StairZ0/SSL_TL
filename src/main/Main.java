package main;
import ihm.PrincipalPanel;

import java.awt.Dimension;
import java.security.Security;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.utils.SwingUtils;

import security.Equipement;
import sockets.Server;


public class Main {
	public static WebFrame frame = new WebFrame("Equipment");

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		SwingUtils.invokeLater(new Runnable(){

			@Override
			public void run() {
				WebLookAndFeel.install ();
				frame.setUndecorated(false);
				frame.setDefaultCloseOperation(WebFrame.DISPOSE_ON_CLOSE);
				frame.setContentPane(new PrincipalPanel());
				frame.setSize(new Dimension(1024,768));
				frame.setVisible(true);
				
				
			}
			
		});
		
		Equipement e = new Equipement("Equipement1", 801);
		Server s = new Server(e);
		
	}

}
