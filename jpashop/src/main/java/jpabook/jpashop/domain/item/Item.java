package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 매핑 : 전략은 부모 클래스에서
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item { //구현체를 가질 것이기 때문에 추상 클래스로

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;


    //공통 속성들
    private String name;
    private int price;
    private int stockQuantity;


    private List<Category> categories = new ArrayList<>();
}
