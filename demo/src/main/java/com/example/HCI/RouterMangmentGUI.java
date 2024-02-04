package com.example.HCI;

import java.net.http.HttpClient;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

import com.example.Business.RouterModel;
import com.example.DOA.RouterDAO;

public class RouterMangmentGUI extends Application {
    private RouterDAO routerDAO = new RouterDAO();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Router Management");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        HBox addRouterContainer = createAddRouterContainer();
        GridPane routersContainer = createRoutersContainer();

        root.getChildren().addAll(addRouterContainer, routersContainer);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private HBox createAddRouterContainer() {
        HBox addRouterContainer = new HBox(10);

        Button addRouterButton = new Button("Add Router");
        addRouterButton.setOnAction(e -> showCreateRouterForm());

        addRouterContainer.getChildren().addAll(addRouterButton);

        return addRouterContainer;
    }

    private GridPane createRoutersContainer() {
        GridPane routersContainer = new GridPane();
        routersContainer.setHgap(10); // Horizontal gap between columns
        routersContainer.setVgap(10); // Vertical gap between rows

        List<RouterModel> routers = routerDAO.getAllRouters();

        int column = 0;
        int row = 0;

        for (RouterModel router : routers) {
            VBox routerBox = createRouterBox(router);
            routersContainer.add(routerBox, column, row);

            // Update column and row indices
            column++;
            if (column == 3) { // Assuming you want 3 columns in each row, adjust as needed
                column = 0;
                row++;
            }
        }

        return routersContainer;
    }
    private VBox createRouterBox(RouterModel router) {
        RouterService routerService =new RouterService();
        VBox routerBox = new VBox(10);
        routerBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px;  -fx-width:100px;"); // Optional border styling
        routerBox.setPrefWidth(200);
        // Image section
        ImageView routerImage = new ImageView(new Image("file:demo/src/main/java/com/example/HCI/router.jpeg"));
        routerImage.setFitWidth(200);
        routerImage.setFitHeight(200);
    
        // Details button
        Button showDetailsButton = new Button("Show Details");
        showDetailsButton.setOnAction(e -> routerService.showRouterDetails(router));
        
    
        // Serial number label
        Label serialNumberLabel = new Label("Serial Number: " + router.getSerialnbre());
    
        // Icon buttons
        Button deleteButton = new Button("");
         ImageView view = new ImageView("file:demo/src/main/java/com/example/HCI/icons8-delete-30.png");
         deleteButton.setGraphic(view);
        
          deleteButton.setOnAction(e -> {
    // Display a confirmation dialog (optional)
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Dialog");
    alert.setHeaderText("Delete Router");
    alert.setContentText("Are you sure you want to delete this router?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        routerDAO.deleteRouter(router.getSerialnbre());
        // You may also update the UI to reflect the deletion
    }
});


        Button updateButton = new Button("");
        updateButton.setOnAction(e -> showUpdateRouterForm(router));
        ImageView view1 = new ImageView("file:HCI/icons8-edit-30.png");
         updateButton.setGraphic(view1);
        
     
        Button rebootButton = new Button("");
        rebootButton.setOnAction(e -> routerDAO.rebootRouter(router));
        ImageView view2 = new ImageView("file:demo/src/main/java/com/example/HCI/icons8-reboot-30.png");
         rebootButton.setGraphic(view2);

        Button resetButton = new Button("");
   // Use newBuilder() to create an instance of HttpClient.Builder
HttpClient httpClient = HttpClient.newBuilder().build();
resetButton.setOnAction(e -> routerDAO.resetRouter(router, httpClient));


    ImageView view3 = new ImageView("file:demo/src/main/java/com/example/HCI/icons8-reset-30.png");
         resetButton.setGraphic(view3);
        Button connectButton = new Button("");
        connectButton.setOnAction(e -> routerDAO.routerConnectivity(router.getSerialnbre()));
    ImageView view4 = new ImageView("file:demo/src/main/java/com/example/HCI/icons8-wifi-30.png");
         connectButton.setGraphic(view4);
        // Buttons section
        HBox buttonsBox = new HBox(5);
        buttonsBox.getChildren().addAll(deleteButton, updateButton, rebootButton, resetButton, connectButton);
    
        // Add components to the VBox
        routerBox.getChildren().addAll(routerImage, showDetailsButton, serialNumberLabel, buttonsBox);
    
        return routerBox;
    }

    private void showCreateRouterForm() {
        Stage createRouterStage = new Stage();
        createRouterStage.initModality(Modality.APPLICATION_MODAL);
        createRouterStage.setTitle("Add Router");

        GridPane formLayout = new GridPane();
        formLayout.setVgap(10);
        formLayout.setHgap(10);
        formLayout.setPadding(new Insets(20));

        TextField brandField = new TextField();
        TextField modelField = new TextField();
        TextField serialNumberField = new TextField();
        TextField ipAddressField = new TextField();
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();

        formLayout.add(new Label("Brand:"), 0, 0);
        formLayout.add(brandField, 1, 0);
        formLayout.add(new Label("Model:"), 0, 1);
        formLayout.add(modelField, 1, 1);
        formLayout.add(new Label("Serial Number:"), 0, 2);
        formLayout.add(serialNumberField, 1, 2);
        formLayout.add(new Label("IP Address:"), 0, 3);
        formLayout.add(ipAddressField, 1, 3);
        formLayout.add(new Label("Username:"), 0, 4);
        formLayout.add(usernameField, 1, 4);
        formLayout.add(new Label("Password:"), 0, 5);
        formLayout.add(passwordField, 1, 5);

        Button createButton = new Button("Add Router");
        createButton.setOnAction(e -> {
            RouterModel newRouter = new RouterModel(
                    brandField.getText(),
                    modelField.getText(),
                    serialNumberField.getText(),
                    ipAddressField.getText(),
                    usernameField.getText(),
                    passwordField.getText());
            routerDAO.addRouter(newRouter);
            createRouterStage.close();
            // You may also update the UI to display the newly created router
        });
       

        formLayout.add(createButton, 1, 6);

        Scene formScene = new Scene(formLayout, 300, 300);
        createRouterStage.setScene(formScene);

        createRouterStage.show();
    }
   

    private void showUpdateRouterForm(RouterModel router) {
        Stage updateRouterStage = new Stage();
        updateRouterStage.initModality(Modality.APPLICATION_MODAL);
        updateRouterStage.setTitle("Update Router");

        GridPane formLayout = new GridPane();
        formLayout.setVgap(10);
        formLayout.setHgap(10);
        formLayout.setPadding(new Insets(20));

        TextField brandField = new TextField(router.getBrand());
        TextField modelField = new TextField(router.getModel());
        TextField serialNumberField = new TextField(router.getSerialnbre());
        TextField ipAddressField = new TextField(router.getIpAddress());
        TextField usernameField = new TextField(router.getUsername());
        TextField passwordField = new TextField(router.getPwd());

        formLayout.add(new Label("Brand:"), 0, 0);
        formLayout.add(brandField, 1, 0);
        formLayout.add(new Label("Model:"), 0, 1);
        formLayout.add(modelField, 1, 1);
        formLayout.add(new Label("Serial Number:"), 0, 2);
        formLayout.add(serialNumberField, 1, 2);
        formLayout.add(new Label("IP Address:"), 0, 3);
        formLayout.add(ipAddressField, 1, 3);
        formLayout.add(new Label("Username:"), 0, 4);
        formLayout.add(usernameField, 1, 4);
        formLayout.add(new Label("Password:"), 0, 5);
        formLayout.add(passwordField, 1, 5);

        Button updateButton = new Button("Update Router");
        updateButton.setOnAction(e -> {
            router.setBrand(brandField.getText());
            router.setModel(modelField.getText());
            router.setSerialnbre(serialNumberField.getText());
            router.setIpAddress(ipAddressField.getText());
            router.setUsername(usernameField.getText());
            router.setPwd(passwordField.getText());

            routerDAO.updateRouter(router);
            updateRouterStage.close();
            // You may also update the UI to reflect the changes
        });

        formLayout.add(updateButton, 1, 6);

        Scene formScene = new Scene(formLayout, 300, 300);
        updateRouterStage.setScene(formScene);

        updateRouterStage.show();
    }

    public class RouterService {

        public void showRouterDetails(RouterModel router) {
            Stage detailsStage = new Stage();
            detailsStage.initModality(Modality.APPLICATION_MODAL);
            detailsStage.setTitle("Router Details");
    
            VBox detailsLayout = new VBox(10);
            detailsLayout.setStyle("-fx-padding: 10px;");
    
            Label brandLabel = new Label("Brand: " + router.getBrand());
            Label modelLabel = new Label("Model: " + router.getModel());
            Label serialNumberLabel = new Label("Serial Number: " + router.getSerialnbre());
            Label ipAddressLabel = new Label("IP Address: " + router.getIpAddress());
            Label usernameLabel = new Label("Username: " + router.getUsername());
            Label passwordLabel = new Label("Password: " + router.getPwd());
    
            detailsLayout.getChildren().addAll(brandLabel, modelLabel, serialNumberLabel, ipAddressLabel, usernameLabel, passwordLabel);
    
            Scene detailsScene = new Scene(detailsLayout, 300, 200);
            detailsStage.setScene(detailsScene);
    
            detailsStage.show();
        }
    }

}
