package ba.unsa.etf.si.tim1.jKP;

public class PristupniPodaci {
	private String korisnickoIme;
	private String lozinka;
	
	public PristupniPodaci(String korisnickoIme, String lozinka) {
		setKorisnickoIme(korisnickoIme);
		setLozinka(lozinka);
	}
	
	public String getKorisnickoIme() {
		return korisnickoIme;
	}
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
}