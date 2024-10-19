package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String location;

    private Double latitude;

    private Double longitude;

    @ElementCollection
    @CollectionTable(name = "board_image_urls", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "image_urls", columnDefinition = "TEXT", nullable = false)
    private List<String> imageUrls;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> comments;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeLocation(String location) {
        this.location = location;
    }

    public void changeLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void changeLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void changeImgUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Board(Long boardId, Member member, String title, String content) {
        this.boardId = boardId;
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
