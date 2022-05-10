import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint

client_id = "ee12035224474264be168146ae120d5b"
client_secret = "5a27ae1f3ff84d0394015556a63aadbe"

# BTS / spotif web에 접속하면 알 수 있는 아티스트 아이디
lz_uri = 'spotify:artist:3Nrfpe0tUJi4K4DXYWgMUX'
artist = "소녀시대"

client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=client_secret)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

#아티스트에 대한 정보 검색, limit=1로하여 가장 상단의 아티스트만  --> 노래로하면 limit늘리고, type=노래로 변경하기 search괄호안의맨앞변수도 수정해야함!
result = sp.search(artist, limit=1, type='artist')
pprint.pprint(result)

#아티스트의 고유id알아내기
id = result['artists']['items'][0]['id']
pprint.pprint(id)

results = sp.artist_top_tracks(lz_uri)
# get top 5 tracks
'''
for track in results['tracks'][:5]:
    print('track    : ' + track['name'])
    print('audio    : ' + track['preview_url'])
    print('cover art: ' + track['album']['images'][0]['url'])
    print()
'''

#아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수
rec = sp.recommendations(seed_artists=[id], seed_genres=["pop"], seed_tracks=["1r9xUipOqoNwggBpENDsvJ"], limit=5)
for track in rec['tracks']:
    print(track['artists'][0]['name'], track['name'])