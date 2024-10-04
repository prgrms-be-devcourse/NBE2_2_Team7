//package com.hunmin.domain.json;
//
//import com.hunmin.domain.dto.word.WordResponseDTO;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import org.apache.commons.io.IOUtils;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import java.util.ArrayList;
//import java.util.List;
//import org.springframework.stereotype.Component;
//
//@Component
//public class WordsJsonReader {
//
//    // 모든 JSON 파일의 내용을 저장할 리스트
//    private List<JSONObject> jsonObjects = new ArrayList<>();
//
//    // JSON 파일들을 읽어오는 메서드
//    public void readJsonFiles() {
//        // 읽어올 JSON 파일 이름 배열
//        String[] fileNames = {
//                "wordsjson/1_5000_20240912.json",
//                 ///*
//                "wordsjson/2_5000_20240912.json",
//                "wordsjson/3_5000_20240912.json",
//                "wordsjson/4_5000_20240912.json",
//                "wordsjson/5_5000_20240912.json",
//                "wordsjson/6_5000_20240912.json",
//                "wordsjson/7_5000_20240912.json",
//                "wordsjson/8_5000_20240912.json",
//                "wordsjson/9_5000_20240912.json",
//                "wordsjson/10_5000_20240912.json",
//                "wordsjson/11_1952_20240912.json"
//                //*/
//        };
//
//        // 각 파일을 순회하며 JSON 객체 읽음
//        for (String fileName : fileNames) {
//            JSONObject jsonObject = readJsonFile(fileName);     // JSON 파일 읽기
//            if (jsonObject != null) {
//                jsonObjects.add(jsonObject);    // 읽은 JSON 객체를 리스트에 추가
//                System.out.println("Successfully loaded JSON file: " + fileName);   // 성공 메세지 출력
//            }
//        }
//
//        // 읽어온 JSON 객체 수 출력
//        System.out.println("Loaded JSON objects count: " + jsonObjects.size());
//
//        // JSON 데이터를 DTO 리스트로 변환
//        List<WordResponseDTO> wordResponseDTOs = convertJsonToDto();
//
//        // 변환된 DTO 개수 출력
//        System.out.println("Converted DTOs count: " + wordResponseDTOs.size());
//    }
//
//    // 주어진 파일 이름의 JSON 파일을 읽어오는 메서드
//    private JSONObject readJsonFile(String fileName) {
//        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
//            // 입력 스트림이 null이 아니면 JSON 텍스트를 읽음
//            if (inputStream != null) {
//                String jsonText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//                return new JSONObject(jsonText);    // JSON 객체 반환
//            } else {
//                System.err.println("File not found: " + fileName);  // 파일을 찾을 수 없을 때 에러 메세지
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();    // 예외 발생 시 스택 트레이스 출력
//            return null;
//        }
//    }
//
//    // Json 데이터 추출
//    public List<WordResponseDTO> convertJsonToDto() {
//        List<WordResponseDTO> wordResponseDTOs = new ArrayList<>(); // 변환된 DTO 리스트 초기화
//
//        // 처리할 언어 리스트
//        Set<String> targetLangs = new HashSet<>(Arrays.asList("영어", "일본어", "중국어", "베트남어", "프랑스어")); // 필요한 언어만 추가
//
//        // JSON 객체들을 순회
//        for (JSONObject jsonObject : jsonObjects) {
//            // "LexicalResource" 키가 있는지 확인
//            if (jsonObject.has("LexicalResource")) {
//                JSONObject lexicalResource = jsonObject.getJSONObject("LexicalResource"); // LexicalResource 객체 추출
//
//                // "Lexicon" 키가 있는지 확인
//                if (lexicalResource.has("Lexicon")) {
//                    JSONObject lexicon = lexicalResource.getJSONObject("Lexicon"); // Lexicon 객체 추출
//                    JSONArray lexicalEntries = lexicon.getJSONArray("LexicalEntry"); // LexicalEntry 배열 가져오기
//
//                    // 각 LexicalEntry를 순회
//                    for (int i = 0; i < lexicalEntries.length(); i++) {
//                        JSONObject entry = lexicalEntries.getJSONObject(i); // 현재 LexicalEntry 객체 추출
//                        String title = null; // 표제어 초기화
//
//                        // "feat" 키가 있는지 확인
//                        if (entry.has("feat")) {
//                            Object featObj = entry.get("feat"); // feat 객체 추출
//                            String partOfSpeech = null; // 품사 초기화
//
//                            // feat가 JSONArray인 경우
//                            if (featObj instanceof JSONArray) {
//                                JSONArray feats = (JSONArray) featObj; // JSONArray로 캐스팅
//                                for (int j = 0; j < feats.length(); j++) {
//                                    JSONObject feat = feats.getJSONObject(j); // 각 feat 객체 추출
//                                    if ("partOfSpeech".equals(feat.getString("att"))) { // 품사 확인
//                                        partOfSpeech = feat.getString("val"); // 품사 값 저장
//                                        break; // 찾으면 반복 종료
//                                    }
//                                }
//                            }
//                            // feat가 JSONObject인 경우
//                            else if (featObj instanceof JSONObject) {
//                                JSONObject feat = (JSONObject) featObj; // JSONObject로 캐스팅
//                                if ("partOfSpeech".equals(feat.getString("att"))) {
//                                    partOfSpeech = feat.getString("val"); // 품사 값 저장
//                                }
//                            }
//
//                            // 품사가 "명사"일 경우
//                            if ("명사".equals(partOfSpeech)) {
//                                if (entry.has("Lemma")) {
//                                    Object lemmaObj = entry.get("Lemma"); // Lemma 객체 추출
//                                    if (lemmaObj instanceof JSONObject) {
//                                        JSONObject lemma = (JSONObject) lemmaObj; // JSONObject로 캐스팅
//                                        if (lemma.has("feat")) {
//                                            Object lemmaFeatObj = lemma.get("feat"); // Lemma의 feat 추출
//                                            if (lemmaFeatObj instanceof JSONArray) {
//                                                JSONArray lemmaFeats = (JSONArray) lemmaFeatObj; // JSONArray로 캐스팅
//                                                for (int k = 0; k < lemmaFeats.length(); k++) {
//                                                    JSONObject lemmaFeat = lemmaFeats.getJSONObject(k); // 각 feat 객체 추출
//                                                    if ("writtenForm".equals(
//                                                            lemmaFeat.getString("att"))) { // "writtenForm" 확인
//                                                        title = lemmaFeat.getString("val"); // 표제어 값 저장
//                                                        break; // 찾으면 반복 종료
//                                                    }
//                                                }
//                                            } else if (lemmaFeatObj instanceof JSONObject) {
//                                                JSONObject lemmaFeat = (JSONObject) lemmaFeatObj; // JSONObject로 캐스팅
//                                                if ("writtenForm".equals(lemmaFeat.getString("att"))) {
//                                                    title = lemmaFeat.getString("val"); // 표제어 값 저장
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    // 표제어가 null이 아닌 경우 출력
//                                    if (title != null) {
//                                        System.out.println("Processing noun: " + title);
//                                    } else {
//                                        System.out.println("No title found for entry: " + entry); // 표제어가 없을 경우
//                                    }
//
//                                    // "Sense" 정보 처리
//                                    if (entry.has("Sense")) {
//                                        Object senseObj = entry.get("Sense"); // Sense 객체 추출
//
//                                        // Sense가 JSONArray인 경우
//                                        if (senseObj instanceof JSONArray) {
//                                            JSONArray senses = (JSONArray) senseObj; // JSONArray로 캐스팅
//                                            for (int j = 0; j < senses.length(); j++) {
//                                                JSONObject sense = senses.getJSONObject(j); // 각 Sense 객체 추출
//
//                                                // "Equivalent" 정보 처리
//                                                if (sense.has("Equivalent")) {
//                                                    Object equivalentObj = sense.get("Equivalent"); // Equivalent 객체 추출
//
//                                                    // Equivalent가 JSONArray인 경우
//                                                    if (equivalentObj instanceof JSONArray) {
//                                                        JSONArray equivalents = (JSONArray) equivalentObj; // JSONArray로 캐스팅
//                                                        for (int k = 0; k < equivalents.length(); k++) {
//                                                            JSONObject equivalent = equivalents.getJSONObject(
//                                                                    k); // 각 Equivalent 객체 추출
//                                                            String lang = null; // 언어 초기화
//                                                            String translation = null; // 번역 초기화
//                                                            String definition = null; // 정의 초기화
//
//                                                            if (equivalent.has("feat")) {
//                                                                Object featObjEquivalent = equivalent.get(
//                                                                        "feat"); // feat 추출
//
//                                                                // "feat"가 JSONArray인 경우
//                                                                if (featObjEquivalent instanceof JSONArray) {
//                                                                    JSONArray equivalentFeats = (JSONArray) featObjEquivalent; // JSONArray로 캐스팅
//                                                                    for (int l = 0; l < equivalentFeats.length(); l++) {
//                                                                        JSONObject feat = equivalentFeats.getJSONObject(
//                                                                                l); // 각 feat 객체 추출
//
//                                                                        // 언어가 있는지 체크
//                                                                        if ("language".equals(feat.getString("att"))) {
//                                                                            lang = feat.getString("val"); // 언어 값 저장
//                                                                        }
//                                                                        // 번역 및 정의 저장
//                                                                        if ("lemma".equals(feat.getString("att"))) {
//                                                                            translation = feat.getString("val");
//                                                                        }
//                                                                        if ("definition".equals(
//                                                                                feat.getString("att"))) {
//                                                                            definition = feat.getString("val");
//                                                                        }
//                                                                    }
//
//                                                                    // 특정 언어일 경우 DTO 생성
//                                                                    if (lang != null && targetLangs.contains(lang)
//                                                                            && title != null) {
//                                                                        WordResponseDTO dto = new WordResponseDTO(); // DTO 생성
//                                                                        dto.setTitle(title); // 제목 설정
//                                                                        dto.setLang(lang); // 언어 설정
//                                                                        dto.setTranslation(translation); // 번역 설정
//                                                                        dto.setDefinition(definition); // 정의 설정
//                                                                        wordResponseDTOs.add(dto); // 리스트에 추가
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("Converted DTOs count: " + wordResponseDTOs.size()); // 변환된 DTO의 개수 출력
//        return wordResponseDTOs; // 변환된 DTO 리스트 반환
//    }
//}
