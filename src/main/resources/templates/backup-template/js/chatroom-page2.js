// // 채팅방을 클릭할 때 호출되는 함수
// // SockJS를 통해 WebSocket 연결 설정
// const stompClient = new StompJs.Client({
//     brokerURL: 'ws://localhost:8080/ws'
// });
//
// // WebSocket 연결 시도
// stompClient.onConnect = (frame) => {
//     setConnected(true);
//     console.log('Connected: ' + frame);
//     stompClient.subscribe('/topic/greetings', (greeting) => {
//         showGreeting(JSON.parse(greeting.body).content);
//     });
// };
//
// stompClient.onWebSocketError = (error) => {
//     console.error('Error with websocket', error);
// };
//
// stompClient.onStompError = (frame) => {
//     console.error('Broker reported error: ' + frame.headers['message']);
//     console.error('Additional details: ' + frame.body);
// };
//
// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     if (connected) {
//         $("#conversation").show();
//     }
//     else {
//         $("#conversation").hide();
//     }
//     $("#greetings").html("");
// }
//
// function connect() {
//     stompClient.activate();
// }
//
// function disconnect() {
//     stompClient.deactivate();
//     setConnected(false);
//     console.log("Disconnected");
// }
//
// function sendName() {
//     stompClient.publish({
//         destination: "/app/hello",
//         body: JSON.stringify({'name': $("#name").val()})
//     });
// }
//
// function showGreeting(message) {
//     $("#greetings").append("<tr><td>" + message + "</td></tr>");
// }
// //
// // stompClient.connect({}, function (frame) {
// //     // 연결 성공 시 실행
// //     $('#connectingElement').text('Connected'); // 연결된 상태를 표시
// //
// //     // 해당 채팅방을 구독 (이 채팅방에서 메시지를 수신)
// //     stompClient.subscribe('/topic/chatRoom/' + chatRoom.chatRoomId, function (payload) {
// //         var message = JSON.parse(payload.body); // 메시지를 JSON으로 변환
// //         console.log("메시지 수신:", message); // 메시지 수신 처리
// //         // 여기에서 메시지를 화면에 표시하는 로직을 추가할 수 있습니다.
// //     });
// //
// //     // 서버로 JOIN 메시지 전송 (사용자가 채팅방에 입장했음을 알림)
// //     var messageTime = new Date(); // 현재 시간
// //     stompClient.send("/app/chat.register", {}, JSON.stringify({
// //         memberId: member.memberId,
// //         chatRoomId: chatRoom.chatRoomId,
// //         sender: member.nickname,
// //         messageTime: messageTime
// //     }));
// // }, function (error) {
// //     // WebSocket 연결 실패 시 실행
// //     $('#connectingElement').text('WebSocket에 연결할 수 없습니다. 페이지를 새로 고치거나 관리자에게 문의하십시오.');
// //     $('#connectingElement').addClass('connecting'); // 연결 실패 상태 표시
// // });
//
// // 웹소켓 연결 이후 페이지 이동
// window.location.href = '/chat-page'; // /chat-page로 이동
//
// $(function () {
//     // $("form").on('submit', (e) => e.preventDefault());
//     $("#connect").click(() => connect());
//     // $( "#disconnect" ).click(() => disconnect());
//     // $( "#send" ).click(() => sendName());
// });