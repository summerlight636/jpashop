package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);//Ctrl+Space 누르면 자동완성 도와줌

        //주문 저장
        orderRepository.save(order); //order만 persist해도 cascade 덕분에 delivery, orderItems 모두 같이 persist
        // order가 orderItems를 관리하고 order가 delivery를 관리하기 때문에 cascade걸어준 것(delivery와 orderItem은 order말고 다른 데에서 안 씀)
        // 그렇기 때문에 deliveryRepository와 orderItemRepository를 따로 만들지 않아도 되는 것 (repository에서 save와 findOne 같은 메서드를 정의하니까. 즉 해당 엔티티의 등록과 삭제가 불가능해짐)

        return order.getId();
    }

    //취소
    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
/*
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
*/


}
