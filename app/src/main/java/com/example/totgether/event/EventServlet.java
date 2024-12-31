package com.example.totgether.event;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EventServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createEvent(request, response);
                break;
            case "edit":
                editEvent(request, response);
                break;
            case "delete":
                deleteEvent(request, response);
                break;
            case "getAll":
                getAllEvents(request, response);
                break;
            default:
                writeResponse(response, "error", "Invalid action");
        }
    }

    private void createEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String date = request.getParameter("date");
        String address = request.getParameter("address");
        String reason = request.getParameter("reason");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            String query = "INSERT INTO events (user_id, date, address, reason) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setString(2, date);
                stmt.setString(3, address);
                stmt.setString(4, reason);
                stmt.executeUpdate();
                writeResponse(response, "success", null);
            }
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void editEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String date = request.getParameter("date");
        String address = request.getParameter("address");
        String reason = request.getParameter("reason");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            String query = "UPDATE events SET date = ?, address = ?, reason = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, date);
                stmt.setString(2, address);
                stmt.setString(3, reason);
                stmt.setInt(4, eventId);
                stmt.executeUpdate();
                writeResponse(response, "success", null);
            }
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void deleteEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int eventId = Integer.parseInt(request.getParameter("eventId"));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            String query = "DELETE FROM events WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, eventId);
                stmt.executeUpdate();
                writeResponse(response, "success", null);
            }
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void getAllEvents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("[");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/totgether", "root", "Root0110")) {
            String query = "SELECT * FROM events WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    jsonResponse.append("{")
                            .append("\"id\":").append(rs.getInt("id")).append(",")
                            .append("\"date\":\"").append(rs.getString("date")).append("\",")
                            .append("\"address\":\"").append(rs.getString("address")).append("\",")
                            .append("\"reason\":\"").append(rs.getString("reason")).append("\"")
                            .append("},");
                }
                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Удаляем последнюю запятую
                }
                jsonResponse.append("]");
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException e) {
            writeResponse(response, "error", e.getMessage());
        }
    }

    private void writeResponse(HttpServletResponse response, String status, String message) throws IOException {
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("status", status);
        if (message != null) {
            jsonResponse.put("message", message);
        }

        PrintWriter out = response.getWriter();
        out.write(new JSONObject(jsonResponse).toString());
        out.flush();
    }
}
