package pt.pauloortolan.qraitoolbox.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.pauloortolan.qraitoolbox.services.APODTools;
import pt.pauloortolan.qraitoolbox.services.QRTools;

@Configuration
@RequiredArgsConstructor
public class ToolsConfiguration {

    private final ChatClient.Builder chatClientBuilder;

    private final QRTools qrToolbox;
    private final APODTools apodTools;

    @Bean
    public ChatClient qrChatClient() {
        return chatClientBuilder
                .defaultTools(qrToolbox)
                .defaultSystem("""
                        You are a QR code assistant. When the user asks to generate a QR code,
                        call the appropriate tool and return ONLY the raw base64 string from the tool result.
                        No explanation, no markdown, no extra text. Just the base64 string.
                        """)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient apodChatClient() {
        return chatClientBuilder
                .defaultTools(apodTools)
                .defaultSystem("""
                        You are an astronomy assistant with access to NASA's Astronomy Picture of the Day (APOD).
                        When asked about a picture, fetch it and describe:
                        - The title and date
                        - What is shown in the image and its astronomical significance
                        - Whether it's an image or video
                        - The image URL so the user can view it
                        - Copyright information if present
                        Always be enthusiastic and educational about space!
                        """)
                .build();
    }

}
