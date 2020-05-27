package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.Main;

import java.io.IOException;

public class UserHomeController extends Controller {
	
	@FXML
	private Button suggestion_but;
	
	@FXML
	private Button wishlist_but;
	
	@FXML
	private Button catalogue_but;
	
	@FXML
	private Button read_but;
	
	@FXML
	void cataloguePage(ActionEvent event) throws IOException
	{
		Main.changeScene(4);
	}
	
	@FXML
	void readBooksPage(ActionEvent event) throws IOException
	{
		//Main.changeScene(5);
	}
	
	@FXML
	void suggestionPage(ActionEvent event) throws IOException
	{
		Main.changeScene(2);
	}
	
	@FXML
	void wishListPage(ActionEvent event) throws IOException
	{
		Main.changeScene(3);
	}
	
}

