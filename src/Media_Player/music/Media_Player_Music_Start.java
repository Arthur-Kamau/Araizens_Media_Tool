/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media_Player.music;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

/**
 *
 * @author kamau
 */
public class Media_Player_Music_Start {

    private MediaPlayer mediaPlayer;

    Stage primarystage;
    Scene scene;
    Media media;
    
    List<String> playlist_items;
    List<String> play_list_items;
    ListView<Object> play_list_items_view;

    VBox contaiiner_vbox;
    HBox nav_bar;
    Label nave_Label;
    Button nav_button;

    VBox content_container;
    VBox contents;
    HBox controls_menu_tabpane;
    ToggleGroup toggleGroup;
    ToggleButton all_songs_tab;
    ToggleButton playlist_tab;
    ToggleButton current_play_tab;

    StackPane stackpane;

    VBox controls_vbox;
    HBox controls_items_hbox;
    HBox controls_slider;

    Button play_button;
    Button jump_back_button;
    Button jump_forwad_button;
    Slider volumeSlider;
    Slider playtimeSlider;
    Label playTime;

     private final String image_player_icon = "/assets/media plyer.jpg";
    String menu_image = "/assets/menu.png";
    String play_imag = "/assets/ic_play_arrow_2x.png";
    String jump_forwad_image = "/assets/ic_fast_forward_2x.png";
    String jump_back_image = "/assets/ic_fast_rewind_2x.png";
    final double MAX_FONT_SIZE = 20.0;

    public Media_Player_Music_Start() {
        primarystage = new Stage();

        playlist_items = new ArrayList<>();
        play_list_items = new ArrayList<>();
        play_list_items_view = new ListView<Object>();

        contaiiner_vbox = new VBox();
        contaiiner_vbox.setId("contaiiner_vbox");
        nav_bar = new HBox();
        nave_Label = new Label();
        nav_button = new Button();
        nav_button.setId("nav_button");

        toggleGroup = new ToggleGroup();

        content_container = new VBox();
        content_container.setId("content_container");
        contents = new VBox();
        controls_menu_tabpane = new HBox();
        all_songs_tab = new ToggleButton();
        all_songs_tab.setText("Songs");
        all_songs_tab.setId("all_songs_tab");

        playlist_tab = new ToggleButton();
        playlist_tab.setText("Playlist");
        playlist_tab.setId("playlist_tab");

        current_play_tab = new ToggleButton();
        current_play_tab.setText("Play");
        current_play_tab.setId("current_play_tab");

        stackpane = new StackPane();
        stackpane.setId("stackpane");

        controls_vbox = new VBox();
        controls_items_hbox = new HBox();
        controls_slider = new HBox();

        play_button = new Button();
        play_button.setId("play_button");

        jump_back_button = new Button();
        jump_back_button.setId("jump_back_button");

        jump_forwad_button = new Button();
        jump_forwad_button.setId("jump_forwad_button");

        volumeSlider = new Slider();
        volumeSlider.setId("volumeSlider");

        playtimeSlider = new Slider();
        playtimeSlider.setId("playtimeSlider");

        playTime = new Label();

    }

    public void start(String folder_path) {

        nav_bar.setPrefHeight(40);
        nav_bar.setMaxHeight(40);
        controls_vbox.setPrefHeight(50);
        controls_vbox.setMaxHeight(50);
        controls_menu_tabpane.setPrefHeight(primarystage.getHeight() - 50);
        controls_menu_tabpane.setPrefWidth(primarystage.getWidth());

        all_songs_tab.setSelected(true);
        all_songs_tab.setOnAction((e) -> {
            System.out.println("all_songs_tab");

        });
        playlist_tab.setOnAction((e) -> {
            System.out.println("playlist_tab");

        });
        current_play_tab.setOnAction((e) -> {
            System.out.println("current_play_tab");

        });

        nav_button.setGraphic(new ImageView(new Image(menu_image, 25, 25, true, true)));
        nave_Label.setText("Music");
        nave_Label.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        nave_Label.setFont(javafx.scene.text.Font.font("Courier New", MAX_FONT_SIZE));

        primarystage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            controls_menu_tabpane.setPrefWidth(newVal.doubleValue());
            controls_vbox.setPrefWidth(newVal.doubleValue());
        });
        //translate controls pane 
        Translate translate_controls = new Translate();
        // Set arguments for translation
        translate_controls.setX(0);
        translate_controls.setY(500);//*0.8);// -200);
        translate_controls.setZ(0);

        controls_vbox.getTransforms().add(translate_controls);

        play_button.setGraphic(new ImageView(new Image(play_imag, 30, 30, true, true)));
        jump_back_button.setGraphic(new ImageView(new Image(jump_back_image, 30, 30, true, true)));
        jump_forwad_button.setGraphic(new ImageView(new Image(jump_forwad_image, 30, 30, true, true)));

        nav_bar.getChildren().addAll(nav_button, nave_Label);
        controls_items_hbox.getChildren().addAll(volumeSlider, jump_back_button, play_button, jump_forwad_button);
        controls_slider.getChildren().addAll(playtimeSlider, playTime);
        controls_vbox.getChildren().addAll(controls_items_hbox, controls_slider);

        all_songs_tab.setToggleGroup(toggleGroup);
        playlist_tab.setToggleGroup(toggleGroup);
        current_play_tab.setToggleGroup(toggleGroup);
        controls_menu_tabpane.getChildren().addAll(all_songs_tab, playlist_tab, current_play_tab);

        content_container.getChildren().addAll(controls_menu_tabpane, contents);

        stackpane.getChildren().addAll(content_container, controls_vbox);
        contaiiner_vbox.getChildren().addAll(nav_bar, stackpane);

        scene = new Scene(contaiiner_vbox, 500, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primarystage.setScene(scene);

        primarystage.setResizable(false);
//        primarystage.setTitle("Araizen Media Tool");
        primarystage.initStyle(StageStyle.UNIFIED);
        primarystage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                System.out.println("----------<<<<<<< on close event home >>>>>>>>>>>>---------");

                System.exit(0);

            }

        });
        primarystage.show();
    }

}
