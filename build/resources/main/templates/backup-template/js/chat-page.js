// // 채팅 메시지를 전송하는 공통 함수 (사용자 메시지 전송)
// function sendMessageCommon() {
//     const inputField = document.getElementById('chat-input');
//     const messageText = inputField.value.trim();
//
//     if (messageText !== '') {
//         // 메시지 요소 생성 (사용자 메시지)
//         const messageElement = document.createElement('div');
//         messageElement.classList.add('message', 'user');
//         messageElement.textContent = messageText; // 사용자가 입력한 메시지를 추가
//
//         // 채팅 메시지 목록에 추가
//         const chatMessages = document.getElementById('chat-messages');
//         chatMessages.insertBefore(messageElement, chatMessages.firstChild);
//
//         // 입력창 비우기
//         inputField.value = '';
//
//         // 여기서 실제로 상대방에게 메시지를 보내는 로직이 추가될 수 있습니다.
//         // 예: 웹소켓을 통해 서버로 메시지를 전송하는 로직
//         // sendMessageToServer(messageText);
//     }
// }
//
// // 상대방 메시지를 화면에 표시하는 함수
// function displayPartnerMessage(messageText) {
//     if (messageText !== '') {
//         // 메시지 요소 생성 (상대방 메시지)
//         const messageElement = document.createElement('div');
//         messageElement.classList.add('message', 'partner'); // partner 클래스를 적용
//         messageElement.textContent = messageText; // 상대방이 보낸 메시지를 추가
//
//         // 채팅 메시지 목록에 추가
//         const chatMessages = document.getElementById('chat-messages');
//         chatMessages.insertBefore(messageElement, chatMessages.firstChild);
//     }
// }
//
// // Enter 키로 메시지 전송
// function sendMessage(event) {
//     if (event.key === 'Enter') {
//         sendMessageCommon();
//     }
// }
//
// // 버튼 클릭으로 메시지 전송
// function sendMessageOnClick() {
//     sendMessageCommon();
// }
//
// // Exit button 클릭 시 채팅방 나가기 함수
// function exitChat() {
//     alert('채팅방을 나갑니다!');
//     window.location.href = '/chatroom-page'; // 홈 페이지로 이동
// }
