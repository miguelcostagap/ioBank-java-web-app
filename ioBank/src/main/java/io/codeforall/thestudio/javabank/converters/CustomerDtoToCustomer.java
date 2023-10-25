package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.CustomerDto;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoToCustomer implements Converter<CustomerDto, Customer> {

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Customer convert(CustomerDto customerDto) {

        Customer customer = (customerDto.getId() != null ? customerService.get(customerDto.getId()) : new Customer());

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());

        if (customerDto.getPhotoURL() != null && !customerDto.getPhotoURL().equals("")) {
            customer.setPhotoURL(customerDto.getPhotoURL());
        } else {
            customer.setPhotoURL("default-user.png");
        }

        return customer;
    }
}
