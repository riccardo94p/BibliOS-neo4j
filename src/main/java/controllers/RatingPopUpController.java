package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.Main;
import main.java.models.Book;

import java.io.IOException;

public class RatingPopUpController extends Controller {
	
	@FXML
	private MenuButton rating_but;
	
	@FXML
	private Button confirm_but;
	
	private Book selectedBook;
	private int returnPage; //specifies which interface I shall return to
	
	public void setSelectedBook(Book b) {
		this.selectedBook = b;
	}
	
	public void setReturnPage(int p) { this.returnPage = p; }
	
	@FXML
	void confirmRating(ActionEvent event) throws IOException
	{
		if(menuOption != null)
		{
			System.out.println("Rating "+menuOption+"/5 stars book: "+this.selectedBook);
			Main.gm.addRating(this.selectedBook.getBookId(), Main.lm.getIdNode(), Integer.parseInt(menuOption));
			Main.changeScene(returnPage);
		}
	}
	
	@FXML
	protected void setMenuOption(ActionEvent event) {
		menuOption = ((MenuItem) event.getSource()).getText();
		rating_but.setText(menuOption);
	}
}
