package io.codeforall.thestudio.javabank.controllers.rest;

import io.codeforall.thestudio.javabank.converters.AccountDtoToAccount;
import io.codeforall.thestudio.javabank.converters.AccountToAccountDto;
import io.codeforall.thestudio.javabank.dtos.AccountDto;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.AssociationExistsException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.services.AccountService;
import io.codeforall.thestudio.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestAccountController {

    private CustomerService customerService;
    private AccountService accountService;
    private AccountToAccountDto accountToAccountDto;
    private AccountDtoToAccount accountDtoToAccount;

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account")
    public ResponseEntity<List<AccountDto>> listCustomerAccounts(@PathVariable Integer cid) {

        Customer customer = customerService.get(cid);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(accountToAccountDto.convert(customer.getAccounts()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account/{aid}")
    public ResponseEntity<AccountDto> showCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Account account = accountService.get(aid);

        if (account == null || account.getCustomer() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!account.getCustomer().getId().equals(cid)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountToAccountDto.convert(account), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{cid}/account")
    public ResponseEntity<?> addAccount(@PathVariable Integer cid, @Valid @RequestBody AccountDto accountDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || accountDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Account account = customerService.addAccount(cid, accountDtoToAccount.convert(accountDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + cid + "/account/" + account.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{cid}/account/{aid}")
    public ResponseEntity<?> closeAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {

            customerService.closeAccount(cid, aid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AssociationExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }

    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }
}


