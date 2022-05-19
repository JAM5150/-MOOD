def is_bookmark(sp, artist_name, artist_id, track_name, track_id):
    #북마크 할 경우로 하려고 했는데.. 그냥 장르 구하는 함수

    result = sp.search(artist_name)
    track = result['tracks']['items']
    artist = sp.artist(track["artists"][0]["external_urls"]["spotify"])

    genre = artist["genres"]

    return artist_name, artist_id, track_name, track_id, genre