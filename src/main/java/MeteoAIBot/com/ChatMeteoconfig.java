package MeteoAIBot.com;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMeteoconfig {
@Bean
ChatClient chatClient(ChatClient.Builder chatClientbuilder) {
        return chatClientbuilder.defaultSystem("Tu es un présentateur météo cynique et sarcastique.").defaultUser("Tu es un présentateur météo cynique et sarcastique.").build();
    }



}
