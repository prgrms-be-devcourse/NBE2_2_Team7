import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import CommentPage from '../comment/CommentPage';
import KakaoMapSearch from './KakaoMapSearch'; // 추가

const BoardDetailPage = () => {
    const { boardId } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [isEditMode, setIsEditMode] = useState(false);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [location, setLocation] = useState(null); // 선택한 위치 상태
    const [originalLocation, setOriginalLocation] = useState(null); // 수정 전 위치 상태

    useEffect(() => {
        fetchBoard();
    }, [boardId]);

    const fetchBoard = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/board/${boardId}`);
            setBoard(response.data);
            setTitle(response.data.title);
            setContent(response.data.content);
            setOriginalLocation({
                name: response.data.location,
                latitude: response.data.latitude,
                longitude: response.data.longitude,
            }); // 수정 전 위치 정보 설정
            setLocation({
                name: response.data.location,
                latitude: response.data.latitude,
                longitude: response.data.longitude,
            }); // 수정 전 위치 정보와 동일하게 설정
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
        if (!location) {
            alert('위치를 선택해 주세요!');
            return;
        }

        try {
            const updatedBoard = {
                title,
                content,
                location: location.name, // 수정 후 위치 정보 추가
                latitude: location.latitude,
                longitude: location.longitude,
            };
            await axios.put(`http://localhost:8080/api/board/${boardId}`, updatedBoard);
            setIsEditMode(false);
            fetchBoard();
        } catch (error) {
            console.error('Error updating board:', error);
        }
    };

    const handleLocationSelect = (selectedLocation) => {
        setLocation(selectedLocation); // 새로운 위치 정보 업데이트
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
                    <h2>게시글 수정</h2>
                    제목
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <div></div>
                    내용
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                    <div>
                    </div>
                    장소
                    <KakaoMapSearch onLocationSelect={handleLocationSelect} /> {/* 장소 검색 추가 */}
                    <div>
                        <h4>수정 전 장소: {originalLocation?.name || '없음'}</h4>
                        <h4>수정 후 장소: {location?.name || '선택된 장소 없음'}</h4>
                    </div>
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
