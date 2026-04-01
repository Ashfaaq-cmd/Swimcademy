import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MyBookingsPage {
	private final User currentUser;
	private TableView<Session> table;
	
	public MyBookingsPage(User user) {
		this.currentUser = user;
	}

	public void show(Stage stage) {
		// TODO Auto-generated method stub
		Label title = new Label("My Bookings");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));
		title.setTextFill(Color.WHITE);
		
		
}
}