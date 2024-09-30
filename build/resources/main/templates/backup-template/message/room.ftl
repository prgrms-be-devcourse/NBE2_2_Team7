<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-6">
            <h3>채팅방 목록</h3>
        </div>
        <div class="col-md-6 text-right">
            <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
        </div>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">사용자 번호</label>
        </div>
        <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId"
            v-on:click="enterRoom(item.chatRoomId, item.memberId)">
            <h6>{{item.memberId}} <span class="badge badge-info badge-pill">{{item.userCount}}</span></h6>
        </li>
    </ul>
</div>


<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            room_name: '',
            chatrooms: []
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function () {
                axios.get('/chat/rooms').then(response => {

                    if (Object.prototype.toString.call(response.data) === "[object Array]")
                        this.chatrooms = response.data;
                });
            },
            createRoom: function () {
                if ("" === this.room_name) {
                    alert("사용자 번호를 입력해 주십시요.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("memberId", this.room_name);
                    axios.post('/chat/room', params)
                        .then(
                            response => {
                                console.log(response.data); // 응답 데이터 출력
                                alert("방 번호는 "+ params +" 입니다.")
                                this.room_name = '';
                                this.findAllRoom();
                            }
                        )
                        .catch(reason => {
                            alert("채팅방 개설에 실패하였습니다.");
                        });
                }
            },
            enterRoom: function (roomId, memberId) {
                localStorage.setItem('wschat.roomId', roomId);
                localStorage.setItem('wschat.memberId', memberId);
                location.href = "/chat/room/enter/" + roomId;
            }
        }
    });
</script>
</body>
</html>