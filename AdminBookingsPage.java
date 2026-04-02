import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminBookingsPage {

	  private final User currentUser;

	    public AdminBookingsPage(User user) {
	        this.currentUser = user;
	    }

	    public void show(Stage stage) {

	        // Title
	        Label title = new Label("All Bookings & Payments");
	        title.setFont(Font.font("System", FontWeight.BOLD, 24));
	        title.setTextFill(Color.web("#0d47a1"));

	        //Bookings Table
	        Label lblBook = new Label("Bookings");
	        lblBook.setFont(Font.font("System", FontWeight.BOLD, 15));
	        
	        TableView<Booking> bookTable = new TableView<>();
	bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
	        bookTable.setPrefHeight(200);
	        
	        TableColumn<Booking, Integer> bId = new TableColumn<>("ID");
	        bId.setCellValueFactory(new PropertyValueFactory<>("id"));
	        bId.setMaxWidth(50);
	        
	        TableColumn<Booking, String> bUser = new TableColumn<>("Student");
	        bUser.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(fetchUserName(d.getValue().getUserId())));
	        TableColumn<Booking, String> bSession = new TableColumn<>("Session");
	        bSession.setCellValueFactory(new PropertyValueFactory<>("sessionTitle"));
	        
	        TableColumn<Booking, String> bDate = new TableColumn<>("Session Date");
	        bDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
	        
	        TableColumn<Booking, String> bBooked = new TableColumn<>("Booked On");
	        bBooked.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
	        
	        TableColumn<Booking, String> bStatus = new TableColumn<>("Status");
	        bStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
	        
	      bookTable.getColumns().addAll(bId, bUser, bSession, bDate, bBooked, bStatus);

	      //Payments Table
	      Label lblPay = new Label("Payments");
	        lblPay.setFont(Font.font("System", FontWeight.BOLD, 15));

	        TableView<Payment> payTable = new TableView<>();	     payTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
	        payTable.setPrefHeight(180);

	        TableColumn<Payment, Integer> pId = new TableColumn<>("ID");
	        pId.setCellValueFactory(new PropertyValueFactory<>("id"));
	        pId.setMaxWidth(50);
	        TableColumn<Payment, Integer> pBooking = new TableColumn<>("Booking ID");
	        pBooking.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
	        TableColumn<Payment, String> pStudent = new TableColumn<>("Student");
	        pStudent.setCellValueFactory(new PropertyValueFactory<>("studentName"));
	        TableColumn<Payment, String> pSession = new TableColumn<>("Session");
	        pSession.setCellValueFactory(new PropertyValueFactory<>("sessionTitle"));
	        TableColumn<Payment, Double> pAmount = new TableColumn<>("Amount (Rs)");
	        pAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
	        TableColumn<Payment, String> pDate = new TableColumn<>("Date");
	        pDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
	        TableColumn<Payment, String> pStatus = new TableColumn<>("Status");
	        pStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

	        payTable.getColumns().addAll(pId, pBooking, pStudent, pSession, pAmount, pDate, pStatus);
	        
	        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/back.png")));
	        backIcon.setFitWidth(18);
	        backIcon.setFitHeight(18);
	        Button btnBack = new Button("Back to Dashboard");
	        btnBack.setGraphic(backIcon);
	        btnBack.setContentDisplay(ContentDisplay.LEFT);
	        btnBack.setPrefWidth(200);
	        btnBack.setStyle(
	            "-fx-background-color: #546e7a;" +
	            "-fx-text-fill: white;" +
	            "-fx-font-weight: bold;" +
	            "-fx-background-radius: 20;" +
	            "-fx-padding: 8 16;" +
	            "-fx-cursor: hand;"
	        );
	        VBox root = new VBox(12,
	                title,
	                lblBook, bookTable,
	                new Separator(),
	                lblPay, payTable,
	                btnBack
	            );
	            root.setPadding(new Insets(25));
	            root.setStyle("-fx-background-color: #f5f9ff;");
	            
	            //Load Data
	            

}
}