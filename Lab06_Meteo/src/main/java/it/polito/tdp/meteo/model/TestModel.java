package it.polito.tdp.meteo.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		List<Citta> leCitta=m.getLeCitta();
		
		
		for(Citta c : leCitta) {
		System.out.println(m.getUmiditaMedia(12,c));
		}
		
		System.out.println(m.trovaSequenza(5));
		

	}

}
