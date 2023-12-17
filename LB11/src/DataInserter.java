import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DataInserter {

    private Connection connection;
    private Date sendDate;

    public DataInserter(Connection connection) {
        this.connection = connection;
    }

    public void insertPerson(String fullName, Date birthDate) {
        String sql = "INSERT INTO persons (full_name, birth_date) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setDate(2, new java.sql.Date(birthDate.getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Обработка исключения
        }
    }

    public void insertEmail(int senderId, int receiverId, String subject, String text) {
        String sql = "INSERT INTO emails (sender_id, receiver_id, subject, text, send_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setString(3, subject);
            statement.setString(4, text);
            statement.setDate(5, new java.sql.Date(sendDate.getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Обработка исключения
        }
    }
}
