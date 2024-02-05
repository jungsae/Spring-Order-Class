package com.encore.ordering.order.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController
{
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> orderCreate(@RequestBody OrderReqDto orderReqDto)
    {
        Ordering ordering = orderService.create(orderReqDto);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED, " Order Ok " ,ordering.getId()), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username") //컨트롤러 앞에서 잡기
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponse> orderCreate(@PathVariable Long id)
    {
        Ordering ordering = orderService.cancel(id);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED, " Order Canceled" ,ordering.getId()), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public List<OrderResDto> orders()
    {
        return orderService.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public List<OrderResDto> findMemberOrders(@PathVariable Long id)
    {
        return orderService.findByMember(id);
    }

    @GetMapping("/member/orders")
    public List<OrderResDto> myOrders()
    {
        return orderService.myOrders();
    }
}
