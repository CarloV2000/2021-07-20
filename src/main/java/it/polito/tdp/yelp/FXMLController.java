/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.SimResult;
import it.polito.tdp.yelp.model.User;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<String> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer anno = this.cmbAnno.getValue();
    	String n = this.txtN.getText();
    	Integer nMinRecensioni;
    	if(anno == null) {
    		this.txtResult.setText("Inserire un anno nella box Anno");
    		return;
    	}
    	if(n == null) {
    		this.txtResult.setText("Inserire un valore nel field N");
    		return;
    	}
    	try {
    		nMinRecensioni = Integer.parseInt(n);
    		
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Inserire un valore numerico nel field N");
    		return;
    	}
    	String s = model.creaGrafo(anno, nMinRecensioni);
    	this.txtResult.setText(s);
    	for(User x : model.getGrafo().vertexSet()) {
    		String utente = x.getUserId()+"-"+x.getName();
    		this.cmbUtente.getItems().add(utente);
    	}
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	String s = "\nUsers con massima similarità: \n";
    	String u = this.cmbUtente.getValue();
    	String uID = u.substring(0, u.indexOf("-"));
    	User x = this.model.getIdMapUsers().get(uID);
    	Integer anno = this.cmbAnno.getValue();
    	List<User>res = new ArrayList<>(model.getAllUtentiConMaggiorSimilarita(x));
    	for(User y : res) {
    		s += y.getUserId()+" "+y.getName()+" ("+model.getMaxSimilarita()+")\n";
    	}
    	this.txtResult.appendText(s);
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	String x1s = this.txtX1.getText();
    	String x2s = this.txtX2.getText();
    	Integer x1;
    	Integer x2;
    	Integer anno = this.cmbAnno.getValue();
    	String nMinRecensioniS = this.txtN.getText();
    	Integer nMinRecensioni = Integer.parseInt(nMinRecensioniS);
    	try {
    		x1 = Integer.parseInt(x1s);
    		
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Inserire un valore numerico nel field x1");
    		return;
    	}
    	try {
    		x2 = Integer.parseInt(x2s);
    		
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Inserire un valore numerico nel field x2");
    		return;
    	}
    	SimResult res = model.simula(x1, x2, anno, nMinRecensioni);
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        for(int i = 2005; i<= 2013; i++) {
        	this.cmbAnno.getItems().add(i);
        }
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
