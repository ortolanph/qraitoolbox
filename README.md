Once running, hit /qr/generate with natural language:

| Prompt                                         | Tool called               |   Works?    |
|------------------------------------------------|---------------------------|:-----------:|
| Generate a QR code for Hello World             | `generateTextQrCode`      |     yes     |
| QR code for https://google.com                 | `generateLinkQrCode`      |     yes     |
| QR for Wi-Fi SSID=MyNet password=1234 type=WPA | `generateWifiQrCode`      |      ?      |
| WhatsApp QR for +5511999990000                 | `generateWhatsAppQrCode`  |     yes     |
| QR for PDF at https://example.com/doc.pdf      | `generatePdfQrCode`       | don't know  |
| Phone call QR for +351912345678                | `generatePhoneCallQrCode` |     yes     |
| SMS QR to +351912345678 saying Hello           | `generateSmsQrCode`       |     yes     |

Once running, hit /apod with natural language:

| Prompt                                       | Tool invoked                  | Works? |
|----------------------------------------------|-------------------------------|:------:|
| What is NASA's picture today?                | `getTodayApod`                |  yes   |
| Show me the APOD from July 4th 2020          | `getApodByDate("2020-07-04")` |  yes   |
| What was the astronomy picture one year ago? | `getApodOneYearAgo`           |  yes   |