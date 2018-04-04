/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Media_Player.music.Media_Player_Music_Start;
import Media_Player.video.Media_Player_Empty;
import Media_Player.video.Media_Player_Playlist;
import Media_Player.video.Media_Player_Start;
import Media_Tool.Media_Tool;
import Media_Viewer.Media_View_Start;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 *
 * @author kamau
 */
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    //open cv loa native
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static ArrayList<String> play_items;
    private static String type;

    public static void main(String[] args) throws IOException {
//      List <String> drives = new ArrayList<>();
//        drives = usb_devices_letters();
//        
//        drives.forEach((e)->{
//            System.out.println("drive = "+e);
//        });
//        
        Runtime runtime = Runtime.getRuntime();

        String cmds = (String) "cmd /C powershell 'C:\\Users\\kamau\\Documents\\NetBeansProjects\\media player code\\src\\assets\\Powershell\\externaldrives.ps1'";

        Process proc = runtime.exec(cmds);

        proc.getOutputStream().close();

        InputStream inputstream = proc.getInputStream();

        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);

        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        String line;

        while ((line = bufferedreader.readLine()) != null) {

            System.out.println("-->" + line);

        }

        play_items = new ArrayList<>();
        //detect the number of args 
        if (args.length == 1) {
            //  args is one 
            type = "single";

        } else if (args.length > 1) {
            type = "playlist";
            //args more than one
            for (String s : args) {
                System.out.println(s);
                play_items.add(s);
            }
        } else {
            //there are no args
            type = "none";
        }
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        Home hm= new Home();
//        hm.start();
        Media_Player_Music_Start mpms = new Media_Player_Music_Start();
        mpms.start("C:\\Users\\kamau\\Music");

//Media_View_Start mvs = new Media_View_Start();
                //mvs.start("C:\\Users\\kamau\\Pictures\\79d1e041244ce4358218e8a2bb568014.jpg");//208381,xcitefun-3d-cool-wallpapers-1.jpg");
                //        if (type.equals("single")) {
                //            String media_path = "C:\\Users\\kamau\\Videos\\4K Video Downloader\\fun\\Video Game Trailers that Will Always Remain flawless.mp4";
                //            Media_Player_Start st = new Media_Player_Start();
                //            st.start(media_path);
                //        } else if (type.equals("playlist")) {
                //            Media_Player_Playlist pl = new Media_Player_Playlist();
                //            pl.start(play_items);
                //        } else {
                //            Media_Player_Empty em = new Media_Player_Empty();
                //            em.start();
                //        }
                //        //launch ui

    }

}
