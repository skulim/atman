package com.intive.atman.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountNotFoundException;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.LackOfFundsException;
import com.intive.atman.exception.LockTimeoutException;
import com.intive.atman.exception.TransactionOperationException;
import com.intive.atman.repository.AccountStorage;
import com.intive.atman.service.AccountService;
import com.intive.atman.service.TransferService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferServiceImplTest {

    @Autowired
    TransferService transferService;

    @Autowired
    AccountService accountService;

    @Mock
    private AccountStorage accountStorageMock;

    @Test
    public void testGetOfExistingAccount() {
        String accountNo = "1";
        AccountDTO testAccountDTO = prepareAccountDTO(accountNo, BigDecimal.valueOf(10000));
        accountService.initAccount(testAccountDTO);

        try {
            AccountWithHistoryDTO accountDTO = accountService.getAccount(accountNo);
            Assertions.assertThat(accountDTO.getAccountDTO()).isEqualTo(testAccountDTO);
        } catch (AccountOperationException ex) {
            fail("Exception occured");
        }
    }

    @Test
    public void testGetOfNonExistingAccount() {
        try {
            accountService.getAccount("Non existing");
            fail("Exception should occure");
        } catch (AccountOperationException ex) {
            assertTrue(ex instanceof AccountNotFoundException);
        }
    }

    @Test
    public void testGetExistingAccountWithTransactionHistory() {
        String accountNo1 = "1";
        String accountNo2 = "2";
        BigDecimal transactionAmount = BigDecimal.valueOf(5000);
        BigDecimal initAccountBalance = BigDecimal.valueOf(10000);
        AccountDTO accountDTO = prepareAccountDTO(accountNo1, initAccountBalance);
        accountService.initAccount(accountDTO);
        accountDTO = prepareAccountDTO(accountNo2, initAccountBalance);
        accountService.initAccount(accountDTO);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(accountNo1);
        transactionDTO.setTargetAccountNo(accountNo2);
        transactionDTO.setAmount(transactionAmount);
        
        try {
            transferService.transferMoney(transactionDTO);
            
            accountDTO = accountService.getAccount(accountNo1).getAccountDTO();
            Assertions.assertThat(accountDTO.getBalance().compareTo(initAccountBalance.subtract(transactionAmount))).isEqualTo(0);
            accountDTO = accountService.getAccount(accountNo2).getAccountDTO();
            Assertions.assertThat(accountDTO.getBalance().compareTo(initAccountBalance.add(transactionAmount))).isEqualTo(0);
        } catch (AccountOperationException | TransactionOperationException ex) {
            fail("Exception occured");
        }
    }

    @Test
    public void testTransferMoneyBetweenTheSameAccountShouldThrowException() {
        String accountNo1 = "1";
        BigDecimal transactionAmount = BigDecimal.valueOf(5000);
        BigDecimal initAccountBalance = BigDecimal.valueOf(10000);
        AccountDTO testAccountDTO = prepareAccountDTO(accountNo1, initAccountBalance);
        accountService.initAccount(testAccountDTO);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(accountNo1);
        transactionDTO.setTargetAccountNo(accountNo1);
        transactionDTO.setAmount(transactionAmount);

        try {
            transferService.transferMoney(transactionDTO);

            fail("Exception should occure");
        } catch (AccountOperationException | TransactionOperationException ex) {
            assertTrue(ex instanceof TransactionOperationException);
        }
    }

    @Test
    public void testTransferMoneyFromAccountWithNotEnoughtFundShouldThrowException() {
        String accountNo1 = "1";
        String accountNo2 = "2";
        BigDecimal transactionAmount = BigDecimal.valueOf(15000);
        BigDecimal initAccountBalance = BigDecimal.valueOf(10000);
        AccountDTO accountDTO = prepareAccountDTO(accountNo1, initAccountBalance);
        accountService.initAccount(accountDTO);
        accountDTO = prepareAccountDTO(accountNo2, initAccountBalance);
        accountService.initAccount(accountDTO);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(accountNo1);
        transactionDTO.setTargetAccountNo(accountNo2);
        transactionDTO.setAmount(transactionAmount);

        try {
            transferService.transferMoney(transactionDTO);

            fail("Exception should occure");
        } catch (AccountOperationException | TransactionOperationException ex) {
            assertTrue(ex instanceof LackOfFundsException);
        }
    }

    @Test
    public void testTransferMoneyWhenSourceAccountDoesnotExistShouldThrowException() {
        String accountNo2 = "2";
        BigDecimal transactionAmount = BigDecimal.valueOf(5000);
        BigDecimal initAccountBalance = BigDecimal.valueOf(10000);
        AccountDTO accountDTO = prepareAccountDTO(accountNo2, initAccountBalance);
        accountService.initAccount(accountDTO);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo("not exist");
        transactionDTO.setTargetAccountNo(accountNo2);
        transactionDTO.setAmount(transactionAmount);

        try {
            transferService.transferMoney(transactionDTO);

            fail("Exception should occure");
        } catch (AccountOperationException | TransactionOperationException ex) {
            assertTrue(ex instanceof AccountNotFoundException);
        }
    }

    @Test
    public void testTransferMoneyWhenTargetAccountDoesnotExistShouldThrowException() {
        String accountNo1 = "1";
        BigDecimal transactionAmount = BigDecimal.valueOf(5000);
        BigDecimal initAccountBalance = BigDecimal.valueOf(10000);
        AccountDTO accountDTO = prepareAccountDTO(accountNo1, initAccountBalance);
        accountService.initAccount(accountDTO);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(accountNo1);
        transactionDTO.setTargetAccountNo("not exist");
        transactionDTO.setAmount(transactionAmount);

        try {
            transferService.transferMoney(transactionDTO);

            fail("Exception should occure");
        } catch (AccountOperationException | TransactionOperationException ex) {
            assertTrue(ex instanceof AccountNotFoundException);
        }
    }

    @Test
    public void testTransferMoneyWhenLockOfAccountCannotBeSetShouldThrowException() throws AccountOperationException {
        String accountNo1 = "1";
        String accountNo2 = "2";
        BigDecimal transactionAmount = BigDecimal.valueOf(5000);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber(accountNo1);
        accountDTO.setBalance(transactionAmount);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(accountNo1);
        transactionDTO.setTargetAccountNo(accountNo2);
        transactionDTO.setAmount(transactionAmount);

        TransferService service = new TransferServiceImpl(accountStorageMock, new ValidatorServiceImpl(accountStorageMock));

        when(accountStorageMock.getAccount(anyString())).thenReturn(accountDTO);
        when(accountStorageMock.lockAccount("1")).thenReturn(false);
        
        try {
            service.transferMoney(transactionDTO);

            fail("Exception should occure");
        } catch (AccountOperationException | TransactionOperationException ex) {
            assertTrue(ex instanceof LockTimeoutException);
        }
    }

    @Test
    public void testTransferForMultipleTransactions() {
        String accountNo1 = "1";
        String accountNo2 = "2";
        BigDecimal initialBalance = BigDecimal.valueOf(10);
        AccountDTO accountDTO = prepareAccountDTO(accountNo1, initialBalance);
        accountService.initAccount(accountDTO);
        accountDTO = prepareAccountDTO(accountNo2, initialBalance);
        accountService.initAccount(accountDTO);

        TransactionDTO transaction1DTO = new TransactionDTO();
        transaction1DTO.setSourceAccountNo(accountNo1);
        transaction1DTO.setTargetAccountNo(accountNo2);
        transaction1DTO.setAmount(BigDecimal.ONE);

        TransactionDTO transaction2DTO = new TransactionDTO();
        transaction2DTO.setSourceAccountNo(accountNo2);
        transaction2DTO.setTargetAccountNo(accountNo1);
        transaction2DTO.setAmount(BigDecimal.ONE);

        try {
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    try {
                        transferService.transferMoney(transaction1DTO);
                    } catch (AccountOperationException | TransactionOperationException e) {
                        fail("Exception occured" + e.getMessage());
                    }
                }).start();
                new Thread(() -> {
                    try {
                        transferService.transferMoney(transaction2DTO);
                    } catch (AccountOperationException | TransactionOperationException e) {
                        fail("Exception occured" + e.getMessage());
                    }
                }).start();
            }

            Thread.sleep(2000);
            accountDTO = accountService.getAccount(accountNo2).getAccountDTO();
            Assertions.assertThat(accountDTO.getBalance().compareTo(initialBalance)).isEqualTo(0);
            accountDTO = accountService.getAccount(accountNo2).getAccountDTO();
            Assertions.assertThat(accountDTO.getBalance().compareTo(initialBalance)).isEqualTo(0);

        } catch (Exception ex) {
            fail("Exception occured");
        }
    }

    private AccountDTO prepareAccountDTO(String number, BigDecimal balance) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber(number);
        accountDTO.setBalance(balance);

        return accountDTO;
    }
}
