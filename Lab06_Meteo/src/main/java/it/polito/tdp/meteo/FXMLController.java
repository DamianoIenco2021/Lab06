/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	
    	Integer m = boxMese.getValue();
    	
    	if(m!=null) {
    		List<Citta>best= model.trovaSequenza(m);
    		
    		txtResult.appendText(String.format("Sequenza ottima per il mese %s\n", Integer.toString(m)));
    		txtResult.appendText(best + "\n");
    	}
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	
    	Integer m = boxMese.getValue();
    	
    	if(m!=null) {
    		txtResult.appendText(String.format("Dati del mese %s\n", Integer.toString(m)));
    		
    		for(Citta c :model.getLeCitta()) {
    			Double u = model .getUmiditaMedia(m, c);
    			txtResult.appendText(String.format("Citta %s: umidità %f\n", c.getNome(), u));
    		}
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    private void setChoiceMesi() {
    	for(int mese=1; mese<=12; mese++ ) {
    		boxMese.getItems().add(mese);
    	}
    }
    
    private void setModel (Model m) {
    	this.model=m;
    	setChoiceMesi();
    }
}

