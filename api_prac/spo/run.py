import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from get_spotify_access import get_spotify_credentials
from get_search import get_search
from get_song_recommen import get_song_recommen
from spotify_credentials import CLIENT_ID, CLIENT_SECRET
import pprint

sp = get_spotify_credentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
search_for = 'WEEKEND'

artist_id, artist_name, tracks_id, tracks_name, tracks_image, genre, search_result = get_search(sp=sp, search=search_for)

#recommend = get_song_recommen(sp=sp, artist_id=artist_id, genre=genre, track_id=tracks_id )