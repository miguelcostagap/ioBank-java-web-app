package io.codeforall.thestudio.javabank.controllers.web;

import io.codeforall.thestudio.javabank.converters.*;
import io.codeforall.thestudio.javabank.dtos.*;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.AssociationExistsException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.services.AccountService;
import io.codeforall.thestudio.javabank.services.CustomerService;
import io.codeforall.thestudio.javabank.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/customer")
public class AccountController {

    private CustomerService customerService;
    private TransferService transferService;
    private AccountService accountService;
    private AccountDtoToAccount accountDtoToAccount;
    private TransferDtoToTransfer transferDtoToTransfer;

    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/account"})
    public String addAccount(@PathVariable Integer cid, @Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure","Account creation failed. Balance must be at least 100");
            return "redirect:/customer/" + cid;
        }

        try {
            Account account = accountDtoToAccount.convert(accountDto);
            customerService.addAccount(cid, account);
            redirectAttributes.addFlashAttribute("lastAction", "Created " + account.getAccountType() + " account.");
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Savings account must have a minimum value of 100 at all times");
            return "redirect:/customer/" + cid;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/deposit"})
    public String deposit(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") AccountTransactionDto accountTransactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Deposit failed. Please use valid information.");
            return "redirect:/customer/" + cid;
        }

        accountService.deposit(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));
        redirectAttributes.addFlashAttribute("lastAction", "Deposited " + accountTransactionDto.getAmount() + "€ into account #" + accountTransactionDto.getId());
        return "redirect:/customer/" + cid;
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/withdraw"})
    public String withdraw(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") AccountTransactionDto accountTransactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Withdraw failed. Please use valid information.");
            return "redirect:/customer/" + cid;
        }

        try {
            accountService.withdraw(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));
            redirectAttributes.addFlashAttribute("lastAction", "Withdrew " + accountTransactionDto.getAmount() + "€ from account #" + accountTransactionDto.getId());
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Withdraw failed. " + accountTransactionDto.getAmount() + "€ is over the current balance for account #" + accountTransactionDto.getId());
            return "redirect:/customer/" + cid;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account/{aid}/close")
    public String closeAccount(@PathVariable Integer cid, @PathVariable Integer aid, RedirectAttributes redirectAttributes) throws Exception {

        try {
            customerService.closeAccount(cid, aid);
            redirectAttributes.addFlashAttribute("lastAction", "Closed account " + aid);
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform closing operation. Account # " + aid + " still has funds");
            return "redirect:/customer/" + cid;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/transfer"})
    public String transferToAccount(@PathVariable Integer cid, @Valid @ModelAttribute("transfer") TransferDto transferDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Transfer failed. Please use valid information.");
            return "redirect:/customer/" + cid;
        }

        try {
            transferService.transfer(transferDtoToTransfer.convert(transferDto), cid);
            redirectAttributes.addFlashAttribute("lastAction", "Account #" + transferDto.getSrcId() + " transfered " + transferDto.getAmount() + "€ to account #" + transferDto.getDstId());
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform transaction: value above the allowed amount");
            return "redirect:/customer/" + cid;
        } catch (AccountNotFoundException ex) {
            redirectAttributes.addFlashAttribute("failure", "Account not found. Source account must be yours, destination account must be a recipient or one of yours");
            return "redirect:/customer/" + cid;
        } catch (AssociationExistsException ex) {
            redirectAttributes.addFlashAttribute("failure", "You can't transfer to the same account");
            return "redirect:/customer/" + cid;
        }
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }

    @Autowired
    public void setTransferDtoToTransfer(TransferDtoToTransfer transferDtoToTransfer) {
        this.transferDtoToTransfer = transferDtoToTransfer;
    }
}
