package utils;

import java.text.Normalizer;
import java.util.Locale;

public final class TextoUtils {

    private TextoUtils() {
    }

    public static String normalizarNombre(String texto) {
        if (texto == null) {
            return "";
        }

        String normalizado = Normalizer.normalize(
                texto.trim().toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
        );

        return normalizado
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}