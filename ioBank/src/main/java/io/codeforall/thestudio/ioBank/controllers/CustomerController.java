package io.codeforall.thestudio.ioBank.controllers;

import io.codeforall.thestudio.ioBank.converters.CustomerConverter;
import io.codeforall.thestudio.ioBank.dto.CustomerDTO;
import io.codeforall.thestudio.ioBank.model.Customer;
import io.codeforall.thestudio.ioBank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {
    private CustomerConverter customerConverter;
    private CustomerService customerService;

    @Autowired
    public void setCustomerConverter(CustomerConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"/list", "/", ""})
    public String listCustomers(Model model) {
        List<Customer> customerList = customerService.list();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerDTO customerDTO = customerConverter.toDTO(customer);
            customerDTOList.add(customerDTO);
        }
        model.addAttribute("customerDTOs", customerDTOList);
        return "index";
    }

    @GetMapping("/get/customerId={id}")
    public String getCustomer(Model model, @PathVariable("id") Integer id) {
        CustomerDTO customerDTO = customerConverter.toDTO(customerService.get(id));
        model.addAttribute("customerDTO", customerDTO);
        return "customer";
    }

    @GetMapping("/new-client")
    public String createClient(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "manageCustomer";
    }

    @PostMapping("/new-client")
    public String saveCustomer(@ModelAttribute("customerDTO") CustomerDTO customerDTO, RedirectAttributes redirectAttributes) {
        Customer customer = customerConverter.toCustomer(customerDTO);
        customerService.add(customer);
        return "redirect:/list";
    }

    @GetMapping("/get/edit-client={id}")
    public String editCustomer(Model model, @PathVariable("id") Integer id) {
        CustomerDTO customerDTO = customerConverter.toDTO(customerService.get(id));
        model.addAttribute("customerDTO", customerDTO);
        return "manageCustomer";
    }

    @PostMapping("/get/edit-client={id}")
    public String reSaveCustomer(@ModelAttribute("customerDTO") CustomerDTO customerDTO, @PathVariable("id") Integer id) {
        Customer customer = customerConverter.toCustomer(customerDTO);
        customerService.updateCustomer(id, customer);
        return "customer";
    }

    @GetMapping("/get/delete-client={id}")
    public String deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        return "redirect:/list";
    }
}
