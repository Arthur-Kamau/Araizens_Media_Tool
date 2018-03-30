/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media_Tool;

import Media_Player.video.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.Home;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author kamau
 */
public class Media_Tool {

    String home_icon = "/assets/home.png";
    private final String image_player_icon = "/assets/media plyer.jpg";

    Stage primarystage;
    Scene scene;
    Button home_nav_bar_item;
    HBox nav_bar;

    ScrollPane folder_items_root;
    VBox folder_item_list;

    VBox root_vbox;
    HBox root_hbox;
    Pane tools_container;

    List<String> validExtensions_mediaplayer;
    List<String> validExtensions_imageviewer;
    List<String> validExtensions_musicplayer;

    ScrollPane directory_item_contents_scrollpane;
    TilePane directory_item_contents_tilepane;

    int item_image_width = 40;
    int item_image_height = 40;

    Group media_player_group_pane;
    VBox media_player_controls_slider_container;
    HBox media_player_controls_container;
    HBox media_player_slider_container;

    HBox media_player_commands_show;
    Button media_player_commands_show_button;

    StackPane dropdown_container;

    StackPane media_player_view_container;

    Button media_player_play_pause_button;
    Button media_player_forwad_button;
    Button media_player_rewind_button;

    Slider media_player_video_progress;
    Slider media_player_volume;
    Label media_player_play_time;

    private Duration media_player_duration;

    MediaView media_player_view;
    MediaPlayer media_player;
    Media media;

    /**
     * camera items
     */
    VideoCapture capture;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;

    ImageView self_picture_view;
    Button camera_button;
    Button video_button;

    /**
     * transitiions no fade out bcs faout depends on fadein
     */
    FadeTransition fadein;

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

        root_vbox = new VBox();
        root_vbox.setId("root_vbox");

        root_hbox = new HBox();
        root_vbox.setId("root_hbox");

        tools_container = new Pane();
        tools_container.setId("tools_container");

        validExtensions_mediaplayer = Arrays.asList("mp4", "mkv", "oog");
        validExtensions_imageviewer = Arrays.asList("png", "jpg", "mpeg");
        validExtensions_musicplayer = Arrays.asList("mp3", "mkv");

        directory_item_contents_scrollpane = new ScrollPane();
        directory_item_contents_scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
        directory_item_contents_scrollpane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        directory_item_contents_tilepane = new TilePane();
        directory_item_contents_tilepane.setHgap(6);
        directory_item_contents_tilepane.setVgap(0.4);

        /**
         * media player
         */
        media_player_group_pane = new Group();
        media_player_controls_slider_container = new VBox();
        media_player_controls_container = new HBox();
        media_player_slider_container = new HBox();

        dropdown_container = new StackPane();
        dropdown_container.setId("dropdown_container");

        media_player_commands_show = new HBox();
        media_player_commands_show.setId("media_player_commands_show");

        media_player_commands_show_button = new Button();
        media_player_commands_show_button.setId("media_player_commands_show_button");

        media_player_play_pause_button = new Button();
        media_player_forwad_button = new Button("forwad");
        media_player_rewind_button = new Button("rewind");

        media_player_video_progress = new Slider();
        media_player_volume = new Slider();
        media_player_volume.setOrientation(Orientation.HORIZONTAL);
        media_player_play_time = new Label();

        media_player_view_container = new StackPane();
        media_player_view_container.setId("media_player_view_container");

        media_player_view = new MediaView();
        /**
         * camera
         */
        self_picture_view = new ImageView();
        camera_button = new Button();
        video_button = new Button();

    }

    public void start(String dir_path) {
        File file = new File(dir_path);
        if (file.isDirectory()) {
            File[] fList = file.listFiles();
            System.out.println(fList);
            for (File file_item : fList) {
                if (file_item.isFile()) {
                    Button container_button = new Button();
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

                    container_button.setStyle("-fx-background-insets : 40;");
                    container_button.setGraphic(file_item_ui);
                    //file
                    container_button.setOnAction((e) -> {

                        if (validExtensions_imageviewer.contains(getFileExtension(file_item.getName()).toLowerCase())) {
                            show_image_view(file_item);
                        } else if (validExtensions_mediaplayer.contains(getFileExtension(file_item.getName()).toLowerCase())) {
                            show_media_player(file_item);
                        } else if (validExtensions_musicplayer.contains(getFileExtension(file_item.getName()).toLowerCase())) {
                            show_music_player(file_item);
                        } else {  //non valid extension like docs

                        }

                    });
                    folder_item_list.getChildren().add(container_button);
                } else if (file_item.isDirectory()) {
                    Button container_button = new Button();
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

                    container_button.setStyle("-fx-background-insets : 40;");
                    container_button.setGraphic(file_item_ui);
                    //directory
                    container_button.setOnAction((e) -> {

                        show_directory_contents(file_item);

                    });

                    folder_item_list.getChildren().add(container_button);
                }

            }
        } else {
            Button container_button = new Button();
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

            container_button.setStyle("-fx-background-insets : 40;");
            container_button.setGraphic(file_item_ui);
            folder_item_list.getChildren().add(container_button);
        }

        nav_bar.setMaxHeight(50);
        nav_bar.prefWidthProperty().bind(primarystage.widthProperty());
        //set initial values

        //show home and close current dir
        home_nav_bar_item.setOnAction((e) -> {
            primarystage.close();
//            Home hm = new Home();
//            hm.start();
        });
        nav_bar.getChildren().add(home_nav_bar_item);

        folder_items_root.setContent(folder_item_list);

        folder_items_root.setContent(folder_item_list);

        root_hbox.getChildren().addAll(folder_items_root, tools_container);
        root_vbox.getChildren().addAll(nav_bar, root_hbox);

        scene = new Scene(root_vbox, 870, 650);
        scene.getStylesheets().add(getClass().getResource("Media_Tool.css").toExternalForm());

        primarystage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                System.out.println("----------<<<<<<< on close event media tool >>>>>>>>>>>>---------");
                if (media_player != null) {
                    boolean playing_ = media_player.getStatus().equals(MediaPlayer.Status.PLAYING);
                    if (playing_ == true) {
                        media_player.stop();
                        media_player.dispose();
                    }
                }
                primarystage.close();
            }

        });
        primarystage.setScene(scene);
        primarystage.setMaximized(true);
        primarystage.setResizable(true);
        primarystage.setTitle("Araizen Media Tool");
//        primarystage.initModality(Modality.WINDOW_MODAL);

        // primarystage.setAlwaysOnTop(true);
//        primarystage.getIcons().add(new Image(image_player_icon));
        primarystage.show();

        System.out.println("2 --width" + primarystage.getWidth());

        System.out.println("after sceneW " + scene.getWidth());
        System.out.println("after sceneH " + scene.getHeight());
        System.out.println("after stageW " + primarystage.getWidth());
        System.out.println("after stageH " + primarystage.getHeight());

        System.out.println("width" + primarystage.getWidth() + "height" + primarystage.getHeight());
//        folder_item_list.setPrefWidth(150);
//        tools_container.setMaxWidth(primarystage.getWidth()-150);//primarystage.getWidth()*0.95 );//setPrefWidth(50);//primarystage.getWidth() * 0.5);

        folder_item_list.setPrefHeight(primarystage.getHeight());

        folder_items_root.setPrefWidth(scene.getWidth() * 0.167);
        tools_container.setPrefWidth(scene.getWidth() * 0.843);

        //bind
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            folder_item_list.setPrefWidth(newVal.intValue() * 0.157);
            folder_items_root.setPrefWidth(newVal.intValue() * 0.167);

            directory_item_contents_tilepane.setPrefWidth(newVal.intValue() * 0.843);
            directory_item_contents_scrollpane.setPrefWidth(newVal.intValue() * 0.843);

            tools_container.setPrefWidth(newVal.intValue() * 0.833);
        });

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

    private void show_directory_contents(File file) {

        tools_container.getChildren().clear();
        directory_item_contents_tilepane.getChildren().clear();
        directory_item_contents_scrollpane.setContent(null);

        if (media_player != null) {
            boolean playing_ = media_player.getStatus().equals(MediaPlayer.Status.PLAYING);
            if (playing_ == true) {
                media_player.stop();
                media_player.dispose();
            }
        }

        File[] fList = file.listFiles();

        for (File file_item : fList) {
            if (file_item.isFile()) {

                VBox item_vbox = new VBox();
                ImageView item_imageview = new ImageView();
                String rightResult = insertCharacterForEveryNDistanceFromRight(12, file_item.getName(), "\n");
                Label item_label = new Label(rightResult);

                Button Item_button = new Button();
                Item_button.setStyle("-fx-background-insets : 60;");
                Item_button.setPadding(new Insets(5, 5, 5, 5));
                Image item_image = new Image("file:" + file_item.getAbsolutePath(), item_image_height, item_image_width, true, true);

//                    if(getFileExtension(file_item.getName()).equalsIgnoreCase("mp3")){
//                        item_image = new Image("file:");
//                    }else if(getFileExtension(file_item.getName()).equalsIgnoreCase("mp4")){
//                         item_image = new Image();
//                    }else if(getFileExtension(file_item.getName()).equalsIgnoreCase("png")){
//                         item_image = new Image();
//                    }
                item_imageview.setImage(item_image);

                item_vbox.getChildren().addAll(item_imageview, item_label);
                Item_button.setGraphic(item_vbox);

                directory_item_contents_tilepane.getChildren().add(Item_button);

            } else if (file_item.isDirectory()) {

            }

        }
        directory_item_contents_tilepane.setPrefSize(tools_container.getWidth(), tools_container.getHeight());
        directory_item_contents_tilepane.setStyle(" -fx-background-color:#ffffff;");

        directory_item_contents_scrollpane.setPrefSize(tools_container.getWidth(), tools_container.getHeight());
        directory_item_contents_scrollpane.setStyle(" -fx-background-color:red;");
        directory_item_contents_scrollpane.setContent(directory_item_contents_tilepane);

        tools_container.getChildren().add(directory_item_contents_tilepane);

    }

    private void show_media_player(File file_item) {
        if (media_player != null) {
            boolean playing_ = media_player.getStatus().equals(MediaPlayer.Status.PLAYING);
            if (playing_ == true) {
                media_player.stop();
                media_player.dispose();
            }
        }
        tools_container.getChildren().clear();

        media_player_commands_show.getChildren().clear();
        //controls
        media_player_controls_container.getChildren().clear();
        //slider and time
        media_player_slider_container.getChildren().clear();
        //controls and slider
        media_player_controls_slider_container.getChildren().clear();
        //view
        media_player_view_container.getChildren().clear();
        //view_container and controls
        media_player_group_pane.getChildren().clear();

        media_player_controls_slider_container.getTransforms().clear();

        /**
         * fadin and out for information media_player_commands_show_button
         */
        fadein = new FadeTransition(Duration.seconds(2), media_player_commands_show_button);
        fadein.setFromValue(0);
        fadein.setToValue(1);
        fadein.setCycleCount(1);

        fadein.play();

        FadeTransition fadeout = new FadeTransition(Duration.seconds(2), media_player_commands_show_button);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);
        fadeout.setCycleCount(1);

        fadein.setOnFinished((e) -> {

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                @Override
                public void run() {
                    fadeout.play();

                }
            }, 5550
            );
        });

        File file = new File(file_item.getAbsolutePath());
        String path = file.toURI().toASCIIString();

        media = new Media(path);
        media_player = new MediaPlayer(media);

        media_player_view.setMediaPlayer(media_player);
        media_player_view.setPreserveRatio(false);
        media_player.play();

        media_player_play_pause_button.setText("pause");

        //show commads button cick
        media_player_commands_show_button.setText("press tab for menu");
        media_player_commands_show.prefWidth(120);
        media_player_commands_show_button.prefWidth(120);
        media_player_commands_show_button.setFocusTraversable(false);
        media_player_commands_show_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("hide event button");
            }
        });

        /**
         * media player button events
         */
        media_player_play_pause_button.setOnAction((e) -> {
            boolean playing_ = media_player.getStatus().equals(MediaPlayer.Status.PLAYING);
            if (playing_ == true) {
                media_player.pause();
                media_player_play_pause_button.setText("play");
            } else {
                media_player.play();
                media_player_play_pause_button.setText("pause");
            }
        });
        media_player_forwad_button.setOnAction((e) -> {
//            media_player.pause();
            media_player.seek(media_player.getCurrentTime().multiply(1.5));
            
            System.out.println("old val" + media_player.getCurrentTime() + "new val" + media_player.getCurrentTime().multiply(1.5));
//            media_player.play();
            fadein.play();
            media_player_commands_show_button.setText("forwad");

        });
        media_player_rewind_button.setOnAction((e) -> {
            System.out.println("control left --speed rewind");
            
            media_player.seek(media_player.getCurrentTime().divide(1.5));
            
            fadein.play();
            media_player_commands_show_button.setText("rewind");
        });

        media_player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                media_player_video_progress.setValue(newValue.toSeconds());
//                updateValues();
                Duration currentTime = media_player.getCurrentTime();
                media_player_play_time.setText(formatTime(currentTime, media_player_duration));
                media_player_video_progress.setDisable(media_player_duration.isUnknown());
            }

        });
        media_player_video_progress.setOnMouseClicked((Event) -> {

            media_player.seek(Duration.seconds(media_player_video_progress.getValue()));
            Duration currentTime = media_player.getCurrentTime();
            media_player_play_time.setText(formatTime(currentTime, media_player_duration));
            media_player_video_progress.setDisable(media_player_duration.isUnknown());
        });
        media_player.setOnReady(new Runnable() {
            @Override
            public void run() {
                File f = new File(media.getSource());
                System.out.println(f.getName());
//                primaryStage.setTitle("player::    " + f.getName());
                media_player_duration = media_player.getMedia().getDuration();
                //updateValues();

                media_player_video_progress.setMin(0.0);
                media_player_video_progress.setValue(0.0);
//                media_player_video_progress.setMax(media_player.getTotalDuration().toSeconds());

                media_player_video_progress.maxProperty().bind(Bindings.createDoubleBinding(
                        () -> media_player.getTotalDuration().toSeconds(),
                        media_player.totalDurationProperty()));
            }
        }
        );

        media_player.setOnPlaying(new Runnable() {
            @Override
            public void run() {
//                updateValues();

                System.out.println("update values - media playing");
            }
        });

        media_player_view.fitWidthProperty().bind(tools_container.widthProperty());
        media_player_view.fitHeightProperty().bind(tools_container.heightProperty());

        media_player_commands_show.setTranslateX((tools_container.getWidth() - 90));

        Translate translate_controls = new Translate();

        // Set arguments for translation
        translate_controls.setX(0);
        translate_controls.setY(tools_container.getHeight() * 0.91);// -200);
        translate_controls.setZ(0);

        media_player_controls_slider_container.getTransforms().add(translate_controls);

        media_player_play_time.setMaxWidth(70);
        media_player_controls_container.setPrefWidth(tools_container.getWidth());
        media_player_video_progress.setPrefWidth(tools_container.getWidth() - 80);
        tools_container.widthProperty().addListener((obs, oldVal, newVal) -> {
            media_player_commands_show.setTranslateX((newVal.doubleValue() - 90));
            media_player_controls_container.setPrefWidth(newVal.doubleValue());
            media_player_video_progress.setPrefWidth(newVal.doubleValue() - 75);
        });
        tools_container.heightProperty().addListener((obs, oldVal, newVal) -> {
            media_player_controls_slider_container.getTransforms().clear();
            Translate translate_controls_height_changed = new Translate();
//
//        // Set arguments for translation
            translate_controls_height_changed.setX(0);
            translate_controls_height_changed.setY(newVal.doubleValue() * 0.91);// -200);
            translate_controls_height_changed.setZ(0);

            media_player_controls_slider_container.getTransforms().add(translate_controls_height_changed);
        });
        //commands being done by player top right
        media_player_commands_show.getChildren().add(media_player_commands_show_button);
        //controls
        media_player_controls_container.getChildren().addAll(media_player_rewind_button, media_player_play_pause_button, media_player_forwad_button, media_player_volume);
        //slider and time
        media_player_slider_container.getChildren().addAll(media_player_video_progress, media_player_play_time);
        //controls and slider
        media_player_controls_slider_container.getChildren().addAll(media_player_controls_container, media_player_slider_container);

        media_player_group_pane.getChildren().addAll(media_player_view, media_player_controls_slider_container, media_player_commands_show);

        tools_container.getChildren().add(media_player_group_pane);
    }

    private void show_image_view(File file) {

        tools_container.getChildren().clear();

    }

    private void show_music_player(File file) {

    }

    public String insertCharacterForEveryNDistanceFromRight(int distance, String original, String c) {
        StringBuilder sb = new StringBuilder();
        char[] charArrayOfOriginal = original.toCharArray();
        for (int ch = charArrayOfOriginal.length; ch > 0; ch--) {
            if (ch % distance == 0 && ch != charArrayOfOriginal.length) {
                sb.append(c).append(charArrayOfOriginal[charArrayOfOriginal.length - ch]);
            } else {
                sb.append(charArrayOfOriginal[charArrayOfOriginal.length - ch]);
            }
        }
        return sb.toString();
    }

    private static String formatTime(Duration elapsed, Duration duration) {

        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public void runAnotherApp(Class<? extends Application> anotherAppClass) throws Exception {
        Application app2 = anotherAppClass.newInstance();
        Stage anotherStage = new Stage();
        app2.start(anotherStage);
    }

    public static List<String> usb_devices_letters() throws IOException {
        List<String> usb_drive_letter = new ArrayList<>();

        //String command = "powershell.exe  your command";
        String command = "powershell.exe   'C:\\Users\\kamau\\Documents\\NetBeansProjects\\media player code\\src\\assets\\Powershell\\externaldrives.ps1' ";
//                + "gwmi win32_diskdrive | ?{$_.interfacetype -eq \"USB\"} | %{gwmi -Query \"ASSOCIATORS OF {Win32_DiskDrive.DeviceID=`\"$($_.DeviceID.replace('\\','\\'))`\"} WHERE AssocClass = Win32_DiskDriveToDiskPartition\"} |  %{gwmi -Query \"ASSOCIATORS OF {Win32_DiskPartition.DeviceID=`\"$($_.DeviceID)`\"} WHERE AssocClass = Win32_LogicalDiskToPartition\"} | %{$_.deviceid}";
        // Executing the command
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        // Getting the results
        powerShellProcess.getOutputStream().close();
        String line;
        System.out.println("Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(
                powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }
        stdout.close();
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(
                powerShellProcess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            System.out.println(line);
        }
        stderr.close();
        System.out.println("Done");

        String lines_items[] = line.split("\\r?\\n");

        for (String item : lines_items) {
            usb_drive_letter.add(item);
        }
        return usb_drive_letter;
    }

    /**
     * open cv stuf
     */
    protected void startCamera() {
        if (!this.cameraActive) {
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = Utils.mat2Image(frame);
                        updateImageView(self_picture_view, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                self_picture_view.setImage(new Image(image_player_icon, tools_container.getWidth() / 3, tools_container.getHeight() / 3, true, true));
                // update the button content
                camera_button.setText("Stop Camera");
            } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            camera_button.setText("Start Camera");

            // stop the timer
            this.stopAcquisition();
        }
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Mat} to show
     */
    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX Main thread
     *
     * @param view the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        this.stopAcquisition();
    }

}
