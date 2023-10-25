package io.codeforall.thestudio.javabank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

public class AccountTransactionDto {

    @NotNull(message = "Id is mandatory")
    private Integer id;

    @NumberFormat
    @NotBlank(message = "An initial amount is mandatory")
    @NotNull(message = "Amount is mandatory")
    private String amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
