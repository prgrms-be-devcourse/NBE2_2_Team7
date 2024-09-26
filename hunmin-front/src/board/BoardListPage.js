import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const BoardListPage = () => {
    const [boards, setBoards] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);

    useEffect(() => {
        fetchBoards();
    }, [page, size]);

    const fetchBoards = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/board', {
                params: { page, size },
            });
            setBoards(response.data.content);
        } catch (error) {
            console.error('Error fetching boards:', error);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    return (
        <div>
            <h1>게시판</h1>
            <Link to="/create-board">
                <button>게시글 작성</button>
            </Link>
            <ul>
                {boards.map((board) => (
                    <li key={board.boardId}>
                        <Link to={`/board/${board.boardId}`}>
                            <strong>{board.title}</strong> - {board.nickname} <br />
                            {board.updatedAt ? (
                                <>
                                    <span>수정일: {formatDate(board.updatedAt)}</span>
                                </>
                            ) : (
                                <>
                                    <span>작성일: {formatDate(board.createdAt)}</span>
                                </>
                            )}
                            <br />
                            {board.location && (
                                <span>Location: {board.location}</span>
                            )}
                        </Link>
                    </li>
                ))}
            </ul>
            <div>
                <button disabled={page === 1} onClick={() => setPage(page - 1)}>
                    이전
                </button>
                <button onClick={() => setPage(page + 1)}>다음</button>
            </div>
        </div>
    );
};

export default BoardListPage;
