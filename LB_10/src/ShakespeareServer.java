import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShakespeareServer {
    private static final int PORT = 12345;
    private static List<String> sonnets = new ArrayList<>();

    public static void main(String[] args) {
        loadSonnets(); // Загрузка сонетов из файла

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер ожидает подключения на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен клиент: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String request = reader.readLine();
            if ("GET_SONNET".equals(request)) {
                String randomSonnet = getRandomSonnet();
                clientSocket.getOutputStream().write(randomSonnet.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSonnets() {
        try (BufferedReader reader = new BufferedReader(new FileReader("shakespeare_sonnets.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sonnets.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomSonnet() {
        Random random = new Random();
        return sonnets.get(random.nextInt(sonnets.size()));
    }
}
