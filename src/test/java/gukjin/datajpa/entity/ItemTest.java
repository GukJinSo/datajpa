package gukjin.datajpa.entity;

import gukjin.datajpa.repository.ItemRepository;
import gukjin.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@Rollback(value = false)
@SpringBootTest
class ItemTest {

    @Autowired private ItemRepository itemRepository;
    @PersistenceContext EntityManager em;

    @Test
    @Transactional
    public void persistOrMergeTest() throws Exception {

        Item item = new Item("A");
        System.out.println("before persist = " +item.isNew()); // true
        itemRepository.save(item);
        em.flush();
        System.out.println("after persist = " + item.isNew()); // false

        em.clear();

        Item findItem = em.find(Item.class, "A");
        System.out.println("postLoaded = " + findItem.isNew());; // false

    }
}