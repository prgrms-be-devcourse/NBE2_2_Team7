package com.hunmin.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatMessages is a Querydsl query type for ChatMessages
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatMessages extends EntityPathBase<ChatMessages> {

    private static final long serialVersionUID = -189699563L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatMessages chatMessages = new QChatMessages("chatMessages");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Long> chatMessageId = createNumber("chatMessageId", Long.class);

    public final QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath isRead = createBoolean("isRead");

    public final QMember member;

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChatMessages(String variable) {
        this(ChatMessages.class, forVariable(variable), INITS);
    }

    public QChatMessages(Path<? extends ChatMessages> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatMessages(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatMessages(PathMetadata metadata, PathInits inits) {
        this(ChatMessages.class, metadata, inits);
    }

    public QChatMessages(Class<? extends ChatMessages> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

