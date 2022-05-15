package com.example.backend.member;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    private static String client_id = "1417199790d5e442654d27578fe4e890";
    private static String google_client_id ="512592128492-b88aomr2gk1n6ivkbs8h2t0lc04e97ng.apps.googleusercontent.com";
    private static String redirect_uri = "http://j5c104.p.ssafy.io";

    @GetMapping("/kakao")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) throws CustomException {
        Map<String, Object> resultMap = new HashMap<>();

        String token = getKakaoToken(code);
        Map<String, String> userInfo = getKaKaoUserInfo(token);

        if(userInfo.get("email") == null){
            int id = cancelKakao(token);
            System.out.println(id);
            throw new CustomException(ErrorCode.NEED_EMAIL);
        }

        Optional<MemberDto> dto = memberService.getMemberByEmail(userInfo.get("email"));

        if(!dto.isPresent()){
            memberService.joinMember("KAKAO", userInfo.get("email"));
            dto = memberService.getMemberByEmail(userInfo.get("email"));
        }

        resultMap.put("status", HttpStatus.OK);
        resultMap.put("message", "카카오 유저 정보");
        resultMap.put("userInfo", dto);
        resultMap.put("token", token);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    private int cancelKakao(String token){
        int id = -1;
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            JsonObject idObject = element.getAsJsonObject().getAsJsonObject();

            id = element.getAsJsonObject().get("id").getAsInt();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    private String getKakaoToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        HttpURLConnection conn = null;

        try {
            URL url = new URL("https://kauth.kakao.com/oauth/token");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client_id);
            sb.append("&redirect_uri=" + redirect_uri);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public Map<String, String> getKaKaoUserInfo(String accessToken) {

        Map<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String id = element.getAsJsonObject().get("id").getAsString();
            String email = null;
            if (kakaoAccount.getAsJsonObject().get("email") != null) {
                email = kakaoAccount.getAsJsonObject().get("email").getAsString();
                userInfo.put("id", id);
                userInfo.put("email", email);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public Map<String, String> deleteToken(String accessToken) {

        Map<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }


    @GetMapping("/google/{idTokenString}")
    public ResponseEntity<Map<String, Object>> googleLogin(@PathVariable String idTokenString){
        Map<String, Object> resultMap = new HashMap<>();
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(google_client_id))
                .build();
        // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                System.out.println("email: " + email);
                // Use or store profile information
                if(emailVerified) {
                    Optional<MemberDto> dto = memberService.getMemberByEmail(email);
                    if (!dto.isPresent()) {
                        memberService.joinMember("GOOGLE", email);
                        dto = memberService.getMemberByEmail(email);
                    }
                    resultMap.put("message", "구글 유저 정보");
                    resultMap.put("User Dto", dto);

                    return new ResponseEntity<>(resultMap, HttpStatus.OK);
                }

            } else {
                resultMap.put("message","GOOGLE: Invalid ID token");
                return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, MemberDto memberDto) throws IOException, CustomException {
        Map<String, Object> resultMap = new HashMap<>();

        memberDto.setUid(id);
        MemberDto changedMemberDto = memberService.updateMember(memberDto);

        resultMap.put("message", "회원 정보 수정 완료");
        resultMap.put("userInfo", changedMemberDto);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) throws CustomException {
        Map<String, Object> resultMap = new HashMap<>();

        MemberService.deleteUser(id);

        resultMap.put("message", "회원 탈퇴 성공");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}