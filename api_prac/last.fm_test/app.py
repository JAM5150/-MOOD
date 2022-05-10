from flask import Flask, jsonify, render_template, request	#플라스크 관련
app = Flask(__name__)

@app.route('/')
def call_API():
    return render_template('song-search.html')	#버튼, api있는 html 렌더

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)	#localhost:5000으로 서버접속