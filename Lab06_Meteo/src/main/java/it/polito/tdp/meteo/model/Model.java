package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List <Citta>leCitta;
	private List<Citta>best;
	
	public List<Citta> getLeCitta(){
		return leCitta;
	}
	
	// of course you can change the String output with what you think works best
	public Double getUmiditaMedia(int mese,Citta citta) {
		
		MeteoDAO dao = new MeteoDAO();
		return dao.getUmiditaMedia(mese, citta);
	}

	public Model() {
		MeteoDAO dao= new MeteoDAO();
		this.leCitta=dao.getAllCitta();
	}


	
	// of course you can change the String output with what you think works best
	/**
	 * Calcola la sequenza ottimale di visita della citta nel mese specificato
	 * @param mese
	 * @return
	 */
	public List<Citta> trovaSequenza(int mese) {
		List<Citta>parziale = new ArrayList<>();
		this.best=null;
		
		MeteoDAO dao= new MeteoDAO();
		
		for(Citta c :leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale,0);
		
		return best;
	}

	/**
	 * Procedura ricorsiva per il calcolo delle citta ottimali 
	 * per informazioni sull'impostazione della ricorsione , vedere il filenlogica della ricorsione.txt
	 * nella cartella del progetto
	 * @param parziale soluzione parziale in via di costruzione
	 * @param livello = livello della ricorsione, cioè il giorno in cui si sta cercando di definire la citta
	 */
	private void cerca(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			//caso terminale 
			Double costo=calcolaCosto(parziale);
			if(best==null || costo< calcolaCosto(best)) {
				best= new ArrayList<>(parziale);
			}
			
		}else {
			for(Citta prova :leCitta) {
				if(aggiuntaValida(prova,parziale)) {
					parziale.add(prova);
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	
	}

	

	/**
	 * Calcola il costo di una determinata soluzione (totale)
	 * @param parziale
	 * @return
	 */
	private Double calcolaCosto(List<Citta> parziale) {
		double costo= 0.0;
		
		//sommatoria delle umidità di ciascuna città considerando il rilevamento del giorno giusto
		//SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		for(int giorno=1; giorno<= NUMERO_GIORNI_TOTALI; giorno++) {
			//dove mi trovo
			Citta c = parziale.get(giorno-1);
			//che umidità ho quel giorno in quella città=?
			double umid= c.getRilevamenti().get(giorno-1).getUmidita();
			costo+=umid;
			
			
		}
		
		//poi devo sommare 100*numero di volte cin cui cambio città
		for(int giorno=2; giorno<=NUMERO_GIORNI_TOTALI; giorno ++) {
			//dove mi trovo
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo+=COST;
			}
		}
		return costo;
	}
	

	/**
	 * Verifica se, data la soluzione {@code parziale} gia definita, sia lecito 
	 * aggiungere la città {@code prova}, rispettando i vincoli sui numeri giorni minimi e messimi di permanenza.
	 * 
	 * @param prova la citta che sto cercando di aggiungere
	 * @param parziale la sequenza di citta gia composta
	 * @return {@code true} se {@code prova} è lecita,  {@code false } se invece viola qualche vincolo
	 * 
	 */

	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		//Verifica giorni massimi
		//contiamo quante volte la città  'prova' era gia apparsa nell'attuale listacostruita fin qui
		
		int conta=0;
		for(Citta precedente :parziale) {
			if(precedente.equals(prova))
				conta++;
		}
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		

		// verifica dei giorni minimi
		if (parziale.size()==0) //primo giorno posso inserire qualsiasi città
				return true;
		if (parziale.size()==1 || parziale.size()==2) {
			//siamo al secondo o terzo giorno, non posso cambiare
			//quindi l'aggiunta è valida solo se la città di prova coincide con la sua precedente
			return parziale.get(parziale.size()-1).equals(prova); 
		}
		//nel caso generale, se ho già passato i controlli sopra, non c'è nulla che mi vieta di rimanere nella stessa città
		//quindi per i giorni successivi ai primi tre posso sempre rimanere
		if (parziale.get(parziale.size()-1).equals(prova))
			return true; 
		// se cambio città mi devo assicurare che nei tre giorni precedenti sono rimasto fermo 
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
		&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
			
		return false;
	}


}
