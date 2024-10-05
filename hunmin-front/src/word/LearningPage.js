import React, { useEffect, useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import api from '../axios';

const LearningPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const lang = queryParams.get('lang');
    const level = queryParams.get('level');

    const [words, setWords] = useState([]);
    const [timeLeft, setTimeLeft] = useState(0);
    const [showTranslation, setShowTranslation] = useState(false);
    const [showEndMessage, setShowEndMessage] = useState(false);
    const [fixedWordDisplays, setFixedWordDisplays] = useState([]);

    // 레벨에 따라 시간을 설정하는 함수
    const getTimeForLevel = (level) => {
        switch (level) {
            case '1': // Level 1
                return 30; // 1분
            case '2': // Level 2
                return 20; // 50초
            case '3': // Level 3
                return 10; // 40초
            default:
                return 30; // 기본값 1분
        }
    };

    useEffect(() => {
        const fetchWords = async () => {
            try {
                const response = await api.get(`/words/learning/start`, {
                    params: { lang, level },
                });
                setWords(response.data.words);
                setTimeLeft(getTimeForLevel(level)); // 레벨에 따른 초기화
            } catch (error) {
                console.error("Error fetching words:", error);
            }
        };

        fetchWords();
    }, [lang, level]);

    useEffect(() => {
        // 타이머가 설정되면 시작
        const countdown = setInterval(() => {
            if (timeLeft <= 1) {
                setShowTranslation(true);
                setShowEndMessage(true);
                clearInterval(countdown);
            } else {
                setTimeLeft(prevTime => prevTime - 1);
            }
        }, 1000);

        // 타이머가 변경될 때마다 클리어
        return () => {
            clearInterval(countdown);
        };
    }, [timeLeft]);

    const handleRetry = () => {
        window.location.reload();
    };

    const getRandomWordDisplay = (word) => {
        return Math.random() < 0.5 ? word.displayWord : word.displayTranslation;
    };

    useEffect(() => {
        if (words.length > 0) {
            const newFixedDisplays = words.map((word) => {
                return getRandomWordDisplay(word);
            });
            setFixedWordDisplays(newFixedDisplays);
        }
    }, [words]);

    return (
        <Container>
            <Box sx={{ backgroundColor: '#007bff', color: 'white', padding: 2, textAlign: 'center', marginBottom: 3, boxShadow: 3 }}>
                단어 학습
            </Box>
            <Button variant="contained" color="primary" onClick={() => navigate('/word-learning')} sx={{ marginBottom: 2 }}>돌아가기</Button>
            {!showEndMessage ? (
                <Typography variant="h6" align="center" color="#007bff">남은 시간: {timeLeft}초</Typography>
            ) : (
                <Typography variant="h6" align="center" color="#007bff" marginTop={3}>시간이 종료되었습니다!</Typography>
            )}
            <Button variant="contained" color="primary" onClick={handleRetry} sx={{ display: showEndMessage ? 'block' : 'none', margin: '20px auto' }}>다시하기</Button>
            <Box className="word-list" marginTop={2}>
                {words.map((word, index) => (
                    <Box key={index} sx={{ padding: 2, borderBottom: '1px solid #ccc', transition: 'background-color 0.3s ease', '&:hover': { backgroundColor: '#f1f1f1' } }}>
                        <Typography variant="body1" fontWeight="bold">{fixedWordDisplays[index]}</Typography>
                        {showTranslation && (
                            <Box>
                                <Typography variant="body2" color="textSecondary">{word.displayTranslation}</Typography>
                                <Typography variant="body2" color="textSecondary">{word.definition}</Typography>
                            </Box>
                        )}
                    </Box>
                ))}
            </Box>
        </Container>
    );
};

export default LearningPage;
