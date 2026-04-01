import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboard {
	private final User currentUser;

	public AdminDashboard(User user) {
		// TODO Auto-generated constructor stub
		this.currentUser = user;
	}

	public void show(Stage stage) {
		// TODO Auto-generated method stub
		  Label title = new Label("Admin Dashboard");
	        title.setFont(Font.font("System", FontWeight.BOLD, 26));
	        title.setTextFill(Color.web("#0d47a1"));

	        Label subtitle = new Label("SwimCademy Management Panel");
	        subtitle.setFont(Font.font("System", FontPosture.ITALIC, 13));
	        subtitle.setTextFill(Color.GRAY);

	        //Stats 
	        int[] stats = fetchStats();

	        VBox card1 = makeCard("Total Users", String.valueOf(stats[0]), "#1565c0");
	        VBox card2 = makeCard("Total Sessions", String.valueOf(stats[1]), "#6a1b9a");
	        VBox card3 = makeCard("Total Bookings", String.valueOf(stats[2]), "#2e7d32");
	        VBox card4 = makeCard("Total Revenue", "Rs " + (stats[3]), "#e65100");

	        HBox cards = new HBox(15, card1, card2, card3, card4);
	        cards.setAlignment(Pos.CENTER);


	        //button
	        ImageView sessionsIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/swimming-pool.png")));
	        sessionsIcon.setFitWidth(18);
	        sessionsIcon.setFitHeight(18);
	        Button btnManageSessions = makeNavBtn("Manage Sessions", "#6a1b9a", "#9b59b6", sessionsIcon);

	        
	        ImageView allBookingsIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/file.png")));
	        allBookingsIcon.setFitWidth(18);
	        allBookingsIcon.setFitHeight(18);
	        Button btnBookings = makeNavBtn("View All Bookings", "#2e7d32", "#66bb6a", allBookingsIcon);

	        
	        ImageView logoutIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/logout.png")));
	        logoutIcon.setFitWidth(18);
	        logoutIcon.setFitHeight(18);
	        Button btnLogout = makeNavBtn("Logout", "#e53935", "#b71c1c", logoutIcon);;

	        VBox navBox = new VBox(12,btnManageSessions, btnBookings, btnLogout);
	        navBox.setAlignment(Pos.CENTER);

	        Label quick = new Label("Admin Actions:");
	        quick.setFont(Font.font("System", FontWeight.BOLD, 15));
	        quick.setTextFill(Color.web("#333"));

	        //Card Container
	        VBox card = new VBox(18,
	                title,
	                subtitle,
	                new Separator(),
	                cards,
	                new Separator(),
	                quick,
	                navBox
	        );

	        card.setPadding(new Insets(25));
	        card.setMaxWidth(720);

	        card.setStyle(
	            "-fx-background-color: white;" +
	            "-fx-background-radius: 15;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);"
	        );

	      
	        VBox root = new VBox(card);
	        root.setAlignment(Pos.CENTER);
	        root.setPadding(new Insets(30));

	        root.setStyle(
	            "-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);"
	        );

	        //Actions
	        btnManageSessions.setOnAction(e -> new AdminSessionsPage(currentUser).show(stage));
	        btnBookings.setOnAction(e -> new AdminBookingsPage(currentUser).show(stage));
	        btnLogout.setOnAction(e -> new LoginPage().show(stage));

	        Scene scene = new Scene(root, 900, 600);
	        stage.setTitle("Admin Dashboard");
	        stage.setScene(scene);
	        stage.show();
	    }

	    private Button makeNavBtn(String text, String colorStart, String colorEnd, ImageView icon) {
	        Button btn = new Button(text);
	        btn.setPrefWidth(280);
	        btn.setStyle(
	            "-fx-background-color: linear-gradient(to right, " + colorStart + ", " + colorEnd + ");" +
	            "-fx-text-fill: white;" +
	            "-fx-font-size: 13;" +
	            "-fx-font-weight: bold;" +
	            "-fx-background-radius: 8;" +
	            "-fx-padding: 10 20;" +
	            "-fx-cursor: hand;"
	        );
	        if (icon != null) {
	            btn.setGraphic(icon);
	            btn.setContentDisplay(ContentDisplay.LEFT); 
	            btn.setGraphicTextGap(10); 
	        }
	        return btn;
	    }
	   
	    private VBox makeCard(String label, String value, String color) {

	        Label val = new Label(value);
	        val.setFont(Font.font("System", FontWeight.BOLD, 30));
	        val.setTextFill(Color.WHITE);

	        Label lbl = new Label(label);
	        lbl.setFont(Font.font("System", 13));
	        lbl.setTextFill(Color.WHITE);
	        lbl.setWrapText(true);

	        VBox card = new VBox(8, val, lbl);
	        card.setPadding(new Insets(15));
	        card.setPrefWidth(150);
	        card.setAlignment(Pos.CENTER_LEFT);

	        card.setStyle(
	            "-fx-background-color: " + color + ";" +
	            "-fx-background-radius: 15;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
	        );

	        return card;
	    }
	    
	    private int[] fetchStats() {

	        int users = 0, sessions = 0, bookings = 0, revenue = 0;

	        try (Connection conn = DatabaseConnection.getConnection()) {
	        	ResultSet rs;
	        	rs=conn.createStatement().executeQuery("SELECT COUNT(*) FROM users WHERE role='student'");
	        	if(rs.next()) users = rs.getInt(1);
	        	
	        	rs=conn.createStatement().executeQuery("SELECT COUNT(*) FROM sessions");
	        	if(rs.next()) sessions = rs.getInt(1);
	        	
	        	rs=conn.createStatement().executeQuery("SELECT COUNT(*) FROM bookings WHERE status='confirmed'");
	        	if(rs.next()) bookings = rs.getInt(1);
	        	
	        	rs=conn.createStatement().executeQuery("SELECT COALESCE(SUM(amount),0) FROM payments WHERE payment_status='paid'");
	        	if(rs.next()) revenue = rs.getInt(1);

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	        return new int[]{users, sessions, bookings, revenue};
	    }
	}


