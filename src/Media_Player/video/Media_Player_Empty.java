/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media_Player.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author kamau
 */
public class Media_Player_Empty {

      private MediaPlayer mediaPlayer;

    Stage primaryStage;
    MediaView view;
    FadeTransition fadein;
    Slider video_progress;
 private Duration duration;

    StackPane dropdown_container;
     private List<File> files;

    List<String> play_list_items;
    ListView<Object> play_list_items_view;

    private String url = null;
    private int index_of_current_playing;

    List<String> validExtensions;
 /*
    *camera
     */
    //set contents of the emerging panes
    Button camera_button;
    Button video_button;
    ImageView self_picture_view;

    VideoCapture capture;
  private Slider volumeSlider;
  
    private Label playTime;
    /**
     * stage for dialog
     */
    Stage url_stage;
    Stage command_stage;
    Stage error_stage;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;
    
    private final String image_player_icon = "/assets/media plyer.jpg";
    Group root;
   Media media;
   
    public Media_Player_Empty() {
        volumeSlider = new Slider();
        
        String media_path = "";
        File file = new File(media_path);
        String path = file.toURI().toASCIIString();

         primaryStage = new Stage();

        root = new Group();
        view = new MediaView();
         dropdown_container = new StackPane();
         
        play_list_items = new ArrayList<>();
        play_list_items_view = new ListView<>();
        /*
    *camera
         */
        //set contents of the emerging panes
        camera_button = new Button("picture");
        video_button = new Button("video");
        self_picture_view = new ImageView();

        capture = new VideoCapture();

        validExtensions = Arrays.asList("mp4", "mkv", "oog");
    }

    public void start() {
        
HBox instructions_menu = new HBox();
        view = new MediaView();
        HBox controls_menu = new HBox();
        playTime = new Label();
        video_progress = new Slider();

        //set up details for the top items
        instructions_menu.setMaxHeight(40);
        instructions_menu.setMaxWidth(100);
        controls_menu.setMaxHeight(40);

//        //background color
        dropdown_container.setStyle("-fx-background-color: blue;");
//        helppane.setStyle("-fx-background-color: black;");
//        scrollpane.setStyle("-fx-background-color: red;");
        instructions_menu.setStyle("-fx-background-color: #51329e; ");//"-fx-background-color: #bfc2c7;");
//        camerapane.setStyle("-fx-background-color: yellow;");
        view.setStyle("-fx-background-color: #bfc2c7;");
        controls_menu.setStyle("-fx-background-color: #d82424;");

        
        //items on control menu.
        Button info_button = new Button();
        info_button.setText("press tab for menu");
        info_button.prefWidth(100);
        info_button.setStyle("-fx-background-insets : 40");
        info_button.setFocusTraversable(false);
        info_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("hide event button");
            }
        });

        controls_menu.getChildren().addAll(video_progress, playTime);
        playTime.setText("00:00/00:00");
        instructions_menu.getChildren().add(info_button);

        root.getChildren().addAll(view, controls_menu, instructions_menu, dropdown_container);

        
        
        video_progress.setPrefWidth(primaryStage.getWidth() - 90);

                /**
         * bind h & w
         */
        view.setPreserveRatio(false);
        view.fitWidthProperty().bind(primaryStage.widthProperty());
        view.fitHeightProperty().bind(primaryStage.heightProperty());

        instructions_menu.prefWidthProperty().bind(primaryStage.widthProperty());
        controls_menu.prefWidthProperty().bind(primaryStage.widthProperty());

        instructions_menu.setTranslateY(20);
        //stage listeners
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            dropdown_container.setTranslateX((newVal.intValue() / 6));
//            helppane.setTranslateX((newVal.intValue() / 6));
//            scrollpane.setTranslateX((newVal.intValue() / 6));
//            camerapane.setTranslateX((newVal.intValue() / 6));

            dropdown_container.setMaxWidth(primaryStage.getWidth() / 1.5);
//            scrollpane.setMaxWidth(primaryStage.getWidth() / 1.5);
//            helppane.setMaxWidth(primaryStage.getWidth() / 1.5);
//            camerapane.setMaxWidth(primaryStage.getWidth() / 1.5);

            instructions_menu.setTranslateX((newVal.intValue() - 80));
            video_progress.setPrefWidth(newVal.intValue() - 90);

            dropdown_container.setPrefWidth(primaryStage.getWidth());
//            scrollpane.setPrefWidth(primaryStage.getWidth());
//            helppane.setPrefWidth(primaryStage.getWidth());
//            camerapane.setPrefWidth(primaryStage.getWidth());

        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
        });
        /**
         * progressbar fade in
         */
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

        Scene scene = new Scene(root, 550, 350);

        primaryStage.setTitle("Media player");
        primaryStage.getIcons().add(new Image(image_player_icon));
        primaryStage.setScene(scene);
        primaryStage.show();

          /**
         * drag drop
         */
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
                    int index = 1;
                    event.getDragboard().getFiles().forEach(
                            file -> {
                                System.out.println(file.getAbsolutePath());
                                play_list_items.add(file.getAbsolutePath());
//                                index++;
                                
                                /**
                                 * counter
                                 */
                            }
                            
                    );
                    
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        
        /**
         * keyboard listeners
         */
        //control u play from url
        KeyCombination cntrlu = new KeyCodeCombination(KeyCode.U, KeyCodeCombination.CONTROL_DOWN);
        //control c camera
        KeyCombination cntrlc = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN);
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
                
                if (event.getCode() == KeyCode.ESCAPE) {
                    // url_stage.close();
                    System.out.println(" big esc");
                }
                //show playlist /hide playlist
                if (cntrlu.match(event) || event.getCode() == KeyCode.U) {
                    //Do something
                    url_stage = new Stage();
                    VBox vbox_conatiner = new VBox();
                    vbox_conatiner.setAlignment(Pos.CENTER);
                    Button inst = new Button("Play");
                    
                    TextField urls_field = new TextField();
                    
                    urls_field.prefWidth(url_stage.getWidth());
                    urls_field.prefHeight(url_stage.getHeight() / 10);
                    inst.prefWidth(url_stage.getWidth() / 2);
                    inst.prefHeight(url_stage.getHeight() / 15);
                    
                    urls_field.setPromptText("Paste url");
                    vbox_conatiner.getChildren().addAll(urls_field, inst);
                    
                    Scene scen = new Scene(vbox_conatiner, 500, 70);
                    url_stage.setScene(scen);
                    url_stage.initStyle(StageStyle.UNIFIED);
                    url_stage.setResizable(false);
                    url_stage.setTitle("Play from Url");
                    url_stage.initModality(Modality.APPLICATION_MODAL);
                    url_stage.initOwner(primaryStage);
                    url_stage.setAlwaysOnTop(true);
                    url_stage.getIcons().add(new Image(image_player_icon));
                    url_stage.showAndWait();
                    
                    inst.setOnAction((e) -> {
                        System.out.println(e);
                        try {
                            String txt_url = urls_field.getText();
                            
                            URL url = new URL(txt_url);
                            System.out.println("play url" + url);
                            //Path mp3 = Files.createTempFile("now-playing", ".mp3");

//                            try (InputStream stream = url.openStream()) {
//                                Files.copy(stream, mp3, StandardCopyOption.REPLACE_EXISTING);
//                            }
                            mediaPlayer.stop();
                            Media media = new Media(txt_url);
                            MediaPlayer mediaPlayer = new MediaPlayer(media);

                            // view = new MediaView(mediaPlayer);
                            view.setMediaPlayer(mediaPlayer);
                            mediaPlayer.play();
                            
                            media.setOnError(() -> {
                                media_error_dialog(media.getError());
                            });
                            
                            url_stage.close();
                            
                        } catch (MalformedURLException ex) {
                            media_error_dialog(ex);
                        
                        }
                    });
                    
                }
                //show playlist /hide playlist
                if (cntrll.match(event) || event.getCode() == KeyCode.L) {
                    //Do something
                    System.out.println("control l or l ");
                    System.out.println("heigh" + dropdown_container.getHeight());
                    if (dropdown_container.getOpacity() == 0) {
                        
                        dropdown_container.getChildren().removeAll(dropdown_container.getChildren());
                        dropdown_container.setOpacity(1);
                        ListView<String> playlist = new ListView<>();
                        
                        playlist.getItems().add("Item 1");
                        playlist.getItems().add("Item 2");
                        playlist.getItems().add("Item 3");
                        
                        dropdown_container.getChildren().add(playlist);
                        dropdown_container.setPrefHeight(primaryStage.getHeight() - 100);
                        System.err.println("scroll pane h=" + dropdown_container.getHeight() + "stage h=" + primaryStage.getHeight());
                    } else {
                        dropdown_container.setOpacity(0);
                        dropdown_container.getChildren().removeAll();
                        
                    }
                }

                //camera pane
                if (cntrlc.match(event) || event.getCode() == KeyCode.C) {
                    System.out.println("heigh" + dropdown_container.getHeight());
                    if (dropdown_container.getOpacity() == 0) {

                        //dropdown_container.getChildren().removeAll();
                        dropdown_container.getChildren().removeAll(dropdown_container.getChildren());
                        
                        dropdown_container.setOpacity(1);
                        camera_button.setOnAction((e) -> {
                            startCamera();
                        });
                        video_button.setOnAction((e) -> {
                            
                        });
                        
                        self_picture_view.minHeight(primaryStage.getHeight() / 3);
                        self_picture_view.minWidth(primaryStage.getWidth() / 3);
                        
                        HBox button_continer = new HBox();
                        button_continer.setAlignment(Pos.CENTER);
                        button_continer.setMaxHeight(50);
                        
                        VBox camera_option_container = new VBox();
                        camera_option_container.setAlignment(Pos.CENTER);
                        
                        button_continer.getChildren().addAll(camera_button, video_button);
                        camera_option_container.getChildren().addAll(self_picture_view, button_continer);
                        
                        dropdown_container.getChildren().add(camera_option_container);
                        
                    } else {
                        dropdown_container.setOpacity(0);
                        
                    }
                }

                //help pane
                if (event.getCode() == KeyCode.H || event.getCode() == KeyCode.TAB) {
                    System.out.println("heigh" + dropdown_container.getHeight());
                    
                    if (dropdown_container.getOpacity() == 0) {
                        dropdown_container.getChildren().removeAll();
                        dropdown_container.setOpacity(1);
                        Label help_title = new Label("Help");
                        
                        dropdown_container.setPrefHeight(primaryStage.getHeight() - 100);
                        dropdown_container.getChildren().add(help_title);
                        
                        System.err.println("scroll pane h=" + dropdown_container.getHeight() + "stage h=" + primaryStage.getHeight());
                    } else {
                        dropdown_container.setOpacity(0);
                    }
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
                        Double w = primaryStage.getWidth();
                        Double h = primaryStage.getHeight();
                        
                        primaryStage.setWidth(w - 50);
                        primaryStage.setHeight(h - 50);
                    }
                }
                // make stage biigger 
                if (cntrlopen_curlybracket.match(event)) {
                    if (!primaryStage.isFullScreen()) {
                        //not full screen
                        Double w = primaryStage.getWidth();
                        Double h = primaryStage.getHeight();
                        
                        primaryStage.setWidth(w + 50);
                        primaryStage.setHeight(h + 50);
                    }
                }

                //reload media
                if (event.getCode() == KeyCode.R) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    
                }
                if (event.getCode() == KeyCode.O) {
                    
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.flv", "*.mp4", "*.mpeg"));
                    File file = fc.showOpenDialog(null);
                    if (!file.getAbsolutePath().isEmpty() || file != null) {
                        String path = file.getAbsolutePath();
                        path = path.replace("\\", "/");
                        
                        media = new Media(new File(path).toURI().toString());
                        mediaPlayer.stop();
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        view.setMediaPlayer(mediaPlayer);
                    }
                }
                
            }
            
        });

    }
    
      private void media_error_dialog(Exception error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Critical error");
        alert.setContentText("Media Player expirirnced a critical error");
        
        Exception ex = error;

// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();
        
        Label label = new Label("The exception stacktrace was:");
        
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
        
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
    
    protected void updateValues() {
        
        Duration currentTime = mediaPlayer.getCurrentTime();
        playTime.setText(formatTime(currentTime, media.getDuration()));
        
        if (playTime != null && video_progress != null && volumeSlider != null && duration != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    video_progress.setDisable(duration.isUnknown());
                    if (!video_progress.isDisabled() && duration.greaterThan(Duration.ZERO) && !video_progress.isValueChanging()) {
                        video_progress.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume() * 100));
                    }
                }
            });
        }
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
                
                self_picture_view.setImage(new Image(image_player_icon, primaryStage.getWidth() / 3, primaryStage.getHeight() / 3, true, true));
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
