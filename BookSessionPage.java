import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BookSessionPage {
	private final User currentUser;
	private TableView<Session> table;
	
	public BookSessionPage(User user) {
		this.currentUser = user;
	}

	public void show(Stage stage) {
		// TODO Auto-generated method stub
		Label title = new Label("Book a Session");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));
		title.setTextFill(Color.WHITE);
		title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 0, 2);");
		
		//Filter Controls
		Label lblFilter = new Label("Filter by Level:");
		lblFilter.setFont(Font.font("System",FontWeight.BOLD,13));
		ComboBox<String> lvlFilter = new ComboBox<>();
		lvlFilter.getItems().addAll("All","Beginner","Intermediate" , "Advanced");
		lvlFilter.setValue("All");
		
		Button btnFilter = new Button("Apply Filter");
		btnFilter.setStyle(  "-fx-background-color: #1565c0; -fx-text-fill: white;" +
	            "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;"
	            );
		HBox filterRow = new HBox(10,lblFilter,lvlFilter,btnFilter);
		filterRow.setAlignment(Pos.CENTER_LEFT);
		
		//Table
		table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		table.setPrefHeight(300);
		
		TableColumn<Session,String> colTitle = new TableColumn<>("Session");
		colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		
		TableColumn<Session,String> colCoach = new TableColumn<>("Coach");
		colCoach.setCellValueFactory(new PropertyValueFactory<>("coach"));
		
		TableColumn<Session,String> colDate = new TableColumn<>("Date");
		colDate.setCellValueFactory(new PropertyValueFactory<>("SessionDate"));
		
		TableColumn<Session,String> colTime= new TableColumn<>("Time");
		colTime.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
		
		TableColumn<Session,String> colLvl= new TableColumn<>("Level");
		colLvl.setCellValueFactory(new PropertyValueFactory<>("level"));
		
		TableColumn<Session,String> colSlots = new TableColumn<>("Slots Left");
		colSlots.setCellValueFactory(new PropertyValueFactory<>("slotsAvailable"));
		
		TableColumn<Session,String> colPrice = new TableColumn<>("Price (Rs)");
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		table.getColumns().addAll(colTitle,colCoach,colDate,colTime,colLvl,colSlots,colPrice);
		
		Label lblMsg = new Label("");
		lblMsg.setWrapText(true);
		
		Button btnBook = new Button("Book Selected Session");
		btnBook.setPrefWidth(260);
        btnBook.setStyle(
            "-fx-background-color: #2e7d32; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-font-size: 13; -fx-cursor: hand;" +
            "-fx-background-radius: 6; -fx-padding: 8 16;"
        );

        Button btnBack = new Button("Back to Dashboard");
        btnBack.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #1565c0;" +
            "-fx-cursor: hand; -fx-underline: true; -fx-border-color: transparent;"
        );
        
        HBox btnRow = new HBox(15,btnBook,btnBack);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox root = new VBox(14,title,filterRow,table);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f5f9ff;");
		
		//Load initial data
        loadSessions("All");
        
		btnFilter.setOnAction(e->{
			lblMsg.setText("");
			loadSessions(lvlFilter.getValue());
		});
		
		btnBook.setOnAction(e->{
			Session selected = table.getSelectionModel().getSelectedItem();
			if(selected == null) {
				lblMsg.setTextFill(Color.ORANGE);
				lblMsg.setText("Please select a session first.");
				return;
			}
			if(selected.getSlotsAvailable()==0) {
				lblMsg.setTextFill(Color.RED);
				lblMsg.setText("Sorry, this session is full.");
				return;
			}
			if(alreadyBooked(currentUser.getId(), selected.getId())) {
				lblMsg.setTextFill(Color.RED);
				lblMsg.setText("You have already booked this session.");
				return;
			}
			boolean success = bookSession(currentUser.getId(),selected);
				if(success) {
					lblMsg.setTextFill(Color.GREEN);
					lblMsg.setText("Session booked successfully! Payment recorded.");
					loadSessions(lvlFilter.getValue());
				}
				else {
					lblMsg.setTextFill(Color.RED);
					lblMsg.setText("Booking failed. Please try again.");
					
			}
		});
		
		btnBack.setOnAction(e-> new StudentDashboard(currentUser).show(stage));
		  Scene scene = new Scene(root, 850, 560);
	        stage.setTitle("Book a Session");
	        stage.setScene(scene);
	        stage.show();
	}

	private boolean bookSession(int userId, Session session) {
		// TODO Auto-generated method stub
		String sqlBook = "INSERT INTO bookings(user_id, session_id, status) VALUES (?,?,'confirmed')";
		String sqlUpdate = "UPDATE sessions SET slots_available = slots_available -1 WHERE id = ?";
		String sqlPayment = "INSERT INTO payments (booking_id, amount, payment_status) VALUES (?,?,'paid')";
		
		try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            //Insert Booking
            PreparedStatement ps1 = conn.prepareStatement(sqlBook, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, userId);
            ps1.setInt(2, session.getId());
            ps1.executeUpdate();
            ResultSet keys = ps1.getGeneratedKeys();
            int bookingId = 0;
            if(keys.next()) {
            	bookingId = keys.getInt(1);
            }
            //Update Slots
            PreparedStatement ps2 = conn.prepareStatement(sqlUpdate);
            ps2.setInt(1, session.getId());
            ps2.executeUpdate();
            // Insert payment
            PreparedStatement ps3 = conn.prepareStatement(sqlPayment);
            ps3.setInt(1, bookingId);
            ps3.setDouble(2, session.getPrice());
            ps3.executeUpdate();

            conn.commit();
            return true;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
			
		}
		
		
		
	}

	

	private boolean alreadyBooked(int userId, int sessionId) {
		// TODO Auto-generated method stub
		 String sql = "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND session_id = ? AND status = 'confirmed'";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, userId);
	            ps.setInt(2, sessionId);
	            ResultSet rs = ps.executeQuery();
	            return rs.next() && rs.getInt(1) > 0;
	        } 
	        catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		return false;
	}

	private void loadSessions(String level) {
		// TODO Auto-generated method stub
		List<Session> sessions = new ArrayList<>();
		String sql = level.equals("All") 
				?"SELECT * FROM sessions WHERE slots_available > 0 ORDER BY session_date,session_time"
				:"SELECT * FROM sessions WHERE slots_available > 0 AND level = ? ORDER BY session_date,session_time";
		 try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            if (!level.equals("All")) ps.setString(1, level);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Session s = new Session(
	                    rs.getInt("id"),
	                    rs.getString("title"),
	                    rs.getString("coach"),
	                    rs.getString("session_date"),
	                    rs.getString("session_time"),
	                    rs.getInt("capacity"),
	                    rs.getInt("slots_available"),
	                    rs.getString("level"),
	                    rs.getDouble("price")
	                );
	                sessions.add(s);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		 table.setItems(FXCollections.observableArrayList(sessions));
	}

}
