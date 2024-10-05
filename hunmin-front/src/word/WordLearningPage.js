import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Container, Typography, Button } from '@mui/material';

const WordLearningPage = () => {
    const navigate = useNavigate();

    const handleLanguageSelect = (language) => {
        // 선택한 언어로 레벨 선택 페이지로 이동
        navigate(`/word-learning/levelSelect?lang=${language}`);
    };

    const handleDictionaryPage = () => {
        navigate('/word-management'); // 단어사전 페이지로 이동
    };

    return (
        <Container>
            {/* 배너 영역 */}
            <Box sx={{
                backgroundColor: '#007bff',
                color: 'white',
                padding: 2,
                textAlign: 'center',
                marginBottom: 3,
                boxShadow: 3
            }}>
                단어 학습 - 언어 선택
            </Box>

            <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ marginTop: 1,
                    marginBottom: 3}}
                onClick={handleDictionaryPage}
            >
                단어사전
            </Button>

            {/* 본문 컨테이너 */}
            <Box sx={{
                maxWidth: '500px',
                margin: '0 auto',
                padding: 3,
                backgroundColor: '#ffffff',
                borderRadius: 2,
                boxShadow: 3,
                textAlign: 'center'
            }}>
                <Typography variant="h5" sx={{ marginBottom: 3, fontWeight: 'bold', color: '#007bff' }}>
                    언어를 선택하세요
                </Typography>

                {/* 언어 선택 버튼들 */}
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ marginBottom: 2 }}
                    onClick={() => handleLanguageSelect('영어')}
                >
                    영어 (English)
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ marginBottom: 2 }}
                    onClick={() => handleLanguageSelect('일본어')}
                >
                    일본어 (日本語)
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ marginBottom: 2 }}
                    onClick={() => handleLanguageSelect('중국어')}
                >
                    중국어 (中文)
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ marginBottom: 2 }}
                    onClick={() => handleLanguageSelect('베트남어')}
                >
                    베트남어 (Tiếng Việt)
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    onClick={() => handleLanguageSelect('프랑스어')}
                >
                    프랑스어 (Français)
                </Button>
            </Box>
        </Container>
    );
};

export default WordLearningPage;
