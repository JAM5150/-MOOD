def get_song_recommen (sp, id, genres, track_id): #(sp, artist_id, genres, tracks_id)
    #return type : tuple
    #나눠서 리스트로 정리하고 zip(dict)로 묶어서 출력, 반환해보기

    #아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수
    rec = sp.recommendations(seed_artists=[id], seed_genres=[genres], seed_tracks=[track_id], limit=5)
    recommend_song = []

    for track, i in rec['tracks']:
        recommend_song.append((track['artists'][i]['name'], track['name'], track['album']['images'][i]['url']))
        #cover_art = (track['tracks']['items'][0]['album']['images'][0]['url']'])
 
        #recommend = dict(map(reversed, recommend_song)) 
    
    return recommend_song