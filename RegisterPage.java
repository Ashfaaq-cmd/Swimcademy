import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegisterPage {

	public void show(Stage stage) {
		// TODO Auto-generated method stub
		Label title = new Label("Create Account");
		title.setFont(Font.font("System", FontWeight.BOLD, 32));
		title.setTextFill(Color.WHITE);
		title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 0, 2);");
		
		Label lblName = new Label("Full Name: ");
		lblName.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtName = new TextField();
        txtName.setPromptText("Enter your Full Name");
     
        Label lblEmail = new Label("Email: ");
		lblEmail.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter your email");
       
        Label lblPassword = new Label("Password:");
        lblPassword.setFont(Font.font("System", FontWeight.BOLD, 13));
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Enter your password");
        
        Label lblConfirm = new Label("Confirm Password:");
        lblConfirm.setFont(Font.font("System", FontWeight.BOLD, 13));
        PasswordField txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Re-Enter your password");
		
        Label lblMsg = new Label();
        lblMsg.setWrapText(true);
        
        Button btnRegister = new Button("Create Account");
        btnRegister.setPrefWidth(300);
        btnRegister.setStyle(
                "-fx-background-color: #1565c0; -fx-text-fill: white;" +
                "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand;" +
                "-fx-background-radius: 6;");
        
        Button btnBack = new Button("Back to Login");
        btnBack.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #1565c0;" +
            "-fx-cursor: hand; -fx-underline: true; -fx-border-color: transparent;"
        );
        
        VBox form = new VBox(10,lblName,txtName,lblEmail,txtEmail,lblPassword,txtPass,lblConfirm,txtConfirm,lblMsg,btnRegister,btnBack);
        
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: white;" +
                "-fx-border-radius: 10; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, #cccccc, 10, 0, 0, 3);"
            );
        form.setMaxWidth(380);
  
        
		VBox root = new VBox(18,title,form);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		root.setStyle(
			    "-fx-background-image: url('/image/water.jpeg');" +
			    "-fx-background-size: cover;" +
			    "-fx-background-position: center center;" +
			    "-fx-background-color: rgba(0,0,0,0.3);" + 
			    "-fx-background-blend-mode: darken;"
			);
		
		btnRegister.setOnAction(e->{
			String name = txtName.getText().trim();
			String email = txtEmail.getText().trim();
			String password = txtPass.getText().trim();
			String confirm = txtConfirm.getText().trim();
			
			
			if(name.isEmpty() || email.isEmpty() || password.isEmpty()|| confirm.isEmpty()) {
				lblMsg.setTextFill(Color.RED);
				lblMsg.setText("Please fill in all fields.");
				return;
			}
			if(!password.equals(confirm)) {
				lblMsg.setTextFill(Color.RED);
				lblMsg.setText("Password do not match!");
				return;
			}
			if(registerUser(name,email,password)) {
				lblMsg.setTextFill(Color.GREEN);
				lblMsg.setText("Account Created! Redirecting to login...");
				new Thread(()->{
					try {
						Thread.sleep(1500);
					}
					catch(InterruptedException ex) {
						ex.printStackTrace();
					}
					javafx.application.Platform.runLater(()->{
						LoginPage login = new LoginPage();
						login.show(stage);
					});
					
				}).start();
			}
			else {
				lblMsg.setTextFill(Color.RED);
				lblMsg.setText("User Already Exist");
			}
		});
		
		btnBack.setOnAction(e->{
			LoginPage login = new LoginPage();
			login.show(stage);
		});
		
		Scene scene = new Scene(root, 700, 500);
        stage.setTitle("SwimCademy Register");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
        stage.setScene(scene);
        stage.show();
	}

	private boolean registerUser(String name, String email, String password) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO users(full_name,email,password,role) VALUES (?,?,?,'student')";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.execute();
			return true;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
	}

}
