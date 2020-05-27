package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.Main;
import main.java.models.*;

import java.io.IOException;
import java.util.List;


public class StatsPopUpController extends Controller {

    @FXML
    private Label book_lab, wish_lab, tags_lab;

    @FXML
    private TableView<Tag> tag_table;

    @FXML
    private TableColumn tagCol;

    @FXML
    private Button back_but;

    private Book selectedBook;
    private int returnPage; //specifies which interface I shall return to

    public void setSelectedBook(Book b) {
        this.selectedBook = b;
    }

    public void setReturnPage(int p) { this.returnPage = p; }

    public void backScene(ActionEvent event) throws IOException {
        Main.changeScene(this.returnPage);
    }

    public void init() {
        book_lab.setText(this.selectedBook.getTitle());

        tagCol.setCellValueFactory(new PropertyValueFactory<Tag, String>("tag_name"));

        List<Tag> tmpTags = Main.gm.getTags(this.selectedBook.getBookId());

        ObservableList<Tag> tags = FXCollections.observableArrayList();
        tags.addAll(tmpTags);
        updateTable(tags);

        wish_lab.setText("Cointained in " + Main.gm.getTotWish(this.selectedBook.getBookId()) + " wishlist");
        tags_lab.setText("# of Tags: " + Main.gm.getTotTag(this.selectedBook.getBookId()));
    }

    public void updateTable(ObservableList<Tag> list) {
        tag_table.setItems(list);
    }
    
    @FXML
    void backTo(ActionEvent event) throws IOException
    {
        Main.changeScene(this.returnPage);
    }
}
