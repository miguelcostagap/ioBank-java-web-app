package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.RecipientDto;
import io.codeforall.thestudio.javabank.model.Recipient;
import org.springframework.stereotype.Component;

@Component
public class RecipientToRecipientDto extends AbstractConverter<Recipient, RecipientDto> {

    @Override
    public RecipientDto convert(Recipient recipient) {

        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setId(recipient.getId());
        recipientDto.setAccountNumber(recipient.getAccountNumber());
        recipientDto.setName(recipient.getName());
        recipientDto.setDescription(recipient.getDescription());

        return recipientDto;
    }
}
