import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from get_spotify_access import get_spotify_credentials
from get_search import get_search
from get_song_recommen import get_song_recommen
from spotify_credentials import CLIENT_ID, CLIENT_SECRET
import pprint
import json

#예외 처리 필요 

sp = get_spotify_credentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)

search_for = 'IU'

artist_id, artist_name, tracks_id, tracks_name, tracks_image, search_result = get_search(sp=sp, search=search_for)

s = 1 #선택
artist_id = artist_id[s]
artist_name = artist_name[s]
tracks_id = tracks_id[s]
tracks_name = tracks_name[s]
tracks_image = tracks_image[s]

artist_id, artistname, tracks, track_id, tracks_img, recommend = get_song_recommen(sp=sp, artist_id=artist_id, artist_name=artist_name, track_id=tracks_id)

reco = list(recommend)
pprint.pprint(reco)
jsons = json.dumps(reco)
print()
pprint.pprint(jsons)


#recommend = get_song_recommen(sp=sp, artist_id=artist_id, genre=genre, track_id=tracks_id )