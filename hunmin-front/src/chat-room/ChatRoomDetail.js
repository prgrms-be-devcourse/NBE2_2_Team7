import React, { useState, useEffect } from 'react';
import api from '../axios'; // 올바른 상대 경로
import { useParams } from 'react-router-dom';
import './styles.css';


const ChatRoomDetail = () => {
    const { chatRoomId } = useParams();
    const [chatRoom, setChatRoom] = useState(null);

    useEffect(() => {
        api.get(`/api/chat-room/${chatRoomId}`)
            .then(response => {
                setChatRoom(response.data);
            })
            .catch(error => {
                console.error('Error fetching chat room details:', error);
            });
    }, [chatRoomId]);

    if (!chatRoom) return <div>Loading...</div>;

    return (
        <div className="container" style={{ maxWidth: '800px', marginTop: '20px' }}>
            <h3>{chatRoom.nickname} Chat Room</h3>
            <p>Number of Users: {chatRoom.userCount}</p>
            <p>Latest Message: {chatRoom.latestMessageContent}</p>
            <small>Last Active: {chatRoom.latestMessageDate}</small>
        </div>
    );
};

export default ChatRoomDetail;