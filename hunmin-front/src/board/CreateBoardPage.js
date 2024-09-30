import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import KakaoMapSearch from './KakaoMapSearch';
import { TextField, Button, Typography, Container, Box, Paper } from '@mui/material';

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
        <Container maxWidth="sm">
            <Paper elevation={3} style={{ padding: '20px', marginTop: '20px' }}>
                <Typography variant="h4" gutterBottom>
                    게시글 작성
                </Typography>
                <Box component="form" noValidate autoComplete="off">
                    <TextField
                        fullWidth
                        label="제목"
                        variant="outlined"
                        margin="normal"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <TextField
                        fullWidth
                        label="내용"
                        variant="outlined"
                        margin="normal"
                        multiline
                        rows={4}
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                    <Typography variant="subtitle1" gutterBottom>
                        장소
                    </Typography>
                    <KakaoMapSearch onLocationSelect={handleLocationSelect} />
                    <Button
                        variant="contained"
                        color="primary"
                        fullWidth
                        style={{ marginTop: '20px' }}
                        onClick={handleSubmit}
                    >
                        저장
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default CreateBoardPage;
