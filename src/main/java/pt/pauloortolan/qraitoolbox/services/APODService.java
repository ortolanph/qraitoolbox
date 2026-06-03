package pt.pauloortolan.qraitoolbox.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import pt.pauloortolan.qraitoolbox.pojo.ApodResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class APODService {

    private final ChatClient apodChatClient;

    public ApodResponse getAPODPicture(String prompt) {
        log.info("APODService::getAPODPicture(prompt = {})", prompt);

        return apodChatClient
                .prompt()
                .user(prompt)
                .call()
                .entity(ApodResponse.class);
    }
}
