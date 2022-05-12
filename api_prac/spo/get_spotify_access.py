import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from spotify_credentials import CLIENT_ID, CLIENT_SECRET

def get_spotify_credentials(CLIENT_ID, CLIENT_SECRET):

    client_id = CLIENT_ID
    client_secret = CLIENT_SECRET

    client_credentials_manager = SpotifyClientCredentials(
        client_id=client_id, client_secret=client_secret
    )
    sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

    return sp