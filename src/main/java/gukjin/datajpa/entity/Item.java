package gukjin.datajpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
    @Id private String id;

    public Item(String id){
        this.id = id;
    }

    @Transient // 컬럼 매핑 제외
    @JsonIgnore // 혹시 모를 직렬화 제외
    private boolean isNew = true;

    @Override // 검사
    public boolean isNew() {
        return isNew;
    }

    @PostPersist // Persist 한 다음 isNew = false
    @PostLoad // find되어 1차 캐시에 저장된 다음 isNew = false
    public void modifyIsNew(){
        this.isNew = false;
    }
}
