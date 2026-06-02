package pt.pauloortolan.qraitoolbox.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.qraitoolbox.services.QRService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @GetMapping()
    public ResponseEntity<byte[]> generateTextQRCode(@RequestParam("prompt") String prompt) {
        log.info("QRTools::generateTextQRCode(prompt = {})", prompt);

        byte[] qrCodeImage = qrService.generateQRCode(prompt);
        String fileName = String.format("qrcode-%s.png", UUID.randomUUID());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("image/png"))
                .body(qrCodeImage);
    }

}
