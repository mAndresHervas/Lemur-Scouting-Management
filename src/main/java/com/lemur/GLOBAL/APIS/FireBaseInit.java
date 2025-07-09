package com.lemur.GLOBAL.APIS;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 */
public abstract class FireBaseInit {
    /**
     *
     * @throws IOException
     */
    public static void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/java/com/lemur/GLOBAL/APIS/browniesphere-firebase-adminsdk-fbsvc-15d4e5fa39.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}