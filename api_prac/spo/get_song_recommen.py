import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint

def get_song_recommen (sp, id, genres, track_id): #(sp, artist_name, artist_id, genres, tracks_id)

    #아티스트 아이디와 장르, 트랙입력하면 유사한 음악 추천해주는 함수  => seed_tracks 수정필요/tracks > 
    rec = sp.recommendations(seed_artists=[id], seed_genres=[genres], seed_tracks=[track_id], limit=5)
    for track in rec['tracks']:
        recommend = (track['artists'][0]['name'], track['name']) #+앨범아트코드
    
    return recommend