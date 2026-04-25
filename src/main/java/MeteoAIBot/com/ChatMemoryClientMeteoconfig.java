package MeteoAIBot.com;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryClientMeteoconfig {
@Bean
ChatMemory chatMemory (JdbcChatMemoryRepository chatMemoryRepository) {
   return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(chatMemoryRepository).build();

}




@Bean("chatMemoryChatClient")
ChatClient chatClient(ChatClient.Builder chatClientbuilder, ChatMemory chatMemory) {
    Advisor MemoryAdvisor=MessageChatMemoryAdvisor.builder(chatMemory).build();
    Advisor loggerAdvisor = new SimpleLoggerAdvisor();

    return chatClientbuilder.defaultAdvisors(List.of(loggerAdvisor,MemoryAdvisor))
    .build();
}



}
