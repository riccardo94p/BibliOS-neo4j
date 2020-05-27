package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.Main;

import java.io.IOException;


public class AdminHomeController extends Controller
{
	@FXML
	void cataloguePage(ActionEvent event) throws IOException
	{
		Main.changeScene(4);
	}
	
	@FXML
	void addBook(ActionEvent event) throws IOException
	{
		Main.changeScene(6);
	}
}
