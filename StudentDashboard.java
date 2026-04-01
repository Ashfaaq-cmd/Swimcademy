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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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

       
        Label welcome = new Label("Welcome, " + currentUser.getFullName());
        welcome.setFont(Font.font("System", FontWeight.BOLD, 26));
        welcome.setTextFill(Color.web("#0d47a1"));

        Label subtitle = new Label("Student Portal");
        subtitle.setFont(Font.font("System", FontPosture.ITALIC, 13));
        subtitle.setTextFill(Color.GRAY);

        //Stats 
        int[] stats = fetchStats();

        VBox card1 = makeCard("Total Bookings", String.valueOf(stats[0]), "#fb8c00");
        VBox card2 = makeCard("Active Bookings", String.valueOf(stats[1]), "#43a047");
        VBox card3 = makeCard("Cancelled", String.valueOf(stats[2]), "#e53935");
        VBox card4 = makeCard("Available Sessions", String.valueOf(stats[3]), "#1e88e5");

        HBox cards = new HBox(15, card1, card2, card3, card4);
        cards.setAlignment(Pos.CENTER);


        // Book a Session button
        ImageView bookIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/book.png")));
        bookIcon.setFitWidth(18);
        bookIcon.setFitHeight(18);
        Button btnBook = makeNavBtn("Book a Session", "#fb8c00", "#ef6c00", bookIcon);

        // My Bookings button
        ImageView myBookIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/booking.png")));
        myBookIcon.setFitWidth(18);
        myBookIcon.setFitHeight(18);
        Button btnMyBook = makeNavBtn("My Bookings", "#43a047", "#2e7d32", myBookIcon);

        // Logout button
        ImageView logoutIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/logout.png")));
        logoutIcon.setFitWidth(18);
        logoutIcon.setFitHeight(18);
        Button btnLogout = makeNavBtn("Logout", "#e53935", "#b71c1c", logoutIcon);;

        VBox navBox = new VBox(12, btnBook, btnMyBook, btnLogout);
        navBox.setAlignment(Pos.CENTER);

        Label quick = new Label("Quick Actions:");
        quick.setFont(Font.font("System", FontWeight.BOLD, 15));
        quick.setTextFill(Color.web("#333"));

        //Card Container
        VBox card = new VBox(18,
                welcome,
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
        btnBook.setOnAction(e -> new BookSessionPage(currentUser).show(stage));
        btnMyBook.setOnAction(e -> new MyBookingsPage(currentUser).show(stage));
        btnLogout.setOnAction(e -> new LoginPage().show(stage));

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Student Dashboard");
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

        int tot = 0, active = 0, cancelled = 0, available = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Total
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM bookings WHERE user_id = ?");
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) tot = rs.getInt(1);

            // Active
            ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND status = 'confirmed'");
            ps.setInt(1, currentUser.getId());
            rs = ps.executeQuery();
            if (rs.next()) active = rs.getInt(1);

            // Cancelled
            ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND status = 'cancelled'");
            ps.setInt(1, currentUser.getId());
            rs = ps.executeQuery();
            if (rs.next()) cancelled = rs.getInt(1);

            // Available Sessions
            ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM sessions WHERE slots_available > 0");
            rs = ps.executeQuery();
            if (rs.next()) available = rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new int[]{tot, active, cancelled, available};
    }
}