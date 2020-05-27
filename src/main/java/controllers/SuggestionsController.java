package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.Main;
import main.java.models.Book;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SuggestionsController extends Controller {

	@FXML
	private Button logout_but, back, mark_but, add_but, view_but, tag_but, remove_but, stats_but;
	
	@FXML
	private Label welcome_msg;
	
	@FXML
	private TableView<Book> book_table;
	
	@FXML
	private TableColumn idCol, titleCol, authorCol, ratingCol;

	public void initialize(URL url, ResourceBundle resourceBundle) {
		welcome_msg.setText("Welcome, " + Controller.getUsername());

		//admin può taggare, votare ecc i libri?
		//if(Main.lm.getPrivilege()==1) remove_but.setVisible(true);

		//blocking table column resize
		idCol.setResizable(false);
		titleCol.setResizable(false);
		authorCol.setResizable(false);
		ratingCol.setResizable(false);

		//associating the table's column with the corresponding attributes of the book
		idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookId"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("avgRating"));

		tableOffset = 0;
		updateTable();
	}

	private void updateTable() {
		List<Book> tmpBooksR = Main.gm.getSuggestion(Main.lm.getIdNode());
		//List<Book> tmpBooksW = Main.gm.getSuggestion(Main.lm.getIdNode(), false);

		ObservableList<Book> books = FXCollections.observableArrayList();
		books.addAll(tmpBooksR);
		//books.addAll(tmpBooksW);

		book_table.setItems(books);
	}

	@FXML
	void addToWishList(ActionEvent event) {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();

		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}

		System.out.println("Selected book: "+selectedBook.getBookId()+", "+ selectedBook.getTitle()+", "+ selectedBook.getAuthor());
		Main.gm.addWish(Main.lm.getIdNode(), selectedBook.getBookId());
	}

	@FXML
	void viewStatistics(ActionEvent event) throws IOException {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();

		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}

		System.out.println("Selected book: "+selectedBook.getBookId()+", "+ selectedBook.getTitle()+", "+ selectedBook.getAuthor());
		Main.statsPopUp(selectedBook, 2);
	}
}