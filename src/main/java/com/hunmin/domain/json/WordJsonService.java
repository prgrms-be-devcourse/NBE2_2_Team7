//package com.hunmin.domain.json;
//
//import com.hunmin.domain.dto.word.WordRequestDTO;
//import com.hunmin.domain.dto.word.WordResponseDTO;
//import com.hunmin.domain.entity.Member;
//import com.hunmin.domain.entity.MemberRole;
//import com.hunmin.domain.entity.Word;
//import com.hunmin.domain.exception.WordCustomException;
//import com.hunmin.domain.exception.WordException;
//import com.hunmin.domain.json.WordsJsonReader;
//import com.hunmin.domain.repository.MemberRepository;
//import com.hunmin.domain.repository.WordRepository;
//import jakarta.annotation.PostConstruct;
//import java.util.List;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//public class WordJsonService {
//    private final WordRepository wordRepository;
//    private final MemberRepository memberRepository;
//    private final WordsJsonReader wordsJsonReader;
//
//    @PostConstruct
//    public void init() {
//        try {
//            saveWordsFromJson();    // 애플리케이션 시작 시 JSON 파일에서 단어 로드
//        } catch (Exception e) {
//            log.error("에러" + e);
//        }
//    }
//
//    private String fetchWordDescriptionFromApi(String title) {
//        String apiKey = "15DDC50197E7362476426D27190A480A";  // 발급받은 인증키로 변경
//        String url = "https://api.example.com/search?word=" + title + "&key=" + apiKey;
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(url);
//            HttpResponse response = httpClient.execute(request);
//
//            if (response.getStatusLine().getStatusCode() == 200) {
//                String jsonResponse = EntityUtils.toString(response.getEntity());
//                JSONObject jsonObject = new JSONObject(jsonResponse);
//                return jsonObject.getString("meaning"); // API 응답에 맞춰 수정
//            } else {
//                log.error("Failed to fetch word description: {}", response.getStatusLine());
//                throw new WordCustomException(WordException.WORD_NOT_REGISTERED);
//            }
//        } catch (Exception e) {
//            log.error("Error fetching word description from API", e);
//            throw new WordCustomException(WordException.WORD_NOT_REGISTERED);
//        }
//    }
//
//    public WordResponseDTO createWord(WordRequestDTO wordRequestDTO) {
//        Optional<Member> foundMember = memberRepository.findById(wordRequestDTO.getMemberId());
//        Member member = foundMember.orElseThrow(() -> new WordCustomException(WordException.MEMBER_NOT_FOUND));
//
//        if (member.getMemberRole() == MemberRole.USER) {
//            throw new WordCustomException(WordException.MEMBER_NOT_ADMIN);
//        }
//
//        String definition = fetchWordDescriptionFromApi(wordRequestDTO.getTitle());
//        wordRequestDTO.setDefinition(definition); // DTO에 의미 추가
//
//        // 단어를 데이터베이스에 저장
//        Word word = wordRequestDTO.toEntity();
//        wordRepository.save(word);
//        return new WordResponseDTO(word);
//    }
//
//    // JSON 파일에서 단어 저장
//    public void saveWordsFromJson() {
//        wordsJsonReader.readJsonFiles(); // JSON 파일 읽기
//        List<WordResponseDTO> wordResponseDTOs = wordsJsonReader.convertJsonToDto(); // DTO 변환
//
//        for (WordResponseDTO dto : wordResponseDTOs) {
//            // 이미 존재하는 단어인지 체크
//            if (!wordRepository.findByTitleAndLang(dto.getTitle(), dto.getLang()).isPresent()) {
//                Word word = dto.toEntity(); // DTO를 엔티티로 변환
//                wordRepository.save(word); // 데이터베이스에 저장
//            } else {
//                log.warn("Word already exists for title: {} and lang: {}", dto.getTitle(), dto.getLang());
//            }
//        }
//    }
//}
