# NBE2_2_Team7
외국인 대상 한국어 학습 및 모임 서비스
<채팅구현>
-----------------------------------------------------------------
-의존성-

dependencies {
    // 실시간 채팅을 위한 필요 라이브러리들
    implementation 'org.springframework.boot:spring-boot-starter-websocket'
    implementation 'org.springframework.boot:spring-boot-starter-freemarker'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    implementation 'it.ozimov:embedded-redis:0.7.2'
    implementation 'org.webjars.bower:bootstrap:4.3.1'
    implementation 'org.webjars.bower:vue:2.5.16'
    implementation 'org.webjars.bower:axios:0.17.1'
    implementation 'org.webjars:sockjs-client:1.1.2'
    implementation 'org.webjars:stomp-websocket:2.3.3-1'
    implementation 'com.google.code.gson:gson:2.8.0'


    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    //implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    //compileOnly 'org.projectlombok:lombok'
    implementation 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    //testImplementation 'org.springframework.security:spring-security-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
-----------------------------------------------------------------

-application.properties-

spring.application.name=hunmin
spring.datasource.url=jdbc:mysql://localhost:3306/world
spring.datasource.username=root
spring.datasource.password=1111
# DDL generation none|validate|update|create|create-drop
spring.jpa.hibernate.ddl-auto=update
# SQL SHOW
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.springframework.security.web=TRACE

##Redis properties ??
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.database=0
spring.profiles.include=key

#?? freemaker ? ??? ??
spring.freemarker.template-loader-path=classpath:/templates/
spring.freemarker.suffix=.ftl
-----------------------------------------------------------------
-application.yml

spring:
  profiles:
    active: local
  devtools:
    livereload:
      enabled: true
    restart:
      enabled: false
    add-properties: false
  freemarker:
    cache: false
  jwt:
    secret: govlepel@$&
spring:
  profiles:
    active: local
  devtools:
    livereload:
      enabled: true
    restart:
      enabled: false
    add-properties: false
  freemarker:
    cache: false
  jwt:
    secret: govlepel@$&
-----------------------------------------------------------------
