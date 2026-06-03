package pt.pauloortolan.qraitoolbox.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class QRService {

    private final ChatClient qrChatClient;

    public byte[] generateQRCode(String prompt) {
        log.info("QRService::generateQRCode(prompt = {})", prompt);
        String base64 = qrChatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        assert base64 != null;
        return Base64.getDecoder().decode(base64.trim());
    }


}
