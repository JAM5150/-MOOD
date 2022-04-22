import requests 
from bs4 import BeautifulSoup 

req = requests.get('https://www.music-flo.com/api/meta/v1/track/KPOP/new?page=1&size=100&timestamp=1581420059879')
data = req.json() 

new_musics = data['data']['list'] 
result = [] 

for new_music in new_musics: 
    result.append({ 
        'title' : new_music['name'], 
        'artist' : new_music['artistList'][0]['name'] 
        }) 
        
for value in result: 
    print(value)
