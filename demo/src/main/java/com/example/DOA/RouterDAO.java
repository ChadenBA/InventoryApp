package com.example.DOA;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.Business.RouterModel;
import com.example.db.DatabaseConnectionTest;



public class RouterDAO {

    // CREATE
    public void addRouter(RouterModel router) {
        String query = "INSERT INTO routers (brand, model, serial_number, ip_address, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnectionTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, router.getBrand());
            preparedStatement.setString(2, router.getModel());
            preparedStatement.setString(3, router.getSerialnbre());
            preparedStatement.setString(4, router.getIpAddress());
            preparedStatement.setString(5, router.getUsername());
            preparedStatement.setString(6, router.getPwd());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public RouterModel getRouterBySerialNumber(String serialNumber) {
        String query = "SELECT * FROM routers WHERE serial_number = ?";
        RouterModel router = null;

        try (Connection connection = DatabaseConnectionTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, serialNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    router = extractRouterFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return router;
    }

    public List<RouterModel> getAllRouters() {
        String query = "SELECT * FROM routers";
        List<RouterModel> routers = new ArrayList<>();

        try (Connection connection = DatabaseConnectionTest.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                routers.add(extractRouterFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routers;
    }

    // UPDATE
    public void updateRouter(RouterModel updatedRouter) {
        String query = "UPDATE routers SET brand = ?, model = ?, ip_address = ?, username = ?, password = ? WHERE serial_number = ?";

        try (Connection connection = DatabaseConnectionTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, updatedRouter.getBrand());
            preparedStatement.setString(2, updatedRouter.getModel());
            preparedStatement.setString(3, updatedRouter.getIpAddress());
            preparedStatement.setString(4, updatedRouter.getUsername());
            preparedStatement.setString(5, updatedRouter.getPwd());
            preparedStatement.setString(6, updatedRouter.getSerialnbre());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteRouter(String serialNumber) {
        String query = "DELETE FROM routers WHERE serial_number = ?";

        try (Connection connection = DatabaseConnectionTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, serialNumber);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private RouterModel extractRouterFromResultSet(ResultSet resultSet) throws SQLException {
        RouterModel router = new RouterModel();
        router.setBrand(resultSet.getString("brand"));
        router.setModel(resultSet.getString("model"));
        router.setSerialnbre(resultSet.getString("serial_number"));
        router.setIpAddress(resultSet.getString("ip_address"));
        router.setUsername(resultSet.getString("username"));
        router.setPwd(resultSet.getString("password"));
        return router;
    }

    private HttpClient httpClient; // New field for HttpClient

    public void setHttpClient(CustomHttpClient httpClient) {
        this.httpClient = (HttpClient) httpClient;
    }

    //reboot 
    public void rebootRouter(RouterModel router) {
        try {
            String rebootUrl = "http://" + router.getIpAddress()  + "/reboot"; // Replace with the actual reboot URL
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(rebootUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Reboot response code: " + response.statusCode());
            System.out.println("Reboot response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //reset 
    private int lastResetResponseCode;  // Add this variable to store the last reset response code
    // reset 
    public void resetRouter(RouterModel router, HttpClient client) {
        try {
            String resetUrl = "http://" + router.getIpAddress() + "/reset"; // Replace with the actual reset URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(resetUrl))
                    .build();
    
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            // Store the last reset response code
            lastResetResponseCode = response.statusCode();
    
            System.out.println("Reset response code: " + lastResetResponseCode);
            System.out.println("Reset response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    // Add a getter for lastResetResponseCode
    public int getLastResetResponseCode() {
        return lastResetResponseCode;
    }

    //connectivity 
    public void routerConnectivity(String serialNumber) {
        RouterModel router =getRouterBySerialNumber(serialNumber);

        if (router != null) {
            // Check connectivity
            boolean isReachable = checkRouterConnectivity(router.getIpAddress());

            if (isReachable) {
                System.out.println("Router with serial number " + serialNumber + " is reachable.");
            } else {
                System.out.println("Router with serial number " + serialNumber + " is not reachable.");
            }
        } else {
            System.out.println("Router with serial number " + serialNumber + " not found in the database.");
        }
    }

   

    private boolean checkRouterConnectivity(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isReachable(5000); // 5000 milliseconds timeout
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

