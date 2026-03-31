import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage {


	public void show(Stage stage) {
		// TODO Auto-generated method stub
		//Title 
		Label title = new Label("Swimcademy");
		title.setFont(Font.font("System",FontWeight.BOLD,24));
		title.setTextFill(Color.web("#0d47a1"));
		
		VBox root = new VBox(18,title);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		 root.setStyle("-fx-background-color: #e3f2fd;");
		 
		 Scene scene = new Scene(root, 700, 500);
	        stage.setTitle("Swimming Academy – Login");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
	        stage.setScene(scene);
	        stage.show();
	}

}
