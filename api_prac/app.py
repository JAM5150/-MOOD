from flask import Flask, request, jsonify
app = Flask(__name__)
 
@app.route('/hello_world')#test_api
def hello_world():
    return 'Hello, World!'

@app.route('/echo_call/<param>')
def get_echo_call(param):
    return jsonify({"param": param})

@app.route('/echo_call', methods=['POST']) #post echo api
def post_echo_call():
    param = request.get_json()
    return jsonify(param)

@app.route('/userLogin', methods = ['POST'])
def userLogin():
    user = request.get_json()#json 데이터를 받아옴
    return jsonify(user)# 받아온 데이터를 다시 전송
 
@app.route('/environments/<language>')
def environments(language):
    return jsonify({"language":language})
 
if __name__ == "__main__":
    app.run()