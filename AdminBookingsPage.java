import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
	        bUser.setCellValueFactory(new PropertyValueFactory<>("studentName"));
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
	        		 "-fx-background-color: linear-gradient(to right,#546e7a,#748993);"+
	     		            "-fx-text-fill: white;" +
	     		            "-fx-font-size: 13;" +
	     		            "-fx-font-weight: bold;" +
	     		            "-fx-background-radius: 8;" +
	     		            "-fx-padding: 10 20;" +
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
	            bookTable.setItems(FXCollections.observableArrayList(loadBookings()));
	            payTable.setItems(FXCollections.observableArrayList(loadPayments()));
	            btnBack.setOnAction(e-> new AdminDashboard(currentUser).show(stage));
	            
	            Scene scene = new Scene(root, 900, 620);
	            stage.setTitle("All Bookings & Payments");
	            stage.setScene(scene);
	            stage.show();

}


		private List<Payment> loadPayments() {
			// TODO Auto-generated method stub
			 List<Payment> list = new ArrayList<>();
		        String sql =
		            "SELECT p.id, p.booking_id, p.amount, p.payment_date, p.payment_status, " +
		            "       u.full_name, s.title " +
		            "FROM payments p " +
		            "JOIN bookings b ON p.booking_id = b.id " +
		            "JOIN users    u ON b.user_id    = u.id " +
		            "JOIN sessions s ON b.session_id = s.id " +
		            "ORDER BY p.id DESC";
		        try (Connection conn = DatabaseConnection.getConnection();
		             PreparedStatement ps = conn.prepareStatement(sql)) {
		            ResultSet rs = ps.executeQuery();
		            while (rs.next()) {
		                Payment py = new Payment();
		                py.setId(rs.getInt("id"));
		                py.setBookingId(rs.getInt("booking_id"));
		                py.setAmount(rs.getDouble("amount"));
		                py.setPaymentDate(rs.getString("payment_date"));
		                py.setPaymentStatus(rs.getString("payment_status"));
		                py.setStudentName(rs.getString("full_name"));
		                py.setSessionTitle(rs.getString("title"));
		                list.add(py);
		            }
		        } catch (SQLException ex) {
		        	ex.printStackTrace();
		        	}
		        return list;
			
		}

		private List<Booking> loadBookings() {
	        List<Booking> list = new ArrayList<>();
	        String sql =
	        	    "SELECT b.id, b.user_id, b.session_id, b.booking_date, b.status, " +
	        	    "       s.title, s.session_date, s.session_time, s.price, u.full_name " +
	        	    "FROM bookings b " +
	        	    "JOIN sessions s ON b.session_id = s.id " +
	        	    "JOIN users u ON b.user_id = u.id " +
	        	    "ORDER BY b.id DESC";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Booking bk = new Booking();
	                bk.setId(rs.getInt("id"));
	                bk.setUserId(rs.getInt("user_id"));
	                bk.setSessionId(rs.getInt("session_id"));
	                bk.setBookingDate(rs.getString("booking_date"));
	                bk.setStatus(rs.getString("status"));
	                bk.setSessionTitle(rs.getString("title"));
	                bk.setSessionDate(rs.getString("session_date"));
	                bk.setSessionTime(rs.getString("session_time"));
	                bk.setPrice(rs.getDouble("price"));
	                bk.setStudentName(rs.getString("full_name"));
	                list.add(bk);
	            }
	        } catch (SQLException ex) {
	        	ex.printStackTrace(); 
	        	}
	        return list;
	    }
}