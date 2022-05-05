import sys
import requests
import base64
import json
import logging

#코드 정보가 출력이 안됨. 상태코드 200이 출력되어야하는데...~

client_id = "ee12035224474264be168146ae120d5b"
client_secret = "5a27ae1f3ff84d0394015556a63aadbe"

def main():
    endpoint = "https://accounts.spotify.com/api/token" ## 토큰 받아옴
    encoded = base64.b64encode("{}:{}".format(client_id, client_secret).encode('utf-8')).decode('ascii')    ## id ,secret 인코딩

    headers = {
        "Authorization": "Basic {}".format(encoded)
    }

    payload = {
        "grant_type": "client_credentials"
    }

    r = requests.post(endpoint, data=payload, headers=headers)

    print(r.status_code) ## 에러 코드
    print(r.text) ## 내용물

    sys.exit(0) ##중간에 끊어서 보기 위함