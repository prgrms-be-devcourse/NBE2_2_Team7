import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import KakaoMapSearch from './KakaoMapSearch';

const CreateBoardPage = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [location, setLocation] = useState(null); // 추가: 위치 정보 상태
    const navigate = useNavigate();

    const handleLocationSelect = (selectedLocation) => {
        setLocation(selectedLocation); // 위치 정보를 상태에 저장
    };

    const handleSubmit = async () => {
        if (!location) {
            alert('위치를 선택해 주세요!');
            return;
        }

        try {
            const boardData = {
                title,
                content,
                location: location.name, // 위치 이름
                latitude: location.latitude, // 위도
                longitude: location.longitude, // 경도
                memberId: 1, // Replace with actual userId
            };
            await axios.post('http://localhost:8080/api/board', boardData);
            navigate('/');
        } catch (error) {
            console.error('Error creating board:', error);
        }
    };

    return (
        <div>
            <h1>게시글 작성</h1>
            제목
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            <div></div>
            내용
            <textarea
                placeholder="Content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
            />
            <div></div>
            장소
            <KakaoMapSearch onLocationSelect={handleLocationSelect} />
            <button onClick={handleSubmit}>저장</button>
        </div>
    );
};

export default CreateBoardPage;
