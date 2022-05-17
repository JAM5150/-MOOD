'''앨범 세부사항 '''
def get_album_details(sp, artist_name):
    """
    :rtype: (list, dict, dict)
    """
    result = sp.search(artist_name)  # search query

    # Extract Artist's uri
    artist_uri = result["tracks"]["items"][0]["artists"][0]["uri"]

    # Pull all of the artist's albums
    sp_albums = sp.artist_albums(artist_uri, album_type="album")
    # CHECK THAT ALL THE ALBUMS ARE BEING DOWNLOADED -- MIGHT BE A BUG
    # Store artist's albums' names' and uris in separate lists
    album_names = []
    album_uris = []
    album_img_urls = []
    for i in range(len(sp_albums["items"])):
        album_names.append(sp_albums["items"][i]["name"])
        album_uris.append(sp_albums["items"][i]["uri"])
        album_img_urls.append(sp_albums["items"][i]["images"][0]["url"])

    album_name_dict = dict(zip(album_names, album_uris)) #앨범이름:앨범uri
    album_img_url_dict = dict(zip(album_names, album_img_urls)) #앨범이름:앨범아트

    return album_names, album_name_dict, album_img_url_dict, artist_uri
