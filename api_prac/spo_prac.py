import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from spo.get_spotify_access import get_spotify_credentials
from spo.get_search import get_search
from spo.get_song_recommen import get_song_recommen
import pprint
from spo.spotify_credentials import CLIENT_ID, CLIENT_SECRET

sp = get_spotify_credentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
#serach함수 형식 -> name:artist, limit=(int), type='album,track'   (= artist가 포함된 album, track정보)
#정보 검색, limit=1로하여 가장 상단의 아티스트만
result = sp.search('소녀시대', limit=1, type='track')
pprint.pprint(result)

#artist_info = requests.get('https://api.spotify.com/v1/search', headers={'Authorization': 'Bearer {token}'.format(token=access_token)},params={ 'q': artist, 'type': 'artist' })

#아티스트의 고유id알아내기
'''
id = result['artists']['items'][0]['id']
pprint.pprint(id)
'''

#results = sp.artist_top_tracks(lz_uri)
# get top 5 tracks
'''
for track in results['tracks'][:5]:
    print('track    : ' + track['name'])
    print('audio    : ' + track['preview_url'])
    print('cover art: ' + track['album']['images'][0]['url'])
    print()
'''