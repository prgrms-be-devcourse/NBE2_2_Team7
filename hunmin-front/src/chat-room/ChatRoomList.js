// // ChatRoomList.js
import React, { useState, useEffect, useRef } from 'react';
import api from '../axios'; // 올바른 상대 경로
import './styles.css';  // CSS 파일 import

const ChatRoomCard = ({ room, onEnter, onRightClick }) => {
    const cardRef = useRef(null);

    return (
        <div
            key={room.chatRoomId}
            className="chatroom-card"
            onClick={() => onEnter(room.chatRoomId)}
            onContextMenu={(e) => onRightClick(e, room.chatRoomId, cardRef)}
            ref={cardRef}
        >

            <div className="chatroom-header">
                <h6>{room.nickname}</h6>
            </div>
            <div className="chatroom-body">
                <p>최근 메시지: {room.latestMessageContent || 'No recent messages.'}</p>
            </div>
            <div className="chatroom-footer">
                <small>최근 활동: {room.latestMessageDate || 'N/A'}</small>
            </div>
        </div>
    );
};

const ChatRoomList = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const [selectedChatRoom, setSelectedChatRoom] = useState(null);
    const containerRef = useRef(null);

    useEffect(() => {
        api.get('/api/chat-room/list')
            .then(response => {
                setChatRooms(response.data);
            })
            .catch(error => {
                console.error('Error fetching chat rooms:', error);
            });

        const handleClickOutside = (event) => {
            if (popupVisible && containerRef.current && !containerRef.current.contains(event.target)) {
                setPopupVisible(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [popupVisible]);

    const enterRoom = (chatRoomId) => {
        window.location.href = `/chat-room/${chatRoomId}`;
    };

    const handleRightClick = (event, chatRoomId, cardRef) => {
        event.preventDefault(); // Prevent the default context menu
        setPopupPosition({
            x: event.clientX,  // Use event.clientX for horizontal position
            y: event.clientY   // Use event.clientY for vertical position
        });
        setSelectedChatRoom(chatRoomId);
        setPopupVisible(true);
    };

    const deleteChatRoom = () => {
        if (selectedChatRoom) {
            api.delete(`/api/chat-room/${selectedChatRoom}`)
                .then(() => {
                    setChatRooms(chatRooms.filter(room => room.chatRoomId !== selectedChatRoom));
                    alert('Chat room deleted successfully.');
                })
                .catch(error => {
                    console.error('Error deleting chat room:', error);
                    alert('Failed to delete chat room.');
                });
            setPopupVisible(false);
        }
    };

    const handleClosePopup = () => {
        setPopupVisible(false);
    };

    return (
        <div className="container" ref={containerRef}>
            <div className="d-flex justify-content-between align-items-center mb-3">
            </div>
            {chatRooms.map(room => (
                <ChatRoomCard
                    key={room.chatRoomId}
                    room={room}
                    onEnter={enterRoom}
                    onRightClick={handleRightClick}
                />
            ))}
            {popupVisible && (
                <div
                    className="popup-menu"
                    style={{
                        top: popupPosition.y,
                        left: popupPosition.x
                    }}
                >
                    <button onClick={deleteChatRoom}>채팅방 삭제</button>
                </div>
            )}
        </div>
    );
};

export default ChatRoomList;
