/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.AlbumBilancio;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	
    	Album a = this.cmbA1.getValue();
    	
    	if(a == null) {
    		txtResult.appendText("Selezionare un album");
    		return;
    	}
    	else {
    		List<AlbumBilancio> lista = this.model.getCammino(a);
    		
    		for(AlbumBilancio a1: lista) {
    			txtResult.appendText(a1.toString()+"\n");
    		}
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	txtResult.clear();
    	Album a1 = this.cmbA1.getValue();
    	Album a2 = this.cmbA2.getValue();
    	
    	if(a1 == null) {
    		txtResult.appendText("Errore: selezionare un album");
    		return;
    	}
    	else if(a2 == null) {
    		txtResult.appendText("Errore: selezionare un album");
    		return;
    	}
    	
    	String tx = txtX.getText();
    	int x;
    	try {
    		x = Integer.parseInt(tx);
    	}catch(NumberFormatException n) {
    		txtResult.appendText("Inserire un x numerico");
    		return;
    	}
    	
    	 
    	List<Album> lista = this.model.calcolaPercorso(a1, a2, x);
    	
    	for(Album a: lista) {
    		txtResult.appendText(a.toString() + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	String testo = txtN.getText();
    	int n;
    	if(testo == null) {
    		txtResult.appendText("Errore: inserire un valore n");
    		return;
    	}
    	
    	try {
    		n = Integer.parseInt(testo);
    		
    	}catch (NumberFormatException nfe) {
    		txtResult.appendText("Errore: inserire un valore numerico intero");
    		return;
    		}
    	
    		String msg = this.model.creaGrafo(n);
    		txtResult.appendText(msg);
    		
    		this.cmbA1.getItems().addAll(this.model.getAlbum());
    		this.cmbA2.getItems().addAll(this.model.getAlbum());
    		
    	}

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
