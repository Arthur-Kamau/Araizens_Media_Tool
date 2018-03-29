/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media_Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author kamau
 */
public class Media_Tool {

    String home_icon = "/assets/home.png";
    Stage primarystage;
    Scene scene;
    Button home_nav_bar_item;
    HBox nav_bar;

    ScrollPane folder_items_root;
    VBox folder_item_list;

    BorderPane root;
    AnchorPane tools_container;

    public Media_Tool() {
        primarystage = new Stage();

        nav_bar = new HBox();
        nav_bar.setId("nav_bar");

        home_nav_bar_item = new Button();
        home_nav_bar_item.setId("home_nav_bar_item");
        home_nav_bar_item.setGraphic(new ImageView(new Image(home_icon, 30, 30, true, true)));

        folder_items_root = new ScrollPane();
        folder_items_root.setId("folder_items_root");
// Always show vertical scroll bar
         folder_items_root.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        // Horizontal scroll bar is only displayed when needed
         folder_items_root.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        folder_item_list = new VBox();
        folder_item_list.setPadding(new Insets(10, 5, 0, 6));
        folder_item_list.setSpacing(5);
        folder_item_list.setId("folder_item_list");
        

        root = new BorderPane();
        root.setId("root");

        tools_container = new AnchorPane();
        tools_container.setId("tools_container");
    }

    public void start(String dir_path) {
        File file = new File(dir_path);
        if (file.isDirectory()) {
            File[] fList = file.listFiles();
            System.out.println(fList);
            for (File file_item : fList) {
                if (file_item.isFile()) {
                    Label name = new Label(file_item.getName());
                    Label type = new Label(getFileExtension(file_item.getName()));

                    Map<String, Double> file_size = func_get_file_size(file);
                    Label size = new Label(file_size.get("megabytes").toString());

                    HBox file_details_ui = new HBox();
                    Region region1 = new Region();
                    HBox.setHgrow(region1, Priority.ALWAYS);
                    file_details_ui.getChildren().addAll(type, region1, size);

                    VBox file_item_ui = new VBox();
                    file_item_ui.getChildren().addAll(name, file_details_ui);
                    file_item_ui.setPadding(new Insets(10, 0, 0, 0));
                    file_item_ui.setSpacing(5);

                    folder_item_list.getChildren().add(file_item_ui);
                } else if (file_item.isDirectory()) {
                    Label name = new Label(file_item.getName());
                    Label type = new Label("Directory");

                    Map<String, Double> file_size = func_get_file_size(file);
                    Label size = new Label(file_size.get("megabytes").toString());

                    HBox file_details_ui = new HBox();
                    Region region1 = new Region();
                    HBox.setHgrow(region1, Priority.ALWAYS);
                    file_details_ui.getChildren().addAll(type, region1, size);

                    VBox file_item_ui = new VBox();

                    file_item_ui.getChildren().addAll(name, file_details_ui);

                    file_item_ui.setPadding(new Insets(10, 0, 0, 5));
                    file_item_ui.setSpacing(5);

                    folder_item_list.getChildren().add(file_item_ui);
                }

            }
        } else {
            Label name = new Label(file.getName());
            Label type = new Label(getFileExtension(file.getName()));

            Map<String, Double> file_size = func_get_file_size(file);
            Label size = new Label(file_size.get("megabytes").toString());

            HBox file_details_ui = new HBox();
            Region region1 = new Region();
            HBox.setHgrow(region1, Priority.ALWAYS);
            file_details_ui.getChildren().addAll(type, region1, size);

            VBox file_item_ui = new VBox();
            file_item_ui.getChildren().addAll(name, file_details_ui);

            file_item_ui.setPadding(new Insets(10, 0, 0, 0));
            file_item_ui.setSpacing(5);

            folder_item_list.getChildren().add(file_item_ui);
        }

        nav_bar.setMaxHeight(50);
        nav_bar.prefWidthProperty().bind(primarystage.widthProperty());
        //set initial values

           folder_item_list.setPrefWidth(primarystage.getWidth() * 0.167);
            tools_container.setPrefWidth(primarystage.getWidth() * 0.843);
     
             folder_item_list.setPrefHeight(primarystage.getHeight() );
            tools_container.setPrefHeight(primarystage.getHeight());
            
        //bind
        primarystage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            folder_item_list.setPrefWidth(newVal.intValue() * 0.157);
            folder_items_root.setPrefWidth(newVal.intValue() * 0.167);
            
            tools_container.setPrefWidth(newVal.intValue() * 0.843);
        });
        primarystage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            folder_item_list.setPrefHeight(newVal.intValue() );
     
            tools_container.setPrefHeight(newVal.intValue());
        });

        nav_bar.getChildren().add(home_nav_bar_item);

        folder_items_root.setContent(folder_item_list);
        
        folder_items_root.setContent(folder_item_list);
        
        
        root.setTop(nav_bar);
        root.setLeft(folder_items_root);
        root.setCenter(tools_container);

        scene = new Scene(root, 870, 650);
        scene.getStylesheets().add(getClass().getResource("Media_Tool.css").toExternalForm());
        primarystage.setScene(scene);
        primarystage.setMaximized(true);
        primarystage.setResizable(true);
        primarystage.setTitle("Araizen Media Tool");
        //primarystage.initModality(Modality.APPLICATION_MODAL);

        primarystage.setAlwaysOnTop(true);
//        primarystage.getIcons().add(new Image(image_player_icon));
        primarystage.showAndWait();

    }

    private Map func_get_file_size(File file) {
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);
        double terabytes = (gigabytes / 1024);
        double petabytes = (terabytes / 1024);
        double exabytes = (petabytes / 1024);
        double zettabytes = (exabytes / 1024);
        double yottabytes = (zettabytes / 1024);

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("bytes", bytes);
        map.put("megabytes", megabytes);
        map.put("gigabytes", gigabytes);
        map.put("terabytes", terabytes);
        map.put("petabytes", petabytes);
        map.put("exabytes", exabytes);
        map.put("zettabytes", zettabytes);
        map.put("yottabytes", yottabytes);

        return map;
    }

    private String getFileExtension(String files) {
        File file = new File(files);
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}
