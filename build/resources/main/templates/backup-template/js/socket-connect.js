// $(document).ready(function () {//자바스크립트가 html, DOM준비 완료 된 뒤 순서에 맞게 실행하도록 제어하는거-안전성!
//
//     // WebSocket 클라이언트와 사용자 이름 관련 변수
//     var stompClient = null;
//     var username = [[${member.Nickname}]];///데이터 이동 자바->타임리프->자바스크립트
//     var chatRoomId = [[${chatRoom.chatRoomId}]];//데이터 이동 자바->타임리프->자바스크립트
//
//     if (username) {
//         // SockJS를 통해 WebSocket 연결 설정
//         var socket = new SockJS('ws/chat' + chatRoomId);//소켓을 websocket주소 따라서 방번호 가지고 사용. 방마다 다른 소켓 사용.
//         stompClient = Stomp.over(socket);//스톰프 사용해서 소켓통해서 메시지 기능하기
//         stompClient.connect({}, onConnected, onError);//connect메서드 사용으로 연결 시작
//         //{}는 헤더 부분 비었다=기본헤더사용, onConnected=콜백함수 연결이후 이어질 행위 함수 포함(채팅방 구독 및 사용자 정보 전송을 처리)
//         //onError = 오류나면 정보 알려줌.
//     }
//
//     // WebSocket 연결 성공 시
//     function onConnected() { //연결 성공시 실행 되는 함수
//         $('#connectingElement').text('Connected'); // #connectingElement는 id="connectingElement"를 가진 <div> 요소를 가리킵니다.  그리고  $('#connectingElement')는 jQuery('#connectingElement')와 동일 DOM요소 조작 및 이벤트 처리
//         stompClient.subscribe('/topic/chatRoom/' + chatRoomId, onMessageReceived);//stompClient로 구독하고 (첫번째 매개변수), 두번째는 함수다.
//
//         var messageTime = new Date(); //시간을 현재로 처리하는 변수
//         stompClient.send("/app/chat.register", {}, //stomp websocket통신연결 객체로 메세지 보내는 것 string->json
//             JSON.stringify({
//                 memberId: userId,
//                 chatRoomId: chatRoomId,
//                 sender: username,
//                 type: 'JOIN',
//                 messageTime: messageTime
//             })
//         );
//     }
//
//     // WebSocket 연결 오류 발생 시
//     function onError(error) {//$('#connectingElement')는 id가 **connectingElement**인 HTML 요소를 선택합니다
//         // . 예를 들어 <div id="connectingElement">...</div>와 같은 요소를 가리킵니다.text는 선택된 요소의 텍스트를 지정된 메시지로 바꿉니다.
//         $('#connectingElement').text('WebSocket에 연결할 수 없습니다. 페이지를 새로 고치거나 관리자에게 문의하십시오.');
//         $('#connectingElement').addClass('connecting'); //addClass('connecting')는 선택된 HTML 요소에 **connecting**이라는 CSS 클래스를 추가합니다. CSS 클래스는 스타일링에 사용되며, 예를 들어 텍스트 색상이나 크기, 배경색 등의 스타일을 제어할 수 있습니다.
//     }
//
//     // 메시지 전송 함수
//     function send(event) {
//         var messageContent = $('#messageInput').val().trim();//$('#messageInput')는 id가 messageInput인 HTML 입력 요소를 선택합니다. 이는 주로 사용자가 입력하는 텍스트 상자(input element)를 의미합니다.
//         //.val()은 해당 입력 상자에 있는 값을 가져오는 메서드입니다. trim()은 가져온 값에서 앞뒤 공백을 제거합니다. 결과적으로, 사용자가 입력한 메시지를 가져와 messageContent 변수에 저장합니다.
//
//         if (messageContent && stompClient) { //if 안에 것들이 있으면 발동하는 if문
//             var messageTime = new Date();
//             var chatMessage = {
//                 userId: userId,
//                 chatRoomId: chatRoomId,
//                 sender: username,
//                 content: messageContent,
//                 type: 'CHAT',
//                 messageTime: messageTime
//             }; //변수에 값 저장
//
//             stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage)); //메세지 보내기! (반복되고있죠) //{}는 헤더정보입력
//             $('#messageInput').val('');//메시지를 성공적으로 전송한 후, id가 messageInput인 입력 상자의 값을 빈 문자열로 초기화합니다.
//         }//이 객체를 WebSocket을 통해 서버로 전송합니다.
//         event.preventDefault();//이벤트 기본동장 = 새로고침시 나타나는 오류들을 없애준다.//값제거되고 동작안하는 그런것있음.
//     }
//
//     //이렇게 메세지를 클라로부터 받고 메세지 저장하고 그리고 다른 곳으로 보낼 수 있다.
//
//     // 서버로부터 메시지를 수신할 때 호출되는 함수-> 서버->웹 메세지 보내질때 거쳐가는 경우.
//     function onMessageReceived(payload) {//payload는 클라로부터온 메세지 본문이다.
//         var message = JSON.parse(payload.body);//json으로 메세지가 오기때문에 이를 javascript가 읽을 수 있도록 객체로 바꿔준다. 그리고 변수에 담는다.
//         var messageElement = $('<div>').addClass('chat-message'); //css추가 $('div')로 div태그 생성 -> 메세지표시 컨테이너 동적생산!
//
//         var senderElement = $('<span>').addClass('sender').text(message.sender);//span 생성 css추가, message.sender를 text값으로 설정!
//         var contentElement = $('<span>').text(": " + message.content);//span생성, 택스트 내용= :+message.content -> message는 payload->객체된것
//         var timestampElement = $('<span>').addClass('timestamp').text(" (" + new Date(message.messageTime).toLocaleTimeString() + ")");
//         //span생성, text=message에서 messageTime 선택, localtime으로 타입변환하고 ()에 담아서 보내기
//         messageElement.append(senderElement).append(contentElement).append(timestampElement);//div인 messageElement 요소 선택, 자식들을 추가! -> 메세지 나갈때 세트로 나감.
//         $('#chatMessages').append(messageElement);//기존 메시지 뒤에 새로운 메세지를 붙인다. 큰 채팅 상자 아래에 작은 메세지 상자 붙이는것. DOM조작 append와 jv append는 다르다!
//         $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);//id가 chatmessages 스크롤을 챗 메세지 상자의 가장 아래로 위치시키는 함수
//     }
// });