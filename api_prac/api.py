from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
from oauth2client.tools import argparser

DEVELOPER_KEY = 'AIzaSyCwSQ7b4gGFBilnTI54g5--Ggs3r9f2jAc' # 유튜브 API 키값을 입력하세요
YOUTUBE_API_SERVICE_NAME = 'youtube'
YOUTUBE_API_VERSION = 'v3'

youtube = build(YOUTUBE_API_SERVICE_NAME, YOUTUBE_API_VERSION, developerKey=DEVELOPER_KEY)
search_response = youtube.search().list(
    q = '슈카월드',
    order = 'relevance',
    part = 'snippet',
    maxResults = 50
).execute()

print(search_response)