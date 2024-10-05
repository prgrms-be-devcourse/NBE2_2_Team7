// src/components/ChatRoomList.js
import React, { useState, useEffect } from 'react';
import api from '../axios'; // Axios 인스턴스 경로 확인
import {
    Box,
    Card,
    CardHeader,
    CardContent,
    CardActions,
    Typography,
    IconButton,
    Menu,
    MenuItem,
    List,
    ListItem,
    Divider,
    Snackbar,
    Alert,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField,
} from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteIcon from '@mui/icons-material/Delete';

const ChatRoomCard = ({ room, onEnter, onRightClick }) => {
    return (
        <Card
            sx={{ marginBottom: 2, cursor: 'pointer' }}
            onClick={() => onEnter(room.chatRoomId)}
            onContextMenu={(e) => onRightClick(e, room.chatRoomId)}
        >
            <CardHeader
                title={room.nickname}
                action={
                    <IconButton onClick={(e) => onRightClick(e, room.chatRoomId)}>
                        <MoreVertIcon />
                    </IconButton>
                }
            />
            <CardContent>
                <Typography variant="body2" color="text.secondary">
                    최근 메시지: {room.latestMessageContent || '최근 메시지가 없습니다.'}
                </Typography>
            </CardContent>
            <CardActions>
                <Typography variant="caption" color="text.secondary">
                    최근 활동: {room.latestMessageDate ? new Date(room.latestMessageDate).toLocaleString() : 'N/A'}
                </Typography>
            </CardActions>
        </Card>
    );
};

const ChatRoomList = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);
    const [selectedChatRoom, setSelectedChatRoom] = useState(null);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
    const [openDialog, setOpenDialog] = useState(false);
    const [newNickName, setNewNickName] = useState('');

    useEffect(() => {
        // 인증된 사용자인지 확인 후 요청
        api.get('/chat-room/list')
            .then(response => {
                setChatRooms(response.data);
            })
            .catch(error => {
                console.error('채팅방 가져오기 오류:', error);
                setSnackbar({
                    open: true,
                    message: '채팅방 목록을 불러오는데 실패했습니다.',
                    severity: 'error',
                });
            });
    }, []);

    const enterRoom = (chatRoomId) => {
        window.location.href = `/chat-room/${chatRoomId}`;
    };

    const handleMenuOpen = (event, chatRoomId) => {
        event.stopPropagation(); // onClick 트리거 방지
        setAnchorEl(event.currentTarget);
        setSelectedChatRoom(chatRoomId);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
        setSelectedChatRoom(null);
    };

    const deleteChatRoom = () => {
        if (selectedChatRoom) {
            api.delete(`/chat-room/${selectedChatRoom}`)
                .then(() => {
                    setChatRooms(chatRooms.filter(room => room.chatRoomId !== selectedChatRoom));
                    setSnackbar({
                        open: true,
                        message: '채팅방이 성공적으로 삭제되었습니다.',
                        severity: 'success',
                    });
                })
                .catch(error => {
                    console.error('채팅방 삭제 오류:', error);
                    setSnackbar({
                        open: true,
                        message: '채팅방 삭제에 실패했습니다.',
                        severity: 'error',
                    });
                });
            handleMenuClose();
        }
    };

    const handleCloseSnackbar = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    const handleOpenDialog = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setNewNickName('');
    };

    const handleCreateChatRoom = () => {
        if (newNickName.trim() === '') {
            setSnackbar({
                open: true,
                message: '닉네임을 입력해주세요.',
                severity: 'warning',
            });
            return;
        }

        api.post(`/chat-room/${newNickName}`)
            .then(response => {
                setChatRooms([...chatRooms, response.data]);
                setSnackbar({
                    open: true,
                    message: '채팅방이 성공적으로 생성되었습니다.',
                    severity: 'success',
                });
                handleCloseDialog();
            })
            .catch(error => {
                console.error('채팅방 생성 오류:', error);
                setSnackbar({
                    open: true,
                    message: '채팅방 생성에 실패했습니다.',
                    severity: 'error',
                });
            });
    };

    return (
        <Box sx={{ maxWidth: 600, margin: 'auto', padding: 2 }}>
            <Typography variant="h4" gutterBottom>
                채팅방 목록
            </Typography>
            <Button variant="contained" color="primary" onClick={handleOpenDialog} sx={{ marginBottom: 2 }}>
                채팅방 생성
            </Button>
            <List>
                {chatRooms.map(room => (
                    <React.Fragment key={room.chatRoomId}>
                        <ListItem>
                            <ChatRoomCard
                                room={room}
                                onEnter={enterRoom}
                                onRightClick={handleMenuOpen}
                            />
                        </ListItem>
                        <Divider component="li" />
                    </React.Fragment>
                ))}
            </List>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <MenuItem onClick={deleteChatRoom}>
                    <DeleteIcon fontSize="small" sx={{ marginRight: 1 }} />
                    채팅방 삭제
                </MenuItem>
            </Menu>
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>채팅방 생성</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        생성할 채팅방의 닉네임을 입력해주세요.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="닉네임"
                        type="text"
                        fullWidth
                        variant="standard"
                        value={newNickName}
                        onChange={(e) => setNewNickName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>취소</Button>
                    <Button onClick={handleCreateChatRoom}>생성</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default ChatRoomList;
