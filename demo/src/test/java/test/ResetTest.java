
package test;




import com.example.db.DatabaseConnectionTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import java.io.ByteArrayOutputStream;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


import com.sun.net.httpserver.HttpServer;

import com.example.Business.*;
import com.example.DOA.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.InetSocketAddress;
import java.net.InetSocketAddress;

import java.io.OutputStream;
import java.util.concurrent.Executors;


public class ResetTest {

    private RouterDAO routerDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Set up the test environment, create tables, etc.
        routerDAO = new RouterDAO();
        createTestTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up the test environment, drop tables, etc.
        dropTestTable();
    }

    private void createTestTable() throws SQLException {
        try (Connection connection = DatabaseConnectionTest.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE routers (" +
                    "brand VARCHAR(255), " +
                    "model VARCHAR(255), " +
                    "serial_number VARCHAR(255) PRIMARY KEY, " +
                    "ip_address VARCHAR(15), " +
                    "username VARCHAR(255), " +
                    "password VARCHAR(255))");
        }
    }

    private void dropTestTable() throws SQLException {
        try (Connection connection = DatabaseConnectionTest.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE routers");
        }
    }
    @Test
    void testResetRouter() throws IOException, InterruptedException {
        // Create RouterDAO
        RouterDAO routerDAO = new RouterDAO();

        // Create a simple HttpClient
        HttpClient httpClient = HttpClient.newBuilder()
                .executor(Executors.newSingleThreadExecutor())
                .build();

        // Create a RouterModel for testing
        RouterModel router = new RouterModel();
        router.setIpAddress("192.168.1.1");

        // Perform the reset operation
        routerDAO.resetRouter(router, httpClient);

        // Example assertion:
        assertEquals(200, routerDAO.getLastResetResponseCode());
    }
//Raesset 

    private HttpClient httpClient;

    @BeforeEach
    public void setUpp() {
        routerDAO = new RouterDAO();
        httpClient = HttpClient.newHttpClient();
    }

  

    @Test
    public void testResetNonExistingRouter() {
        // Set up a non-existing router
        RouterModel nonExistingRouter = new RouterModel();
        nonExistingRouter.setBrand("Brand");
        nonExistingRouter.setModel("Model");
        nonExistingRouter.setSerialnbre("NonExistingSerial");
        nonExistingRouter.setIpAddress("192.168.1.101");
        nonExistingRouter.setUsername("user");
        nonExistingRouter.setPwd("password");

        // Attempt to reset a non-existing router
        routerDAO.resetRouter(nonExistingRouter, httpClient);

        int lastResetResponseCode = routerDAO.getLastResetResponseCode();
        assertEquals(0, lastResetResponseCode, "Resetting a non-existing router should return a not found code .");
    }

    @Test
    public void testResetRouterWithNullIPAddress() {
        // Set up a router with null IP address
        RouterModel routerWithNullIPAddress = new RouterModel();
        routerWithNullIPAddress.setBrand("Brand");
        routerWithNullIPAddress.setModel("Model");
        routerWithNullIPAddress.setSerialnbre("Serial");
        routerWithNullIPAddress.setIpAddress(null); // Set IP address to null
        routerWithNullIPAddress.setUsername("user");
        routerWithNullIPAddress.setPwd("password");

       
        routerDAO.resetRouter(routerWithNullIPAddress, httpClient);

        int lastResetResponseCode = routerDAO.getLastResetResponseCode();
        assertEquals(0, lastResetResponseCode, "Resetting a router with null IP address should return a bad request code .");
    }

    @Test
    public void testResetRouterWithInvalideIpAdressType() {
       
        RouterModel routerWithWrongDataType = new RouterModel();
        routerWithWrongDataType.setBrand("Brand");
        routerWithWrongDataType.setModel("Model");
        routerWithWrongDataType.setSerialnbre("Serial");
        routerWithWrongDataType.setIpAddress(12345); // Set IP address with wrong data type
        routerWithWrongDataType.setUsername("user");
        routerWithWrongDataType.setPwd("password");

     
        routerDAO.resetRouter(routerWithWrongDataType, httpClient);

        int lastResetResponseCode = routerDAO.getLastResetResponseCode();
        assertEquals(500, lastResetResponseCode, "Resetting a router with wrong data type should return an internal server error code (500).");
    }
}