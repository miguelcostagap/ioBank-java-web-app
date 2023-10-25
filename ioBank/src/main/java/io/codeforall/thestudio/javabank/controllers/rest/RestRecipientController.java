package io.codeforall.thestudio.javabank.controllers.rest;

import io.codeforall.thestudio.javabank.converters.RecipientDtoToRecipient;
import io.codeforall.thestudio.javabank.converters.RecipientToRecipientDto;
import io.codeforall.thestudio.javabank.dtos.RecipientDto;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.RecipientNotFoundException;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.services.CustomerService;
import io.codeforall.thestudio.javabank.services.RecipientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestRecipientController {

    private CustomerService customerService;
    private RecipientService recipientService;
    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtotoRecipient;

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient")
    public ResponseEntity<List<RecipientDto>> listRecipients(@PathVariable Integer cid) {

        try {

            return new ResponseEntity<>(recipientToRecipientDto.convert(customerService.listRecipients(cid)), HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}")
    public ResponseEntity<RecipientDto> showRecipient(@PathVariable Integer cid, @PathVariable Integer id) {

        Recipient recipient = recipientService.get(id);

        if (recipient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipientToRecipientDto.convert(recipient), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{cid}/recipient")
    public ResponseEntity<?> addRecipient(@PathVariable Integer cid, @Valid @RequestBody RecipientDto recipientDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || recipientDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Recipient recipient = customerService.addRecipient(cid, recipientDtotoRecipient.convert(recipientDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + cid + "/recipient/" + recipient.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/recipient/{id}")
    public ResponseEntity<RecipientDto> editRecipient(@PathVariable Integer cid, @PathVariable Integer id, @Valid @RequestBody RecipientDto recipientDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (recipientDto.getId() != null && recipientDto.getId() != id) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            recipientDto.setId(id);

            Recipient savedRecipient = recipientDtotoRecipient.convert(recipientDto);
            customerService.addRecipient(cid, savedRecipient);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{cid}/recipient/{id}")
    public ResponseEntity<?> deleteRecipient(@PathVariable Integer cid, @PathVariable Integer id) {

        try {

            customerService.removeRecipient(cid, id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    @Autowired
    public void setRecipientDtotoRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtotoRecipient = recipientDtoToRecipient;
    }
}
