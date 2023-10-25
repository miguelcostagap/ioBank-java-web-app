package io.codeforall.thestudio.javabank.controllers.rest;


import io.codeforall.thestudio.javabank.converters.TransferDtoToTransfer;
import io.codeforall.thestudio.javabank.dtos.AccountTransactionDto;
import io.codeforall.thestudio.javabank.dtos.TransferDto;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.services.AccountService;
import io.codeforall.thestudio.javabank.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestTransactionController {

    private TransferService transferService;
    private AccountService accountService;
    private TransferDtoToTransfer transferDtoToTransfer;

    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/transfer")
    public ResponseEntity<TransferDto> transfer(@PathVariable Integer cid, @Valid @RequestBody TransferDto transferDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            transferService.transfer(transferDtoToTransfer.convert(transferDto), cid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/deposit")
    public ResponseEntity<AccountTransactionDto> deposit(@PathVariable Integer cid, @Valid @RequestBody AccountTransactionDto accountTransactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            accountService.deposit(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/withdraw")
    public ResponseEntity<AccountTransactionDto> withdraw(@PathVariable Integer cid, @Valid @RequestBody AccountTransactionDto accountTransactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            accountService.withdraw(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public void setTransferDtoToTransfer(TransferDtoToTransfer transferDtoToTransfer) {
        this.transferDtoToTransfer = transferDtoToTransfer;
    }

}
