package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.CustomerDto;
import io.codeforall.thestudio.javabank.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerToCustomerDto extends AbstractConverter<Customer, CustomerDto> {

    @Override
    public CustomerDto convert(Customer customer) {

        CustomerDto customerDto = new CustomerDto();

        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhone(customer.getPhone());
        customerDto.setPhotoURL(customer.getPhotoURL());
        customerDto.setNumOfAccounts(customer.getAccounts().size());
        customerDto.setBalance(customer.getBalance());

        return customerDto;
    }
}
