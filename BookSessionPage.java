import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

        
        Label title = new Label("📅 Book a Session");
        title.setFont(Font.font("System", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#0d47a1"));

      
        Label lblFilter = new Label("Level:");
        lblFilter.setFont(Font.font("System", FontWeight.BOLD, 13));

        ComboBox<String> lvlFilter = new ComboBox<>();
        lvlFilter.getItems().addAll("All", "Beginner", "Intermediate", "Advanced");
        lvlFilter.setValue("All");
        lvlFilter.setPrefWidth(160);

        Button btnFilter = new Button("Apply");
        btnFilter.setStyle(
            "-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);" +
            "-fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-background-radius: 20; -fx-padding: 6 16;"
        );

        HBox filterRow = new HBox(10, lblFilter, lvlFilter, btnFilter);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        //table 
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(320);

        table.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );

        TableColumn<Session, String> colTitle = new TableColumn<>("Session");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Session, String> colCoach = new TableColumn<>("Coach");
        colCoach.setCellValueFactory(new PropertyValueFactory<>("coach"));

        TableColumn<Session, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));

        TableColumn<Session, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));

        TableColumn<Session, String> colLvl = new TableColumn<>("Level");
        colLvl.setCellValueFactory(new PropertyValueFactory<>("level"));

        TableColumn<Session, Integer> colSlots = new TableColumn<>("Slots Left");
        colSlots.setCellValueFactory(new PropertyValueFactory<>("slotsAvailable"));

        TableColumn<Session, Double> colPrice = new TableColumn<>("Price (Rs)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(colTitle, colCoach, colDate, colTime, colLvl, colSlots, colPrice);

       
        Label lblMsg = new Label();
        lblMsg.setStyle("-fx-font-size: 13;");

     // Book Button
        ImageView tickIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/tick.png")));
        tickIcon.setFitWidth(18);
        tickIcon.setFitHeight(18);
        Button btnBook = new Button("Book Session");
        btnBook.setGraphic(tickIcon);
        btnBook.setContentDisplay(ContentDisplay.LEFT);
        btnBook.setPrefWidth(220);
        btnBook.setStyle(
            "-fx-background-color: linear-gradient(to right, #43a047, #2e7d32);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 18;"
        );

        // Back Button
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/back.png")));
        backIcon.setFitWidth(18);
        backIcon.setFitHeight(18);
        Button btnBack = new Button("Back");
        btnBack.setGraphic(backIcon);
        btnBack.setContentDisplay(ContentDisplay.LEFT);
        btnBack.setPrefWidth(140);
        btnBack.setStyle(
            "-fx-background-color: #546e7a;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;"
        );

        HBox btnRow = new HBox(15, btnBook, btnBack);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        
        VBox card = new VBox(15, title, filterRow, table, btnRow, lblMsg);
        card.setPadding(new Insets(25));
        card.setMaxWidth(780);

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

        //Load Data 
        loadSessions("All");

        //Actions 
        btnFilter.setOnAction(e -> {
            lblMsg.setText("");
            loadSessions(lvlFilter.getValue());
        });

        btnBook.setOnAction(e -> {
            Session selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                lblMsg.setTextFill(Color.ORANGE);
                lblMsg.setText("Please select a session first.");
                return;
            }

            if (selected.getSlotsAvailable() == 0) {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Sorry, this session is full.");
                return;
            }

            if (alreadyBooked(currentUser.getId(), selected.getId())) {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("You have already booked this session.");
                return;
            }

            boolean success = bookSession(currentUser.getId(), selected);

            if (success) {
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Session booked successfully!");
                loadSessions(lvlFilter.getValue());
            } else {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Booking failed. Please try again.");
            }
        });

        btnBack.setOnAction(e -> new StudentDashboard(currentUser).show(stage));

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Book a Session");
        stage.setScene(scene);
        stage.show();
    }

    

    private boolean bookSession(int userId, Session session) {
        String sqlBook = "INSERT INTO bookings(user_id, session_id, status) VALUES (?,?,'confirmed')";
        String sqlUpdate = "UPDATE sessions SET slots_available = slots_available -1 WHERE id = ?";
        String sqlPayment = "INSERT INTO payments (booking_id, amount, payment_status) VALUES (?,?,'paid')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(sqlBook, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, userId);
            ps1.setInt(2, session.getId());
            ps1.executeUpdate();

            ResultSet keys = ps1.getGeneratedKeys();
            int bookingId = 0;
            if (keys.next()) {
                bookingId = keys.getInt(1);
            }

            PreparedStatement ps2 = conn.prepareStatement(sqlUpdate);
            ps2.setInt(1, session.getId());
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement(sqlPayment);
            ps3.setInt(1, bookingId);
            ps3.setDouble(2, session.getPrice());
            ps3.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean alreadyBooked(int userId, int sessionId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND session_id = ? AND status = 'confirmed'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, sessionId);
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void loadSessions(String level) {
        List<Session> sessions = new ArrayList<>();

        String sql = level.equals("All")
                ? "SELECT * FROM sessions WHERE slots_available > 0 ORDER BY session_date, session_time"
                : "SELECT * FROM sessions WHERE slots_available > 0 AND level = ? ORDER BY session_date, session_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!level.equals("All")) ps.setString(1, level);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("coach"),
                        rs.getString("session_date"),
                        rs.getString("session_time"),
                        rs.getInt("capacity"),
                        rs.getInt("slots_available"),
                        rs.getString("level"),
                        rs.getDouble("price")
                ));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        table.setItems(FXCollections.observableArrayList(sessions));
    }
}