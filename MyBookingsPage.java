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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyBookingsPage {

    private final User currentUser;
    private TableView<Booking> table;

    public MyBookingsPage(User user) {
        this.currentUser = user;
    }

    public void show(Stage stage) {

        // Title
        Label title = new Label("My Bookings");
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

        TableColumn<Booking, Integer> colId = new TableColumn<>("#");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        TableColumn<Booking, String> colSession = new TableColumn<>("Session");
        colSession.setCellValueFactory(new PropertyValueFactory<>("sessionTitle"));

        TableColumn<Booking, String> colDate = new TableColumn<>("Session Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));

        TableColumn<Booking, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));

        TableColumn<Booking, String> colBooked = new TableColumn<>("Booked On");
        colBooked.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

        TableColumn<Booking, Double> colPrice = new TableColumn<>("Price (Rs)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Booking, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(colId, colSession, colDate, colTime, colBooked, colPrice, colStatus);

       
        Label lblMsg = new Label(" ");
        lblMsg.setFont(Font.font(13));

        // Buttons with icons
        ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/close.png")));
        cancelIcon.setFitWidth(18);
        cancelIcon.setFitHeight(18);
        Button cancelBtn = new Button("Cancel Booking");
        cancelBtn.setGraphic(cancelIcon);
        cancelBtn.setContentDisplay(ContentDisplay.LEFT);
        cancelBtn.setPrefWidth(200);
        cancelBtn.setStyle(
            "-fx-background-color: #e53935;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;"
        );

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/back.png")));
        backIcon.setFitWidth(18);
        backIcon.setFitHeight(18);
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setGraphic(backIcon);
        backBtn.setContentDisplay(ContentDisplay.LEFT);
        backBtn.setPrefWidth(200);
        backBtn.setStyle(
            "-fx-background-color: #546e7a;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;"
        );

        HBox btnRow = new HBox(15, cancelBtn, backBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        // Root card
        VBox card = new VBox(15, title, table, lblMsg, btnRow);
        card.setPadding(new Insets(25));
        card.setMaxWidth(800);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);"
        );

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        // Load Data
        loadBookings();

        // Actions
        cancelBtn.setOnAction(e -> {
            Booking selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblMsg.setTextFill(Color.ORANGE);
                lblMsg.setText("Please select a booking to cancel.");
                return;
            }
            if ("cancelled".equals(selected.getStatus())) {
                lblMsg.setTextFill(Color.GRAY);
                lblMsg.setText("This booking is already cancelled.");
                return;
            }
            boolean ok = cancelBooking(selected.getId(), selected.getSessionId());
            if (ok) {
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Booking cancelled. Refund processed.");
                loadBookings();
            } else {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Could not cancel. Please try again.");
            }
        });

        backBtn.setOnAction(e -> new StudentDashboard(currentUser).show(stage));

        // Show scene
        Scene scene = new Scene(root, 900, 520);
        stage.setTitle("My Bookings");
        stage.setScene(scene);
        stage.show();
    }

    private void loadBookings() {
        List<Booking> list = new ArrayList<>();
        String sql =
            "SELECT b.id, b.session_id, b.booking_date, b.status, " +
            "       s.title, s.session_date, s.session_time, s.price " +
            "FROM bookings b " +
            "JOIN sessions s ON b.session_id = s.id " +
            "WHERE b.user_id = ? ORDER BY b.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking bk = new Booking();
                bk.setId(rs.getInt("id"));
                bk.setSessionId(rs.getInt("session_id"));
                bk.setBookingDate(rs.getString("booking_date"));
                bk.setStatus(rs.getString("status"));
                bk.setSessionTitle(rs.getString("title"));
                bk.setSessionDate(rs.getString("session_date"));
                bk.setSessionTime(rs.getString("session_time"));
                bk.setPrice(rs.getDouble("price"));
                list.add(bk);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(FXCollections.observableArrayList(list));
    }

    private boolean cancelBooking(int bookingId, int sessionId) {
        String sql1 = "UPDATE bookings SET status = 'cancelled' WHERE id = ?";
        String sql2 = "UPDATE sessions SET slots_available = slots_available + 1 WHERE id = ?";
        String sql3 = "UPDATE payments SET payment_status = 'refunded' WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, bookingId);
            ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, sessionId);
            ps2.executeUpdate();
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setInt(1, bookingId);
            ps3.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}