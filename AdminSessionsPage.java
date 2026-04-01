import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminSessionsPage {

	  private final User currentUser;
	    private TableView<Booking> table;

	    public AdminSessionsPage(User user) {
	        this.currentUser = user;
	    }

	    public void show(Stage stage) {

	        // Title
	        Label title = new Label("Manage Sessions");
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
	        addCol("ID", "id");
	        addCol("Title", "title");
	        addCol("Coach", "coach");
	        addCol("Date", "sessionDate");
	        addCol("Time", "sessionTime");
	        addCol("Level", "level");
	        addCol("Capacity", "capacity");
	        addCol("Slots Left", "slotsAvailable");
	        addCol("Price", "price");
	        
	        //Add Session form
	        TextField txtTitle = new TextField();
	        txtTitle.setPromptText("Session Title"); 
	        TextField txtCoach = new TextField();
	        txtCoach.setPromptText("Coach name");
	        TextField txtDate = new TextField();
	        txtDate.setPromptText("Date (YYYY-MM-DD)");
	        TextField txtTime = new TextField();
	        txtTime.setPromptText("Time(HH:MM:SS)");
	        
	        ComboBox<String> cbLvl = new ComboBox<>();
	        cbLvl.getItems().addAll("Beginner","Intermediate", "Advanced");
	        cbLvl.setValue("Beginner");
	        
	        TextField txtCapacity = new TextField(); 
	        txtCapacity.setPromptText("Capacity");
	        TextField txtPrice    = new TextField();
	        txtPrice.setPromptText("Price");


}

		private void addCol(String string, String string2) {
			// TODO Auto-generated method stub
			
		}
	    
}
