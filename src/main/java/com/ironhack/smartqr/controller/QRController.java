package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @PostMapping(value = "/table/{tableNumber}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public byte[] generateQRCodeForTable(@PathVariable Integer tableNumber) throws Exception {
        String qrUrl = "http://localhost:8080/products/menu?table=" + tableNumber;

        return qrService.generateAndSaveTableQRCode(qrUrl, tableNumber, 300, 300);
    }
}


