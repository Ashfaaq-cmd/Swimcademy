import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminBookingsPage {

	  private final User currentUser;
	    private TableView<Booking> table;

	    public AdminBookingsPage(User user) {
	        this.currentUser = user;
	    }

	    public void show(Stage stage) {

	        // Title
	        Label title = new Label("My Bookings");
	        title.setFont(Font.font("System", FontWeight.BOLD, 24));
	        title.setTextFill(Color.web("#0d47a1"));

	        // Table
	        table = new TableView<>();
	        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
	        table.setPrefHeight(280);
	        table.setStyle(
	            "-fx-background-color: white;" +
	            "-fx-border-radius: 10;" +
	            "-fx-background-radius: 10;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
	        );

}
}