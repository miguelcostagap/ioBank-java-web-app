package io.codeforall.thestudio.javabank.controllers.web;

import io.codeforall.thestudio.javabank.converters.AccountToAccountDto;
import io.codeforall.thestudio.javabank.converters.CustomerToCustomerDto;
import io.codeforall.thestudio.javabank.converters.RecipientDtoToRecipient;
import io.codeforall.thestudio.javabank.converters.RecipientToRecipientDto;
import io.codeforall.thestudio.javabank.dtos.CustomerDto;
import io.codeforall.thestudio.javabank.dtos.RecipientDto;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/customer")
public class RecipientController {

    private CustomerService customerService;
    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtoToRecipient;
    private CustomerToCustomerDto customerToCustomerDto;
    private AccountToAccountDto accountToAccountDto;

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/add")
    public String addRecipient(@PathVariable Integer cid, Model model) {

        Customer customer = customerService.get(cid);
        CustomerDto customerDto = customerToCustomerDto.convert(customer);

        model.addAttribute("customer", customerDto);
        model.addAttribute("recipient", new RecipientDto());
        model.addAttribute("recipients", recipientToRecipientDto.convert(customer.getRecipients()));
        model.addAttribute("accounts", accountToAccountDto.convert(customer.getAccounts()));
        model.addAttribute("operation", "Create Recipient");

        return "add-recipient";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"})
    public String saveRecipient(Model model, @PathVariable Integer cid, @Valid @ModelAttribute("recipient") RecipientDto recipientDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));

        if (bindingResult.hasErrors()) {
            return "redirect:/customer/" + cid;
        }

        try {
            Recipient recipient = recipientDtoToRecipient.convert(recipientDto);
            customerService.addRecipient(cid, recipient);

            redirectAttributes.addFlashAttribute("lastAction", "Saved " + recipientDto.getName());
            return "redirect:/customer/" + cid;

        } catch (AccountNotFoundException ex) {

            bindingResult.rejectValue("accountNumber", "invalid.account", "invalid account number");
            redirectAttributes.addFlashAttribute("failure", "You can't create a recipient of your own account");
            return "redirect:/customer/" + cid;
        } catch (CustomerNotFoundException ex) {
            return "redirect:/customer/" + cid;
        } catch (TransactionInvalidException ex){
            bindingResult.rejectValue("accountNumber", "invalid.account", "invalid account number");
            redirectAttributes.addFlashAttribute("failure", "That account does not exist ");
            return "redirect:/customer/" + cid;
        }
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    @Autowired
    public void setRecipientDtoToRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtoToRecipient = recipientDtoToRecipient;
    }

    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }
}
