/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.api;

import java.util.ArrayList;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import pl.setblack.airomem.direct.banksample.domain.Account;
import pl.setblack.airomem.direct.banksample.domain.Bank;

/**
 *
 */
public class BankAdapter extends XmlAdapter<BankDto, Bank> {

    @Override
    public Bank unmarshal(BankDto v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BankDto marshal(Bank v) throws Exception {
        final AccountAdapter adapter = new AccountAdapter();
        final BankDto bank = new BankDto();
        final ArrayList<AccountDto> accounts = new ArrayList<>();
        for (final Account acc : v.getAllAccounts()) {
            accounts.add(adapter.marshal(acc));
        }
        bank.setAccounts(accounts);
        return bank;
    }

}
