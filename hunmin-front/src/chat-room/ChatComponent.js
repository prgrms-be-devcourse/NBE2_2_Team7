import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import api from '../axios'; // 사용자 정보 가져오는 API 추가

const ChatComponent = ({ chatRoomId, setMessages }) => { // chatRoomId를 props로 받음
    const [message, setMessage] = useState(''); // 메시지 입력 상태
    const stompClientRef = useRef(null);
    const [type, setType] = useState('TALK');
    const [memberId, setMemberId] = useState(null); // memberId 상태
    const [isConnected, setIsConnected] = useState(false); // 연결 상태 관리

    // 사용자 정보 가져오기 함수
    const fetchUserInfo = async () => {
        try {
            const response = await api.get('/chat/user-info'); // 사용자 정보 API 호출
            setMemberId(response.data.memberId); // 사용자 ID 설정
            console.log('사용자 정보:', response.data); // 사용자 정보 콘솔 출력
        } catch (error) {
            console.error('사용자 정보를 가져오는 데 실패했습니다:', error);
        }
    };

    // 웹소켓 연결
    const stompConnect = () => {
        try {
            const token = localStorage.getItem('token'); // 토큰을 가져옴
            const sock = new SockJS("http://localhost:8080/ws-stomp"); // SockJS 연결 설정
            const client = new Client({
                webSocketFactory: () => sock, // WebSocket 설정
                connectHeaders: {
                    Authorization: `${token}`, // 토큰 추가 (Bearer 스킴 사용)
                },
                debug: (str) => console.log(str), // 디버깅용 로그
                onConnect: () => {
                    console.log('STOMP 연결 성공');
                    setIsConnected(true); // 연결 상태 업데이트

                    // 채팅방 구독
                    client.subscribe(`/sub/chat/room/${chatRoomId}`, (message) => {
                        console.log('메시지 수신 중...');
                        if (message.body) {
                            const newMessage = JSON.parse(message.body);
                            console.log('수신한 메시지:', newMessage);
                            setMessages((prevMessages) => [ ...prevMessages, newMessage]);
                        } else {
                            console.log('수신한 메시지의 body가 없습니다.');
                        }
                    }, (error) => {
                        console.error('구독 실패:', error);
                    });
                },
                onStompError: (frame) => {
                    console.error('Broker error:', frame.headers['message']);
                    setIsConnected(false); // 오류 시 연결 상태 업데이트
                },
                onWebSocketClose: () => {
                    console.log('WebSocket 연결 종료');
                    setIsConnected(false); // 연결 종료 시 상태 업데이트
                },
            });

            client.activate();
            stompClientRef.current = client;
        } catch (err) {
            console.error('WebSocket 연결 실패:', err);
        }
    };

    // 웹소켓 연결 해제
    const stompDisconnect = () => {
        if (stompClientRef.current) {
            stompClientRef.current.deactivate(() => console.log('STOMP 연결 해제'));
        }
    };

    useEffect(() => {
        fetchUserInfo(); // 컴포넌트 마운트 시 사용자 정보 가져오기
        stompConnect(); // 웹소켓 연결

        return () => {
            stompDisconnect(); // 컴포넌트 언마운트 시 WebSocket 연결 해제
        };
    }, [chatRoomId]); // chatRoomId가 변경될 때마다 재연결

    // 메시지 전송
    const sendMessage = () => {
        console.log('sendMessage 함수 호출'); // 함수 호출 확인

        if (message.trim() === '' || !memberId) {
            console.log('메시지 전송 조건 미충족: message 또는 memberId'); // 조건 미충족 확인
            return;
        }

        const token = localStorage.getItem('token'); // 토큰 가져오기
        console.log('token send message:', token);

        const chatMessageDTO = {
            chatRoomId: Number(chatRoomId), // 숫자 타입으로 변환
            nickName: '나', // 현재 사용자의 이름 (여기서는 '나'로 설정)
            memberId: Number(memberId), // 상태 변수 사용
            message: message,
            type: type,
        };

        console.log('전송한 메시지:', chatMessageDTO); // 전송할 메시지를 콘솔에 출력

        if (stompClientRef.current && isConnected) { // isConnected 상태 사용
            try {
                stompClientRef.current.publish({
                    destination: '/pub/api/chat/message', // 메시지 발행 경로
                    body: JSON.stringify(chatMessageDTO), // 메시지를 JSON으로 변환하여 전송
                    headers: {
                        Authorization: `${token}`, // 토큰을 헤더에 포함하여 전송
                    },
                });
                console.log('메시지 전송 완료');
            } catch (error) {
                console.error('메시지 전송 중 오류 발생:', error);
            }
        } else {
            console.error('WebSocket 연결이 되어 있지 않습니다.');
        }

        setMessage(''); // 입력 필드를 초기화
    };

    return (
        <div className="chat-input-container">
            <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)} // 메시지 입력 상태 업데이트
                onKeyPress={(e) => {
                    if (e.key === 'Enter') sendMessage(); // Enter 키로 메시지 전송
                }}
                placeholder="메시지를 입력하세요"
                className="chat-input"
            />
            <button onClick={sendMessage} className="chat-send-button">전송</button>
        </div>
    );
};

export default ChatComponent;
