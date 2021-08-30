package uz.pdp.bankcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.bankcard.entity.Action;
import uz.pdp.bankcard.entity.Cash;
import uz.pdp.bankcard.payload.ActionToFill;
import uz.pdp.bankcard.payload.ActionToWithdraw;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.service.ActionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/action")
public class ActionController {

    @Autowired
    ActionService actionService;

    @PostMapping("refill")
    public HttpEntity<?> refillATM(@RequestBody ActionToFill actionToFill) {
        ApiResponse fillATM = actionService.fillATM(actionToFill);
        return ResponseEntity.status(fillATM.isSuccess() ? 200 : 409).body(fillATM.getMessage());
    }

    //    bankomatdan naqd pul olish
    @PostMapping("withdraw")
    public HttpEntity<?> withdraw(@RequestBody ActionToWithdraw actionToWithdraw) {
        ApiResponse withdrawMoney = actionService.withdrawMoney(actionToWithdraw);
        return ResponseEntity.status(withdrawMoney.isSuccess() ? 200 : 409).body(withdrawMoney.getMessage());
    }

    //    kartaga pul solish
    @PostMapping("cash-to-card")
    public HttpEntity<?> cashToCard(UUID atmId, List<Cash> cashList) {
        ApiResponse apiResponse = actionService.cashToCard(atmId, cashList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("income-outcome/{atm-id}")
    public List<Action> incomeAndOutcome(@PathVariable("atm-id") UUID atmId) {
        return actionService.incomeOutcomeById(atmId);
    }

    @GetMapping("get-daily-income/{atm-id}")
    public List<Action> getDailyIncome(@PathVariable("atm-id") UUID atmId) {
        return actionService.getDailyIncome(atmId);
    }

    @GetMapping("get-daily-outcome/{atm-id}")
    public List<Action> getDailyOutcome(@PathVariable("atm-id") UUID atmId) {
        return actionService.getDailyOutcome(atmId);
    }

    @GetMapping("refills/{atm-id}")
    public List<Action> getDailyRefill(@PathVariable("atm-id") UUID atmId) {
        return actionService.getRefills(atmId);
    }


}
