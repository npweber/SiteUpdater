package npwd.tech.update;

import npwd.tech.roots.*;
import npwd.tech.roots.Writer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static npwd.tech.roots.Runner.runExec;
import static npwd.tech.roots.Sleeper.sleep;
import static npwd.tech.roots.Writer.fromString;
import static npwd.tech.roots.Writer.workingDir;

//Updater: SiteUpdater Main class Program

public class Updater {

    public static void main(String[] args){
        new Updater(args);
    }

    //Main
    private Updater(String[] intake){
        //Intake content to post
        Argumenter argumenter = new Argumenter(new Grouper<String>(){{ importFromArray(new String[]{ "-title", "-content"}); }});
        argumenter.intake(intake);

        //Create temporary ftp script in working directory to be used in retrieving the current update page
        new Grouper<String>(){{
           //Script Contents
           importFromArray(new String[]{
               "user Server bababebe",
               "get /updates/updates.html updates.html",
               "quit"
           });

           //Open Writer & Write contents into created file
           Writer writer = new Writer(fromString(workingDir() + "\\temp.ftp"), false);
           forEach(writer::write);
           writer.finalizeFile();
           //Close stream
           writer.close();
        }};

        //Run script and wait until files transfer
        runExec("cmd /c start /D " + workingDir() + " ftp -n -s:temp.ftp 192.168.1.237");
        sleep(1000);

        //Buffer Already Written Web Data
        Grouper<String> currentWebData = new ReaderGrouper(new File(workingDir() + "/updates.html")).readIntoGrouper();

        //Prepare Template from Assets
        Grouper<String> updateTemplate = new ReaderStreamer(getClass().getResourceAsStream("/resources/updateTemplate.txt")).getReaderGrouper();

        //Insert Input into the Template Buffer
        updateTemplate.set(updateTemplate.indexOf("%Date%"), new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
        updateTemplate.set(updateTemplate.indexOf("%Title%"), argumenter.getValue("-title"));
        updateTemplate.set(updateTemplate.indexOf("%Content%"), argumenter.getValue("-content"));

        //Insert Prepared Entry from template to WebPage at the end of the last entry
        currentWebData.addAll(currentWebData.indexOf("</section>") - 2, updateTemplate);

        //Write FileBuffer into the retrieved webpage
        Writer writer = new Writer(fromString(workingDir() + "/updates.html"), false);
        currentWebData.forEach(writer::write);
        writer.close();

        //Write over the current script new commands to upload the file instead of download
        new Grouper<String>(){{
            importFromArray(new String[]{
                "user Server bababebe",
                "put updates.html /updates/updates.html",
                "quit"
            });
            Writer writer = new Writer(fromString(workingDir() + "\\temp.ftp"), false);
            forEach(writer::write);
            writer.close();
        }};

        //Then upload and wait until transfer is complete
        runExec("cmd /c start /D " + workingDir() + " ftp -n -s:temp.ftp 192.168.1.237");
        sleep(1000);

        //Remove all temporary files
        runExec("cmd /c start /D " + workingDir() + " rm temp.ftp updates.html");
    }
}
