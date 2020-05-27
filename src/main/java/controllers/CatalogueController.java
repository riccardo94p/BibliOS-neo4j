package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.models.*;
import main.java.Main;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CatalogueController extends Controller {

	@FXML
	private Button logout_but, next_but, previous_but, back, mark_but, add_but, view_but, tag_but, remove_but, stats_but;
	
	@FXML
	private MenuButton search_filter;
	
	@FXML
	private Label welcome_msg;
	
	@FXML
	private TableView<Book> book_table;
	
	@FXML
	private TableColumn idCol, titleCol, authorCol, ratingCol;
	
	@FXML
	private Label page_count;
	
	@FXML
	private TextField tag_field;
	
	boolean viewRated = false;
	
	public void initialize(URL url, ResourceBundle resourceBundle) {
		welcome_msg.setText("Welcome, " + Controller.getUsername());
		
		//admin può taggare, votare ecc i libri?
		if(Main.lm.getPrivilege()==1) {
			remove_but.setVisible(true);
			stats_but.setVisible(true);

			mark_but.setVisible(false);
			add_but.setVisible(false);
			tag_but.setVisible(false);
			tag_field.setVisible(false);
			view_but.setVisible(false);
		}
		
		//blocking table column resize
		//idCol.setResizable(false);
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
		
		previous_but.setDisable(true);
		totalPages = ((Main.gm.getNumBooks(Main.lm.getIdNode(), false) + 14)/15);
		currentPage = 1;
		
		page_count.setText("Page " + currentPage + " of " + totalPages);
	}
	
	private void updateTable() {
		List<Book> tmpBooks = Main.gm.getBooks(Main.lm.getIdNode(), viewRated, tableOffset*15);
		
		ObservableList<Book> books = FXCollections.observableArrayList();
		for(Book b: tmpBooks)
			books.add(b);
		
		book_table.setItems(books);
	}
	
	private void updateTable(List<Book> tmpBooks) {
		ObservableList<Book> books = FXCollections.observableArrayList();
		for(Book b: tmpBooks)
			books.add(b);
		
		book_table.setItems(books);
	}
	
	
	@FXML
	void viewRated(ActionEvent event) {
		viewRated = !viewRated;
		if(viewRated) {
			view_but.setText("View non-Read");
			mark_but.setText("Change Rating");
			ratingCol.setText("My Rating");
			add_but.setDisable(true);
			totalPages = ((Main.gm.getNumBooks(Main.lm.getIdNode(), true)+14)/15);
			super.resetPageButtons();
		}
		else {
			view_but.setText("View Read");
			mark_but.setText("Mark as Read");
			ratingCol.setText("AVG Rating");
			add_but.setDisable(false);
			totalPages = ((Main.gm.getNumBooks(Main.lm.getIdNode(), false)+14)/15);
			super.resetPageButtons();
			//quando torno a NON-RATED  il total Page = 9999
		}
		
		updateTable();
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
	void markAsRead(ActionEvent event) throws IOException {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();
		
		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}
		Main.ratingPopUp(selectedBook, 4); //4: catalogue page
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
	void addTag(ActionEvent event) {
		if(tag_field.getText().isEmpty()) {
			System.out.println("Tag field is empty!");
		}
		else {
			Main.gm.addTag(book_table.getSelectionModel().getSelectedItem().getBookId(), tag_field.getText());
		}
		tag_field.clear();
	}

	@FXML
	void viewStatistics(ActionEvent event) throws IOException {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();
		
		if(selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}

		System.out.println("Selected book: "+selectedBook.getBookId()+", "+ selectedBook.getTitle()+", "+ selectedBook.getAuthor());
		Main.statsPopUp(selectedBook, 4);
	}

	
	@FXML
	void removeBook(ActionEvent event) {
		Book selectedBook = book_table.getSelectionModel().getSelectedItem();

		if (selectedBook == null) {
			System.out.println("No book was selected. Please select a book a retry.");
			return;
		}

		System.out.println("Selected book: " + selectedBook.getBookId() + ", " + selectedBook.getTitle() + ", " + selectedBook.getAuthor());
		Main.gm.removeBook(selectedBook.getBookId());
		updateTable();
	}
}

