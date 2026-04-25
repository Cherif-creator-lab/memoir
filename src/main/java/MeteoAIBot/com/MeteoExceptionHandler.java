package MeteoAIBot.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class MeteoExceptionHandler {

    // Un petit outil pour afficher les erreurs dans la console du serveur, très utile pour le débogage.
    private static final Logger logger = LoggerFactory.getLogger(MeteoExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        // On ignore silencieusement les erreurs de type favicon.ico ou h2-console introuvable
        // car cela encombre la console inutilement et bloque parfois l'affichage.
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Requête invalide : {}", ex.getMessage());
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ApiError> handleNonTransientAiException(NonTransientAiException ex) {
        logger.error("Erreur de communication avec le service d'IA : {}", ex.getMessage());
        ApiError error = new ApiError("Erreur de communication avec le service d'IA. Vérifiez votre clé API et vos crédits.", HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        logger.error("Une erreur inattendue est survenue : {}", ex.getMessage(), ex);
        ApiError error = new ApiError("Une erreur interne est survenue sur le serveur.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
