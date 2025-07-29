package com.nexters.teamace.user.infrastructure;

import com.nexters.teamace.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends BaseEntity {

    private String username;

    private String nickname;

    public UserEntity(final Long id, final String username, final String nickname) {
        super(id);
        this.username = username;
        this.nickname = nickname;
    }
}
