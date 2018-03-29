/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author kamau
 */
public class Home {

    Stage primarystage;
    Scene scene;

    BorderPane container;
    VBox home;

    Button home_item_open_playlist;
    Button home_item_drag_items_here;
    Button home_item_continue_playlist;

    Button sidemenu_button;
    ImageView sidemenu_imageview;
    VBox sidemenu_pane;

    HBox sidemenu_item_video;
    HBox sidemenu_item_music;
    HBox sidemenu_item_image;
    HBox sidemenu_item_signin;

    ImageView sidemenu_item_video_imageview;
    ImageView sidemenu_item_music_imageview;
    ImageView sidemenu_item_image_imageview;
    ImageView sidemenu_item_signin_imageview;

    Button sidemenu_item_video_button;
    Button sidemenu_item_music_button;
    Button sidemenu_item_image_button;
    Button sidemenu_item_signin_button;

    Label menu_label;

    HBox menu;
    private final String image_player_icon = "/assets/media plyer.jpg";
    private final String sidemenu_icon = "/assets/menu.png";

    private Boolean is_sidemenuactive = true;

    public Home() {
        primarystage = new Stage();
        //scene = new Scene();

        container = new BorderPane();
        container.setId("container");

        home = new VBox();
        home.setId("home");

        menu = new HBox();
        menu.setId("menu");

        menu_label = new Label();
        menu_label.setId("menu_label_label");

        home_item_drag_items_here = new Button("Drag Folder /Media files here \n     (.jpg,.png,.mp4,.mp3)   ");
        home_item_drag_items_here.setId("home_item_drag_items_here");

        home_item_open_playlist = new Button("Open Playlist");
        home_item_open_playlist.setId("home_item_open_playlist");

        home_item_continue_playlist = new Button("continue rescent");
        home_item_continue_playlist.setId("home_item_continue_playlist");

        sidemenu_button = new Button();
        sidemenu_button.setId("sidemenu_button");

        sidemenu_imageview = new ImageView();

        sidemenu_pane = new VBox();

        sidemenu_pane.setId("sidemenu_pane");
        sidemenu_item_video = new HBox();
        sidemenu_item_music = new HBox();
        sidemenu_item_image = new HBox();
        sidemenu_item_signin = new HBox();

        sidemenu_item_video_imageview = new ImageView();
        sidemenu_item_music_imageview = new ImageView();
        sidemenu_item_image_imageview = new ImageView();
        sidemenu_item_signin_imageview = new ImageView();

        sidemenu_item_video_button = new Button("Video Player");
        sidemenu_item_video_button.setOpacity(0);
        sidemenu_item_video_button.setId("sidemenu_item_video_button");

        sidemenu_item_music_button = new Button("Music Player");
        sidemenu_item_music_button.setOpacity(0);
        sidemenu_item_music_button.setId("sidemenu_item_music_button");

        sidemenu_item_image_button = new Button("Image Viewer");
        sidemenu_item_image_button.setOpacity(0);
        sidemenu_item_image_button.setId("sidemenu_item_image_button");

        sidemenu_item_signin_button = new Button("Sign In");
        sidemenu_item_signin_button.setOpacity(0);
        sidemenu_item_signin_button.setId("sidemenu_item_signin_button");

        sidemenu_item_video.getChildren().addAll(sidemenu_item_video_imageview, sidemenu_item_video_button);
        sidemenu_item_music.getChildren().addAll(sidemenu_item_music_imageview, sidemenu_item_music_button);
        sidemenu_item_image.getChildren().addAll(sidemenu_item_image_imageview, sidemenu_item_image_button);

        sidemenu_pane.setPrefWidth(-100);
        sidemenu_pane.getChildren().addAll(sidemenu_item_video, sidemenu_item_music, sidemenu_item_image, sidemenu_item_signin);

    }

    public void start() {

        //menu_label label
        menu_label.setText("Home");

        //imageview
        sidemenu_imageview.setImage(new Image(sidemenu_icon, 30, 30, true, true));
        //button

        sidemenu_button.setGraphic(sidemenu_imageview);

        //add contents to menu
        menu.getChildren().addAll(sidemenu_button, menu_label);

        // container.getChildren().addAll(sidemenu_pane,menu, home);
        container.setLeft(sidemenu_pane);
        container.setTop(menu);
        container.setCenter(home);

        home_item_open_playlist.setPrefWidth(250);
        home_item_drag_items_here.setPrefWidth(250);
        home_item_continue_playlist.setPrefWidth(250);

        home.setAlignment(Pos.CENTER);
        home.getChildren().addAll(home_item_open_playlist, home_item_drag_items_here, home_item_continue_playlist);

        scene = new Scene(container, 500, 600);
        scene.getStylesheets().add(getClass().getResource("Home.css").toExternalForm());

        /**
         * binding height
         */
        //max
        menu.setMaxHeight(50);
        menu_label.setMaxSize(100, 35);
        sidemenu_pane.setMaxWidth(100);
        container.prefHeightProperty().bind(primarystage.heightProperty());
        sidemenu_pane.prefHeightProperty().bind(primarystage.heightProperty());
        container.prefWidthProperty().bind(primarystage.widthProperty());
        primarystage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            menu.setPrefWidth(newVal.intValue());
            home.setPrefWidth(newVal.intValue() - 300);
        });
        primarystage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
//             menu.setPrefHeight(newVal.intValue() * 0.45);
            home.setPrefHeight(newVal.intValue() * 0.65);
        });

        /**
         * button action
         *
         */
        sidemenu_button.setOnMouseEntered(evt -> {
            System.out.println("menu entered");

        });
        sidemenu_button.setOnMouseExited(evt -> {
            System.out.println("mouse exit");
        });
        sidemenu_button.setOnAction((e) -> {
            System.out.println("menu click");

            /**
             * side menu transalation
             */
            if (is_sidemenuactive) { //true
                is_sidemenuactive = false;
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(sidemenu_pane.prefWidthProperty(), 300)
                        ),
                        new KeyFrame(Duration.millis(300),
                                new KeyValue(sidemenu_pane.prefWidthProperty(), 0)//primarystage.getWidth()*0.35)
                        )
                );
                timeline.play();

                sidemenu_item_video_button.setOpacity(0);
                sidemenu_item_music_button.setOpacity(0);
                sidemenu_item_image_button.setOpacity(0);
                sidemenu_item_signin_button.setOpacity(0);
            } else { //false
                is_sidemenuactive = true;
                Timeline timeout = new Timeline();
                timeout.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(sidemenu_pane.prefWidthProperty(), 0)
                        ),
                        new KeyFrame(Duration.millis(300),
                                new KeyValue(sidemenu_pane.prefWidthProperty(), 300)//primarystage.getWidth()*0.35)
                        )
                );
                timeout.play();

                sidemenu_item_video_button.setOpacity(1);
                sidemenu_item_music_button.setOpacity(1);
                sidemenu_item_image_button.setOpacity(1);
                sidemenu_item_signin_button.setOpacity(1);
            }

        });

        /**
         * drag and drop evnts
         *
         */
        scene.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != scene
                        && event.getDragboard().hasFiles()) {

                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        scene.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getGestureSource() != container && event.getDragboard().hasFiles()) {
                    // Print files
                    event.getDragboard().getFiles().forEach(
                            file -> {
                                System.out.println(file.getAbsolutePath());

                            });
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        primarystage.setScene(scene);

        primarystage.setResizable(false);
        primarystage.setTitle("Araizen Media Tool");
        //primarystage.initModality(Modality.APPLICATION_MODAL);

        primarystage.setAlwaysOnTop(true);
//        primarystage.getIcons().add(new Image(image_player_icon));
        primarystage.showAndWait();
    }
    
    

}
