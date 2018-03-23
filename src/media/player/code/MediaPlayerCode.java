/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media.player.code;

import javafx.application.Platform;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author kamau
 */
public class MediaPlayerCode extends Application {

    private static Stage stage;
    private static Scene scene;
    private static MediaPlayer mp;
    private static MediaView mediaView;
    private static final boolean repeat = false;
    private static final boolean stopRequested = false;
    private static boolean atEndOfMedia = false;
    private static Duration duration;
    private static Slider timeSlider;
    private static Label playTime;
    private static Label totalTime;
    private static Slider volumeSlider;
    private static HBox mediaBar;
    private static EventHandler<MouseEvent> mouseHandler;

    private static final String image_player_icon = "/assets/Wecrypt_mediaplayer_icon.jpg";
    private static final String image_full_screen = "/assets/pictures/mediaplayermaximize.jpg";
    private static final String image_play = "/assets/pictures/mediaplayer/ic_play_arrow_2x.png";
    private static final String image_pause = "/assets/pictures/mediaplayer/ic_pause_2x.png";
    private static final String image_stop = "/assets/pictures/mediaplayer/ic_stop_2x.png";
    private static final String image_jump_forwad = "/assets/pictures/mediaplayer/ic_skip_next_2x.png";
    private static final String image_jump_back = "/assets/pictures/mediaplayer/ic_skip_previous_2x.png";
    private static final String image_increase_volume = "/assets/pictures/mediaplayer/ic_volume_up_2x.png";
    private static final String image_decrease_volume = "/assets/pictures/mediaplayer/ic_volume_down_2x.png";
    private static final String image_menu = "/assets/pictures/mediaplayer/ic_menu_2x.png";
    private static ImageView play_or_pause;
    private static ImageView full_screen;
    private static ImageView stop;
    private static ImageView jump_fwd;
    private static ImageView jump_back;
    private static ImageView add_volume;
    private static ImageView reduce_volume;

    private static Button full_screen_Button;
    private static Button play_or_pause_Button;
    private static Button stop_Button;
    private static Button jump_fwd_Button;
    private static Button jump_back_Button;
    private static Button add_volume_Button;
    private static Button reduce_volume_Button;

    private static Region region1;
    private static Region region2;
    private static Region region3;
    private static Region region4;
    private static Region region5;

    public static final String APPLICATION_ICON = "/assets/1.jpg";

    //list of all files dropped
    private static List<File> files = new LinkedList<>();

    private static String url = null;
    private static int index_of_current_playing;

    List<String> validExtensions = Arrays.asList("mp4", "mkv", "oog");

    List<String> play_list_items = new ArrayList<>();
    ListView<Object> play_list_items_view = new ListView<>();
    FadeTransition fadein;
Slider video_progress;
    @Override
    public void start(Stage primaryStage) {

        //top items
        HBox instructions_menu = new HBox();
        MediaView view = new MediaView();
        HBox controls_menu = new HBox();

        //set up details for the top items
        instructions_menu.setMaxHeight(40);
        instructions_menu.setMaxWidth(100);
        controls_menu.setMaxHeight(40);

//        //background color
        instructions_menu.setStyle("-fx-background-color: #51329e; ");//"-fx-background-color: #bfc2c7;");
        view.setStyle("-fx-background-color: #7ca0dd;");
        controls_menu.setStyle("-fx-background-color: #d82424;");

//set play
        String media_path = "C:\\\\Users\\\\kamau\\\\Videos\\\\4K Video Downloader\\\\fun\\\\Most Epic Game Trailers in 4k Part 7.mp4";
        File file = new File(media_path);
        String path = file.toURI().toASCIIString();

        Media media = new Media(path);
        mp = new MediaPlayer(media);

        mp = new MediaPlayer(media);
        view = new MediaView(mp);
        mp.play();

        //items on control menu
        Button info_button = new Button();
        info_button.setText("press tab for menu");
        info_button.setStyle("-fx-background-insets : 40");
        info_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("hide event");
            }
        });

        playTime = new Label();
        video_progress = new Slider();

        mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                video_progress.setValue(newValue.toSeconds());
            }

        });

        mp.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                updateValues();
                System.out.println("update valuew ...");
            }
        });

        mp.currentTimeProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                updateValues();

            }
        });
        mp.setOnReady(new Runnable() {
            @Override
            public void run() {

                primaryStage.setTitle("player::    " + "name");
                duration = mp.getMedia().getDuration();
                updateValues();

                video_progress.setMin(0.0);
                video_progress.setValue(0.0);
                video_progress.setMax(mp.getTotalDuration().toSeconds());

            }
        });

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                System.out.println("media end");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (files == null || files.isEmpty()) {
                    mp.stop();
                } else {

                    String current_play = media.getSource();
                    for (File file : files) {
                        final String source = file.getAbsolutePath();
                        if (current_play.equalsIgnoreCase(source)) {
                            index_of_current_playing = files.indexOf(source);
                        }
                        System.out.println("cunrent playing=" + current_play + " playing index-" + index_of_current_playing);

                        final File next_item = files.get(index_of_current_playing + 1);
                        final String file_string = next_item.getAbsolutePath();

                        System.out.println("next item path" + file_string);
                        mp.stop();
                        Media media = new Media(file_string);
                        mp = new MediaPlayer(media);
                        mediaView = new MediaView(mp);
                        mp.play();

                    }
                }

            }
        });

        video_progress.setOnScrollFinished(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                System.out.println("on scroll finished.......");

            }

        });
        video_progress.setOnSwipeRight(new EventHandler<SwipeEvent>() {
            @Override
            public void handle(SwipeEvent event) {
                System.out.println("slider swipe right <<<<<<<<..");

            }

        });
        video_progress.setOnSwipeLeft(new EventHandler<SwipeEvent>() {
            @Override
            public void handle(SwipeEvent event) {
                System.out.println("slider swipe left >>>>>>>..");

            }

        });
        video_progress.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (video_progress.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    if (duration != null) {
                        mp.seek(duration.multiply(video_progress.getValue() / 100.0));
                    }
                    updateValues();
                }
            }
        });
        video_progress.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("click slider................");
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                mp.pause();
                double tm = video_progress.getValue();
                mp.seek(Duration.seconds(tm));
                video_progress.setValue(tm);
                mp.play();
                System.out.println(video_progress.getValue());
                System.out.println(mp.getCurrentTime());
            }

        });

        controls_menu.getChildren().addAll(video_progress, playTime);
        instructions_menu.getChildren().add(info_button);
        //container
        //StackPane root = new StackPane();
        Group root = new Group();
        root.getChildren().addAll(view, controls_menu, instructions_menu);

        Scene scene = new Scene(root, 300, 250);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Media player");
        primaryStage.getIcons().add(new Image(APPLICATION_ICON));
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                System.out.println("----------<<<<<<< on close event media player >>>>>>>>>>>>---------");
                mp.stop();
                mp.dispose();

            }

        });

        //stage listeners
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            instructions_menu.setTranslateX((newVal.intValue() - 80));
            video_progress.setPrefWidth(newVal.intValue() - 90);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
        });

        view.setPreserveRatio(false);
        view.fitWidthProperty().bind(primaryStage.widthProperty());
        view.fitHeightProperty().bind(primaryStage.heightProperty());

        instructions_menu.prefWidthProperty().bind(primaryStage.widthProperty());
        controls_menu.prefWidthProperty().bind(primaryStage.widthProperty());

        instructions_menu.setTranslateY(20);
        //   open_player("C:\\Users\\kamau\\Videos\\4K Video Downloader\\fun\\Most Epic Game Trailers in 4k Part 7.mp4");

        /**
         * drag and drop evnts
         *
         */
        scene.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != scene
                        && event.getDragboard().hasFiles()) {
                    if (!validExtensions.containsAll(
                            event.getDragboard().getFiles().stream()
                                    .map(file -> getExtension(file.getName()))
                                    .collect(Collectors.toList()))) {

                        event.consume();
                        return;
                    }
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
                if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
                    // Print files
                    event.getDragboard().getFiles().forEach(
                            file -> {
                                System.out.println(file.getAbsolutePath());
                                play_list_items.add(file.getAbsolutePath());
                            });
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        /**
         * mouse hover over contrls
         *
         */
        final Timeline slidein_controls_menu = new Timeline();
        final Timeline slideout_controls_menu = new Timeline();

        //for animation
        slideout_controls_menu.getKeyFrames().addAll(
                new KeyFrame(new Duration(200),
                        //                                new KeyValue(controls_menu.translateYProperty(), 40),
                        new KeyValue(controls_menu.opacityProperty(), .9)
                ),
                new KeyFrame(new Duration(600),
                        //                                new KeyValue(controls_menu.translateYProperty(), 40),
                        new KeyValue(controls_menu.opacityProperty(), 0.0)
                )
        );

        slidein_controls_menu.getKeyFrames().addAll(
                new KeyFrame(new Duration(200),
                        //                                new KeyValue(controls_menu.translateYProperty(), 40),
                        new KeyValue(controls_menu.opacityProperty(), 0.0)
                ),
                new KeyFrame(new Duration(600),
                        //                                new KeyValue(controls_menu.translateYProperty(), 40),
                        new KeyValue(controls_menu.opacityProperty(), .9)
                )
        );

        controls_menu.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                slideout_controls_menu.play();
            }

        });
        controls_menu.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                slidein_controls_menu.play();
            }

        });

        /**
         * mouse click listener
         */
        // Handler for mouse pressed, to get current state.
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // This works
            }
        });

        /**
         * keyboard listeners
         */
        //control n --for previous  or n
        KeyCombination cntrln = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
        //control p --for previous  or p
        KeyCombination cntrlp = new KeyCodeCombination(KeyCode.P, KeyCodeCombination.CONTROL_DOWN);
        // contol up for speed volume increase
        KeyCombination cntrlup = new KeyCodeCombination(KeyCode.UP, KeyCodeCombination.CONTROL_DOWN);
        // contol down for speed volume increase
        KeyCombination cntrldown = new KeyCodeCombination(KeyCode.DOWN, KeyCodeCombination.CONTROL_DOWN);
        // contol up for speed forwad 
        KeyCombination cntrlright = new KeyCodeCombination(KeyCode.RIGHT, KeyCodeCombination.CONTROL_DOWN);
        // contol down for speed rewind
        KeyCombination cntrlleft = new KeyCodeCombination(KeyCode.LEFT, KeyCodeCombination.CONTROL_DOWN);
        //control l for playlist
        KeyCombination cntrll = new KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN);
        //full screen or not
        KeyCombination cntrlf = new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN);
         //close screen if not full screen
        KeyCombination cntrlclose_curlybracket = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        //open screen if not full screen
        KeyCombination cntrlopen_curlybracket = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                //show playlist /hide playlist
                if (cntrll.match(event) || event.getCode() == KeyCode.L) {
                    //Do something
                    System.out.println("control p or p ");
                }

                //next in playlist
                if (cntrlp.match(event) || event.getCode() == KeyCode.P) {
                    //Do something
                    System.out.println("control p or p ");
                }

                //previous in playlist
                if (cntrln.match(event) || event.getCode() == KeyCode.N) {
                    //Do something
                    System.out.println("control n or n");
                }

                //pause or play
                if (event.getCode() == KeyCode.SPACE) {
                    System.out.println("space bar --pause");
                    //Stop letting it do anything else
                    System.out.println("play");
                    boolean playing_ = mp.getStatus().equals(MediaPlayer.Status.PLAYING);

                    if (playing_ == true) {
                        //is playing set to pause
                        System.out.println("is playing set to pause");

                        mp.pause();
                    } else {
                        //is pauset set to play
                        System.out.println("is pauset set to play");

                        mp.play();
                    }
                    event.consume();
                }
                /*
                *volume
                 */
                // increase
                if (cntrlup.match(event)) {
                    System.out.println("control up volume --speed increase");
                    Double vol_curr = mp.getVolume();
                    System.out.println(vol_curr);
                    if (0 > vol_curr || vol_curr < 1) {
                        Double new_vol = vol_curr + 0.05;
                        mp.setVolume(new_vol);
                        fadein.play();
                        info_button.setText(String.valueOf(Math.round(new_vol * 100)));
                    } else {
                        //mp.setVolume(10.0);
                        System.out.println("max volume");
                        fadein.play();
                        info_button.setText("max volume");
                    }
                }
                //decrease
                if (cntrldown.match(event)) {
                    System.out.println("control down --speed decrease");
                    Double vol_curr = mp.getVolume();
                    System.out.println(vol_curr);
                    if (vol_curr == 0 || vol_curr < 0) {
                        mp.setVolume(0);
                        fadein.play();
                        info_button.setText("min volume");
                    } else {
                        Double new_vol = vol_curr - 0.05;
                        mp.setVolume(new_vol);
                        fadein.play();
                        info_button.setText(String.valueOf(Math.round(new_vol * 100)));
                    }
                }
                //increase slow
                if (event.getCode() == KeyCode.UP) {
                    System.out.println("volume increase slow");
                    //Stop letting it do anything else
                    Double vol_curr = mp.getVolume();
                    System.out.println(vol_curr);
                    if (0 > vol_curr || vol_curr < 1) {
                        Double new_vol = vol_curr + 0.005;
                        mp.setVolume(new_vol);
                        fadein.play();

                        info_button.setText(String.valueOf(Math.round(new_vol * 100)));
                    } else {
                        //mp.setVolume(10.0);
                        System.out.println("max volume");
                        fadein.play();
                        info_button.setText("max volume");
                    }
                    event.consume();
                }
                //decrease slow
                if (event.getCode() == KeyCode.DOWN) {
                    System.out.println("volume decrese  slow");
                    //Stop letting it do anything else
                    Double vol_curr = mp.getVolume();
                    System.out.println(vol_curr);
                    if (vol_curr == 0 || vol_curr < 0) {
                        mp.setVolume(0);
                        fadein.play();
                        info_button.setText("min volume");
                    } else {
                        Double new_vol = vol_curr - 0.005;
                        mp.setVolume(new_vol);
                        fadein.play();
                        info_button.setText(String.valueOf(Math.round(new_vol * 100)));
                    }
                    event.consume();
                }

                /**
                 * foward rewind jump next jump previous
                 */
                //speed forwad
                if (cntrlright.match(event)) {
                    System.out.println("control right  --speed forwad");
                    mp.pause();
                    mp.seek(mp.getCurrentTime().multiply(1.5));
                    mp.play();
                    
                    fadein.play();
                        info_button.setText("Fast forwad");
                    
                    

                }
                //speed rewind
                if (cntrlleft.match(event)) {
                    System.out.println("control left --speed rewind");
                    mp.pause();
                    mp.seek(mp.getCurrentTime().divide(1.5));
                    mp.play();
                    fadein.play();
                        info_button.setText("Fast rewind");
                }
                //increase slow
                if (event.getCode() == KeyCode.RIGHT) {
                    System.out.println("slow foward");
                    //Stop letting it do anything else
                    mp.seek(mp.getCurrentTime().multiply(0.5));
                    event.consume();
                    fadein.play();
                        info_button.setText(" forwad");
                }
                //decrease slow
                if (event.getCode() == KeyCode.LEFT) {
                    System.out.println("slow rewind");
                    //Stop letting it do anything else
                    mp.seek(mp.getCurrentTime().divide(0.5));
                    event.consume();
                    fadein.play();
                        info_button.setText("rewind");
                }

                //full screen
                if (cntrlf.match(event) || event.getCode() == KeyCode.F) {
                    if (primaryStage.isFullScreen()) {
                        primaryStage.setFullScreen(false);
                    } else {
                        primaryStage.setFullScreen(true);
                    }
                }
                //make stage smaller
                if (cntrlclose_curlybracket.match(event)) {
                    if (!primaryStage.isFullScreen()) {
                        //not full screen
                       Double w= primaryStage.getWidth();
                       Double h = primaryStage.getHeight();
                       
                       primaryStage.setWidth(w-50);
                       primaryStage.setHeight(h-50);  
                    } 
                }
              // make stage biigger 
                if (cntrlopen_curlybracket.match(event)) {
                    if (!primaryStage.isFullScreen()) {
                        //not full screen
                       Double w= primaryStage.getWidth();
                       Double h = primaryStage.getHeight();
                       
                       primaryStage.setWidth(w+50);
                       primaryStage.setHeight(h+50);
                    }
                }
                
                
                
            }
        });

        /**
         * fadin and out for information button
         */
        fadein = new FadeTransition(Duration.seconds(2), info_button);
        fadein.setFromValue(0);
        fadein.setToValue(1);
        fadein.setCycleCount(1);

        fadein.play();

        FadeTransition fadeout = new FadeTransition(Duration.seconds(2), info_button);
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

        //track mouse position
        view.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(mouseEvent.getEventType() + "\n"
                        + "X : Y - " + mouseEvent.getX() + " : " + mouseEvent.getY() + "\n"
                        + "SceneX : SceneY - " + mouseEvent.getSceneX() + " : " + mouseEvent.getSceneY() + "\n"
                        + "ScreenX : ScreenY - " + mouseEvent.getScreenX() + " : " + mouseEvent.getScreenY());

            }

        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    // Method to to get extension of a file
    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) //if the name is not empty
        {
            return fileName.substring(i + 1).toLowerCase();
        }

        return extension;
    }

    public static void open_empty_player() {

    }

    public static void open_player(String media_path) {

    }

    protected  void updateValues() {

        Duration currentTime = mp.getCurrentTime();
        playTime.setText(formatTime(currentTime, duration));

        if (playTime != null && video_progress != null && volumeSlider != null && duration != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    video_progress.setDisable(duration.isUnknown());
                    if (!video_progress.isDisabled() && duration.greaterThan(Duration.ZERO) && !video_progress.isValueChanging()) {
                        video_progress.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume() * 100));
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

}
