package com.example.backend.diary.Diary;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.UserAuth;
import com.example.backend.member.emotion.Genre;
import com.example.backend.member.emotion.UserEmotion;
import com.example.backend.member.emotion.UserEmotionDao;
import com.example.backend.member.emotion.UserEmotionPK;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.example.backend.config.KeyConfig;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import com.example.backend.music.MusicDao;
import com.example.backend.music.MusicInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.backend.member.emotion.UserEmotionDao;
import com.example.backend.member.emotion.UserEmotionPK;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class DiaryService {
    private DiaryDao diaryDao;
    private DiaryAnalyticsDao diaryAnalyticsDao;
    private DiaryAnalyticsSentimentDao diaryAnalyticsSentimentDao;
    private DiaryMusicDao diaryMusicDao;
    private MemberAuthDao memberAuthDao;
    private UserEmotionDao userEmotionDao;
    private MusicDao musicDao;
    private RedisTemplate<String, String> redisTemplate;

    private final KeyConfig keyConfig;


    public DiaryDto getDiaryByUidAndDiaryDate(String Uid, LocalDate DiaryDate) {
        DiaryDto result =new DiaryDto();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(Uid);
        Diary diary=diaryDao.getDiaryByUserAuthAndDiaryDateAndIsDeletedIsFalse(userAuth, DiaryDate);
        if(diary!=null) {
            DiaryAnalytics diaryAnalytics = diaryAnalyticsDao.getDiaryAnalyticsByDiary(diary);
            DiaryAnalyticsSentiment diaryAnalyticsSentiment=diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(diary);
            DiaryMusic diaryMusic=diaryMusicDao.getDiaryMusicByDiary(diary);
            result=DiaryAdapter.entityToDto(diaryAnalytics, diaryAnalyticsSentiment, diary, diaryMusic);
        }
        return result;
    }

    public DiaryDto getSimilarDiary(String uid,DiaryDto dto) {
        DiaryDto result =new DiaryDto();
        UserAuth userAuth = MemberAuthDao.getUserAuthByUid(uid);
        LocalDate startdate=dto.getDiaryDate().minusMonths(2);
        LocalDate enddate=dto.getDiaryDate().minusDays(1);
        List<Diary> diaryList=diaryDao.findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(userAuth, startdate, enddate);
        if (diaryList.size()==0) {
            return null;
        }

        //비슷한 일기 찾기
        float[] currentDiary_Anlatics=new float[5];
        currentDiary_Anlatics[0]=dto.getHappy();
        currentDiary_Anlatics[1]=dto.getJoy();
        currentDiary_Anlatics[2]=dto.getSoso();
        currentDiary_Anlatics[3]=dto.getSad();
        currentDiary_Anlatics[4]=dto.getAngry();

        //코사인 유사도 구하는 작업
        int ans=-1;
        int ans_value=Integer.MIN_VALUE;
        for(int i=0;i<diaryList.size();i++) {
            Diary tmp=diaryList.get(i);
            DiaryAnalytics compareDiary=diaryAnalyticsDao.getDiaryAnalyticsByDiary(tmp);
            int tmp_index=tmp.getDiaryId();
            float[] compare=new float[5];
            compare[0]=compareDiary.getHappy();
            compare[1]=compareDiary.getJoy();
            compare[2]=compareDiary.getSoso();
            compare[3]=compareDiary.getSad();
            compare[4]=compareDiary.getAngry();
            float score=cosineSimilarity(currentDiary_Anlatics, compare);
            if(ans_value<=score)ans=tmp_index;
        }
        if(ans==-1)return null;
        Diary diary=diaryDao.getDiaryByDiaryId(ans);
        DiaryAnalytics diaryAnalytics =diaryAnalyticsDao.getDiaryAnalyticsByDiary(diary);
        DiaryAnalyticsSentiment diaryAnalyticsSentiment=diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(diary);
        DiaryMusic diaryMusic = diaryMusicDao.getDiaryMusicByDiary(diary);
        result=DiaryAdapter.entityToDto(diaryAnalytics, diaryAnalyticsSentiment, diary,diaryMusic);
//
        return result;
    }





    public int WriteDiary(String Uid,String content,LocalDate date) throws CustomException {
        float[] emotion=new float[5];
        emotion=find(content);
        DiaryDto dto=new DiaryDto();
        dto.setContent(content);
        dto=getSentiment(dto);

        if(emotion == null) throw new CustomException(ErrorCode.ANALYSIS_NOT_WORKING);

        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(Uid);

        Diary diary_info=Diary.builder()
                .userAuth(userAuth)
                .content(content)
                .diaryDate(date)
                .isDeleted(false)
                .build();
        Diary diary=diaryDao.save(diary_info);
        DiaryAnalytics diaryAnalytics=DiaryAnalytics.builder()
                .diary(diary)
                .happy(emotion[0])
                .joy(emotion[1])
                .soso(emotion[2])
                .sad(emotion[3])
                .angry(emotion[4])
                .build();
        diaryAnalyticsDao.save(diaryAnalytics);
        DiaryAnalyticsSentiment diaryAnalyticsSentiment=DiaryAnalyticsSentiment.builder()
                .diary(diary)
                .sentiment(dto.getSentiment())
                .build();
        diaryAnalyticsSentimentDao.save(diaryAnalyticsSentiment);

        return diary.getDiaryId();
    }

    public int modifyDiary(int diary_id,String content,LocalDate date) {
        float[] emotion = new float[5];
        emotion = find(content);
        DiaryDto dto=new DiaryDto();
        dto.setContent(content);;
        dto=getSentiment(dto);
        Diary deleteDiary=diaryDao.getDiaryByDiaryId(diary_id);
        deleteDiary.deletediary();
        UserAuth userAuth=deleteDiary.getUserAuth();
        Diary diary_info=Diary.builder()
                .userAuth(userAuth)
                .content(content)
                .diaryDate(date)
                .isDeleted(false)
                .build();
        Diary diary=diaryDao.save(diary_info);
        DiaryAnalytics diaryAnalytics=DiaryAnalytics.builder()
                .diary(diary)
                .happy(emotion[0])
                .joy(emotion[1])
                .soso(emotion[2])
                .sad(emotion[3])
                .angry(emotion[4])
                .build();
        DiaryAnalyticsSentiment diaryAnalyticsSentiment=DiaryAnalyticsSentiment.builder()
                .diary(diary)
                .sentiment(dto.getSentiment())
                .build();
        diaryAnalyticsSentimentDao.save(diaryAnalyticsSentiment);
        diaryAnalyticsDao.save(diaryAnalytics);
        diaryDao.save(deleteDiary);

        return diary.getDiaryId();
    }

    public void deleteDiary(int diary_id) {
        Diary deleteDiary=diaryDao.getDiaryByDiaryId(diary_id);
        deleteDiary.deletediary();
        diaryDao.save(deleteDiary);
    }

    public MusicInfo[] getMusicList(int diary_id, String uid) {
        MusicInfo[] result=new MusicInfo[5];
        Diary diary=diaryDao.getDiaryByDiaryId(diary_id);
        float[] Diary_Anlatics=new float[5];
        DiaryAnalytics emotion=diaryAnalyticsDao.getDiaryAnalyticsByDiary(diary);
        Diary_Anlatics[0]=emotion.getHappy();
        Diary_Anlatics[1]=emotion.getJoy();
        Diary_Anlatics[2]=emotion.getSoso();
        Diary_Anlatics[3]=emotion.getSad();
        Diary_Anlatics[4]=emotion.getAngry();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(uid);
        Optional<List<UserEmotion>> optional=userEmotionDao.getUserEmotionByUserAuth(userAuth);
        List<UserEmotion> userEmotionList=optional.get();

        rank[] rank=new rank[12];
        for(int i=0;i<12;i++) {
            UserEmotion userEmotion=userEmotionList.get(i);
            UserEmotionPK emGenre=userEmotion.getEmbGenre();
            Genre genre=emGenre.getGenre();
            String genreName=genre.toString();
            float[] compare=new float[5];
            compare[0]=userEmotion.getHappy();
            compare[1]=userEmotion.getJoy();
            compare[2]=userEmotion.getSoso();
            compare[3]=userEmotion.getSad();
            compare[4]=userEmotion.getAngry();

            float score=cosineSimilarity(Diary_Anlatics, compare);
            rank tmp=new rank(genreName,score);
            rank[i]=tmp;
        }
        Arrays.sort(rank);
        for(int i=0;i<12;i++) {
            System.out.println(rank[i].cosine);
        }

        ValueOperations<String, String>valueOperations = redisTemplate.opsForValue();
        ArrayList<String> selectedMusicList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (long i = 0; i < 7; i++){
            selectedMusicList.add(valueOperations.get(uid + localDate.minusDays(i)));
        }

        for(int i=0;i<3;i++) {
            String genre=rank[i].genre;
            List<MusicInfo> musicList=musicDao.getMusicInfoByMusicGenre(genre);

            // TODO 중간에 체크하는 과정 추가.해야함
            int random;
            loop:
            do {
                random=(int) (Math.random()*musicList.size());
                for(String mid : selectedMusicList){
                    if(mid!=null&&musicList.get(random).getMid() == Integer.parseInt(mid)) {
                        System.out.println("중복되는 노래발견=============");
                        continue loop;
                    }
                }
                break;
            }while (true);

            result[i]=musicList.get(random);
        }
        int v=0;
        int CheckDuplicate=0;
        for(int i=3;i<5;i++) {
            while(true) {
                int min=3;
                int max=12;
                v=(int) (Math.random()*(max-min)+min);
                System.out.println(v);
                if(CheckDuplicate-v==0)continue;
                else break;
            }
            String genre=rank[v].genre;
            List<MusicInfo> musicList=musicDao.getMusicInfoByMusicGenre(genre);

            // TODO 중간에 체크하는 과정 추가.해야함
            int random;
            loop:
            do {
                random=(int) (Math.random()*musicList.size());
                for(String mid : selectedMusicList){
                    if(mid!=null&&musicList.get(random).getMid() == Integer.parseInt(mid)) {
                        System.out.println("중복되는 노래발견");
                        continue loop;
                    }
                }
                break;
            }while (true);

            result[i]=musicList.get(random);
            CheckDuplicate+=v;
        }

        return result;
    }

    private float[] find(String content) {
        double[] tmp=new double[5];
        float[] result=new float[5];
        JSONObject data=new JSONObject();
        data.put("content", content);
//		Flask호출부분
        HttpURLConnection conn=null;
        String reqURL= "http://localhost:5000/predict";
        try {
            URL url = new URL(reqURL);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);//OutputStream을 사용하여 post body 데이터 전송
            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"POST BODY JSON");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+reqURL);
            System.out.println("http 요청 데이터 : "+data);
            System.out.println("");
            //Post방식으로 스트링을통한 JSON전송
            BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(data.toString());
            bw.flush();
            bw.close();
            //서버에서 보낸 응답 데이터 수신 받기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg=in.readLine();
            System.out.println("응답메세지: "+returnMsg);

            JSONParser parser =new JSONParser();
            Object obj=parser.parse(returnMsg);
            JSONObject result_data=(JSONObject) obj;

            tmp[0]=(double) result_data.get("happy");
            tmp[1]=(double) result_data.get("joy");
            tmp[2]=(double) result_data.get("soso");
            tmp[3]=(double) result_data.get("sad");
            tmp[4]=(double) result_data.get("angry");
            result[0]=(float)tmp[0];
            result[1]=(float)tmp[1];
            result[2]=(float)tmp[2];
            result[3]=(float)tmp[3];
            result[4]=(float)tmp[4];
            return result;
        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * getSentiment의 설명을 작성
     * diaryDto의 content를 GCP로 보내
     * diaryDto의 accuracy, sentiment에 값을 저장
     **/
    public DiaryDto getSentiment(DiaryDto diaryDto){
        JSONObject data=new JSONObject();
        JSONObject document=new JSONObject();
        document.put("type", "PLAIN_TEXT");
        document.put("content", diaryDto.getContent());
        data.put("document", document);

        System.out.println(keyConfig.getGCPKey());

        HttpURLConnection conn=null;
        String reqURL= "https://language.googleapis.com/v1/documents:analyzeSentiment?key=" + keyConfig.getGCPKey();
        try {
            URL url = new URL(reqURL);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(data.toJSONString().length()));
            conn.setDoOutput(true);
            conn.connect();

            //Post방식으로 스트링을통한 JSON전송
            BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(data.toString());
            bw.flush();
            bw.close();

            //서버에서 보낸 응답 데이터 수신 받기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            JsonObject documentSentiment = element.getAsJsonObject().get("documentSentiment").getAsJsonObject();

            String magnitude = documentSentiment.getAsJsonObject().get("magnitude").getAsString();
            String score = documentSentiment.getAsJsonObject().get("score").getAsString();

            diaryDto.setSentiment(Float.parseFloat(score));
        }catch(Exception e) {
            e.printStackTrace();
        }

        return diaryDto;
    }
    public DiaryAnalyticsSentiment getDiaryAnalyticsSentiment(int DiaryID){
        Diary diary=diaryDao.getDiaryByDiaryId(DiaryID);
        DiaryAnalyticsSentiment diarySentiment = diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(diary);

        return diarySentiment;
    }

    public MusicInfo getMusicInfo(Diary diary){
        DiaryMusic diaryMusic = diaryMusicDao.getDiaryMusicByDiary(diary);
        if(diaryMusic == null){
            System.out.println("null 값임");
        }
        MusicInfo musicInfo = diaryMusic.getMusicInfo();
        return musicInfo;
    }

    static public class rank implements Comparable<rank>{
        String genre;
        float cosine;
        public rank(String genre, float cosine) {
            super();
            this.genre = genre;
            this.cosine = cosine;
        }
        @Override
        public int compareTo(rank o) {
            if(this.cosine>o.cosine)return -1;
            return 1;
        }

    }

    public static float cosineSimilarity(float[] vectorA, float[] vectorB) {
        float dotProduct = 0;
        float normA = 0;
        float normB = 0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        double tmp=dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        float result=(float)tmp;
        return result;
    }

}