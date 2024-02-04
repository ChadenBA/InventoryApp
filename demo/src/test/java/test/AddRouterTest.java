

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
 import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertNull;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddRouterTest {

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
@Epic("Login Tests Epic")
@Feature("Invalid Login Features")
   @Test
   @Story("User tries to login the system with invalid username and invalid password.")
   @Description("Invalid Login Test with Invalid Username and Invalid Password.")
    void testAddRouter() {
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




    
        //cas de test invalide 
        //use case ADD router 
        @Test
    void testAddRouterWithDuplicateSerialNumber() {
        RouterModel router1 = new RouterModel();
        router1.setBrand("TestBrand1");
        router1.setModel("TestModel1");
        router1.setSerialnbre("DuplicateSerialNumber");
        router1.setIpAddress("192.168.1.100");
        router1.setUsername("testUser1");
        router1.setPwd("testPassword1");
    
        RouterModel router2 = new RouterModel();
        router2.setBrand("TestBrand2");
        router2.setModel("TestModel2");
        router2.setSerialnbre("DuplicateSerialNumber");  // Same serial number as router1
        router2.setIpAddress("192.168.1.101");
        router2.setUsername("testUser2");
        router2.setPwd("testPassword2");
    
        // Add the first router (should be successful)
        routerDAO.addRouter(router1);
    
        // Try to add the second router with the same serial number
        assertThrows(SQLException.class, () -> routerDAO.addRouter(router2));
    }
    
    
    @Test
    void testAddRouterWithMissingField() {
        RouterModel router = new RouterModel();
        router.setBrand("TestBrand");
        router.setModel("TestModel");
        router.setSerialnbre("TestSerialNumber");
        router.setIpAddress("192.168.1.100");
        router.setUsername(null);  // Missing required field
    
        // Try to add the router with a missing required field
        assertThrows(SQLException.class, () -> routerDAO.addRouter(router));
    }
    
    
    @Test
    void testAddRouterWithNullSerialNumber() {
        RouterModel router = new RouterModel();
        router.setBrand("TestBrand");
        router.setModel("TestModel");
        router.setSerialnbre(null);  // Setting serial number to null
        router.setIpAddress("192.168.1.100");
        router.setUsername("testUser");
        router.setPwd("testPassword");
    
        // Attempt to add a router with a null serial number
        assertThrows(SQLException.class, () -> routerDAO.addRouter(router));
    }
    
    
    
    
    @Test
    void testAddRouterWithInvalidDataType() {
        RouterModel router = new RouterModel();
        router.setBrand("TestBrand");
        router.setModel("TestModel");
     // router.setSerialnbre(1222);  // putting serial number an integer instead of string 
        router.setIpAddress("192.168.1.100");
        router.setUsername("testUser");
        router.setPwd("testPassword");
    
        // Attempt to add a router with a null serial number
        assertThrows(SQLException.class, () -> routerDAO.addRouter(router));
    }
    
    
    
    
    
    


}