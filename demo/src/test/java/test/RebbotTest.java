
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


public class RebbotTest {

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

    //reboot 
    @Test
    void testRebootRouter() throws IOException, InterruptedException {
        // Set up and start the embedded HTTP server
        int port = 3000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/reboot", new RebootHandler());
        Executors.newSingleThreadExecutor().submit(httpServer::start);

        // Create RouterDAO with the actual HttpClient
        RouterDAO routerDAO = new RouterDAO();

        // Create a RouterModel for testing
        RouterModel router = new RouterModel();
        router.setIpAddress("http://localhost:" + port);

        // Perform the reboot operation
        routerDAO.rebootRouter(router);

        // Stop the embedded HTTP server
        httpServer.stop(0);
    }

    // Handler to simulate the /reboot endpoint
    static class RebootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Simulate a successful response
            String response = "Reboot successful";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
@Test
void testRebootNonExistentRouter() {
    // Attempt to reboot a router that doesn't exist
    RouterModel nonExistentRouter = new RouterModel();
    nonExistentRouter.setSerialnbre("NonExistentSerialNumber");

    routerDAO.rebootRouter(nonExistentRouter);

    //No assertion is needed as this is just to verify no exceptions are thrown
}

@Test
void testRebootRouterWithNullIPAddress() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("ValidSerialNumber");
    // Set other properties, but leave IP address as null
    routerDAO.addRouter(router);

    // Attempt to reboot the router with a null IP address
    router.setIpAddress(null);
    routerDAO.rebootRouter(router);

    // No assertion is needed as this is just to verify no exceptions are thrown
}


@Test
void testRebootRouterWithInvalidIPAddress() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("ValidSerialNumber");
    // Set other properties with a valid IP address
    routerDAO.addRouter(router);

    // Attempt to reboot the router with an invalid IP address
    router.setIpAddress("InvalidIPAddress");
    routerDAO.rebootRouter(router);

    // No assertion is needed as this is just to verify no exceptions are thrown
}
@Test
void testRebootRouterWithInvalidDataType() {
    RouterModel router = new RouterModel();
    router.setSerialnbre("ValidSerialNumber");
    // Set other properties with a valid IP address
    routerDAO.addRouter(router);

    // Attempt to reboot the router with an invalid IP address
    router.setIpAddress(1111);
    routerDAO.rebootRouter(router);

    // No assertion is needed as this is just to verify no exceptions are thrown
}
}