package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.RecipientDto;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipientDtoToRecipient implements Converter<RecipientDto, Recipient> {

    private RecipientService recipientService;

    @Override
    public Recipient convert(RecipientDto recipientDto) {

        Recipient recipient = (recipientDto.getId() != null ? recipientService.get(recipientDto.getId()) : new Recipient());

        recipient.setAccountNumber(recipientDto.getAccountNumber());
        recipient.setName(recipientDto.getName());
        recipient.setDescription(recipientDto.getDescription());

        return recipient;
    }

    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }
}
