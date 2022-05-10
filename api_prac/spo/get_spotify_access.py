import spotipy
from spotipy.oauth2 import SpotifyClientCredentials

def get_spotify_credentials(client_id, client_secret):

    client_id = client_id
    client_secret = client_secret

    client_credentials_manager = SpotifyClientCredentials(
        client_id=client_id, client_secret=client_secret
    )
    sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

    return sp