package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.Main;

public class AddBookController extends Controller {
	
	@FXML
	private TextField title_field, author_field;
	
	@FXML
	private Button confirm_but, back_but;

	@FXML
	void addBook(ActionEvent event) {
		if(title_field.getText().isEmpty() || author_field.getText().isEmpty()) {
			System.out.println("ERROR: Fill in all required fields!");
			return;
		}
		
		Main.gm.addBook(title_field.getText(), author_field.getText());
		title_field.clear();
		author_field.clear();
	}
}
