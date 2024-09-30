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
<%--    var socket = null;--%>
<%--    var isStomp = false;--%>
<%--    function connectStomp(){--%>
<%--        var sock = new SockJS("stompTest");--%>
<%--        var client = Stomp.over(sock);--%>
<%--        isStomp = true;--%>
<%--        socket = client;--%>
<%--        client.connect({}, function (){--%>
<%--            console.log("Connected stompTest!");--%>
<%--            client.send('/TTT',{},"msg:Haha~~");--%>

<%--            //해당 토픽 구독--%>
<%--            client.subscribe('/topic/message',function (event){--%>
<%--                console.log("!!!!!!!!!!!event>>", event)--%>
<%--            });--%>
<%--        });--%>

<%--    }--%>
<%--</script>--%>
