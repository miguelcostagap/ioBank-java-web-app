package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.RestCustomerDto;
import io.codeforall.thestudio.javabank.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerToRestCustomerDto extends AbstractConverter<Customer, RestCustomerDto> {

    @Override
    public RestCustomerDto convert(Customer customer) {

        RestCustomerDto restCustomerDto = new RestCustomerDto();

        restCustomerDto.setId(customer.getId());
        restCustomerDto.setFirstName(customer.getFirstName());
        restCustomerDto.setLastName(customer.getLastName());
        restCustomerDto.setEmail(customer.getEmail());
        restCustomerDto.setPhone(customer.getPhone());

        return restCustomerDto;
    }
}
