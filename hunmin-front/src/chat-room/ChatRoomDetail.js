import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './styles.css';


const ChatRoomDetail = () => {
    const { chatRoomId } = useParams();
    const [chatRoom, setChatRoom] = useState(null);

    useEffect(() => {
        axios.get(`/api/chat-room/${chatRoomId}`)
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
