package com.hunmin.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
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
    // Object 직렬화
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
    // ChatMessageDTO 직렬화
    @Bean
    public RedisTemplate<String, ChatMessageDTO> redisTemplateMessage(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ChatMessageDTO> redisTemplateMessage = new RedisTemplate<>();
        redisTemplateMessage.setConnectionFactory(connectionFactory);
        redisTemplateMessage.setKeySerializer(new StringRedisSerializer());        // Key Serializer
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));      // Value Serializer

        return redisTemplateMessage;
    }
}
