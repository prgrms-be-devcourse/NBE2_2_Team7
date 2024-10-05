package com.hunmin.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.dto.chat.ChatRoomRequestDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@Log4j2
public class RedisConfig {
    @Bean
    public ChannelTopic topicPattern() {

        return new ChannelTopic("chatRoom");
    }
    //클라이언트로 부터 메세지 수신
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory
                                                            ,MessageListenerAdapter listenerAdapter,
                                                              ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }
    //클라이언트로 부터 메세지 수신
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }
    // chatRoom 직렬화
    @Bean
    public RedisTemplate<String, ChatRoomRequestDTO> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, ChatRoomRequestDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // GenericJackson2JsonRedisSerializer 설정
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Key Serializer 설정
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value Serializer 설정
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public HashOperations<String, String, ChatRoomRequestDTO> hashOperations(RedisTemplate<String, ChatRoomRequestDTO> redisTemplate) {
        return redisTemplate.opsForHash();
    }
}
