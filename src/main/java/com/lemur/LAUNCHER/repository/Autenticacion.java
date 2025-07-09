package com.lemur.LAUNCHER.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lemur.GLOBAL.model.Usuario;
import com.lemur.LAUNCHER.controller.ControllerLogin;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.prefs.Preferences;

/**
 *
 */
public class Autenticacion {
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    public static ControllerLogin controllerLogin = new ControllerLogin();
    private static final Firestore db = FirestoreClient.getFirestore();
    private static final String FIREBASE_API_KEY_WEB = "AIzaSyDRvoh24TuLzT8ckz_x8UEB2aK6kfKmz14";

    public boolean iniciarSesion(String username, String password) {
        try {
            String email = getEmailUser(username);

            if (email == null) {
                logger.info("Usuario no encontrado o sin email.");
                return false;
            }

            if (validarCredenciales(email, password)) {
                controllerLogin.setUsuario(this.obtenerUsuarioPorUserName(username));
                logger.info("Inicio de sesión exitoso para: " + username);
                return true;
            } else {
                logger.info("Credenciales incorrectas.");
            }
        } catch (Exception e) {
            logger.info("Error al iniciar sesión: " + e.getMessage());
        }
        return false;
    }

    public String getEmailUser(String username) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("usuarios")
                    .whereEqualTo("username", username)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            if (querySnapshot.isEmpty()) {
                return null;
            }

            for (QueryDocumentSnapshot document : querySnapshot) {
                return document.getString("email");
            }
        } catch (Exception e) {
            logger.info("Error al obtener el email del usuario: " + e.getMessage());
        }
        return null;
    }


    private static boolean validarCredenciales(String email, String password) {
        try {
            String urlStr = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY_WEB;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format(
                    "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                    email, password
            );
            logger.info("[MARC_DEBUG] JSON ENVIADO A FIREBASE: " + jsonInput);


            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            if (status == 200) {
                return true;
            } else {
                Scanner scanner = new Scanner(conn.getErrorStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                JsonObject errorObj = JsonParser.parseString(response).getAsJsonObject();
                logger.info("Error en login: " + errorObj);
            }
            Scanner scanner = new Scanner(conn.getErrorStream()).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            logger.info("Respuesta de error del servidor: " + response);
            response = scanner.hasNext() ? scanner.next() : "";
            logger.info("⚠️ ERROR COMPLETO DEL SERVIDOR: " + response);
        } catch (Exception e) {
            logger.info("Error al validar credenciales: " + e.getMessage());
        }
        return false;
    }
    public Usuario obtenerUsuarioPorUserName(String username) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("usuarios")
                    .whereEqualTo("username", username)
                    .get();

            QuerySnapshot snapshot = future.get();

            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);

                String nombreUsuario = doc.getString("username");
                boolean esSecretaria = Boolean.TRUE.equals(doc.getBoolean("secretaria"));
                boolean esMaterial = Boolean.TRUE.equals(doc.getBoolean("material"));

                return new Usuario(nombreUsuario, esSecretaria, esMaterial);
            } else {
                logger.info("No se encontró el usuario con ese nombre.");
            }
        } catch (Exception e) {
            logger.info("Error al obtener el usuario: " + e.getMessage());
        }
        return null;
    }
    public void cerrarSesionUsuario()
    {
        Preferences userPrefs = Preferences.userRoot().node("auth");
        userPrefs.remove("idToken");
        userPrefs.remove("refreshToken");
        userPrefs.remove("uid");
        try {
            String uid = userPrefs.get("uid", null);
            if (uid != null) {
                FirebaseAuth.getInstance().revokeRefreshTokens(uid);
            }
        } catch (FirebaseAuthException e) {
            logger.info("Error revocando el token: " + e.getMessage());
        }
    }

    public void restablecerContraseña(String email) {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                + FIREBASE_API_KEY_WEB + "&hl=es";

        String jsonBody = "{"
                + "\"requestType\":\"PASSWORD_RESET\","
                + "\"email\":\"" + email + "\""
                + "}";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);

            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonBody));

            httpClient.execute(request);
            logger.info("Correo de restablecimiento enviado");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

}