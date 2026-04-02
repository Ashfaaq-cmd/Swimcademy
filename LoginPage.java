import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage {


	public void show(Stage stage) {
		// TODO Auto-generated method stub
		//Title 
		Label title = new Label("SwimCademy");
		title.setFont(Font.font("System", FontWeight.BOLD, 32));
		title.setTextFill(Color.WHITE);
		title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 0, 2);");

		Label subtitle = new Label("Your swimming journey starts here");
		subtitle.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 13));
		subtitle.setTextFill(Color.WHITE);
		
		//Form fields
		Label lblEmail = new Label("Email: ");
		lblEmail.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter your email");
        txtEmail.setPrefWidth(300);
		
        Label lblPassword = new Label("Password:");
        lblPassword.setFont(Font.font("System", FontWeight.BOLD, 13));
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Enter your password");
        
        Label lblMsg = new Label("");
        lblMsg.setTextFill(Color.RED);
        
        //Buttons
        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(300);
        btnLogin.setStyle(
                "-fx-background-color: #1565c0; -fx-text-fill: white;" +
                "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand;" +
                "-fx-background-radius: 6;"
            );
        Button btnRegister = new Button("Don't have an account? Register");
        btnRegister.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #1565c0;" +
            "-fx-cursor: hand; -fx-underline: true; -fx-border-color: transparent;"
        );
        
        //Layout
        
        VBox form = new VBox(10,lblEmail,txtEmail,lblPassword,txtPass,lblMsg,btnLogin,btnRegister);
        
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: white;" +
                "-fx-border-radius: 10; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, #cccccc, 10, 0, 0, 3);"
            );
        form.setMaxWidth(380);
  
        
		VBox root = new VBox(18,title,subtitle,form);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		root.setStyle(
			    "-fx-background-image: url('/image/water.jpeg');" +
			    "-fx-background-size: cover;" +
			    "-fx-background-position: center center;" +
			    "-fx-background-color: rgba(0,0,0,0.3);" + 
			    "-fx-background-blend-mode: darken;"
			);
		 
		 //Actions
		btnLogin.setOnAction(e->{
			String email = txtEmail.getText().trim();
			String password = txtPass.getText().trim();
			
			if(email.isEmpty() || password.isEmpty()) {
				lblMsg.setText("Please fill in all fields.");
				return;
			}
			if (!isValidEmail(email)) {
			    lblMsg.setTextFill(Color.RED);
			    lblMsg.setText("Invalid email format. Example: name@example.com");
			    return;
			}
			User user = authenticate(email,password);
			if (user != null) {
				if(user.getRole().equals("admin")) {
					AdminDashboard adminDash = new AdminDashboard(user);
					adminDash.show(stage);
				}
				else{
					StudentDashboard studentDash = new StudentDashboard(user);
					studentDash.show(stage);
				}
			}
				else {
					lblMsg.setText("Invalid Email or Password!!");
				}
			
		});
		
		btnRegister.setOnAction(e ->{
			RegisterPage reg = new RegisterPage();
			reg.show(stage);
		});
		
		
		 Scene scene = new Scene(root, 700, 500);
	        stage.setTitle("SwimCademy Login");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
	        stage.setScene(scene);
	        stage.show();
	}
	private boolean isValidEmail(String email) {
	    return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
	}
	private User authenticate(String email, String password) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setFullName(rs.getString("full_name"));
				u.setEmail(rs.getString("email"));
				u.setPassword(rs.getString("password"));
				u.setRole(rs.getString("role"));
				return u;
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
