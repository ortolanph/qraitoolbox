package pt.pauloortolan.qraitoolbox.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.pauloortolan.qraitoolbox.services.QRTools;

@Configuration
@RequiredArgsConstructor
public class QRConfiguration {

    private final ChatClient.Builder chatClientBuilder;

    private final QRTools toolbox;

    @Bean
    public ChatClient qrChatClient() {
        return chatClientBuilder
                .defaultTools(toolbox)
                .defaultSystem("""
                        You are a QR code assistant. When the user asks to generate a QR code,
                        call the appropriate tool and return ONLY the raw base64 string from the tool result.
                        No explanation, no markdown, no extra text. Just the base64 string.
                        """)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

}
