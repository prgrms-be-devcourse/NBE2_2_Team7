import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CommentPage from '../comment/CommentPage';
import KakaoMapSearch from './map/KakaoMapSearch';
import { Typography, Button, TextField, Grid, Paper } from '@mui/material';
import LocationOnIcon from "@mui/icons-material/LocationOn";
import BoardWrite from '../board/write/BoardWrite'; // BoardWrite 컴포넌트 추가
import api from '../axios';

const BoardDetailPage = () => {
    const { boardId } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [isEditMode, setIsEditMode] = useState(false);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [location, setLocation] = useState(null);
    const [originalLocation, setOriginalLocation] = useState(null);
    const [imageUrls, setImageUrls] = useState([]); // 이미지 URL 상태 추가
    const [memberId, setMemberId] = useState(''); // memberId 상태 추가

    useEffect(() => {
        fetchBoard();
    }, [boardId]);

    const fetchBoard = async () => {
        try {
            const response = await api.get(`/board/${boardId}`);
            setBoard(response.data);
            setTitle(response.data.title);
            setContent(response.data.content);
            setOriginalLocation({
                name: response.data.location,
                latitude: response.data.latitude,
                longitude: response.data.longitude,
            });
            setLocation({
                name: response.data.location,
                latitude: response.data.latitude,
                longitude: response.data.longitude,
            });
            setImageUrls(response.data.imageUrls || []); // 이미지 URL 초기화
        } catch (error) {
            console.error('Error fetching board:', error);
        }
    };

    const handleDelete = async () => {
        try {
            await api.delete(`/board/${boardId}`);
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
                memberId : 3,
                title,
                content,
                location: location.name,
                latitude: location.latitude,
                longitude: location.longitude,
                imageUrls: imageUrls.length > 0 ? imageUrls : null, // 이미지 URL 추가
            };
            await api.put(`/board/${boardId}`, updatedBoard);
            setIsEditMode(false);
            fetchBoard();
        } catch (error) {
            console.error('Error updating board:', error);
        }
    };

    const handleLocationSelect = (selectedLocation) => {
        setLocation(selectedLocation);
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    const uploadImage = async (file) => {
        const formData = new FormData();
        formData.append('files', file);

        try {
            const response = await api.post('/board/uploadImage', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log('Uploaded image URL:', response.data[0]); // URL을 배열에서 가져옴
            return response.data[0]; // 첫 번째 URL 반환
        } catch (error) {
            console.error('Image upload failed:', error);
            return null;
        }
    };

    if (!board) {
        return <div>Loading...</div>;
    }

    return (
        <div style={{ padding: '20px' }}>
            <Paper elevation={3} style={{ padding: '20px' }}>
                {isEditMode ? (
                    <div>
                        <Typography variant="h5">게시글 수정</Typography>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    label="제목"
                                    variant="outlined"
                                    fullWidth
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <BoardWrite
                                    value={content}
                                    onChange={setContent}
                                    uploadImage={uploadImage} // 이미지 업로드 함수 전달
                                    setImageUrls={setImageUrls} // 이미지 URL 상태 변경 함수 전달
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <KakaoMapSearch onLocationSelect={handleLocationSelect} />
                                <Typography variant="subtitle1">수정 전 장소: {originalLocation?.name || '없음'}</Typography>
                                <Typography variant="subtitle1">수정 후 장소: {location?.name || '선택된 장소 없음'}</Typography>
                            </Grid>
                            <Grid item xs={12}>
                                <Button variant="contained" color="primary" onClick={handleUpdate}>저장</Button>
                                <Button variant="outlined" color="secondary" onClick={() => setIsEditMode(false)}>취소</Button>
                            </Grid>
                        </Grid>
                    </div>
                ) : (
                    <div>
                        <Typography variant="h5">{board.title}</Typography>
                        <Typography variant="body1">
                            <strong>작성자 :</strong> {board.nickname}
                            <span style={{ margin: '0 8px' }} /> {/* 공백 추가 */}
                            <strong>{board.updatedAt ? '수정일 :' : '작성일 :'}</strong> {formatDate(board.updatedAt || board.createdAt)}
                            <span style={{ margin: '0 8px' }} /> {/* 공백 추가 */}
                            {board.location && (
                                <>
                                    <LocationOnIcon style={{ marginRight: '4px' }} />
                                    {board.location}
                                </>
                            )}
                        </Typography>
                        <hr />
                        <Typography variant="body1" dangerouslySetInnerHTML={{ __html: board.content }} />
                        <Grid item xs={12}>
                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                                <Button variant="contained" color="primary" onClick={() => setIsEditMode(true)}>수정</Button>
                                <Button variant="outlined" color="secondary" onClick={handleDelete}>삭제</Button>
                            </div>
                        </Grid>
                    </div>
                )}
            </Paper>
            <CommentPage boardId={boardId} />
        </div>
    );
};

export default BoardDetailPage;
