import React from 'react';
import api from '../axios'; // Axios 인스턴스 import

const ChatDeleteComponent = ({ chatMessageId, onDeleteSuccess, onCancel }) => {
    const handleDelete = async () => {
        try {
            const response = await api.delete(`/chat/${chatMessageId}`);
            console.log('메시지 삭제 성공:', response.data);
            onDeleteSuccess(chatMessageId);
        } catch (error) {
            console.error('메시지 삭제 실패:', error);
            // 에러 처리 추가 가능
        }
    };

    return (
        <div className="chat-delete-container">
            <p>이 메시지를 삭제하시겠습니까?</p>
            <div className="chat-delete-buttons">
                <button onClick={handleDelete} className="chat-delete-confirm-button">
                    삭제
                </button>
                <button onClick={onCancel} className="chat-delete-cancel-button">
                    취소
                </button>
            </div>
        </div>
    );
};

export default ChatDeleteComponent;
