package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    private Long id; //상품 수정이 있기 때문에 id값이 꼭 필요

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
