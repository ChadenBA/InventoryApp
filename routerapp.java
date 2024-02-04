
import java.util.List;
import java.util.Scanner;

import Business.RouterModel;
import DOA.RouterDAO;
public class routerapp {

    public static void main(String[] args) {
        RouterDAO routerDAO = new RouterDAO();

        // CREATE
       Scanner scanner = new Scanner(System.in);

        // Create a RouterModel object to store user input
        RouterModel router = new RouterModel();

        // Get user input for each property
        System.out.println("Enter router brand:");
        router.setBrand(scanner.nextLine());

        System.out.println("Enter router model:");
        router.setModel(scanner.nextLine());

        System.out.println("Enter router serial number:");
        router.setSerialnbre(scanner.nextLine());

        System.out.println("Enter router IP address:");
        router.setIpAddress(scanner.nextLine());

        System.out.println("Enter router username:");
        router.setUsername(scanner.nextLine());

        System.out.println("Enter router password:");
        router.setPwd(scanner.nextLine());

        // Call the addRouter method to insert the router into the database
        RouterDAO  database = new RouterDAO();
        database.addRouter(router);

        // // // READ
         RouterModel retrievedRouter = routerDAO.getRouterBySerialNumber("123456789");
        // System.out.println("Retrieved Router: " + retrievedRouter.getBrand());

    // //      // UPDATE
    //   retrievedRouter.setIpAddress("192.168.1.1");
    // //    routerDAO.updateRouter(retrievedRouter);
    // //    System.out.println("Retrieved Router: " + retrievedRouter.getIpAddress());

    //     // // READ All
    //     // List<RouterModel> allRouters = routerDAO.getAllRouters();
    //     // System.out.println("All Routers: " + allRouters);

    //     // // DELETE
    //     // routerDAO.deleteRouter("12345");

    //     // Verify deletion
    //    // System.out.println("All Routers after deletion: " + routerDAO.getAllRouters());

    //     RouterModel router = new RouterModel();
    //     router.setIpAddress("192.168.1.1"); // Replace with your router's IP address

    //     // Reboot the router
    //     routerDAO.rebootRouter(router);
    //     routerDAO.routerConnectivity("1237");
    //     // Reset the router
    //     //router.resetRouter();

    }
}
