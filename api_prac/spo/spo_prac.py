import spotipy
from spotipy.oauth2 import SpotifyClientCredentials

client_id = "ee12035224474264be168146ae120d5b"
client_secret = "5a27ae1f3ff84d0394015556a63aadbe"

# BTS
lz_uri = 'spotify:artist:3Nrfpe0tUJi4K4DXYWgMUX'

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

results = sp.artist_top_tracks(lz_uri)

# get top 5 tracks
for track in results['tracks'][:5]:
    print('track    : ' + track['name'])
    print('audio    : ' + track['preview_url'])
    print('cover art: ' + track['album']['images'][0]['url'])
    print()