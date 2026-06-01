package com.ironhack.smartqr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class QRService {

    private static final Path TABLE_QR_DIRECTORY = Paths.get("requests", "table_qr");

    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public byte[] generateAndSaveTableQRCode(
            String text,
            Integer tableNumber,
            int width,
            int height
    ) throws Exception {
        byte[] qrImage = generateQRCodeImage(text, width, height);

        Files.createDirectories(TABLE_QR_DIRECTORY);

        Path outputFile = TABLE_QR_DIRECTORY.resolve(
                "table-" + tableNumber + "-qr.png"
        );

        Files.write(
                outputFile,
                qrImage,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );

        return qrImage;
    }
}


