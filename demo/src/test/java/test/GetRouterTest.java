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


public class GetRouterTest {

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
    void testGetRouterBySerialNumber() {
        RouterModel router = new RouterModel();
        router.setBrand("TestBrand");
        router.setModel("TestModel");
        router.setSerialnbre("TestSerialNumber");
        router.setIpAddress("192.168.1.100");
        router.setUsername("testUser");
        router.setPwd("testPassword");

        routerDAO.addRouter(router);

        RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("TestSerialNumber");
        assertNotNull(retrievedRouter);
        assertEquals("TestBrand", retrievedRouter.getBrand());
    }
    @Test
    void testGetAllRouters() {
        // Similar to the above tests, add routers, retrieve all, and assert the results
        RouterModel router1 = new RouterModel();
        router1.setBrand("Brand1");
        router1.setModel("Model1");
        router1.setSerialnbre("Serial1");
        router1.setIpAddress("192.168.1.101");
        router1.setUsername("user1");
        router1.setPwd("password1");

        RouterModel router2 = new RouterModel();
        router2.setBrand("Brand2");
        router2.setModel("Model2");
        router2.setSerialnbre("Serial2");
        router2.setIpAddress("192.168.1.102");
        router2.setUsername("user2");
        router2.setPwd("password2");

        routerDAO.addRouter(router1);
        routerDAO.addRouter(router2);

        List<RouterModel> routers = routerDAO.getAllRouters();
        assertNotNull(routers);
        assertEquals(2, routers.size());
    }

    //READ 
//1: Read with snbre
@Test
void testGetNonExistentRouter() {
    // Try to retrieve a router with a serial number that doesn't exist
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("78888");
    //The retrieved router should be null
    assertNull(retrievedRouter);

}

@Test
void testGetRouterWithNullSerialNumber() {
    // Try to retrieve a router with a null serial number
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber(null);

    assertNull(retrievedRouter);
}
@Test
void testGetRouterWithEmptySerialNumber() {
    // Try to retrieve a router with an empty serial number
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("");

    assertNull(retrievedRouter);
}
@Test
void testGetRouterWithWrongDataTypeForSerialNumber() {
    // Try to retrieve a router with a serial number of the wrong data type
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber(123);  // Providing an integer instead of a string

    assertNull(retrievedRouter);
}

//2: Read all
@Test
void testGetAllRoutersWhenDatabaseIsEmpty() {
    List<RouterModel> routers = routerDAO.getAllRouters();
    assertTrue(routers.isEmpty(), "The list of routers should be empty when the database is empty");
}


}