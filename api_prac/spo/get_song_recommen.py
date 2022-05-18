def get_song_recommen (sp, id, genres, track_id): #(sp, artist_id, genres, tracks_id)

    #아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수
    rec = sp.recommendations(seed_artists=[id], seed_genres=[genres], seed_tracks=[track_id], limit=5)
    for track in rec['tracks']:
        recommend_song = (track['artists'][0]['name'], track['name'], track['album']['images'][0]['url'])
        #cover_art = (track['tracks']['items'][0]['album']['images'][0]['url']'])
    
    return recommend_song