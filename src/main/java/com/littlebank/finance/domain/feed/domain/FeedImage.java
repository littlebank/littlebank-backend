package com.littlebank.finance.domain.feed.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.security.cert.CertPathBuilder;

@Entity
@Table(name = "feed_image")
@Getter
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE feed_image SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name = "url")
    private String url; // 실제 접근 가능한 url
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

}
