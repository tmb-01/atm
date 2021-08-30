package uz.pdp.bankcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.bankcard.entity.ATM;
import uz.pdp.bankcard.service.ATM_Service;

import java.util.List;

@RestController
@RequestMapping("api/v1/atm")
public class ATM_Controller {

    @Autowired
    ATM_Service atmService;

    @GetMapping
    public List<ATM> moneyOfATMs() {
        return atmService.moneyOfAtms();
    }

}
