from spotipy.oauth2 import SpotifyClientCredentials
import spotipy
import pprint


client_id = "ee12035224474264be168146ae120d5b"
client_secret = "5a27ae1f3ff84d0394015556a63aadbe"

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

result = sp.search("coldplay", limit=1, type='artist')
pprint.pprint(result)

id = result['artists']['items'][0]['id']
image = result['artists']['items'][0]['images'][0]['url']

pprint.pprint(id)
pprint.pprint(image)