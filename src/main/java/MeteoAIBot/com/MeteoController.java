package MeteoAIBot.com;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * Cette classe est le contrôleur (Controller) de l'application.
 * En Java Spring Boot, un contrôleur agit comme un point d'accueil : 
 * il reçoit les requêtes (demandes) depuis internet (par exemple depuis votre navigateur web)
 * et décide comment y répondre. Ici, il va gérer les demandes concernant la météo.
 */
@RestController
public class MeteoController {

    /**
     * C'est le service qui va faire le travail d'analyse pour nous.
     * C'est comme un assistant qui sait analyser la météo.
     */
    private final MeteoService meteoService;

    /**
     * C'est notre outil pour parler avec l'Intelligence Artificielle.
     * C'est un peu comme le téléphone que le contrôleur va utiliser pour appeler l'IA.
     */
    private final ChatClient chatClient;

    /**
     * C'est le modèle de notre question (le "prompt").
     * Les mots entre accolades {ville} et {jour} sont des "trous" ou variables 
     * que l'on remplacera avec la vraie ville et la vraie date plus tard.
     */
    private static final String PROMPT_METEO = "Donne moi la météo de {ville} à ce jour : {jour}";

    /**
     * C'est le "constructeur". Il est appelé automatiquement au lancement du programme
     * pour fabriquer le MeteoController. On lui donne ce dont il a besoin pour fonctionner.
     *
     * @param builder L'outil de Spring qui permet de construire le ChatClient.
     * @param meteoService L'assistant qui sait analyser le ciel (Spring va le fournir automatiquement).
     */
    public MeteoController(ChatClient.Builder builder, MeteoService meteoService) {
        this.chatClient = builder.build();
        // On connecte notre assistant (meteoService) pour pouvoir l'utiliser plus tard
        this.meteoService = meteoService;
    }

    /**
     * Cette méthode est la porte d'entrée pour la météo simple. 
     * L'annotation @GetMapping("/meteo") signifie : "Si quelqu'un tape l'adresse /meteo, exécute cette méthode".
     *
     * @param timestamp La date demandée. Si l'utilisateur ne donne rien, ce sera vide.
     * @param ville La ville demandée. Si l'utilisateur ne dit rien, ce sera "Lyon" par défaut.
     * @return La réponse texte générée par l'Intelligence Artificielle.
     */
    @GetMapping(value = "/meteo", produces = "text/plain;charset=UTF-8")
    public String getMeteo(
            @RequestParam(value = "jour", required = false) String timestamp,
            @RequestParam(value = "ville", defaultValue = "Lyon") String ville) {

        // Si la date n'est pas fournie dans l'adresse (ex: ?jour=...), on prend la date d'aujourd'hui.
        if (timestamp == null || timestamp.isEmpty()) {
            timestamp = LocalDate.now().toString();
        }
        String finalTimestamp = timestamp;

        // On appelle l'IA pour lui poser la question préparée
        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec
                        .text(PROMPT_METEO)
                        .param("ville", ville)
                        .param("jour", finalTimestamp)
                )
                .call()
                .content();
    }

    /**
     * Cette méthode est la porte d'entrée pour analyser une description du ciel.
     * L'annotation @GetMapping("/api/meteo/analyze") permet d'y accéder.
     *
     * @param descriptionDuCiel Le texte qui décrit le ciel.
     * @return Le résultat de l'analyse fait par le MeteoService.
     */
    @GetMapping(value = "/api/meteo/analyze", produces = "application/json;charset=UTF-8")
    public MeteoAnalysis analyzeMeteo(@RequestParam("descriptionDuCiel") String descriptionDuCiel)   {
        // On demande à notre assistant (meteoService) d'analyser la description
        MeteoAnalysis meteoAnalysis = meteoService.analyserCiel(descriptionDuCiel);
        return meteoAnalysis;
    }

@GetMapping(value ="api/stream",produces = "text/plain;charset=UTF-8")
    public Flux<String> testegenererBulletin(@RequestParam("ville") String ville){
        return meteoService.genererBulletin(ville);
}

@GetMapping(value = "/api/meteo/tenue", produces = "application/json;charset=UTF-8")
    public TenueSuggestion testsuggererTenue( @RequestParam("descriptionDuCiel") String descriptionDuCiel){
    return meteoService.suggererTenue(descriptionDuCiel);
    }

    @GetMapping(value = "/api/meteo/completeMeteo", produces = "application/json;charset=UTF-8")
    public CompleteMeteo completeMeteo(@RequestParam("descriptionDuCiel") String descriptionDuCiel){
        return meteoService.analyzeComplete(descriptionDuCiel);
    }
    }