package pt.pauloortolan.qraitoolbox.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.pauloortolan.qraitoolbox.integration.goqr.GoQRClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class QRTools {

    private final GoQRClient client;

    @Value("${integrations.goqr.api.size}")
    private String size;
    @Value("${integrations.goqr.api.charset-source}")
    private String charsetSource;
    @Value("${integrations.goqr.api.ecc}")
    private String ecc;
    @Value("${integrations.goqr.api.format}")
    private String format;
    @Value("${integrations.goqr.api.margin}")
    private int margin;

    @Tool(description = "Generates a QR Code for a given text content")
    public byte[] generateTextQRCode(
            @ToolParam(description = "The text to encode into QRCode") String text) {
        log.info("QRTools::generateTextQRCode(text = {})", text);

        log.info("Calling the integration with QRCode Generator - Simple Text");
        return client.createQrCode(text, size, charsetSource, ecc, format, margin);
    }

    @Tool(description = "Generates a QR code URL for a web link or URL")
    public byte[] generateLinkQRCode(
            @ToolParam(description = "The full URL, e.g. https://example.com") String url) {
        log.info("QRTools::generateLinkQRCode(url = {})", url);

        log.info("Calling the integration with QRCode Generator");
        return client.createQrCode(url, size, charsetSource, ecc, format, margin);
    }

    @Tool(description = "Generates a QR code URL for Wi-Fi network credentials")
    public byte[] generateWifiQRCode(
            @ToolParam(description = "Wi-Fi SSID (network name)") String ssid,
            @ToolParam(description = "Wi-Fi password") String password,
            @ToolParam(description = "Authentication type: WPA, WEP, or nopass") String authType) {
        log.info("QRTools::generateWifiQRCode(ssid = {}, password = {}, authType = {})", ssid, password, authType);

        String data = String.format("WIFI:T:%s;S:%s;P:%s;;", ssid, password, authType);

        return client.createQrCode(data, size, charsetSource, ecc, format, margin);
    }

    @Tool(description = "Generates a QR code URL that opens a WhatsApp chat with a pre-filled message")
    public byte[] generateWhatsAppQRCode(
            @ToolParam(description = "Phone number in international format, e.g. +3519990000000") String phoneNumber,
            @ToolParam(description = "Pre-filled message text (optional)") String message
    ) {
        log.info("QRTools::generateWhatsAppQRCode()");

        String cleaned = phoneNumber.replaceAll("[^\\d]", "");
        String link = message != null && !message.isBlank()
                ? "https://wa.me/" + cleaned + "?text=" + message
                : "https://wa.me/" + cleaned;

        return client.createQrCode(link, size, charsetSource, ecc, format, margin);
    }

    @Tool(description = "Generates a QR code URL that initiates a phone call when scanned")
    public byte[] generatePhoneCallQrCode(
            @ToolParam(description = "Phone number in international format, e.g. +3519990000000") String phoneNumber) {
        String data = "tel:" + phoneNumber.replaceAll("\\s", "");
        return client.createQrCode(data, size, charsetSource, ecc, format, margin);
    }

    @Tool(description = "Generates a QR code URL that pre-fills an SMS when scanned")
    public byte[] generateSmsQrCode(
            @ToolParam(description = "Recipient phone number in international format") String phoneNumber,
            @ToolParam(description = "Pre-filled SMS message body") String message) {
        String data = String.format("SMSTO:%s:%s", phoneNumber, message);
        return client.createQrCode(data, size, charsetSource, ecc, format, margin);
    }

}
