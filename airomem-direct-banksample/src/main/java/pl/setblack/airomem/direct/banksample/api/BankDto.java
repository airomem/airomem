/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.api;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement
public class BankDto {

    private List<AccountDto> accounts;

    @XmlElement
    public List<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }

}
