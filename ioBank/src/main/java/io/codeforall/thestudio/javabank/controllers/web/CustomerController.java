package io.codeforall.thestudio.javabank.controllers.web;

import io.codeforall.thestudio.javabank.converters.AccountToAccountDto;
import io.codeforall.thestudio.javabank.converters.CustomerDtoToCustomer;
import io.codeforall.thestudio.javabank.converters.CustomerToCustomerDto;
import io.codeforall.thestudio.javabank.converters.RecipientToRecipientDto;
import io.codeforall.thestudio.javabank.dtos.*;
import io.codeforall.thestudio.javabank.exceptions.AssociationExistsException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.services.CustomerService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;
    private CustomerToCustomerDto customerToCustomerDto;
    private CustomerDtoToCustomer customerDtoToCustomer;
    private RecipientToRecipientDto recipientToRecipientDto;
    private AccountToAccountDto accountToAccountDto;
    private static final long MAX_FILE_SIZE = 1048576;

    @Value("${absolute.upload.dir}")
    private String absUploadDir;

    @Value("${relative.upload.dir}")
    private String relUploadDir;
    private final ServletContext servletContext;

    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerToCustomerDto.convert(customerService.list()));
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String showCustomer(@PathVariable Integer id, Model model) {

        Customer customer = customerService.get(id);
        CustomerDto customerDto = customerToCustomerDto.convert(customer);
        List<RecipientDto> recipients = recipientToRecipientDto.convert(customer.getRecipients());
        List<AccountDto> accountDtos =
            accountToAccountDto.convert(customer.getAccounts()).stream().sorted(Comparator.comparingInt(AccountDto::getId)).collect(Collectors.toList());

        model.addAttribute("customer", customerDto);
        model.addAttribute("accounts", accountDtos);
        model.addAttribute("account",new AccountDto());
        model.addAttribute("recipients", recipients);
        model.addAttribute("recipient",new RecipientDto());
        model.addAttribute("transaction", new AccountTransactionDto());
        model.addAttribute("transfer",new TransferDto());

        return "profile";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "add-update";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/edit")
    public String editCustomer(@PathVariable Integer id, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(id)));
        return "add-update";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/", ""})
    public String saveCustomer(@RequestParam("photo") MultipartFile file, @ModelAttribute("customer") CustomerDto customerDto, Model model, RedirectAttributes redirectAttributes) {

        if (file.getSize() > MAX_FILE_SIZE) {

            model.addAttribute("error", "File size should be less than 1MB.");
            model.addAttribute("customer", customerDto);
            return "add-update";
        }
        String fileName = uploadPicture(file);

        customerDto.setPhotoURL(fileName);

        Customer savedCustomer = customerService.add(customerDtoToCustomer.convert(customerDto));

        redirectAttributes.addFlashAttribute("lastAction", "Saved " + savedCustomer.getFirstName() +
                " " + savedCustomer.getLastName());

        return "redirect:/customer/list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            Customer customer = customerService.get(id);
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("lastAction", "Deleted " + customer.getFirstName() +
                    " " + customer.getLastName());
            return "redirect:/customer/list";

        } catch (AssociationExistsException ex) {
            redirectAttributes.addFlashAttribute("failure","Customer contains accounts.");
            return "redirect:/customer/list";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("failure","Customer doesn't exists");
            return "redirect:/customer/list";
        }
    }

    private String uploadPicture(MultipartFile file) {

        String fileName = null;

        if (!file.isEmpty()) {
            try {
                fileName = "profile_picture_" + file.getOriginalFilename();

                Path absFilePath = Paths.get(absUploadDir, fileName);
                Path relFilePath = Paths.get(servletContext.getRealPath(relUploadDir), fileName);

                Files.copy(file.getInputStream(), absFilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(file.getInputStream(), relFilePath, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    @Autowired
    public CustomerController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    @Autowired
    public void setCustomerDtoToCustomer(CustomerDtoToCustomer customerDtoToCustomer) {
        this.customerDtoToCustomer = customerDtoToCustomer;
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
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }
}