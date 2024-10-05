import React, { useState, useEffect } from 'react';
import { Box, Container, Typography, Button, List, ListItem, ListItemText, Pagination } from '@mui/material';
import { Link, useLocation } from 'react-router-dom';
import api from '../axios';

const WordListPage = () => {
    const [lang, setLang] = useState('');
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [words, setWords] = useState([]);

    const location = useLocation();

    const fetchWords = async (selectedLang, currentPage) => {
        try {
            const params = {
                page: currentPage,
                size: 10,
                lang: selectedLang, // 선택된 언어를 여기 추가
            };

            const response = await api.get('/words', { params });
            setWords(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('단어를 가져오는 중 오류 발생:', error);
        }
    };

    useEffect(() => {
        // URL 쿼리에서 언어를 가져옴
        const queryParams = new URLSearchParams(location.search);
        const language = queryParams.get('lang');
        if (language) {
            setLang(language);
            fetchWords(language, page); // 언어가 결정되면 단어를 가져옴
        }
    }, [location.search, page]);

    const handlePageChange = (event, value) => {
        setPage(value);
        fetchWords(lang, value); // 선택한 페이지의 단어를 새로 가져옴
    };

    return (
        <Container maxWidth="md">
            <Box sx={{
                backgroundColor: '#007bff',
                color: 'white',
                padding: 2,
                textAlign: 'center',
                marginBottom: 3,
                boxShadow: 3
            }}>
                단어 목록
            </Box>

            {lang && (
                <Box sx={{ marginBottom: 3 }}>
                    <Typography variant="h6" color="textSecondary" align="center" sx={{ marginBottom: 2 }}>
                        언어: {lang}
                    </Typography>
                </Box>
            )}

            {lang && (
                <Box
                    sx={{
                        backgroundColor: '#ffffff',
                        padding: 3,
                        borderRadius: 2,
                        boxShadow: 3,
                    }}
                >
                    <Typography variant="h5" color="primary" fontWeight="bold" gutterBottom>
                        단어 목록
                    </Typography>

                    <List>
                        {words.length > 0 ? words.map((word) => (
                            <ListItem key={word.title}>
                                <ListItemText
                                    primary={<Link to={`/words/view?title=${word.title}&lang=${word.lang}`}>{word.title}</Link>}
                                    secondary={word.translation}
                                />
                                <Button
                                    variant="outlined"
                                    color="primary"
                                    component={Link}
                                    to={`/words/update/check?title=${word.title}&lang=${word.lang}`}
                                    sx={{ marginRight: 1 }}
                                >
                                    수정
                                </Button>
                                <Button
                                    variant="outlined"
                                    color="secondary"
                                    component={Link}
                                    to={`/words/delete/check?title=${word.title}&lang=${word.lang}`}
                                >
                                    삭제
                                </Button>
                            </ListItem>
                        )) : (
                            <Typography variant="body1" color="textSecondary">
                                해당 언어에 대한 단어가 없습니다.
                            </Typography>
                        )}
                    </List>
                </Box>
            )}

            {lang && (
                <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: 3 }}>
                    <Pagination
                        count={totalPages}
                        page={page}
                        onChange={handlePageChange}
                        color="primary"
                    />
                </Box>
            )}

            <Box sx={{ textAlign: 'center', marginTop: 3 }}>
                <Button component={Link} to="/word-management" variant="contained" color="primary" sx={{ marginRight: 1 }}>
                    사전으로 돌아가기
                </Button>
                <Button component={Link} to="/word-view" variant="outlined" sx={{ marginRight: 1 }}>
                    단어 검색
                </Button>
            </Box>
        </Container>
    );
};

export default WordListPage;
