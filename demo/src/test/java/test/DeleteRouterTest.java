
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


public class DeleteRouterTest {

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

      // @Test
    // void testDeleteRouter() {
    //     RouterModel router = new RouterModel();
    //     router.setBrand("Brand");
    //     router.setModel("Model");
    //     router.setSerialnbre("Serial");
    //     router.setIpAddress("192.168.1.100");
    //     router.setUsername("user");
    //     router.setPwd("password");

    //     routerDAO.addRouter(router);

    //     // Delete the router and check if it no longer exists
    //     routerDAO.deleteRouter("Serial");
    //     RouterModel deletedRouter = routerDAO.getRouterBySerialNumber("Serial");
    //     assertNull(deletedRouter);
    // }

    //delete 
@Test
void testDeleteNonExistentRouter() {
    // Attempt to delete a router that doesn't exist
    routerDAO.deleteRouter("NonExistentSerialNumber");

    // Verify that no router is deleted
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("NonExistentSerialNumber");
    //"No router should be deleted as it doesn't exist"
    assertNull(retrievedRouter);
}

@Test
void testDeleteRouterWithNullSerialNumber() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    // Attempt to delete a router with a null serial number
    routerDAO.deleteRouter(null);

    // Verify that the original router is not deleted
    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertNotNull(originalRouter, "The original router should not be deleted");
}

@Test
void testDeleteRouterWithInvalidSerialNumber() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    // Attempt to delete a router with an invalid serial number
    routerDAO.deleteRouter("");

    // Verify that the original router is not deleted
    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertNotNull(originalRouter, "The original router should not be deleted");
}
@Test
void testDeleteRouterWithInvalidSerialNumberType() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    // Attempt to delete a router with an invalid serial number
    routerDAO.deleteRouter(5555);

    // Verify that the original router is not deleted
    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertNotNull(originalRouter, "The original router should not be deleted");
}

}

