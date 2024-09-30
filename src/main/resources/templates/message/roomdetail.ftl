<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
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

        .message.user {
            background-color: #dcf8c6;
            align-self: flex-end;
        }

        .message.partner {
            background-color: #f1f0f0;
            align-self: flex-start;
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
            transition: transform 0.2s ease, box-shadow 0.2s ease; /* 부드러운 눌림 효과 */
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
            transition: transform 0.2s ease, box-shadow 0.2s ease; /* 부드러운 눌림 효과 */
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
        {{ memberId }}
        <button class="exit-button" @click="exitChat"></button>
    </div>

    <!-- 채팅 메시지 목록 -->
    <div class="chat-messages">
        <div v-for="message in messages"
             :class="{'message': true, 'user': message.sender === memberId, 'partner': message.sender !== memberId}">
            <div>
                <strong>{{ message.sender }}</strong>
            </div>
            <div>{{ message.message }}</div>
        </div>
    </div>

    <!-- 채팅 입력창 및 전송 버튼 -->
    <div class="chat-input-container">
        <input
                type="text"
                class="chat-input"
                v-model="message"
                @keypress.enter="sendMessage('TALK')"
                placeholder="메시지를 입력하세요..."
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
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    var isConnected = false;

    var vm = new Vue({
        el: '#app',
        data: {
            roomId: '',
            memberId: '',
            message: '',
            messages: [],
            userCount: 0
        },
        created() {
            this.roomId = localStorage.getItem('wschat.roomId');
            this.memberId = localStorage.getItem('wschat.memberId');
            console.log('Room ID:', this.roomId);
            console.log('Member ID:', this.memberId);
            var _this = this;

            // 이전 메시지 가져오기
            axios.get('/chat/messages/' + this.roomId).then(response => {
                _this.messages = response.data.map(function (recv) {
                    return {
                        "type": recv.type,
                        "sender": recv.memberId,
                        "message": recv.message,
                    };
                    console.log('response.data=',response.data);
                });
            }).catch(error => {
                console.error('이전메세지 가져오기 에러:', error);
            });
            ws.connect({}, function (frame) {
                ws.subscribe("/sub/chat/room/" + _this.roomId, function (message) {
                    var recv = JSON.parse(message.body);
                    console.log('recv:',recv)
                    _this.recvMessage(recv);
                });
            }, function (error) {
                console.error('WebSocket Connection Error:', error);
                alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
                location.href = "/chat/room";
            });
        },
        methods: {
            sendMessage(type) {
                const messageData = {
                    type: type,
                    chatRoomId: this.roomId,
                    message: this.message,
                    memberId: this.memberId,
                };
                console.log('Sending message:', messageData);
                ws.send("/pub/chat/message", {}, JSON.stringify(messageData));
                this.message = '';
            },
            recvMessage(recv) {
                console.log('Received message:', recv);
                this.messages.push({
                    "chatRoomId":recv.roomId,
                    "type": recv.type,
                    "sender": recv.memberId,
                    "message": recv.message,
                });
                this.$nextTick(() => {
                    const chatMessagesElement = document.querySelector('.chat-messages');
                    chatMessagesElement.scrollTop = chatMessagesElement.scrollHeight;
                });
            },
            exitChat() {
                location.href = "/chat/room";
            }
        }
    });
</script>
</body>
</html>
