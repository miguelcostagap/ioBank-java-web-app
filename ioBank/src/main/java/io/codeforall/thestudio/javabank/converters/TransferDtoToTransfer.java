package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.domain.Transfer;
import io.codeforall.thestudio.javabank.dtos.TransferDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;


@Component
public class TransferDtoToTransfer implements Converter<TransferDto,Transfer> {

    @Override
    public Transfer convert(TransferDto transferDto) {

        Transfer transfer = new Transfer();

        transfer.setSrcId(transferDto.getSrcId());
        transfer.setDstId(transferDto.getDstId());
        transfer.setAmount(Double.parseDouble(transferDto.getAmount()));

        return transfer;
    }
}
