package com.art.controllers.view;

import com.art.model.AppUser;
import com.art.model.supporting.dto.BalanceDTO;
import com.art.service.AccountTransactionService;
import com.art.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class InvestmentsController {

    private final AccountTransactionService accountTransactionService;

    private final UserService userService;

    public InvestmentsController(AccountTransactionService accountTransactionService,
                                 UserService userService) {
        this.accountTransactionService = accountTransactionService;
        this.userService = userService;
    }

    @PostMapping(path = "/investments")
    public String showInvestments(@ModelAttribute AppUser user, ModelMap model) {
        String login = user.getLogin();
        model.addAttribute("investorLogin", login);
        AppUser investor = userService.findByLogin(login);
        Long ownerId = investor.getId();
        BalanceDTO balanceDTO = accountTransactionService.getBalance(ownerId);
        BigDecimal balance = balanceDTO.getSummary();
        if (balance.compareTo(BigDecimal.valueOf(-1)) > 0 && balance.compareTo(BigDecimal.ONE) < 0) {
            balance = BigDecimal.ZERO;
        }
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("balance", balance);
        return "/flows";
    }

}
