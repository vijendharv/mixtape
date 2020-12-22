package com.vijay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vijay.model.Mixtape;
import com.vijay.model.change.Change;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

public class App {

    public static void main(String[] args){

        // This application expects paths to 2 files
        // 1) Path to mixtape.json
        // 2) Path to changes.json (Changes that needed to be applied to mixtape)
        if (args.length != 2) {
            System.out.println("This application expects paths to 2 files");
            System.out.println("1) Path to mixtape.json");
            System.out.println("2) Path to changes.json (Changes that needed to be applied to mixtape");
            System.exit(1);
        }

        final Logger logger = Logger.getLogger(App.class);
        try {
            logger.info("Started applying changes to mix tape");
            String mixtapeFilePath = args[0];
            String changesFilePath = args[1];

            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            Mixtape mixtape = objectMapper.readValue(new File(mixtapeFilePath), Mixtape.class);
            List<Change> changes = objectMapper.readValue(new File(changesFilePath), new TypeReference<List<Change>>() {});

            MixtapeController mixtapeController = new MixtapeController(mixtape, changes);
            mixtapeController.mix();

            objectMapper.writeValue(new File(Constants.OUTPUT_FILE), mixtapeController.getMixtape());
            logger.info("Completed applying changes to mix tape.");
            System.out.println("Please check the log file mixtapechanges.log and make sure that there were no errors.");
        }
        catch(Exception e){
            logger.error("Encountered an exception while applying the changes. Please check the log file for more information.", e);
        }
    }
}
