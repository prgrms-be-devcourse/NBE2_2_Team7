// // 채팅방을 추가하는 함수
// function addChatroom() {
//     // AJAX 요청을 사용해 서버에 채팅방 생성 요청
//     fetch('/create-chatroom', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({
//             roomName: '새로운 채팅방'  // 방 이름을 서버로 전달
//         })
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log('채팅방이 생성되었습니다:', data);
//             // 여기서 추가적으로 생성된 채팅방을 화면에 표시할 수 있습니다.
//         })
//         .catch(error => {
//             console.error('채팅방 생성 중 오류 발생:', error);
//         });
// }
//
// // 채팅방을 동적으로 추가하는 함수
// // function addChatroom(chatroomData) {
// //     // 채팅방 정보를 가상 데이터로 설정 (실제로는 서버에서 데이터를 받아올 수 있습니다)
// //     var chatroomData = document.createElement('div');
// //     chatroomCard.classList.add('chatroom-card');
// //     chatroomCard.setAttribute('onclick', openChatRoom(`${chatroomData.id}`));
// //
// //
// //     // 새로운 채팅방 카드 HTML을 생성
// //     const chatroomCard = document.createElement('div');
// //     chatroomCard.classList.add('chatroom-card');
// //     chatroomCard.setAttribute('onclick', `openChatRoom('${chatroomData.id}')`); // 채팅방 클릭 시 ID로 이동
// //
// //     // chatroomData에서 서버로부터 받은 정보를 표시
// //     chatroomCard.innerHTML = `
// //         <div class="chatroom-header">
// //             <h2>${chatroomData.partnerName}</h2> <!-- 대화 상대 이름 -->
// //         </div>
// //         <div class="chatroom-body">
// //             <p class="recent-chat">${chatroomData.recentMessage}</p> <!-- 최근 대화 -->
// //         </div>
// //         <div class="chatroom-footer">
// //             <p>${chatroomData.recentMessageTime}</p> <!-- 최근 대화 시간 -->
// //         </div>
// //     `;
// //
// //     // chatroom-list div에 새로운 채팅방을 추가 (맨 아래에 추가됨)
// //     document.getElementById('chatroom-list').appendChild(chatroomCard);
// // }
// // // 서버에서 채팅방 데이터를 받아서 화면에 추가
// // function loadChatrooms() {
// //     // 여기서는 서버에서 채팅방 목록을 받아오는 로직이 필요합니다.
// //     // 아래는 예시 데이터입니다.
// //     const chatrooms = [
// //         {
// //             id: '101', // 채팅방 ID
// //             partnerName: 'John Doe', // 대화 상대 이름
// //             recentMessage: 'Hello, how are you?', // 최근 메시지 내용
// //             recentMessageTime: '2023-09-28 12:45' // 최근 메시지 시간
// //         },
// //         {
// //             id: '102', // 다른 채팅방 ID
// //             partnerName: 'Jane Doe',
// //             recentMessage: 'See you soon!',
// //             recentMessageTime: '2023-09-28 12:50'
// //         }
// //     ];
// //
// //     // 각 채팅방 데이터를 화면에 추가
// //     chatrooms.forEach(chatroom => {
// //         addChatroom(chatroom);
// //     });
// // }
//
// // 채팅방을 열기 위한 함수
// function openChatRoom(id) {
//     // 서버로부터 해당 채팅방 ID를 이용해 이동 (예: /chatroom/123)
//     window.location.href = '/chatroom/' + id; // #채팅방 ID로 서버에서 처리
// }
// // 페이지 로드 시 채팅방 목록 불러오기
// window.onload = function() {
//     loadChatrooms(); // 채팅방 목록을 불러와서 추가
// };
// function loadChatrooms(){
//     fetch('/read-list', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({
//             roomName: '새로운 채팅방'  // 방 이름을 서버로 전달
//         })
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log('채팅방이 리스트가 생성되었습니다:', data);
//             // 여기서 추가적으로 생성된 채팅방을 화면에 표시할 수 있습니다.
//         })
//         .catch(error => {
//             console.error('채팅방 생성 중 오류 발생:', error);
//         });
// }