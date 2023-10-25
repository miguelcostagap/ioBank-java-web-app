package io.codeforall.thestudio.javabank.services.mock;

import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.services.RecipientService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("mock")
public class RecipientServiceMock implements RecipientService {

    private Map<Integer, Recipient> recipients = new HashMap<>();

    protected Integer getNextId() {
        return recipients.isEmpty() ? 1 : Collections.max(recipients.keySet()) + 1;
    }

    @Override
    public Recipient get(Integer id) {

        return recipients.get(id);
    }
}
