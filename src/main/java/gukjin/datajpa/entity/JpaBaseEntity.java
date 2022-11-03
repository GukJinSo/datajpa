package gukjin.datajpa.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass // jpa 상속관계가 아닌, 필드만 상속받겠다
@Getter
public abstract class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist // persist 전의 이벤트 발생
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        updateDate = LocalDateTime.now();
    }
}
