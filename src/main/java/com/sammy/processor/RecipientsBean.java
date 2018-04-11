package com.sammy.processor;

import org.apache.camel.RecipientList;
import org.apache.camel.jsonpath.JsonPath;

public class RecipientsBean {

    @RecipientList
    public String[] recipients(@JsonPath("$..-infotype") String customerInfo){

        if(isLoanCustomer(customerInfo)){
            return new String[]{"{{activemq.accountQueue}}", "{{activemq.customerServices}}"};
        }else {
            return new String[]{"{{activemq.accountQueue}}"};
        }
    }

    private boolean isLoanCustomer(String customerInfo) {
        return customerInfo.equals("LoanCustomer");
    }
}