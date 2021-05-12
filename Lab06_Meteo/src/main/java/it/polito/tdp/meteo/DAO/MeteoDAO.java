package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	//public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita)
	/**
	 * Carica i rilevamenti di una determinata città in un determinato mese
	 * @param mese
	 * @param citta
	 * @return
	 */

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {
		
		final String  sql = "SELECT localita, DATA, umidita FROM situazione "
				+ "WHERE localita =? AND MONTH(DATA)=? "
				+ "ORDER BY DATA asc";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setString(2, Integer.toString(mese));

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}	
		
}
          /**
           * Elenco tutte le città
           * @return
           */

	public List<Citta> getAllCitta(){
		final String sql= "SELECT DISTINCT localita "
				+" FROM situazione ORDER BY localita";
		
		List<Citta> result = new ArrayList<Citta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	
	/**Dato un mese ed una città estrarre dal DB l'umidità media relativa a tale mese e tale città 
	 * (tutti i calcoli sono delegati al database
	 * @mese
	 * @citta
	 * @return
	 */
	
	public Double getUmiditàMedia(int mese,Citta citta) {
		final String sql= "SELECT AVG(Umidita) AS U "
				+ "FROM situazione "
				+ "WHERE localita=? "
				+ "AND MONTH(DATA)=? ";
		
	
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, citta.getNome());
			//st.setString(2, mese.getValue()); se fosse un oggetto month
			st.setString(2, Integer.toString(mese)); 

			ResultSet rs = st.executeQuery();

			rs.next(); // si posiziona sulla prima (ed unica) riga
			Double u = rs.getDouble("U");
			
			conn.close();
			return u;
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
}
