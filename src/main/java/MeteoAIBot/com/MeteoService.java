package MeteoAIBot.com;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MeteoService {
    private final ChatClient chatClient;
    public MeteoService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    public MeteoAnalysis analyserCiel(String descriptionDuCiel){
        String prompt="Analyse"+descriptionDuCiel;
        return chatClient.prompt().user(prompt).call().entity(MeteoAnalysis.class);
    }


    public Flux<String> genererBulletin(String ville){

        return chatClient.prompt().user(ville).stream().content();
    }

 public TenueSuggestion   suggererTenue(String descriptionDuCiel){
        String test="les vetements recomander"+descriptionDuCiel;
        return chatClient.prompt().user(test).call().entity(TenueSuggestion.class);
 }


 public CompleteMeteo analyzeComplete(String descriptionDuCiel ){
 //appelle d'abord MeteoAnalysi, PUIS suggererTenue, PUIS blagueMeteo,
     // et renvoie le CompleteMeteo.

     if (descriptionDuCiel == null || descriptionDuCiel.trim().isEmpty()) {
         throw new IllegalArgumentException("La description Du Ciel ne peut pas être vide.");
     }
     MeteoAnalysis  meteoAnalysis=this.analyserCiel(descriptionDuCiel);
     TenueSuggestion tenueSuggestion=this.suggererTenue(descriptionDuCiel);
  return new CompleteMeteo(meteoAnalysis,tenueSuggestion);
 }




}