import React, { useState } from 'react';
import api from '../axios'; // Axios 인스턴스 import

const ChatEditComponent = ({ chatMessageId, originalMessage, onUpdateSuccess, onCancel }) => {
    const [editedMessage, setEditedMessage] = useState(originalMessage);

    const handleUpdate = async () => {
        try {
            const response = await api.put('/chat', {
                chatMessageId: chatMessageId,
                message: editedMessage,
            });
            console.log('메시지 수정 성공:', response.data);
            onUpdateSuccess(response.data);
        } catch (error) {
            console.error('메시지 수정 실패:', error);
            // 에러 처리 추가 가능
        }
    };

    return (
        <div className="chat-edit-container">
      <textarea
          value={editedMessage}
          onChange={(e) => setEditedMessage(e.target.value)}
          className="chat-edit-textarea"
      />
            <div className="chat-edit-buttons">
                <button onClick={handleUpdate} className="chat-edit-save-button">
                    저장
                </button>
                <button onClick={onCancel} className="chat-edit-cancel-button">
                    취소
                </button>
            </div>
        </div>
    );
};

export default ChatEditComponent;
