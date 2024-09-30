<%--<script src="webjars/sockjs-client/sockjs.min.js"></script>--%>
<%--<script>--%>
<%--    $('btnSend').on('click',function (event){--%>
<%--        event.preventDefault();--%>
<%--        if (socket.readyState !== 1){--%>
<%--            return--%>
<%--        }--%>
<%--        let msg = $('input#msg').val();--%>
<%--        socket.send(msg);--%>
<%--    });--%>
<%--    connect();--%>
<%--</script>--%>

<%--&lt;%&ndash;WebSocket 연결부&ndash;%&gt;--%>
<%--<script>--%>
<%--    var socket = null;--%>
<%--    function connect (){--%>
<%--        var ws = new WebSocket("ws://localhost:8080/replyEcho?bno=1234");--%>
<%--        socket = ws;--%>
<%--        ws.onopen = function (){--%>
<%--            console.log('Info: connection opened.');--%>
<%--        };--%>

<%--        ws.onmessage = function (event){--%>
<%--            console.log("ReceiveMessage:", event.data+'\n');--%>
<%--        };--%>

<%--        ws.onclose = function (event){--%>
<%--            console.log('error', err);--%>
<%--            // setTimeout(function (){ connect(); }, 1000) //retry connect!!--%>
<%--        };--%>

<%--        ws.onerror = function (event){--%>
<%--            console.log('Info: connection closed.');--%>
<%--        };--%>
<%--    }--%>
<%--</script>--%>

<%--<script>--%>
<%--    $(document).ready(function (){--%>
<%--        connectStomp();--%>

<%--        $('btnSend').on('click',function (event){--%>
<%--            event.preventDefault();--%>
<%--            if (!isStomp && socket.readyState !== 1){--%>
<%--                return--%>
<%--            }--%>
<%--            let msg = $('input#msg').val();--%>
<%--            console.log("mmmmmmmmm",msg)--%>
<%--            if (isStomp){--%>
<%--                socket.send('/TTT',{},JSON.stringify({id: 1234 , msg:msg}));--%>
<%--            }else socket.send(msg);--%>
<%--        });--%>
<%--    })--%>
<%--    // 구독자(Subscriber) 구현--%>
<%--    function connectStomp(){--%>
<%--        const sock = new SockJS("http://localhost:8080/ws/chat");--%>
<%--        const ws = Stomp.over(sock);--%>

<%--        ws.connect({"Authorization": getCookie("Authorization")}, function(frame) {--%>
<%--            console.log('Connected: ' + frame);--%>

<%--            // 채팅방 목록에 방생성 및 각종 이벤트를 알리기 위한 구독(재랜더링용)--%>
<%--            ws.subscribe(`/sub/chatRoom/renew/memberId`, (message) => {--%>
<%--                const newMessage = JSON.parse(message.body);--%>
<%--                console.log(newMessage);--%>
<%--                setRenewRoom(prevState => !prevState)--%>
<%--            },{"Authorization" : getCookie("Authorization")})--%>
<%--        });--%>

<%--    }--%>
<%--</script>--%>
