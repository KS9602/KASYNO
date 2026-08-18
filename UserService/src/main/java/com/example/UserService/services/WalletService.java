package com.example.UserService.services;

import com.example.UserService.entities.UserModel;
import com.example.UserService.entities.WalletModel;
import com.example.UserService.enums.WalletCurrency;
import com.example.UserService.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public void createWallet(UserModel userModel){
        log.info("Creating wallet for user {}", userModel);
        WalletModel walletModel = new WalletModel();
        walletModel.setUserId(userModel.getUserId());
        walletModel.setCurrency(WalletCurrency.PLN);
        walletRepository.saveAndFlush(walletModel);
        log.info("Wallet created: {}", walletModel);
    }

}
