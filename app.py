from concurrent.futures import thread
from operator import methodcaller
from flask import Flask, render_template, request, jsonify, json, url_for
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
CLIENT_ID = ' '
CLIENT_SECRET = ' '
client_credentials_manager = SpotifyClientCredentials(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
app = Flask(__name__)
def on_json_loading_failed_return_dict(e):  
    return {}
def get_search(sp, search):
    #return type : dict
    result = sp.search(search,type='track')
    search_dict = {"artist_id" : [], "artist_name" : [], "tracks_id" : [], "tracks_name":[], "tracks_image":[]};

    for i in range(len(result['tracks']["items"])):
        search_dict["artist_id"].append({result["tracks"]["items"][i]["artists"][0]["id"]})
        search_dict["artist_name"].append(result['tracks']['items'][i]['artists'][0]['name']) 
        search_dict["tracks_id"].append(result['tracks']['items'][i]['id'])
        search_dict["tracks_name"].append(result['tracks']['items'][i]['name'])
        search_dict["tracks_image"].append(result['tracks']['items'][i]['album']['images'][0]['url'])

    return search_dict

def get_song_recommen (sp, artist_id, artist_name, track_id): #북마크했던 노래의 아티스트 정보 기반으로 추천
    #return type : list, list, list, any, zip(tuple)
    #장르 구하기
    result = sp.search(artist_name, type='track') #전달받은 아티스트의 정보 그대로 검색(초기 검색할때와 동일)
    track = result['tracks']['items'][0]
    for i in range(len(result['tracks']['items'])):
        artist_search = result["tracks"]["items"][i]["artists"][0]["id"] ##(artist의 id구하기)
        if artist_search == artist_id : ##전달받은 아티스트의 정보와 검색한 아티스트의 정보가 ==id로 식별하여 같을때
            artist = sp.artist(track["artists"][0]["external_urls"]["spotify"])
            genre = artist["genres"] #장르 구하기
            if genre==None : #장르가 없을 경우 ''
                genre = ''
        break
    genre = ''.join(map(str, genre))
    #아티스트 아이디, 장르, 트랙 입력하면 음악 추천해주는 recommendations
    rec = sp.recommendations(seed_artists=[artist_id], seed_genres=[genre], seed_tracks=[track_id], limit=3)
    artist_name = []
    tracks = []
    tracks_img = []
    #track_prev = []
    for track in rec['tracks']:
        artist_name.append(track['artists'][0]['name'])
        tracks.append(track['name'])
        tracks_img.append(track['album']['images'][0]['url'])
        #track_prev.append(track['preview_url'])

    recommend_song = zip(artist_name, tracks, tracks_img)
    return artist_id, artist_name, tracks, track_id, tracks_img, recommend_song

@app.route('/')#basic
def main_get():
    return 'Hello World!'

#음악검색 GET, POST
@app.route('/search', methods=['GET', 'POST'])
def search():
    search_for = request.json
    search_for = search_for['search'] ##BODY에 들어갈 내용 { "search" : "검색어 " }
    request.on_json_loading_failed = on_json_loading_failed_return_dict ##예외처리
    search_result = get_search(sp=sp, search=search_for) ##검색함수
    #검색결과(가수이름, 노래제목, 앨범아트) dict->json형태로 변환
    jsonString = json.dumps(search_result, default=str)
    return jsonify(jsonString)

#음악추천
@app.route('/recommend', methods=['GET','POST'])
def recommend():
    Bookmark = request.json ##BODY에 들어갈 내용 { "artist_id" : "아티스트id", "artist_name" : "아티스트이름", "track_id" : "트랙id" }
    request.on_json_loading_failed = on_json_loading_failed_return_dict #예외처리
    artist_id = Bookmark['artist_id']
    artist_name = Bookmark['artist_name']
    track_id = Bookmark['track_id']

    artist_id, artist_name, tracks, track_id, tracks_img, recommend_song = get_song_recommen(sp=sp, artist_id=artist_id, artist_name=artist_name, track_id=track_id)
    recommend_song = list(recommend_song)
    recommend_song = json.dumps(recommend_song)
    return recommend_song

if __name__ == "__main__":
    app.run(debug=True, threaded=True)