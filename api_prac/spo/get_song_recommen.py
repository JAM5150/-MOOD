def get_song_recommen (sp, artist_id, artist_name, track_id): #(sp, artist_id, genres, tracks_id)
    #return type : list, list, list, any, zip(tuple)

    #장르 구하기
    result = sp.search(artist_name, type='track')
    track = result['tracks']['items'][0]

    for i in range(len(result['tracks']['items'])):
        artist_search = result["tracks"]["items"][i]["artists"][0]["id"]
        if(artist_search == artist_id):
            artist = sp.artist(track["artists"][0]["external_urls"]["spotify"])
            genre = artist["genres"]

    genre = ''.join(map(str, genre))

    #아티스트 아이디, 장르, 트랙 입력하면 음악 추천해주는 함수
    rec = sp.recommendations(seed_artists=[artist_id], seed_genres=[genre], seed_tracks=[track_id], limit=5)
    artist_name = []
    tracks = []
    tracks_img = []

    for track in rec['tracks']:
        artist_name.append(track['artists'][0]['name'])
        tracks.append(track['name'])
        tracks_img.append(track['album']['images'][0]['url'])

    recommend_song = zip(artist_name, tracks, tracks_img)

    return artist_id, artist_name, tracks, track_id, tracks_img, recommend_song