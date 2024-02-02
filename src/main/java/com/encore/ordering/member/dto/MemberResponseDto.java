package com.encore.ordering.member.dto;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto
{
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;

    public static MemberResponseDto toMemberResponseDto(Member member)
    {
        MemberResponseDtoBuilder builder = MemberResponseDto.builder();
        builder.name(member.getName());
        builder.email(member.getEmail());
        builder.id(member.getId());
        builder.orderCount(member.getOrderings().size());
        Address address = member.getAddress();
        if (address!=null)
        {
            builder.city(member.getAddress().getCity());
            builder.zipcode(member.getAddress().getZipcode());
            builder.street(member.getAddress().getStreet());
        }
        return builder.build();
    }
}
