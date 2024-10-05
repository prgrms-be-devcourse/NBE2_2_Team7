import React, { useState } from 'react';
import api from '../axios';
import ChatRoomList from './ChatRoomList';
import './styles.css';

const CreateChatRoom = () => {
    const [nickname, setNickname] = useState('');

    const createRoom = () => {
        if (!nickname) {
            alert('Please enter a nickname.');
            return;
        }

        api.post('/api/chat-room', null, {
            params: { nickName: nickname }
        })
            .then(response => {
                alert(`Room [${response.data.nickName}] has been created.`);
                setNickname('');
            })
            .catch(error => {
                alert('Failed to create chat room.');
                console.error('Error creating chat room:', error);
            });
    };

    return (
        <div className="container" style={{maxWidth: '800px', marginTop: '20px'}}>
            <div className="header">
                <a className="btn-home" href="/">Home</a> {/* Aligned to the right */}
            </div>
            <h1>채팅방 목록</h1>
            <div className="input-group">
                <input
                    type="text"
                    className="form-control"
                    value={nickname}
                    onChange={e => setNickname(e.target.value)}
                    placeholder="닉네임을 입력하시오."
                />
                <button className="btn btn-primary" onClick={createRoom}>채팅방 개설</button>
            </div>
            <ChatRoomList/>
        </div>
    );
};

export default CreateChatRoom;
