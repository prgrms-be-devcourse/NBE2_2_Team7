<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            min-height: 100vh;
        }
        #app {
            width: 100%;
            max-width: 800px;
        }

        .header, .input-group {
            margin-bottom: 20px;
        }

        .chatroom-card {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: 10px 0;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.2s;
        }

        .chatroom-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .chatroom-header h6 {
            margin: 0;
            font-weight: bold;
            color: #333;
        }

        .chatroom-body {
            margin-top: 10px;
            color: #666;
        }

        .chatroom-footer {
            margin-top: 10px;
            color: #888;
            font-size: 0.9em;
        }

        .chatroom-card:hover {
            background-color: #f1f1f1;
            transform: scale(1.01);
        }

        .chatroom-card:active {
            transform: scale(0.99);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="header d-flex justify-content-between align-items-center">
        <h3>채팅방 목록</h3>
        <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
    </div>

    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">사용자 닉네임</label>
        </div>
        <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
        </div>
    </div>

    <!-- 채팅방 목록 -->
    <div v-for="item in chatrooms" v-bind:key="item.chatRoomId" class="chatroom-card" @click="enterRoom(item.chatRoomId, item.memberId)">
        <div class="chatroom-header">
            <h6>{{ item.nickname }} </h6>
        </div>
        <div class="chatroom-body">
            <p>최근 메시지: {{ item.latestMessageContent || '최근 메시지가 없습니다.' }}</p>
        </div>
        <div class="chatroom-footer">
            <small>최근 활동: {{ item.latestMessageDate || 'N/A' }}</small>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script>
    //전역 변수
    var vm = new Vue({
        el: '#app',
        data: {
            room_name: '',
            chatrooms: []
        },
        //페이지 시작시 실행 메서드
        created() {
            this.findAllRoom();
        },
        //모든 채팅방 목록 조회
        methods: {
            findAllRoom: function () {
                axios.get('/api/chat-room/list').then(response => {
                    if (Object.prototype.toString.call(response.data) === "[object Array]") {
                        this.chatrooms = response.data;
                    }
                });
            },
            //채팅방 생성
            createRoom: function () {
                if ("" === this.room_name) {
                    alert("사용자 번호를 입력해 주십시요.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("nickName", this.room_name);
                    axios.post('/api/chat-room', params)
                        .then(response => {
                            console.log(response.data);
                            alert("["+response.data.nickName+"]방이 개설 되었습니다.");
                            localStorage.setItem('wschat.nickName', response.data.nickName);
                            this.room_name = '';
                            this.findAllRoom();
                        })
                        .catch(reason => {
                            alert("채팅방 개설에 실패하였습니다.");
                        });
                }
            },
            //채팅방 안으로 입장 + 로컨 스토리지에 정보 저장
            enterRoom: function (roomId, memberId) {
                localStorage.setItem('wschat.roomId', roomId);
                localStorage.setItem('wschat.memberId', memberId);
                location.href = "/api/chat-room/enter/" + roomId;
            }
        }
    });
</script>
</body>
</html>
