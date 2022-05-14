import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint


def get_song_recommen (sp, artist_name):
#아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수  => seed_tracks 수정해야함/tracks > 
    '''
    rec = sp.recommendations(seed_artists=[id], seed_genres=["pop"], seed_tracks=["1r9xUipOqoNwggBpENDsvJ"], limit=5)
    for track in rec['tracks']:
    print(track['artists'][0]['name'], track['name'])
    '''