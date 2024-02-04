
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


public class ConnectTest {

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

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    void testRouterConnectivity() {
        // Assuming there is a router with the given serial number in the database
        RouterDAO routerDAO = new RouterDAO();
        RouterModel router = new RouterModel();
        router.setIpAddress("192.168.1.1");
        routerDAO.addRouter(router);
    
        // Perform the router connectivity test
        routerDAO.routerConnectivity("TestSerialNumber");
    
        // Capture the console output
        String consoleOutput = outputStream.toString();
    
        // Check if the console output contains the expected message
        assertFalse(consoleOutput.contains("Router with serial number TestSerialNumber is not reachable."));
    }


    @Test
void testRouterConnectivityWithNonExistingRouter() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("NonExistingRouter");
    // Set other properties with a valid IP address
    routerDAO.addRouter(router);

    // Attempt to check the connectivity of the router with an invalid IP address
    router.setIpAddress("192.168.1.2");
    routerDAO.routerConnectivity("NonExistingRouter");

}
@Test
void testRouterConnectivityWithNullIPAddress() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("valideSerial number");
    // Set other properties with a valid IP address
    routerDAO.addRouter(router);

    // Attempt to check the connectivity of the router with an invalid IP address
    router.setIpAddress(null);
    routerDAO.routerConnectivity("ValidSerialNumber");

}
@Test
void testRouterConnectivityWithInvalidIPAddress() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("ValidSerialNumber");
    // Set other properties with a valid IP address
    routerDAO.addRouter(router);

    // Attempt to check the connectivity of the router with an invalid IP address
    router.setIpAddress(111111);
    routerDAO.routerConnectivity("ValidSerialNumber");

}





}