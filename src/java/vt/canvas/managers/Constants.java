/*
 * Created by Sait Tuna Onder on 2017.08.29  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.managers;

/**
 *
 * @author Onder
 */
public class Constants {

    /* =========== Our Design Decision ===========
        We decided to use directories external to our application 
        for the storage and retrieval of user's files.
     */
    public static final String FILES_ABSOLUTE_PATH = "/Users/Onder/TrafficSimulatorFileStorage/";
//    public static final String FILES_ABSOLUTE_PATH = "/home/Onder/TrafficSimulatorFileStorage/";
//     public static final String FILES_ABSOLUTE_PATH = "/home/balci/TrafficSimulatorFileStorage/";

    /*
    In glassfish-web.xml file, we designated the '/TrafficSimulatorFileStorage/' directory as the
    Alternate Document Root directory.
    
    Relative path is defined with respect to the Alternate Document Root starting with 'TrafficSimulatorFileStorage'.
     */
    public static final String FILES_RELATIVE_PATH = "TrafficSimulatorFileStorage/";

    public static final String DEFAULT_BACKGROUND_MAP = "DowntownClean.jpg";

    /* Temporary filename */
    public static final String TEMP_FILE = "tmp_file";
}
