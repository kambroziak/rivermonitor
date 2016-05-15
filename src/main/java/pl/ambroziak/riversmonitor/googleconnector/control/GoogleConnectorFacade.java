package pl.ambroziak.riversmonitor.googleconnector.control;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStationAvarageState;
import pl.ambroziak.riversmonitor.googleconnector.boundry.ExportToGoogleTimer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 03.05.16.
 */
@Component
@Scope("singleton")
public class GoogleConnectorFacade {

    private static final Logger logger = Logger.getLogger(GoogleConnectorFacade.class);


    private static final String APPLICATION_NAME =
            "riverMonitor";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/riverMonitor.json");
    public static final String SRIPT_ID = "Mp_3MUlTEaqzsqVSb4dWvaJ7piRvQKNJy";
    public static final String INSERT_NEW_DATA = "insertNewData";

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    private static final List<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/spreadsheets");

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                GoogleConnectorFacade.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return httpRequest -> {
            requestInitializer.initialize(httpRequest);
            httpRequest.setReadTimeout(380000);
        };
    }

    private static Script getScriptService() throws IOException {
        Credential credential = authorize();
        return new Script.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void export(LocalDate day, Optional<HydroStationAvarageState> valueCzarnkow, Optional<HydroStationAvarageState> valueUjscie) throws IOException {
        String date = day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Script service = getScriptService();

        // Create an execution request object.
        Double czarnkow = valueCzarnkow.map(HydroStationAvarageState
                ::getValue).orElse(0d);
        Double ujscie = valueUjscie.map(HydroStationAvarageState::getValue)
                .orElse(0d);
        logger.info("Exporting to google day: " + date + "values: czarnkow=" + czarnkow + ", ujscie=" + ujscie);
        ExecutionRequest request = new ExecutionRequest()
                .setFunction(INSERT_NEW_DATA)
                .setParameters(Arrays.asList(date, czarnkow, ujscie));

        service.scripts().run(SRIPT_ID, request).execute();
    }
}
