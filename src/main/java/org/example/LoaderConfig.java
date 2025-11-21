package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoaderConfig {

    public static Config incarcareConfiguratie() {
        try {
            Gson gson = new Gson();
            return gson.fromJson(new FileReader("config.json"), Config.class);

        } catch (FileNotFoundException e) {
            System.out.println("Eroare: Fișierul de configurare 'config.json' nu a fost găsit!");
            System.out.println("Vă rugăm verificați instalarea aplicației.");
            return null;

        } catch (JsonSyntaxException e) {
            System.out.println("Eroare: Fișierul 'config.json' este corupt sau formatat greșit!");
            System.out.println("Vă rugăm editați fișierul și corectați structura JSON.");
            return null;
        }
    }
}
