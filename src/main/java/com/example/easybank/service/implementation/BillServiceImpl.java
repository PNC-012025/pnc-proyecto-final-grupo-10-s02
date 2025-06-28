package com.example.easybank.service.implementation;

import com.example.easybank.domain.dto.request.BillRequestDTO;
import com.example.easybank.domain.dto.response.BillResponseDTO;
import com.example.easybank.domain.mapper.BillMapper;
import com.example.easybank.exception.*;
import com.example.easybank.service.BillService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.repository.AccountRepository;
import com.example.easybank.repository.BillRepository;
import com.example.easybank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public void save(BillRequestDTO billRequestDTO) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        if (billRequestDTO.getId() != null) {
            if (!billRequestDTO.getId().equals(user.getId())) {
                throw new AccessDeniedException("Insufficient funds");
            }
        }

        if (billRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        Bill bill = BillMapper.toEntity(billRequestDTO);
        bill.setUser(user);
        bill.setState("PENDING");

        billRepository.save(bill);
    }

    @Override
    public List<BillResponseDTO> getAllMyBills() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        return user.getBills()
                .stream()
                .map(bill -> {
                    if (bill.getState().equals("PENDING")) {
                        return BillMapper.toDTO(bill);
                    }
                    return null;
                }).toList();
    }

    @Override
    public void delete(UUID id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Bill not found"));

        // TODO: CREAR EXCEPCION PARA ESTO
        if (!bill.getUser().getUsername().equals(username)) {
            throw new ModelNotFoundException("User not logged in");
        }

        billRepository.delete(bill);
    }

    @Override
    public void payBill(UUID id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Bill not found"));

        Account account = bill.getUser().getAccounts().getFirst();

        BigDecimal billAmount = bill.getAmount();
        BigDecimal accountBalance = account.getBalance();

        if (billAmount.compareTo(accountBalance) > 0) {
            throw new InsufficientFundsException("You don't have enough money");
        }

        if (!bill.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("User is not the owner of this bill");
        }

        if (bill.getState().equals("PAID")) {
            throw new BillAlreadyPaidException("Bill is already paid");
        }
        account.setBalance(account.getBalance().subtract(bill.getAmount()));
        bill.setState("PAID");

        billRepository.save(bill);
        accountRepository.save(account);
    }
}
