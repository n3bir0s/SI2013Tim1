package ba.unsa.etf.si.tim1.GUI;

import ba.unsa.etf.si.tim1.Entiteti.Zaposlenik;
import ba.unsa.etf.si.tim1.Entiteti.TipUposlenika;
import ba.unsa.etf.si.tim1.Hibernate.HibernatePristupniPodaci;
import ba.unsa.etf.si.tim1.Hibernate.HibernateZaposlenik;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Login extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private Zaposlenik korisnik;
	
	public Login() {
		//ubijOnogaKoJePravioHibernate();
		inicijalizirajBazu();
		
		setTitle("jKP");
		setSize(300, 400);
		JPanel jp = new JPanel();
		jp.setBackground(Color.WHITE);
		jp.setBounds(0, 0, 300, 600);
		getContentPane().add(jp);
		jp.setLayout(null);
		
		ImageIcon image = new ImageIcon("logo.jpg");
        JLabel logo = new JLabel("", image, JLabel.CENTER);
        logo.setBounds(100 ,80, image.getIconWidth(), image.getIconHeight());
        jp.add(logo);
        
        JLabel lblKorisnikoIme = new JLabel("Korisni\u010Dko ime: ");
        lblKorisnikoIme.setBounds(10, 224, 98, 14);
        lblKorisnikoIme.setHorizontalAlignment(SwingConstants.RIGHT);
        jp.add(lblKorisnikoIme);
        
        JLabel lblifra = new JLabel("Šifra: ");
        lblifra.setBounds(22, 249, 86, 14);
        lblifra.setHorizontalAlignment(SwingConstants.RIGHT);
        jp.add(lblifra);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(112, 221, 150, 20);
        jp.add(txtUsername);
        txtUsername.setColumns(10);
        txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
	                prijava();
	            }
			}
		});
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(112, 246, 150, 20);
        jp.add(txtPassword);
        txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
	                prijava();
	            }
			}
		});
        
        JButton btnPrijava = new JButton("Prijava");
        btnPrijava.setBackground(Color.LIGHT_GRAY);
        btnPrijava.setBounds(87, 301, 117, 25);
        btnPrijava.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		prijava();
        	}
        });
        btnPrijava.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
	                prijava();
	            }
			}
		});
        jp.add(btnPrijava);
        
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
	}
	
	public void prijava() {
		try {
			long id = HibernatePristupniPodaci.provjeriPodatke(txtUsername.getText(), txtPassword.getText());
			korisnik = HibernateZaposlenik.dajZaposlenikaPoPristupnimPodacima(id);
			if ((korisnik.getTipUposlenika().equals(TipUposlenika.neaktivan.toString()))) throw new Exception("Neaktivan korisnik !");
			if ((korisnik.getTipUposlenika().equals(TipUposlenika.izbrisan.toString()))) throw new Exception("Izbrisani korisnik !");
			GlavniProzor prozor = new GlavniProzor(korisnik);
    		prozor.setVisible(true);
    		setVisible(false);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	// /*Djelomično*/ Potpuno riješava problem spašavanja enumeracija u bazu upotrebom crne magije
	/*void ubijOnogaKoJePravioHibernate() {
		HibernateZaposlenik.ubijOnogaKoJePravioHibernate();
		HibernateRadniNalog.ubijOnogaKoJePravioHibernate();
	}*/
	
	// Ukoliko je baza prazna napravi korisnika admin/admin
	void inicijalizirajBazu() {
		try {
			if (HibernatePristupniPodaci.dajBrojKorisnika() == 0) {
				long podaci = HibernatePristupniPodaci.spremiPodatke("admin", "admin");
				//HibernateZaposlenik.ubijOnogaKoJePravioHibernate(); // danas smo brutalni...
				Zaposlenik z = new Zaposlenik("Administrator", "Administrator", TipUposlenika.privilegirani.toString(), podaci);
				HibernateZaposlenik.pohraniZaposlenika(z, podaci);
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Greška", JOptionPane.WARNING_MESSAGE);
		}
	}
}
