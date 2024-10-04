<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f0f0f0;
        }

        .chat-container {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            width: 400px;
            height: 80vh;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        .chat-header {
            padding: 10px;
            background-color: #4caf50;
            color: white;
            text-align: center;
            font-size: 18px;
            position: relative;
        }

        .chat-messages {
            flex-grow: 1;
            padding: 10px;
            overflow-y: auto;
            display: flex;
            flex-direction: column-reverse;
        }

        .message {
            padding: 8px 10px;
            margin: 5px 0;
            border-radius: 8px;
            max-width: 70%;
        }

        .user {
            align-self: flex-end;
            background-color: #dcf8c6;
            border-radius: 10px 10px 10px 0;
            padding: 10px;
        }

        .partner {
            background-color: #f1f0f0;
            align-self: flex-start;
            border-radius: 10px 10px 10px 0;
            padding: 10px;
        }

        .message-sender {
            font-size: 12px;
            color: gray;
        }

        .message-content {
            font-size: 14px;
        }

        .chat-input-container {
            display: flex;
            padding: 10px;
            align-items: center;
            justify-content: center;
            background-color: #f9f9f9;
            border-top: 1px solid #ddd;
            margin-left: 10px;
        }

        .chat-input {
            flex-grow: 1;
            padding: 10px;
            font-size: 16px;
            border-radius: 4px;
            border: 1px solid #ccc;
            box-sizing: border-box;
            transition: border-color 0.3s ease;
        }

        .chat-input:focus {
            border-color: rgba(0, 0, 0, 0.4);
            outline: none;
        }

        .send-button {
            background-image: url('/images/pressicon.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            width: 30px;
            height: 30px;
            border: none;
            cursor: pointer;
            margin-left: 10px;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .send-button:hover {
            background-color: rgb(228, 226, 226);
        }

        .send-button:active {
            transform: scale(0.95);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        .send-button:focus {
            outline: none;
        }

        .exit-button {
            background-color: #4caf50;
            background-image: url('/images/exiticon.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            width: 40px;
            height: 40px;
            position: absolute;
            top: 5px;
            right: 7px;
            border: none;
            cursor: pointer;
            color: white;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .delete-button:hover {
            color: darkred;
        }

        .exit-button:hover {
            transform: scale(0.98);
            box-shadow: 0 3px 6px rgba(0, 0, 0, 0.15);
        }

        .exit-button:active {
            transform: scale(0.95);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        .exit-button:focus {
            outline: none;
        }
    </style>
</head>
<body>
<div class="chat-container" id="app" v-cloak>
    <!-- 채팅방 상단 제목 -->
    <div class="chat-header">
        {{ roomName }}
        <button class="exit-button" @click="exitChat"></button>
    </div>
    <!-- 채팅 메시지 목록 /id나 이름으로 구분하여 채팅 표기가능-->
    <div class="chat-messages">
        <div v-for="(message,index) in messages"
             :key="message.chatMessageId"
             :class="{'message': true, 'user': message.memberId == roomName, 'partner': message.memberId != roomName}">
            <div class="message-sender">
                <strong>{{ message.memberId == roomName ? '나' : '상대방' }}</strong>
            </div>
            <div class="message-content">
                {{ message.message }}
                <button class="delete-button" @click="confirmDelete(index)">x</button>
            </div>
        </div>
    </div>

    <!-- 채팅 입력창 및 전송 버튼 -->
    <div class="chat-input-container">
        <input
                type="text"
                class="chat-input"
                v-model="message"
                @keypress.enter="sendMessage('TALK')"
                placeholder="메시지를 입력하세요."
                ref="chatInput"
        />
        <button class="send-button" @click="sendMessage('TALK')"></button>
    </div>
</div>

<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    //stomp socket연결 변수 생성
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    var isConnected = false;
    //전역변수
    var vm = new Vue({
        el: '#app',
        data: {
            token: '',
            roomId: '',
            roomName: '',
            message: '',
            messages: [],
            nickName: '',
            userCount: 0
        },
        created() {
            this.roomId = localStorage.getItem('wschat.roomId');
            this.roomName = localStorage.getItem('wschat.memberId');
            this.nickName = localStorage.getItem('wschat.nickName');
            this.token = localStorage.getItem('wschat.token');
            console.log('Room ID:', this.roomId);
            console.log('roomName:', this.roomName);
            var _this = this;

            //socket 연결
            ws.connect({"token": _this.token}, function (frame) {
                ws.subscribe("/sub/chat/room/" + _this.roomId, function (message) {
                    var recv = JSON.parse(message.body);
                    console.log(' ws 연결성공 =', recv);
                    _this.recvMessage(recv);
                });
            }, function (error) {
                console.error('WebSocket Connection Error:', error);
                alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
                location.href = "/api/chat-room";
            });

            // 이전 메시지 가져오기
            axios.get('/api/chat/messages/' + this.roomId).then(response => {
                _this.messages = response.data.map(function (recv) {
                    return {
                        chatRoomId: recv.roomId,
                        type: recv.type,
                        memberId: recv.memberId,
                        message: recv.message
                    };
                    console.log('이전메세지 가져오기 완료=', this.messages);
                });
                this.$nextTick(() => {
                    const chatMessages = this.$el.querySelector('.chat-messages');
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                });
            }).catch(error => {
                console.error('이전메세지 가져오기 에러:', error);
            });
        },
        methods: {
            //메세지 송신
            sendMessage(type) {
                if (this.message.trim() === '') {
                    return;
                }
                var messageData = {
                    type: type,
                    chatRoomId: this.roomId,
                    message: this.message,
                    memberId: this.roomName,
                };
                console.log('메세지 전송:', messageData);
                ws.send("/pub/api/chat/message", {}, JSON.stringify(messageData));
                this.message = '';
                this.$refs.chatInput.focus();

            },
            //메세지 수신
            recvMessage(recv) {
                console.log('recvMessage :', recv);
                this.messages.unshift({
                    chatRoomId: recv.chatRoomId,
                    type: recv.type,
                    memberId: recv.memberId,
                    message: recv.message
                });
                this.$nextTick(() => {
                    const chatMessages = this.$el.querySelector('.chat-messages');
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                });
            },
            // 메시지 삭제 확인
            confirmDelete(index) {
                let message = this.messages[index];
                if (message.memberId == this.roomName) { // 본인 메시지만 삭제 가능하도록 확인
                    if (confirm('메시지를 삭제하시겠습니까?')) {
                        this.deleteMessage(index);
                    }
                } else {
                    alert('본인의 메시지만 삭제할 수 있습니다.');
                }
            },
            deleteMessage(index) {
                let message = this.messages[index];
                var _this = this;
                console.info('컨트롤러 들어가기전')
                axios.delete('/api/chat/' + index)
                    .then(response => {
                        console.info('컨트롤러는 갔다옴')
                        if (response.data) {
                            _this.messages.splice(index, 1);
                            console.log('메시지가 삭제되었습니다:', message.getChatMessageId);
                        } else {
                            console.error('서버가 메시지 삭제를 실패했습니다:', message.chatMessageId);
                            alert('메시지 삭제에 실패하였습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('메세지 삭제에 실패하였습니다.');
                        alert('메세지 삭제에 실패하였습니다.');
                    })
            },
            //채팅방 나가기
            exitChat() {
                ws.disconnect(() => {
                    console.log('WebSocket 연결 해제됨');
                });
                location.href = "/api/chat-room";
            },
        }
    });
</script>
</body>
</html>
