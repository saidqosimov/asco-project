package com.ascoproject.ascoproject.entity;
/*users tabledagi barcha userlar telegram userlardir. ularning chat id lari bor. ba'zilari guruhlar. apiga murjaat qilinganda oddiy userning ma'lumotlarini list shaklida chiqarsin. guruhlar uchun alohida api chiqarilsin. pagenation qo'llanilsin. har bir pageda 10 ta user yoki 10 guruh bo'lsin. userlarni statusini o'zgartirish mumkin bo'lsin. */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id", length = 12, unique = true, nullable = false)
    @NotNull
    private Long chatId;
    @Column(name = "is_active", length = 5, nullable = false)
    @NotNull
    private Boolean isActive = true;
    @Column(name = "step", length = 30, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStep step;
    @Column(name = "lang", length = 3, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Languages lang;
    @Column(name = "three_days_ago", length = 5, nullable = false)
    @NotNull
    private Boolean threeDaysAgo = false;
    @Column(name = "two_days_ago", length = 5, nullable = false)
    @NotNull
    private Boolean twoDaysAgo = false;
    @Column(name = "the_day_before", length = 5, nullable = false)
    @NotNull
    private Boolean theDayBefore = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_tax_info",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tax_info_id", referencedColumnName = "id")}
    )
    private List<TaxInfoEntity> taxInfo = new LinkedList<>();
}

/*
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    step VARCHAR(30) NOT NULL,
    lang VARCHAR(3) NOT NULL,
    three_day_before BOOLEAN NOT NULL DEFAULT FALSE,
    two_day_before BOOLEAN NOT NULL DEFAULT FALSE,
    day_before BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE user_tax_info (
    user_id BIGINT NOT NULL,
    tax_info_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, tax_info_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tax_info_id) REFERENCES tax_info(id)
);

 */