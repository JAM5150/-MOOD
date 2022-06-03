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
    #return type : list, list, list, list, list, zip(tuple)
    result = sp.search(search,type='track')

    artist_id=[] #artist의 고유 id(추천함수 및 분류에 사용)
    artist_name=[] #artist 이름
    tracks_id=[] #트랙의 id (추천함수 및 분류에 사용)
    tracks_name=[] #노래제목
    tracks_image=[] #트랙의 앨범이미지url

    for i in range(len(result['tracks']["items"])):
        artist_id.append(result["tracks"]["items"][i]["artists"][0]["id"]) 
        artist_name.append(result['tracks']['items'][i]['artists'][0]['name']) 
        tracks_id.append(result['tracks']['items'][i]['id'])
        tracks_name.append(result['tracks']['items'][i]['name'])
        tracks_image.append(result['tracks']['items'][i]['album']['images'][0]['url'])

    song_infor_dict = zip(artist_name, tracks_id, tracks_name, tracks_image)

    return artist_id, artist_name, tracks_id, tracks_name, tracks_image, song_infor_dict    

@app.route('/')#test_api
def main_get(inputr=None):
    return render_template('search_test.html', input=input)

'''
@app.route("/user",methods=['GET', 'POST'])
def post():
	if(request.method =='GET'):
		return render_template('input.html')

	elif(request.method == 'POST'):
		value = request.form['input']
		return render_template('default.html', name=value)
'''   

#음악검색 GET, POST
@app.route('/search', methods=['GET', 'POST'])
def search(input = None):
    if request.method=='POST':
        pass
    elif request.method=='GET':
        ##검색어 넘겨받기
        search_for = request.args.get("input")
        #return render_template('searchresult.html', name = search_for)
        request.on_json_loading_failed = on_json_loading_failed_return_dict
        artist_id, artist_name, tracks_id, tracks_name, tracks_image, search_result = get_search(sp=sp, search=search_for)
        listre = list(search_result)
        jsonString = json.dumps(listre)
        return render_template('search_test.html', input=search_for, result=jsonString)

if __name__ == "__main__":
    app.run(debug=True, threaded=True)