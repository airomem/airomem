/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import pl.setblack.airomem.direct.banksample.api.AccountAdapter;

/**
 *
 */
@XmlJavaTypeAdapter(AccountAdapter.class)
public final class Account implements Serializable {

    public final String id;

    public final BigDecimal value;

    Account(final String id, final BigDecimal value) {
        this.id = id;
        this.value = value;
    }

    Account change(final BigDecimal change) {
        return new Account(this.id, value.add(change));
    }

}
