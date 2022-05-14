import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint

from get_spotify_access import get_spotify_credentials
from get_albums_details import get_album_details
from get_album_art import get_album_art
from spotify_credentials import CLIENT_ID, CLIENT_SECRET

sp = get_spotify_credentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)

artist = "소녀시대"
print(get_album_details(sp, artist))