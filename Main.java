import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		LoginPage loginpage = new LoginPage();
		loginpage.show(stage);
	}

}
