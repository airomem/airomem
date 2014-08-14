/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.api;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import pl.setblack.airomem.direct.banksample.domain.Account;

/**
 *
 */
public class AccountAdapter extends XmlAdapter<AccountDto, Account> {

    @Override
    public Account unmarshal(AccountDto v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AccountDto marshal(Account v) throws Exception {
        System.out.println("marschalling");
        final AccountDto acc = new AccountDto();
        acc.setId(v.id);
        acc.setValue(v.value);
        return acc;
    }

}
