package com.example.easybank.application.service;

import com.example.easybank.domain.entity.Card;

public interface CardService {
    public Card create() throws Exception;
    public Card findMyOwnCard(Long id) throws Exception;
}
