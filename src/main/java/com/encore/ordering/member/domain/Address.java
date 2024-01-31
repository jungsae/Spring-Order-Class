package com.encore.ordering.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable //다른객체에 임베딩 가능
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address
{
    private String city;
    private String street;
    private String zipcode;
}
