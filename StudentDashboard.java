import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StudentDashboard {
private final User currentUser;

public StudentDashboard(User user) {
	this.currentUser = user;
}

public void show(Stage stage) {
	// TODO Auto-generated method stub
	//Header
	Label welcome = new Label("Welcome, " + currentUser.getFullName());
	welcome.setFont(Font.font("System", FontWeight.BOLD, 25));
	welcome.setTextFill(Color.WHITE);
	welcome.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 0, 2);");

	Label subtitle = new Label("Student Portal");
	subtitle.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 13));
	subtitle.setTextFill(Color.WHITE);
	
	//Status Cards
	int [] stats = fetchStats();
	VBox card1 = makeCard("My Total Bookings", String.valueOf(stats[0]), "#e65100");
	VBox card2 = makeCard("Active Bookings", String.valueOf(stats[1]), "#2e7d32");
	VBox card3 = makeCard("Cancelled  Bookings", String.valueOf(stats[2]), "#b71c1c");
	VBox card4 = makeCard("Available Sessions", String.valueOf(stats[3]), "#e65100");
	
	HBox cards = new HBox(15,card1,card2,card3,card4);
	cards.setAlignment(Pos.CENTER_LEFT);
	
	//Nav Btn
	Button btnBook = makeNavBtn("Book a Session","#e65100 ");
	Button btnMyBook = makeNavBtn("My Bookings","#2e7d32 ");
	Button btnLogout = makeNavBtn("Logout","#b71c1c ");
	
	VBox navBox = new VBox(12,btnBook,btnMyBook,btnLogout);
	navBox.setAlignment(Pos.CENTER_LEFT);
	
	 VBox root = new VBox(18,
	            welcome, subtitle,
	            new Separator(),
	            cards,
	            new Separator(),
	            new Label("Quick Actions:"),
	            navBox
	        );
	        root.setPadding(new Insets(30));
	        root.setStyle("-fx-background-color:#5d8ddf");
	        
	        
	
}

private Button makeNavBtn(String string, String string2) {
	// TODO Auto-generated method stub
	return null;
}

private VBox makeCard(String string, String valueOf, String string2) {
	// TODO Auto-generated method stub
	return null;
}

private int[] fetchStats() {
	// TODO Auto-generated method stub
	return null;
}


}
