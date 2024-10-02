import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../axios';
import Map from './map/KakaoMap'; // Map 컴포넌트 임포트
import { FaUserCircle } from 'react-icons/fa'; // 프로필 아이콘 임포트
import {
    Box,
    Button,
    Typography,
    List,
    ListItem,
    ListItemText,
    TextField,
    AppBar,
    Toolbar,
    IconButton,
    Container,
    Pagination,
    Grid, // Grid 컴포넌트 임포트
} from '@mui/material';

const BoardListPage = ({ memberName }) => {
    const memberId = 3;
    const [boards, setBoards] = useState([]);
    const [filteredBoards, setFilteredBoards] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수 상태 추가
    const [searchLocation, setSearchLocation] = useState('');
    const [mapCenter, setMapCenter] = useState({ lat: 37.5665, lng: 126.978 }); // 초기 지도 중심 설정
    const [mapLevel, setMapLevel] = useState(9); // 지도 레벨 상태 추가
    const [showMyBoards, setShowMyBoards] = useState(false); // 내 작성글 보기 상태

    useEffect(() => {
        if (showMyBoards) {
            fetchMyBoards(); // 내 게시글 목록 가져오기
        } else {
            fetchBoards(); // 전체 게시글 목록 가져오기
        }
    }, [page, size, showMyBoards]);

    useEffect(() => {
        setFilteredBoards(boards);
    }, [boards]);

    const fetchBoards = async () => {
        try {
            const response = await api.get('/board', {
                params: { page, size },
            });
            setBoards(response.data.content);
            setTotalPages(response.data.totalPages); // 전체 페이지 수 설정
        } catch (error) {
            console.error('Error fetching boards:', error);
        }
    };

    const fetchMyBoards = async () => {
        try {
            const response = await api.get(`/board/member/${memberId}`, {
                params: { page, size },
            });
            setBoards(response.data.content);
            setTotalPages(response.data.totalPages); // 전체 페이지 수 설정
        } catch (error) {
            console.error('Error fetching my boards:', error);
        }
    };

    const handleSearch = async () => {
        const kakao = window.kakao;
        const ps = new kakao.maps.services.Places();

        ps.keywordSearch(searchLocation, (data, status) => {
            if (status === kakao.maps.services.Status.OK) {
                const firstPlace = data[0];
                const center = { lat: firstPlace.y, lng: firstPlace.x };

                const nearbyBoards = boards.filter(board => {
                    const distance = getDistance(board.latitude, board.longitude, center.lat, center.lng);
                    return distance <= 5000;
                });

                setFilteredBoards(nearbyBoards);
                setMapCenter(center);
                setMapLevel(5); // 검색 후 지도 확대
            } else {
                setFilteredBoards([]);
                setMapLevel(9); // 초기 레벨로 설정
            }
        });
    };

    const getDistance = (lat1, lon1, lat2, lon2) => {
        const R = 6371e3;
        const φ1 = lat1 * Math.PI / 180;
        const φ2 = lat2 * Math.PI / 180;
        const Δφ = (lat2 - lat1) * Math.PI / 180;
        const Δλ = (lon2 - lon1) * Math.PI / 180;

        const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
            Math.cos(φ1) * Math.cos(φ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    return (
        <Container>
            <AppBar position="static">
                <Toolbar style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
                    <IconButton edge="start" color="inherit" aria-label="logo">
                        <FaUserCircle size={30} />
                    </IconButton>
                    <Typography variant="h6" style={{ marginLeft: '10px' }}>
                        {memberName}
                    </Typography>
                    <Button color="inherit" onClick={() => setShowMyBoards(!showMyBoards)} style={{ marginLeft: '10px' }}>
                        {showMyBoards ? '전체 글 보기' : '내 글 보기'}
                    </Button>
                </Toolbar>
            </AppBar>

            <Box mt={2}>
                <Typography variant="h4">{showMyBoards ? '내 글' : '전체 글'}</Typography>
                <Link to="/create-board">
                    <Button variant="contained" color="primary">게시글 작성</Button>
                </Link>
            </Box>

            <Grid container spacing={2} mt={2}>
                <Grid item xs={12} md={6}>
                    <List>
                        {filteredBoards.map((board) => (
                            <ListItem key={board.boardId}>
                                <Grid container alignItems="center">
                                    <Grid item xs={10}>
                                        <ListItemText
                                            primary={
                                                <Link to={`/board/${board.boardId}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                                    <strong>{board.title}</strong> - {board.nickname}
                                                </Link>
                                            }
                                            secondary={
                                                <>
                                                    {board.updatedAt ? (
                                                        <span>수정일: {formatDate(board.updatedAt)}</span>
                                                    ) : (
                                                        <span>작성일: {formatDate(board.createdAt)}</span>
                                                    )}
                                                    {board.location && (
                                                        <span> 장소: {board.location}</span>
                                                    )}
                                                </>
                                            }
                                        />
                                    </Grid>

                                    {/* 썸네일 이미지가 있을 경우 오른쪽에 표시 */}
                                    {board.imageUrls && board.imageUrls.length > 0 ? (
                                        <Grid item xs={2}>
                                            <img
                                                src={board.imageUrls[0]} // 첫 번째 이미지를 썸네일로 사용
                                                alt={board.title}
                                                style={{ width: '100%', height: 'auto', borderRadius: '8px' }}
                                            />
                                        </Grid>
                                    ) : (
                                        <Grid item xs={2}>
                                            {/* 이미지가 없는 경우 빈 공간으로 유지 */}
                                            <div style={{
                                                width: '100%',
                                                height: '100%',
                                                backgroundColor: '#f0f0f0',
                                                borderRadius: '8px',
                                            }}>
                                            </div>
                                        </Grid>
                                    )}
                                </Grid>
                            </ListItem>
                        ))}
                    </List>
                    <Pagination
                        count={totalPages}
                        page={page}
                        onChange={(event, value) => setPage(value)}
                        color="primary"
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <TextField
                        label="위치를 입력하세요"
                        variant="outlined"
                        value={searchLocation}
                        onChange={(e) => setSearchLocation(e.target.value)}
                        fullWidth
                    />
                    <Button variant="contained" color="primary" onClick={handleSearch} style={{ marginTop: '10px' }}>
                        검색
                    </Button>
                    <Map boards={filteredBoards} mapLevel={mapLevel} mapCenter={mapCenter} />
                </Grid>
            </Grid>
        </Container>
    );
};

export default BoardListPage;
