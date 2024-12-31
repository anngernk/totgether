package com.example.totgether;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        } else {
            writeResponse(response, "error", "Invalid action");
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Проверка на пустые поля
        if (username == null ||password == null || username.isEmpty() || password.isEmpty()) {
            writeResponse(response, "error", "Username and password must not be null or empty");
            return;
        }

        // Хранение пользователя в БД
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            // Проверка на существование пользователя
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    writeResponse(response, "error", "Username already exists");
                    return;
                }
            }

            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // В реальном приложении используйте хеширование
                stmt.executeUpdate();
            }
            writeResponse(response, "success", null);
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Проверка на пустые поля
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            writeResponse(response, "error", "Username and password must not be null or empty");
            return;
        }

        // Проверка пользователя в БД
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    writeResponse(response, "success", String.valueOf(rs.getInt("id")));
                } else {
                    writeResponse(response, "error", "Invalid credentials");
                }
            }
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void writeResponse(HttpServletResponse response, String status, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse.put("status", status); // Добавление статуса
            if (message != null) {
                jsonResponse.put("message", message);
            }
        } catch (JSONException e) {
            // Обработка исключения
            e.printStackTrace();
            writeResponse(response, "error", "Ошибка при создании JSON");
            return;
        }

        PrintWriter out = response.getWriter();
        out.write(jsonResponse.toString());
        out.flush();
    }
}