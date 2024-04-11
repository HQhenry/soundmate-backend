package com.edu.basaoc.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Setter
    @Getter
    @Column(name = "chat_id")
    private String chatId;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_fk", referencedColumnName = "profile_id")
    private Profile sender;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient_fk", referencedColumnName = "profile_id")
    private Profile recipient;


}
