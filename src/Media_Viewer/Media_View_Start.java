/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media_Viewer;

import java.io.File;
import java.net.MalformedURLException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author kamau
 */
public class Media_View_Start {
    
    Stage primarystage;
    Scene scene;
    ImageView image_view;
    
    Button move_left_center_items;
    Button move_right_center_items;
    
    Button move_left_image_list;
    Button move_right_image_list;
    
    ScrollPane image_list_items;
    
    VBox root;
    HBox center_items;
    HBox image_list;
    
    public Media_View_Start() {
        primarystage = new Stage();
        image_view = new ImageView();
        
        move_left_center_items = new Button("lc");
        move_right_center_items = new Button("rc");
        
         move_left_image_list = new Button("ll");
        move_right_image_list = new Button("rl");
        
        image_list_items = new ScrollPane();
        
        root = new VBox();
        center_items = new HBox();
        image_list = new HBox();
        
    }
    
    public void start(String path) throws MalformedURLException {
        image_view.setImage(new Image( "file:"+path,primarystage.getWidth()*0.85,primarystage.getHeight()*0.7,true,true));
        
        
        File file = new File(path);
        File parent_file = file.getParentFile();
        System.out.println("File parent name" + parent_file.getName() + " path " + parent_file.getAbsolutePath());
        
        if (parent_file.isDirectory()) {
            File[] fList = parent_file.listFiles();
//            
            for (File file_item : fList) {
                String ext = getFileExtension(file_item.getName());
                System.out.println("contents of parent file" + file_item.getAbsolutePath() + " extension " + ext);
                if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("mpeg")) {
                    
                    String the_path = "file:"+ file_item;
                    ImageView img_item = new ImageView(new Image(the_path,100,100,true,true));
                    image_list.getChildren().add(img_item);
                    
                }
            }
        }
        
        center_items.getChildren().addAll(move_left_center_items,image_view,move_right_center_items);
        image_list.getChildren().addAll(move_left_image_list,image_list_items,move_right_image_list);
        
        
        root.getChildren().addAll(center_items,image_list);
        
        //bind
        primarystage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
           
           center_items.setPrefWidth(newVal.intValue());
            
            image_list.setPrefWidth(newVal.intValue());
        });
        primarystage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            image_list.setPrefHeight(newVal.intValue() *0.35 );
     
            center_items.setPrefHeight(newVal.intValue()*0.65);
        });
        
        
        scene = new Scene(root, 870, 650);
        scene.getStylesheets().add(getClass().getResource("Media_View_Start.css").toExternalForm());
        primarystage.setScene(scene);
        
        primarystage.setResizable(true);
        primarystage.setTitle("Araizen Media Image View Tool");
        
        primarystage.setAlwaysOnTop(true);
        primarystage.showAndWait();
        
        
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
