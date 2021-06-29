package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("\nCalcola artisti connessi\n");
    	String ruolo = this.boxRuolo.getValue();
    
    	if(ruolo==null) {
       		this.txtResult.appendText("\nATTENZIONE! Seleziona un ruolo\n");
       		return;
       	}
    	List<Adiacenza> ad = new ArrayList<>(model.getAdiacenze(ruolo));
    	for(Adiacenza a: ad)
    		this.txtResult.appendText(a + "\n");
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("\nCalcola percorso\n");
    	int id;
    	if(this.txtArtista.getText() == null) {
    		this.txtResult.appendText("ATTENZIONE! Inserisci un ID\n");
    		return;
    	}
    	try {
    		id = Integer.parseInt(this.txtArtista.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.appendText("ATTENZIONE! Inserisci un numero\n");
    		return;
    	}
    	
    	if(!model.getIdArtisti().contains(id)) {
    		this.txtResult.appendText("ATTENZIONE! ID non valido\n");
    		return;
    	}
    	this.txtResult.appendText("\nNumero esposizioni condivise per massimizzare il risultato: " + model.getNumEsposizioniPercorsoMax() +"\n");
    	for(Artist a: model.cercaPercorso(id)) {
    		this.txtResult.appendText(a + "\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo==null) {
    		this.txtResult.appendText("\nATTENZIONE! Seleziona un ruolo\n");
    		return;
    	}
    	model.creaGrafo(ruolo);
    	this.btnArtistiConnessi.setDisable(false);
    	this.btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(model.getRuoli());
    	this.btnArtistiConnessi.setDisable(true);
    	this.btnCalcolaPercorso.setDisable(true);
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
