package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class FirebaseAppInitializer {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(FirebaseAppInitializer.class, args);

		FileInputStream serviceAccount = new FileInputStream("C:\\Users\\aliiski\\Desktop\\work\\spring-boot-vuejs\\backend\\src\\main\\java\\com\\example\\serviceAccountKey.json");

		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://spring-boot-vue-1545055785370.firebaseio.com")
                    .build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FirebaseApp.initializeApp(options);
	}
}
