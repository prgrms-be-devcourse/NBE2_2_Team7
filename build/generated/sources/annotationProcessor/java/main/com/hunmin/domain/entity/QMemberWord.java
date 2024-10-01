package com.hunmin.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberWord is a Querydsl query type for MemberWord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberWord extends EntityPathBase<MemberWord> {

    private static final long serialVersionUID = -1904913835L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberWord memberWord = new QMemberWord("memberWord");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QMember member;

    public final NumberPath<Long> memberWordId = createNumber("memberWordId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QWord word;

    public QMemberWord(String variable) {
        this(MemberWord.class, forVariable(variable), INITS);
    }

    public QMemberWord(Path<? extends MemberWord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberWord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberWord(PathMetadata metadata, PathInits inits) {
        this(MemberWord.class, metadata, inits);
    }

    public QMemberWord(Class<? extends MemberWord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.word = inits.isInitialized("word") ? new QWord(forProperty("word")) : null;
    }

}

