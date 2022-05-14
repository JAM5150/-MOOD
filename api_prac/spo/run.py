import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint
from get_spotify_access import get_spotify_credentials
from get_albums_details import get_album_details
from get_album_art import get_album_art

from spotify_credentials import CLIENT_ID, CLIENT_SECRET

sp = get_spotify_credentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)

artist_name = '소녀시대'
album_names, album_name_uri_dict, album_img_url_dict = get_album_details(sp=sp, artist_name=artist_name)
pprint.pprint(album_names)
#print(album_img_url_dict)

print()
#serach함수 형식 -> name:artist, limit=(int), type='album,track'   (= artist가 포함된 album, track정보)
#정보 검색, limit=1로하여 가장 상단의 아티스트만
#result = sp.search(artist_name, limit=1, type='track')
#pprint.pprint(result)

album_names = album_names[0]

get_album_art(album_name=album_names, album_img_url_dict=album_img_url_dict,album_cover_path='./')
