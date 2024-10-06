import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ChatComponent from './ChatComponent'; // WebSocket 연결 컴포넌트
import ChatRoomInfo from './ChatRoomInfo'; // 채팅방 정보 가져오는 컴포넌트
import api from '../axios'; // Axios 인스턴스 import
import './ChatRoomDetail.css'; // 스타일 파일 import
import moment from 'moment'; // 날짜와 시간을 포맷하기 위한 라이브러리 import
import { v4 as uuidv4 } from 'uuid'; // UUID 생성 라이브러리 추가
import ArrowBackIcon from '@mui/icons-material/ArrowBack'; // 아이콘 import

const ChatRoomDetail = () => {
    const { chatRoomId } = useParams(); // URL에서 chatRoomId를 가져옴
    const navigate = useNavigate(); // navigate 함수
    const [messages, setMessages] = useState([]); // 실시간 채팅 메시지를 관리하는 상태
    const [nickname, setNickname] = useState(''); // 대화 상대방의 닉네임을 저장하는 상태
    const [currentUserId, setCurrentUserId] = useState(''); // 현재 사용자의 ID
    const [currentUserName, setCurrentUserName] = useState(''); // 현재 사용자의 이름
    const messagesEndRef = useRef(null); // 스크롤을 위한 ref

    // 과거 메시지 불러오기 함수
    const fetchPastMessages = async () => {
        try {
            // API를 호출하여 과거 채팅 메시지를 가져옴
            const response = await api.get(`/chat/messages/${chatRoomId}`);
            // ****** 메시지 순서를 오래된 순으로 변경
            setMessages(response.data); // ****** ********
            console.log('과거 메시지 불러오기 성공:', response.data);
        } catch (error) {
            console.error('과거 메시지 불러오기에 실패했습니다:', error);
            // ****** 추가 에러 핸들링 코드 필요
        }
    };

    // 현재 사용자 정보 가져오기
    const fetchCurrentUserInfo = async () => {
        try {
            const response = await api.get('/chat/user-info'); // 사용자 정보 API 호출
            setCurrentUserId(response.data.memberId);
            setCurrentUserName(response.data.nickname); // API가 nickname을 반환한다고 가정
            console.log('현재 사용자 ID:', response.data.memberId);
            console.log('현재 사용자 이름:', response.data.nickname);
        } catch (error) {
            console.error('현재 사용자 정보를 가져오는 데 실패했습니다:', error);
            // ****** 추가 에러 핸들링 코드 필요
        }
    };

    useEffect(() => {
        fetchPastMessages(); // 컴포넌트가 마운트되면 과거 메시지를 불러옴
        fetchCurrentUserInfo(); // 현재 사용자 정보 가져오기
    }, [chatRoomId]); // chatRoomId가 변경될 때마다 다시 실행

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    };

    // 메시지 작성 시간을 포맷하는 함수
    const formatDateTime = (dateTime) => {
        return moment(dateTime).format('YYYY-MM-DD HH:mm'); // 날짜 및 시간을 포맷
    };

    return (
        <div className="chat-room-container"> {/* 전체 컨테이너 스타일 적용 */}
            {/* 채팅방 헤더 */}
            <div className="chat-room-header">
                <h1 className="chat-room-title">{currentUserName}과 {nickname}의 대화</h1>
                <button className="leave-button" onClick={() => navigate(-1)}>채팅방 나가기</button>
            </div>

            {/* 채팅방 정보 */}
            <ChatRoomInfo
                chatRoomId={chatRoomId}
                setNickname={setNickname} // nickname을 설정하는 함수 전달
            />

            {/* 채팅 메시지 목록 */}
            <div className="chat-messages-container">
                <ul className="chat-messages-list">
                    {messages.map((msg) => (
                        <li
                            key={msg.chatMessageId ? msg.chatMessageId : uuidv4()} // chatMessageId가 없으면 UUID 사용
                            className={msg.memberId == currentUserId ? 'my-message' : 'other-message'}>
                            <div className="message-content">
                                <strong className="sender-name">
                                    {msg.memberId == currentUserId ? '나' : (msg.nickName || '상대방')}
                                </strong>
                                <div className="message-text">
                                    {msg.message}
                                </div>
                            </div>
                            <div className="message-time">
                                {formatDateTime(msg.createdAt)}
                            </div>
                        </li>
                    ))}
                </ul>
                <div ref={messagesEndRef} /> {/* 스크롤을 맨 아래로 이동시키기 위한 요소 */}
            </div>

            {/* 채팅 입력 컴포넌트 */}
            <ChatComponent
                chatRoomId={chatRoomId} // chatRoomId를 props로 전달
                setMessages={setMessages} // WebSocket 메시지를 업데이트하는 함수 전달
                memberId={currentUserId} // 현재 사용자 ID를 memberId로 전달
            />
        </div>
    );

};

export default ChatRoomDetail;
