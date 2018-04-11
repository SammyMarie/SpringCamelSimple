package com.sammy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustInfo {

    private String firstName;
    private String lastName;
    private String nationalId;
    private String occupation;
    private String infoType;
    private int age;
}