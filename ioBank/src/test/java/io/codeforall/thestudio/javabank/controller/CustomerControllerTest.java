package io.codeforall.thestudio.javabank.controller;

import io.codeforall.thestudio.javabank.controllers.web.CustomerController;
import io.codeforall.thestudio.javabank.converters.AccountToAccountDto;
import io.codeforall.thestudio.javabank.converters.CustomerDtoToCustomer;
import io.codeforall.thestudio.javabank.converters.CustomerToCustomerDto;
import io.codeforall.thestudio.javabank.dtos.CustomerDto;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private CustomerDtoToCustomer customerDtoToCustomer;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(new CustomerDto());
        customerDtos.add(new CustomerDto());

        when(customerService.list()).thenReturn(customers);
        when(customerToCustomerDto.convert(customers)).thenReturn(customerDtos);

        customerController.listCustomers(model);

        verify(customerService, times(1)).list();
        verify(customerToCustomerDto, times(1)).convert(customers);
        verify(model, times(1)).addAttribute(eq("customers"), anyList());
    }

    @Test
    public void testShowCustomer() {
        Integer fakeId = 1;
        Customer customer = new Customer();
        when(customerService.get(fakeId)).thenReturn(customer);

        customerController.showCustomer(fakeId, model);

        verify(customerService, times(1)).get(fakeId);
        verify(customerToCustomerDto, times(1)).convert(customer);
        verify(accountToAccountDto, times(1)).convert(customer.getAccounts());
        verify(model, times(1)).addAttribute(eq("customer"), any());
        verify(model, times(1)).addAttribute(eq("accounts"), any());
    }

    @Test
    public void testAddCustomer() {
        customerController.addCustomer(model);

        verify(model, times(1)).addAttribute(eq("customer"), any());
    }

    @Test
    public void testSaveCustomer() {
        Integer fakeId = 1;
        String firstName = "John";
        String lastName = "Doe";
        String phone = "123456789";
        String email = "john.doe@example.com";

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        when(customerDtoToCustomer.convert(any(CustomerDto.class))).thenReturn(new Customer());
        when(customerService.add(any(Customer.class))).thenReturn(new Customer());

        customerController.saveCustomer(file, customerDto, model, redirectAttributes);

        verify(customerDtoToCustomer, times(1)).convert(customerDto);
        verify(customerService, times(1)).add(any(Customer.class));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("lastAction"), anyString());
    }

    @Test
    public void testDeleteCustomer() {
        Integer fakeId = 1;
        Customer customer = new Customer();
        when(customerService.get(fakeId)).thenReturn(customer);

        customerController.deleteCustomer(fakeId, redirectAttributes);

        verify(customerService, times(1)).get(fakeId);
        verify(customerService, times(1)).delete(fakeId);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("lastAction"), anyString());
    }

    // Additional test cases can be added as needed for other methods and scenarios.

}
