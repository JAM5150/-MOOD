import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint

from spotify_credentials import CLIENT_ID, CLIENT_SECRET
from get_spotify_access import get_spotify_credentials
from get_albums_details import get_album_details
from get_album_art import get_album_art

sp = get_spotify_credentials(CLIENT_ID, CLIENT_SECRET)

artist = "소녀시대"
print(get_album_details(sp, artist))

'''
#serach함수 형식 -> name:artist, limit=(int), type='album,track'   (= artist가 포함된 album, track정보)
#정보 검색, limit=1로하여 가장 상단의 아티스트만
result = sp.search(artist, limit=1, type='track')
pprint.pprint(result)
'''

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

#아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수  => seed_tracks 수정해야함/tracks > 
'''
rec = sp.recommendations(seed_artists=[id], seed_genres=["pop"], seed_tracks=["1r9xUipOqoNwggBpENDsvJ"], limit=5)
for track in rec['tracks']:
    print(track['artists'][0]['name'], track['name'])
'''