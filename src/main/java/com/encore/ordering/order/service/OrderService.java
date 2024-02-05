package com.encore.ordering.order.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.repository.ItemRepository;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.repository.MemberRepository;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.repository.OrderRepository;
import com.encore.ordering.order_item.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService
{
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, MemberRepository memberRepository)
    {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    public Ordering create(OrderReqDto orderReqDto)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Member does not exist"));
        Ordering ordering = Ordering.builder().member(member).build();

        for (OrderReqDto.OrderReqItemDto dto :orderReqDto.getOrderReqItemDtos())
        {
            Item item = itemRepository.findById(dto.getItemId()).orElseThrow(()->new EntityNotFoundException("Item does not exist"));
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .quantity(dto.getCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);

            int cal = item.getStockQuantity()-dto.getCount();
            if (cal < 0)
                throw new IllegalArgumentException("재고없음");
            else
                orderItem.getItem().updateStockQuantity(cal);
        }
        return orderRepository.save(ordering);
    }

    public Ordering cancel(Long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
//        List<GrantedAuthority> role_list = (List<GrantedAuthority>) authentication.getAuthorities();
//        System.out.println(role_list.get(0));
//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Member does not exist"));
        Ordering ordering = orderRepository.findById(id).orElseThrow(() ->new EntityNotFoundException("Member can not found"));
//        authentication.getAuthorities().contains("ROLE_ADMIN");
        if (ordering.getMember().getEmail().equals(email) || authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN")))
        )
        {
            if (ordering.getOrderStatus().toString().equals("CANCELED")) throw new IllegalArgumentException("이미 취소된 주문입니다.");
            for (OrderItem orderItem : ordering.getOrderItems())
                orderItem.getItem().updateStockQuantity(orderItem.getQuantity() + orderItem.getItem().getStockQuantity());
            ordering.cancelOrder();
        }
        else
            throw new AccessDeniedException("접근 제한");
        return ordering;
    }

    public List<OrderResDto> findAll()
    {
        List<Ordering> orderings = orderRepository.findAll();
        return orderings.stream()
                .map(o -> OrderResDto.toDto(o))
                .collect(Collectors.toList());
    }

    public List<OrderResDto> findByMember(Long id)
    {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 유저"));
        List<Ordering>orderings = member.getOrderings();
        return orderings.stream().map(o -> OrderResDto.toDto(o)).collect(Collectors.toList());

    }

    public List<OrderResDto> myOrders()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 유저"));
        List<Ordering> orderings = member.getOrderings();
        return orderings.stream().map(ordering -> OrderResDto.toDto(ordering)).collect(Collectors.toList());

    }
}
