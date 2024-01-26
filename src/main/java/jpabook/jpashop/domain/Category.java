package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();
    //다 대 다 관계를 예제상으로 보여주기 위함
    //실제로는 다 대 다 관계는 추천하지 않음. 이대로 매핑하고 끝이기떄문
    //실제로는 다른 데이터를 추가하던가 이외의 상황이 많음.
    //중간에 테이블이 매핑되어있어야 함.


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; //부모
    //죽 내려오는 형태인데 어떻게 구현해야하지? 트리구조

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); //자녀

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
    //child에 집어넣으면 양쪽으로 다 들어가야함.


}
