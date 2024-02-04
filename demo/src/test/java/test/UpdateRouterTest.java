
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


public class UpdateRouterTest {

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
    void testUpdateRouter() {
        RouterModel router = new RouterModel();
        router.setBrand("Brand");
        router.setModel("Model");
        router.setSerialnbre("Serial");
        router.setIpAddress("192.168.1.100");
        router.setUsername("user");
        router.setPwd("password");

        routerDAO.addRouter(router);

        // Update the router's information
        RouterModel updatedRouter = new RouterModel();
        updatedRouter.setBrand("UpdatedBrand");
        updatedRouter.setModel("UpdatedModel");
        updatedRouter.setSerialnbre("Serial");
        updatedRouter.setIpAddress("192.168.1.200");
        updatedRouter.setUsername("updatedUser");
        updatedRouter.setPwd("updatedPassword");

        routerDAO.updateRouter(updatedRouter);

        // Retrieve the updated router and check if it was modified
        RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("Serial");
        assertNotNull(retrievedRouter);
        assertEquals("UpdatedBrand", retrievedRouter.getBrand());
        assertEquals("192.168.1.200", retrievedRouter.getIpAddress());
    }

    //UPDATE
@Test

void testUpdateNonExistentRouter() {
    // Attempt to update a router that doesn't exist
    RouterModel updatedRouter = new RouterModel();
    updatedRouter.setBrand("UpdatedBrand");
    updatedRouter.setModel("UpdatedModel");
    updatedRouter.setSerialnbre("nvrm");
    updatedRouter.setIpAddress("192.168.1.100");
    updatedRouter.setUsername("updatedUser");
    updatedRouter.setPwd("updatedPassword");

    routerDAO.updateRouter(updatedRouter);

    // Verify that the router remains non-existent in the database
    RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("nvrm");
    //"The non-existent router should not be updated"
    assertNull(retrievedRouter);
}

@Test
void testUpdateRouterWithNullSerialNumber() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    // Attempt to update a router with a null serial number
    RouterModel updatedRouter = new RouterModel();
    updatedRouter.setBrand("UpdatedBrand");
    updatedRouter.setModel("UpdatedModel");
    updatedRouter.setSerialnbre(null);  // Setting the serial number to null
    updatedRouter.setIpAddress("192.168.1.200");
    updatedRouter.setUsername("updatedUser");
    updatedRouter.setPwd("updatedPassword");
    routerDAO.updateRouter(updatedRouter);

    // Verify that the original router is not updated
    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertEquals("Brand", originalRouter.getBrand(), "The original router should not be updated");
}



@Test
void testUpdateRouterWithEmptySerialNumber() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    
    RouterModel updatedRouter = new RouterModel();
    updatedRouter.setBrand("ssssss");
    updatedRouter.setModel(",xsk");
    updatedRouter.setSerialnbre(""); 
    updatedRouter.setIpAddress("ssssss");
    updatedRouter.setUsername("sssss");
    updatedRouter.setPwd("ssssssss");
    routerDAO.updateRouter(updatedRouter);

    // Verify that the original router is not updated
    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertEquals("Brand", originalRouter.getBrand(), "The original router should not be updated");
}


@Test
void testUpdateRouterWithInvalidDataType() {
    RouterModel router = new RouterModel();
    router.setBrand("Brand");
    router.setModel("Model");
    router.setSerialnbre("Serial");
    router.setIpAddress("192.168.1.100");
    router.setUsername("user");
    router.setPwd("password");
    routerDAO.addRouter(router);

    // Attempt to update a router with an invalid IP address
    RouterModel updatedRouter = new RouterModel();
    updatedRouter.setBrand("UpdatedBrand");
    updatedRouter.setModel("UpdatedModel");
    updatedRouter.setSerialnbre(11222);
    updatedRouter.setIpAddress("IPAddress"); 
    updatedRouter.setUsername("updatedUser");
    updatedRouter.setPwd("updatedPassword");
    routerDAO.updateRouter(updatedRouter);


    RouterModel originalRouter = routerDAO.getRouterBySerialNumber("Serial");
    assertEquals("Brand", originalRouter.getBrand(), "The original router should not be updated");
}
}