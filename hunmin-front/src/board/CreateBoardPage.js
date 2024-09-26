import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const CreateBoardPage = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async () => {
        try {
            const boardData = { title, content, memberId: 1 }; // Replace memberId with actual userId
            await axios.post('http://localhost:8080/api/board', boardData);
            navigate('/');
        } catch (error) {
            console.error('Error creating board:', error);
        }
    };

    return (
        <div>
            <h1>Create New Board</h1>
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            <div></div>
            <textarea
                placeholder="Content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
            />
            <button onClick={handleSubmit}>Create</button>
        </div>
    );
};

export default CreateBoardPage;
