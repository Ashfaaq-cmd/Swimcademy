import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminSessionsPage {

	  private final User currentUser;
	    private TableView<Session> table;

	    public AdminSessionsPage(User user) {
	        this.currentUser = user;
	    }

	    public void show(Stage stage) {

	        // Title
	        Label title = new Label("Manage Sessions");
	        title.setFont(Font.font("System", FontWeight.BOLD, 24));
	        title.setTextFill(Color.web("#0d47a1"));

	        // Table
	        table = new TableView<>();
	        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
	        table.setPrefHeight(280);
	        table.setStyle(
	            "-fx-background-color: white;" +
	            "-fx-border-radius: 10;" +
	            "-fx-background-radius: 10;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
	        );
	        addCol("ID", "id");
	        addCol("Title", "title");
	        addCol("Coach", "coach");
	        addCol("Date", "sessionDate");
	        addCol("Time", "sessionTime");
	        addCol("Level", "level");
	        addCol("Capacity", "capacity");
	        addCol("Slots Left", "slotsAvailable");
	        addCol("Price", "price");
	        
	        //Add Session form
	        TextField txtTitle = new TextField();
	        txtTitle.setPromptText("Session Title"); 
	        TextField txtCoach = new TextField();
	        txtCoach.setPromptText("Coach name");
	        TextField txtDate = new TextField();
	        txtDate.setPromptText("Date (YYYY-MM-DD)");
	        TextField txtTime = new TextField();
	        txtTime.setPromptText("Time(HH:MM:SS)");
	        
	        ComboBox<String> cbLvl = new ComboBox<>();
	        cbLvl.getItems().addAll("Beginner","Intermediate", "Advanced");
	        cbLvl.setValue("Beginner");
	        
	        TextField txtCapacity = new TextField(); 
	        txtCapacity.setPromptText("Capacity");
	        TextField txtPrice    = new TextField();
	        txtPrice.setPromptText("Price");
	        
	        GridPane form = new GridPane();
	        form.setHgap(10);
	        form.setVgap(8);
	        form.addRow(0, new Label("Title:"), txtTitle, new Label("Coach:"),txtCoach);
	        form.addRow(1, new Label("Date:"), txtDate, new Label("Time:"),txtTime);
	        form.addRow(2, new Label("Level:"), cbLvl, new Label("Capacity:"),txtCapacity);
	        form.addRow(3, new Label("Price:"), txtPrice);
	        
	        Label formTitle = new Label("Add New Session:");
	        formTitle.setFont(Font.font("System", FontWeight.BOLD,14));
	        
	        Label lblMsg = new Label("");
	        
	        //Buttons
	        ImageView addIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/add.png")));
	        addIcon.setFitWidth(18);
	        addIcon.setFitHeight(18);
	        Button btnAdd = makeNavBtn("Add Session", "#1565c0", "#5c92d1", addIcon);

	        
	        ImageView dltIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/delete.png")));
	        dltIcon.setFitWidth(18);
	        dltIcon.setFitHeight(18);
	        Button btnDlt = makeNavBtn("Delete Selected", "#e53935", "#ef5350", dltIcon);

	        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/back.png")));
	        backIcon.setFitWidth(18);
	        backIcon.setFitHeight(18);
	        Button btnBack = makeNavBtn("Back","546e7a","#7895a2",backIcon);
	       
	        

	        VBox btnRow = new VBox(10,btnAdd, btnDlt, btnBack);
	        btnRow.setAlignment(Pos.CENTER);
	        
	        VBox card = new VBox(12, title, table, new Separator(),formTitle,form,lblMsg,btnRow);
	        card.setPadding(new Insets(25));
	        card.setStyle(
	            "-fx-background-color: white;" +
	            "-fx-background-radius: 15;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);"
	        );

	        VBox root = new VBox(card);
	        root.setAlignment(Pos.CENTER);
	        root.setPadding(new Insets(30));
	        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

	        // Load Sessions
	        loadSessions();
	        btnAdd.setOnAction(e->{
	        	String t = txtTitle.getText().trim();
	        	 String c  = txtCoach.getText().trim();
	             String d  = txtDate.getText().trim();
	             String tm = txtTime.getText().trim();
	             String lv = cbLvl.getValue();
	             String cp = txtCapacity.getText().trim();
	             String pr = txtPrice.getText().trim();
	             
	             if(t.isEmpty() || c.isEmpty() ||d.isEmpty() || tm.isEmpty() ||cp.isEmpty() || pr.isEmpty() ) {
	            	 lblMsg.setTextFill(Color.RED);
	            	 lblMsg.setText("Please fill all fields");
	            	 return;
	             }
	             try {
	            	 int cap = Integer.parseInt(cp);
	            	 double price = Double.parseDouble(pr);
	            	  String sql = "INSERT INTO sessions (title,coach,session_date,session_time,capacity,slots_available,level,price) VALUES (?,?,?,?,?,?,?,?)";
	                  try (Connection conn = DatabaseConnection.getConnection();
	                       PreparedStatement ps = conn.prepareStatement(sql)) {
	                      ps.setString(1, t); ps.setString(2, c);
	                      ps.setString(3, d); ps.setString(4, tm);
	                      ps.setInt(5, cap);  ps.setInt(6, cap);
	                      ps.setString(7, lv); ps.setDouble(8, price);
	                      ps.executeUpdate();
	                      lblMsg.setTextFill(Color.GREEN);
	                      lblMsg.setText("Session added successfully!");
	                      txtTitle.clear();
	                      txtCoach.clear();
	                      txtDate.clear();
	                      txtTime.clear();
	                      txtCapacity.clear(); 
	                      txtPrice.clear();
	                      loadSessions();
	             }
	             }
	             catch(NumberFormatException ex) {
	            	 lblMsg.setTextFill(Color.RED);
	                 lblMsg.setText("Capacity and Price must be numbers");
	            	 
	             }
	             catch(SQLException ex) {
	            	  ex.printStackTrace();
	                  lblMsg.setTextFill(Color.RED);
	                  lblMsg.setText("Error adding session");
	            	 
	             }
	        });
	        btnDlt.setOnAction(e->{
	        	
	        	Session sel = table.getSelectionModel().getSelectedItem();
	            if (sel == null) {
	                  lblMsg.setTextFill(Color.ORANGE);
	                  lblMsg.setText("Select a session to delete.");
	                  return;
	              }
	              try (Connection conn = DatabaseConnection.getConnection();
	                   PreparedStatement ps = conn.prepareStatement("DELETE FROM sessions WHERE id = ?")) {
	                  ps.setInt(1, sel.getId());
	                  ps.executeUpdate();
	                  lblMsg.setTextFill(Color.GREEN);
	                  lblMsg.setText("Session deleted.");
	                  loadSessions();
	              } catch (SQLException ex) {
	                  ex.printStackTrace();
	                  lblMsg.setTextFill(Color.RED);
	                  lblMsg.setText("Cannot delete (has bookings)");
	              }
	          });

	          btnBack.setOnAction(e -> new AdminDashboard(currentUser).show(stage));

	          Scene scene = new Scene(root, 900, 640);
	          stage.setTitle("Manage Sessions");
	          stage.setScene(scene);
	          stage.show();


}
	    @SuppressWarnings("unchecked")
	    private<T> void addCol(String header, String property) {
	    	 TableColumn<Session, T> col = new TableColumn<>(header);
	         col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(property));
	         table.getColumns().add(col);
			
		}

		private void loadSessions() {
			// TODO Auto-generated method stub
			  List<Session> list = new ArrayList<>();
		        try (Connection conn = DatabaseConnection.getConnection();
		             PreparedStatement ps = conn.prepareStatement("SELECT * FROM sessions ORDER BY session_date")) {
		            ResultSet rs = ps.executeQuery();
		            while (rs.next()) {
		                list.add(new Session(rs.getInt("id"), rs.getString("title"),rs.getString("coach"), rs.getString("session_date"), rs.getString("session_time"),rs.getInt("capacity"), rs.getInt("slots_available"), rs.getString("level"), rs.getDouble("price")
		                ));
		            }
		        } catch (SQLException ex) {
		        	ex.printStackTrace(); 
		        	}
		        table.setItems(FXCollections.observableArrayList(list));
		    }
		

		private Button makeNavBtn(String text, String colorStart, String colorEnd, ImageView icon) {
			// TODO Auto-generated method stub
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

		
}
