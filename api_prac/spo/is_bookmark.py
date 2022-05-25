def is_bookmark(sp, artist_name, artist_id, track_name, track_id):
    #북마크 할 경우로 하려고 했는데.. 그냥 장르 구하는 함수
    #전달받은 artist_id가 검색한 것과 같으면 장르 구하는 걸로 수정해야함

    result = sp.search(artist_name, type='artist')
    track = result['artist']['items']

    for i in range(len(track)):
        artist_search = result["tracks"]["items"][i]["artists"][0]["id"]
        if(artist_search == artist_id):
            artist = sp.artist(track["artists"][0]["external_urls"]["spotify"])
            genre = artist["genres"]

    return artist_name, artist_id, track_name, track_id, genre