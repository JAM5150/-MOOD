from spotipy.oauth2 import SpotifyClientCredentials
import spotipy
import pprint


client_id = " "
client_secret = " "

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

artist_name = "소녀시대"

result = sp.search(artist_name, limit=3, type='artist')
pprint.pprint(result)

id = result['artists']['items'][0]['id']
image = result['artists']['items'][0]['images'][0]['url']

pprint.pprint(id)
pprint.pprint(image)