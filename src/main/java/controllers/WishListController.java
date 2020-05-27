package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.Main;
import main.java.models.Book;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WishListController extends Controller
{
	@FXML
	private Label welcome_msg;
	
	@FXML
	private TableView<Book> book_table;
	
	@FXML
	private TableColumn idCol, titleCol, authorCol, ratingCol;
	
	@FXML
	private Button mark_but, next_but, previous_but, remove_but;
	
	@FXML
	private Label page_count;
	
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		welcome_msg.setText("Welcome, " + Controller.getUsername());
		
		//blocking table column resize
		titleCol.setResizable(false);
		authorCol.setResizable(false);
		ratingCol.setResizable(false);
		
		//associating the table's column with the corresponding attributes of the book class
		idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookId"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("avgRating"));
		
		tableOffset = 0;
		updateTable();
		
		previous_but.setDisable(true);
		totalPages = ((Main.gm.getNumWish(Main.lm.getIdNode()) + 14)/15);
		currentPage = 1;
		if(totalPages == 1) next_but.setDisable(true);
		
		page_count.setText("Page " + currentPage + " of " + totalPages);
	}
	
	private void updateTable() {
		List<Book> tmpBooks = Main.gm.getWishList(Main.lm.getIdNode(), tableOffset*15);
		
		ObservableList<Book> books = FXCollections.observableArrayList();
		for(Book b: tmpBooks)
			books.add(b);
		
		book_table.setItems(books);
	}
	
	@FXML
	public void nextPage(ActionEvent ev) {
		super.nextPage();
		updateTable();
	}
	
	@FXML
	public void previousPage(ActionEvent ev) {
		super.previousPage();
		updateTable();
	}
	
	@FXML
	void removeSelected(ActionEvent event) {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();
		
		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}
		
		System.out.println("Removed book from WishList: "+selectedBook.getBookId()+", "+ selectedBook.getTitle()+", "+ selectedBook.getAuthor());
		Main.gm.removeWish(Main.lm.getIdNode(), selectedBook.getBookId());
		
		//refresh table
		totalPages = ((Main.gm.getNumWish(Main.lm.getIdNode()) + 14)/15);
		updateTable();
	}
	
	@FXML
	void markRead(ActionEvent event) throws IOException
	{
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();
		
		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}
		Main.ratingPopUp(selectedBook, 3); //3 for wish page
	}

}