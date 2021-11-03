package com.art.controllers.view;

import com.art.model.supporting.dto.BalanceDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.service.AccountTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Alexandr Stegnin
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestmentsController {

  AccountTransactionService accountTransactionService;

  @PostMapping(path = "/investments")
  public String showInvestments(@ModelAttribute UserDTO user, ModelMap model) {
    String login = user.getLogin();
    model.addAttribute("investorLogin", login);
    BalanceDTO balanceDTO = accountTransactionService.getBalanceByInvestorLogin(login);
    model.addAttribute("ownerId", balanceDTO.getAccountId());
    model.addAttribute("balance", balanceDTO.getSummary());
    return "/flows";
  }

}
