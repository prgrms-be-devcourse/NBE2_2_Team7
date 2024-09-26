import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import CommentPage from '../comment/CommentPage';

const BoardDetailPage = () => {
    const { boardId } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [isEditMode, setIsEditMode] = useState(false);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    useEffect(() => {
        fetchBoard();
    }, [boardId]);

    const fetchBoard = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/board/${boardId}`);
            setBoard(response.data);
            setTitle(response.data.title);
            setContent(response.data.content);
        } catch (error) {
            console.error('Error fetching board:', error);
        }
    };

    const handleDelete = async () => {
        try {
            await axios.delete(`http://localhost:8080/api/board/${boardId}`);
            navigate('/');
        } catch (error) {
            console.error('Error deleting board:', error);
        }
    };

    const handleUpdate = async () => {
        try {
            const updatedBoard = { title, content };
            await axios.put(`http://localhost:8080/api/board/${boardId}`, updatedBoard);
            setIsEditMode(false);
            fetchBoard();
        } catch (error) {
            console.error('Error updating board:', error);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    if (!board) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            {isEditMode ? (
                <div>
                    <h2>Edit Board</h2>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                    <button onClick={handleUpdate}>Update</button>
                    <button onClick={() => setIsEditMode(false)}>Cancel</button>
                </div>
            ) : (
                <div>
                    <h2>제목: {board.title}</h2>
                    <p><strong>작성자:</strong> {board.nickname}</p>
                    <p>
                        <strong>{board.updatedAt ? '수정일' : '작성일'}:</strong> {formatDate(board.updatedAt || board.createdAt)}
                    </p>
                    {board.location && (
                        <p><strong>Location:</strong> {board.location}</p>
                    )}
                    <p>내용: {board.content}</p>
                    <button onClick={() => setIsEditMode(true)}>수정</button>
                    <button onClick={handleDelete}>삭제</button>
                </div>
            )}

            <CommentPage boardId={boardId} />
        </div>
    );
};

export default BoardDetailPage;
