package com.example.totgether;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class SimpleServer {
    private static final int PORT = 3306;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту: " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            StringBuilder requestBody = new StringBuilder();

            // Чтение заголовков
            while (!(inputLine = in.readLine()).isEmpty()) {
                // Обрабатываем только заголовки
            }

            // Чтение тела запроса
            char[] buffer = new char[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                requestBody.append(buffer, 0, read);
            }

            // Обработка запросов на вход и регистрацию
            String response;
            if (requestBody.toString().startsWith("action=register")) {
                response = registerUser(requestBody.toString());
            } else if (requestBody.toString().startsWith("action=login")) {
                response = login(requestBody.toString());
            } else {
                response = "Ошибка: Неправильное действие.";
            }

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println();
            out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String login(String requestBody) {
        String login = null;
        String password = null;

        String[] params = requestBody.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1].replace("+", " "); // Заменяем '+' на пробелы
                if ("login".equals(key)) {
                    login = value;
                } else if ("password".equals(key)) {
                    password = value;
                }
            }
        }

        if (login == null || password == null) {
            return "Ошибка: login или пароль не переданы.";
        }

        try (Connection conn = connectToDatabase()) {
            String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return "Вход успешен";
                } else {
                    return "Неверная электронная почта или пароль";
                }
            }
        } catch (SQLException e) {
            return "Ошибка соединения с базой данных: " + e.getMessage();
        }
    }

    private static String registerUser(String requestBody) {
        String login = null;
        String password = null;
        String[] params = requestBody.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1].replace("+", " "); // Заменяем '+' на пробелы
                if ("login".equals(key)) {
                    login = value;
                } else if ("password".equals(key)) {
                    password = value;
                }
            }
        }

        if (login == null || password == null) {
            return "Ошибка: login или пароль не переданы.";
        }

        try (Connection conn = connectToDatabase()) {
            // Проверка существования пользователя
            String checkSql = "SELECT * FROM users WHERE login = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, login);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    return "Ошибка: Пользователь с таким login уже существует.";
                }
            }

            // Регистрация нового пользователя
            String sql = "INSERT INTO users (login, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, password);
                stmt.executeUpdate();
                return "Регистрация успешна";
            }
        } catch (SQLException e) {
            return "Ошибка соединения с базой данных: " + e.getMessage();
        }
    }

    private static Connection connectToDatabase() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/totgether";
        String dbUser = "root";
        String dbPassword = "Root0110";
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}