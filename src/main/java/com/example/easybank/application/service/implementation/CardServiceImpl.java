package com.example.easybank.application.service.implementation;

import com.example.easybank.application.service.CardService;
import com.example.easybank.domain.CreditCardData;
import com.example.easybank.domain.entity.Card;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.CardRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import com.example.easybank.util.generator.RandomCreditCardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final RandomCreditCardGenerator randomCreditCardGenerator;

    @Override
    public Card create() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        if(!user.getCards().isEmpty()) {
            throw new AlreadyExistsException("Card already exists");
        }

        CreditCardData cardData = randomCreditCardGenerator.generate();

        Card card = Card.builder()
                .cardNumber(cardData.cardNumber())
                .cvv(cardData.cvv())
                .expiryDate(cardData.expiration().toString())
                .account(user.getAccounts().getFirst())
                .user(user)
                .build();

        return cardRepository.save(card);
    }
}
