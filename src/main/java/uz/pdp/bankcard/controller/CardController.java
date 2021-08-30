package uz.pdp.bankcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.payload.CardDto;
import uz.pdp.bankcard.service.CardService;

@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("add-card")
    public ApiResponse addCard(CardDto cardDto) {
        return cardService.addCard(cardDto);
    }

    @PutMapping("enable-card/{cardNumber}")
    public ApiResponse enableCard(@PathVariable String cardNumber) {
        return cardService.enableCard(cardNumber);
    }

}
