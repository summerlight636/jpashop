package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //Entity에 직접 넣은 이유: 데이터를 가지고 있는 쪽이 비즈니스 메서드를 갖고 있는 것이 응집력 면에서 좋다.
    //stockQuantity 를 변경해야 할 일이 있으면 setter를 가지고 stockQuantity를 가져와서 바깥에서 계산해서 넣는 것이 아니라,
    //그냥 이 안에서 addStock, removeStock을 이용해서 바꾸어 주어야 한다.
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity -= quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
