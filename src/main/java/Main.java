package main.java;

import javafx.application.*;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import javafx.geometry.*;
import main.java.controllers.RatingPopUpController;
import main.java.controllers.StatsPopUpController;
import main.java.models.Book;

import java.io.IOException;
import java.util.*;

public class Main extends Application {
    private static Stage stage;
    private static Parent root;
    public static LibraryManager lm;
    public static GraphManager gm;
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        lm = new LibraryManager();
        lm.setup();
        
        gm = new GraphManager("bolt://localhost:7687", "neo4j", "test");

        stage = primaryStage;
        stage.setTitle("BookRater");
        changeScene(0); //0: login
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void changeScene(int type) throws IOException {
        switch(type) {
            case 0:
                root = FXMLLoader.load(Main.class.getResource("/Login.fxml"));
                stage.setScene(new Scene(root, 640, 400));
                break;
            case 1:
                root = FXMLLoader.load(Main.class.getResource("/UserHome.fxml"));
                stage.setScene(new Scene(root, 230, 360));
                break;
            case 2:
            	root = FXMLLoader.load(Main.class.getResource("/Suggestions.fxml"));
                stage.setScene(new Scene(root, 1000, 640));
            	break;
            case 3:
            	root = FXMLLoader.load(Main.class.getResource("/WishList.fxml"));
                stage.setScene(new Scene(root, 960, 650));
            	break;
            case 4:
            	root = FXMLLoader.load(Main.class.getResource("/Catalogue.fxml"));
                stage.setScene(new Scene(root, 1000, 640));
            	break;
            case 5:
            	root = FXMLLoader.load(Main.class.getResource("/AdminHome.fxml"));
                stage.setScene(new Scene(root, 230, 360));
            	break;
            case 6:
                root = FXMLLoader.load(Main.class.getResource("/AddBook.fxml"));
                stage.setScene(new Scene(root, 640, 400));
                break;
            default:
                break;
        }

        stage.show();
        centerStage(stage);
    }
    
    public static void ratingPopUp(Book b, int page) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/RatingPopUp.fxml"));
        root = loader.load();
        
        RatingPopUpController cvc = loader.getController();
        System.out.println(cvc);
        cvc.setSelectedBook(b); // Passing the book-object to the other Controller
        cvc.setReturnPage(page); //telling which interface to return to - 3 wish, 4 catalogue
        
        stage.setScene(new Scene(root, 360, 230));
        stage.show();
        centerStage(stage);
    }

    public static void statsPopUp(Book b, int page) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/StatsPopUp.fxml"));
        root = loader.load();

        StatsPopUpController cvc = loader.getController();
        System.out.println(cvc);
        cvc.setSelectedBook(b); // Passing the book-object to the other Controller
        cvc.setReturnPage(page); //telling which interface to return to - 3 wish, 4 catalogue
        cvc.init();

        stage.setScene(new Scene(root, 360, 440));
        stage.show();
        centerStage(stage);
    }

    public static void exit() {
        lm.exit();
        gm.close();
        Platform.exit(); }

    public static void centerStage(Stage s) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
        s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
    }
}