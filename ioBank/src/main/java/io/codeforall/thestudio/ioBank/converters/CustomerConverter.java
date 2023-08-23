package io.codeforall.thestudio.ioBank.converters;

import io.codeforall.thestudio.ioBank.dto.AccountDTO;
import io.codeforall.thestudio.ioBank.dto.CustomerDTO;
import io.codeforall.thestudio.ioBank.model.Customer;
import io.codeforall.thestudio.ioBank.model.account.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerConverter {
    public CustomerDTO toDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setProfilePictureURL(customer.getProfilePictureURL());

        List<AccountDTO> dtosAccounts = new ArrayList<>();
        for (Account account : customer.getAccounts()) {
         AccountDTO accountDTO = new AccountDTO();
         accountDTO.setId(account.getId());
         accountDTO.setBalance(account.getBalance());
         dtosAccounts.add(accountDTO);
        }

        customerDTO.setAccountDTOS(dtosAccounts);

        return customerDTO;
    }

    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setId(customerDTO.getId());
        customer.setPhone(customerDTO.getPhone());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setProfilePictureURL(customerDTO.getProfilePictureURL());
        return customer;
    }

}
