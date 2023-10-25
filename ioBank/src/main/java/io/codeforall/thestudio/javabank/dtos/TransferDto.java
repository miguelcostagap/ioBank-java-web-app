package io.codeforall.thestudio.javabank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

public class TransferDto {

    @NotNull(message = "The id of the source account is mandatory\n")
    private Integer srcId;

    @NotNull(message = "The id of the destination account is mandatory\n")
    private Integer dstId;

    @NumberFormat
    @NotBlank(message = "An initial amount is mandatory\n")
    @NotNull(message = "Amount is mandatory")
    private String amount;

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public Integer getDstId() {
        return dstId;
    }

    public void setDstId(Integer dstId) {
        this.dstId = dstId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
