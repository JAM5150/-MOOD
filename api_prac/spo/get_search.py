def get_search(sp, search):
    result = sp.search(search,type='track')

    artist_id=[] #artist의 고유 id(추천함수 및 분류에 사용)
    artist_name=[] #artist 이름
    tracks_id=[] #트랙의 id (추천함수 및 분류에 사용)
    tracks_name=[] #노래제목
    tracks_image=[] #트랙의 앨범이미지url
    tracks_genres=[] #트랙의 장르

    for i in range(len(result['tracks']["items"])):
        # Extract Artist's uri
        artist_id.append(result["tracks"]["items"][i]["artists"][0]["id"]) 
        artist_name.append(result['tracks']['items'][i]['artists'][0]['name']) 
        tracks_id.append(result['tracks']['items'][i]['id'])
        tracks_name.append(result['tracks']['items'][i]['name'])
        tracks_image.append(result['tracks']['items'][i]['album']['images'][0]['url'])

    song_infor_dict = zip(artist_name, tracks_id, tracks_name, tracks_image)

    return artist_id, artist_name, tracks_id, tracks_name, tracks_image, song_infor_dict