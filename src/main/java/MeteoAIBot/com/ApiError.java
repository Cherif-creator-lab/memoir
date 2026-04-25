package MeteoAIBot.com;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        int status,
        LocalDateTime timestamp
) {
    // Petit constructeur pratique pour générer le timestamp automatiquement
    public ApiError(String message, int status) {
        this(message, status, LocalDateTime.now());
    }
}