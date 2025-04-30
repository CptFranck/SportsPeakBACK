package com.CptFranck.SportsPeak.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class GraphQLQueryLoader {
    public static String load(String filePath) {
        try (InputStream is = GraphQLQueryLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Fichier non trouvé : " + filePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Erreur de lecture du fichier GraphQL", e);
        }
    }
}
