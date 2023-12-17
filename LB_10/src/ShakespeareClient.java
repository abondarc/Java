import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ShakespeareClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Отправляем запрос на получение сонета
            socket.getOutputStream().write("GET_SONNET\n".getBytes());

            // Получаем и выводим сонет
            String sonnet = reader.readLine();
            System.out.println("Получен сонет:\n" + sonnet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

