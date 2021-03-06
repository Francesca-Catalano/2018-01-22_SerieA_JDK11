package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.SeasondAndPoints;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	if(this.boxSquadra.getValue()==null)
    	{ this.txtResult.appendText("seleziona squadra!\n");
    		return;}
    if(	this.model.listSeasondAndPoints(this.boxSquadra.getValue().getTeam())== null)
    	return;
    for(SeasondAndPoints s : 	this.model.listSeasondAndPoints(this.boxSquadra.getValue().getTeam()))
    {
    	this.txtResult.appendText(s.toString()+"\n");
    }

    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	if(this.boxSquadra.getValue()==null)
    	{ this.txtResult.appendText("seleziona squadra!\n");
    		return;}
    	String team = this.boxSquadra.getValue().getTeam();
    	this.model.creaGrafo(team);
    	 this.txtResult.appendText("Vertici :"+ this.model.vertexSize());
    	 this.txtResult.appendText("Edge :"+ this.model.edgeSize());
    	 this.txtResult.appendText("Punteggio :"+ this.model.getBest());
    	 this.txtResult.appendText("Season :"+ this.model.getBestSeason().getSeason());
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(this.model.listTeams());
	}
}
